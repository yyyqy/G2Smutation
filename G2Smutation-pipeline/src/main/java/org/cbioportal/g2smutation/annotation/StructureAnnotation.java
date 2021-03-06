package org.cbioportal.g2smutation.annotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.cbioportal.g2smutation.util.CommandProcessUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.StringUtil;
import org.cbioportal.g2smutation.util.models.MutationUsageRecord;
import org.cbioportal.g2smutation.util.models.StructureAnnotationRecord;
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
    int structureAnnotationFilenum = 0;
       
    public int getStructureAnnotationFilenum() {
		return structureAnnotationFilenum;
	}

	public void setStructureAnnotationFilenum(int structureAnnotationFilenum) {
		this.structureAnnotationFilenum = structureAnnotationFilenum;
	}

	StringUtil su = new StringUtil();

    /**
     * generate structure annotation for strucure_annotation_entry
     * Generate results first and then write to sql file
     * 
     * @param MutationUsageRecord
     * @param outputFilename
     * @param structureAnnoHm <String, StructureAnnotationRecord>
     * @param urlFlag True:  Call URL
     * 				  False: not call URL
     * 
     */
	public void parseGenerateMutationResultSQL4StructureAnnotationEntry(MutationUsageRecord mUsageRecord,
			String outputFilename, Map structureAnnoHm, boolean urlFlag) {
		HashMap<Integer, String> mutationIdHm = mUsageRecord.getMutationIdHm();
		HashMap<Integer, String> residueHm = mUsageRecord.getResidueHm();
		int filecount = 0;
		try {
			List<StructureAnnotationRecord> sarList = new ArrayList<>();

			int count = 0;
			// ArrayList<String> annoKeyList = new ArrayList<String>();
			SortedSet<String> annoKeySet = new TreeSet<>();
			for (int mutationId : mutationIdHm.keySet()) {
				String annoKey = residueHm.get(mutationId);
				annoKeySet.add(annoKey);
			}

			List<String> naccessLines = new ArrayList<>();
			List<String> dsspLines = new ArrayList<>();
			List<String> asaLines = new ArrayList<>();
			List<String> sequenceDomainLines = new ArrayList<>();
			List<String> structureDomainLines = new ArrayList<>();
			File dir = new File(ReadConfig.tmpdir);
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".pdb");
				}
			});
			// dummy init, find random
			Structure struc = new PDBFileReader().getStructure(files[files.length - 2]);
			SortedMap<String, StructureAnnotationRecord> pdbContentMp = new TreeMap<>();

			String pdbOld = "";
			
			//CATH related:
			HashMap<String, List<String>> cathLines = readCathAllFile();
			HashMap<String, String> cathNamesLines = readCathNamesFile();

			//default updateFlag is false
			boolean updateFlag = Boolean.parseBoolean(ReadConfig.structureAnnoHmUpdate);
			Iterator it = annoKeySet.iterator();
			while (it.hasNext()) {
				String annoKey = it.next().toString();
				StructureAnnotationRecord sarOut = new StructureAnnotationRecord();
				
				boolean doFlag = false;				
				//flag to override hashmap, in case the API results are not perfect
				if (structureAnnoHm.containsKey(annoKey)) {
					if(updateFlag) {
						StructureAnnotationRecord tmpsar = (StructureAnnotationRecord)structureAnnoHm.get(annoKey);
						if(tmpsar.getCathName().equals("")) {
							doFlag = true;
						}
					}					
				}else {
					doFlag = true;
				}
				if (doFlag) {
					String pdb = annoKey.split("_")[0];
					String chain = annoKey.split("_")[1];
					String index = annoKey.split("_")[2];
					// System.out.println("annoKey:" + annoKey);

					// Save IO
					if (!pdb.equals(pdbOld)) {
						
						getNaccessInfo(naccessLines, pdbContentMp);
						getDSSPInfo(dsspLines, pdbContentMp);
						getHETInfo(asaLines, struc, pdbContentMp);
						getSequenceDomainInfo(sequenceDomainLines, pdbOld, pdbContentMp);
						getStructureDomainInfo(structureDomainLines, pdbOld, pdbContentMp);

						for (String contentKey : pdbContentMp.keySet()) {
							StructureAnnotationRecord sar = pdbContentMp.get(contentKey);
							//holder
							//getDomainUrlPlaceholder(sar);
							String contentchain = contentKey.split("_")[0];
							String contentindex = contentKey.split("_")[1];
							sar.setPdbAnnoKey(pdbOld + "_" + contentKey);
							sar.setPdbNo(pdbOld + "_" + contentchain);
							sar.setPdbResidueIndex(Integer.parseInt(contentindex));							

							structureAnnoHm.put(pdbOld + "_" + contentKey, sar);
							sarList.add(sar);
							// System.out.println("PutKey: " + pdbOld+"_"+contentKey);

						}

						//Get all information from files
						try {
							naccessLines = FileUtils.readLines(
									new File((ReadConfig.tmpdir + pdb + ReadConfig.naccessFileSuffix)),
									StandardCharsets.UTF_8.name());							
							asaLines = FileUtils.readLines(new File((ReadConfig.tmpdir + pdb + ".asa")),
									StandardCharsets.UTF_8.name());
							struc = new PDBFileReader().getStructure(ReadConfig.tmpdir + pdb + ".pdb");
							//Read sequence domain URL
							sequenceDomainLines = getSequenceDomainAnnotationsURL(pdb);
							//Read structrue domain URL
							structureDomainLines = getStructureDomainAnnotationsURL(pdb);
							
							pdbContentMp = new TreeMap<>();

							pdbOld = pdb;
						}catch(FileNotFoundException ex) {
							log.info(ex);
						}catch (Exception ex) {
							ex.printStackTrace();
						}
						

						// For many cases, dssp dose not existed
						try {
							dsspLines = FileUtils.readLines(
									new File(ReadConfig.dsspLocalDataFile + pdb + ReadConfig.dsspFileSuffix),
									StandardCharsets.UTF_8.name());							
						}catch(FileNotFoundException ex) {
							log.info(ex);
						}catch (Exception ex) {
							ex.printStackTrace();
						}
					}

					pdbContentMp.put(chain + "_" + index, new StructureAnnotationRecord());

					// Use placeholder now
					// getHETInfoPlaceholder(sar);
					// getDomainUrlPlaceholder(sar);

				} else {
					sarOut = (StructureAnnotationRecord)structureAnnoHm.get(annoKey);
					sarList.add(sarOut);
				}

				if (count % 10000 == 0) {
					log.info("Processing " + count + "th in total size of " + annoKeySet.size() + " annoKeyList");
				}

				count++;
				if (count % 100000 == 0) {
					System.out.println(sarList.size());
					generateMutationResultSQL4StructureAnnotation(sarList,
							outputFilename + "." + Integer.toString(filecount));					
					filecount++;
					sarList = new ArrayList<StructureAnnotationRecord>();
				}
			}

			// Post, the last one
			getNaccessInfo(naccessLines, pdbContentMp);
			getDSSPInfo(dsspLines, pdbContentMp);
			getHETInfo(asaLines, struc, pdbContentMp);
			getSequenceDomainInfo(sequenceDomainLines, pdbOld, pdbContentMp);
			getStructureDomainInfo(structureDomainLines, pdbOld, pdbContentMp);

			for (String contentKey : pdbContentMp.keySet()) {
				StructureAnnotationRecord sar = pdbContentMp.get(contentKey);
				getDomainUrlPlaceholder(sar);
				String contentchain = contentKey.split("_")[0];
				String contentindex = contentKey.split("_")[1];
				sar.setPdbAnnoKey(pdbOld + "_" + contentKey);
				sar.setPdbNo(pdbOld + "_" + contentchain);
				sar.setPdbResidueIndex(Integer.parseInt(contentindex));			

				structureAnnoHm.put(pdbOld + "_" + contentKey, sar);
				sarList.add(sar);
				// System.out.println("PostPutKey: " + pdbOld+"_"+contentKey);
			}

			generateMutationResultSQL4StructureAnnotation(sarList, outputFilename + "." + Integer.toString(filecount));
			
			this.structureAnnotationFilenum = filecount;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    /**
     * Generate sql file for table structrue_annotation_entry
     * 
     * @param sarList
     * @param outputFilename
     */
    public void generateMutationResultSQL4StructureAnnotation(List<StructureAnnotationRecord> sarList,
            String outputFilename) {
        try {
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            
            for(StructureAnnotationRecord sar:sarList){
                outputlist.add(makeTable_structureAnnotation_insert(sar));
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);            
            log.info("Total Structure mutation is "+outputlist.size());
            outputlist = null;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Generating entry for table structure_annotation_entry
     * 
     * @param sar
     * @return
     */
    public String makeTable_structureAnnotation_insert(StructureAnnotationRecord sar) {
        String str = "INSERT INTO `structure_annotation_entry` (`PDB_ANNOKEY`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,"
                + "`BURIED`,`ALL_ATOMS_ABS`,`ALL_ATOMS_REL`,`TOTAL_SIDE_ABS`,`TOTAL_SIDE_REL`,`MAIN_CHAIN_ABS`,`MAIN_CHAIN_REL`,"
                + "`NON_POLAR_ABS`,`NON_POLAR_REL`,`ALL_POLAR_ABS`,`ALL_POLAR_REL`,`SEC_STRUCTURE`,`THREE_TURN_HELIX`,"
                + "`FOUR_TURN_HELIX`,`FIVE_TURN_HELIX`,`GEOMETRICAL_BEND`,`CHIRALITY`,`BETA_BRIDGE_LABELA`,`BETA_BRIDGE_LABELB`,"
                + "`BPA`,`BPB`,`BETA_SHEET_LABEL`,`ACC`,`LIGAND_BINDING_PROTEIN`,`LIGAND_BINDING_DIRECT`,`LIGAND_NAME`,`INTERPRO_ID`,"
                + "`INTERPRO_NAME`,`INTERPRO_IDENTIFIER`,`INTERPRO_START`,`INTERPRO_END`,`PFAM_ID`,`PFAM_NAME`,"
                + "`PFAM_DESCRIPTION`,`PFAM_IDENTIFIER`,`PFAM_START`,`PFAM_END`,`CATH_ID`,`CATH_ARCHITECTURE`,`CATH_CLASS`,"
                + "`CATH_HOMOLOGY`,`CATH_IDENTIFIER`,`CATH_NAME`,`CATH_TOPOLOGY`,`CATH_DOMAIN_ID`,`CATH_DOMAIN_START`,"
                + "`CATH_DOMAIN_END`,`SCOP_ID`,`SCOP_DESCRIPTION`,`SCOP_IDENTIFIER`,`SCOP_SCCS`,`SCOP_CLASS_SUNID`,"
                + "`SCOP_CLASS_DESCRIPTION`,`SCOP_FOLD_SUNID`,`SCOP_FOLD_DESCRIPTION`,`SCOP_SUPERFAMILY_SUNID`,"
                + "`SCOP_SUPERFAMILY_DESCRIPTION`,`SCOP_START`,`SCOP_END`)VALUES('" + sar.getPdbAnnoKey() + "','"
                + sar.getPdbNo() + "'," + sar.getPdbResidueIndex() + ",'"
                + sar.getPdbResidueName() + "','" + sar.getBuried() + "','" + sar.getAllAtomsABS() + "','"
                + sar.getAllAtomsREL() + "','" + sar.getTotalSideABS() + "','" + sar.getTotalSideREL() + "','"
                + sar.getMainChainABS() + "','" + sar.getMainChainREL() + "','" + sar.getNonPolarABS() + "','"
                + sar.getNonPolarREL() + "','" + sar.getAllPolarABS() + "','" + sar.getAllPolarREL() + "','"
                + sar.getSecStructure() + "','" + sar.getThreeTurnHelix() + "','" + sar.getFourTurnHelix() + "','"
                + sar.getFiveTurnHelix() + "','" + sar.getGeometricalBend() + "','" + sar.getChirality() + "','"
                + sar.getBetaBridgeLabela() + "','" + sar.getBetaBridgeLabelb() + "','" + sar.getBpa() + "','"
                + sar.getBpb() + "','" + sar.getBetaSheetLabel() + "','" + sar.getAcc() + "',"
                + sar.getLigandBindingProtein() + "," + sar.getLigandBindingdirect() + ",'" + sar.getLigandName()
                + "','" + sar.getInterproId() + "','" + sar.getInterproName() + "','" + sar.getInterproIdentifier()
                + "','" + sar.getInterproStart() + "','" + sar.getInterproEnd() + "','" + sar.getPfamId() + "','"
                + sar.getPfamName() + "','" + sar.getPfamDescription() + "','" + sar.getPfamIdentifier() + "','"
                + sar.getPfamStart() + "','" + sar.getPfamEnd() + "','" + sar.getCathId() + "','"
                + sar.getCathArchitecture() + "','" + sar.getCathClass() + "','" + sar.getCathHomology() + "','"
                + sar.getCathIdentifier() + "','" + sar.getCathName() + "','" + sar.getCathTopology() + "','"
                + sar.getCathDomainId() + "','" + sar.getCathDomainStart() + "','" + sar.getCathDomainEnd() + "','"
                + sar.getScopId() + "','" + sar.getScopDescription() + "','" + sar.getScopIdentifier() + "','"
                + sar.getScopSccs() + "','" + sar.getScopClassSunid() + "','" + sar.getScopClassDescription() + "','"
                + sar.getScopFoldSunid() + "','" + sar.getScopFoldDescription() + "','" + sar.getScopSuperfamilySunid()
                + "','" + sar.getScopFoldDescription() + "','" + sar.getScopStart() + "','" + sar.getScopEnd()
                + "');\n";
        return str;
    }

	public void getDSSPInfo(List<String> lines, SortedMap<String, StructureAnnotationRecord> pdbContentMp) {
		boolean flag = false;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).substring(2, 3).equals("#")) {
				flag = true;
				continue;
			}
			if (flag) {
				String currentStr = lines.get(i);
				String checkStr = currentStr.substring(11, 12) + "_" + currentStr.substring(6, 10).trim();

				if (pdbContentMp.containsKey(checkStr)) {
					StructureAnnotationRecord sar = pdbContentMp.get(checkStr);

					sar.setPdbResidueName(currentStr.substring(13, 14));
					sar.setSecStructure(currentStr.substring(16, 17));
					sar.setThreeTurnHelix(currentStr.substring(18, 19));
					sar.setFourTurnHelix(currentStr.substring(19, 20));
					sar.setFiveTurnHelix(currentStr.substring(20, 21));
					sar.setGeometricalBend(currentStr.substring(21, 22));
					sar.setChirality(currentStr.substring(22, 23));
					sar.setBetaBridgeLabela(currentStr.substring(23, 24));
					sar.setBetaBridgeLabelb(currentStr.substring(24, 25));
					sar.setBpa(currentStr.substring(26, 29));
					sar.setBpb(currentStr.substring(30, 33));
					sar.setBetaSheetLabel(currentStr.substring(33, 34));
					sar.setAcc(currentStr.substring(35, 38));
					
					pdbContentMp.put(checkStr, sar);
				}
			}
		}
	}

	public void getNaccessInfo(List<String> lines, SortedMap<String, StructureAnnotationRecord> pdbContentMp) {

		for (int i = 0; i < lines.size(); i++) {
			String currentStr = lines.get(i);
			String checkStr = currentStr.substring(8, 9) + "_" + currentStr.substring(9, 13).trim();
			if (currentStr.substring(0, 3).equals("RES") && pdbContentMp.containsKey(checkStr)) {
				StructureAnnotationRecord sar = pdbContentMp.get(checkStr);
				String[] tmpArray = currentStr.split("\\s+");
				sar.setBuried(tmpArray[tmpArray.length - 1]);
				sar.setAllAtomsABS(currentStr.substring(16, 22));
				sar.setAllAtomsREL(currentStr.substring(23, 28));
				sar.setTotalSideABS(currentStr.substring(29, 35));
				sar.setTotalSideREL(currentStr.substring(36, 41));
				sar.setMainChainABS(currentStr.substring(42, 48));
				sar.setMainChainREL(currentStr.substring(49, 54));
				sar.setNonPolarABS(currentStr.substring(55, 61));
				sar.setNonPolarREL(currentStr.substring(62, 67));
				sar.setAllPolarABS(currentStr.substring(68, 74));
				sar.setAllPolarREL(currentStr.substring(75, 80));
				pdbContentMp.put(checkStr, sar);
			}
		}

	}
    
    /**
     * Add placeholder in HET
     * 
     * @param sar
     */
    public void getHETInfoPlaceholder(StructureAnnotationRecord sar) {
        sar.setLigandBindingdirect(0);
        sar.setLigandBindingProtein(0);
        sar.setLigandName("");
    }

    /**
     * 
     * Get HET information
     * 
     * @param sar
     * @param pdbId
     * @param pdbChain
     * @param pdbResidueIndex
     */
    public void getHETInfo(List<String> lines,  Structure struc, SortedMap<String, StructureAnnotationRecord> pdbContentMp) {

            double x1, x2, y1, y2, z1, z2, ra, rl, dis;
            for (int k =0; k<lines.size(); k++) {
            	String currentStr = lines.get(k);
    			String checkStr = currentStr.substring(21, 22) + "_" + currentStr.substring(22, 26).trim();

    			if(currentStr.substring(13, 15).equals("CA") && pdbContentMp.containsKey(checkStr)) {
    				StructureAnnotationRecord sar = pdbContentMp.get(checkStr);
    				x1 = Double.parseDouble(currentStr.substring(30, 38));
    	            y1 = Double.parseDouble(currentStr.substring(38, 46));
    	            z1 = Double.parseDouble(currentStr.substring(46, 54));
    	            ra = Double.parseDouble(currentStr.substring(64, 68));
    	            
    	            List<Group> hetGroup = new ArrayList<Group>();
    	            String ligantNames = "";
    	            hetGroup.addAll(struc.getHetGroups());
    	            if (hetGroup.isEmpty()) {
    	                sar.setLigandBindingdirect(0);
    	                sar.setLigandBindingProtein(0);
    	                sar.setLigandName(ligantNames);
    	            } else {
    	                sar.setLigandBindingProtein(1);
    	                for (int i = 0; i < hetGroup.size(); i++) {
    	                    for (int j = 0; j < hetGroup.get(i).size(); j++) {
    	                        x2 = hetGroup.get(i).getAtom(j).getX();
    	                        y2 = hetGroup.get(i).getAtom(j).getY();
    	                        z2 = hetGroup.get(i).getAtom(j).getZ();
    	                        rl = getLigantRadius(hetGroup.get(i).getPDBName());
    	                        dis = getDistance(x1, y1, z1, x2, y2, z2, ra, rl);
    	                        if (dis < 0.50) {
    	                        	// Threshold value from: Jianyi Yang, Ambrish Roy, Yang Zhang, BioLiP: a semi-manually curated database for biologically relevant ligand–protein interactions, Nucleic Acids Research, Volume 41, Issue D1, 1 January 2013, Pages D1096–D1103, https://doi.org/10.1093/nar/gks966
    	                            sar.setLigandBindingdirect(1);
    	                            ligantNames = ligantNames + hetGroup.get(i).getPDBName() + "; ";
    	                        }
    	                    }
    	                }
    	                sar.setLigandName(ligantNames);
    	            }
    	            pdbContentMp.put(checkStr, sar);
    				
    			}
            }            

        
    }

    public double getDistance(double x1, double y1, double z1, double x2, double y2, double z2, double ra, double rl) {
        double dis = 0;
        dis = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2)) - ra - rl;
        return dis;
    }

    public double getLigantRadius(String name) {
    	//information from https://periodictable.com/Properties/A/VanDerWaalsRadius.v.html
        double rl = 0;
        switch (name) {
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
        default:
        	//if no ligand information, choose default 1.40 as C
        	rl = 1.40;
        }
        return rl;
    }

    /**
     * Add placeholder in domain
     * TODO: will change that when we have local installation
     * @param sar
     */
    public void getDomainUrlPlaceholder(StructureAnnotationRecord sar){
        sar.setPfamId("");
        sar.setPfamName("");
        sar.setPfamIdentifier("");
        sar.setPfamDescription("");
        sar.setPfamStart("");
        sar.setPfamEnd("");
        sar.setInterproId("");
        sar.setInterproName("");
        sar.setInterproIdentifier("");
        sar.setInterproStart("");
        sar.setInterproEnd("");
        sar.setCathId("");
        sar.setCathName("");
        sar.setCathIdentifier("");
        sar.setCathArchitecture("");
        sar.setCathClass("");
        sar.setCathHomology("");
        sar.setCathTopology("");
        sar.setCathDomainStart("");
        sar.setCathDomainEnd("");
        sar.setCathDomainId("");
        sar.setScopId("");
        sar.setScopSccs("");
        sar.setScopIdentifier("");
        sar.setScopDescription("");
        sar.setScopClassSunid("");
        sar.setScopClassDescription("");
        sar.setScopFoldSunid("");
        sar.setScopFoldDescription("");
        sar.setScopSuperfamilySunid("");
        sar.setScopSuperfamilyDescription("");
        sar.setScopStart("");
        sar.setScopEnd("");
        
        
    }
    
    /**
     * Can be delete later
     * Get domains URL waste time
     * 
     * @param sar
     * @param pdbId
     * @param pdbChain
     * @param pdbResidueIndex
     * @throws Exception
     */
    public void getDomainsUrl(StructureAnnotationRecord sar, String pdbId, String pdbChain, String pdbResidueIndex)
            throws Exception {
        // get pfam and interpro info from sequence_domains
        String seUrlName = ReadConfig.getSequenceDomainsUrl() + pdbId;
        log.info(seUrlName);
        URL seUrl = new URL(seUrlName);
        int residueIndex = Integer.parseInt(pdbResidueIndex);
        String pfamIds = "";
        String pfamNames = "";
        String pfamDescriptions = "";
        String pfamIdentifiers = "";
        String pfamStarts = "";
        String pfamEnds = "";
        String interproIds = "";
        String interproNames = "";
        String interproIdentifiers = "";
        String interproStarts = "";
        String interproEnds = "";

        try {
            String s1;
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(seUrl.openStream()));
            if ((s1 = reader1.readLine()) != null) {
                JSONObject jso = new JSONObject(s1);
                JSONObject name = jso.getJSONObject(pdbId);
                JSONObject pfam = name.getJSONObject("Pfam");
                Iterator pfIt = pfam.keySet().iterator();
                int pfFlag = 0;
                while (pfIt.hasNext()) {
                    int pfMapFlag = 0;
                    String pfId = pfIt.next().toString();
                    JSONObject pfOb = pfam.getJSONObject(pfId);
                    String pfName = su.toSQLstring(pfOb.getString("name"));
                    String pfDes = su.toSQLstring(pfOb.getString("description"));
                    String pfIdent = su.toSQLstring(pfOb.getString("identifier"));
                    JSONArray pfMaps = pfOb.getJSONArray("mappings");
                    String pfStartsTemp = "";
                    String pfEndsTemp = "";
                    for (int i = 0; i < pfMaps.length(); i++) {
                        JSONObject pfMapInfo = pfMaps.getJSONObject(i);
                        String pfChain = pfMapInfo.getString("chain_id");
                        if (!pdbChain.equals(pfChain))
                            continue;
                        JSONObject pfStartOb = pfMapInfo.getJSONObject("start");
                        int pfStart = pfStartOb.getInt("residue_number");
                        if (residueIndex < pfStart)
                            continue;
                        JSONObject pfEndOb = pfMapInfo.getJSONObject("end");
                        int pfEnd = pfEndOb.getInt("residue_number");
                        if (residueIndex > pfEnd)
                            continue;
                        else {
                            if (pfMapFlag == 0) {
                                pfStartsTemp = pfStartsTemp + String.valueOf(pfStart);
                                pfamEnds = pfamEnds + String.valueOf(pfEnd);
                                pfMapFlag = 1;
                            } else {
                                pfStartsTemp = pfStartsTemp + "," + String.valueOf(pfStart);
                                pfEndsTemp = pfEndsTemp + "," + String.valueOf(pfEnd);
                            }
                        }
                    }
                    if (pfMapFlag == 1) {
                        if (pfFlag == 0) {
                            pfamStarts = pfamStarts + pfStartsTemp;
                            pfamEnds = pfamEnds + pfEndsTemp;
                            pfamIds = pfamIds + pfId;
                            pfamNames = pfamNames + pfName;
                            pfamDescriptions = pfamDescriptions + pfDes;
                            pfamIdentifiers = pfamIdentifiers + pfIdent;
                        } else {
                            pfamStarts = pfamStarts + ";" + pfStartsTemp;
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

                JSONObject interPro = name.getJSONObject("InterPro");
                Iterator iPIt = interPro.keySet().iterator();
                int iPFlag = 0;
                while (iPIt.hasNext()) {
                    int iPMapFlag = 0;
                    String iPId = iPIt.next().toString();
                    JSONObject iPOb = interPro.getJSONObject(iPId);
                    String iPName = su.toSQLstring(iPOb.getString("name"));
                    String iPIdent = su.toSQLstring(iPOb.getString("identifier"));
                    JSONArray iPMaps = iPOb.getJSONArray("mappings");
                    String interproStartsTemp = "";
                    String interproEndsTemp = "";
                    for (int i = 0; i < iPMaps.length(); i++) {
                        JSONObject iPMapInfo = iPMaps.getJSONObject(i);
                        String iPChain = iPMapInfo.getString("chain_id");
                        if (!pdbChain.equals(iPChain))
                            continue;
                        JSONObject iPStartOb = iPMapInfo.getJSONObject("start");
                        int iPStart = iPStartOb.getInt("residue_number");
                        if (residueIndex < iPStart)
                            continue;
                        JSONObject iPEndOb = iPMapInfo.getJSONObject("end");
                        int iPEnd = iPEndOb.getInt("residue_number");
                        if (residueIndex > iPEnd)
                            continue;
                        else {
                            if (iPMapFlag == 0) {
                                interproStartsTemp = interproStartsTemp + String.valueOf(iPStart);
                                interproEndsTemp = interproEndsTemp + String.valueOf(iPEnd);
                                iPMapFlag = 1;
                            } else {
                                interproStartsTemp = interproStartsTemp + "," + String.valueOf(iPStart);
                                interproEndsTemp = interproEndsTemp + "," + String.valueOf(iPEnd);
                            }
                        }
                    }
                    if (iPMapFlag == 1) {
                        if (iPFlag == 0) {
                            interproStarts = interproStarts + interproStartsTemp;
                            interproEnds = interproEnds + interproEndsTemp;
                            interproIds = interproIds + iPId;
                            interproNames = interproNames + iPName;
                            interproIdentifiers = interproIdentifiers + iPIdent;
                            iPFlag = 1;
                        } else {
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
        } catch (Exception e1) {
            log.error(e1);
            sar.setPfamId(pfamIds);
            sar.setPfamName(pfamNames);
            sar.setPfamIdentifier(pfamIdentifiers);
            sar.setPfamDescription(pfamDescriptions);
            sar.setPfamStart(pfamStarts);
            sar.setPfamEnd(pfamEnds);
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

        try {
            String s2;
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(stUrl.openStream()));
            if ((s2 = reader2.readLine()) != null) {
                JSONObject jso = new JSONObject(s2);
                JSONObject name = jso.getJSONObject(pdbId);
                JSONObject cath = name.getJSONObject("CATH");
                Iterator caIt = cath.keySet().iterator();
                int caFlag = 0;
                while (caIt.hasNext()) {
                    int caMapFlag = 0;
                    String caId = caIt.next().toString();
                    // log.info(caId);
                    JSONObject caOb = cath.getJSONObject(caId);
                    String caName = su.toSQLstring(caOb.getString("name"));
                    String caArchitecture = su.toSQLstring(caOb.getString("architecture"));
                    String caIdent = su.toSQLstring(caOb.getString("identifier"));
                    String caClass = su.toSQLstring(caOb.getString("class"));
                    String caHomology = su.toSQLstring(caOb.getString("homology"));
                    String caTopology = su.toSQLstring(caOb.getString("topology"));
                    // log.info(caName+caArchitecture+caIdent+caClass+caHomology+caTopology);
                    JSONArray caMaps = caOb.getJSONArray("mappings");
                    String caStartsTemp = "";
                    String caEndsTemp = "";
                    String caDomainTemp = "";
                    for (int i = 0; i < caMaps.length(); i++) {
                        JSONObject caMapInfo = caMaps.getJSONObject(i);
                        String caChain = caMapInfo.getString("chain_id");
                        String caDomianId = caMapInfo.getString("domain");
                        if (!pdbChain.equals(caChain))
                            continue;
                        JSONObject caStartOb = caMapInfo.getJSONObject("start");
                        int caStart = caStartOb.getInt("residue_number");
                        if (residueIndex < caStart)
                            continue;
                        JSONObject caEndOb = caMapInfo.getJSONObject("end");
                        int caEnd = caEndOb.getInt("residue_number");
                        if (residueIndex > caEnd)
                            continue;
                        else {
                            if (caMapFlag == 0) {
                                caStartsTemp = caStartsTemp + String.valueOf(caStart);
                                caEndsTemp = caEndsTemp + String.valueOf(caEnd);
                                caDomainTemp = caDomainTemp + caDomianId;
                                caMapFlag = 1;
                            } else {
                                caStartsTemp = caStartsTemp + "," + String.valueOf(caStart);
                                caEndsTemp = caEndsTemp + "," + String.valueOf(caEnd);
                                caDomainTemp = caDomainTemp + "," + caDomianId;
                            }
                        }
                    }
                    if (caMapFlag == 1) {
                        if (caFlag == 0) {
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
                        } else {
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

                JSONObject scop = name.getJSONObject("SCOP");
                Iterator scIt = scop.keySet().iterator();
                int scFlag = 0;
                while (scIt.hasNext()) {
                    int scMapFlag = 0;
                    String scId = scIt.next().toString();
                    // log.info(scId);
                    JSONObject scOb = scop.getJSONObject(scId);
                    String scDes = su.toSQLstring(scOb.getString("description"));
                    String scSccs = su.toSQLstring(scOb.getString("sccs"));
                    String scIdent = su.toSQLstring(scOb.getString("identifier"));
                    JSONObject scClass = scOb.getJSONObject("class");
                    int scClassSunid = scClass.getInt("sunid");
                    String scClassDes = su.toSQLstring(scClass.getString("description"));
                    JSONObject scFold = scOb.getJSONObject("fold");
                    int scFoldSunid = scFold.getInt("sunid");
                    String scFoldDes = su.toSQLstring(scFold.getString("description"));
                    JSONObject scSF = scOb.getJSONObject("superfamily");
                    int scSFSunid = scSF.getInt("sunid");
                    String scSFDes = su.toSQLstring(scSF.getString("description"));
                    // log.info(scDes+scSccs+scIdent+scClassSunid+scClassDes+scFoldSunid+scFoldDes+scSFSunid+scSFDes);
                    JSONArray scMaps = scOb.getJSONArray("mappings");
                    String scStartsTemp = "";
                    String scEndsTemp = "";
                    for (int i = 0; i < scMaps.length(); i++) {
                        JSONObject scMapInfo = scMaps.getJSONObject(i);
                        String scChain = scMapInfo.getString("chain_id");
                        if (!scChain.equals(scChain))
                            continue;
                        JSONObject scStartOb = scMapInfo.getJSONObject("start");
                        int scStart = scStartOb.getInt("residue_number");
                        if (residueIndex < scStart)
                            continue;
                        JSONObject scEndOb = scMapInfo.getJSONObject("end");
                        int scEnd = scEndOb.getInt("residue_number");
                        if (residueIndex > scEnd)
                            continue;
                        else {
                            if (scMapFlag == 0) {
                                scStartsTemp = scStartsTemp + String.valueOf(scStart);
                                scEndsTemp = scEndsTemp + String.valueOf(scEnd);
                                scMapFlag = 1;
                            } else {
                                scStartsTemp = scStartsTemp + "," + String.valueOf(scStart);
                                scEndsTemp = scEndsTemp + "," + String.valueOf(scEnd);
                            }
                        }
                    }
                    if (scMapFlag == 1) {
                        if (scFlag == 0) {
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
                        } else {
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
        } catch (Exception e2) {
//            log.error(e2);
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
    
    /**
     * Get sequence domain annotation
     * https://www.ebi.ac.uk/pdbe/api/mappings/sequence_domains/1cbs
     * 
     * @param pdbId
     * @return
     */
    public List<String> getSequenceDomainAnnotationsURL(String pdbId) {
    	List<String> domainLines = new ArrayList<>();
    	try {
    		String seUrlName = ReadConfig.getSequenceDomainsUrl() + pdbId;
            URL seUrl = new URL(seUrlName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(seUrl.openStream()));
            String str;
            if ((str = reader.readLine()) != null) {
            	domainLines.add(str);
            }
    	}catch (FileNotFoundException ex) {
    		log.info(ex);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}   	
    	return domainLines;    	
    }
    
    /**
     * Get structure domain annotation
     * https://www.ebi.ac.uk/pdbe/api/mappings/structural_domains/1cbs
     * 
     * @param pdbId
     * @return
     */
    public List<String> getStructureDomainAnnotationsURL(String pdbId) {
    	List<String> domainLines = new ArrayList<>();
    	try {
    		String seUrlName = ReadConfig.getStructureDomainsUrl() + pdbId;
            URL seUrl = new URL(seUrlName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(seUrl.openStream()));
            String str;
            if ((str = reader.readLine()) != null) {
            	domainLines.add(str);
            }
    	}catch (FileNotFoundException ex) {
    		log.info(ex);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}   	
    	return domainLines;    	
    }
    

    /**
     * Get sequence domains URL
     * 
     * @param domainLines
     * @param pdbId
     * @param pdbContentMp
     */
    public void getSequenceDomainInfo(List<String> domainLines, String pdbId, SortedMap<String, StructureAnnotationRecord> pdbContentMp) {
		// get pfam and interpro info from sequence_domains

		try {
			// String checkStr = currentStr.substring(8, 9) + "_" + currentStr.substring(9,
			// 13).trim();
			for (String checkStr : pdbContentMp.keySet()) {
				String pdbChain = checkStr.split("_")[0];
				String pdbResidueIndex = checkStr.split("_")[1];
				int residueIndex = Integer.parseInt(pdbResidueIndex);
				StructureAnnotationRecord sar = pdbContentMp.get(checkStr);
				String pfamIds = "";
				String pfamNames = "";
				String pfamDescriptions = "";
				String pfamIdentifiers = "";
				String pfamStarts = "";
				String pfamEnds = "";
				String interproIds = "";
				String interproNames = "";
				String interproIdentifiers = "";
				String interproStarts = "";
				String interproEnds = "";
				//placeholder
		        sar.setPfamId("");
		        sar.setPfamName("");
		        sar.setPfamIdentifier("");
		        sar.setPfamDescription("");
		        sar.setPfamStart("");
		        sar.setPfamEnd("");
		        sar.setInterproId("");
		        sar.setInterproName("");
		        sar.setInterproIdentifier("");
		        sar.setInterproStart("");
		        sar.setInterproEnd("");

				for (String domainStr : domainLines) {
					JSONObject jso = new JSONObject(domainStr);
					JSONObject name = jso.getJSONObject(pdbId);
					JSONObject pfam = name.getJSONObject("Pfam");
					Iterator pfIt = pfam.keySet().iterator();
					int pfFlag = 0;
					while (pfIt.hasNext()) {
						int pfMapFlag = 0;
						String pfId = pfIt.next().toString();
						JSONObject pfOb = pfam.getJSONObject(pfId);
						String pfName = su.toSQLstring(pfOb.getString("name"));
						String pfDes = su.toSQLstring(pfOb.getString("description"));
						String pfIdent = su.toSQLstring(pfOb.getString("identifier"));
						JSONArray pfMaps = pfOb.getJSONArray("mappings");
						String pfStartsTemp = "";
						String pfEndsTemp = "";
						for (int i = 0; i < pfMaps.length(); i++) {
							JSONObject pfMapInfo = pfMaps.getJSONObject(i);
							String pfChain = pfMapInfo.getString("chain_id");
							if (!pdbChain.equals(pfChain))
								continue;
							JSONObject pfStartOb = pfMapInfo.getJSONObject("start");
							int pfStart = pfStartOb.getInt("residue_number");
							if (residueIndex < pfStart)
								continue;
							JSONObject pfEndOb = pfMapInfo.getJSONObject("end");
							int pfEnd = pfEndOb.getInt("residue_number");
							if (residueIndex > pfEnd)
								continue;
							else {
								if (pfMapFlag == 0) {
									pfStartsTemp = pfStartsTemp + String.valueOf(pfStart);
									pfamEnds = pfamEnds + String.valueOf(pfEnd);
									pfMapFlag = 1;
								} else {
									pfStartsTemp = pfStartsTemp + "," + String.valueOf(pfStart);
									pfEndsTemp = pfEndsTemp + "," + String.valueOf(pfEnd);
								}
							}
						}
						if (pfMapFlag == 1) {
							if (pfFlag == 0) {
								pfamStarts = pfamStarts + pfStartsTemp;
								pfamEnds = pfamEnds + pfEndsTemp;
								pfamIds = pfamIds + pfId;
								pfamNames = pfamNames + pfName;
								pfamDescriptions = pfamDescriptions + pfDes;
								pfamIdentifiers = pfamIdentifiers + pfIdent;
							} else {
								pfamStarts = pfamStarts + ";" + pfStartsTemp;
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

					JSONObject interPro = name.getJSONObject("InterPro");
					Iterator iPIt = interPro.keySet().iterator();
					int iPFlag = 0;
					while (iPIt.hasNext()) {
						int iPMapFlag = 0;
						String iPId = iPIt.next().toString();
						JSONObject iPOb = interPro.getJSONObject(iPId);
						String iPName = su.toSQLstring(iPOb.getString("name"));
						String iPIdent = su.toSQLstring(iPOb.getString("identifier"));
						JSONArray iPMaps = iPOb.getJSONArray("mappings");
						String interproStartsTemp = "";
						String interproEndsTemp = "";
						for (int i = 0; i < iPMaps.length(); i++) {
							JSONObject iPMapInfo = iPMaps.getJSONObject(i);
							String iPChain = iPMapInfo.getString("chain_id");
							if (!pdbChain.equals(iPChain))
								continue;
							JSONObject iPStartOb = iPMapInfo.getJSONObject("start");
							int iPStart = iPStartOb.getInt("residue_number");
							if (residueIndex < iPStart)
								continue;
							JSONObject iPEndOb = iPMapInfo.getJSONObject("end");
							int iPEnd = iPEndOb.getInt("residue_number");
							if (residueIndex > iPEnd)
								continue;
							else {
								if (iPMapFlag == 0) {
									interproStartsTemp = interproStartsTemp + String.valueOf(iPStart);
									interproEndsTemp = interproEndsTemp + String.valueOf(iPEnd);
									iPMapFlag = 1;
								} else {
									interproStartsTemp = interproStartsTemp + "," + String.valueOf(iPStart);
									interproEndsTemp = interproEndsTemp + "," + String.valueOf(iPEnd);
								}
							}
						}
						if (iPMapFlag == 1) {
							if (iPFlag == 0) {
								interproStarts = interproStarts + interproStartsTemp;
								interproEnds = interproEnds + interproEndsTemp;
								interproIds = interproIds + iPId;
								interproNames = interproNames + iPName;
								interproIdentifiers = interproIdentifiers + iPIdent;
								iPFlag = 1;
							} else {
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
					
					pdbContentMp.put(checkStr, sar);
				}
			}
		} catch (Exception ex) {
			log.error(ex);
		}
    }
        
        

    /**
     * Get structure domains URL
     * 
     * @param domainLines
     * @param pdbId
     * @param pdbContentMp
     */
	public void getStructureDomainInfo(List<String> domainLines, String pdbId,
			SortedMap<String, StructureAnnotationRecord> pdbContentMp) {
		// get cath and scop info from structure_domains


		try {
			// String checkStr = currentStr.substring(8, 9) + "_" + currentStr.substring(9,
			// 13).trim();
			for (String checkStr : pdbContentMp.keySet()) {
				String pdbChain = checkStr.split("_")[0];
				String pdbResidueIndex = checkStr.split("_")[1];
				int residueIndex = Integer.parseInt(pdbResidueIndex);
				StructureAnnotationRecord sar = pdbContentMp.get(checkStr);
				//placeholder
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
				

				for (String domainStr : domainLines) {
					JSONObject jso = new JSONObject(domainStr);
					JSONObject name = jso.getJSONObject(pdbId);
					JSONObject cath = name.getJSONObject("CATH");
					Iterator caIt = cath.keySet().iterator();
					int caFlag = 0;
					while (caIt.hasNext()) {
						int caMapFlag = 0;
						String caId = caIt.next().toString();
						// log.info(caId);
						JSONObject caOb = cath.getJSONObject(caId);
						String caName = su.toSQLstring(caOb.getString("name"));
						String caArchitecture = su.toSQLstring(caOb.getString("architecture"));
						String caIdent = su.toSQLstring(caOb.getString("identifier"));
						String caClass = su.toSQLstring(caOb.getString("class"));
						String caHomology = su.toSQLstring(caOb.getString("homology"));
						String caTopology = su.toSQLstring(caOb.getString("topology"));
						// log.info(caName+caArchitecture+caIdent+caClass+caHomology+caTopology);
						JSONArray caMaps = caOb.getJSONArray("mappings");
						String caStartsTemp = "";
						String caEndsTemp = "";
						String caDomainTemp = "";
						for (int i = 0; i < caMaps.length(); i++) {
							JSONObject caMapInfo = caMaps.getJSONObject(i);
							String caChain = caMapInfo.getString("chain_id");
							String caDomianId = caMapInfo.getString("domain");
							if (!pdbChain.equals(caChain))
								continue;
							JSONObject caStartOb = caMapInfo.getJSONObject("start");
							int caStart = caStartOb.getInt("residue_number");
							if (residueIndex < caStart)
								continue;
							JSONObject caEndOb = caMapInfo.getJSONObject("end");
							int caEnd = caEndOb.getInt("residue_number");
							if (residueIndex > caEnd)
								continue;
							else {
								if (caMapFlag == 0) {
									caStartsTemp = caStartsTemp + String.valueOf(caStart);
									caEndsTemp = caEndsTemp + String.valueOf(caEnd);
									caDomainTemp = caDomainTemp + caDomianId;
									caMapFlag = 1;
								} else {
									caStartsTemp = caStartsTemp + "," + String.valueOf(caStart);
									caEndsTemp = caEndsTemp + "," + String.valueOf(caEnd);
									caDomainTemp = caDomainTemp + "," + caDomianId;
								}
							}
						}
						if (caMapFlag == 1) {
							if (caFlag == 0) {
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
							} else {
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

					JSONObject scop = name.getJSONObject("SCOP");
					Iterator scIt = scop.keySet().iterator();
					int scFlag = 0;
					while (scIt.hasNext()) {
						int scMapFlag = 0;
						String scId = scIt.next().toString();
						// log.info(scId);
						JSONObject scOb = scop.getJSONObject(scId);
						String scDes = su.toSQLstring(scOb.getString("description"));
						String scSccs = su.toSQLstring(scOb.getString("sccs"));
						String scIdent = su.toSQLstring(scOb.getString("identifier"));
						JSONObject scClass = scOb.getJSONObject("class");
						int scClassSunid = scClass.getInt("sunid");
						String scClassDes = su.toSQLstring(scClass.getString("description"));
						JSONObject scFold = scOb.getJSONObject("fold");
						int scFoldSunid = scFold.getInt("sunid");
						String scFoldDes = su.toSQLstring(scFold.getString("description"));
						JSONObject scSF = scOb.getJSONObject("superfamily");
						int scSFSunid = scSF.getInt("sunid");
						String scSFDes = su.toSQLstring(scSF.getString("description"));
						// log.info(scDes+scSccs+scIdent+scClassSunid+scClassDes+scFoldSunid+scFoldDes+scSFSunid+scSFDes);
						JSONArray scMaps = scOb.getJSONArray("mappings");
						String scStartsTemp = "";
						String scEndsTemp = "";
						for (int i = 0; i < scMaps.length(); i++) {
							JSONObject scMapInfo = scMaps.getJSONObject(i);
							String scChain = scMapInfo.getString("chain_id");
							if (!scChain.equals(scChain))
								continue;
							JSONObject scStartOb = scMapInfo.getJSONObject("start");
							int scStart = scStartOb.getInt("residue_number");
							if (residueIndex < scStart)
								continue;
							JSONObject scEndOb = scMapInfo.getJSONObject("end");
							int scEnd = scEndOb.getInt("residue_number");
							if (residueIndex > scEnd)
								continue;
							else {
								if (scMapFlag == 0) {
									scStartsTemp = scStartsTemp + String.valueOf(scStart);
									scEndsTemp = scEndsTemp + String.valueOf(scEnd);
									scMapFlag = 1;
								} else {
									scStartsTemp = scStartsTemp + "," + String.valueOf(scStart);
									scEndsTemp = scEndsTemp + "," + String.valueOf(scEnd);
								}
							}
						}
						if (scMapFlag == 1) {
							if (scFlag == 0) {
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
							} else {
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

					pdbContentMp.put(checkStr, sar);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public int stringToInt(String str) {
        int pos = 0;
        int i = 0;
        while (str.charAt(i) == ' ') {
            i++;
        }
        pos = Integer.parseInt(str.substring(i, str.length()));
        return pos;
    }

    /**
     * Using naccess to generate results .rsa
     * 
     * @param mUsageRecord
     * @param HashSet<String> pdbSet
     * @param forceRunFlag   true:If File existed,rerun (Update)
     * 						false:If file not existed, not rerun (init) 
     * 
     */
    public void generateNaccessResults(MutationUsageRecord mUsageRecord, HashSet<String> pdbSet, boolean forceRunFlag) {
        HashMap<Integer, String> residueHm = mUsageRecord.getResidueHm();
        int count = 0;
        int totalSize = residueHm.size();
        for (int mutationId : residueHm.keySet()) {
            String pdbNo = residueHm.get(mutationId).split("_")[0];
            if (!pdbSet.contains(pdbNo)) {
                runNaccessFromLocal(pdbNo, forceRunFlag);
                pdbSet.add(pdbNo);
            }
            if (count % 10000 == 0) {
                log.info("Finish " + count + "th of total "+ totalSize+" in naccess");
            }
            count++;
        }
    }

    /**
     * Dealing with .rsa get results.
     * 
     * @param mUsageRecord
     * @param HashSet<String> pdbSet
     * @param forceRunFlag   true:If File existed,rerun (Update)
     * 						false:If file not existed, not rerun (init)
     * 
     */
    public void generateNaccessResultsBuried(MutationUsageRecord mUsageRecord, HashSet<String> pdbSet, boolean forceRunFlag) {
        HashMap<Integer, String> residueHm = mUsageRecord.getResidueHm();
        int count = 0;
        for (int mutationId : residueHm.keySet()) {
            String pdbNo = residueHm.get(mutationId).split("_")[0];
            if (!pdbSet.contains(pdbNo)) {
                generateBuriedAtomicFile(pdbNo, forceRunFlag);
                pdbSet.add(pdbNo);
            }
            if (count % 100000 == 0) {
                log.info("Finish " + count + "th mutation in rsa in naccess to showburied");
            }
            count++;
        }
        
        //save HashSet<String>: pdb as pdbSet.ser
        String filename = ReadConfig.workspace + ReadConfig.pdbSetFile;
        try{
        	log.info("Serialize pdbSet, size is "+pdbSet.size());
            FileUtils.writeByteArrayToFile(new File(filename), SerializationUtils.serialize(pdbSet));
            log.info("Serialization completed");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * run Naccess on a pdb file
     * 
     * @param input
     *            pdbfile ep. 2acf.pdb
     * @param forceRunFlag
     * 				true: rerun
     * 				false: not rerun if the file exisited
     * @return .asa .log .rsa
     */
    public int runNaccessFromLocal(String pdbNo, boolean forceRunFlag) {
        int shellReturnCode = 0;
        try {
            CommandProcessUtil cu = new CommandProcessUtil();
            ArrayList<String> paralist = new ArrayList<String>();
            String inputFilename = ReadConfig.tmpdir + pdbNo + ".pdb";
          
            //if the file is there, avoid IO
            if( !forceRunFlag && new File(inputFilename).exists()) {
            	
            }else {
            	if (Boolean.parseBoolean(ReadConfig.readLocalPDBinNaccess)) {
                    String foldername = pdbNo.substring(1, 3).toLowerCase();
                    String pdburl = ReadConfig.pdbRepo + foldername + "/pdb" + pdbNo.toLowerCase() + ".ent.gz";
                    paralist = new ArrayList<String>();
                    paralist.add(pdburl);
                    paralist.add(inputFilename);
                    cu.runCommand("gunzip", paralist);
                } else {
                    String pdburl = ReadConfig.pdbStructureService + pdbNo.toUpperCase() + ".pdb";
                    paralist = new ArrayList<String>();
                    paralist.add(pdburl);
                    paralist.add(inputFilename);
                    cu.runCommand("wget", paralist);
                }           	
            } 
            
            //if the file is there, avoid IO
            if(!forceRunFlag && new File(ReadConfig.tmpdir + pdbNo + ".asa").exists()) {
            	
            }else {
                paralist = new ArrayList<String>();
                paralist.add(inputFilename);
                paralist.add(ReadConfig.tmpdir);
                cu.runCommand("naccess", paralist);           	
            }
        } catch (Exception ex) {
            log.error("[SHELL] Fatal Error: Could not Successfully process command, exit the program now");
            log.error(ex.getMessage());
            ex.printStackTrace();
            // System.exit(0);
        }
        return shellReturnCode;
    }

    /**
     * find burried residue in .rsa file
     * 
     * @param input
     *            pdb.rsa
     * @return pdb.showburied
     */
    public void generateBuriedAtomicFile(String pdbNo, boolean forceRunFlag) {
        try {
            String rsafilepwd = new String(ReadConfig.tmpdir + pdbNo + ".rsa");
            File rsafile = new File(rsafilepwd);
            String resfilepwd = new String(ReadConfig.tmpdir + pdbNo + ".showburied");
            File resfile = new File(resfilepwd);
            // Force rerun or not
            if( !forceRunFlag && resfile.exists()) {
            	
            }else {
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
                            lines.set(i, lines.get(i) + "\t1");
                        } else {
                            lines.set(i, lines.get(i) + "\t0");
                        }
                        // if(lines.get(i).split("\\s+")[2].equals("A")) {
                        // sum1 += Float.parseFloat(lines.get(i).split("\\s+")[4]);
                        // sum2 += Float.parseFloat(lines.get(i).split("\\s+")[5]);
                        // }
                    }
                    if (lines.get(i).split("\\s+")[0].equals("REM") && lines.get(i).split("\\s+")[1].equals("ABS")) {
                        lines.set(i, lines.get(i) + "\tIFBURIED");
                    } else
                        continue;
                }
                // log.info(sum1 + " " + sum2);
                FileUtils.writeLines(resfile, StandardCharsets.UTF_8.name(), lines);            	
            }
            
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
            while (caIt.hasNext()) {
                String caId = caIt.next().toString();
                // log.info(caId);
                JSONObject caOb = cath.getJSONObject(caId);
                String caName = caOb.getString("name");
                String caArchitecture = caOb.getString("architecture");
                String caIdent = caOb.getString("identifier");
                String caClass = caOb.getString("class");
                String caHomology = caOb.getString("homology");
                String caTopology = caOb.getString("topology");
                // log.info(caName+caArchitecture+caIdent+caClass+caHomology+caTopology);
                JSONArray caMaps = caOb.getJSONArray("mappings");
                for (int i = 0; i < caMaps.length(); i++) {
                    JSONObject caMapInfo = caMaps.getJSONObject(i);
                    String caChain = caMapInfo.getString("chain_id");
                    String caDomianId = caMapInfo.getString("domain");
                    if (!pdbChain.equals(caChain))
                        continue;
                    JSONObject caStartOb = caMapInfo.getJSONObject("start");
                    int caStart = caStartOb.getInt("residue_number");
                    if (residueIndex < caStart)
                        continue;
                    JSONObject caEndOb = caMapInfo.getJSONObject("end");
                    int caEnd = caEndOb.getInt("residue_number");
                    if (residueIndex > caEnd)
                        continue;
                    else {

                    }
                }
            }
            JSONObject scop = name.getJSONObject("SCOP");
            Iterator scIt = scop.keySet().iterator();
            while (scIt.hasNext()) {
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
                log.info(scDes + scSccs + scIdent + scClassSunid + scClassDes + scFoldSunid + scFoldDes + scSFSunid
                        + scSFDes);
                JSONArray scMaps = scOb.getJSONArray("mappings");
                for (int i = 0; i < scMaps.length(); i++) {
                    JSONObject scMapInfo = scMaps.getJSONObject(i);
                    String scChain = scMapInfo.getString("chain_id");
                    if (!scChain.equals(scChain))
                        continue;
                    JSONObject scStartOb = scMapInfo.getJSONObject("start");
                    int scStart = scStartOb.getInt("residue_number");
                    if (residueIndex < scStart)
                        continue;
                    JSONObject scEndOb = scMapInfo.getJSONObject("end");
                    int scEnd = scEndOb.getInt("residue_number");
                    if (residueIndex > scEnd)
                        continue;
                    else {

                    }
                }
            }
        }
    }
    
    /**
     * Cath Information as hashmap
     * @return
     */
    public HashMap<String, List<String>> readCathAllFile(){
    	HashMap<String, List<String>> hm = new HashMap<>();
		String cathpwd = new String(ReadConfig.workspace + ReadConfig.cathFile);
		try {
			File cathfile = new File(cathpwd);
			List<String> lines = FileUtils.readLines(cathfile, StandardCharsets.UTF_8.name());
			for(int i=0; i<lines.size(); i++) {
				String key = lines.get(i).substring(0, 4);
				List<String> val = new ArrayList<>();
				if(!hm.containsKey(key)) {
					val.add(lines.get(i).substring(4));
					hm.put(key, val);
				}
				else {
					val = hm.get(key);
					val.add(lines.get(i).substring(4));
					hm.put(key, val);
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}		
    	return hm;
    }
    
    /**
     * Cath Information as hashmap
     * @return
     */
    public HashMap<String, String> readCathNamesFile() {
    	HashMap<String, String> hm = new HashMap<>();
		String cathNamespwd = new String(ReadConfig.workspace + ReadConfig.cathNamesFile);
		try {
			File cathNamesfile = new File(cathNamespwd);
			List<String> lines = FileUtils.readLines(cathNamesfile, StandardCharsets.UTF_8.name());
			for(int i=0; i<lines.size(); i++) {
				String key = lines.get(i).split(" ", 2)[0];
				String val = lines.get(i).split(" ", 2)[1];
				hm.put(key, val);
			}			
		}catch(Exception ex) {
			ex.printStackTrace();
		}		
    	return hm;
    }
    
    /**
     * Cath information
     * 
     * @param sar
     * @param pdbId
     * @param pdbChain
     * @param pdbResidueIndex
     * @throws Exception
     */
    public void getCathInfo(StructureAnnotationRecord sar, String pdbId, String pdbChain, String pdbResidueIndex, HashMap<String, List<String>> cathLines, HashMap<String, String> cathNamesLines) {
    	String cathIds = "";
        String cathIdentifiers = "";
        String cathArchitectures = "";
        String cathClasses = "";
        String cathHomologys = "";
        String cathTopologys = "";
        String cathDomains = "";
        String cathStarts = "";
        String cathEnds = "";
    	try {
    		//log.info(pdbId + "+" + pdbChain + "+" + pdbResidueIndex);
    		if(cathLines.containsKey(pdbId)) {
    			for(int i=0; i<cathLines.get(pdbId).size(); i++) {
    				if(cathLines.get(pdbId).get(i).substring(0, 1).equals(pdbChain)) {
		    			String cathIdTemp = "";
		    			String homoTemp = "";
		    			String mapInfo = cathLines.get(pdbId).get(i).split(" ")[3];
		    			int sectionLen = mapInfo.split(",").length;
		    			for(int j=0; j<sectionLen; j++) {
		    				String startEnd = mapInfo.split(",")[j].split(":")[0];
		    				int startEndLen = startEnd.split("-").length;
		    				String start = startEnd.split("-")[startEndLen-2];
		    				String end = startEnd.split("-")[startEndLen-1];
		    				start = start.replaceAll("[^\\d]", ""); //keep only numbers
		    				end = end.replaceAll("[^\\d]", ""); //keep only numbers
		    				String domain = pdbId + cathLines.get(pdbId).get(i).split(" ")[0];
		    				if(Integer.parseInt(pdbResidueIndex)>=Integer.parseInt(start) && Integer.parseInt(pdbResidueIndex)<=Integer.parseInt(end)) {
		    					cathIdTemp = cathLines.get(pdbId).get(i).split(" ")[2];
		    					cathIds = cathIds + cathIdTemp + ";";
		    					cathStarts = cathStarts + start + ";";
		    					cathEnds = cathEnds + end + ";";   					
		    					cathDomains = cathDomains + domain +";";
		    					break;
		    				}
		    			}
		    			if(!cathIdTemp.equals("")) {
		    				//log.info(cathIdTemp);
		    				String str1 = cathIdTemp.split("\\.")[0];
		    				String str2 = cathIdTemp.split("\\.")[0] + "." + cathIdTemp.split("\\.")[1];
		    				String str3 = cathIdTemp.split("\\.")[0] + "." + cathIdTemp.split("\\.")[1] + "." + cathIdTemp.split("\\.")[2];
		    				String str4 = cathIdTemp;
		    				if(cathNamesLines.containsKey(str1)) {
		    					cathClasses = cathClasses + cathNamesLines.get(str1) + ";";
		    				}
		    				if(cathNamesLines.containsKey(str2)) {
		    					cathArchitectures = cathArchitectures + cathNamesLines.get(str2) + ";";
		    				}
		    				if(cathNamesLines.containsKey(str3)) {
		    					cathIdentifiers = cathIdentifiers + cathNamesLines.get(str3) + ";";
		    					cathTopologys = cathTopologys + cathNamesLines.get(str3) + ";";
		    					homoTemp = cathNamesLines.get(str3);
		    				}
		    				if(cathNamesLines.containsKey(str4)) {
		    					if(!cathNamesLines.get(str4).equals("")) {
		    						homoTemp = cathNamesLines.get(str4);
		    					}
		    					cathHomologys = cathHomologys + homoTemp + ";";
		    				}
		    			}
		    		}
	    		}
    		}
    		if(!cathIds.equals("")) {
    			cathIds = cathIds.substring(0, cathIds.length()-1);
    			cathIdentifiers = cathIdentifiers.substring(0, cathIdentifiers.length()-1);
    			cathArchitectures = cathArchitectures.substring(0, cathArchitectures.length()-1);
    			cathClasses = cathClasses.substring(0, cathClasses.length()-1);
    			cathHomologys = cathHomologys.substring(0, cathHomologys.length()-1);
    			cathTopologys = cathTopologys.substring(0, cathTopologys.length()-1);
    			cathDomains = cathDomains.substring(0, cathDomains.length()-1);
    			cathStarts = cathStarts.substring(0, cathStarts.length()-1);
    			cathEnds = cathEnds.substring(0, cathEnds.length()-1);
    		}
    		sar.setCathId(cathIds);
            sar.setCathIdentifier(cathIdentifiers);
            sar.setCathArchitecture(cathArchitectures);
            sar.setCathClass(cathClasses);
            sar.setCathHomology(cathHomologys);
            sar.setCathTopology(cathTopologys);
            sar.setCathDomainStart(cathStarts);
            sar.setCathDomainEnd(cathEnds);
            sar.setCathDomainId(cathDomains);
            //log.info(pdbId + "+" + pdbChain + "+" + pdbResidueIndex + "+" + cathIds + "+" + cathIdentifiers + "+" + cathArchitectures + "+");
            //log.info(cathClasses + "+" + cathHomologys + "+" + cathTopologys + "+" + cathStarts + "+" + cathEnds + "+" + cathDomains);
    	}catch(Exception ex) {
    		log.error("Error in: "+pdbId + "+" + pdbChain + "+" + pdbResidueIndex);
    		ex.printStackTrace();
    	}
    }
}
