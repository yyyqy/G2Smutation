package org.cbioportal.G2Smutation.util;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineMakeSQL;
import org.cbioportal.G2Smutation.util.models.NucleotideType;
import org.cbioportal.G2Smutation.util.models.api.Mappings;
import org.cbioportal.G2Smutation.util.models.api.QuoteCoor;
import org.cbioportal.G2Smutation.util.models.api.QuotePro;
import org.cbioportal.G2Smutation.util.models.api.Transcript_consequences;


/**
 * All API calling
 * 
 * @author Juexin Wang
 *
 */
public class UtilAPI {
    final static Logger log = Logger.getLogger(UtilAPI.class);

    /**
     * Call Genomic cooridinate in GRCh37 from EVP
     * https://grch37.rest.ensembl.org/map/translation/ENSP00000356671.3/167..
     * 167?content-type=application/json
     * 
     * @param proteinName
     *            (ENSP00000356671.3)
     * @param proteinIndex
     *            (167)
     * @return
     * @throws Exception
     */
    public String callAPICoor(String proteinName, String proteinIndex) throws Exception {
        
        String outstr = "";

        String url = ReadConfig.getProtein2GenomicURL();
        url = url.replace("ENSPID", proteinName);
        url = url.replace("LOCATION", proteinIndex);
        //log.info(url);

        RestTemplate restTemplate = new RestTemplate();
        
        QuoteCoor quote = restTemplate.getForObject(url, QuoteCoor.class);
        List<Mappings> mappings = quote.getMappings();

        for (Mappings mapping : mappings) {
            if (mapping.getAssembly_name().equals("GRCh37")) {
                outstr = mapping.getSeq_region_name() + "_" + mapping.getStart() + "_" + mapping.getEnd();
            } else {
                System.out.println("Not GRCh37 in " + mapping + ", Check!");
            }
        }

        return outstr;
    }
    

    /**
     * Call url, input chr pos, and protein seqId_index
     * 
     * http://annotation.genomenexus.org/hgvs/17:g.79478130C%3ET?isoformOverrideSource=uniprot&summary=summary
     * 
     * @param en2SeqHm <ensemblName,seqId>
     * @param gpos2proHm <chr_pos,seqId_startindex>
     * @param gpos  chr_pos
     * @throws Exception
     */
    public void callgpos2ensemblAPI(HashMap<String,Integer> en2SeqHm, HashMap<String,String> gpos2proHm, String gpos) throws Exception {
    	
    	String chromosomeNum = gpos.split("_")[0];
		String position = gpos.split("_")[1];

        String url = ReadConfig.getGnApiGnUrl();
        url = url.replace("CHROMSOME", chromosomeNum);
        url = url.replace("POSITION", position);
        for (NucleotideType val: NucleotideType.values()){
        	url = url.replace("ORIGINAL", val.toString());
        	// Artificial mutation for API
            String mutation = "A";
            if (val.toString().equals("A")) {
                mutation = "T";
            }
        	url = url.replace("MUTATION", mutation);
        	
        	RestTemplate restTemplate = new RestTemplate();            
            QuotePro quote = restTemplate.getForObject(url, QuotePro.class);
            if (quote.toString() !=""){            	
            	List<Transcript_consequences> list = quote.getTranscript_consequences();
                
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProtein_start() != 0) {
                    	String ensp = list.get(i).getProtein_id();
                    	if (en2SeqHm.containsKey(ensp)){
                    		int seqId = en2SeqHm.get(ensp);
                        	int protein_index = list.get(i).getProtein_start();
                        	String mutation_NO = Integer.toString(seqId) + "_" + Integer.toString(protein_index);
                        	gpos2proHm.put(gpos, mutation_NO);               		
                    	}else{
                    		//log.info(ensp + " does not included in the system");
                    	}
                    }
                }
                break;// if the nucleotide is correct, then does not need to run API again.           	
            }
                   	
        }
                
    }

}
