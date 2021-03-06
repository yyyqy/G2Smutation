package org.cbioportal.g2smutation.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.models.AllMutationRecord;
import org.cbioportal.g2smutation.util.models.RSMutationRecord;
import org.cbioportal.g2smutation.util.models.SNPAnnotationType;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * SQL Insert statements Generation from API TODO: Do this again with storing
 * into mysql database and we need reconstruct
 * 
 * @author Juexin Wang
 *
 */
public class PdbScriptsPipelineApiToSQL {
    final static Logger log = Logger.getLogger(PdbScriptsPipelineApiToSQL.class);

    /**
     * Obsolete! Paired with function generateRsSQLfile()
     * 
     * @param filename
     * @return
     */
    public List<String> getDbSNPIdFromMappingFile(String filename) {
        List<String> snpIds = new ArrayList<>();
        try {
            // open "SNP3D_PDB_GRCH37" file
            String SNPfilepwd = new String(filename);
            File SNPfile = new File(SNPfilepwd);
            List<String> SNPfilelines = FileUtils.readLines(SNPfile, StandardCharsets.UTF_8.name());
            int SNPNum = SNPfilelines.size() - 1;
            for (int i = 1; i <= SNPNum; i++) {
                snpIds.add(SNPfilelines.get(i).split("\\s+")[2]);
            }
            snpIds = PdbScriptsPipelineMakeSQL.removeStringListDupli(snpIds);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return snpIds;

    }

    /**
     * Obsolete! Paired with function generateRsSQLfile() Will not use anymore
     * for it is slow for all mapping Call millions of API from genomenexus, not
     * use it anymore
     * 
     * @param urlName
     * @param bufferLines
     * @param snpId
     * @return
     */
    public List<String> callUrl(String urlName, List<String> bufferLines, String snpId) {
        try {
            URL url = new URL(urlName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            String HMkey;
            HashMap<String, String> lineHM = new HashMap<>();
            RSMutationRecord rmr = new RSMutationRecord();
            if ((s = reader.readLine()) != null) {
                JSONArray jsonarray = JSONArray.fromObject(s);
                for (int i = 0; i < jsonarray.size(); i++) {
                    JSONObject jsonobj = JSONObject.fromObject(jsonarray.get(i));
                    JSONArray residueMapping = JSONArray.fromObject(jsonobj.get("residueMapping"));
                    // log.info(residueMapping);
                    HMkey = jsonobj.get("pdbNo").toString() + jsonobj.get("pdbSeg").toString();
                    if (lineHM.containsKey(HMkey)) {
                        continue;
                    } else {
                        lineHM.put(HMkey, "");
                        if (!residueMapping.isEmpty()) {
                            JSONObject residueobj = JSONObject.fromObject(residueMapping.get(0));
                            rmr.setSeqResidueIndex(Integer.parseInt(residueobj.get("queryPosition").toString()));
                            rmr.setSeqResidueName(residueobj.get("queryAminoAcid").toString());
                            rmr.setPdbResidueIndex(Integer.parseInt(residueobj.get("pdbPosition").toString()));
                            rmr.setPdbResidueName(residueobj.get("pdbAminoAcid").toString());
                        } else {
                            rmr.setSeqResidueIndex(-1);
                            rmr.setSeqResidueName("");
                            rmr.setPdbResidueIndex(-1);
                            rmr.setPdbResidueName("");
                        }
                        rmr.setRs_mutationId(Integer.parseInt(snpId));
                        rmr.setSeqId(Integer.parseInt(jsonobj.get("seqId").toString()));
                        rmr.setPdbNo(jsonobj.get("pdbNo").toString());
                        rmr.setAlignmentId(Integer.parseInt(jsonobj.get("alignmentId").toString()));
                        bufferLines.add(makeTable_rs_mutation_insert(rmr));
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferLines;
    }

    /**
     * Obsolete! Will not use anymore for it is slow for all mapping Call
     * millions of API from genomenexus, not use it anymore
     * 
     * @return
     */
    public int generateRsSQLfile() {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        String rssqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(rssqlfilepwd);
        List<String> snpIds = getDbSNPIdFromMappingFile(ReadConfig.workspace + ReadConfig.dbsnpFile);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");
        log.info("Begin to generate sql file");
        log.info("Total rsSNP is:" + snpIds.size());
        for (int j = 0; j < snpIds.size(); j++) { // 20
            if (j % 1000 == 0) {
                log.info("Now start working on " + j + "th SNP");
            }
            String snpId = snpIds.get(j);
            // snpId = "1800369";
            String url = ReadConfig.getGnApiDbsnpInnerUrl();
            url = url.replace("DBSNPID", snpId);
            // log.info(j + "th URL:" + url);
            // if (j % sql_insert_output_interval != 0 || j ==0) {
            if (j % sql_insert_output_interval != sql_insert_output_interval - 1) {
                tempLines = new ArrayList<String>();
                // original use .clear() but may be better to directly allocate
                // new instead
                // tempLines.clear();
                tempLines = callUrl(url, tempLines, snpId);
                outputLines.addAll(tempLines);
            } else {
                outputLines.add("commit;");
                try {
                    FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
                } catch (IOException e) {
                    log.info("input " + rssqlfilepwd + " failed");
                }
                log.info("Finished generating the " + fileCount + "th SQL file");
                fileCount++;
                rssqlfilepwd = ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount;
                rssqlfile = new File(rssqlfilepwd);
                outputLines = new ArrayList<String>();
                outputLines.add("SET autocommit = 0;");
                outputLines.add("start transaction;");
                tempLines = new ArrayList<String>();
                // original use .clear() but may be better to directly allocate
                // new instead
                // tempLines.clear();
                tempLines = callUrl(url, tempLines, snpId);
                outputLines.addAll(tempLines);
            }
        }
        outputLines.add("commit;");
        try {
            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
        } catch (IOException e) {
            log.info("input " + rssqlfilepwd + " failed");
        }
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("insert rssql successful!");
        return fileCount;
    }

    /**
     * Old, can be deleted, used for only mapping dbSNP SNPs paried with
     * generateRsSQLfile()
     * 
     * @param rmr
     * @return
     */
    public String makeTable_rs_mutation_insert(RSMutationRecord rmr) {
        String str = "INSERT INTO `rs_mutation_entry` (`RS_SNP_ID`,`SEQ_ID`,`SEQ_INDEX`,`SEQ_RESIDUE`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,`ALIGNMENT_ID`)VALUES ("
                + rmr.getRs_mutationId() + "," + rmr.getSeqId() + "," + rmr.getSeqResidueIndex() + ",'"
                + rmr.getSeqResidueName() + "','" + rmr.getPdbNo() + "'," + rmr.getPdbResidueIndex() + ",'"
                + rmr.getPdbResidueName() + "'," + rmr.getAlignmentId() + ");\n";
        return str;
        // String str = "INSERT INTO `mutation_entry` VALUES ("
        // + rmr.getRs_mutationId() + ","+ rmr.getSeqId() + ",'" +
        // rmr.getPdbNo() + "',"
        // + rmr.getPdbResidueIndex() + ",'" + rmr.getPdbResidueName() + "'," +
        // rmr.getAlignmentId() + ");\n";
        // return str;
    }

    /**
     * Call URL using chr_posstart
     * 
     * @param urlName
     * @param bufferLines
     * @param gpos
     * @param annotationTypeIds
     * @return
     */
    public List<String> callGposUrl(String urlName, List<String> bufferLines, String gpos, String mutation_NO) {
        try {
            URL url = new URL(urlName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            String HMkey;
            HashMap<String, String> lineHM = new HashMap<>();
            AllMutationRecord armr = new AllMutationRecord();
            if ((s = reader.readLine()) != null) {
                JSONArray jsonarray = JSONArray.fromObject(s);
                for (int i = 0; i < jsonarray.size(); i++) {
                    JSONObject jsonobj = JSONObject.fromObject(jsonarray.get(i));
                    JSONArray residueMapping = JSONArray.fromObject(jsonobj.get("residueMapping"));
                    // log.info(residueMapping);
                    HMkey = jsonobj.get("pdbNo").toString() + jsonobj.get("pdbSeg").toString();
                    if (lineHM.containsKey(HMkey)) {
                        continue;
                    } else {
                        lineHM.put(HMkey, "");
                        if (!residueMapping.isEmpty()) {
                            JSONObject residueobj = JSONObject.fromObject(residueMapping.get(0));
                            armr.setSeqResidueIndex(Integer.parseInt(residueobj.get("queryPosition").toString()));
                            armr.setSeqResidueName(residueobj.get("queryAminoAcid").toString());
                            armr.setPdbResidueIndex(Integer.parseInt(residueobj.get("pdbPosition").toString()));
                            armr.setPdbResidueName(residueobj.get("pdbAminoAcid").toString());
                        } else {
                            armr.setSeqResidueIndex(-1);
                            armr.setSeqResidueName("");
                            armr.setPdbResidueIndex(-1);
                            armr.setPdbResidueName("");
                        }
                        armr.setChr_pos(gpos);
                        armr.setSeqId(Integer.parseInt(jsonobj.get("seqId").toString()));
                        armr.setPdbNo(jsonobj.get("pdbNo").toString());
                        armr.setAlignmentId(Integer.parseInt(jsonobj.get("alignmentId").toString()));
                        bufferLines.add(makeTable_all_mutation_insert(armr));
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferLines;
    }

    /**
     * Use POST to call all snps: input chr and pos, get inner seqId and protein
     * index
     * 
     * @param gpos2proHm
     *            <chr_pos,seq_index>
     * @param currentDir
     * @return
     */
    public int generateAllMappingSQLfileHuge(Map gpos2proHm, String currentDir) {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        
        String allsqlfilepwd = new String(currentDir + ReadConfig.gposAlignSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(allsqlfilepwd);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");
        log.info("Begin to generate sql file");
        log.info("Total gpos number: " + gpos2proHm.size());

        int count = 0;
        Iterator it = gpos2proHm.entrySet().iterator();
        // for (String gpos : gpos2proHm.keySet()) {
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String gpos = (String) pair.getKey();
            HashSet<String> tmpSet = (HashSet<String>) gpos2proHm.get(gpos);

            for (String mutation_NO : tmpSet) {
                if (count % 10000 == 0) {
                    log.info("Now start working on " + count + "th SNP");
                }

                String seqId = mutation_NO.split("_")[0];
                String pos = mutation_NO.split("_")[1];
                String url = ReadConfig.getGnApiAlignInnerGposUrl();
                url = url.replace("SEQID", seqId);
                url = url.replace("PROTEININDEX", pos);

                if (count % sql_insert_output_interval != sql_insert_output_interval - 1) {
                    tempLines = new ArrayList<String>();
                    // original use .clear() but may be better to directly
                    // allocate
                    // new instead
                    // tempLines.clear();
                    tempLines = callGposUrl(url, tempLines, gpos, mutation_NO);
                    outputLines.addAll(tempLines);
                } else {
                    outputLines.add("commit;");
                    try {
                        FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
                    } catch (IOException e) {
                        log.info("input " + allsqlfilepwd + " failed");
                    }
                    log.info("Finished generating the " + fileCount + "th SQL file");
                    fileCount++;
                    allsqlfilepwd = currentDir + ReadConfig.gposAlignSqlInsertFile + "." + fileCount;
                    rssqlfile = new File(allsqlfilepwd);
                    outputLines = new ArrayList<String>();
                    outputLines.add("SET autocommit = 0;");
                    outputLines.add("start transaction;");
                    tempLines = new ArrayList<String>();
                    // original use .clear() but may be better to directly
                    // allocate
                    // new instead
                    // tempLines.clear();
                    tempLines = callGposUrl(url, tempLines, gpos, mutation_NO);
                    outputLines.addAll(tempLines);
                }
                count++;
            }
        }
        outputLines.add("commit;");
        try {
            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
        } catch (IOException e) {
            log.info("input " + allsqlfilepwd + " failed");
        }
        log.info("Total mapping protein_index from SNP is:" + count);
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("Insert allmapping sql successful!");
        return fileCount + 1;
    }

    /**
     * Old, can be deleted. It works for original usage for dbSNP mapping Call
     * all mapping
     * 
     * @param inputHm
     *            key: chr_posstart_end value: dbsnp:123;clinvar:321;...
     * @return
     */
    public int generateAllMappingSQLfile(HashMap<String, String> inputHm) {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        String allsqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(allsqlfilepwd);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");
        log.info("Begin to generate sql file");
        log.info("Total gpos number: " + inputHm.size());

        int count = 0;
        for (String gpos : inputHm.keySet()) {
            String annotationTypeIds = inputHm.get(gpos);
            if (count % 1000 == 0) {
                log.info("Now start working on " + count + "th SNP");
            }
            String chr = gpos.split("_")[0];
            String pos = gpos.split("_")[1];
            String url = ReadConfig.getGnApiDbsnpInnerGposUrl();
            url = url.replace("CHROMSOME", chr);
            url = url.replace("LOCATION", pos);

            if (count % sql_insert_output_interval != sql_insert_output_interval - 1) {
                tempLines = new ArrayList<String>();
                // original use .clear() but may be better to directly allocate
                // new instead
                // tempLines.clear();
                tempLines = callGposUrl(url, tempLines, gpos, annotationTypeIds);
                outputLines.addAll(tempLines);
            } else {
                outputLines.add("commit;");
                try {
                    FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
                } catch (IOException e) {
                    log.info("input " + allsqlfilepwd + " failed");
                }
                log.info("Finished generating the " + fileCount + "th SQL file");
                fileCount++;
                allsqlfilepwd = ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount;
                rssqlfile = new File(allsqlfilepwd);
                outputLines = new ArrayList<String>();
                outputLines.add("SET autocommit = 0;");
                outputLines.add("start transaction;");
                tempLines = new ArrayList<String>();
                // original use .clear() but may be better to directly allocate
                // new instead
                // tempLines.clear();
                tempLines = callGposUrl(url, tempLines, gpos, annotationTypeIds);
                outputLines.addAll(tempLines);
            }
            count++;
        }
        log.info("Total mapping SNP is:" + count);
        outputLines.add("commit;");
        try {
            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
        } catch (IOException e) {
            log.info("input " + allsqlfilepwd + " failed");
        }
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("Insert allmapping sql successful!");
        return fileCount;
    }

    /**
     * Generate sql for allmapping_mutation_entry table
     * 
     * @param armr
     * @return
     */
    public String makeTable_all_mutation_insert(AllMutationRecord armr) {
        String str = "INSERT INTO `gpos_allmapping_pdb_entry` (`CHR_POS`,`SEQ_ID`,`SEQ_INDEX`,`SEQ_RESIDUE`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,`ALIGNMENT_ID`)VALUES ('"
                + armr.getChr_pos() + "'," + armr.getSeqId() + "," + armr.getSeqResidueIndex() + ",'"
                + armr.getSeqResidueName() + "','" + armr.getPdbNo() + "'," + armr.getPdbResidueIndex() + ",'"
                + armr.getPdbResidueName() + "'," + armr.getAlignmentId() + ");\n";
        return str;
    }

    /**
     * Generate gpos_allmapping_entry tables sql file
     * 
     * @param chr_pos
     * @param annotationTypeIds
     * @return
     */
    public String makeTable_gpos_allmapping_insert(String chr_pos, String annotationTypeIds) {
        HashMap<SNPAnnotationType, HashMap<String, String>> hm = new HashMap<>();
        for (SNPAnnotationType type : SNPAnnotationType.values()) {
            hm.put(type, new HashMap<String, String>());
        }
        if (annotationTypeIds.contains(";")) {
            String[] strArray = annotationTypeIds.split(";");
            for (String str : strArray) {
                String typestr = str.split(":")[0];
                String idstr = str.split(":")[1];
                HashMap<String, String> tmpHm = hm.get(SNPAnnotationType.valueOf(typestr));
                tmpHm.put(idstr, "");
                hm.put(SNPAnnotationType.valueOf(typestr), tmpHm);
            }
        } else {
            String typestr = annotationTypeIds.split(":")[0];
            String idstr = annotationTypeIds.split(":")[1];
            HashMap<String, String> tmpHm = hm.get(SNPAnnotationType.valueOf(typestr));
            tmpHm.put(idstr, "");
            hm.put(SNPAnnotationType.valueOf(typestr), tmpHm);
        }
        // String str = "INSERT INTO `gpos_allmapping_entry`
        // (`CHR_POS`,`DBSNP_ID`,`CLINVAR_ID`,`COSMIC_ID`,`GENIE_ID`,`TCGA_ID`)VALUES
        // ("
        // + chr_pos + "," + armr.getChr_pos() + ");\n";
        String str = "INSERT INTO `gpos_allmapping_entry` (`CHR_POS`";
        for (SNPAnnotationType type : SNPAnnotationType.values()) {
            str = str + ",`" + type.toString() + "_ID`";
        }
        str = str + ")VALUES('" + chr_pos + "'";
        String outputstr = "";
        String individualStr = str;
        int count = 0;
        for (SNPAnnotationType type : SNPAnnotationType.values()) {           
            for (String cStr : hm.get(type).keySet()) {
                String afterstr = individualStr + ",'" + cStr + "'";
                for (int i=count+1; i<SNPAnnotationType.values().length;i++){
                    afterstr = afterstr + ",''";
                }
                afterstr = afterstr + ");\n";
                outputstr = outputstr + afterstr;               
            }
            individualStr = individualStr + ",''";
            count++;
        }       
        return outputstr;
    }

    /**
     * generate insert sentences of table gpos_protein_entry
     * 
     * @param gpos
     * @param mutation_NO
     * @return
     */
    public String makeTable_gpos_protein_insert(String gpos, String mutation_NO) {
        String seqId = mutation_NO.split("_")[0];
        String proteinIndex = mutation_NO.split("_")[1];
        String str = "INSERT INTO `gpos_protein_entry` (`CHR_POS`,`MUTATION_NO`,`SEQ_ID`,`SEQ_INDEX`)VALUES ('" + gpos
                + "','" + mutation_NO + "','" + seqId + "','" + proteinIndex + "');\n";
        return str;
    }

    /**
     * Generate gpos_allmapping_entry tables
     * 
     * @param inputHm
     *            key: chr_posstart_end value: dbsnp:123;clinvar:321;...
     * @return
     */
    public int generateGposAllMappingSQLfile(HashMap<String, String> inputHm) {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        String allsqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(allsqlfilepwd);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");
        log.info("Begin to generate gpos_allmapping file");
        log.info("Total gpos number: " + inputHm.size());

        int count = 0;
        for (String gpos : inputHm.keySet()) {
            String annotationTypeIds = inputHm.get(gpos);
            if (count % 100000 == 0) {
                log.info("Now start working on " + count + "th SNP");
            }

            if (count % sql_insert_output_interval != sql_insert_output_interval - 1) {
                tempLines = new ArrayList<String>();
                tempLines.add(makeTable_gpos_allmapping_insert(gpos, annotationTypeIds));
                outputLines.addAll(tempLines);
            } else {
                outputLines.add("commit;");
                try {
                    FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
                } catch (IOException e) {
                    log.info("input " + allsqlfilepwd + " failed");
                }
                log.info("Finished generating the " + fileCount + "th SQL file");
                fileCount++;
                allsqlfilepwd = ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount;
                rssqlfile = new File(allsqlfilepwd);
                outputLines = new ArrayList<String>();
                outputLines.add("SET autocommit = 0;");
                outputLines.add("start transaction;");
                tempLines = new ArrayList<String>();
                tempLines.add(makeTable_gpos_allmapping_insert(gpos, annotationTypeIds));
                outputLines.addAll(tempLines);
            }
            count++;
        }

        outputLines.add("commit;");
        try {
            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
        } catch (IOException e) {
            log.info("input " + allsqlfilepwd + " failed");
        }
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("Total mapping SNP is:" + count);
        log.info("Insert allmapping sql successful!");
        return fileCount + 1;
    }

    /**
     * Generate sql file table gpos_protein_entry
     * 
     * @param gpos2proHm
     * @return
     */
    public int generateGposProteinSQLfile(Map gpos2proHm) {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        String allsqlfilepwd = new String(ReadConfig.workspace + ReadConfig.gposSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(allsqlfilepwd);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");
        log.info("Begin to generate gpos_protein file");
        log.info("Total gpos number has protein loc: " + gpos2proHm.size());

        int count = 0;
        Iterator it = gpos2proHm.entrySet().iterator();
        // for (String gpos : gpos2proHm.keySet()) {
        while (it.hasNext()) {
            Map.Entry<String, HashSet<String>> pair = (Map.Entry) it.next();
            String gpos = pair.getKey();
            HashSet<String> tmpSet = (HashSet<String>) gpos2proHm.get(gpos);
            for (String mutation_NO : tmpSet) {
                if (count % 100000 == 0) {
                    log.info("Now start working on " + count + "th SNP");
                }

                if (count % sql_insert_output_interval != sql_insert_output_interval - 1) {
                    tempLines = new ArrayList<String>();
                    tempLines.add(makeTable_gpos_protein_insert(gpos, mutation_NO));
                    outputLines.addAll(tempLines);
                } else {
                    outputLines.add("commit;");
                    try {
                        FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
                    } catch (IOException e) {
                        log.info("input " + allsqlfilepwd + " failed");
                    }
                    log.info("Finished generating the " + fileCount + "th SQL file");
                    fileCount++;
                    allsqlfilepwd = ReadConfig.workspace + ReadConfig.gposSqlInsertFile + "." + fileCount;
                    rssqlfile = new File(allsqlfilepwd);
                    outputLines = new ArrayList<String>();
                    outputLines.add("SET autocommit = 0;");
                    outputLines.add("start transaction;");
                    tempLines = new ArrayList<String>();
                    tempLines.add(makeTable_gpos_protein_insert(gpos, mutation_NO));
                    outputLines.addAll(tempLines);
                }
            }

            count++;
        }

        outputLines.add("commit;");
        try {
            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
        } catch (IOException e) {
            log.info("input " + allsqlfilepwd + " failed");
        }
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("Total mapping SNP to protein is:" + count);
        log.info("Insert allmapping sql successful!");
        return fileCount + 1;
    }

    synchronized boolean isFinished(StringBuffer sb, int count) {
        if (sb.toString().length() > count || count == 0) {
            return true;
        }
        return false;
    }

}
