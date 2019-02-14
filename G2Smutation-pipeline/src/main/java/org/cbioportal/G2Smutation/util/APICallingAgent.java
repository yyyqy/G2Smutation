package org.cbioportal.G2Smutation.util;

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
import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.util.models.RSMutationRecord;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Call API
 * 
 * @author wangjue
 *
 */
public class APICallingAgent {
    final static Logger log = Logger.getLogger(APICallingAgent.class);

    public void work(List<String> snpIds, int agentNo) {
        List<String> tempLines = new ArrayList<String>();
        int fileCount = 0;
        String rssqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
        File rssqlfile = new File(rssqlfilepwd);
        int sql_insert_output_interval = Integer.parseInt(ReadConfig.sqlInsertOutputInterval);

        List<String> outputLines = new ArrayList<String>();
        outputLines.add("SET autocommit = 0;");
        outputLines.add("start transaction;");

        for (int i = 0; i < snpIds.size(); i++) { // 20
            if (i % 20 == agentNo) {
                if(i%1000==0){
                    log.info("Now start working on "+i+"th SNP of "+agentNo+"th thread");
                }
                String snpId = snpIds.get(i);
                //System.out.println(i);
                String url = ReadConfig.getGnApiDbsnpInnerUrl();
                url = url.replace("DBSNPID", snpId);
                if (i % sql_insert_output_interval != sql_insert_output_interval - 1) {
                    tempLines = new ArrayList<String>();
                    tempLines = callUrlMT(url, tempLines, snpId);
                    outputLines.addAll(tempLines);
                } else {
                    outputLines.add("commit;");
                    try {
                        synchronized (this) {
                            FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines, true);
                        }
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
                    tempLines = callUrlMT(url, tempLines, snpId);
                    outputLines.addAll(tempLines);
                }
            }
        }
        outputLines.add("commit;");
        try {
            synchronized (this) {
                FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines, true);
            }
        } catch (IOException e) {
            log.info("input " + rssqlfilepwd + " failed");
        }
        log.info("Finished generating the " + fileCount + "th SQL file");
        log.info("insert rssql successful!");
    }

    synchronized List<String> callUrlMT(String urlName, List<String> bufferLines, String snpId) {
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
            System.out.println("Thread  interrupted.");
        }
        return bufferLines;
    }

    synchronized public String makeTable_rs_mutation_insert(RSMutationRecord rmr) {
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

}
