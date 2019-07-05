package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @Autowired
    private AlignmentRepository alignmentRepository;
    
    @Autowired
    private MainGetMappedProteinMutationController pmController;

	
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
    	Map<Integer,Alignment> aliHm = new HashMap<>();
    	for(MutationUsageTable entry: entries) {
    		if(!aliHm.containsKey(entry.getAlignmentId())) {
    			Alignment ali = alignmentRepository.findByAlignmentId(entry.getAlignmentId());
    			aliHm.put(entry.getAlignmentId(), ali);
    		}
    		Alignment aliU = aliHm.get(entry.getAlignmentId());
    		MutationUsageTableVariantsInfo mui = new MutationUsageTableVariantsInfo(entry, aliU);
    		outentries.add(mui);
    	}
    	result.setData(outentries);
    	return result;
    }
	
	/**
	 * For display usage
	 * 
	 * @param id_type
	 * @param id
	 * @param pdb_id
	 * @param chain_id
	 * @param positionList
	 * @return
	 */
	@RequestMapping(value = "/unifiedProteinMutationQueryAnno/{idinfo}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation by uniprot or ensembl and PDB")
	public ModelAndView postProteinMutationAnnotationByPDB(@ApiParam(required = true, value = "Input id e.g.\n"
			+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n" + "uniprot:P04637/P53_HUMAN;\n"
			+ "uniprot_isoform:P04637_1/P53_HUMAN_1; and  2pcx_A_282") @PathVariable String idinfo)// P53_HUMAN_2pcx_A_282
	{

		String[] tmpp = idinfo.split("_");
		String id = tmpp[0];
		for (int i = 1; i < tmpp.length - 3; i++) {
			id = id + "_" + tmpp[i];
		}
		String pdb_id = tmpp[tmpp.length - 3];
		String chain_id = tmpp[tmpp.length - 2];
		String position = tmpp[tmpp.length - 1];
		List<MutationAnnotation> annotations = new ArrayList<MutationAnnotation>();
		List<String> positionList = new ArrayList<>();
		positionList.add(position);

		if (id.startsWith("ENSP") || id.startsWith("ENSG")) {// EnsemblID:
			// ENSP00000269305.4/ENSP00000269305
			annotations = pmController.postProteinMutationAnnotationByPDB("ensembl", id, pdb_id, chain_id,
					positionList);
		} else {
			annotations = pmController.postProteinMutationAnnotationByPDB("uniprot", id, pdb_id, chain_id,
					positionList);
		}
		//For here, it should only output one element
		MutationAnnotation annotation = annotations.get(0);

		return new ModelAndView("/annodetail", "annotation", annotation);
	}
	
	
	/**
	 * For display usage
	 * 
	 * @param id_type
	 * @param id
	 * @param pdb_id
	 * @param chain_id
	 * @param positionList
	 * @return
	 */
	@RequestMapping(value = "/unifiedProteinMutationQueryPosAnno/{idinfo}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation by uniprot or ensembl and PDB")
	public ModelAndView postProteinMutationAnnotationPosByPDB(@ApiParam(required = true, value = "Input id e.g.\n"
			+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n" + "uniprot:P04637/P53_HUMAN;\n"
			+ "uniprot_isoform:P04637_1/P53_HUMAN_1; and  2pcx_A_282") @PathVariable String idinfo// P53_HUMAN_282
	) {

		String[] tmpp = idinfo.split("_");
		String id = tmpp[0];
		for (int i = 1; i < tmpp.length - 1; i++) {
			id = id + "_" + tmpp[i];
		}
		String position = tmpp[tmpp.length - 1];
		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		List<String> positionList = new ArrayList<>();
		positionList.add(position);

		if (id.startsWith("ENSP")) {
			
			// ENSP00000269305.4/ENSP00000269305
			List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
			for (Ensembl ensembl : ensembllist) {
				// System.out.println(ensembl.getSeqId());

				outList.addAll(seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), positionList));

			}
		} else if (id.startsWith("ENSG")) {// EnsemblGene:
			// ENSG00000141510.11/ENSG00000141510
			List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
			// Original implementation, just find exact word
			// ENSG00000141510.16
			// List<Ensembl> ensembllist =
			// ensemblRepository.findByEnsemblGene(id);
			if (ensembllist.size() >= 1) {
				for (Ensembl en : ensembllist) {

					outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId(), positionList));

				}
			}
		} else if (id.length() == 6 && id.split("_").length != 2) {// Accession:
			// P04637

			outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1", positionList));

		} else if (id.split("_").length == 2) {// ID: P53_HUMAN

			outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1", positionList));

		}

		return new ModelAndView("/annodetail", "outList", outList);
	}

}
