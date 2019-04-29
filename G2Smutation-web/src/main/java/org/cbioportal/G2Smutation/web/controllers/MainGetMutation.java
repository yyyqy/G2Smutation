package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.web.domain.EnsemblRepository;
import org.cbioportal.G2Smutation.web.domain.MutationUsageTableRepository;
import org.cbioportal.G2Smutation.web.domain.UniprotRepository;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.cbioportal.G2Smutation.web.models.Ensembl;
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
 * Main controller get ResidueMutation: Get Mutations
 * 
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Get Mutation", description = "mutation in ensembl/uniprot/hgvs/sequences")
@RequestMapping(value = "/api/")
public class MainGetMutation {
    final static Logger log = Logger.getLogger(MainGetMutation.class);
    
    @Autowired
    private EnsemblRepository ensemblRepository;
    @Autowired
    private SeqIdAlignmentController seqController;
    @Autowired
    private UniprotRepository uniprotRepository;
    @Autowired
    private MutationUsageTableRepository mutationUsageTableRepository;
    
    @RequestMapping(value = "/mutation/{id_type}/{id:.+}", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("POST mutation by ProteinId")
    public List<Mutation> postProteinMutation(
            @ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
                    + "uniprot_isoform;\n hgvs-grch37; dbsnp") @PathVariable String id_type,
            @ApiParam(required = true, value = "Input id e.g.\n"
                    + "ensembl:ENSP00000484409.1/ENSG00000141510.16/ENST00000504290.5;\n"
                    + "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_9/P53_HUMAN_9;\n"
                    + "hgvs-grch37:17:g.79478130C>G;\n" + "hgvs-grch38:17:g.7676594T>G;\n"
                    + "dbsnp:rs1800369") @PathVariable String id,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,100; Anynumber for hgvs;\n"
                    + "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {
        
        List<Mutation> outList = new ArrayList<Mutation>();
        if (id_type.equals("ensembl")) {
            if (id.startsWith("ENSP")) {// EnsemblID:
                // ENSP00000484409.1/ENSP00000484409
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
                for (Ensembl ensembl : ensembllist) {
                    //System.out.println(ensembl.getSeqId());
                    if (positionList == null) {
                        outList.addAll(seqController.getMutationUsageBySeqId(ensembl.getSeqId()));
                    } else {
                        outList.addAll(seqController.getMutationUsageBySeqId(ensembl.getSeqId(), positionList));
                    }

                }
            } else if (id.startsWith("ENSG")) {// EnsemblGene:
                // ENSG00000141510.16/ENSG00000141510
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);
                // Original implementation, just find exact word
                // ENSG00000141510.16
                // List<Ensembl> ensembllist =
                // ensemblRepository.findByEnsemblGene(id);
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
                // ENST00000504290.5/ENST00000504290
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
            if (id.length() == 6 && id.split("_").length != 2) {// Accession:
                // P04637
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
            if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession:
                // P04637
                if (positionList == null) {
                    outList.addAll(
                            seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]));
                } else {
                    outList.addAll(seqController.getMutationUsageByUniprotAccessionIso(id.split("_")[0], id.split("_")[1],
                            positionList));
                }

            } else if (id.split("_").length == 3) {// ID: P53_HUMAN
                if (positionList == null) {
                    outList.addAll(seqController.getMutationUsageByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2]));
                } else {
                    outList.addAll(seqController.getMutationUsageByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2], positionList));
                }

            } else {
                log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
            }
        } 
        /*
        else if (id_type.equals("hgvs-grch37")) {
            // http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

            String genomeVersion = "GRCH37";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);

            System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
            outList.addAll(
                    seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType, genomeVersion));

        } else if (id_type.equals("hgvs-grch38")) {
            // http://rest.ensembl.org/vep/human/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?content-type=application/json&protein=1
            String genomeVersion = "GRCH38";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);
            System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
            outList.addAll(
                    seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType, genomeVersion));

        } else if (id_type.equals("dbsnp")) {
            // https://www.genomenexus.org/beta/annotation/dbsnp/rs116035550
            // https://www.genomenexus.org/beta/annotation/dbsnp/dbSNPID
            System.out.println("dbsnp: " + id);
            outList.addAll(seqController.getPdbResidueByEnsemblIddbSNPID(id));
        } 
        */
        else {
            log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
        }
        return outList;
    }
    
    @RequestMapping(value = "/mutationanno/{id_type}/{id:.+}", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("POST mutation and annotation by ProteinId")
    public List<MutationAnnotation> postProteinMutationAnnotation(
            @ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
                    + "uniprot_isoform;\n hgvs-grch37; dbsnp") @PathVariable String id_type,
            @ApiParam(required = true, value = "Input id e.g.\n"
                    + "ensembl:ENSP00000484409.1/ENSG00000141510.16/ENST00000504290.5;\n"
                    + "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_9/P53_HUMAN_9;\n"
                    + "hgvs-grch37:17:g.79478130C>G;\n" + "hgvs-grch38:17:g.7676594T>G;\n"
                    + "dbsnp:rs1800369") @PathVariable String id,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,100; Anynumber for hgvs;\n"
                    + "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {
        
        List<MutationAnnotation> outList = new ArrayList<MutationAnnotation>();
        if (id_type.equals("ensembl")) {
            if (id.startsWith("ENSP")) {// EnsemblID:
                // ENSP00000484409.1/ENSP00000484409
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
                for (Ensembl ensembl : ensembllist) {
                    //System.out.println(ensembl.getSeqId());
                    if (positionList == null) {
                        outList.addAll(seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId()));
                    } else {
                        outList.addAll(seqController.getMutationUsageAnnotationBySeqId(ensembl.getSeqId(), positionList));
                    }

                }
            } else if (id.startsWith("ENSG")) {// EnsemblGene:
                // ENSG00000141510.16/ENSG00000141510
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
                            outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId(), positionList));
                        }

                    }
                }
            } else if (id.startsWith("ENST")) {// EnsemblTranscript:
                // ENST00000504290.5/ENST00000504290
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
                if (ensembllist.size() >= 1) {
                    for (Ensembl en : ensembllist) {
                        if (positionList == null) {
                            outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId()));
                        } else {
                            outList.addAll(seqController.getMutationUsageAnnotationBySeqId(en.getSeqId(), positionList));
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
                    outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id, "1", positionList));
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
                    outList.addAll(
                            seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]));
                } else {
                    outList.addAll(seqController.getMutationUsageAnnotationByUniprotAccessionIso(id.split("_")[0], id.split("_")[1],
                            positionList));
                }

            } else if (id.split("_").length == 3) {// ID: P53_HUMAN
                if (positionList == null) {
                    outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2]));
                } else {
                    outList.addAll(seqController.getMutationUsageAnnotationByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2], positionList));
                }

            } else {
                log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
            }
        } 
        /*
        else if (id_type.equals("hgvs-grch37")) {
            // http://annotation.genomenexus.org/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?isoformOverrideSource=uniprot&summary=summary

            String genomeVersion = "GRCH37";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);

            System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
            outList.addAll(
                    seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType, genomeVersion));

        } else if (id_type.equals("hgvs-grch38")) {
            // http://rest.ensembl.org/vep/human/hgvs/CHROMSOME:g.POSITIONORIGINAL%3EMUTATION?content-type=application/json&protein=1
            String genomeVersion = "GRCH38";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);
            System.out.println(chromosomeNum + " " + pos + " " + nucleotideType + " " + genomeVersion);
            outList.addAll(
                    seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType, genomeVersion));

        } else if (id_type.equals("dbsnp")) {
            // https://www.genomenexus.org/beta/annotation/dbsnp/rs116035550
            // https://www.genomenexus.org/beta/annotation/dbsnp/dbSNPID
            System.out.println("dbsnp: " + id);
            outList.addAll(seqController.getPdbResidueByEnsemblIddbSNPID(id));
        } 
        */
        else {
            log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
        }
        return outList;
    }

}

