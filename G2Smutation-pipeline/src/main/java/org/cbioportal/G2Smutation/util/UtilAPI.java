package org.cbioportal.G2Smutation.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineMakeSQL;
import org.cbioportal.G2Smutation.util.models.NucleotideType;
import org.cbioportal.G2Smutation.util.models.api.GenomePosition;
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
     * GET method
     * Call url, input chr pos, and protein seqId_index 
     * Slow if calling multiple times
     * 
     * http://annotation.genomenexus.org/hgvs/17:g.79478130C%3ET?isoformOverrideSource=uniprot&summary=summary
     * 
     * @param en2SeqHm <ensemblName,seqId>
     * @param gpos2proHm <chr_pos,seqId_startindex>
     * @param gpos  chr_pos
     * @throws Exception
     */
    public void callgpos2ensemblAPIGet(HashMap<String,Integer> en2SeqHm, HashMap<String,String> gpos2proHm, String gpos) throws Exception {
    	
    	String chromosomeNum = gpos.split("_")[0];
		String position = gpos.split("_")[1];
        
        for (NucleotideType val: NucleotideType.values()){
            String url = ReadConfig.getGnApiGnUrl();
            url = url.replace("CHROMSOME", chromosomeNum);
            url = url.replace("POSITION", position);
        	url = url.replace("ORIGINAL", val.toString());
        	// Artificial mutation for API
            String mutation = "A";
            if (val.toString().equals("A")) {
                mutation = "T";
            }
        	url = url.replace("MUTATION", mutation);
        	//System.out.println(url);        	
        	
        	try{
        	    RestTemplate restTemplate = new RestTemplate(); 
                QuotePro[] quote = restTemplate.getForObject(url, QuotePro[].class);
        	    List<Transcript_consequences> list = quote[0].getTranscript_consequences();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProtein_start() != 0) {
                        //System.out.println("((("+list.get(i));
                        String ensp = list.get(i).getProtein_id();
                        if (en2SeqHm.containsKey(ensp)){
                            int seqId = en2SeqHm.get(ensp);
                            int protein_index = list.get(i).getProtein_start();
                            String mutation_NO = Integer.toString(seqId) + "_" + Integer.toString(protein_index);
                            //System.out.println(ensp + "\t" + gpos + "\t" +mutation_NO);
                            gpos2proHm.put(gpos, mutation_NO);                      
                        }else{
                            //log.info(ensp + " does not included in the system");
                        }
                    }
                }
                break;
        	}catch(Exception ex){
        	    //Don't print for it is too much
        	    //ex.printStackTrace();
        	}                    	
        }
                
    }
    
    /**
     * POST method
     * Good for bulk calling
     * Call url, input chr pos, and protein seqId_index 
     * 
     * http://annotation.genomenexus.org/hgvs/17:g.79478130C%3ET?isoformOverrideSource=uniprot&summary=summary
     * 
     * @param en2SeqHm <ensemblName,seqId>
     * @param gpos2proHm <chr_pos,seqId_startindex>
     * @param gpos  chr_pos
     * @throws Exception
     */
    public void callgpos2ensemblAPIPost(HashMap<String, Integer> en2SeqHm, HashMap<String, String> gpos2proHm,
            List<String> gposList) throws Exception {

        String url = ReadConfig.getGnApiGnPostUrl();
        List<GenomePosition> gpList = new ArrayList<>();

        for (String gpos : gposList) {

            String chromosomeNum = gpos.split("_")[0];
            String position = gpos.split("_")[1];

            for (NucleotideType val : NucleotideType.values()) {
                GenomePosition gp = new GenomePosition();
                gp.setChromosome(chromosomeNum);
                gp.setStart(Integer.parseInt(position));
                gp.setEnd(Integer.parseInt(position));
                gp.setReferenceAllele(val.toString());
                // Artificial mutation for API
                String mutation = "A";
                if (val.toString().equals("A")) {
                    mutation = "T";
                }
                gp.setVariantAllele(mutation);
                gpList.add(gp);
                System.out.println(gp.toString());
            }
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            HttpHeaders headers = new HttpHeaders();  
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");
            HttpEntity<List<GenomePosition>> request = new HttpEntity<>(gpList, headers);
            QuotePro[] quoteArray = restTemplate.postForObject(uri, request, QuotePro[].class);
            for (QuotePro quote: quoteArray){
                String variant = quote.getVariant();
                String[] tmpArray = variant.split(":");
                int tmpl =  tmpArray[1].split(">")[0].length();
                String gpos = tmpArray[0]+"_"+tmpArray[1].substring(2, tmpl-2);
                List<Transcript_consequences> list = quote.getTranscript_consequences();
                System.out.println(quote.getVariant() + "Size:" + list.size());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProtein_start() != 0) {
                        System.out.println("((("+list.get(i));
                        String ensp = list.get(i).getProtein_id();
                        if (en2SeqHm.containsKey(ensp)) {
                            int seqId = en2SeqHm.get(ensp);
                            int protein_index = list.get(i).getProtein_start();
                            String mutation_NO = Integer.toString(seqId) + "_" + Integer.toString(protein_index);
                            System.out.println("&&&"+ensp + "\t" + gpos + "\t" + mutation_NO);
                            gpos2proHm.put(gpos, mutation_NO);
                        } else {
                            // log.info(ensp + " does not included in the
                            // system");
                        }
                    }
                }               
            }
        } catch (Exception ex) {
            // Don't print for it is too much
            // ex.printStackTrace();
        }

    }

}
