package org.cbioportal.G2Smutation.web.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineRunCommand;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.web.domain.EnsemblRepository;
import org.cbioportal.G2Smutation.web.domain.UniprotRepository;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.cbioportal.G2Smutation.web.models.InputAlignment;
import org.cbioportal.G2Smutation.web.models.InputSequence;
import org.cbioportal.G2Smutation.web.models.ResidueMapping;
import org.cbioportal.G2Smutation.web.models.Uniprot;
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
 * Main controller get ResidueMappings: Get ResidueMappings
 * 
 * @author wangjue
 *
 */
@RestController // shorthand for @Controller, @ResponseBody
@CrossOrigin(origins = "*") // allow all cross-domain requests
@Api(tags = "Get ResidueMapping", description = "ensembl/uniprot/hgvs/sequences")
@RequestMapping(value = "/api/")
public class MainGetResidueMappingController {

    final static Logger log = Logger.getLogger(MainGetResidueMappingController.class);

    @Autowired
    private EnsemblRepository ensemblRepository;
    @Autowired
    private SeqIdAlignmentController seqController;
    @Autowired
    private UniprotRepository uniprotRepository;

    @RequestMapping(value = "/alignments/{id_type}/{id:.+}/residueMapping", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("POST PDB Residue Mapping by ProteinId")
    public List<Alignment> postResidueMapping(
            @ApiParam(required = true, value = "Input id_type: ensembl; uniprot;"
                    + "uniprot_isoform;\n hgvs-grch37; hgvs-grch38; dbsnp") @PathVariable String id_type,
            @ApiParam(required = true, value = "Input id e.g.\n"
                    + "ensembl:ENSP00000484409.1/ENSG00000141510.16/ENST00000504290.5;\n"
                    + "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_9/P53_HUMAN_9;\n"
                    + "hgvs-grch37:17:g.79478130C>G;\n" + "hgvs-grch38:17:g.7676594T>G;\n"
                    + "dbsnp:rs1800369") @PathVariable String id,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,100; Anynumber for hgvs;\n"
                    + "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

        List<Alignment> outList = new ArrayList<Alignment>();
        if (id_type.equals("ensembl")) {
            if (id.startsWith("ENSP")) {// EnsemblID:
                // ENSP00000484409.1/ENSP00000484409
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
                for (Ensembl ensembl : ensembllist) {
                    if (positionList == null) {
                        outList.addAll(seqController.getPdbResidueBySeqId(ensembl.getSeqId()));
                    } else {
                        outList.addAll(seqController.getPdbResidueBySeqId(ensembl.getSeqId(), positionList));
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
                            outList.addAll(seqController.getPdbResidueBySeqId(en.getSeqId()));
                        } else {
                            outList.addAll(seqController.getPdbResidueBySeqId(en.getSeqId(), positionList));
                        }

                    }
                }
            } else if (id.startsWith("ENST")) {// EnsemblTranscript:
                // ENST00000504290.5/ENST00000504290
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
                if (ensembllist.size() >= 1) {
                    for (Ensembl en : ensembllist) {
                        if (positionList == null) {
                            outList.addAll(seqController.getPdbResidueBySeqId(en.getSeqId()));
                        } else {
                            outList.addAll(seqController.getPdbResidueBySeqId(en.getSeqId(), positionList));
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
                    outList.addAll(seqController.getPdbResidueByUniprotAccessionIso(id, "1"));
                } else {
                    outList.addAll(seqController.getPdbResidueByUniprotAccessionIso(id, "1", positionList));
                }

            } else if (id.split("_").length == 2) {// ID: P53_HUMAN
                if (positionList == null) {
                    outList.addAll(seqController.getPdbResidueByUniprotIdIso(id, "1"));
                } else {
                    outList.addAll(seqController.getPdbResidueByUniprotIdIso(id, "1", positionList));
                }

            } else {
                log.info("Error in Input. id_type:Uniprot id: " + id + " position:" + positionList);
            }

        } else if (id_type.equals("uniprot_isoform")) {
            if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession:
                // P04637
                if (positionList == null) {
                    outList.addAll(
                            seqController.getPdbResidueByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]));
                } else {
                    outList.addAll(seqController.getPdbResidueByUniprotAccessionIso(id.split("_")[0], id.split("_")[1],
                            positionList));
                }

            } else if (id.split("_").length == 3) {// ID: P53_HUMAN
                if (positionList == null) {
                    outList.addAll(seqController.getPdbResidueByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2]));
                } else {
                    outList.addAll(seqController.getPdbResidueByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2], positionList));
                }

            } else {
                log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
            }
        } else if (id_type.equals("hgvs-grch37")) {
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
        } else {
            log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList);
        }
        return outList;
    }

    @RequestMapping(value = "/alignments/{id_type}/{id:.+}/pdb/{pdb_id}_{chain_id}/residueMapping", method = {
            RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Post Residue Mapping by ProteinId, PDBId and Chain")
    public List<Alignment> postResidueMappingByPDB(
            @ApiParam(required = true, value = "Input id_type: ensembl; uniprot; uniprot_isoform; hgvs-grch37; hgvs-grch38") @PathVariable String id_type,
            @ApiParam(required = true, value = "Input id e.g. \n"
                    + "ensembl:ENSP00000484409.1/ENSG00000141510.16/ENST00000504290.5;\n"
                    + "uniprot:P04637/P53_HUMAN;\n" + "uniprot_isoform:P04637_9/P53_HUMAN_9;\n"
                    + "hgvs-grch37:17:g.79478130C>G;\n" + "hgvs-grch38:17:g.7676594T>G") @PathVariable String id,
            @ApiParam(required = true, value = "Input PDB Id e.g. 2fej") @PathVariable String pdb_id,
            @ApiParam(required = true, value = "Input Chain e.g. A") @PathVariable String chain_id,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,100 (Anynumber for hgvs);\n"
                    + "Return all residue mappings if none") @RequestParam(required = false) List<String> positionList) {

        ArrayList<Alignment> outList = new ArrayList<Alignment>();
        if (id_type.equals("ensembl")) {

            if (id.startsWith("ENSP")) {// EnsemblID:
                // ENSP00000484409.1/ENSP00000484409

                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblIdStartingWith(id);
                for (Ensembl ensembl : ensembllist) {
                    List<Alignment> list = null;
                    if (positionList == null) {
                        list = seqController.getPdbResidueBySeqId(ensembl.getSeqId());
                    } else {
                        list = seqController.getPdbResidueBySeqId(ensembl.getSeqId(), positionList);
                    }

                    for (Alignment ali : list) {
                        String pd = ali.getPdbId().toLowerCase();
                        String ch = ali.getChain().toLowerCase();
                        if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                            outList.add(ali);
                        }
                    }

                }

            } else if (id.startsWith("ENSG")) {// EnsemblGene:
                // ENSG00000141510.16/ENSG00000141510
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblGene(id);

                if (ensembllist.size() > 0) {
                    List<Alignment> list = null;
                    if (positionList == null) {
                        list = seqController.getPdbResidueBySeqId(ensembllist.get(0).getSeqId());
                    } else {
                        list = seqController.getPdbResidueBySeqId(ensembllist.get(0).getSeqId(), positionList);
                    }

                    for (Alignment ali : list) {
                        String pd = ali.getPdbId().toLowerCase();
                        String ch = ali.getChain().toLowerCase();
                        if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                            outList.add(ali);
                        }
                    }
                }

            } else if (id.startsWith("ENST")) {// EnsemblTranscript:
                // ENST00000504290.5/ENST00000504290
                List<Ensembl> ensembllist = ensemblRepository.findByEnsemblTranscript(id);
                if (ensembllist.size() > 0) {
                    List<Alignment> list = null;
                    if (positionList == null) {
                        list = seqController.getPdbResidueBySeqId(ensembllist.get(0).getSeqId());
                    } else {
                        list = seqController.getPdbResidueBySeqId(ensembllist.get(0).getSeqId(), positionList);
                    }

                    for (Alignment ali : list) {
                        String pd = ali.getPdbId().toLowerCase();
                        String ch = ali.getChain().toLowerCase();
                        if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                            outList.add(ali);
                        }
                    }
                }
            } else {
                log.info("Error in Input. id_type:Uniprot id: " + id + " By PDB:" + pdb_id + " id:" + chain_id
                        + " position:" + positionList);
            }

        } else if (id_type.equals("uniprot")) {
            if (id.length() == 6 && id.split("_").length != 2) {// Accession:
                // P04637

                List<Alignment> list = new ArrayList<Alignment>();

                if (positionList == null) {
                    list = seqController.getPdbResidueByUniprotAccessionIso(id, "1");
                } else {
                    list = seqController.getPdbResidueByUniprotAccessionIso(id, "1", positionList);
                }

                for (Alignment re : list) {
                    String pd = re.getPdbId().toLowerCase();
                    String ch = re.getChain().toLowerCase();
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                        outList.add(re);
                    }
                }

            } else if (id.split("_").length == 2) {// ID: P53_HUMAN

                List<Alignment> list = null;
                if (positionList == null) {
                    list = seqController.getPdbResidueByUniprotIdIso(id, "1");
                } else {
                    list = seqController.getPdbResidueByUniprotIdIso(id, "1", positionList);
                }

                for (Alignment re : list) {
                    String pd = re.getPdbId().toLowerCase();
                    String ch = re.getChain().toLowerCase();
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                        outList.add(re);
                    }
                }

            } else {
                log.info("Error in Input. id_type:Uniprot id: " + id + " By PDB:" + pdb_id + " id:" + chain_id
                        + " position:" + positionList);
            }

        } else if (id_type.equals("uniprot_isoform")) {
            if (id.split("_").length == 2 && id.split("_")[0].length() == 6) {// Accession:
                // P04637
                List<Alignment> list = null;
                if (positionList == null) {
                    list = seqController.getPdbResidueByUniprotAccessionIso(id.split("_")[0], id.split("_")[1]);
                } else {
                    list = seqController.getPdbResidueByUniprotAccessionIso(id.split("_")[0], id.split("_")[1],
                            positionList);
                }

                for (Alignment re : list) {
                    String pd = re.getPdbId().toLowerCase();
                    String ch = re.getChain().toLowerCase();
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                        outList.add(re);
                    }
                }

            } else if (id.split("_").length == 3) {// ID: P53_HUMAN
                List<Alignment> list = null;
                if (positionList == null) {
                    list = seqController.getPdbResidueByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2]);
                } else {
                    list = seqController.getPdbResidueByUniprotIdIso(id.split("_")[0] + "_" + id.split("_")[1],
                            id.split("_")[2], positionList);
                }

                for (Alignment re : list) {
                    String pd = re.getPdbId().toLowerCase();
                    String ch = re.getChain().toLowerCase();
                    if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                        outList.add(re);
                    }
                }

            } else {
                log.info("Error in Input. id_type:Uniprot_isoform id: " + id);
            }

        } else if (id_type.equals("hgvs-grch37")) {
            String genomeVersion = "GRCH37";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);

            List<Alignment> tmpList = seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType,
                    genomeVersion);

            for (Alignment residue : tmpList) {
                String pd = residue.getPdbId().toLowerCase();
                String ch = residue.getChain().toLowerCase();
                if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                    outList.add(residue);
                }
            }

        } else if (id_type.equals("hgvs-grch38")) {
            String genomeVersion = "GRCH38";

            String chromosomeNum = id.split(":g\\.")[0];
            String tmp = id.split(":g\\.")[1];
            long pos = Long.parseLong(tmp.substring(0, tmp.length() - 3));
            String nucleotideType = tmp.substring(tmp.length() - 3, tmp.length() - 2);

            List<Alignment> tmpList = seqController.getPdbResidueByEnsemblIdGenome(chromosomeNum, pos, nucleotideType,
                    genomeVersion);

            for (Alignment residue : tmpList) {
                String pd = residue.getPdbId().toLowerCase();
                String ch = residue.getChain().toLowerCase();
                if (pd.equals(pdb_id.toLowerCase()) && ch.equals(chain_id.toLowerCase())) {
                    outList.add(residue);
                }
            }

        } else {
            log.info("Error in Input. id_type:" + id_type + " id: " + id + " position:" + positionList + " PDB:"
                    + pdb_id + " ChainID:" + chain_id);
        }
        return outList;
    }

    @RequestMapping(value = "/alignments/residueMapping", method = { RequestMethod.GET,
            RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get PDB Residue Mapping by Protein Sequence and Residue position")
    public List<Alignment> getPdbAlignmentReisudeBySequence(
            @ApiParam(required = true, value = "Input Protein Sequence: ETGQSVNDPGNMSFVKETVDKLLKGYDIRLRPDFGGPP") @RequestParam String sequence,
            @ApiParam(required = false, value = "Input Residue Positions e.g. 10,20") @RequestParam(required = false) List<String> positionList,
            @ApiParam(required = false, value = "Default Blast Parameters:\n"
                    + " Evalue=1e-10,Wordsize=3,Gapopen=11,Gapextend=1,\n" + " Matrix=BLOSUM62,Comp_based_stats=2,\n"
                    + "Threshold=11,Windowsize=40") @RequestParam(required = false) List<String> paramList
    /*
     * @ApiParam(value = "Blast Parameters Evalue: (Default) 1e-10") String
     * evalue,
     * 
     * @ApiParam(value = "Blast Parameters Wordsize: (Default) 3") String
     * wordsize,
     * 
     * @ApiParam(value = "Blast Parameters Gapopen: (Default) 11") String
     * gapopen,
     * 
     * @ApiParam(value = "Blast Parameters Gapextend: (Default) 1") String
     * gapextend,
     * 
     * @ApiParam(value = "Blast Parameters Matrix: (Default) BLOSUM62") String
     * matrix,
     * 
     * @ApiParam(value = "Blast Parameters Comp_based_stats: (Default) 2")
     * String compbasedstats,
     * 
     * @ApiParam(value = "Blast Parameters Threshold: (Default) 11") String
     * threshold,
     * 
     * @ApiParam(value = "Blast Parameters Windowsize: (Default) 40") String
     * windowsize
     */) {

        InputSequence inputsequence = new InputSequence();
        inputsequence.setSequence(sequence);
        inputsequence.setResidueNumList(positionList);

        ReadConfig rc = ReadConfig.getInstance();

        // Use default first
        inputsequence.setEvalue(rc.getBlastEvalue());
        inputsequence.setWord_size(rc.getBlastWordsize());
        inputsequence.setGapopen(rc.getBlastGapopen());
        inputsequence.setGapextend(rc.getBlastGapextend());
        inputsequence.setMatrix(rc.getBlastMatrix());
        inputsequence.setComp_based_stats(rc.getBlastComp());
        inputsequence.setThreshold(rc.getBlastThreshold());
        inputsequence.setWindow_size(rc.getBlastWindowsize());

        // If have user defined input, use them
        if (paramList != null) {
            for (String param : paramList) {
                String tmp[] = param.split("=");
                if (tmp[0].equals("Evalue")) {
                    inputsequence.setEvalue(tmp[1]);
                } else if (tmp[0].equals("Wordsize")) {
                    inputsequence.setWord_size(tmp[1]);
                } else if (tmp[0].equals("Gapopen")) {
                    inputsequence.setGapopen(tmp[1]);
                } else if (tmp[0].equals("Gapextend")) {
                    inputsequence.setGapextend(tmp[1]);
                } else if (tmp[0].equals("Matrix")) {
                    inputsequence.setMatrix(tmp[1]);
                } else if (tmp[0].equals("Comp_based_stats")) {
                    inputsequence.setComp_based_stats(tmp[1]);
                } else if (tmp[0].equals("Threshold")) {
                    inputsequence.setThreshold(tmp[1]);
                } else if (tmp[0].equals("Windowsize")) {
                    inputsequence.setWindow_size(tmp[1]);
                }
            }
        }

        PdbScriptsPipelineRunCommand pdbScriptsPipelineRunCommand = new PdbScriptsPipelineRunCommand();
        List<InputAlignment> alignments = pdbScriptsPipelineRunCommand.runCommand(inputsequence);

        List<Alignment> result = new ArrayList<Alignment>();

        for (InputAlignment ali : alignments) {

            Alignment rm = new Alignment();

            rm.setAlignmentId(ali.getAlignmentId());
            rm.setBitscore((float) ali.getBitscore());
            rm.setChain(ali.getChain());
            rm.setSeqAlign(ali.getSeqAlign());
            rm.setSeqFrom(ali.getSeqFrom());
            rm.setSeqId(ali.getSeqId());
            rm.setSeqTo(ali.getSeqTo());
            rm.setSegStart(ali.getSegStart());
            rm.setEvalue(Double.toString(ali.getEvalue()));
            rm.setIdentity(ali.getIdentity());
            rm.setIdentityPositive(ali.getIdentp());
            rm.setMidlineAlign(ali.getMidlineAlign());
            rm.setPdbAlign(ali.getPdbAlign());
            rm.setPdbFrom(ali.getPdbFrom());
            rm.setPdbId(ali.getPdbId());
            rm.setPdbNo(ali.getPdbNo());
            rm.setPdbSeg(ali.getPdbSeg());
            rm.setPdbTo(ali.getPdbTo());

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date today = Calendar.getInstance().getTime();
            rm.setUpdateDate(df.format(today));

            List<ResidueMapping> residueMapping = new ArrayList<ResidueMapping>();

            // if null, return all the mapping
            if (positionList == null) {
                for (int inputAA = ali.getSeqFrom(); inputAA <= ali.getSeqTo(); inputAA++) {
                    ResidueMapping rp = new ResidueMapping();

                    rp.setQueryAminoAcid(
                            ali.getSeqAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                    rp.setQueryPosition(inputAA - ali.getSeqFrom() + 1);

                    rp.setPdbAminoAcid(
                            ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                    rp.setPdbPosition(
                            Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom() + (inputAA - ali.getSeqFrom()));
                    // Withdraw if mapped to linker of the protein
                    if (!rp.getPdbAminoAcid().equals("X")) {
                        residueMapping.add(rp);
                    }
                }

            } else {
                for (String position : positionList) {
                    int inputAA = Integer.parseInt(position);
                    if (inputAA >= ali.getSeqFrom() && inputAA <= ali.getSeqTo()) {
                        ResidueMapping rp = new ResidueMapping();
                        rp.setQueryAminoAcid(sequence.substring(inputAA - 1, inputAA));
                        rp.setQueryPosition(inputAA);
                        rp.setPdbAminoAcid(ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(),
                                inputAA - ali.getSeqFrom() + 1));
                        rp.setPdbPosition(Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom()
                                + (inputAA - ali.getSeqFrom()));
                        // Withdraw if mapped to linker of the protein
                        if (!rp.getPdbAminoAcid().equals("X")) {
                            residueMapping.add(rp);
                        }
                    }
                }
            }

            rm.setResidueMapping(residueMapping);

            result.add(rm);

        }

        return result;
    }

}
