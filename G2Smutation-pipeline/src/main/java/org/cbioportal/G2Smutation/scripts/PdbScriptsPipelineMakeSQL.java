package org.cbioportal.G2Smutation.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.util.blast.BlastDataBase;
import org.cbioportal.G2Smutation.util.blast.BlastOutput;
import org.cbioportal.G2Smutation.util.blast.BlastOutputIterations;
import org.cbioportal.G2Smutation.util.blast.BlastResult;
import org.cbioportal.G2Smutation.util.blast.Hit;
import org.cbioportal.G2Smutation.util.blast.Hsp;
import org.cbioportal.G2Smutation.util.blast.Iteration;
import org.cbioportal.G2Smutation.util.blast.IterationHits;
import org.cbioportal.G2Smutation.util.models.MutationRecord;

/**
 * SQL Insert statements Generation
 * 
 * @author Juexin Wang
 *
 */

public class PdbScriptsPipelineMakeSQL {
    final static Logger log = Logger.getLogger(PdbScriptsPipelineMakeSQL.class);
    private static final String HTTP_AGENT_PROPERTY_VALUE = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private BlastDataBase db;
    private int matches;
    private int seqFileCount;
    private String workspace;
    private String sqlInsertFile;
    private String sqlInsertOutputInterval;
    private String sqlDeleteFile;
    private String insertSequenceSQL;
    private boolean updateTag;// if update, then true;

    private int testcase;// use for test cases

    /**
     * 
     * Constructor
     * 
     * @param app
     */
    PdbScriptsPipelineMakeSQL(PdbScriptsPipelineRunCommand app) {
        this.db = app.getDb();
        this.matches = app.getMatches();
        this.seqFileCount = app.getSeqFileCount();
        this.workspace = ReadConfig.workspace;
        this.sqlInsertFile = ReadConfig.sqlInsertFile;
        this.sqlInsertOutputInterval = ReadConfig.sqlInsertOutputInterval;
        this.sqlDeleteFile = ReadConfig.sqlDeleteFile;
        this.insertSequenceSQL = ReadConfig.insertSequenceSQL;
        this.updateTag = app.isUpdateTag();
    }

    /**
     * 
     * Constructor for test
     * 
     * @param app
     */
    PdbScriptsPipelineMakeSQL(PdbScriptsPipelineRunCommand app, int testcase) {
        this.db = app.getDb();
        this.matches = app.getMatches();
        this.seqFileCount = app.getSeqFileCount();
        this.workspace = ReadConfig.workspace;
        this.sqlInsertFile = ReadConfig.sqlInsertFile;
        this.sqlInsertOutputInterval = ReadConfig.sqlInsertOutputInterval;
        this.sqlDeleteFile = ReadConfig.sqlDeleteFile;
        this.insertSequenceSQL = ReadConfig.insertSequenceSQL;
        this.updateTag = app.isUpdateTag();
        this.testcase = testcase;
    }

    /**
     * parse XML blast results to INSERT SQL file
     * 
     * @param oneInputTag
     *            multiple SQL or not
     * @param currentDir
     *            on which directory to store this sql
     */
    public void parse2sql(boolean oneInputTag, String currentDir, int countnum) {
        // System.out.println(this.updateTag);
        System.setProperty("javax.xml.accessExternalDTD", "all");
        System.setProperty("http.agent", HTTP_AGENT_PROPERTY_VALUE); // http.agent
        // is
        // needed
        // to fetch
        // dtd from
        // some
        // servers
        // System.out.println("this.seqFileCount:" + this.seqFileCount);
        this.workspace = currentDir;
        this.seqFileCount = countnum;
        if (!oneInputTag) {
            // multiple input, multiple sql generated incrementally
            if (this.seqFileCount == -1) {
                parseblastresultsSmallMem();
            } else {
                // Usually use this, save memory and time
                HashMap<String, String> pdbHm = new HashMap<String, String>();
                for (int i = 0; i < this.seqFileCount; i++) {
                    log.info("[Parsing] Read blast results on " + i + "th xml file");
                    parseblastresultsSmallMem(i, pdbHm);
                }
            }
        } else {
            // test for small datasets: single input, single sql generated in
            // one time
            // TODO
            // Does not work for mutation list
            MutationAlignmentResult outresults = new MutationAlignmentResult();
            outresults.setAlignmentList(parseblastresultsSingle(currentDir));
            generateSQLstatementsSingle(outresults, currentDir);
        }
    }

    /**
     * Used for individual results to save space
     * 
     * @param oneInputTag
     * @param currentDir
     * @param countnum
     * @param i
     */
    public void parse2sqlPartition(boolean oneInputTag, String currentDir, int countnum, int i,
            HashMap<String, String> pdbHm) {
        // System.out.println(this.updateTag);
        System.setProperty("javax.xml.accessExternalDTD", "all");
        System.setProperty("http.agent", HTTP_AGENT_PROPERTY_VALUE); // http.agent
        // is
        // needed
        // to fetch
        // dtd from
        // some
        // servers
        // System.out.println("this.seqFileCount:" + this.seqFileCount);
        this.workspace = currentDir;
        this.seqFileCount = countnum;
        if (!oneInputTag) {
            // multiple input, multiple sql generated incrementally
            if (this.seqFileCount == -1) {
                parseblastresultsSmallMem();
            } else {
                parseblastresultsSmallMem(i, pdbHm);
            }
        } else {
            // test for small datasets: single input, single sql generated in
            // one time
            // TODO
            // Does not work for mutationlist
            MutationAlignmentResult outresults = new MutationAlignmentResult();
            outresults.setAlignmentList(parseblastresultsSingle(currentDir));
            generateSQLstatementsSingle(outresults, currentDir);
        }
    }

    /**
     * parse Single blast XML results, output to SQL file incrementally
     * 
     */
    public void parseblastresultsSmallMem() {
        try {
            log.info("[BLAST] Read blast results from xml file...");
            File blastresults = new File(this.workspace + this.db.resultfileName);
            File outputfile = new File(this.workspace + this.sqlInsertFile);
            HashMap<String, String> pdbHm = new HashMap<String, String>();
            int count = parsexml(blastresults, outputfile, pdbHm);
            this.matches = count;
            log.info("[BLAST] Insert Statements of the file is : " + this.matches);
        } catch (Exception ex) {
            log.error("[BLAST] Error Parsing BLAST Result");
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * parse multiple blast XML results, output to SQL file incrementally
     * 
     * @param filecount:
     *            id of the multiple files
     * @return
     */
    public void parseblastresultsSmallMem(int filecount, HashMap<String, String> pdbHm) {
        try {
            log.info("[BLAST] Read blast results from " + filecount + "th xml file...");
            File blastresults = new File(this.workspace + this.db.resultfileName + "." + filecount);
            File outputfile;
            // Check whether multiple files existed
            if (this.seqFileCount != -1) {
                outputfile = new File(this.workspace + this.sqlInsertFile + "." + filecount);
            } else {
                outputfile = new File(this.workspace + this.sqlInsertFile);
            }
            int count = parsexml(blastresults, outputfile, pdbHm);
            this.matches = this.matches + count;
            log.info("[BLAST] Insert statements after parsing " + filecount + "th xml : " + this.matches);
        } catch (Exception ex) {
            log.error("[BLAST] Error Parsing BLAST Result");
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * main body of parsing xml
     * 
     * @param blastresults
     * @param outputfile
     * @param pdbHm
     * @return count
     */
    public int parsexml(File blastresults, File outputfile, HashMap<String, String> pdbHm) {
        int count = 1;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.cbioportal.G2Smutation.util.blast");
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(null);
            BlastOutput blast = (BlastOutput) u.unmarshal(blastresults);
            List<BlastResult> alignmentList = new ArrayList<BlastResult>();
            List<MutationRecord> mutationList = new ArrayList<MutationRecord>();
            MutationAlignmentResult maresult = new MutationAlignmentResult();
            BlastOutputIterations iterations = blast.getBlastOutputIterations();
            log.info("[BLAST] Start parsing results...");
            int sql_insert_output_interval = Integer.parseInt(this.sqlInsertOutputInterval);
            int sequence_count = 0;
            for (Iteration iteration : iterations.getIteration()) {
                String querytext = iteration.getIterationQueryDef();
                // log.info(querytext);
                // querytext example: 8;C1F9K2_1 OBG_ACIC5
                IterationHits hits = iteration.getIterationHits();
                if (!hits.getHit().isEmpty()) {
                    sequence_count++;
                }
                for (Hit hit : hits.getHit()) {
                    MutationAlignmentResult mar = parseSingleAlignment(querytext, hit, count);
                    alignmentList.addAll(mar.getAlignmentList());
                    mutationList.addAll(mar.getMutationList());
                    count = alignmentList.size() + 1;
                }
            }
            maresult.setAlignmentList(alignmentList);
            maresult.setMutationList(mutationList);
            log.info("[BLAST] " + sequence_count + " sequences have blast results");
            // output remaining contents to the SQL file
            // Not output now
            genereateSQLstatementsSmallMem(maresult, pdbHm, count, outputfile);

        } catch (Exception ex) {
            log.error("[BLAST] Error Parsing BLAST Result");
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return count;
    }

    /**
     * generate SQL insert text to Table pdb_entry
     * 
     * @param br
     * @return
     */
    public String makeTable_pdb_entry_insert(BlastResult br) {
        String pdbNo = br.getSseqid().split("\\s+")[0];
        String[] strarrayS = pdbNo.split("_");
        String segStart = br.getSseqid().split("\\s+")[3];

        String str = "INSERT IGNORE INTO `pdb_entry` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`) VALUES ('"
                + pdbNo + "', '" + strarrayS[0] + "', '" + strarrayS[1] + "', '" + strarrayS[2] + "', '" + segStart
                + "');\n";
        return str;
    }

    /**
     * generate SQL insert text to Table pdb_seq_alignment
     * 
     * @param br
     * @return generated SQL statements
     */
    public String makeTable_pdb_seq_insert(BlastResult br) {
        String[] strarrayQ = br.getQseqid().split(";");
        String pdbNo = br.getSseqid().split("\\s+")[0];
        String[] strarrayS = pdbNo.split("_");
        String segStart = br.getSseqid().split("\\s+")[3];
        String str = "INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`)VALUES ('"
                + pdbNo + "','" + strarrayS[0] + "','" + strarrayS[1] + "','" + strarrayS[2] + "','" + segStart + "','"
                + strarrayQ[0] + "'," + br.getsStart() + "," + br.getsEnd() + "," + br.getqStart() + "," + br.getqEnd()
                + ",'" + br.getEvalue() + "'," + br.getBitscore() + "," + br.getIdent() + "," + br.getIdentp() + ",'"
                + br.getSeq_align() + "','" + br.getPdb_align() + "','" + br.getMidline_align() + "',CURDATE());\n";
        return str;
    }

    /**
     * generate SQL insert text to Table mutation_entry
     * 
     * @param br
     * @return generated SQL statements
     */
    public String makeTable_mutation_insert(MutationRecord mr) {
       String str = "INSERT INTO `mutation_entry` (`MUTATION_NO`,`SEQ_ID`,`SEQ_NAME`,`SEQ_INDEX`,`SEQ_RESIDUE`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,`ALIGNMENT_ID`)VALUES ('"
                + mr.getSeqId() + "_" + mr.getSeqResidueIndex() + "','" + mr.getSeqId() + "','" + mr.getSeqName() + "',"
                + mr.getSeqResidueIndex() + ",'" + mr.getSeqResidueName() + "','" + mr.getPdbNo() + "',"
                + mr.getPdbResidueIndex() + ",'" + mr.getPdbResidueName() + "'," + mr.getAlignmentId() + ");\n";
        return str;
    }

    /**
     * Used for Update: generate SQL insert text to Table pdb_ensembl_alignment
     * 
     * The only variate in the procedure is alignment limit, which now is set as
     * 50
     * 
     * Call Procedure InsertUpdate ()
     * 
     * The Procedure is integrated with pdb.sql:
     * 
     * DROP PROCEDURE IF EXISTS `InsertUpdate`; DELIMITER // CREATE PROCEDURE
     * InsertUpdate(IN inPDB_NO VARCHAR(12), IN inPDB_ID VARCHAR(4), IN inCHAIN
     * VARCHAR(4), IN inPDB_SEG VARCHAR(2), IN inSEG_START VARCHAR(4), IN
     * inSEQ_ID int, IN inPDB_FROM int, IN inPDB_TO int, IN inSEQ_FROM int, IN
     * inSEQ_TO int, IN inEVALUE double, IN inBITSCORE float, IN inIDENTITY
     * float, IN inIDENTP float, IN inSEQ_ALIGN text, IN inPDB_ALIGN text, IN
     * inMIDLINE_ALIGN text, IN inUPDATE_DATE DATE ) BEGIN DECLARE maxEvalue
     * double; DECLARE countEvalue double; SELECT COUNT(*) INTO countEvalue FROM
     * pdb_seq_alignment where PDB_NO=inPDB_NO; SELECT MAX(D) INTO countEvalue
     * FROM pdb_seq_alignment where PDB_NO=inPDB_NO; IF(inEVALUE<maxEvalue) THEN
     * IF(countEvalue<50) THEN INSERT INTO `pdb_seq_alignment`
     * (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`
     * PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`
     * SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES
     * (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,
     * inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,
     * inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE); ELSE DELETE FROM
     * `pdb_seq_alignment` WHERE (PDB_NO=inPDB_NO and EVALUE=inEVALUE); INSERT
     * INTO `pdb_seq_alignment`
     * (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`
     * PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`
     * SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES
     * (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,
     * inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,
     * inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE); END IF; ELSE
     * IF(countEvalue<50) THEN INSERT INTO `pdb_seq_alignment`
     * (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`
     * PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`
     * SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES
     * (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,
     * inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,
     * inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE); END IF; END IF;
     * END // DELIMITER ;
     * 
     * 
     * 
     * 
     * @param br
     * @return generated SQL statements
     */
    public String makeTable_pdb_seq_insert_Update(BlastResult br) {
        String[] strarrayQ = br.getQseqid().split(";");
        String pdbNo = br.getSseqid().split("\\s+")[0];
        String[] strarrayS = pdbNo.split("_");
        String segStart = br.getSseqid().split("\\s+")[3];

        String str = "call InsertUpdate('" + pdbNo + "','" + strarrayS[0] + "','" + strarrayS[1] + "','" + strarrayS[2]
                + "','" + segStart + "','" + strarrayQ[0] + "'," + br.getsStart() + "," + br.getsEnd() + ","
                + br.getqStart() + "," + br.getqEnd() + ",'" + br.getEvalue() + "'," + br.getBitscore() + ","
                + br.getIdent() + "," + br.getIdentp() + ",'" + br.getSeq_align() + "','" + br.getPdb_align() + "','"
                + br.getMidline_align() + "',CURDATE());\n";

        /*
         * String str =
         * "INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`)VALUES ('"
         * + pdbNo + "','" + strarrayS[0] + "','" + strarrayS[1] + "','" +
         * strarrayS[2] + "','" + segStart + "','" + strarrayQ[0] + "'," +
         * br.getsStart() + "," + br.getsEnd() + "," + br.getqStart() + "," +
         * br.getqEnd() + ",'" + br.getEvalue() + "'," + br.getBitscore() + ","
         * + br.getIdent() + "," + br.getIdentp() + ",'" + br.getSeq_align() +
         * "','" + br.getPdb_align() + "','" + br.getMidline_align() +
         * "',CURDATE());\n";
         */
        return str;
    }

    /**
     * Parse list of String blast results to input SQL statements, time and
     * memory consuming for huge files Use
     * 
     * @param outresults
     *            List<BlastResult>
     */
    public void generateSQLstatementsSingle(MutationAlignmentResult results, String currentDir) {
        try {
            log.info("[SHELL] Start Write insert.sql File...");
            File file = new File(currentDir + this.sqlInsertFile);
            // HashMap pdbHm is designed to avoid duplication of
            // primary keys in SQL
            // if we already have the entry, do nothing; otherwise generate the
            // SQL and add the keys into the HashMap
            HashMap<String, String> pdbHm = new HashMap<String, String>();
            List<String> outputlist = makeSQLText(results, pdbHm);
            FileUtils.writeLines(file, outputlist, "");
            log.info("[SHELL] Write insert.sql Done");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Parse multiple list of String blast results to multiple input SQL
     * statements
     * 
     * @param MutationAlignmentResult
     *            results List<MutationRecord> List<BlastResult>
     * 
     * @param pdbHm
     * @param count
     * @param outputfile
     */
    public void genereateSQLstatementsSmallMem(MutationAlignmentResult results, HashMap<String, String> pdbHm,
            long count, File outputfile) {
        try {
            log.info("[SHELL] Start Write insert.sql File from Alignment " + count + "...");
            if (count == 1) {
                // check, if starts, make sure it is empty
                if (outputfile.exists()) {
                    outputfile.delete();
                }
            }
            List<String> outputlist = makeSQLText(results, pdbHm);
            FileUtils.writeLines(outputfile, outputlist, "", true);
            outputlist = null;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * main body of generate SQL Insert text
     * 
     * @param results
     * @param pdbHm
     * @return
     */
    public List<String> makeSQLText(MutationAlignmentResult results, HashMap<String, String> pdbHm) {
        List<String> outputlist = new ArrayList<String>();
        // Add transaction
        outputlist.add("SET autocommit = 0;");
        outputlist.add("start transaction;");
        // First generate alignments SQL files
        List<BlastResult> al = results.getAlignmentList();
        for (BlastResult br : al) {
            if (pdbHm.containsKey(br.getSseqid())) {
                // do nothing
            } else {
                outputlist.add(makeTable_pdb_entry_insert(br));
                pdbHm.put(br.getSseqid(), "");
            }
            // If it is update, then call function
            if (this.updateTag) {
                outputlist.add(makeTable_pdb_seq_insert_Update(br));
                // If it is init, generate INSERT statements
            } else {
                outputlist.add(makeTable_pdb_seq_insert(br));
            }
        }
        // Then generate mutation SQL files
        List<MutationRecord> ml = results.getMutationList();
        for (MutationRecord mr : ml) {
            // If it is update, then call function
            if (this.updateTag) {
                // TODO
                // outputlist.add(makeTable_mutation_insert_Update(mr));
                // If it is init, generate INSERT statements
            } else {
                outputlist.add(makeTable_mutation_insert(mr));
            }
        }
        outputlist.add("commit;");
        return outputlist;
    }

    /**
     * Parse one single file of blast results to list of String, time and memory
     * consuming for huge files
     * 
     * @return List<BlastResult>
     */
    public List<BlastResult> parseblastresultsSingle(String currentDir) {
        List<BlastResult> results = new ArrayList<BlastResult>();
        try {
            log.info("[BLAST] Read blast results from xml file...");
            File blastresults = new File(currentDir + this.db.resultfileName);
            JAXBContext jc = JAXBContext.newInstance("org.cbioportal.G2Smutation.util.blast");
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(null);
            BlastOutput blast = (BlastOutput) u.unmarshal(blastresults);
            BlastOutputIterations iterations = blast.getBlastOutputIterations();
            int count = 1;
            for (Iteration iteration : iterations.getIteration()) {
                String querytext = iteration.getIterationQueryDef();
                IterationHits hits = iteration.getIterationHits();
                for (Hit hit : hits.getHit()) {
                    MutationAlignmentResult mar = parseSingleAlignment(querytext, hit, count);
                    results.addAll(mar.getAlignmentList());
                    count = results.size() + 1;
                }
            }
            this.matches = count - 1;
            log.info("[BLAST] Total Insert " + this.matches + " alignments");
        } catch (Exception ex) {
            log.error("[BLAST] Error Parsing BLAST Result");
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return results;
    }

    /**
     * Parse XML structure into Object BlastResult Core function
     *
     * 
     * @param querytext
     *            e.g. 13;P11532_7 DMD_HUMAN;ENSP00000367974.4
     *            ENSG00000198947.15 ENST00000378702.8
     * @param hit
     * @param count
     *            // number of alignment, it is the alignmentId
     * @return
     */
    public MutationAlignmentResult parseSingleAlignment(String querytext, Hit hit, int count) {

        // TODO Different criteria
        List<BlastResult> alignmentList = new ArrayList<BlastResult>();
        List<MutationRecord> mutationList = new ArrayList<MutationRecord>();

        /*
         * //HashMap for mutation //Key: IndexofInputProtein //Value: tmpHashMap
         * //tmpHashMap //Key: ResidueName //Value: PDBname_Chain1 index1
         * alignmentId1;PDBname_Chain2 index2 alignmentId2;...
         * HashMap<Integer,HashMap<String,String>> mutationHm = new
         * HashMap<Integer,HashMap<String,String>>();
         * 
         * //HashMap for residue Name, accompany with mutationHm
         * HashMap<Integer,String> inputResidueNameHm = new
         * HashMap<Integer,String>();
         */

        List<Hsp> tmplist = hit.getHitHsps().getHsp();
        for (Hsp tmp : tmplist) {
            BlastResult br = new BlastResult(count);
            br.qseqid = querytext;
            br.sseqid = hit.getHitDef();
            br.ident = Double.parseDouble(tmp.getHspIdentity());
            br.identp = Double.parseDouble(tmp.getHspPositive());
            br.evalue = Double.parseDouble(tmp.getHspEvalue());
            br.bitscore = Double.parseDouble(tmp.getHspBitScore());
            br.qStart = Integer.parseInt(tmp.getHspQueryFrom());
            br.qEnd = Integer.parseInt(tmp.getHspQueryTo());
            br.sStart = Integer.parseInt(tmp.getHspHitFrom());
            br.sEnd = Integer.parseInt(tmp.getHspHitTo());
            br.seq_align = tmp.getHspQseq();
            br.pdb_align = tmp.getHspHseq();
            br.midline_align = tmp.getHspMidline();

            /*
             * alignment.setSeqId(querytext.split("\\s+")[0]);
             * alignment.setPdbNo(hit.getHitDef().split("\\s+")[0]);
             * alignment.setPdbId(hit.getHitDef().split("\\s+")[0].split("_")[0]
             * );
             * alignment.setChain(hit.getHitDef().split("\\s+")[0].split("_")[1]
             * );
             * alignment.setPdbSeg(hit.getHitDef().split("\\s+")[0].split("_")[2
             * ]); alignment.setSegStart(hit.getHitDef().split("\\s+")[3]);
             */

            /*
             * //Original solution: Include all the blast results
             * resultList.add(br); count++;
             */
            // Add filter: Only choose best alignment
            Double alignlen = Double.parseDouble(tmp.getHspAlignLen());
            // count "-" in genSequence
            int seqGapCount = 0;
            // count "-" in pdbSequence
            int pdbGapCount = 0;
            // if(isFilterAlignHighQuality(br,alignlen)){
            if (isFilterAlignHighQuality(br, alignlen, testcase)) {
                for (int i = 0; i < br.midline_align.length(); i++) {
                    String residueAlign = br.midline_align.substring(i, i + 1);
                    String residue = br.pdb_align.substring(i, i + 1);
                    String residueQuery = br.seq_align.substring(i, i + 1);
                    // if there is a "-" in genSequence, seqGapCount +1
                    if (residueQuery.equals("-")) {
                        seqGapCount++;
                        continue;
                    }
                    // if there is a "-" in pdbSequence, pdbGapCount+1
                    if (residue.equals("-")) {
                        pdbGapCount++;
                        continue;
                    }
                    // if we have point mutation here:
                    // Criteria: either space and + are mismatch, and no X as
                    // the linker
                    if ((residueAlign.equals(" ") || residueAlign.equals("+")) && !residue.equals("X")) {
                        // log.info("*"+residueAlign+"&"+residue+"@");

                        int correctProteinIndex = br.qStart + i - seqGapCount;
                        /*
                         * Example here: Seq ID: 13, hitPDB: 1eg14_A_1; q:
                         * 7-239. s:29-260, PDB: 47-306
                         */
                        int correctPDBIndex = Integer.parseInt(br.sseqid.split("\\s+")[3]) + br.sStart - 1 + i
                                - pdbGapCount;
                        String pdbNO = br.sseqid.split("\\s+")[0];
                        /*
                         * if(mutationHm.containsKey(correctProteinIndex)){
                         * HashMap<String,String> tmpHm =
                         * (HashMap<String,String>)mutationHm.get(
                         * correctProteinIndex); if(tmpHm.containsKey(residue)){
                         * String tmpstr = tmpHm.get(residue);
                         * tmpHm.put(residue, tmpstr + pdbNO + " " +
                         * correctPDBIndex + " " + count + ";"); }else{
                         * tmpHm.put(residue, pdbNO + " " + correctPDBIndex +
                         * " " + count + ";"); }
                         * mutationHm.put(correctProteinIndex, tmpHm); }else{
                         * HashMap<String,String> tmpHm = new
                         * HashMap<String,String>(); tmpHm.put(residue, pdbNO +
                         * " " + correctPDBIndex + " " + count + ";");
                         * mutationHm.put(correctProteinIndex, tmpHm); }
                         * inputResidueNameHm.put(correctProteinIndex,
                         * br.seq_align.substring(i,i+1));
                         */
                        MutationRecord mr = new MutationRecord();

                        mr.setSeqId(Integer.parseInt(querytext.split(";")[0]));
                        mr.setSeqName(querytext.substring(querytext.indexOf(";") + 1));
                        mr.setSeqResidueIndex(correctProteinIndex);
                        mr.setSeqResidueName(br.seq_align.substring(i, i + 1));
                        mr.setPdbNo(pdbNO);
                        mr.setPdbResidueIndex(correctPDBIndex);
                        mr.setPdbResidueName(br.pdb_align.substring(i, i + 1));
                        mr.setAlignmentId(count);

                        mutationList.add(mr);
                    }
                }
                alignmentList.add(br);
                count++;
            }
        }

        /*
         * //enumerate the hashmap for (Map.Entry<Integer,
         * HashMap<String,String>> entry: mutationHm.entrySet()){ int
         * proteinResidueIndex = entry.getKey(); HashMap<String, String> tmpHm =
         * (HashMap<String, String>)entry.getValue(); for (Map.Entry<String,
         * String> tentry : tmpHm.entrySet()) { String pdbResidueName =
         * tentry.getKey(); String tmpstr = tentry.getValue(); String[]
         * tmpstrArray = tmpstr.split(";"); for (int i = 0; i <
         * tmpstrArray.length; i++) { MutationRecord mr = new MutationRecord();
         * String[] ttstrArray = tmpstrArray[i].split("\\s+");
         * mr.setSeqId(Integer.parseInt(querytext.split(";")[0]));
         * mr.setSeqName(querytext.substring(querytext.indexOf(";") + 1));
         * mr.setSeqResidueIndex(proteinResidueIndex);
         * mr.setSeqResidueName(inputResidueNameHm.get(proteinResidueIndex));
         * mr.setPdbNo(ttstrArray[0]);
         * mr.setPdbResidueIndex(Integer.parseInt(ttstrArray[1]));
         * mr.setAlignmentId(Integer.parseInt(ttstrArray[2]));
         * mr.setPdbResidueName(pdbResidueName); mutationList.add(mr); } }
         * 
         * }
         */

        MutationAlignmentResult result = new MutationAlignmentResult();
        result.setAlignmentList(alignmentList);
        result.setMutationList(mutationList);

        return result;
    }

    /*
     * private int checkGapinBlastResults(int i, BlastResult br) { int j = i;
     * if(br.seq_align.substring(j-1, j) != "-") {
     * while(br.seq_align.substring(j, j+1) == "-") { j++; } } return j-i; }
     */
    /**
     * Check whether the alignment itself has high quality, define the condition
     * here
     * 
     * @param br
     * @param alignlen
     * @return
     */
    boolean isFilterAlignHighQuality(BlastResult br, Double alignlen) {
        // Hsp_positive-Hsp_identity<=10 && Hsp_positive/Hsp_align-len>=0.95
        // //old
        // if(br.identy-br.identp<=Integer.parseInt(ReadConfig.alignFilterDiff)
        // &&
        // br.identp/alignlen>=Double.parseDouble(ReadConfig.alignFilterRatio))
        // Hsp_positive-Hsp_identity<=10 && Hsp_positive/Hsp_align-len>=0.95
        if (alignlen - br.identp <= Integer.parseInt(ReadConfig.alignFilterDiffT)
                && br.identp / alignlen >= Double.parseDouble(ReadConfig.alignFilterRatio))
            return true;
        else
            return false;
    }

    /**
     * Qualitytest 2: ratio of identp/len >= 0.90/0.95/1.00
     * 
     * @param br
     * @param alignlen
     * @param cases
     * @return
     */
    boolean isQuality2(BlastResult br, Double alignlen, int cases) {
        boolean testcase = false;
        switch (cases) {
        case 1:
            testcase = br.identp / alignlen >= 0.90;
            break;
        case 2:
            testcase = br.identp / alignlen >= 0.95;
            break;
        case 3:
            testcase = br.identp / alignlen >= 1.00;
            break;
        default:
            break;
        }
        return testcase;
    }

    /**
     * Qualitytest 3: len-identp<=10/5
     * 
     * @param br
     * @param alignlen
     * @param cases
     * @return
     */
    boolean isQuality3(BlastResult br, Double alignlen, int cases) {
        boolean testcase = false;
        switch (cases) {
        case 1:
            testcase = alignlen - br.identp <= 10;
            break;
        case 2:
            testcase = alignlen - br.identp <= 5;
            break;
        default:
            break;
        }
        return testcase;
    }

    /**
     * Qualitytest 4: len-identy<=10/5
     * 
     * @param br
     * @param alignlen
     * @param cases
     * @return
     */
    boolean isQuality4(BlastResult br, Double alignlen, int cases) {
        boolean testcase = false;
        switch (cases) {
        case 1:
            testcase = alignlen - br.ident <= 10;
            break;
        case 2:
            testcase = alignlen - br.ident <= 5;
            break;
        default:
            break;
        }
        return testcase;
    }

    /**
     * Check whether the alignment itself has high quality, define the condition
     * here, this function is for test
     * 
     * - 2 3 (1-3) 
     * - 3 2 (4-5) 
     * - 4 2 (6-7) 
     * - 2&&(3&&4) 3*2*2 (8-19) 
     * - 2&&(3||4) 3*2*2 (20-31) 
     * - 2&&3 3*2 (32-37) 
     * - 2&&4 3*2 (38-43) 
     * - 2||(3&&4) 3*2*2 (44-55) 
     * - 2||3||4 3*2*2 (56-67) 
     * - 2||3 3*2 (68-73) 
     * - 2||4 3*2 (74-79)
     * 
     * @param br
     * @param alignlen
     * @return
     */
    boolean isFilterAlignHighQuality(BlastResult br, Double alignlen, int cases) {
        boolean testcase = false;
        switch (cases) {
        case 1:
            testcase = isQuality2(br, alignlen, 1);
            break;
        case 2:
            testcase = isQuality2(br, alignlen, 2);
            break;
        case 3:
            testcase = isQuality2(br, alignlen, 3);
            break;
        case 4:
            testcase = isQuality3(br, alignlen, 1);
            break;
        case 5:
            testcase = isQuality3(br, alignlen, 2);
            break;
        case 6:
            testcase = isQuality4(br, alignlen, 1);
            break;
        case 7:
            testcase = isQuality4(br, alignlen, 2);
            break;

        case 8:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1);
            break;
        case 9:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2);
            break;
        case 10:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1);
            break;
        case 11:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2);
            break;
        case 12:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1);
            break;
        case 13:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2);
            break;
        case 14:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1);
            break;
        case 15:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2);
            break;
        case 16:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1);
            break;
        case 17:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2);
            break;
        case 18:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1);
            break;
        case 19:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2);
            break;

        case 20:
            testcase = isQuality2(br, alignlen, 1) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1));
            break;
        case 21:
            testcase = isQuality2(br, alignlen, 1) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2));
            break;
        case 22:
            testcase = isQuality2(br, alignlen, 1) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1));
            break;
        case 23:
            testcase = isQuality2(br, alignlen, 1) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2));
            break;
        case 24:
            testcase = isQuality2(br, alignlen, 2) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1));
            break;
        case 25:
            testcase = isQuality2(br, alignlen, 2) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2));
            break;
        case 26:
            testcase = isQuality2(br, alignlen, 2) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1));
            break;
        case 27:
            testcase = isQuality2(br, alignlen, 2) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2));
            break;
        case 28:
            testcase = isQuality2(br, alignlen, 3) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1));
            break;
        case 29:
            testcase = isQuality2(br, alignlen, 3) && (isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2));
            break;
        case 30:
            testcase = isQuality2(br, alignlen, 3) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1));
            break;
        case 31:
            testcase = isQuality2(br, alignlen, 3) && (isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2));
            break;

        case 32:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 1);
            break;
        case 33:
            testcase = isQuality2(br, alignlen, 1) && isQuality3(br, alignlen, 2);
            break;
        case 34:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 1);
            break;
        case 35:
            testcase = isQuality2(br, alignlen, 2) && isQuality3(br, alignlen, 2);
            break;
        case 36:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 1);
            break;
        case 37:
            testcase = isQuality2(br, alignlen, 3) && isQuality3(br, alignlen, 2);
            break;

        case 38:
            testcase = isQuality2(br, alignlen, 1) && isQuality4(br, alignlen, 1);
            break;
        case 39:
            testcase = isQuality2(br, alignlen, 1) && isQuality4(br, alignlen, 2);
            break;
        case 40:
            testcase = isQuality2(br, alignlen, 2) && isQuality4(br, alignlen, 1);
            break;
        case 41:
            testcase = isQuality2(br, alignlen, 2) && isQuality4(br, alignlen, 2);
            break;
        case 42:
            testcase = isQuality2(br, alignlen, 3) && isQuality4(br, alignlen, 1);
            break;
        case 43:
            testcase = isQuality2(br, alignlen, 3) && isQuality4(br, alignlen, 2);
            break;

        case 44:
            testcase = isQuality2(br, alignlen, 1) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1));
            break;
        case 45:
            testcase = isQuality2(br, alignlen, 1) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2));
            break;
        case 46:
            testcase = isQuality2(br, alignlen, 1) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1));
            break;
        case 47:
            testcase = isQuality2(br, alignlen, 1) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2));
            break;
        case 48:
            testcase = isQuality2(br, alignlen, 2) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1));
            break;
        case 49:
            testcase = isQuality2(br, alignlen, 2) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2));
            break;
        case 50:
            testcase = isQuality2(br, alignlen, 2) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1));
            break;
        case 51:
            testcase = isQuality2(br, alignlen, 2) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2));
            break;
        case 52:
            testcase = isQuality2(br, alignlen, 3) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 1));
            break;
        case 53:
            testcase = isQuality2(br, alignlen, 3) || (isQuality3(br, alignlen, 1) && isQuality4(br, alignlen, 2));
            break;
        case 54:
            testcase = isQuality2(br, alignlen, 3) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 1));
            break;
        case 55:
            testcase = isQuality2(br, alignlen, 3) || (isQuality3(br, alignlen, 2) && isQuality4(br, alignlen, 2));
            break;

        case 56:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1);
            break;
        case 57:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2);
            break;
        case 58:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1);
            break;
        case 59:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2);
            break;
        case 60:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1);
            break;
        case 61:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2);
            break;
        case 62:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1);
            break;
        case 63:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2);
            break;
        case 64:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 1);
            break;
        case 65:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 1) || isQuality4(br, alignlen, 2);
            break;
        case 66:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 1);
            break;
        case 67:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 2) || isQuality4(br, alignlen, 2);
            break;

        case 68:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 1);
            break;
        case 69:
            testcase = isQuality2(br, alignlen, 1) || isQuality3(br, alignlen, 2);
            break;
        case 70:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 1);
            break;
        case 71:
            testcase = isQuality2(br, alignlen, 2) || isQuality3(br, alignlen, 2);
            break;
        case 72:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 1);
            break;
        case 73:
            testcase = isQuality2(br, alignlen, 3) || isQuality3(br, alignlen, 2);
            break;

        case 74:
            testcase = isQuality2(br, alignlen, 1) || isQuality4(br, alignlen, 1);
            break;
        case 75:
            testcase = isQuality2(br, alignlen, 1) || isQuality4(br, alignlen, 2);
            break;
        case 76:
            testcase = isQuality2(br, alignlen, 2) || isQuality4(br, alignlen, 1);
            break;
        case 77:
            testcase = isQuality2(br, alignlen, 2) || isQuality4(br, alignlen, 2);
            break;
        case 78:
            testcase = isQuality2(br, alignlen, 3) || isQuality4(br, alignlen, 1);
            break;
        case 79:
            testcase = isQuality2(br, alignlen, 3) || isQuality4(br, alignlen, 2);
            break;

        default:
            break;
        }

        // Hsp_positive-Hsp_identity<=10 && Hsp_positive/Hsp_align-len>=0.95
        // //old
        // if(br.identy-br.identp<=Integer.parseInt(ReadConfig.alignFilterDiff)
        // &&
        // br.identp/alignlen>=Double.parseDouble(ReadConfig.alignFilterRatio))

        // output result true or false
        if (testcase)
            return true;
        else
            return false;
    }

    /**
     * generate sql in delete
     * 
     * @param currentDir
     * @param list
     */
    public void generateDeleteSql(String currentDir, List<String> list) {
        try {
            log.info("[PIPELINE] Generating delete SQL statements");
            File outfile = new File(currentDir + this.sqlDeleteFile);
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            for (String pdbName : list) {
                String str = "DELETE pdb_seq_alignment FROM pdb_seq_alignment inner join pdb_entry on pdb_entry.pdb_no=pdb_seq_alignment.pdb_no WHERE pdb_seq_alignment.pdb_id='"
                        + pdbName + "';\n";
                outputlist.add(str);
                String str1 = "DELETE FROM pdb_entry WHERE PDB_ID='" + pdbName + "';\n";
                outputlist.add(str1);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(outfile, outputlist, "");
            log.info("[SHELL] Totally delete " + list.size() + " obsolete and modified alignments");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public HashMap<String, String> BuildSNPHM(HashMap<String, String> SNPHM) {
    	try {
    		// open "SNP3D_PDB_GRCH37" file
    		String SNPfilepwd = new String(ReadConfig.workspace + "SNP3D_PDB_GRCH37");
    		File SNPfile = new File(SNPfilepwd);
    		List<String> SNPfilelines = FileUtils.readLines(SNPfile, StandardCharsets.UTF_8.name());
    		int SNPNum = SNPfilelines.size()-1;
    		List<String> SNPMutationPos = new ArrayList<String>();
    		List<String> SNPid = new ArrayList<String>();
    		// Build SNP HashMap
    		for (int i = 1; i <= SNPNum; i++) {
    			SNPid.add(SNPfilelines.get(i).split("\\s+")[2]);
    			SNPMutationPos.add(SNPfilelines.get(i).split("\\s+")[9] + "_" + SNPfilelines.get(i).split("\\s+")[10] + "_" + SNPfilelines.get(i).split("\\s+")[12]);
    			if (SNPHM.containsKey(SNPMutationPos.get(i-1))) {
    				String SNPTempValue = SNPHM.get(SNPMutationPos.get(i-1)) + ";" + SNPid.get(i-1);
    				SNPHM.put(SNPMutationPos.get(i-1), SNPTempValue);
    			}
    			else {
    				SNPHM.put(SNPMutationPos.get(i-1), SNPid.get(i-1));
    			}
    		}  		
    	} catch (Exception ex) {
    		log.error(ex.getMessage());
            ex.printStackTrace();
    	}
    	return SNPHM;
    }
    
    public HashMap<String, String> BuildOutputHM(HashMap<String, String> OutputSNPHM) {
    	try {
    		// open "SNP3D_PDB_GRCH37" file
    		String SNPfilepwd = new String(ReadConfig.workspace + "SNP3D_PDB_GRCH37");
    		File SNPfile = new File(SNPfilepwd);
    		List<String> SNPfilelines = FileUtils.readLines(SNPfile, StandardCharsets.UTF_8.name());
    		int SNPNum = SNPfilelines.size()-1;
    		List<String> SNPid = new ArrayList<String>();
    		// Build Output HashMap
    		for (int i = 1; i <= SNPNum; i++) {
    			SNPid.add(SNPfilelines.get(i).split("\\s+")[2]);
    			// log.info(SNPid.get(i-1));
    			OutputSNPHM.put(SNPid.get(i-1), SNPfilelines.get(i));
    		}  		
    	} catch (Exception ex) {
    		log.error(ex.getMessage());
            ex.printStackTrace();
    	}
    	return OutputSNPHM;
    }
    
    public void compareMutation(int testcount) {
    	try {
    		log.info("********************The statistics result of the "+ testcount +"th case********************");
    		
    		// Output the mutation number of each mutationresult file
    		String pdbfilepwd = new String(ReadConfig.workspace + ReadConfig.alignFilterStatsResult + "." + testcount);
    		File pdbfile = new File(pdbfilepwd);
    		List<String> pdbfilelines = FileUtils.readLines(pdbfile, StandardCharsets.UTF_8.name());
    		int mutationline = pdbfilelines.size() - 1;
    		
    		// Compare each mutation to the SNP_PDB file 		
    		List<String> pdbMutationNo = new ArrayList<String>();
    		List<String> pdbMutationPos = new ArrayList<String>();
    		HashMap<String, String> pdbMutationHM = new HashMap<String, String>();
    		HashMap<String, String> SNPHM = new HashMap<String, String>();
    		HashMap<String, String> OutputSNPHM = new HashMap<String, String>();
    		List<String> outputSNPSeq = new ArrayList<String>();
    		
    		SNPHM = BuildSNPHM(SNPHM);
    		OutputSNPHM = BuildOutputHM(OutputSNPHM);
    		
    		for (int i = 1; i <= mutationline; i++) {
    			pdbMutationNo.add(pdbfilelines.get(i).split("\\s+")[0]);
    			pdbMutationPos.add(transferPdblines(pdbfilelines.get(i)));
    			if(SNPHM.containsKey(pdbMutationPos.get(i-1))) {
    				pdbMutationHM.put(pdbMutationNo.get(i-1), SNPHM.get(pdbMutationPos.get(i-1)));
    				for(int j = 0; j < pdbMutationHM.get(pdbMutationNo.get(i-1)).split(";").length; j++) {
    					outputSNPSeq.add(OutputSNPHM.get(pdbMutationHM.get(pdbMutationNo.get(i-1)).split(";")[j]));
    				}
    			}
    		}
    		pdbMutationNo = removeStringListDupli(pdbMutationNo);
    		double pdbMutationNum = pdbMutationNo.size();
    		double matchNum = pdbMutationHM.size();
    		log.info("[Statistic]There are totally " + pdbMutationNum + " mutation");
    		log.info("[Statistic]The matching num is " + matchNum);
    		double matchRate = matchNum / pdbMutationNum;
    		log.info("[Statistic]The matching rate is " + matchRate);
    		
    		// output result to "analyzefile.txt"
    		File analyzefile = new File(ReadConfig.workspace + "analyzefile.txt");
    		if (!analyzefile.exists()) {
    			FileUtils.touch(analyzefile);
    		}
    		List<String> analyzefilelines = new ArrayList<>();
    		analyzefilelines.add("mutationresult" + testcount +": ");
    		analyzefilelines.add("MutationNum under threshold: " + pdbMutationNum);
    		analyzefilelines.add("MutationNum Matched: " + matchNum);
    		analyzefilelines.add("MatchRate: " + matchRate);
    		analyzefilelines.add(" ");
    		FileUtils.writeLines(analyzefile, StandardCharsets.UTF_8.name(), analyzefilelines, true);
    		
    		// output SNP information to "SNPresult.txt.testcount"
    		File SNPResultFile = new File(ReadConfig.workspace + "snpresult.txt" + "." + testcount);
    		if (!SNPResultFile.exists()) {
    			FileUtils.touch(SNPResultFile);
    		}
    		List<String> outputTitle = new ArrayList<String>();
    		outputTitle.add("chr	pos	snp_id	master_acc	master_gi	master_pos	master_res	master_var	pdb_gi	pdb	pdb_chain	pdb_res	pdb_pos	blast_ident	clinsig");
    		FileUtils.writeLines(SNPResultFile, StandardCharsets.UTF_8.name(), outputTitle);
    		if (!outputSNPSeq.isEmpty()) {
    			outputSNPSeq = removeStringListDupli(outputSNPSeq);
    			FileUtils.writeLines(SNPResultFile, StandardCharsets.UTF_8.name(), outputSNPSeq, true);
    		}
	
    	} catch (Exception ex) {
    		log.error(ex.getMessage());
            ex.printStackTrace();
    	}
    }
    
    public static List<String> removeStringListDupli(List<String> stringList) {
        Set<String> set = new LinkedHashSet<>();
        set.addAll(stringList);

        stringList.clear();

        stringList.addAll(set);
        return stringList;
    }

    public String transferPdblines(String line) {
    	String transline = new String();
    	transline = line.split("\\s+")[1].substring(0, 7).toUpperCase() + line.split("\\s+")[2]; 	
		return transline;
    }
    
}

/**
 * Used for store mutation and alignment list for generating sql
 * 
 * @author wangjue
 *
 */
class MutationAlignmentResult {
    List<BlastResult> alignmentList;
    List<MutationRecord> mutationList;

    public List<BlastResult> getAlignmentList() {
        return alignmentList;
    }

    public void setAlignmentList(List<BlastResult> alignmentList) {
        this.alignmentList = alignmentList;
    }

    public List<MutationRecord> getMutationList() {
        return mutationList;
    }

    public void setMutationList(List<MutationRecord> mutationList) {
        this.mutationList = mutationList;
    }
}
