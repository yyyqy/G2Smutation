package org.cbioportal.G2Smutation.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cbioportal.G2Smutation.web.domain.AlignmentRepository;
import org.cbioportal.G2Smutation.web.domain.ClinvarRepository;
import org.cbioportal.G2Smutation.web.domain.CosmicRepository;
import org.cbioportal.G2Smutation.web.domain.DbsnpRepository;
import org.cbioportal.G2Smutation.web.domain.EnsemblRepository;
import org.cbioportal.G2Smutation.web.domain.GeneSequenceRepository;
import org.cbioportal.G2Smutation.web.domain.GenieRepository;
import org.cbioportal.G2Smutation.web.domain.MutationUsageTableRepository;
import org.cbioportal.G2Smutation.web.domain.StructureAnnotationRepository;
import org.cbioportal.G2Smutation.web.domain.TcgaRepository;
import org.cbioportal.G2Smutation.web.domain.UniprotRepository;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.cbioportal.G2Smutation.web.models.GenomeResidueInput;
import org.cbioportal.G2Smutation.web.models.Uniprot;
import org.cbioportal.G2Smutation.web.models.api.UtilAPI;
import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.cbioportal.G2Smutation.web.models.db.Dbsnp;
import org.cbioportal.G2Smutation.web.models.db.Genie;
import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.cbioportal.G2Smutation.web.models.db.Tcga;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedPosition;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedPositionInfo;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedResidue;
import org.cbioportal.G2Smutation.web.models.mutation.MutatedResidueInfo;
import org.cbioportal.G2Smutation.web.models.mutation.Mutation;
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotation;
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotationGenome;
import org.cbioportal.G2Smutation.web.controllers.SeqIdAlignmentController;
import org.cbioportal.G2Smutation.web.models.ResidueMapping;
import org.cbioportal.G2Smutation.web.models.StructureAnnotationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *
 * Controller of the API: Input Protein SeqId, inner control
 * 
 * Invisible from webpage
 *
 * @author Juexin Wang
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
// @CrossOrigin(origins = "*") // allow all cross-domain requests
// @Api(tags = "QueryInnerID", description = "Inner ID")
//@RequestMapping(value = "/api/") //invisible for inner usage
public class SeqIdAlignmentController {

    @Autowired
    private AlignmentRepository alignmentRepository;
    @Autowired
    private GeneSequenceRepository geneSequenceRepository;
    @Autowired
    private UniprotRepository uniprotRepository;
    @Autowired
    private EnsemblRepository ensemblRepository;
    @Autowired
    private SeqIdAlignmentController seqController;
    @Autowired
    private MutationUsageTableRepository mutationUsageTableRepository;
    @Autowired
    private ClinvarRepository clinvarRepository;
    @Autowired
    private DbsnpRepository dbsnpRepository;
    @Autowired
    private CosmicRepository cosmicRepository;
    @Autowired
    private GenieRepository genieRepository;
    @Autowired
    private TcgaRepository tcgaRepository;
    @Autowired
    private StructureAnnotationRepository structureAnnotationRepository; 
    

    @RequestMapping(value = "/GeneSeqStructureMapping/{seqId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get PDB Alignments by Protein SeqId")
    public List<Alignment> getPdbAlignmentByGeneSequenceId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {
        return alignmentRepository.findBySeqId(seqId);
    }

    // Query from EnsemblId
    @ApiOperation(value = "Get PDB Alignments by EnsemblId", nickname = "EnsemblStructureMappingQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Alignment.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @RequestMapping(value = "/EnsemblStructureMappingQueryT", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getPdbAlignmentByEnsemblId(
            @RequestParam @ApiParam(value = "Input Ensembl Id e.g. ENSP00000489609.1", required = true, allowMultiple = true) String ensemblId) {
        return ensemblId + "SS";
    }

    @RequestMapping(value = "/GeneSeqStructureMapping/{seqId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get PDB Alignments by Protein SeqId")
    public List<Alignment> getPdbAlignmentByGeneSequenceIdPOST(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {
        return alignmentRepository.findBySeqId(seqId);
    }

    @RequestMapping(value = "/GeneSeqRecognition/{seqId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Whether Protein SeqId Exists")
    public boolean getExistedSeqIdinAlignment(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {
        return geneSequenceRepository.findBySeqId(seqId).size() != 0;
    }

    /**
     * "Get All Residue Mapping by Protein SeqId
     * 
     * @param seqId
     * @return
     */
    public List<Alignment> getPdbResidueBySeqId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {
        List<Alignment> it = alignmentRepository.findBySeqId(seqId);
        List<Alignment> outit = new ArrayList<Alignment>();        

        for (Alignment ali : it) {
            List<ResidueMapping> residueMapping = new ArrayList<ResidueMapping>();
            // count "-" in genSequence
            int seqGapCount = 0;
            // count "-" in pdbSequence
            int pdbGapCount = 0;
            for (int inputAA = ali.getSeqFrom(); inputAA <= ali.getSeqTo(); inputAA++) {
                ResidueMapping rp = new ResidueMapping();

                rp.setQueryAminoAcid(
                        ali.getSeqAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                rp.setPdbAminoAcid(
                        ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));

                // if there is a "-" in genSequence, seqGapCount +1
                if (rp.getQueryAminoAcid().equals("-")) {
                    seqGapCount++;
                    continue;
                }
                // if there is a "-" in pdbSequence, pdbGapCount+1
                if (rp.getPdbAminoAcid().equals("-")) {
                    pdbGapCount++;
                    continue;
                }

                // rp.setQueryPosition(inputAA - ali.getSeqFrom() + 1);
                rp.setQueryPosition(inputAA - seqGapCount);
                rp.setPdbPosition(Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom()
                        + (inputAA - ali.getSeqFrom()) - pdbGapCount);

                // Withdraw if mapped to linker of the protein
                if (!rp.getPdbAminoAcid().equals("X")) {
                    residueMapping.add(rp);
                }
            }

            ali.setResidueMapping(residueMapping);
            outit.add(ali);

        }
        return outit;
    }

    /**
     * "Get Residue Mapping by Protein SeqId and Residue Positions
     * 
     * @param seqId
     * @param positionList
     * @return
     */
    //header added for inner usage, can directly use it, but not visible outside 
    //http://localhost:8080/inner/2/100/residueMapping
    @RequestMapping(value = "/inner/{seqId}/{position:.+}/residueMapping", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("POST PDB Residue Mapping by ProteinId")
    public List<Alignment> getPdbResidueBySeqId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId,
            @ApiParam(required = true, value = "Input Residue Position e.g. 100") @PathVariable String position) {
        List<Alignment> it = alignmentRepository.findBySeqId(seqId);
        List<Alignment> outit = new ArrayList<Alignment>();
        int inputAA = Integer.parseInt(position);        
        for (Alignment ali : it) {
            int seqGapCount = 0;
            int pdbGapCount = 0;
            if (inputAA >= ali.getSeqFrom() && inputAA <= ali.getSeqTo()) {
                ResidueMapping rp = new ResidueMapping();
                List<ResidueMapping> residueMapping = new ArrayList<ResidueMapping>();
                rp.setQueryAminoAcid(
                        ali.getSeqAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                rp.setPdbAminoAcid(
                        ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                if (rp.getQueryAminoAcid().equals("-")) {
                    seqGapCount++;
                    continue;
                }
                if (rp.getPdbAminoAcid().equals("-")) {
                    pdbGapCount++;
                    continue;
                }
                rp.setQueryPosition(inputAA - seqGapCount);
                rp.setPdbPosition(Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom()
                        + (inputAA - ali.getSeqFrom()) - pdbGapCount);
                // Withdraw if mapped to linker of the protein
                if (!rp.getPdbAminoAcid().equals("X")) {
                    residueMapping.add(rp);
                }

                ali.setResidueMapping(residueMapping);
                outit.add(ali);
            }
        }
        return outit;
    }

    /**
     * "Get Residue Mapping by Protein SeqId and Residue Positions list
     * 
     * @param seqId
     * @param positionList
     * @return
     */
    public List<Alignment> getPdbResidueBySeqId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId,
            @ApiParam(required = true, value = "Input Residue Position e.g. 99,100") @PathVariable List<String> positionList) {
        List<Alignment> it = alignmentRepository.findBySeqId(seqId);
        List<Alignment> outit = new ArrayList<Alignment>();


        for (Alignment ali : it) {

            List<ResidueMapping> residueMapping = new ArrayList<ResidueMapping>();
            int seqGapCount = 0;
            int pdbGapCount = 0;

            for (String position : positionList) {
                int inputAA = Integer.parseInt(position);
                if (inputAA >= ali.getSeqFrom() && inputAA <= ali.getSeqTo()) {
                    ResidueMapping rp = new ResidueMapping();
                    rp.setQueryAminoAcid(
                            ali.getSeqAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                    rp.setPdbAminoAcid(
                            ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                    if (rp.getQueryAminoAcid().equals("-")) {
                        seqGapCount++;
                        continue;
                    }
                    if (rp.getPdbAminoAcid().equals("-")) {
                        pdbGapCount++;
                        continue;
                    }
                    rp.setQueryPosition(inputAA - seqGapCount);
                    rp.setPdbPosition(Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom()
                            + (inputAA - ali.getSeqFrom()) - pdbGapCount);

                    // Withdraw if mapped to linker of the protein
                    if (!rp.getPdbAminoAcid().equals("X")) {
                        residueMapping.add(rp);
                    }

                }
            }

            ali.setResidueMapping(residueMapping);
            outit.add(ali);
        }

        return outit;
    }

    /*
     * Support function for alignments
     */

    // P04637
    public List<Alignment> getPdbAlignmentByUniprotAccession(String uniprotAccession) {
        List<Uniprot> uniprotList = uniprotRepository.findByUniprotAccession(uniprotAccession);
        ArrayList<Alignment> outList = new ArrayList<Alignment>();
        for (Uniprot entry : uniprotList) {
            outList.addAll(alignmentRepository.findBySeqId(entry.getSeqId()));
        }
        return outList;
    }

    // P04637 2fej A
    public List<Alignment> getPdbAlignmentByUniprotAccession(String uniprotAccession, String pdbId, String chain) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccession(uniprotAccession);
        if (uniprotlist.size() > 0) {
            List<Alignment> list = alignmentRepository.findBySeqId(uniprotlist.get(0).getSeqId());
            List<Alignment> outlist = new ArrayList<Alignment>();

            for (Alignment ali : list) {
                String pd = ali.getPdbId().toLowerCase();
                String ch = ali.getChain().toLowerCase();
                if (pd.equals(pdbId.toLowerCase()) && ch.equals(chain.toLowerCase())) {
                    outlist.add(ali);
                }
            }
            return outlist;
        } else {
            return null;
        }
    }

    // P53_HUMAN
    //
    public List<Alignment> getPdbAlignmentByUniprotId(String uniprotId) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbAlignmentByUniprotAccession(it.next()));
        }

        return outlist;
    }

    // P53_HUMAN 2fej A
    public List<Alignment> getPdbAlignmentByUniprotId(String uniprotId, String pdbId, String chain) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbAlignmentByUniprotAccession(it.next(), pdbId, chain));
        }

        return outlist;
    }

    // P04637_9
    public List<Alignment> getPdbAlignmentByUniprotAccessionIso(String uniprotAccession, String isoform) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            System.out.println(uniprotlist.get(0).getSeqId());
            return alignmentRepository.findBySeqId(uniprotlist.get(0).getSeqId());
        } else {
            return new ArrayList<Alignment>();
        }
    }

    // P04637_9 2fej A
    public List<Alignment> getPdbAlignmentByUniprotAccessionIso(String uniprotAccession, String isoform, String pdbId,
            String chain) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            List<Alignment> list = alignmentRepository.findBySeqId(uniprotlist.get(0).getSeqId());
            List<Alignment> outlist = new ArrayList<Alignment>();

            for (Alignment ali : list) {
                String pd = ali.getPdbId().toLowerCase();
                String ch = ali.getChain().toLowerCase();
                if (pd.equals(pdbId.toLowerCase()) && ch.equals(chain.toLowerCase())) {
                    outlist.add(ali);
                }
            }
            return outlist;
        } else {
            return null;
        }
    }

    // P53_HUMAN_9
    public List<Alignment> getPdbAlignmentByUniprotIdIso(String uniprotId, String isoform) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbAlignmentByUniprotAccessionIso(it.next(), isoform));
        }

        return outlist;

    }

    // P53_HUMAN_9 2fej A
    public List<Alignment> getPdbAlignmentByUniprotIdIso(String uniprotId, String isoform, String pdbId, String chain) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbAlignmentByUniprotAccessionIso(it.next(), isoform, pdbId, chain));
        }

        return outlist;
    }

    /**
     * ResidueMapping parts support
     */

    /**
     * Implementation of API getPdbResidueByEnsemblIdGenome
     * 
     * @param chromosomeNum
     * @param position
     * @param nucleotideType
     * @param genomeVersion
     * @return
     */
    public List<Alignment> getPdbResidueByEnsemblIdGenome(String chromosomeNum, long position, String nucleotideType,
            String genomeVersion) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.callHumanGenomeAPI(chromosomeNum, position, nucleotideType, genomeVersion);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<GenomeResidueInput> grlistValid = new ArrayList<GenomeResidueInput>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grlistValid.add(gr);
                }
            }
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        if (grlistValid.size() >= 1) {
            for (GenomeResidueInput gr : grlistValid) {
                // System.out.println("Out:\t" + gr.getEnsembl().getSeqId() +
                // "\t:"
                // + Integer.toString(gr.getResidue().getResidueNum()));
                List<Alignment> list = seqController.getPdbResidueBySeqId(gr.getEnsembl().getSeqId(),
                        Integer.toString(gr.getResidue().getResidueNum()));
                outlist.addAll(list);
            }
        }
        return outlist;
    }

    /**
     * Implementation of API getPdbResidueByEnsemblIddbSNPID
     * 
     * @param dbSNPID
     * @return
     */
    public List<Alignment> getPdbResidueByEnsemblIddbSNPID(String dbSNPID) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.calldbSNPAPI(dbSNPID);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<GenomeResidueInput> grlistValid = new ArrayList<GenomeResidueInput>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grlistValid.add(gr);
                }
            }
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        if (grlistValid.size() >= 1) {
            for (GenomeResidueInput gr : grlistValid) {
                // System.out.println("Out:\t" + gr.getEnsembl().getSeqId() +
                // "\t:"
                // + Integer.toString(gr.getResidue().getResidueNum()));
                List<Alignment> list = seqController.getPdbResidueBySeqId(gr.getEnsembl().getSeqId(),
                        Integer.toString(gr.getResidue().getResidueNum()));
                outlist.addAll(list);
            }
        }
        return outlist;
    }

    // P04637
    public List<Alignment> getPdbResidueByUniprotAccession(String uniprotAccession) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotAccession(uniprotAccession);
        List<Alignment> outList = new ArrayList<Alignment>();
        for (Uniprot entry : uniprotList) {
            outList.addAll(seqController.getPdbResidueBySeqId(entry.getSeqId()));
        }
        return outList;
    }

    // P04637, 100
    public List<Alignment> getPdbResidueByUniprotAccession(String uniprotAccession, String position) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotAccession(uniprotAccession);
        List<Alignment> outList = new ArrayList<Alignment>();
        for (Uniprot entry : uniprotList) {
            outList.addAll(seqController.getPdbResidueBySeqId(entry.getSeqId(), position));
        }
        return outList;
    }

    // P04637, 99,100
    public List<Alignment> getPdbResidueByUniprotAccession(String uniprotAccession, List<String> positionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotAccession(uniprotAccession);
        ArrayList<Alignment> outList = new ArrayList<Alignment>();
        for (Uniprot entry : uniprotList) {
            outList.addAll(seqController.getPdbResidueBySeqId(entry.getSeqId(), positionList));
        }
        return outList;
    }

    // P53_HUMAN
    public List<Alignment> getPdbResidueByUniprotId(String uniprotId) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccession(it.next()));
        }

        return outlist;

    }

    // P53_HUMAN 100
    public List<Alignment> getPdbResidueByUniprotId(String uniprotId, String position) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccession(it.next(), position));
        }

        return outlist;

    }

    // P53_HUMAN 99,100
    public List<Alignment> getPdbResidueByUniprotId(String uniprotId, List<String> positionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccession(it.next(), positionList));
        }

        return outlist;

    }

    // P04637_9
    public List<Alignment> getPdbResidueByUniprotAccessionIso(String uniprotAccession, String isoform) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getPdbResidueBySeqId(uniprotlist.get(0).getSeqId());
        } else {
            return new ArrayList<Alignment>();
        }
    }

    // P04637_9 100
    public List<Alignment> getPdbResidueByUniprotAccessionIso(String uniprotAccession, String isoform,
            String position) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getPdbResidueBySeqId(uniprotlist.get(0).getSeqId(), position);
        } else {
            return new ArrayList<Alignment>();
        }
    }

    // P04637_9 99,100
    public List<Alignment> getPdbResidueByUniprotAccessionIso(String uniprotAccession, String isoform,
            List<String> positionList) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getPdbResidueBySeqId(uniprotlist.get(0).getSeqId(), positionList);
        } else {
            return new ArrayList<Alignment>();
        }
    }

    // P53_HUMAN_9
    public List<Alignment> getPdbResidueByUniprotIdIso(String uniprotId, String isoform) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccessionIso(it.next(), isoform));
        }

        return outlist;
    }

    // P53_HUMAN_9 100
    public List<Alignment> getPdbResidueByUniprotIdIso(String uniprotId, String isoform, String position) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccessionIso(it.next(), isoform, position));
        }

        return outlist;
    }

    // P53_HUMAN_9 99,100
    public List<Alignment> getPdbResidueByUniprotIdIso(String uniprotId, String isoform, List<String> positionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Alignment> outlist = new ArrayList<Alignment>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getPdbResidueByUniprotAccessionIso(it.next(), isoform, positionList));
        }

        return outlist;
    }
    
    
    //Mutation Related:
    /**
     * "Get All MutationUsageTable by Protein SeqId
     * 
     * @param seqId
     * @return
     */
    public List<Mutation> getMutationUsageBySeqId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {
        
        List<MutationUsageTable> it = mutationUsageTableRepository.findBySeqId(Integer.parseInt(seqId));
        List<Mutation> outit = new ArrayList<>();
        
        HashMap<String,List<MutatedResidue>> hm = new HashMap<>();
        
        String proteinName = "";
        for(MutationUsageTable entry: it){
            proteinName = entry.getSeqName();
            String key = Integer.toString(entry.getSeqIndex())+"\t"+entry.getSeqResidue();
            MutatedResidue mr = new MutatedResidue();
            String[] pdbNoUse = entry.getPdbNo().split("_");
            //System.out.println(pdbNoUse[0]+"_"+pdbNoUse[1]);
            mr.setPdbNo(pdbNoUse[0]+"_"+pdbNoUse[1]);
            mr.setPdbPos(entry.getPdbIndex());
            mr.setPdbResidue(entry.getPdbResidue());
            List<MutatedResidue> list = new ArrayList<>();
            if (hm.containsKey(key)){
                list = hm.get(key);
                list.add(mr);
            }else{
                list.add(mr);
            }
            hm.put(key, list);           
        }
        
        Mutation entry = new Mutation();
        entry.setProteinName(proteinName);
        List<MutatedPosition> mplist = new ArrayList<>();
        
        SortedSet<String> keys = new TreeSet<>(hm.keySet());
        for (String key: keys){            
            int proteinPos = Integer.parseInt(key.split("\t")[0]);
            String proteinResidue = key.split("\t")[1];
            MutatedPosition mp = new MutatedPosition();            
            mp.setProteinPos(proteinPos);
            mp.setProteinResidue(proteinResidue);
            mp.setMutatedResidue(hm.get(key));
            mplist.add(mp);
        }
        entry.setMutatedPosition(mplist);
        outit.add(entry);
        return outit;
    }
    
    /**
     * "Get All MutationUsageTable by Protein SeqId, only output specific position
     * 
     * @param seqId
     * @return
     */
    public List<Mutation> getMutationUsageBySeqId(
            @ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId,
            @ApiParam(required = true, value = "Input Residue Position e.g. 99,100") @PathVariable List<String> positionList) {
        
        List<MutationUsageTable> it = mutationUsageTableRepository.findBySeqId(Integer.parseInt(seqId));
        List<Mutation> outit = new ArrayList<>();
        
        HashMap<String,List<MutatedResidue>> hm = new HashMap<>();
        
        HashSet<String> posSet = new HashSet<>();
        for(String pos: positionList){
            posSet.add(pos);
        }
        
        String proteinName = "";
        for(MutationUsageTable entry: it){
            if(posSet.contains(Integer.toString(entry.getSeqIndex()))){
                proteinName = entry.getSeqName();
                String key = Integer.toString(entry.getSeqIndex())+"\t"+entry.getSeqResidue();
                MutatedResidue mr = new MutatedResidue();
                String[] pdbNoUse = entry.getPdbNo().split("_");
                mr.setPdbNo(pdbNoUse[0]+"_"+pdbNoUse[1]);
                mr.setPdbPos(entry.getPdbIndex());
                mr.setPdbResidue(entry.getPdbResidue());
                List<MutatedResidue> list = new ArrayList<>();
                if (hm.containsKey(key)){
                    list = hm.get(key);
                    list.add(mr);
                }else{
                    list.add(mr);
                }
                hm.put(key, list);                 
            }                     
        }
        
        Mutation entry = new Mutation();
        entry.setProteinName(proteinName);
        List<MutatedPosition> mplist = new ArrayList<>();
        SortedSet<String> keys = new TreeSet<>(hm.keySet());
        for (String key: keys){           
            int proteinPos = Integer.parseInt(key.split("\t")[0]);
            String proteinResidue = key.split("\t")[1];
            MutatedPosition mp = new MutatedPosition();            
            mp.setProteinPos(proteinPos);
            mp.setProteinResidue(proteinResidue);
            mp.setMutatedResidue(hm.get(key));
            mplist.add(mp);
        }
        entry.setMutatedPosition(mplist);
        outit.add(entry);
        
        return outit;
    }
    
    
    // P04637_9
    public List<Mutation> getMutationUsageByUniprotAccessionIso(String uniprotAccession, String isoform) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getMutationUsageBySeqId(uniprotlist.get(0).getSeqId());
        } else {
            return new ArrayList<Mutation>();
        }
    }

    // P04637_9 99,100
    public List<Mutation> getMutationUsageByUniprotAccessionIso(String uniprotAccession, String isoform,
            List<String> positionList) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getMutationUsageBySeqId(uniprotlist.get(0).getSeqId(), positionList);
        } else {
            return new ArrayList<Mutation>();
        }
    }

    // P53_HUMAN_9
    public List<Mutation> getMutationUsageByUniprotIdIso(String uniprotId, String isoform) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Mutation> outlist = new ArrayList<Mutation>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getMutationUsageByUniprotAccessionIso(it.next(), isoform));
        }

        return outlist;
    }

    // P53_HUMAN_9 99,100
    public List<Mutation> getMutationUsageByUniprotIdIso(String uniprotId, String isoform, List<String> positionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<Mutation> outlist = new ArrayList<Mutation>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getMutationUsageByUniprotAccessionIso(it.next(), isoform, positionList));
        }

        return outlist;
    }
    
    /**
     * Implementation of API getMutationUsageByEnsemblIdGenome
     * 
     * @param chromosomeNum
     * @param position
     * @param nucleotideType
     * @param genomeVersion
     * @return
     */
    public List<Mutation> getMutationUsageByEnsemblIdGenome(String chromosomeNum, long position, String nucleotideType,
            String genomeVersion) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.callHumanGenomeAPI(chromosomeNum, position, nucleotideType, genomeVersion);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        Set<String> grsetValid = new HashSet<>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grsetValid.add(gr.getEnsembl().getSeqId()+"_"+gr.getResidue().getResidueNum());
                }
            }
        }

        List<Mutation> outlist = new ArrayList<Mutation>();
        if (grsetValid.size() >= 1) {
            for (String gr : grsetValid) {
                //System.out.println("Out:\t"+gr);
                String[] arrays = gr.split("_");
            	List<String> tmpStringList = new ArrayList<>();
            	tmpStringList.add(arrays[1]);
                List<Mutation> list = seqController.getMutationUsageBySeqId(arrays[0],
                		tmpStringList);
                outlist.addAll(list);
            }
        }
        return outlist;
    }

    /**
     * Implementation of API getMutationUsageByEnsemblIddbSNPID
     * 
     * @param dbSNPID
     * @return
     */
    public List<Mutation> getMutationUsageByEnsemblIddbSNPID(String dbSNPID) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.calldbSNPAPI(dbSNPID);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        Set<String> grsetValid = new HashSet<>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grsetValid.add(gr.getEnsembl().getSeqId()+"_"+gr.getResidue().getResidueNum());
                }
            }
        }

        List<Mutation> outlist = new ArrayList<Mutation>();
        if (grsetValid.size() >= 1) {
            for (String gr : grsetValid) {
                //System.out.println("Out:\t"+gr);
                String[] arrays = gr.split("_");
            	List<String> tmpStringList = new ArrayList<>();
            	tmpStringList.add(arrays[1]);
                List<Mutation> list = seqController.getMutationUsageBySeqId(arrays[0],
                		tmpStringList);
                outlist.addAll(list);
            }
        }
        
        return outlist;
    }
    
    //Mutation Annotation
    /**
     * "Get All MutationUsageTable by Protein SeqId
     * 
     * @param seqId
     * @return
     */
	public List<MutationAnnotation> getMutationUsageAnnotationBySeqId(
			@ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId) {

		List<MutationUsageTable> it = mutationUsageTableRepository.findBySeqId(Integer.parseInt(seqId));
		List<MutationAnnotation> outit = new ArrayList<>();

		HashMap<String, List<MutatedResidueInfo>> hm = new HashMap<>();

		String proteinName = "";

		for (MutationUsageTable entry : it) {
			proteinName = entry.getSeqName();
			String key = Integer.toString(entry.getSeqIndex()) + "\t" + entry.getSeqResidue();
			MutatedResidueInfo mr = new MutatedResidueInfo();
			String[] pdbNoUse = entry.getPdbNo().split("_");
			// System.out.println(pdbNoUse[0]+"_"+pdbNoUse[1]);
			mr.setPdbNo(pdbNoUse[0] + "_" + pdbNoUse[1]);
			mr.setPdbPos(entry.getPdbIndex());
			mr.setPdbResidue(entry.getPdbResidue());
			// TODO, can improve use OO Design
			String queryPdbNo = pdbNoUse[0] + "_" + pdbNoUse[1] + "_" + entry.getPdbIndex();
			//System.out.println(queryPdbNo);

			StructureAnnotation ma = structureAnnotationRepository.findTopByPdbAnnoKey(queryPdbNo);

			StructureAnnotationInfo maInfo = new StructureAnnotationInfo();
			try {
				maInfo = new StructureAnnotationInfo(ma);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			mr.setStructureAnnotationInfo(maInfo);

			List<MutatedResidueInfo> list = new ArrayList<>();
			if (hm.containsKey(key)) {
				list = hm.get(key);
				list.add(mr);
			} else {
				list.add(mr);
			}
			hm.put(key, list);
		}

		MutationAnnotation entry = new MutationAnnotation();
		entry.setProteinName(proteinName);
		List<MutatedPositionInfo> mplist = new ArrayList<>();

		SortedSet<String> keys = new TreeSet<>(hm.keySet());
		for (String key : keys) {
			int proteinPos = Integer.parseInt(key.split("\t")[0]);
			String proteinResidue = key.split("\t")[1];
			MutatedPositionInfo mp = new MutatedPositionInfo();
			mp.setProteinPos(proteinPos);
			mp.setProteinResidue(proteinResidue);
			mp.setMutatedResidueInfo(hm.get(key));
			List<Dbsnp> dbsnpList = dbsnpRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setDbsnpAnnotation(dbsnpList);
			List<Clinvar> clinvarList = clinvarRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setClinvarAnnotation(clinvarList);
			List<Cosmic> cosmicList = cosmicRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setCosmicAnnotation(cosmicList);
			//Do not redistribute genie
			//List<Genie> genieList = genieRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			//mp.setGenieAnnotation(genieList);
			List<Tcga> tcgaList = tcgaRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setTcgaAnnotation(tcgaList);
			mplist.add(mp);
		}
		entry.setMutatedPositionInfo(mplist);
		outit.add(entry);
		return outit;
	}
    
    /**
     * "Get All MutationUsageTable by Protein SeqId
     * 
     * @param seqId
     * @return
     */
	public List<MutationAnnotation> getMutationUsageAnnotationBySeqId(
			@ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId,
			@ApiParam(required = true, value = "Input Residue Position e.g. 99,100") @PathVariable List<String> positionList) {

		List<MutationUsageTable> it = mutationUsageTableRepository.findBySeqId(Integer.parseInt(seqId));
		List<MutationAnnotation> outit = new ArrayList<>();

		HashMap<String, List<MutatedResidueInfo>> hm = new HashMap<>();

		HashSet<String> posSet = new HashSet<>();
		for (String pos : positionList) {
			posSet.add(pos);
		}

		String proteinName = "";

		for (MutationUsageTable entry : it) {
			if (posSet.contains(Integer.toString(entry.getSeqIndex()))) {
				proteinName = entry.getSeqName();
				String key = Integer.toString(entry.getSeqIndex()) + "\t" + entry.getSeqResidue();
				MutatedResidueInfo mr = new MutatedResidueInfo();
				String[] pdbNoUse = entry.getPdbNo().split("_");
				// System.out.println(pdbNoUse[0]+"_"+pdbNoUse[1]);
				mr.setPdbNo(pdbNoUse[0] + "_" + pdbNoUse[1]);
				mr.setPdbPos(entry.getPdbIndex());
				mr.setPdbResidue(entry.getPdbResidue());
				// TODO, can improve use OO Design
				// queryPdbNo: 2pcx_A_282
				String queryPdbNo = pdbNoUse[0] + "_" + pdbNoUse[1] + "_" + entry.getPdbIndex();

				StructureAnnotation ma = structureAnnotationRepository.findTopByPdbAnnoKey(queryPdbNo);
				StructureAnnotationInfo maInfo = new StructureAnnotationInfo();
				try {
					maInfo = new StructureAnnotationInfo(ma);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				mr.setStructureAnnotationInfo(maInfo);

				List<MutatedResidueInfo> list = new ArrayList<>();
				if (hm.containsKey(key)) {
					list = hm.get(key);
					list.add(mr);
				} else {
					list.add(mr);
				}
				hm.put(key, list);
			}
		}

		MutationAnnotation entry = new MutationAnnotation();
		entry.setProteinName(proteinName);
		List<MutatedPositionInfo> mplist = new ArrayList<>();

		SortedSet<String> keys = new TreeSet<>(hm.keySet());
		for (String key : keys) {
			int proteinPos = Integer.parseInt(key.split("\t")[0]);
			String proteinResidue = key.split("\t")[1];
			MutatedPositionInfo mp = new MutatedPositionInfo();
			mp.setProteinPos(proteinPos);
			mp.setProteinResidue(proteinResidue);
			mp.setMutatedResidueInfo(hm.get(key));
			List<Dbsnp> dbsnpList = dbsnpRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setDbsnpAnnotation(dbsnpList);
			List<Clinvar> clinvarList = clinvarRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setClinvarAnnotation(clinvarList);
			List<Cosmic> cosmicList = cosmicRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setCosmicAnnotation(cosmicList);
			// No Redistributed genie project
			//List<Genie> genieList = genieRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			//mp.setGenieAnnotation(genieList);
			List<Tcga> tcgaList = tcgaRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setTcgaAnnotation(tcgaList);
			mplist.add(mp);
		}
		entry.setMutatedPositionInfo(mplist);
		outit.add(entry);
		return outit;
	}
	
    /**
     * "Get All MutationUsageTable by Protein SeqId, use residue in PDB as the pdbPosistionList
     * 
     * @param seqId
     * @return
     */
	public List<MutationAnnotation> getMutationUsageAnnotationBySeqIdPDB(
			@ApiParam(required = true, value = "Input SeqId e.g. 25625") @PathVariable String seqId,
			@ApiParam(required = true, value = "Input PDB Residue Position e.g. 99,100") @PathVariable List<String> pdbPositionList) {

		List<MutationUsageTable> it = mutationUsageTableRepository.findBySeqId(Integer.parseInt(seqId));
		List<MutationAnnotation> outit = new ArrayList<>();

		HashMap<String, List<MutatedResidueInfo>> hm = new HashMap<>();

		HashSet<String> posSet = new HashSet<>();
		for (String pos : pdbPositionList) {
			posSet.add(pos);
		}

		String proteinName = "";

		for (MutationUsageTable entry : it) {
			if (posSet.contains(Integer.toString(entry.getPdbIndex()))) {
				proteinName = entry.getSeqName();
				String key = Integer.toString(entry.getSeqIndex()) + "\t" + entry.getSeqResidue();
				MutatedResidueInfo mr = new MutatedResidueInfo();
				String[] pdbNoUse = entry.getPdbNo().split("_");
				// System.out.println(pdbNoUse[0]+"_"+pdbNoUse[1]);
				mr.setPdbNo(pdbNoUse[0] + "_" + pdbNoUse[1]);
				mr.setPdbPos(entry.getPdbIndex());
				mr.setPdbResidue(entry.getPdbResidue());
				// TODO, can improve use OO Design
				// queryPdbNo: 2pcx_A_282
				String queryPdbNo = pdbNoUse[0] + "_" + pdbNoUse[1] + "_" + entry.getPdbIndex();
				System.out.println("queryPdbNo:"+queryPdbNo);

				StructureAnnotation ma = structureAnnotationRepository.findTopByPdbAnnoKey(queryPdbNo);
				StructureAnnotationInfo maInfo = new StructureAnnotationInfo();
				try {
					maInfo = new StructureAnnotationInfo(ma);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				mr.setStructureAnnotationInfo(maInfo);

				List<MutatedResidueInfo> list = new ArrayList<>();
				if (hm.containsKey(key)) {
					list = hm.get(key);
					list.add(mr);
				} else {
					list.add(mr);
				}
				hm.put(key, list);
			}
		}

		MutationAnnotation entry = new MutationAnnotation();
		entry.setProteinName(proteinName);
		List<MutatedPositionInfo> mplist = new ArrayList<>();

		SortedSet<String> keys = new TreeSet<>(hm.keySet());
		for (String key : keys) {
			int proteinPos = Integer.parseInt(key.split("\t")[0]);
			String proteinResidue = key.split("\t")[1];
			MutatedPositionInfo mp = new MutatedPositionInfo();
			mp.setProteinPos(proteinPos);
			mp.setProteinResidue(proteinResidue);
			mp.setMutatedResidueInfo(hm.get(key));
			List<Dbsnp> dbsnpList = dbsnpRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setDbsnpAnnotation(dbsnpList);
			List<Clinvar> clinvarList = clinvarRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setClinvarAnnotation(clinvarList);
			List<Cosmic> cosmicList = cosmicRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setCosmicAnnotation(cosmicList);
			// No Redistributed genie project
			//List<Genie> genieList = genieRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			//mp.setGenieAnnotation(genieList);
			List<Tcga> tcgaList = tcgaRepository.findByMutationNo(seqId + "_" + Integer.toString(proteinPos));
			mp.setTcgaAnnotation(tcgaList);
			mplist.add(mp);
		}
		entry.setMutatedPositionInfo(mplist);
		outit.add(entry);
		return outit;
	}
    
 // P04637_9
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotAccessionIso(String uniprotAccession, String isoform) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getMutationUsageAnnotationBySeqId(uniprotlist.get(0).getSeqId());
        } else {
            return new ArrayList<MutationAnnotation>();
        }
    }

    // P04637_9 99,100
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotAccessionIso(String uniprotAccession, String isoform,
            List<String> positionList) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getMutationUsageAnnotationBySeqId(uniprotlist.get(0).getSeqId(), positionList);
        } else {
            return new ArrayList<MutationAnnotation>();
        }
    }
    
    /**
     * P04637_9 99,100
     * 
     * @param uniprotAccession
     * @param isoform
     * @param pdbPositionList
     * @return
     */
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotAccessionIsoPDB(String uniprotAccession, String isoform,
            List<String> pdbPositionList) {

        List<Uniprot> uniprotlist = uniprotRepository.findByUniprotAccessionIso(uniprotAccession + "_" + isoform);
        if (uniprotlist.size() == 1) {
            return seqController.getMutationUsageAnnotationBySeqIdPDB(uniprotlist.get(0).getSeqId(), pdbPositionList);
        } else {
            return new ArrayList<MutationAnnotation>();
        }
    }

    // P53_HUMAN_9
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotIdIso(String uniprotId, String isoform) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<MutationAnnotation> outlist = new ArrayList<MutationAnnotation>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getMutationUsageAnnotationByUniprotAccessionIso(it.next(), isoform));
        }

        return outlist;
    }

    // P53_HUMAN_9 99,100
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotIdIso(String uniprotId, String isoform, List<String> positionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<MutationAnnotation> outlist = new ArrayList<MutationAnnotation>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getMutationUsageAnnotationByUniprotAccessionIso(it.next(), isoform, positionList));
        }

        return outlist;
    }
    

    /**
     * P53_HUMAN_9 99,100
     *  
     * @param uniprotId
     * @param isoform
     * @param pdbPositionList
     * @return
     */
    public List<MutationAnnotation> getMutationUsageAnnotationByUniprotIdIsoPDB(String uniprotId, String isoform, List<String> pdbPositionList) {

        List<Uniprot> uniprotList = uniprotRepository.findByUniprotId(uniprotId);

        Set<String> uniprotAccSet = new HashSet<String>();
        for (Uniprot uniprot : uniprotList) {
            uniprotAccSet.add(uniprot.getUniprotAccession());
        }

        List<MutationAnnotation> outlist = new ArrayList<MutationAnnotation>();
        Iterator<String> it = uniprotAccSet.iterator();
        while (it.hasNext()) {
            outlist.addAll(getMutationUsageAnnotationByUniprotAccessionIsoPDB(it.next(), isoform, pdbPositionList));
        }

        return outlist;
    }
    
    /**
     * Implementation of API getMutationUsageAnnotationByEnsemblIdGenome
     * 
     * @param chromosomeNum
     * @param position
     * @param nucleotideType
     * @param genomeVersion
     * @return
     */
    public List<MutationAnnotation> getMutationUsageAnnotationByEnsemblIdGenome(String chromosomeNum, long position, String nucleotideType,
            String genomeVersion) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.callHumanGenomeAPI(chromosomeNum, position, nucleotideType, genomeVersion);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Set<String> grsetValid = new HashSet<>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grsetValid.add(gr.getEnsembl().getSeqId()+"_"+gr.getResidue().getResidueNum());
                }
            }
        }

        List<MutationAnnotation> outlist = new ArrayList<MutationAnnotation>();
        if (grsetValid.size() >= 1) {
            for (String gr : grsetValid) {
                //System.out.println("Out:\t"+gr);
                String[] arrays = gr.split("_");
            	List<String> tmpStringList = new ArrayList<>();
            	tmpStringList.add(arrays[1]);
                List<MutationAnnotation> list = seqController.getMutationUsageAnnotationBySeqId(arrays[0],
                		tmpStringList);
                outlist.addAll(list);
            }
        }
        return outlist;
    }

    /**
     * Implementation of API getMutationUsageAnnotationByEnsemblIddbSNPID
     * 
     * @param dbSNPID
     * @return
     */
    public List<MutationAnnotation> getMutationUsageAnnotationByEnsemblIddbSNPID(String dbSNPID) {
        // Calling GenomeNexus
        UtilAPI uapi = new UtilAPI();

        List<GenomeResidueInput> grlist = new ArrayList<GenomeResidueInput>();
        try {
            grlist = uapi.calldbSNPAPI(dbSNPID);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            // org.springframework.web.client.HttpClientErrorException: 400 Bad
            // Request
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Set<String> grsetValid = new HashSet<>();
        for (GenomeResidueInput gr : grlist) {
            List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(gr.getEnsembl().getEnsemblid());
            // System.out.println(gr.getEnsembl().getEnsemblid());

            for (Ensembl ensembl : ensembllist) {
                if (geneSequenceRepository.findBySeqId(ensembl.getSeqId()).size() != 0) {
                    Ensembl es = gr.getEnsembl();
                    es.setSeqId(ensembl.getSeqId());
                    // System.out.println("API
                    // ensemblID:\t"+es.getEnsemblid()+"\t:"+es.getSeqId());
                    gr.setEnsembl(es);
                    grsetValid.add(gr.getEnsembl().getSeqId()+"_"+gr.getResidue().getResidueNum());
                }
            }
        }

        List<MutationAnnotation> outlist = new ArrayList<MutationAnnotation>();
        if (grsetValid.size() >= 1) {
            for (String gr : grsetValid) {
                //System.out.println("Out:\t"+gr);
                String[] arrays = gr.split("_");
            	List<String> tmpStringList = new ArrayList<>();
            	tmpStringList.add(arrays[1]);
                List<MutationAnnotation> list = seqController.getMutationUsageAnnotationBySeqId(arrays[0],
                		tmpStringList);
                outlist.addAll(list);
            }
        }
        return outlist;
    }
    

}
