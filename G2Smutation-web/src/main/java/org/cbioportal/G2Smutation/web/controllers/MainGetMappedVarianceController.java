package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
@Api(tags = "Structure Mutations from Variance", description = "dbSNP")
@RequestMapping(value = "/api/")
public class MainGetMappedVarianceController {
	
	final static Logger log = Logger.getLogger(MainGetMappedVarianceController.class);

	@Autowired
	private SeqIdAlignmentController seqController;

	@RequestMapping(value = "/variantMutation/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by variant")
	public List<Mutation> postVariantMutation(
			@ApiParam(required = true, value = "Input id_type: dbsnp") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id: e.g. rs28934574") @PathVariable String id) {

		List<Mutation> outList = new ArrayList<Mutation>();
		// http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

		System.out.println("dbsnp: " + id);
		outList.addAll(seqController.getMutationUsageByEnsemblIddbSNPID(id));
		return outList;
	}

	@RequestMapping(value = "/variantMutationAnnotation/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by variant")
	public List<MutationAnnotation> postVariantMutationAnnotation(
			@ApiParam(required = true, value = "Input id_type: dbsnp") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id: e.g. rs28934574") @PathVariable String id) {

		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		// http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

		System.out.println("dbsnp: " + id);
		outList.addAll(seqController.getMutationUsageAnnotationByEnsemblIddbSNPID(id));
		return outList;
	}

}
