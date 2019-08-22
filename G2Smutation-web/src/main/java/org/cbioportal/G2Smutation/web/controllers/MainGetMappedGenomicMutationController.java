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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller get Mutation from genome position: Get Mutations
 * 
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Structure Mutations from Genomics", description = "GRCH37 Genome")
@RequestMapping(value = "/api/")
public class MainGetMappedGenomicMutationController {
	final static Logger log = Logger.getLogger(MainGetMappedGenomicMutationController.class);

	@Autowired
	private SeqIdAlignmentController seqController;

	@RequestMapping(value = "/genomicMutation/{chr}/{pos}/{wtNucleotide}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation by genomic location")
	public List<Mutation> postGenomicMutation(
			@ApiParam(required = true, value = "Chromsome(1-22,X,Y): e.g.17") @PathVariable String chr,
			@ApiParam(required = true, value = "Position: e.g. 7577094") @PathVariable long pos,
			@ApiParam(required = true, value = "WtNucleotide(A/T/G/C): e.g.G") @PathVariable String wtNucleotide) {

		List<Mutation> outList = new ArrayList<Mutation>();
		// http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

		String genomeVersion = "GRCH37";
		String chromosomeNum = chr;
		String nucleotideType = wtNucleotide;

		System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
		outList.addAll(
				seqController.getMutationUsageByEnsemblIdGenome(chromosomeNum, pos, nucleotideType, genomeVersion));
		return outList;
	}

	@RequestMapping(value = "/genomicMutationAnno/{chr}/{pos}/{wtNucleotide}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Query mutation annotation by genomic location")
	public List<MutationAnnotation> postGenomicMutationAnnotation(
			@ApiParam(required = true, value = "Chromsome(1-22,X,Y): e.g.17") @PathVariable String chr,
			@ApiParam(required = true, value = "Position: e.g. 7577094") @PathVariable long pos,
			@ApiParam(required = true, value = "WtNucleotide(A/T/G/C): e.g.G") @PathVariable String wtNucleotide) {

		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		// http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

		String genomeVersion = "GRCH37";
		String chromosomeNum = chr;
		String nucleotideType = wtNucleotide;

		System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
		outList.addAll(seqController.getMutationUsageAnnotationByEnsemblIdGenome(chromosomeNum, pos, nucleotideType,
				genomeVersion));
		return outList;
	}

}
