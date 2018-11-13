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
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.util.models.RSMutationRecord;
import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class PdbScriptsPipelineApiToSQL {
	final static Logger log = Logger.getLogger(PdbScriptsPipelineMakeSQL.class);
	String hostName = "https://g2s.genomenexus.org/api/";
	String apiName = "alignments";
	String varType = "dbsnp";
	String varName = "rs";
	String varId;// = "550710011";//"1800369";
	String apiType = "residueMapping";
	
	private String sqlInsertOutputInterval;
	
	JSONArray jsonarray;
    JSONObject jsonobj;
    JSONArray residueMapping;
    JSONObject residueobj;
	
	List<String> outputLines = new ArrayList<String>();
	
	String urlName;// = hostName+apiName+"/"+varType+"/"+varName+varId+"/"+apiType;
	RSMutationRecord rmr = new RSMutationRecord();

	
	public List<String> getVarId(List<String> SNPid) {
		try {
    		// open "SNP3D_PDB_GRCH37" file
    		String SNPfilepwd = new String(ReadConfig.workspace + "SNP3D_PDB_GRCH37");
    		File SNPfile = new File(SNPfilepwd);
    		List<String> SNPfilelines = FileUtils.readLines(SNPfile, StandardCharsets.UTF_8.name());
    		int SNPNum = SNPfilelines.size()-1;
    		for (int i = 1; i <= SNPNum; i++) {
    			SNPid.add(SNPfilelines.get(i).split("\\s+")[2]);
    		}
    		SNPid = PdbScriptsPipelineMakeSQL.removeStringListDupli(SNPid);
			} catch (Exception ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}
		return SNPid;
		
	}
	
	public List<String> callUrl(String urlName, List<String> tempLines){
		String rmrLine = null;
		try {
			URL url = new URL(urlName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String s;
			String HMkey;
			HashMap<String, String> lineHM = new HashMap<String, String>();
			if ((s = reader.readLine()) != null) {
				jsonarray = JSONArray.fromObject(s);
				for (int i = 0; i < jsonarray.size(); i++) {
					jsonobj = JSONObject.fromObject(jsonarray.get(i));
					residueMapping = JSONArray.fromObject(jsonobj.get("residueMapping"));
					//log.info(residueMapping);
					HMkey = jsonobj.get("pdbNo").toString() + jsonobj.get("pdbSeg").toString();
					if(lineHM.containsKey(HMkey)) {
						continue;
					}
					else {
						lineHM.put(HMkey, "");
						if(!residueMapping.isEmpty()) {
							residueobj = JSONObject.fromObject(residueMapping.get(0));
							rmr.setRs_mutationId(Integer.parseInt(varId));
							rmr.setSeqId(Integer.parseInt(jsonobj.get("seqId").toString()));
							rmr.setPdbNo(jsonobj.get("pdbNo").toString());
							rmr.setPdbResidueIndex(Integer.parseInt(residueobj.get("pdbPosition").toString()));
							rmr.setPdbResidueName(residueobj.get("pdbAminoAcid").toString());
							rmr.setAlignmentId(Integer.parseInt(jsonobj.get("alignmentId").toString()));
							rmrLine = makeTable_rs_mutation_insert(rmr);
							tempLines.add(rmrLine);
						}
						else {
							rmr.setRs_mutationId(Integer.parseInt(varId));
							rmr.setSeqId(Integer.parseInt(jsonobj.get("seqId").toString()));
							rmr.setPdbNo(jsonobj.get("pdbNo").toString());
							rmr.setPdbResidueIndex(-1);
							rmr.setPdbResidueName("");
							rmr.setAlignmentId(Integer.parseInt(jsonobj.get("alignmentId").toString()));
							rmrLine = makeTable_rs_mutation_insert(rmr);
							tempLines.add(rmrLine);
						}
					}
				}
			}
				reader.close();
		} catch(IOException ex) {
			log.info(ex);;
			}
		return tempLines;
	}
	
	public int generateRsSQLfile() {
		List<String> SNPid = new ArrayList<String>();
		List<String> tempLines = new ArrayList<String>();
		int fileCount = 0;
		//String rssqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
		String rssqlfilepwd = new String(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + fileCount);
		File rssqlfile = new File(rssqlfilepwd);
		SNPid = getVarId(SNPid);
		this.sqlInsertOutputInterval = ReadConfig.sqlInsertOutputInterval;
		int sql_insert_output_interval = Integer.parseInt(this.sqlInsertOutputInterval);
		outputLines.add("SET autocommit = 0;");
		outputLines.add("start transaction;");
		log.info("Begin to generate sql file");
		for (int j = 0; j < SNPid.size(); j++) {	//20
			varId = SNPid.get(j);
			//varId = "1800369";
			urlName = hostName+apiName+"/"+varType+"/"+varName+varId+"/"+apiType;
			if (j % sql_insert_output_interval != 0 || j ==0) {
				tempLines.clear();
				tempLines = callUrl(urlName, tempLines);
				outputLines.addAll(tempLines);
			}
			else {
				outputLines.add("commit;");
				try {
					FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
				} catch (IOException e) {
					log.info("input " + rssqlfilepwd + " failed");
				}
				log.info("Finish the " + fileCount +" file");
				fileCount++;
				rssqlfilepwd = new String(ReadConfig.workspace + "rsInsert.sql" + "." + fileCount);
				rssqlfile = new File(rssqlfilepwd);
				outputLines.clear();
				outputLines.add("SET autocommit = 0;");
				outputLines.add("start transaction;");
				tempLines.clear();
				tempLines = callUrl(urlName, tempLines);
				outputLines.addAll(tempLines);
			}
		}
		outputLines.add("commit;");
		try {
			FileUtils.writeLines(rssqlfile, StandardCharsets.UTF_8.name(), outputLines);
		} catch (IOException e) {
			log.info("input " + rssqlfilepwd + " failed");
		}
		log.info("Finish the " + fileCount +" file");
		log.info("insert rssql successful!");
		return fileCount;
	}

	public String makeTable_rs_mutation_insert(RSMutationRecord rmr) {
        String str = "INSERT INTO `rs_mutation_entry` (`RS_MUTATION_ID`,`SEQ_ID`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,`ALIGNMENT_ID`)VALUES ('"
                + rmr.getRs_mutationId() + "',"+ rmr.getSeqId() + ",'" + rmr.getPdbNo() + "',"
                + rmr.getPdbResidueIndex() + ",'" + rmr.getPdbResidueName() + "'," + rmr.getAlignmentId() + ");\n";
        return str;
//		String str = "INSERT INTO `mutation_entry` VALUES ("
//                + rmr.getRs_mutationId() + ","+ rmr.getSeqId() + ",'" + rmr.getPdbNo() + "',"
//                + rmr.getPdbResidueIndex() + ",'" + rmr.getPdbResidueName() + "'," + rmr.getAlignmentId() + ");\n";
//        return str;
    }
	
}
