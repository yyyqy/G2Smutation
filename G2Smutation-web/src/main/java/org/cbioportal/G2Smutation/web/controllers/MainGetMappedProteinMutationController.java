package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.domain.EnsemblRepository;
import org.cbioportal.G2Smutation.web.domain.MutationUsageTableRepository;
import org.cbioportal.G2Smutation.web.domain.UniprotRepository;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedPosition;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedPositionInfo;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedResidue;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedResidueInfo;
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
 * Controller get Mutation from protein: Get Mutations
 * 
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Structure Mutations from Proteins", description = "ensembl/uniprot")
@RequestMapping(value = "/api/")
public class MainGetMappedProteinMutationController {
	final static Logger log = Logger.getLogger(MainGetMappedProteinMutationController.class);

	@Autowired
	private EnsemblRepository ensemblRepository;
	@Autowired
	private SeqIdAlignmentController seqController;
	@Autowired
	private UniprotRepository uniprotRepository;
	@Autowired
	private MutationUsageTableRepository mutationUsageTableRepository;

	@RequestMapping(value = "/proteinMutation/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation by ProteinId")
	public List<Mutation> postProteinMutation(
			@ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
					+ "uniprot_isoform;") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id e.g.\n"
					+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n"
					+ "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_1/P53_HUMAN_1;") @PathVariable String id,
			@ApiParam(required = false, value = "Input Residue Positions e.g. 202,282;"
					+ "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

		List<Mutation> outList = new ArrayList<Mutation>();
		if (id_type.equals("ensembl")) {
			if (id.startsWith("ENSP")) {// EnsemblID:
				// ENSP00000269305.4/ENSP00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
				for (Ensembl ensembl : ensembllist) {
					// System.out.println(ensembl.getSeqId());
					if (positionList == null) {
						outList.addAll(seqController.getMutationUsageBySeqId(ensembl.getSeqId()));
					} else {
						outList.addAll(seqController.getMutationUsageBySeqId(ensembl.getSeqId(), positionList));
					}

				}
			} else if (id.startsWith("ENSG")) {// EnsemblGene:
				// ENSG00000141510.11/ENSG00000141510
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				// Original implementation, just find exact word
				// ENSG00000141510
				// List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl en : ensembllist) {
						if (positionList == null) {
							outList.addAll(seqController.getMutationUsageBySeqId(en.getSeqId()));
						} else {
							outList.addAll(seqController.getMutationUsageBySeqId(en.getSeqId(), positionList));
						}

					}
				}
			} else if (id.startsWith("ENST")) {// EnsemblTranscript:
				// ENST00000269305.4/ENST00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl en : ensembllist) {
						if (positionList == null) {
							outList.addAll(seqController.getMutationUsageBySeqId(en.getSeqId()));
						} else {
							outList.addAll(seqController.getMutationUsageBySeqId(en.getSeqId(), positionList));
						}

					}
				}
			} else {
				log.info("Error in Input. id_type:Ensembl id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot")) {
			if (id.length() == 6 && id.split("_").length != 2) { // Accession: P04637
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageByUniprotAccessionIso(id, "1"));
				} else {
					outList.addAll(seqController.getMutationUsageByUniprotAccessionIso(id, "1", positionList));
				}

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageByUniprotIdIso(id, "1"));
				} else {
					outList.addAll(seqController.getMutationUsageByUniprotIdIso(id, "1", positionList));
				}

			} else {
				log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot_isoform")) {
			if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession: P04637
				if (positionList == null) {
					outList.addAll(
							seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]));
				} else {
					outList.addAll(seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0],
							id.split("_")[1], positionList));
				}

			} else if (id.split("_").length == 3) {// ID: P53_HUMAN
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2]));
				} else {
					outList.addAll(seqController.getMutationUsageByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2], positionList));
				}

			} else {
				log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
			}
		}

		else {
			log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
		}
		return outList;
	}
	
	@RequestMapping(value = "/proteinMutation/{id_type}/{id:.+}/pdb/{pdb_id}_{chain_id}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation by ProteinId and PDB")
	public List<Mutation> postProteinMutationByPDB(
			@ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
					+ "uniprot_isoform;") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id e.g.\n"
					+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n"
					+ "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_1/P53_HUMAN_1;") @PathVariable String id,
			@ApiParam(required = true, value = "Input PDB Id e.g. 2pcx") @PathVariable String pdb_id,
            @ApiParam(required = true, value = "Input Chain e.g. A") @PathVariable String chain_id,
			@ApiParam(required = false, value = "Input Residue Positions e.g. 202,282;"
					+ "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

		List<Mutation> outList = new ArrayList<Mutation>();
		if (id_type.equals("ensembl")) {
			if (id.startsWith("ENSP")) {// EnsemblID:
				// ENSP00000269305.4/ENSP00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
				for (Ensembl ensembl : ensembllist) {					
					List<Mutation> list = null;
                    if (positionList == null) {
                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId());
                    } else {
                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId(), positionList);
                    }
                    outList = choosePDBresult(list, outList, pdb_id, chain_id);
				}
			} else if (id.startsWith("ENSG")) {// EnsemblGene:
				// ENSG00000141510.11/ENSG00000141510
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				// Original implementation, just find exact word
				// ENSG00000141510
				// List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<Mutation> list = null;
	                    if (positionList == null) {
	                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId(), positionList);
	                    }
	                    outList = choosePDBresult(list, outList, pdb_id, chain_id);
					}
				}
			} else if (id.startsWith("ENST")) {// EnsemblTranscript:
				// ENST00000269305.4/ENST00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<Mutation> list = null;
	                    if (positionList == null) {
	                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageBySeqId(ensembl.getSeqId(), positionList);
	                    }
	                    outList = choosePDBresult(list, outList, pdb_id, chain_id);
					}
				}
			} else {
				log.info("Error in Input. id_type:Ensembl id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot")) {
			if (id.length() == 6 && id.split("_").length != 2) { // Accession: P04637
				List<Mutation> list = null;
				if (positionList == null) {
					list=seqController.getMutationUsageByUniprotAccessionIso(id, "1");
				} else {
					list=seqController.getMutationUsageByUniprotAccessionIso(id, "1", positionList);
				}
				outList = choosePDBresult(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN
				List<Mutation> list = null;
				if (positionList == null) {
					list=seqController.getMutationUsageByUniprotIdIso(id, "1");
				} else {
					list=seqController.getMutationUsageByUniprotIdIso(id, "1", positionList);
				}
				outList = choosePDBresult(list, outList, pdb_id, chain_id);

			} else {
				log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot_isoform")) {
			if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession: P04637
				List<Mutation> list = null;
				if (positionList == null) {
					list = seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]);
				} else {
					list = seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0],
							id.split("_")[1], positionList);
				}
				outList = choosePDBresult(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 3) {// ID: P53_HUMAN
				List<Mutation> list = null;
				if (positionList == null) {
					list = seqController.getMutationUsageByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2]);
				} else {
					list = seqController.getMutationUsageByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2], positionList);
				}
				outList = choosePDBresult(list, outList, pdb_id, chain_id);

			} else {
				log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
			}
		}

		else {
			log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
		}
		return outList;
	}

	@RequestMapping(value = "/proteinMutationAnno/{id_type}/{id:.+}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation and annotation by ProteinId")
	public List<MutationAnnotation> postProteinMutationAnnotation(
			@ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
					+ "uniprot_isoform") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id e.g.\n"
					+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n"
					+ "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_1/P53_HUMAN_1;") @PathVariable String id,
			@ApiParam(required = false, value = "Input Residue Positions e.g. 202,282;"
					+ "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		if (id_type.equals("ensembl")) {
			if (id.startsWith("ENSP")) {// EnsemblID:
				// ENSP00000269305.4/ENSP00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
				for (Ensembl ensembl : ensembllist) {
					// System.out.println(ensembl.getSeqId());
					if (positionList == null) {
						outList.addAll(seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId()));
					} else {
						outList.addAll(
								seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), positionList));
					}

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
						if (positionList == null) {
							outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId()));
						} else {
							outList.addAll(
									seqController.getMutationUsageAnnotationBySeqId(en.getSeqId(), positionList));
						}

					}
				}
			} else if (id.startsWith("ENST")) {// EnsemblTranscript:
				// ENST00000269305.4/ENST00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl en : ensembllist) {
						if (positionList == null) {
							outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId()));
						} else {
							outList.addAll(
									seqController.getMutationUsageAnnotationBySeqId(en.getSeqId(), positionList));
						}

					}
				}
			} else {
				log.info("Error in Input. id_type:Ensembl id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot")) {
			if (id.length() == 6 && id.split("_").length != 2) {// Accession:
				// P04637
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1"));
				} else {
					outList.addAll(
							seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1", positionList));
				}

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1"));
				} else {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1", positionList));
				}

			} else {
				log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot_isoform")) {
			if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession:
				// P04637
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0],
							id.split("_")[1]));
				} else {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0],
							id.split("_")[1], positionList));
				}

			} else if (id.split("_").length == 3) {// ID: P53_HUMAN
				if (positionList == null) {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2]));
				} else {
					outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2], positionList));
				}

			} else {
				log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
			}
		}


		else {
			log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
		}
		return outList;
	}
	
	@RequestMapping(value = "/proteinMutationAnno/{id_type}/{id:.+}/pdb/{pdb_id}_{chain_id}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation annotation by ProteinId and PDB")
	public List<MutationAnnotation> postProteinMutationAnnotationByPDB(
			@ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
					+ "uniprot_isoform;") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id e.g.\n"
					+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n"
					+ "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_1/P53_HUMAN_1;") @PathVariable String id,
			@ApiParam(required = true, value = "Input PDB Id e.g. 2pcx") @PathVariable String pdb_id,
            @ApiParam(required = true, value = "Input Chain e.g. A") @PathVariable String chain_id,
			@ApiParam(required = false, value = "Input Residue Positions e.g. 202,282;"
					+ "Return all residue mappings if none") @RequestParam(required = false) List<String> pdbPositionList) {

		System.out.println("***"+id_type+"\t"+id+"\t"+pdb_id+"\t"+chain_id);
		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		try {
		if (id_type.equals("ensembl")) {
			if (id.startsWith("ENSP")) {// EnsemblID:
				// ENSP00000269305.4/ENSP00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
				for (Ensembl ensembl : ensembllist) {					
					List<MutationAnnotation> list = null;
                    if (pdbPositionList == null) {
                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
                    } else {
                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), pdbPositionList);
                    }

                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
				}
			} else if (id.startsWith("ENSG")) {// EnsemblGene:
				// ENSG00000141510.11/ENSG00000141510
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				// Original implementation, just find exact word
				// ENSG00000141510
				// List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<MutationAnnotation> list = null;
	                    if (pdbPositionList == null) {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), pdbPositionList);
	                    }

	                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
					}
				}
			} else if (id.startsWith("ENST")) {// EnsemblTranscript:
				// ENST00000269305.4/ENST00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<MutationAnnotation> list = null;
	                    if (pdbPositionList == null) {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), pdbPositionList);
	                    }

	                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
					}
				}
			} else {
				log.info("Error in Input. id_type:Ensembl id: " + id + " position:" + pdbPositionList);
			}

		} else if (id_type.equals("uniprot")) {
			if (id.length() == 6 && id.split("_").length != 2) { // Accession: P04637
				List<MutationAnnotation> list = null;
				if (pdbPositionList == null) {
					list=seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1");
				} else {
					list=seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1", pdbPositionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN
				System.out.println("HERE!");
				List<MutationAnnotation> list = null;
				if (pdbPositionList == null) {
					list=seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1");
				} else {
					list=seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1", pdbPositionList);
				}
				
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
			} else {
				log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + pdbPositionList);
			}

		} else if (id_type.equals("uniprot_isoform")) {
			if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession: P04637
				List<MutationAnnotation> list = null;
				if (pdbPositionList == null) {
					list = seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]);
				} else {
					list = seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0],
							id.split("_")[1], pdbPositionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 3) {// ID: P53_HUMAN
				List<MutationAnnotation> list = null;
				if (pdbPositionList == null) {
					list = seqController.getMutationUsageAnnotationByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2]);
				} else {
					list = seqController.getMutationUsageAnnotationByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2], pdbPositionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else {
				log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
			}
		}

		else {
			log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + pdbPositionList);
		}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return outList;
	}
	
	
	@RequestMapping(value = "/proteinMutationAnnoPDB/{id_type}/{id:.+}/pdb/{pdb_id}_{chain_id}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("POST mutation annotation by ProteinId and PDB residue")
	public List<MutationAnnotation> postProteinMutationAnnotationByPDBResidue(
			@ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
					+ "uniprot_isoform;") @PathVariable String id_type,
			@ApiParam(required = true, value = "Input id e.g.\n"
					+ "ensembl:ENSP00000269305.4/ENSG00000141510.11/ENST00000269305.4;\n"
					+ "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_1/P53_HUMAN_1;") @PathVariable String id,
			@ApiParam(required = true, value = "Input PDB Id e.g. 2pcx") @PathVariable String pdb_id,
            @ApiParam(required = true, value = "Input Chain e.g. A") @PathVariable String chain_id,
			@ApiParam(required = false, value = "Input PDB Residue Positions e.g. 202,282;"
					+ "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

		System.out.println("***"+id_type+"\t"+id+"\t"+pdb_id+"\t"+chain_id);
		List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
		try {
		if (id_type.equals("ensembl")) {
			if (id.startsWith("ENSP")) {// EnsemblID:
				// ENSP00000269305.4/ENSP00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
				for (Ensembl ensembl : ensembllist) {					
					List<MutationAnnotation> list = null;
                    if (positionList == null) {
                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
                    } else {
                        list = seqController.getMutationUsageAnnotationBySeqIdPDB(ensembl.getSeqId(), positionList);
                    }

                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
				}
			} else if (id.startsWith("ENSG")) {// EnsemblGene:
				// ENSG00000141510.11/ENSG00000141510
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				// Original implementation, just find exact word
				// ENSG00000141510
				// List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<MutationAnnotation> list = null;
	                    if (positionList == null) {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageAnnotationBySeqIdPDB(ensembl.getSeqId(), positionList);
	                    }

	                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
					}
				}
			} else if (id.startsWith("ENST")) {// EnsemblTranscript:
				// ENST00000269305.4/ENST00000269305
				List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
				if (ensembllist.size() >= 1) {
					for (Ensembl ensembl : ensembllist) {					
						List<MutationAnnotation> list = null;
	                    if (positionList == null) {
	                        list = seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId());
	                    } else {
	                        list = seqController.getMutationUsageAnnotationBySeqIdPDB(ensembl.getSeqId(), positionList);
	                    }

	                    outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
					}
				}
			} else {
				log.info("Error in Input. id_type:Ensembl id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot")) {
			if (id.length() == 6 && id.split("_").length != 2) { // Accession: P04637
				List<MutationAnnotation> list = null;
				if (positionList == null) {
					list=seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1");
				} else {
					list=seqController.getMutationUsageAnnotationByUniprotAccessionIsoPDB(id, "1", positionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 2) {// ID: P53_HUMAN
				System.out.println("HERE!");
				List<MutationAnnotation> list = null;
				if (positionList == null) {
					list=seqController.getMutationUsageAnnotationByUniprotIdIso(id, "1");
				} else {
					list=seqController.getMutationUsageAnnotationByUniprotIdIsoPDB(id, "1", positionList);
				}
				
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);
			} else {
				log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + positionList);
			}

		} else if (id_type.equals("uniprot_isoform")) {
			if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession: P04637
				List<MutationAnnotation> list = null;
				if (positionList == null) {
					list = seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]);
				} else {
					list = seqController.getMutationUsageAnnotationByUniprotAccessionIsoPDB(id.split("_")[0],
							id.split("_")[1], positionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else if (id.split("_").length == 3) {// ID: P53_HUMAN
				List<MutationAnnotation> list = null;
				if (positionList == null) {
					list = seqController.getMutationUsageAnnotationByUniprotIdIso(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2]);
				} else {
					list = seqController.getMutationUsageAnnotationByUniprotIdIsoPDB(
							id.split("_")[0] + "_" + id.split("_")[1], id.split("_")[2], positionList);
				}
				outList = choosePDBresultAnnotation(list, outList, pdb_id, chain_id);

			} else {
				log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
			}
		}

		else {
			log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
		}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return outList;
	}
	
	/**
	 * Find defined PDB from mutation results
	 * @param list
	 * @param outList
	 * @param pdb_id
	 * @param chain_id
	 * @return
	 */
	List<Mutation> choosePDBresult(List<Mutation> list, List<Mutation> outList, String pdb_id, String chain_id){
		for (Mutation mutation : list) {
        	List<MutatedPosition> mpList = mutation.getMutatedPosition();
        	for(MutatedPosition mp: mpList) {
        		List<MutatedResidue> mrList = mp.getMutatedResidue();
        		for(MutatedResidue mr: mrList) {
        			String[] tmpArray = mr.getPdbNo().toLowerCase().split("_");
        			String pd = tmpArray[0];
                    String ch = tmpArray[1];
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                    	//System.out.println(pd+"\t"+ch);
                    	MutatedResidue nmr = new MutatedResidue();
                    	nmr.setPdbNo(mr.getPdbNo());
                    	nmr.setPdbPos(mr.getPdbPos());
                    	nmr.setPdbResidue(mr.getPdbResidue());
                    	List<MutatedResidue> nmrlist = new ArrayList<>();
                    	nmrlist.add(nmr);
                    	//System.out.println("***"+nmrlist.size());
                    	MutatedPosition nmp = new MutatedPosition();
                    	nmp.setMutatedResidue(nmrlist);
                    	nmp.setProteinPos(mp.getProteinPos());
                    	nmp.setProteinResidue(mp.getProteinResidue());
                    	//System.out.println("*****"+nmp.getMutatedResidue().size());
                    	Mutation nmutation = new Mutation();
                    	List<MutatedPosition> nmplist = new ArrayList<>();
                    	nmplist.add(nmp);
                    	nmutation.setMutatedPosition(nmplist);
                    	nmutation.setProteinName(mutation.getProteinName());
                        outList.add(nmutation);
                        //System.out.println(nmutation.getMutatedPosition().get(0).getMutatedResidue().size());
                    }                   			
        		}                   		
        	}                       
        }
		return outList;		
	}
	
	/**
	 * Find defined PDB from mutation annotation results
	 * @param list
	 * @param outList
	 * @param pdb_id
	 * @param chain_id
	 * @return
	 */
	List<MutationAnnotation> choosePDBresultAnnotation(List<MutationAnnotation> list, List<MutationAnnotation> outList, String pdb_id, String chain_id){
		for (MutationAnnotation mutation : list) {
        	List<MutatedPositionInfo> mpList = mutation.getMutatedPositionInfo();
        	for(MutatedPositionInfo mp: mpList) {
        		List<MutatedResidueInfo> mrList = mp.getMutatedResidueInfo();
        		for(MutatedResidueInfo mr: mrList) {
        			String[] tmpArray = mr.getPdbNo().toLowerCase().split("_");
        			String pd = tmpArray[0];
                    String ch = tmpArray[1];
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                    	System.out.println(pd+"\t"+ch);
                    	MutatedResidueInfo nmr = new MutatedResidueInfo();
                    	nmr.setPdbNo(mr.getPdbNo());
                    	nmr.setPdbPos(mr.getPdbPos());
                    	nmr.setPdbResidue(mr.getPdbResidue());
                    	nmr.setStructureAnnotationInfo(mr.getStructureAnnotation());
                    	List<MutatedResidueInfo> nmrlist = new ArrayList<>();
                    	nmrlist.add(nmr);
                    	//System.out.println("***"+nmrlist.size());
                    	MutatedPositionInfo nmp = new MutatedPositionInfo();
                    	nmp.setMutatedResidueInfo(nmrlist);
                    	nmp.setProteinPos(mp.getProteinPos());
                    	nmp.setProteinResidue(mp.getProteinResidue());
                    	nmp.setClinvarAnnotation(mp.getClinvarAnnotation());
                    	nmp.setCosmicAnnotation(mp.getCosmicAnnotation());
                    	nmp.setDbsnpAnnotation(mp.getDbsnpAnnotation());
                    	//nmp.setGenieAnnotation(mp.getGenieAnnotation());
                    	nmp.setTcgaAnnotation(mp.getTcgaAnnotation());
                    	//System.out.println("*****"+nmp.getMutatedResidue().size());
                    	MutationAnnotation nmutation = new MutationAnnotation();
                    	List<MutatedPositionInfo> nmplist = new ArrayList<>();
                    	nmplist.add(nmp);
                    	nmutation.setMutatedPositionInfo(nmplist);
                    	nmutation.setProteinName(mutation.getProteinName());
                        outList.add(nmutation);
                        //System.out.println(nmutation.getMutatedPosition().get(0).getMutatedResidue().size());
                    }                   			
        		}                   		
        	}                       
        }
		return outList;		
	}

}
