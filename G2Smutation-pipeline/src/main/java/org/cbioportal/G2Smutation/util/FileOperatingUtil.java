package org.cbioportal.G2Smutation.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.util.models.MutationUsageRecord;

/**
 * Read mutation_result.txt etc, and output results
 * 
 * @author Juexin Wang
 *
 */
public class FileOperatingUtil {
    final static Logger log = Logger.getLogger(FileOperatingUtil.class);

    /**
     * parse mutation result from inputFile and generate MutationUsageRecord
     * 
     * @param inputFilename
     * @return MutationUsageRecord contains 
     *  (1)HashMap<String, String> mutationIdHm, 
     *          key:MUTATION_ID, value: chr_start_end
     *  (2)HashMap<String, List<Integer>> mutationIdRHm,
     *          key:chr_pos, value: List of mutationId
     *  (3)HashMap<Integer, String> residueHm,
     *          key:MUTATION_ID, value:XXXX_Chain_INDEX
     * 
     */
    public MutationUsageRecord readMutationResult2MutationUsageRecord(String inputFilename) {
        MutationUsageRecord mur = new MutationUsageRecord();
        HashMap<String, List<Integer>> genomicCoorHm = new HashMap<>();
        HashMap<String, String> mutationHm = new HashMap<>();// save time for
                                                             // calling API
        HashMap<Integer, String> mutationIdHm = new HashMap<>();
        HashMap<String, List<Integer>> mutationIdRHm = new HashMap<>();
        HashMap<Integer, String> residueHm = new HashMap<>();

        try {
            UtilAPI uapi = new UtilAPI();
            List<String> contentList = FileUtils.readLines(new File(inputFilename));
            int count = 0;
            for (String content : contentList) {
                if (count == 0) {// deal with the header
                    count++;
                    continue;
                }
                String[] strArray = content.split("\t");
                int mutationID = Integer.parseInt(strArray[0]);
                String mutationNO = strArray[1];
                String proteinName = "";
                String proteinIndex = "";
                String gpos = "";
                List<Integer> mutationList;
                // Pattern pattern = Pattern.compile("ENSP\\d+\\.\\d");
                Pattern pattern = Pattern.compile("ENSP\\d+");
                Matcher matcher = pattern.matcher(strArray[3]);
                String[] tmpArray = strArray[6].split("_");
                residueHm.put(mutationID, tmpArray[0] + "_" + tmpArray[1] + "_" + strArray[7]);

                if (matcher.find()) {
                    proteinName = matcher.group(0);
                    // strArray[3]: "ANT3_HUMAN;ENSP00000356671.3
                    // ENSG00000117601.13 ENST00000367698.3" ->
                    // proteinName: ENSP00000404384.1
                    proteinIndex = strArray[4];
                    if (mutationHm.containsKey(mutationNO)) {
                        gpos = mutationHm.get(mutationNO);
                        mutationList = genomicCoorHm.get(gpos);
                        mutationList.add(mutationID);
                    } else {
                        // Calling GenomeNexus
                        // https://grch37.rest.ensembl.org/map/translation/ENSP00000356671.3/167..167?content-type=application/json
                        try {
                            log.info(count);
                            gpos = uapi.callAPICoor(proteinName, proteinIndex);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        mutationHm.put(mutationNO, gpos); //gpos could be ""
                        mutationList = new ArrayList<Integer>();
                        mutationList.add(mutationID);
                    }
                    mutationIdHm.put(mutationID, gpos); //gpos could be ""
                    genomicCoorHm.put(gpos, mutationList); //mutationList could be ""

                } else {
                    log.info("Not found ENSP in " + strArray[3]);
                    count++;
                    continue;
                }
                count++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Construct Reverse HashMap mutationIdRHm for mutationIdHm
        for (Integer mutationId: mutationIdHm.keySet()){
            String gpos = mutationIdHm.get(mutationId);
            if(!gpos.equals("")){
                System.out.println(mutationId+" "+gpos);
                //Corner Case:
                //HSCHR6_MHC_MCF_29989718_29989720
                //https://grch37.rest.ensembl.org/map/translation/ENSP00000403922.1/176..176?content-type=application/json
                String[] strArray = gpos.split("_");
                int start = Integer.parseInt(strArray[strArray.length-2]);
                int end = Integer.parseInt(strArray[strArray.length-1]);
                for (int i=start;i<=end;i++){
                    String chr_pos = "";
                    for(int j=0;j<strArray.length-2;j++){
                        chr_pos = chr_pos + strArray[j] + "_";
                    }
                    chr_pos = chr_pos+Integer.toString(i);
                    List<Integer> tmpList;
                    if(mutationIdRHm.containsKey(chr_pos)){
                        tmpList = mutationIdRHm.get(chr_pos);                                               
                    }else{
                        tmpList = new ArrayList<Integer>(); 
                    }
                    tmpList.add(mutationId);
                    mutationIdRHm.put(chr_pos, tmpList);                    
                }                
            }           
        }        
        mur.setMutationIdHm(mutationIdHm);
        mur.setMutationIdRHm(mutationIdRHm);
        mur.setResidueHm(residueHm);
        return mur;
    }
    
    /**
     * Convert clinvar contents line as string to HashMap
     * Using table clinvar_entry as the model
     * 
     * @param contentlineStr
     * @return
     */
    public HashMap<String,String> clinvarContentStr2Map(String contentlineStr){
    	HashMap<String,String> hm = new HashMap<>();
    	String[] strArray = contentlineStr.split(";");
    	for (String str : strArray){
    		String[] reArray = str.split("=");
    		hm.put(reArray[0], reArray[1]);
    	}    	
    	return hm;
    }
    
    /**
     * collect all SNP as HashMap
     * @param inputHm key:chr_pos value: dbsnp:12345;clinvar:54321;...
     * @param snpCollectionName
     * @return
     */
    public HashMap<String,String> collectAllSNPs2Map(HashMap<String,String> inputHm, SNPAnnotationType snpCollectionName){
    	try{  		
    		String inputFilename = "";   		
    		switch (snpCollectionName) {
            case DBSNP:
            	inputFilename = ReadConfig.workspace + ReadConfig.dbsnpFile;
            	inputHm = constructAnnotationHmWrapperDbsnp(inputHm, inputFilename,snpCollectionName.toString());
                break;
            case CLINVAR:
            	inputFilename = ReadConfig.workspace + ReadConfig.clinvarFile;
            	inputHm = constructAnnotationHmWrapperClinvar(inputHm, inputFilename,snpCollectionName.toString());
                break;
            case COSMIC:
            	inputFilename = ReadConfig.workspace + ReadConfig.cosmicFile;
            	inputHm = constructAnnotationHmWrapperCosmic(inputHm, inputFilename,snpCollectionName.toString());
                break;
            case GENIE:
            	inputFilename = ReadConfig.workspace + ReadConfig.genieFile;
            	inputHm = constructAnnotationHmWrapperGenieTcga(inputHm, inputFilename,snpCollectionName.toString(),1);
                break;
            case TCGA:
            	inputFilename = ReadConfig.workspace + ReadConfig.tcgaFile;
            	inputHm = constructAnnotationHmWrapperGenieTcga(inputHm, inputFilename,snpCollectionName.toString(),0);
                break;
            default:
                log.error("Now only supports DBSNP, CLINVAR, COSMIC, GENIE and TCGA");
                break;
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return inputHm;
    }
    
    /**
     * inner coding part, avoid redundancy
     * @param inputHm
     * @param gpos
     * @param snpCollectionName
     * @param value
     */
    void constructAnnotationHmWorker(HashMap<String, String> inputHm, String gpos, String snpCollectionName, String value ){    		
    	if(inputHm.containsKey(gpos)){
            String tmpstr = inputHm.get(gpos);  
            inputHm.put(gpos, tmpstr+";"+snpCollectionName+":"+value);
        }else{
        	inputHm.put(gpos, snpCollectionName+":"+value);
        }    	
    }
 
    /**
     * Add dbsnp locations into the hashmap
     * @param inputHm
     * @param inputFilename
     * @param snpCollectionName
     * @return
     */
    HashMap<String,String> constructAnnotationHmWrapperDbsnp(HashMap<String, String> inputHm, String inputFilename,String snpCollectionName){
    	try{
    		LineIterator it = FileUtils.lineIterator(new File(inputFilename));
    		int count = 0;
            while(it.hasNext()){
                String str = it.nextLine();
                String[] strArray = str.split("\t");
                String gpos = strArray[0]+"_"+strArray[1];
                constructAnnotationHmWorker(inputHm,gpos,snpCollectionName,strArray[2]);
                if(count%100000 == 0){
                	log.info(snpCollectionName + " line: " + count);
                }
                count++;
            }    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}   	
        return inputHm;
    }
    
    /**
     * Add clinvar locations into the hashmap
     * @param inputHm
     * @param inputFilename
     * @param snpCollectionName
     * @return
     */
    HashMap<String,String> constructAnnotationHmWrapperClinvar(HashMap<String, String> inputHm, String inputFilename,String snpCollectionName){
    	try{
    		LineIterator it = FileUtils.lineIterator(new File(inputFilename));
            int count =0;
    		while(it.hasNext()){
                String str = it.nextLine();
                String[] strArray = str.split("\t");
                if(!str.startsWith("#")){ 
                	String gpos = strArray[0]+"_"+strArray[1];
                	constructAnnotationHmWorker(inputHm,gpos,snpCollectionName,strArray[2]);                	
                }
                if(count%100000 == 0){
                	log.info(snpCollectionName + " line: " + count);
                }
                count++;
            }     		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}   	
        return inputHm;
    }
    
    /**
     * Add cosmic locations into the hashmap
     * @param inputHm
     * @param inputFilename
     * @param snpCollectionName
     * @return
     */
    HashMap<String,String> constructAnnotationHmWrapperCosmic(HashMap<String, String> inputHm, String inputFilename,String snpCollectionName){
    	try{
    		LineIterator it = FileUtils.lineIterator(new File(inputFilename));
        	int count = 0;
            while(it.hasNext()){
                String str = it.nextLine();
                String[] strArray = str.split("\t");
                if(count>0){
    				if (!strArray[23].equals("")) {
    					String[] tmpArray = strArray[23].split(":");
    					String[] posArray = tmpArray[1].split("-");
    					int start = Integer.parseInt(posArray[0]);
    					int end = Integer.parseInt(posArray[1]);
    					for (int i = start; i <= end; i++) {
    						String gpos = tmpArray[0] + "_" + Integer.toString(i);
    						constructAnnotationHmWorker(inputHm,gpos,snpCollectionName,strArray[16]);
    					}
    				}					
    			}
                if(count%100000 == 0){
                	log.info(snpCollectionName + " line: " + count);
                }
    			count++;
            }   		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}   	 
        return inputHm;
    }
    
    /**
     * Add Genie and Tcga locations into the hashmap
     * @param inputHm
     * @param inputFilename
     * @param snpCollectionName
     * @param dealLineNum
     * @return
     */
    HashMap<String,String> constructAnnotationHmWrapperGenieTcga(HashMap<String, String> inputHm, String inputFilename,String snpCollectionName, int dealLineNum){
    	try{
    		LineIterator it = FileUtils.lineIterator(new File(inputFilename));
    		int count = 0;
    		while (it.hasNext()) {
    			String contentStr = it.nextLine();
    			String[] strArray = contentStr.split("\t");
    			if(count>dealLineNum){
    				int start = Integer.parseInt(strArray[5]);
                    int end = Integer.parseInt(strArray[6]);
                    for (int i = start; i <= end; i++) {
                        String gpos = strArray[4] + "_" + Integer.toString(i);
                        constructAnnotationHmWorker(inputHm,gpos,snpCollectionName,gpos);
                    }					
    			}
    			if(count%100000 == 0){
                	log.info(snpCollectionName + " line: " + count);
                }
    			count++;				
    		}    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}    	
		return inputHm;
    }  

}
