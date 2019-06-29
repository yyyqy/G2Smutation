package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.domain.ClinvarRepository;
import org.cbioportal.G2Smutation.web.domain.CosmicRepository;
import org.cbioportal.G2Smutation.web.domain.DbsnpRepository;
import org.cbioportal.G2Smutation.web.models.mutation.Mutation;
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@Api(tags = "Structure Mutations from Variance", description = "dbSNP/clinVar/COSMIC")
@RequestMapping(value = "/api/")
public class MainGetMappedVarianceController {
	
	final static Logger log = Logger.getLogger(MainGetMappedVarianceController.class);

	@Autowired
	private SeqIdAlignmentController seqController;
	
	@Autowired
	private ClinvarRepository clinvarRepository;
	
	@Autowired
	private DbsnpRepository dbsnpRepository;
	
	@Autowired
	private CosmicRepository cosmicRepository;

	@RequestMapping(value = "/variantMutation/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by variant")
	public List<Mutation> postVariantMutation(
			@ApiParam(required = true, value = "Input id_type: dbsnp/clinvar/cosmic") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id: e.g. rs28934574/140821/COSM1636702") @PathVariable String id) {

		List<Mutation> outList = new ArrayList<Mutation>();
		List<String> mutationNoList = new ArrayList<>();
		
		if(id_type.equals("dbsnp")) {
			/**
			 * Option 1: use API
			 * 
			 * // http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary
			outList.addAll(seqController.getMutationUsageByEnsemblIddbSNPID(id));
			 */
			
			/**
			 * Option 2: use inner database
			 */
			if (id.startsWith("rs")) {
				mutationNoList = dbsnpRepository.findMutationNoByRsId(id.split("rs")[1]);								
			}
								
		}else if(id_type.equals("clinvar")) {
			mutationNoList = clinvarRepository.findMutationNoByClinvarId(id);
		}else if(id_type.equals("cosmic")) {
			if (id.startsWith("COSM")) {
				mutationNoList = cosmicRepository.findMutationNoByCosmicMutationId(id.split("COSM")[1]);				
			}			
		}else {
			log.error("Does not support others");
		}
		
		//get mutationNoList, then get results
		for(String mutationNo: mutationNoList) {
			String[] pdbArrays = mutationNo.split("_");//60004_282
			List<String> posList = new ArrayList<String>();
			posList.add(pdbArrays[1]);
			outList.addAll(seqController.getMutationUsageBySeqId(pdbArrays[0], posList));
		}
		
		return outList;
	}

	@RequestMapping(value = "/variantMutationAnnotation/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by variant")
	public List<MutationAnnotation> postVariantMutationAnnotation(
			@ApiParam(required = true, value = "Input id_type: dbsnp/clinvar") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id: e.g. rs28934574/140821") @PathVariable String id) {

		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();		
		List<String> mutationNoList = new ArrayList<>();
		
		if(id_type.equals("dbsnp")) {
			/**
			 * Option 1: use API
			 * 
			 * // http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary
			outList.addAll(seqController.getMutationUsageAnnotationByEnsemblIddbSNPID(id));
			 */
			
			/**
			 * Option 2: use inner database
			 */
			if (id.startsWith("rs")) {
				mutationNoList = dbsnpRepository.findMutationNoByRsId(id.split("rs")[1]);				
			}
								
		}else if(id_type.equals("clinvar")) {
			mutationNoList = clinvarRepository.findMutationNoByClinvarId(id);
			
		}else if(id_type.equals("cosmic")) {
			if (id.startsWith("COSM")) {
				mutationNoList = cosmicRepository.findMutationNoByCosmicMutationId(id.split("COSM")[1]);								
			}			
		}else {
			log.error("Does not support others");
		}
		
		//get mutationNoList, then get results
		for(String mutationNo: mutationNoList) {
			String[] pdbArrays = mutationNo.split("_");//60004_282
			List<String> posList = new ArrayList<String>();
			posList.add(pdbArrays[1]);
			outList.addAll(seqController.getMutationUsageAnnotationBySeqId(pdbArrays[0], posList));
		}
		
		
		return outList;
	}

}
