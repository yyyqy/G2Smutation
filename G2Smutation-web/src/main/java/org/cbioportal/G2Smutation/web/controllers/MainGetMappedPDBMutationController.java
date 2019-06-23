package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.domain.StructureAnnotationRepository;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.cbioportal.G2Smutation.web.models.mutation.Mutation;
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Main controller get genomics Mutation: Get Mutations
 * 
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Structure Mutations from PDB", description = "PDB")
@RequestMapping(value = "/api/")
public class MainGetMappedPDBMutationController {
	
	final static Logger log = Logger.getLogger(MainGetMappedPDBMutationController.class);

	@Autowired
	private SeqIdAlignmentController seqController;
	
	@Autowired
    private StructureAnnotationRepository structureAnnotationRepository;
	
	@RequestMapping(value = "/pdbMutation/{pdb}/{chain:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by variant")
	public List<StructureAnnotation> postVariantMutation(
			@ApiParam(required = true, value = "PDB: e.g. 2pcx") @PathVariable String pdb,
			@ApiParam(required = true, value = "Chain: e.g. A") @PathVariable String chain,
			@ApiParam(required = false, value = "Input id: e.g. 202,282") @RequestParam(required = false) List<String> positionList) {

		List<StructureAnnotation> outList = new ArrayList<StructureAnnotation>();
		
		String inputStr = pdb+"_"+chain+"_";
		System.out.println(inputStr);
		
		if (positionList == null) {
			List<StructureAnnotation> tmpList = structureAnnotationRepository.findByPdbNoStartingWith(inputStr);
			Set<String> tmpSet = new HashSet<>();
			for(StructureAnnotation sa: tmpList) {
				if(tmpSet.contains(sa.getPdbNo())) {
					
				}else {
					tmpSet.add(sa.getPdbNo());
					outList.add(sa);
				}
			}
        } else {
        	List<StructureAnnotation> tmpList = structureAnnotationRepository.findByPdbNoStartingWith(inputStr);
			Set<String> tmpSet = new HashSet<>();
			for(StructureAnnotation sa: tmpList) {
				if(tmpSet.contains(sa.getPdbNo())) {
					
				}else {
					tmpSet.add(sa.getPdbNo());
					String posStr = sa.getPdbNo().split("_")[2];
					for(String pos: positionList) {
						if(posStr.equals(pos)) {							
							outList.add(sa);						
						}
					}
				}
			}
        }		
		return outList;
	}
}
