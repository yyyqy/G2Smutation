package org.cbioportal.G2Smutation.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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

}
