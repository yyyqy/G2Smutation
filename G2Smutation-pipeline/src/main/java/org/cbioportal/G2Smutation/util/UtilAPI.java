package org.cbioportal.G2Smutation.util;

import org.springframework.web.client.RestTemplate;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineMakeSQL;
import org.cbioportal.G2Smutation.util.models.api.Mappings;
import org.cbioportal.G2Smutation.util.models.api.QuoteCoor;

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
        ReadConfig rc = ReadConfig.getInstance();
        String outstr = "";

        String url = rc.getProtein2GenomicURL();
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

}
