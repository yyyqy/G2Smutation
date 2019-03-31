package org.cbioportal.G2Smutation.annotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.cbioportal.G2Smutation.util.CommandProcessUtil;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.util.models.MutationUsageRecord;
import org.cbioportal.G2Smutation.util.models.StructureAnnotationRecord;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Structure Annotation as a class
 * 
 * @author Qinyuan Yang, Juexin Wang
 *
 */
public class StructureAnnotation {
	final static Logger log = Logger.getLogger(StructureAnnotation.class);
	
    /**
     * generate structure annotation for strucure_annotation_entry
     * 
     * @param MutationUsageRecord
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4StructureAnnotationEntry(MutationUsageRecord mUsageRecord, String outputFilename) {
    	HashMap<Integer, String> mutationIdHm = mUsageRecord.getMutationIdHm();
    	HashMap<Integer, String> residueHm = mUsageRecord.getResidueHm();
    	try {
            List<String> outputlist = new ArrayList<String>();
            StructureAnnotationRecord sar = new StructureAnnotationRecord();
            String strNaccess = null;
            String strDSSP = null;
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            int count = 0;
            for(int mutationId : mutationIdHm.keySet()) {            	
            	sar.setChrPos(mutationIdHm.get(mutationId));
            	sar.setMutationId(mutationId);
            	sar.setPdbNo(residueHm.get(mutationId));//?
            	sar.setPdbResidueIndex(Integer.parseInt(residueHm.get(mutationId).split("_")[2]));    
            	//log.info(residueHm.get(mutationId).split("_")[0]+residueHm.get(mutationId).split("_")[1]+residueHm.get(mutationId).split("_")[2]);
            	strNaccess = getNaccessInfo(residueHm.get(mutationId).split("_")[0],residueHm.get(mutationId).split("_")[1],residueHm.get(mutationId).split("_")[2]);
            	sar.setBuried(Integer.parseInt(strNaccess.substring(86,87)));
            	sar.setAllAtomsABS(Float.parseFloat(strNaccess.substring(16,22)));
            	sar.setAllAtomsREL(Float.parseFloat(strNaccess.substring(23,28)));
            	sar.setTotalSideABS(Float.parseFloat(strNaccess.substring(29,35)));
            	sar.setTotalSideREL(Float.parseFloat(strNaccess.substring(36,41)));
            	sar.setMainChainABS(Float.parseFloat(strNaccess.substring(42,48)));
            	sar.setMainChainREL(Float.parseFloat(strNaccess.substring(49,54)));
            	sar.setNonPolarABS(Float.parseFloat(strNaccess.substring(55,61)));
            	sar.setNonPolarREL(Float.parseFloat(strNaccess.substring(62,67)));
            	sar.setAllPolarABS(Float.parseFloat(strNaccess.substring(68,74)));
            	sar.setAllPolarREL(Float.parseFloat(strNaccess.substring(75,80)));
            	strDSSP = getDSSPInfo(residueHm.get(mutationId).split("_")[0],residueHm.get(mutationId).split("_")[1],residueHm.get(mutationId).split("_")[2]);
            	sar.setPdbResidueName(strDSSP.charAt(13));
            	sar.setSecStructure(strDSSP.charAt(16));
            	sar.setThreeTurnHelix(strDSSP.charAt(18));
            	sar.setFourTurnHelix(strDSSP.charAt(19));
            	sar.setFiveTurnHelix(strDSSP.charAt(20));
            	sar.setGeometricalBend(strDSSP.charAt(21));
            	sar.setChirality(strDSSP.charAt(22));
            	sar.setBetaBridgeLabela(strDSSP.charAt(23));
            	sar.setBetaBridgeLabelb(strDSSP.charAt(24));
            	sar.setBpa(stringToInt(strDSSP.substring(26, 29)));
            	sar.setBpb(stringToInt(strDSSP.substring(30, 33)));
            	sar.setBetaSheetLabel(strDSSP.charAt(33));
            	sar.setAcc(stringToInt(strDSSP.substring(35,38)));
            	getHETInfo(sar,residueHm.get(mutationId).split("_")[0],residueHm.get(mutationId).split("_")[1],residueHm.get(mutationId).split("_")[2]);
            	getDomainsUrl(sar,residueHm.get(mutationId).split("_")[0],residueHm.get(mutationId).split("_")[1],residueHm.get(mutationId).split("_")[2]);
            	outputlist.add(makeTable_structureAnnotation_insert(sar));           	
            	if(count%1000==0) { 
            		log.info("Processing "+count+"th");
            	} 
            	count++; 
            }         
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);
    	}catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Generating entry for table structure_annotation_entry
     * @param sar
     * @return
     */
    public String makeTable_structureAnnotation_insert(StructureAnnotationRecord sar) {
    	String str = "INSERT INTO `structure_annotation_entry` (`CHR_POS`,`MUTATION_ID`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,"
    			+ "`BURIED`,`ALL_ATOMS_ABS`,`ALL_ATOMS_REL`,`TOTAL_SIDE_ABS`,`TOTAL_SIDE_REL`,`MAIN_CHAIN_ABS`,`MAIN_CHAIN_REL`,"
    			+ "`NON_POLAR_ABS`,`NON_POLAR_REL`,`ALL_POLAR_ABS`,`ALL_POLAR_REL`,`SEC_STRUCTURE`,`THREE_TURN_HELIX`,"
    			+ "`FOUR_TURN_HELIX`,`FIVE_TURN_HELIX`,`GEOMETRICAL_BEND`,`CHIRALITY`,`BETA_BRIDGE_LABELA`,`BETA_BRIDGE_LABELB`,"
    			+ "`BPA`,`BPB`,`BETA_SHEET_LABEL`,`ACC`,`LIGAND_BINDING_PROTEIN`,`LIGAND_BINDING_DIRECT`,`LIGAND_NAME`,`INTERPRO_ID`,"
    			+ "`INTERPRO_NAME`,`INTERPRO_IDENTIFIER`,`INTERPRO_START`,`INTERPRO_END`,`PFAM_ID`,`PFAM_NAME`,"
    			+ "`PFAM_DESCRIPTION`,`PFAM_IDENTIFIER`,`PFAM_START`,`PFAM_END`,`CATH_ID`,`CATH_ARCHITECTURE`,`CATH_CLASS`,"
    			+ "`CATH_HOMOLOGY`,`CATH_IDENTIFIER`,`CATH_NAME`,`CATH_TOPOLOGY`,`CATH_DOMAIN_ID`,`CATH_DOMAIN_START`,"
    			+ "`CATH_DOMAIN_END`,`SCOP_ID`,`SCOP_DESCRIPTION`,`SCOP_IDENTIFIER`,`SCOP_SCCS`,`SCOP_CLASS_SUNID`,"
    			+ "`SCOP_CLASS_DESCRIPTION`,`SCOP_FOLD_SUNID`,`SCOP_FOLD_DESCRIPTION`,`SCOP_SUPERFAMILY_SUNID`,"
    			+ "`SCOP_SUPERFAMILY_DESCRIPTION`,`SCOP_START`,`SCOP_END`)VALUES ('"
    			+ sar.getChrPos() + "'," + sar.getMutationId() + ",'" + sar.getPdbNo() + "'," + sar.getPdbResidueIndex() + ",'"
    			+ sar.getPdbResidueName() + "'," + sar.getBuried() + "," + sar.getAllAtomsABS() + "," +sar.getAllAtomsREL() + ","
    			+ sar.getTotalSideABS() + "," + sar.getTotalSideREL() + "," + sar.getMainChainABS() + "," +sar.getMainChainREL() + ","
    			+ sar.getNonPolarABS() + "," +sar.getNonPolarREL() + "," + sar.getAllPolarABS() + "," + sar.getAllPolarREL() + ",'"
    			+ sar.getSecStructure() + "','" + sar.getThreeTurnHelix() + "','" + sar.getFourTurnHelix() + "','"
    			+ sar.getFiveTurnHelix() + "','" + sar.getGeometricalBend() + "','" + sar.getChirality() + "','"
    			+ sar.getBetaBridgeLabela() + "','" + sar.getBetaBridgeLabelb() + "','" + sar.getBpa() + "','"
    			+ sar.getBpb() + "','" + sar.getBetaSheetLabel() + "','" + sar.getAcc() + "'," + sar.getLigandBindingProtein() + ","
    			+ sar.getLigandBindingdirect() + ",'" + sar.getLigandName() + "','" + sar.getInterproId() + "','" 
    			+ sar.getInterproName() + "','" + sar.getInterproIdentifier() + "','" + sar.getInterproStart() + "','"
    			+ sar.getInterproEnd() + "','" + sar.getPfamId() + "','" + sar.getPfamName() + "','"
    			+ sar.getPfamDescription() + "','" + sar.getPfamIdentifier() + "','" + sar.getPfamStart() + "',"
    			+ sar.getPfamEnd() + "','" + sar.getCathId() + "','" + sar.getCathArchitecture() + "','"
    			+ sar.getCathClass() + "','" + sar.getCathHomology() + "','" + sar.getCathIdentifier() + "','"
    			+ sar.getCathName() + "','" + sar.getCathTopology() + "','" + sar.getCathDomainId() + "','"
    			+ sar.getCathDomainStart() + "','" + sar.getCathDomainEnd() + "','" + sar.getScopId() + "','"
    			+ sar.getScopDescription() + "','" + sar.getScopIdentifier() + "','" + sar.getScopSccs() + "','"
    			+ sar.getScopClassSunid() + "','" + sar.getScopClassDescription() + "','" + sar.getScopFoldSunid() + "','"
    			+ sar.getScopFoldDescription() + "','" + sar.getScopSuperfamilySunid() + "','" + sar.getScopFoldDescription() + "','"
    			+ sar.getScopStart() + "','" + sar.getScopEnd() + "');\n";
		return str;
    }
    
    public String getDSSPInfo(String pdbId, String pdbChain, String pdbResidueIndex){
    	String resfilepwd = new String(ReadConfig.dsspLocalDataFile + pdbId + ReadConfig.dsspFileSuffix);
		File resfile = new File(resfilepwd);
		String str = null;
		try {
			List<String> lines = FileUtils.readLines(resfile, StandardCharsets.UTF_8.name());
			int i = 0;
			int index = Integer.parseInt(pdbResidueIndex);
			int flag = 0;
			//log.info(index);
			for(;i<lines.size();i++) {
				if(lines.get(i).substring(2, 3).equals("#")) {
					flag = 1;
				}
				if(flag == 0) {
					continue;
				}
				else {
					if(!(lines.get(i).substring(11,12).equals(pdbChain)&&stringToInt(lines.get(i).substring(6,10))==index)) {
						continue;
					}
					else {
						break;
					}
				}
			}
			str = lines.get(i);
			//log.info(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
    }
    
    public String getNaccessInfo(String pdbId, String pdbChain, String pdbResidueIndex){
    	String resfilepwd = new String(ReadConfig.tmpdir + pdbId + ReadConfig.naccessFileSuffix);
		File resfile = new File(resfilepwd);
		String str = null;
		try {
			List<String> lines = FileUtils.readLines(resfile, StandardCharsets.UTF_8.name());
			int i = 0;
			while(!(lines.get(i).substring(0,3).equals("RES")&&lines.get(i).substring(8,9).equals(pdbChain)&&stringToInt(lines.get(i).substring(9,13))==Integer.parseInt(pdbResidueIndex))){
				i++;
			}
			str = lines.get(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
    }
    
    public void getHETInfo(StructureAnnotationRecord sar, String pdbId, String pdbChain, String pdbResidueIndex) {
		try {
			double x1, x2, y1, y2, z1, z2, ra, rl, dis;
			String asafilepwd = new String(ReadConfig.tmpdir + pdbId + ".asa");
			File asafile = new File(asafilepwd);
			List<String> lines = FileUtils.readLines(asafile, StandardCharsets.UTF_8.name());
			int k = 0;
			while(!(lines.get(k).substring(21,22).equals(pdbChain)&&lines.get(k).substring(13,15).equals("CA")&&stringToInt(lines.get(k).substring(22, 26))==Integer.parseInt(pdbResidueIndex))){
				k++;
			}
			x1 = Double.parseDouble(lines.get(k).substring(30, 38));
			y1 = Double.parseDouble(lines.get(k).substring(38, 46));
			z1 = Double.parseDouble(lines.get(k).substring(46, 54));
			ra = Double.parseDouble(lines.get(k).substring(64, 68));
			PDBFileReader reader = new PDBFileReader();
			Structure struc = reader.getStructure(ReadConfig.workspace + pdbId + ".pdb");
			List<Group> hetGroup = new ArrayList<Group>();
			String ligantNames = "";
			hetGroup.addAll(struc.getHetGroups());
			if(hetGroup.isEmpty()) {
				sar.setLigandBindingdirect(0);
				sar.setLigandBindingProtein(0);
				sar.setLigandName(ligantNames);
			}
			else {
				sar.setLigandBindingProtein(1);
				for(int i=0; i<hetGroup.size(); i++) {
					for(int j=0; j< hetGroup.get(i).size(); j++) {
						x2 = hetGroup.get(i).getAtom(j).getX();
						y2 = hetGroup.get(i).getAtom(j).getY();
						z2 = hetGroup.get(i).getAtom(j).getZ();
						rl = getLigantRadius(hetGroup.get(i).getPDBName());
						dis = getDistance(x1,y1,z1,x2,y2,z2,ra,rl);
						if(dis < 0.50) {
							sar.setLigandBindingdirect(1);
							ligantNames = ligantNames + hetGroup.get(i).getPDBName() + "; ";
						}
					}
				}
				sar.setLigandName(ligantNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public double getDistance(double x1, double y1, double z1, double x2, double y2, double z2, double ra, double rl) {
    	double dis=0;
    	dis = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)+(z1-z2)*(z1-z2)) - ra -rl;    	
    	return dis;
    }
    
    public double getLigantRadius(String name) {
    	double rl = 0;
    	switch(name) {
    		case "H":
    			rl = 1.20;
    			break;
    		case "ZN":
    			rl = 1.39;
    			break;
    		case "CU":
    			rl = 1.40;
    			break;
    		case "F":
    			rl = 1.47;
    			break;
    		case "O":
    			rl = 1.52;
    			break;
    		case "N":
    			rl = 1.55;
    			break;
    		case "HG":
    			rl = 1.55;
    			break;
    		case "CD":
    			rl = 1.58;
    			break;
    		case "NI":
    			rl = 1.63;
    			break;
    		case "PD":
    			rl = 1.63;
    			break;
    		case "AU":
    			rl = 1.66;
    			break;
    		case "C":
    			rl = 1.70;
    			break;
    		case "AG":
    			rl = 1.72;
    			break;
    		case "MG":
    			rl = 1.73;
    			break;
    		case "CL":
    			rl = 1.75;
    			break;
    		case "PT":
    			rl = 1.75;
    			break;
    		case "P":
    			rl = 1.80;
    			break;
    		case "S":
    			rl = 1.80;
    			break;
    		case "LI":
    			rl = 1.82;
    			break;
    		case "AS":
    			rl = 1.85;
    			break;
    		case "BR":
    			rl = 1.85;
    			break;
    		case "U":
    			rl = 1.86;
    			break;
    		case "GA":
    			rl = 1.87;
    			break;
    		case "AR":
    			rl = 1.88;
    			break;
    		case "SE":
    			rl = 1.90;
    			break;
    		case "IN":
    			rl = 1.93;
    			break;
    		case "TI":
    			rl = 1.96;
    			break;
    		case "I":
    			rl = 1.98;
    			break;
    		case "KR":
    			rl = 2.02;
    			break;
    		case "PB":
    			rl = 2.02;
    			break;
    		case "TE":
    			rl = 2.06;
    			break;
    		case "SI":
    			rl = 2.10;
    			break;
    		case "XE":
    			rl = 2.16;
    			break;
    		case "SN":
    			rl = 2.17;
    			break;
    		case "NA":
    			rl = 2.27;
    			break;
    		case "K":
    			rl = 2.75;
    			break;
    	}
		return rl;	
    }
    
    public void getDomainsUrl(StructureAnnotationRecord sar, String pdbId, String pdbChain, String pdbResidueIndex) throws Exception {  
    	// get pfam and interpro info from sequence_domains
    	String seUrlName = ReadConfig.getSequenceDomainsUrl() + pdbId;
    	URL seUrl = new URL(seUrlName);
    	int residueIndex = Integer.parseInt(pdbResidueIndex);
    	String pfamIds = "";
    	String pfamNames = "";
    	String pfamDescriptions = "";
    	String pfamIdentifiers = "";
    	String pfamStarts = "";
    	String pfamEnds = "";
    	String s1;
    	BufferedReader reader1 = new BufferedReader(new InputStreamReader(seUrl.openStream()));
    	if ((s1 = reader1.readLine()) != null) {
            JSONObject jso = new JSONObject(s1);
            JSONObject name = jso.getJSONObject(pdbId);
            JSONObject pfam = name.getJSONObject("Pfam");
            Iterator pfIt = pfam.keySet().iterator();
            int pfFlag = 0;
            while(pfIt.hasNext()) {
            	int pfMapFlag = 0;
            	String pfId = pfIt.next().toString();
            	JSONObject pfOb = pfam.getJSONObject(pfId);
            	String pfName = pfOb.getString("name");
            	String pfDes = pfOb.getString("description");
            	String pfIdent = pfOb.getString("identifier");
            	JSONArray pfMaps = pfOb.getJSONArray("mappings");
            	String pfStartsTemp = "";
            	String pfEndsTemp = "";
            	for(int i = 0; i < pfMaps.length(); i++) {
            		JSONObject pfMapInfo = pfMaps.getJSONObject(i);
            		String pfChain = pfMapInfo.getString("chain_id");
            		if(!pdbChain.equals(pfChain))
            			continue;
            		JSONObject pfStartOb = pfMapInfo.getJSONObject("start");
            		int pfStart = pfStartOb.getInt("residue_number");
            		if(residueIndex < pfStart)
            			continue;
            		JSONObject pfEndOb = pfMapInfo.getJSONObject("end");
            		int pfEnd = pfEndOb.getInt("residue_number");
            		if(residueIndex > pfEnd)
            			continue;
            		else {
            			if(pfMapFlag == 0) {
            				pfStartsTemp = pfStartsTemp + String.valueOf(pfStart);
            				pfamEnds = pfamEnds + String.valueOf(pfEnd);
            				pfMapFlag = 1;
            			}
            			else {
            				pfStartsTemp = pfStartsTemp + "," + String.valueOf(pfStart);
            				pfEndsTemp = pfEndsTemp + "," + String.valueOf(pfEnd);
            			}
            		}
            	}
            	if(pfMapFlag == 1) {
            		if(pfFlag == 0) {
            			pfamStarts = pfamStarts + pfStartsTemp;
            			pfamEnds = pfamEnds + pfEndsTemp;
            			pfamIds = pfamIds + pfId;
            			pfamNames = pfamNames + pfName;
            			pfamDescriptions = pfamDescriptions + pfDes;
            			pfamIdentifiers = pfamIdentifiers + pfIdent;
            		}
            		else {
            			pfamStarts =pfamStarts + ";" + pfStartsTemp;
            			pfamEnds = pfamEnds + ";" + pfEndsTemp;
            			pfamIds = pfamIds + ";" + pfId;
            			pfamNames = pfamNames + ";" + pfName;
            			pfamDescriptions = pfamDescriptions + ";" + pfDes;
            			pfamIdentifiers = pfamIdentifiers + ";" + pfIdent;
            		}
            	}
            }
            sar.setPfamId(pfamIds);
            sar.setPfamName(pfamNames);
            sar.setPfamIdentifier(pfamIdentifiers);
            sar.setPfamDescription(pfamDescriptions);
            sar.setPfamStart(pfamStarts);
            sar.setPfamEnd(pfamEnds);
            
            String interproIds = "";
        	String interproNames = "";
        	String interproIdentifiers = "";
        	String interproStarts = "";
        	String interproEnds = "";
            JSONObject interPro = name.getJSONObject("InterPro");
            Iterator iPIt = interPro.keySet().iterator();
            int iPFlag = 0;
            while(iPIt.hasNext()) {
            	int iPMapFlag = 0;
            	String iPId = iPIt.next().toString();
            	JSONObject iPOb = interPro.getJSONObject(iPId);
            	String iPName = iPOb.getString("name");
            	String iPIdent = iPOb.getString("identifier");
            	JSONArray iPMaps = iPOb.getJSONArray("mappings");
            	String interproStartsTemp = "";
            	String interproEndsTemp = "";
            	for(int i = 0; i < iPMaps.length(); i++) {
            		JSONObject iPMapInfo = iPMaps.getJSONObject(i);
            		String iPChain = iPMapInfo.getString("chain_id");
            		if(!pdbChain.equals(iPChain))
            			continue;
            		JSONObject iPStartOb = iPMapInfo.getJSONObject("start");
            		int iPStart = iPStartOb.getInt("residue_number");
            		if(residueIndex < iPStart)
            			continue;
            		JSONObject iPEndOb = iPMapInfo.getJSONObject("end");
            		int iPEnd = iPEndOb.getInt("residue_number");
            		if(residueIndex > iPEnd)
            			continue;
            		else {
            			if(iPMapFlag == 0) {
            				interproStartsTemp = interproStartsTemp + String.valueOf(iPStart);
            				interproEndsTemp = interproEndsTemp + String.valueOf(iPEnd);
            				iPMapFlag = 1;
            			}
            			else {
            				interproStartsTemp = interproStartsTemp + "," + String.valueOf(iPStart);
            				interproEndsTemp = interproEndsTemp + "," + String.valueOf(iPEnd);
            			}
            		}
            	}
            	if(iPMapFlag == 1) {
            		if(iPFlag == 0) {
            			interproStarts = interproStarts + interproStartsTemp;
            			interproEnds = interproEnds + interproEndsTemp;
            			interproIds = interproIds + iPId;
            			interproNames = interproNames + iPName;
            			interproIdentifiers = interproIdentifiers + iPIdent;
            			iPFlag = 1;
            		}
            		else {
            			interproStarts = interproStarts + ";" + interproStartsTemp;
            			interproEnds = interproEnds + ";" + interproEndsTemp;
            			interproIds = interproIds + ";" + iPId;
            			interproNames = interproNames + ";" + iPName;
            			interproIdentifiers = interproIdentifiers + ";" + iPIdent;
            		}
            	}
            }
            sar.setInterproId(interproIds);
            sar.setInterproName(interproNames);
            sar.setInterproIdentifier(interproIdentifiers);
            sar.setInterproStart(interproStarts);
            sar.setInterproEnd(interproEnds);
    	}
    	
    	// get cath and scop info from structure_domains
    	String StUrlName = ReadConfig.getStructureDomainsUrl() + pdbId;
    	URL stUrl = new URL(StUrlName);
    	String cathIds = "";
    	String cathNames = "";
    	String cathIdentifiers = "";
    	String cathArchitectures = "";
    	String cathClasses = "";
    	String cathHomologys = "";
    	String cathTopologys = "";
    	String cathDomains = "";
    	String cathStarts = "";
    	String cathEnds = "";
    	String s2;
    	BufferedReader reader2 = new BufferedReader(new InputStreamReader(stUrl.openStream()));
    	if ((s2 = reader2.readLine()) != null) {
            JSONObject jso = new JSONObject(s2);
            JSONObject name = jso.getJSONObject(pdbId);
            JSONObject cath = name.getJSONObject("CATH");
            Iterator caIt = cath.keySet().iterator();
            int caFlag = 0;
            while(caIt.hasNext()) {
            	int caMapFlag = 0;
            	String caId = caIt.next().toString();
            	// log.info(caId);
            	JSONObject caOb = cath.getJSONObject(caId);
            	String caName = caOb.getString("name");
            	String caArchitecture = caOb.getString("architecture");
            	String caIdent = caOb.getString("identifier");
            	String caClass = caOb.getString("class");
            	String caHomology = caOb.getString("homology");
            	String caTopology = caOb.getString("topology");
            	//log.info(caName+caArchitecture+caIdent+caClass+caHomology+caTopology);
            	JSONArray caMaps = caOb.getJSONArray("mappings");
            	String caStartsTemp = "";
            	String caEndsTemp = "";
            	String caDomainTemp = "";
            	for(int i = 0; i < caMaps.length(); i++) {
            		JSONObject caMapInfo = caMaps.getJSONObject(i);
            		String caChain = caMapInfo.getString("chain_id");
            		String caDomianId = caMapInfo.getString("domain");
            		if(!pdbChain.equals(caChain))
            			continue;
            		JSONObject caStartOb = caMapInfo.getJSONObject("start");
            		int caStart = caStartOb.getInt("residue_number");
            		if(residueIndex < caStart)
            			continue;
            		JSONObject caEndOb = caMapInfo.getJSONObject("end");
            		int caEnd = caEndOb.getInt("residue_number");
            		if(residueIndex > caEnd)
            			continue;
            		else {
            			if(caMapFlag == 0) {
            				caStartsTemp = caStartsTemp + String.valueOf(caStart);
            				caEndsTemp = caEndsTemp + String.valueOf(caEnd);
            				caDomainTemp = caDomainTemp + caDomianId;
            				caMapFlag = 1;
            			}
            			else {
            				caStartsTemp = caStartsTemp + "," + String.valueOf(caStart);
            				caEndsTemp = caEndsTemp + "," + String.valueOf(caEnd);
            				caDomainTemp = caDomainTemp + "," + caDomianId;
            			}
            		}
            	}
            	if(caMapFlag == 1) {
            		if(caFlag == 0) {
            			cathStarts = cathStarts + caStartsTemp;
            			cathEnds = cathEnds + caEndsTemp;
            			cathDomains = cathDomains + caDomainTemp;
            			cathIds = cathIds + caId;
            			cathNames = cathNames + caName;
            			cathIdentifiers = cathIdentifiers + caIdent;
            			cathArchitectures = cathArchitectures + caArchitecture;
            			cathClasses = cathClasses + caClass;
            			cathHomologys = cathHomologys + caHomology;
            			cathTopologys = cathTopologys + caTopology;
            		}
            		else {
            			cathStarts = cathStarts + ";" + caStartsTemp;
            			cathEnds = cathEnds + ";" + caEndsTemp;
            			cathDomains = cathDomains + ";" + caDomainTemp;
            			cathIds = cathIds + ";" + caId;
            			cathNames = cathNames + ";" + caName;
            			cathIdentifiers = cathIdentifiers + ";" + caIdent;
            			cathArchitectures = cathArchitectures + ";" + caArchitecture;
            			cathClasses = cathClasses + ";" + caClass;
            			cathHomologys = cathHomologys + ";" + caHomology;
            			cathTopologys = cathTopologys + ";" + caTopology;
            		}
            	}
            }
            sar.setCathId(cathIds);
            sar.setCathName(cathNames);
            sar.setCathIdentifier(cathIdentifiers);
            sar.setCathArchitecture(cathArchitectures);
            sar.setCathClass(cathClasses);
            sar.setCathHomology(cathHomologys);
            sar.setCathTopology(cathTopologys);
            sar.setCathDomainStart(cathStarts);
            sar.setCathDomainEnd(cathEnds);
            sar.setCathDomainId(cathDomains);
            
            String scopIds = "";
        	String scopSccses = "";
        	String scopIdentifiers = "";
        	String scopDescriptions = "";
        	String scopClassSunids = "";
        	String scopClassDescriptions = "";
        	String scopFoldSunids = "";
        	String scopFoldDescriptions = "";
        	String scopSuperfamilySunids = "";
        	String scopSuperfamilyDescriptions = "";
        	String scopStarts = "";
        	String scopEnds = "";
            JSONObject scop = name.getJSONObject("SCOP");
            Iterator scIt = scop.keySet().iterator();
            int scFlag = 0;
            while(scIt.hasNext()) {
            	int scMapFlag = 0;
            	String scId = scIt.next().toString();
            	//log.info(scId);
            	JSONObject scOb = scop.getJSONObject(scId);
            	String scDes = scOb.getString("description");
            	String scSccs = scOb.getString("sccs");
            	String scIdent = scOb.getString("identifier");
            	JSONObject scClass = scOb.getJSONObject("class");
            	int scClassSunid = scClass.getInt("sunid");
            	String scClassDes = scClass.getString("description");
            	JSONObject scFold = scOb.getJSONObject("fold");
            	int scFoldSunid = scFold.getInt("sunid");
            	String scFoldDes = scFold.getString("description");
            	JSONObject scSF = scOb.getJSONObject("superfamily");
            	int scSFSunid = scSF.getInt("sunid");
            	String scSFDes = scSF.getString("description");
            	//log.info(scDes+scSccs+scIdent+scClassSunid+scClassDes+scFoldSunid+scFoldDes+scSFSunid+scSFDes);
            	JSONArray scMaps = scOb.getJSONArray("mappings");
            	String scStartsTemp = "";
            	String scEndsTemp = "";
            	for(int i = 0; i < scMaps.length(); i++) {
            		JSONObject scMapInfo = scMaps.getJSONObject(i);
            		String scChain = scMapInfo.getString("chain_id");
            		if(!scChain.equals(scChain))
            			continue;
            		JSONObject scStartOb = scMapInfo.getJSONObject("start");
            		int scStart = scStartOb.getInt("residue_number");
            		if(residueIndex < scStart)
            			continue;
            		JSONObject scEndOb = scMapInfo.getJSONObject("end");
            		int scEnd = scEndOb.getInt("residue_number");
            		if(residueIndex > scEnd)
            			continue;
            		else {
            			if(scMapFlag == 0) {
            				scStartsTemp = scStartsTemp + String.valueOf(scStart);
            				scEndsTemp = scEndsTemp + String.valueOf(scEnd);
            				scMapFlag = 1;
            			}
            			else {
            				scStartsTemp = scStartsTemp + "," + String.valueOf(scStart);
            				scEndsTemp = scEndsTemp + "," + String.valueOf(scEnd);
            			}
            		}
            	}
            	if(scMapFlag == 1) {
            		if(scFlag == 0) {
            			scopStarts = scopStarts + scStartsTemp;
            			scopEnds = scopEnds + scEndsTemp;
            			scopIds = scopIds + scId;
            			scopSccses = scopSccses + scSccs;
            			scopIdentifiers = scopIdentifiers + scIdent;
            			scopDescriptions = scopDescriptions + scDes;
            			scopClassSunids = scopClassSunids + String.valueOf(scClassSunid);
            			scopClassDescriptions = scopClassDescriptions + scClassDes;
            			scopFoldSunids = scopFoldSunids + String.valueOf(scFoldSunid);
            			scopFoldDescriptions = scopFoldDescriptions + scFoldDes;
            			scopSuperfamilySunids = scopSuperfamilySunids + String.valueOf(scSFSunid);
            			scopSuperfamilyDescriptions = scopSuperfamilyDescriptions + scSFDes;
            		}
            		else {
            			scopStarts = scopStarts + ";" + scStartsTemp;
            			scopEnds = scopEnds + ";" + scEndsTemp;
            			scopIds = scopIds + ";" + scId;
            			scopSccses = scopSccses + ";" + scSccs;
            			scopIdentifiers = scopIdentifiers + ";" + scIdent;
            			scopDescriptions = scopDescriptions + ";" + scDes;
            			scopClassSunids = scopClassSunids + ";" + String.valueOf(scClassSunid);
            			scopClassDescriptions = scopClassDescriptions + ";" + scClassDes;
            			scopFoldSunids = scopFoldSunids + ";" + String.valueOf(scFoldSunid);
            			scopFoldDescriptions = scopFoldDescriptions + ";" + scFoldDes;
            			scopSuperfamilySunids = scopSuperfamilySunids + ";" + String.valueOf(scSFSunid);
            			scopSuperfamilyDescriptions = scopSuperfamilyDescriptions + ";" + scSFDes;
            		}
            	}
            }
            sar.setScopId(scopIds);
            sar.setScopSccs(scopSccses);
            sar.setScopIdentifier(scopIdentifiers);
            sar.setScopDescription(scopDescriptions);
            sar.setScopClassSunid(scopClassSunids);
            sar.setScopClassDescription(scopClassDescriptions);
            sar.setScopFoldSunid(scopFoldSunids);
            sar.setScopFoldDescription(scopFoldDescriptions);
            sar.setScopSuperfamilySunid(scopSuperfamilySunids);
            sar.setScopSuperfamilyDescription(scopSuperfamilyDescriptions);
            sar.setScopStart(scopStarts);
            sar.setScopEnd(scopEnds);
    	}
    }
    
    public int stringToInt(String str) {
    	int pos = 0;
    	int i = 0;
    	while(str.charAt(i)==' ') {
    		i++;
    	}
    	pos = Integer.parseInt(str.substring(i, str.length()));
    	return pos;
    }
    
    /**
     * Using naccess to generate results
     * @param mUsageRecord
     */
    public void generateNaccessResults(MutationUsageRecord mUsageRecord){
    	HashMap<Integer, String> residueHm = mUsageRecord.getResidueHm();
    	HashSet<String> pdbSet = new HashSet<>();
    	int count = 0;
    	for(int mutationId : residueHm.keySet()) {
    		String pdbNo = residueHm.get(mutationId).split("_")[0];
    		if(!pdbSet.contains(pdbNo)){
    			runNaccessFromLocal(pdbNo); 
    		}
    		if(count%1000==0){
    			log.info("Finish "+count+"th in naccess");
    		}
    		count++;
    	}    	
    }
    
	/**
     * run Naccess on a pdb file
     * 
     * @param input pdbfile ep. 2acf.pdb
     * @return .asa .log .rsa
     */
	public int runNaccessFromLocal(String pdbNo) {
		int shellReturnCode = 0;
		try {
			CommandProcessUtil cu = new CommandProcessUtil();
			String inputFilename = ReadConfig.tmpdir+pdbNo+".pdb";
			ArrayList<String> paralist = new ArrayList<String>();
			if(Boolean.parseBoolean(ReadConfig.readLocalPDBinNaccess)){
				String foldername = pdbNo.substring(1,3).toLowerCase();
				String pdburl = ReadConfig.pdbRepo+foldername+"/pdb"+pdbNo.toLowerCase()+".ent.gz";				
				paralist = new ArrayList<String>();
		        paralist.add(pdburl);
		        paralist.add(inputFilename);
		        cu.runCommand("gunzip", paralist);
			}else{
				String pdburl = ReadConfig.pdbStructureService+pdbNo.toUpperCase()+".pdb";			
				paralist = new ArrayList<String>();
		        paralist.add(pdburl);
		        paralist.add(inputFilename);
		        cu.runCommand("wget", paralist);				
			}
			paralist = new ArrayList<String>();
	        paralist.add(inputFilename);
	        cu.runCommand("naccess", paralist);			
		}catch (Exception ex) {
            log.error("[SHELL] Fatal Error: Could not Successfully process command, exit the program now");
            log.error(ex.getMessage());
            ex.printStackTrace();
            //System.exit(0);
        }
		return shellReturnCode;
	}
	
	/**
     * find burried residue in .rsa file
     * 
     * @param input pdb.rsa
     * @return pdb.showburied
     */
	public void generateBuriedAtomicFile(String pdbNo) {
		try {
			String rsafilepwd = new String(ReadConfig.tmpdir + pdbNo + ".rsa");
			File rsafile = new File(rsafilepwd);
			String resfilepwd = new String(ReadConfig.tmpdir + pdbNo + ".showburied");
			File resfile = new File(resfilepwd);
			List<String> lines = FileUtils.readLines(rsafile, StandardCharsets.UTF_8.name());
			int linesNum = lines.size();
			// log.info(lines.size());
			HashMap<String, String> absHM = new HashMap<>();
			// float sum1 = 0, sum2 = 0;
			for (int i = linesNum - 1; i >= 0; i--) {
				// log.info(lines.get(i).split("\\s+")[0]);
				if (lines.get(i).split("\\s+")[0].equals("CHAIN")) {
					absHM.put(lines.get(i).split("\\s+")[2], lines.get(i).split("\\s+")[3]);
					// log.info(lines.get(i).split("\\s+")[2] + " " +
					// lines.get(i).split("\\s+")[3]);
				}
				if (lines.get(i).split("\\s+")[0].equals("RES")) {
					// log.info(Float.parseFloat(lines.get(i).split("\\s+")[4]));
					if (Float.parseFloat(lines.get(i).split("\\s+")[5]) < Float.parseFloat(ReadConfig.relativeRatio)) {
						lines.set(i, lines.get(i) + "      1");
					} else {
						lines.set(i, lines.get(i) + "      0");
					}
					// if(lines.get(i).split("\\s+")[2].equals("A")) {
					// sum1 += Float.parseFloat(lines.get(i).split("\\s+")[4]);
					// sum2 += Float.parseFloat(lines.get(i).split("\\s+")[5]);
					// }
				}
				if (lines.get(i).split("\\s+")[0].equals("REM") && lines.get(i).split("\\s+")[1].equals("ABS")) {
					lines.set(i, lines.get(i) + "   IFBURIED");
				} else
					continue;
			}
			// log.info(sum1 + " " + sum2);
			FileUtils.writeLines(resfile, StandardCharsets.UTF_8.name(), lines);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	

    /**
     * Call URL
     * 
     * @param pdbId
     * @param pdbChain
     * @param pdbResidueIndex
     * @throws Exception
     */
    public void getDomainsUrl(String pdbId, String pdbChain, String pdbResidueIndex) throws Exception {  	
    	String StUrlName = ReadConfig.getStructureDomainsUrl() + pdbId;
    	URL stUrl = new URL(StUrlName);
    	int residueIndex = Integer.parseInt(pdbResidueIndex);
    	String s;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(stUrl.openStream()));
    	if ((s = reader.readLine()) != null) {
            JSONObject jso = new JSONObject(s);
            JSONObject name = jso.getJSONObject(pdbId);
            JSONObject cath = name.getJSONObject("CATH");
            Iterator caIt = cath.keySet().iterator();
            while(caIt.hasNext()) {
            	String caId = caIt.next().toString();
            	// log.info(caId);
            	JSONObject caOb = cath.getJSONObject(caId);
            	String caName = caOb.getString("name");
            	String caArchitecture = caOb.getString("architecture");
            	String caIdent = caOb.getString("identifier");
            	String caClass = caOb.getString("class");
            	String caHomology = caOb.getString("homology");
            	String caTopology = caOb.getString("topology");
            	//log.info(caName+caArchitecture+caIdent+caClass+caHomology+caTopology);
            	JSONArray caMaps = caOb.getJSONArray("mappings");
            	for(int i = 0; i < caMaps.length(); i++) {
            		JSONObject caMapInfo = caMaps.getJSONObject(i);
            		String caChain = caMapInfo.getString("chain_id");
            		String caDomianId = caMapInfo.getString("domain");
            		if(!pdbChain.equals(caChain))
            			continue;
            		JSONObject caStartOb = caMapInfo.getJSONObject("start");
            		int caStart = caStartOb.getInt("residue_number");
            		if(residueIndex < caStart)
            			continue;
            		JSONObject caEndOb = caMapInfo.getJSONObject("end");
            		int caEnd = caEndOb.getInt("residue_number");
            		if(residueIndex > caEnd)
            			continue;
            		else {
            			
            		}
            	}
            }
            JSONObject scop = name.getJSONObject("SCOP");
            Iterator scIt = scop.keySet().iterator();
            while(scIt.hasNext()) {
            	String scId = scIt.next().toString();
            	log.info(scId);
            	JSONObject scOb = scop.getJSONObject(scId);
            	String scDes = scOb.getString("description");
            	String scSccs = scOb.getString("sccs");
            	String scIdent = scOb.getString("identifier");
            	JSONObject scClass = scOb.getJSONObject("class");
            	int scClassSunid = scClass.getInt("sunid");
            	String scClassDes = scClass.getString("description");
            	JSONObject scFold = scOb.getJSONObject("fold");
            	int scFoldSunid = scFold.getInt("sunid");
            	String scFoldDes = scFold.getString("description");
            	JSONObject scSF = scOb.getJSONObject("superfamily");
            	int scSFSunid = scSF.getInt("sunid");
            	String scSFDes = scSF.getString("description");
            	log.info(scDes+scSccs+scIdent+scClassSunid+scClassDes+scFoldSunid+scFoldDes+scSFSunid+scSFDes);
            	JSONArray scMaps = scOb.getJSONArray("mappings");
            	for(int i = 0; i < scMaps.length(); i++) {
            		JSONObject scMapInfo = scMaps.getJSONObject(i);
            		String scChain = scMapInfo.getString("chain_id");
            		if(!scChain.equals(scChain))
            			continue;
            		JSONObject scStartOb = scMapInfo.getJSONObject("start");
            		int scStart = scStartOb.getInt("residue_number");
            		if(residueIndex < scStart)
            			continue;
            		JSONObject scEndOb = scMapInfo.getJSONObject("end");
            		int scEnd = scEndOb.getInt("residue_number");
            		if(residueIndex > scEnd)
            			continue;
            		else {
            			
            		}
            	}
            }
    	}
    }
    

}
