package org.cbioportal.G2Smutation.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.cbioportal.G2Smutation.util.APICallingAgent;
import org.cbioportal.G2Smutation.util.APICallingThread;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.util.models.RSMutationRecord;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * SQL Insert statements Generation from API
 * TODO: Do this again with storing into mysql database and we need reconstruct
 * 
 * @author Juexin Wang
 *
 */
public class PdbScriptsPipelineApiToSQL {
    final static Logger log = Logger.getLogger(PdbScriptsPipelineMakeSQL.class);

    /**
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
     * Obsolete! Will not use anymore for it is slow for all mapping
     * Call millions of API from genomenexus, not use it anymore
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
     * Multiple thread version of generateRsSQL
     * 
     * @return
     */
    public int generateRsSQLfileMT() {
        List<String> snpIds = getDbSNPIdFromMappingFile(ReadConfig.workspace + ReadConfig.dbsnpFile);

        log.info("Begin to generate sql file");
        log.info("Total rsSNP has:" + snpIds.size() + " Entries");
        log.info("Start multiple threads");

        ArrayList<APICallingThread> threadsArr = new ArrayList<APICallingThread>();
        APICallingAgent PD = new APICallingAgent();

        try {
            for (int i = 0; i < 20; i++) {
                APICallingThread T1 = new APICallingThread("Thread - " + i, PD, snpIds, i);
                T1.start();
                threadsArr.add(T1);
            }
            for (int i = 0; i < 20; i++) {
                threadsArr.get(i).join();

            }
        } catch (Exception ex) {
            log.info("Thread Interrupted");
            System.out.println(ex);
        }
        log.info("Muliple Thread Finished!");

        int filenumber = 0;
        try {
            File folder = new File(ReadConfig.workspace);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().startsWith(ReadConfig.rsSqlInsertFile)) {
                        int num = Integer
                                .parseInt(listOfFiles[i].getName().split(ReadConfig.rsSqlInsertFile)[1].split("\\.")[1]
                                        .toString());
                        if (num > filenumber) {
                            filenumber = num;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("Total got filenumber " + filenumber);

        return filenumber;
    }

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

    synchronized boolean isFinished(StringBuffer sb, int count) {
        if (sb.toString().length() > count || count == 0) {
            return true;
        }
        return false;
    }

}
