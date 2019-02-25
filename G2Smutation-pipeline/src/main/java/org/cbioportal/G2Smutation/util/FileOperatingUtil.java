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
     *  (2)HashMap<Integer, String> residueHm,
     *          key:MUTATION_ID, value:XXXX_Chain_INDEX
     * 
     */
    public MutationUsageRecord readMutationResult2MutationUsageRecord(String inputFilename) {
        MutationUsageRecord mur = new MutationUsageRecord();
        HashMap<String, List<Integer>> genomicCoorHm = new HashMap<>();
        HashMap<String, String> mutationHm = new HashMap<>();// save time for
                                                             // calling API
        HashMap<Integer, String> mutationIdHm = new HashMap<>();
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
                //Pattern pattern = Pattern.compile("ENSP\\d+\\.\\d");
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
                            gpos = uapi.callAPICoor(proteinName, proteinIndex);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            count++;
                            continue;
                        }
                        mutationHm.put(mutationNO, gpos);
                        mutationList = new ArrayList<Integer>();
                        mutationList.add(mutationID);
                    }
                    mutationIdHm.put(mutationID, gpos);
                    genomicCoorHm.put(gpos, mutationList);

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
        mur.setMutationIdHm(mutationIdHm);
        mur.setResidueHm(residueHm);
        return mur;
    }

}
