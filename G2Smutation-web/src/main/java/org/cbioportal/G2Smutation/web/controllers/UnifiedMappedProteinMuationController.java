package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.domain.AlignmentRepository;
import org.cbioportal.G2Smutation.web.domain.EnsemblRepository;
import org.cbioportal.G2Smutation.web.domain.MutationUsageTableRepository;
import org.cbioportal.G2Smutation.web.domain.UniprotRepository;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.cbioportal.G2Smutation.web.models.MutationUsageTableResult;
import org.cbioportal.G2Smutation.web.models.MutationUsageTableVariantsInfo;
import org.cbioportal.G2Smutation.web.models.QueryProteinName;
import org.cbioportal.G2Smutation.web.models.Uniprot;
import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.cbioportal.G2Smutation.web.models.mutation.Mutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller for 
 * 
 * @author Juexin Wang
 *
 */
@RestController 
//Set as invisible
public class UnifiedMappedProteinMuationController {
	
	final static Logger log = Logger.getLogger(UnifiedMappedProteinMuationController.class);
	
	@Autowired
    private EnsemblRepository ensemblRepository;
    
    @Autowired
    private UniprotRepository uniprotRepository;
    
    @Autowired
	private SeqIdAlignmentController seqController;
    
    @Autowired
	private MutationUsageTableRepository mutationUsageTableRepository;
    
    private AlignmentRepository alignmentRepository;
	
	@RequestMapping(value = "/unifiedProteinMutationQuery/{id}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation by uniprot or ensembl")
    public MutationUsageTableResult postUnifiedProteinQuery(
    		@ApiParam(required = true, value = "Input id: e.g.ENSP00000484409.1/ENSG00000141510/P04637/P53_HUMAN") @PathVariable String id) {
		
		MutationUsageTableResult result = new MutationUsageTableResult();
    	
    	List<MutationUsageTable> entries = new ArrayList<>();
    	List<MutationUsageTableVariantsInfo> outentries = new ArrayList<>();
    	if(id.startsWith("ENSP")) {    		
    		List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
			for (Ensembl ensembl : ensembllist) {
				entries.addAll(mutationUsageTableRepository.findBySeqIdOrderBySeqIndex(Integer.parseInt(ensembl.getSeqId())));
			}    		
    	}else if(id.startsWith("ENSG")) {    		
    		List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
			for (Ensembl ensembl : ensembllist) {
				entries.addAll(mutationUsageTableRepository.findBySeqIdOrderBySeqIndex(Integer.parseInt(ensembl.getSeqId())));
			}    		
    	}else {
    		if (id.length() == 6 && id.split("_").length != 2) { // Accession: P04637
    			
    	        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(id + "_1");
    	        if (uniprotlist.size() == 1) {
    	        	entries.addAll(mutationUsageTableRepository.findBySeqIdOrderBySeqIndex(Integer.parseInt((uniprotlist.get(0).getSeqId()))));
    	        }

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN

				List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(id);

		        Set<String> uniprotAccSet = new HashSet<String>();
		        for (Uniprot uniprot : uniprotList) {
		            uniprotAccSet.add(uniprot.getUniprotAccession());
		        }

		        List<Mutation> outlist = new ArrayList<Mutation>();
		        Iterator<String> it = uniprotAccSet.iterator();
		        while (it.hasNext()) {
		        	List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(it.next() + "_1");
		            if (uniprotlist.size() == 1) {
		            	entries.addAll(mutationUsageTableRepository.findBySeqIdOrderBySeqIndex(Integer.parseInt((uniprotlist.get(0).getSeqId()))));
		            }
		        }
			}   		
    	}
    	for(MutationUsageTable entry: entries) {
    		System.out.println(entry.getMutationNo());
    		System.out.println(entry.getAlignmentId());
    		Alignment ali = alignmentRepository.findByAlignmentId(entry.getAlignmentId());
    		MutationUsageTableVariantsInfo mui = new MutationUsageTableVariantsInfo(entry, ali);
    		outentries.add(mui);
    	}
    	result.setData(outentries);
    	return result;
    }

}
