package org.cbioportal.G2Smutation.web.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Main controller get ResidueMutation: Get Mutations
 * 
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Get Mutation", description = "mutationb in ensembl/uniprot/hgvs/sequences")
@RequestMapping(value = "/api/")
public class MainGetMutation {
    final static Logger log = Logger.getLogger(MainGetMutation.class);
    
    @RequestMapping(value = "/mutation/{id_type}/{id:.+}", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("POST mutation by ProteinId")
    public List<Alignment> postMutation(
            @ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
                    + "uniprot_isoform;\n hgvs-grch37; hgvs-grch38; dbsnp") @PathVariable String id_type,
            @ApiParam(required = true, value = "Input id e.g.\n"
                    + "ensembl:ENSP00000484409.1/ENSG00000141510.16/ENST00000504290.5;\n"
                    + "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_9/P53_HUMAN_9;\n"
                    + "hgvs-grch37:17:g.79478130C>G;\n" + "hgvs-grch38:17:g.7676594T>G;\n"
                    + "dbsnp:rs1800369") @PathVariable String id,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,100; Anynumber for hgvs;\n"
                    + "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {
        
        return null;
    }

}

