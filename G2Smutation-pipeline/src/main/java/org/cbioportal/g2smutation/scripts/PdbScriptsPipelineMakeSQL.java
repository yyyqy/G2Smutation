package org.cbioportal.g2smutation.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.cbioportal.g2smutation.util.FileOperatingUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.StringUtil;
import org.cbioportal.g2smutation.util.blast.BlastDataBase;
import org.cbioportal.g2smutation.util.blast.BlastOutput;
import org.cbioportal.g2smutation.util.blast.BlastOutputIterations;
import org.cbioportal.g2smutation.util.blast.BlastResult;
import org.cbioportal.g2smutation.util.blast.Hit;
import org.cbioportal.g2smutation.util.blast.Hsp;
import org.cbioportal.g2smutation.util.blast.Iteration;
import org.cbioportal.g2smutation.util.blast.IterationHits;
import org.cbioportal.g2smutation.util.models.MutationRecord;
import org.cbioportal.g2smutation.util.models.MutationUsageRecord;
import org.cbioportal.g2smutation.util.models.StructureAnnotationRecord;
import org.cbioportal.g2smutation.util.models.api.Mappings;
import org.cbioportal.g2smutation.util.models.api.QuoteCoor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

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
    private String sqlMutationInsertFile;
    private String sqlInsertOutputInterval;
    private String sqlDeleteFile;
    private String insertSequenceSQL;
    private boolean updateTag;// if update, then true;
    private int alignmentId=1; //alignmentId for mutation usage, keep track of alignmentId

    private int testcase=1;// use for test cases, default 1
    
    

    public BlastDataBase getDb() {
		return db;
	}

	public void setDb(BlastDataBase db) {
		this.db = db;
	}

	public int getAlignmentId() {
		return alignmentId;
	}

	public void setAlignmentId(int alignmentId) {
		this.alignmentId = alignmentId;
	}

	/**
     * 
     * Constructor
     * 
     * @param app
     */
    public PdbScriptsPipelineMakeSQL(PdbScriptsPipelineRunCommand app) {
        this.db = app.getDb();
        this.matches = app.getMatches();
        this.seqFileCount = app.getSeqFileCount();
        this.workspace = ReadConfig.workspace;
        this.sqlInsertFile = ReadConfig.sqlInsertFile;
        this.sqlMutationInsertFile = ReadConfig.sqlMutationInsertFile;
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
    public PdbScriptsPipelineMakeSQL(PdbScriptsPipelineRunCommand app, int testcase) {
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
     * Mutation parse XML blast results to INSERT SQL file
     * 
     * @param oneInputTag
     *            multiple SQL or not
     * @param currentDir
     *            on which directory to store this sql
     */
    public void parse2sqlMutation(boolean oneInputTag, String currentDir, int countnum) {
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
                //alignmentId for mutation:
                this.alignmentId =1;
                for (int i = 0; i < this.seqFileCount; i++) {
                    log.info("[Parsing] Read blast results on " + i + "th xml file");
                    parseblastresultsSmallMem(i, pdbHm, true);
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
     * 
     * parse XML blast results to INSERT SQL file
     * 
     * @param oneInputTag
     *            multiple SQL or not
     * @param currentDir
     *            on which directory to store this sql
     * @param mutationTag
     *            false for original G2S, true for mutation
     */
    public void parse2sql(boolean oneInputTag, String currentDir, int countnum, boolean mutationTag) {
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
                HashMap<String, String> pdbHm = new HashMap<String, String>();
                for (int i = 0; i < this.seqFileCount; i++) {
                    parseblastresultsSmallMem(i, pdbHm, mutationTag);
                }
            }
        } else {
            // test for small datasets: single input, single sql generated in
            // one time
            List<BlastResult> outresults = parseblastresultsSingle(currentDir);
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
     * @param pdbHm
     * @param mutationTag
     *            True for mutation, False for original G2S
     */
    public void parse2sqlPartition(boolean oneInputTag, String currentDir, int countnum, int i,
            HashMap<String, String> pdbHm, boolean mutationTag) {
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
                parseblastresultsSmallMem(i, pdbHm, mutationTag);
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
     * @param mutationFlag:
     *            false: original true: for mutation
     * @return
     */
    public void parseblastresultsSmallMem(int filecount, HashMap<String, String> pdbHm, boolean mutationFlag) {
        try {
            log.info("[BLAST] Read blast results from " + this.workspace + this.db.resultfileName + "." + filecount + " as the xml file...");
            File blastresults = new File(this.workspace + this.db.resultfileName + "." + filecount);
            File outputfile;
            // Check whether multiple files existed
            if (this.seqFileCount != -1) {
                if (mutationFlag) {
                    outputfile = new File(this.workspace + this.sqlMutationInsertFile + "." + filecount);
                } else {
                    outputfile = new File(this.workspace + this.sqlInsertFile + "." + filecount);
                }
            } else {
                if (mutationFlag) {
                    outputfile = new File(this.workspace + this.sqlMutationInsertFile);
                } else {
                    outputfile = new File(this.workspace + this.sqlInsertFile);
                }
            }
            int count = 0;
            if (mutationFlag) {
                count = parsexmlMutation(blastresults, outputfile, pdbHm);
            } else {
                count = parsexml(blastresults, outputfile, pdbHm);
            }
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
            JAXBContext jc = JAXBContext.newInstance("org.cbioportal.g2smutation.util.blast");
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(null);
            BlastOutput blast = (BlastOutput) u.unmarshal(blastresults);
            List<BlastResult> results = new ArrayList<BlastResult>();
            BlastOutputIterations iterations = blast.getBlastOutputIterations();
            log.info("[BLAST] Start parsing results...");
            int sql_insert_output_interval = Integer.parseInt(this.sqlInsertOutputInterval);
            for (Iteration iteration : iterations.getIteration()) {
                String querytext = iteration.getIterationQueryDef();
                IterationHits hits = iteration.getIterationHits();
                for (Hit hit : hits.getHit()) {
                    results.addAll(parseSingleAlignment(querytext, hit, count));
                    count = results.size() + 1;

                    // No need anymore
                    // TODO: need number
                    /*
                     * if (count % sql_insert_output_interval == 0) { // Once
                     * get the criteria, output contents to the SQL // file
                     * genereateSQLstatementsSmallMem(results, pdbHm, count,
                     * outputfile); count+=results.size(); results.clear(); }
                     */
                }
            }
            // output remaining contents to the SQL file
            genereateSQLstatementsSmallMem(results, pdbHm, count, outputfile);

        } catch (Exception ex) {
            log.error("[BLAST] Error Parsing BLAST Result");
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return count;
    }

    /**
     * main body of parsing xml
     * 
     * @param blastresults
     * @param outputfile
     * @param pdbHm
     * @return count
     */
    public int parsexmlMutation(File blastresults, File outputfile, HashMap<String, String> pdbHm) {
        int count = this.alignmentId;
        log.info("Work on "+count);
        try {
            JAXBContext jc = JAXBContext.newInstance("org.cbioportal.g2smutation.util.blast");
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
                /*
                 * Old, huge bug here
                for (Hit hit : hits.getHit()) {
                    MutationAlignmentResult mar = parseSingleAlignmentMutation(querytext, hit, count);
                    alignmentList.addAll(mar.getAlignmentList());
                    mutationList.addAll(mar.getMutationList()); 
                    count = mar.getAlignmentId();
                }
                */
                //Use indexSet to store index of mutation in the query protein
                //<index,HashSet<ResidueName>>
                HashMap <Integer,HashSet<String>> indexHm = new HashMap<>();
                for (Hit hit : hits.getHit()) {
                	indexHm = parseSingleAlignmentMutationFindMutatedIndex(querytext, hit, count, indexHm);
                }
                for (Hit hit : hits.getHit()) {
                    MutationAlignmentResult mar = parseSingleAlignmentMutation(querytext, hit, count, indexHm);
                    alignmentList.addAll(mar.getAlignmentList());
                    mutationList.addAll(mar.getMutationList()); 
                    count = mar.getAlignmentId();
                }
            }
            this.alignmentId = count;
            maresult.setAlignmentList(alignmentList);
            maresult.setMutationList(mutationList);
            log.info("[BLAST] " + sequence_count + " sequences have blast results in " + mutationList.size() + " mutations");
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
     * However, we don't update to 50 now, this is old implementation
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

		/*
		 * Original: make sure get up to 50 results, but we don't need that now, we want
		 * all 
		 * 
		 * String str = "call InsertUpdate('" + pdbNo + "','" + strarrayS[0] + "','"
		 * + strarrayS[1] + "','" + strarrayS[2] + "','" + segStart + "','" +
		 * strarrayQ[0] + "'," + br.getsStart() + "," + br.getsEnd() + "," +
		 * br.getqStart() + "," + br.getqEnd() + ",'" + br.getEvalue() + "'," +
		 * br.getBitscore() + "," + br.getIdent() + "," + br.getIdentp() + ",'" +
		 * br.getSeq_align() + "','" + br.getPdb_align() + "','" + br.getMidline_align()
		 * + "',CURDATE());\n";
		 */

		String str = "INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`)VALUES ('"
				+ pdbNo + "','" + strarrayS[0] + "','" + strarrayS[1] + "','" + strarrayS[2] + "','" + segStart + "','"
				+ strarrayQ[0] + "'," + br.getsStart() + "," + br.getsEnd() + "," + br.getqStart() + "," + br.getqEnd()
				+ ",'" + br.getEvalue() + "'," + br.getBitscore() + "," + br.getIdent() + "," + br.getIdentp() + ",'"
				+ br.getSeq_align() + "','" + br.getPdb_align() + "','" + br.getMidline_align() + "',CURDATE());\n";

		return str;
	}

    /**
     * Parse list of String blast results to input SQL statements, time and
     * memory consuming for huge files Use
     * 
     * @param outresults
     *            List<BlastResult>
     */
    public void generateSQLstatementsSingle(List<BlastResult> results, String currentDir) {
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
     * 
     * Mutation Parse list of String blast results to input SQL statements, time
     * and memory consuming for huge files Use
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
     * @param List<BlastResult>
     *            results
     * @param pdbHm
     * @param count
     * @param outputfile
     */
    public void genereateSQLstatementsSmallMem(List<BlastResult> results, HashMap<String, String> pdbHm, long count,
            File outputfile) {
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
            log.info("[SHELL] Start Write mutation insert.sql File from Alignment " + count + "...");
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
    public List<String> makeSQLText(List<BlastResult> results, HashMap<String, String> pdbHm) {
        List<String> outputlist = new ArrayList<String>();
        // Add transaction
        outputlist.add("SET autocommit = 0;");
        outputlist.add("start transaction;");
        for (BlastResult br : results) {
            if (pdbHm.containsKey(br.getSseqid())) {
                // do nothing
            } else {
                outputlist.add(makeTable_pdb_entry_insert(br));
                pdbHm.put(br.getSseqid(), "");
            }
            // If it is update, then call function
            if (this.updateTag) {// TODO
                outputlist.add(makeTable_pdb_seq_insert_Update(br));
                // If it is init, generate INSERT statements
            } else {
                outputlist.add(makeTable_pdb_seq_insert(br));
            }

        }
        outputlist.add("commit;");
        return outputlist;
    }

    /**
     * Mutation main body of generate SQL Insert text
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
        /*
         * This part is covered in the original G2S
         *
         * // First generate alignments SQL files List<BlastResult> al =
         * results.getAlignmentList(); for (BlastResult br : al) { if
         * (pdbHm.containsKey(br.getSseqid())) { // do nothing } else {
         * outputlist.add(makeTable_pdb_entry_insert(br));
         * pdbHm.put(br.getSseqid(), ""); } // If it is update, then call
         * function if (this.updateTag) {
         * outputlist.add(makeTable_pdb_seq_insert_Update(br)); // If it is
         * init, generate INSERT statements } else {
         * outputlist.add(makeTable_pdb_seq_insert(br)); } }
         */

        // Generate mutation SQL files
        List<MutationRecord> ml = results.getMutationList();
        for (MutationRecord mr : ml) {
            outputlist.add(makeTable_mutation_insert(mr));
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
            JAXBContext jc = JAXBContext.newInstance("org.cbioportal.g2smutation.util.blast");
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(null);
            BlastOutput blast = (BlastOutput) u.unmarshal(blastresults);
            BlastOutputIterations iterations = blast.getBlastOutputIterations();
            int count = 1;
            for (Iteration iteration : iterations.getIteration()) {
                String querytext = iteration.getIterationQueryDef();
                IterationHits hits = iteration.getIterationHits();
                for (Hit hit : hits.getHit()) {
                	//Have not test yet! for new HashSet<Integer>()
                    MutationAlignmentResult mar = parseSingleAlignmentMutation(querytext, hit, count, new HashMap<Integer,HashSet<String>>());
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
     * Parse XML structure into Object BlastResult
     * 
     * @param querytext
     * @param hit
     * @param count
     * @return
     */
    public List<BlastResult> parseSingleAlignment(String querytext, Hit hit, int count) {

        List<BlastResult> resultList = new ArrayList<BlastResult>();

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
            resultList.add(br);
            count++;
        }

        return resultList;
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
    public MutationAlignmentResult parseSingleAlignmentMutation(String querytext, Hit hit, int count, HashMap <Integer,HashSet<String>> indexHm) {

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
                    //if ((residueAlign.equals(" ") || residueAlign.equals("+")) && !residue.equals("X")) {
                    

                    int correctProteinIndex = br.qStart + i - seqGapCount;
                        /*
                         * Example here: Seq ID: 13, hitPDB: 1eg14_A_1; q:
                         * 7-239. s:29-260, PDB: 47-306
                         */
					if (indexHm.containsKey(correctProteinIndex)) {
						HashSet<String> indexSet = indexHm.get(correctProteinIndex);
//						if(querytext.startsWith("59996;P04637_1") && correctProteinIndex==106) {
//							System.out.println(correctProteinIndex+"\t"+indexSet.size()+"\t"+indexSet.toString());
//						}
						//if (indexSet.size() >= 2) {
							int correctPDBIndex = Integer.parseInt(br.sseqid.split("\\s+")[3]) + br.sStart - 1 + i
									- pdbGapCount;
							String pdbNO = br.sseqid.split("\\s+")[0];

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
						//}
					}
                }
                alignmentList.add(br);                
            }
            count++;
        }

        MutationAlignmentResult result = new MutationAlignmentResult();
        result.setAlignmentList(alignmentList);
        result.setMutationList(mutationList);
        result.setAlignmentId(count);

        return result;
    }
    
    
    /**
     * Parsing XML first to find mutation index as MUTATION_NO: as TP53: 59996_282
     * Parse XML structure into Object BlastResult Core function
     *
     * 
     * @param querytext
     *            e.g. 13;P11532_7 DMD_HUMAN;ENSP00000367974.4
     *            ENSG00000198947.15 ENST00000378702.8
     * @param hit
     * @return iHashMap <Integer,HashSet<String>> indexHm: proteinIndex, HashSet of residue 
     */
    public HashMap <Integer,HashSet<String>> parseSingleAlignmentMutationFindMutatedIndex(String querytext, Hit hit, int count, HashMap <Integer,HashSet<String>> indexHm) {

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
             * //Original solution: Include all the blast results
             * resultList.add(br); count++;
             */
            // Add filter: Only choose best alignment
            Double alignlen = Double.parseDouble(tmp.getHspAlignLen());
            // count "-" in genSequence
            int seqGapCount = 0;
            // count "-" in pdbSequence
            int pdbGapCount = 0;
            // Hardcode now, for testcase = 1
            if (isFilterAlignHighQuality(br, alignlen, testcase)) {
            //if (Double.parseDouble(tmp.getHspPositive()) / alignlen >= 0.90) {
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
                    	
                        HashSet<String> indexSet;
                        if(indexHm.containsKey(correctProteinIndex)) {
                        	indexSet = indexHm.get(correctProteinIndex);
                        }else {
                        	indexSet = new HashSet<>();                       	
                        }
                        indexSet.add(residue);
//                        if(indexSet.size()>=2) {
//                        	System.out.println(indexSet.size());
//                        }
                        indexHm.put(correctProteinIndex, indexSet); 
//                        if(querytext.startsWith("59996;P04637_1") && correctProteinIndex==106) {
//                        	System.out.println(residue+"\t"+indexHm.get(correctProteinIndex).toString());
//                    	}
                    }
                }               
            }
        }

        return indexHm;
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
     * here, this function is for test Refer to
     * https://github.com/juexinwang/g2smutation/issues/14
     * 
     * - 2 3 (1-3) - 3 2 (4-5) - 4 2 (6-7) - 2&&(3&&4) 3*2*2 (8-19) - 2&&(3||4)
     * 3*2*2 (20-31) - 2&&3 3*2 (32-37) - 2&&4 3*2 (38-43) - 2||(3&&4) 3*2*2
     * (44-55) - 2||3||4 3*2*2 (56-67) - 2||3 3*2 (68-73) - 2||4 3*2 (74-79)
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
     * generate sql in delete in V1
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
    
    /**
     * generate mutation sql in delete, in use
     * 
     * @param currentDir
     * @param list
     */
    public void generateDeleteMutationSql(String currentDir, List<String> list) {
        try {
            log.info("[PIPELINE] Generating delete SQL mutation statements");
            File outfile = new File(currentDir + this.sqlDeleteFile);
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            // delete for query and delete will cost lots of time, delete here for saving time
            outputlist.add("SET FOREIGN_KEY_CHECKS = 0;\n" +
            		"drop table IF EXISTS mutation_usage_table;\n" + 
            		"drop table IF EXISTS gpos_allmapping_pdb_entry;\n" + 
            		"drop table IF EXISTS mutation_location_entry;\n" +
            		"drop table IF EXISTS structure_annotation_entry;\n" +
            		"SET FOREIGN_KEY_CHECKS = 1;\n");
            for (String pdbName : list) {               
            	/*
            	 * Actually, these tables are rebuild every week, so we could directly delete them
            	 * Delete mutation related annotation first for their foreign key restrains
            	 * But we have to do it for it will get fail for foreign restrain.
            	String str0 = "DELETE FROM mutation_usage_table WHERE PDB_NO like '"
                        + pdbName + "_%';\n";
                outputlist.add(str0); 
            	
                String str1 = "DELETE FROM gpos_allmapping_pdb_entry WHERE PDB_NO like '"
                        + pdbName + "_%';\n";
                outputlist.add(str1);              
                
                String str2 = "DELETE mutation_location_entry FROM mutation_location_entry inner join mutation_entry on mutation_location_entry.MUTATION_ID=mutation_entry.MUTATION_ID WHERE mutation_entry.PDB_NO like '"
                        + pdbName + "_%';\n";
                outputlist.add(str2);
                
                //delete from MUTATION_ID, we could also check from PDB_NO
                String str3 = "DELETE structure_annotation_entry FROM structure_annotation_entry inner join mutation_entry on structure_annotation_entry.MUTATION_ID=mutation_entry.MUTATION_ID WHERE mutation_entry.PDB_NO like '"
                        + pdbName + "_%';\n";
                outputlist.add(str3);
                */
             
                //Then delete Foreign Key based tables, order is important. Mutation, alignment then PDB
                //delete from MUTATION_ID
                String str4 = "DELETE FROM mutation_entry WHERE PDB_NO like '"
                        + pdbName + "_%';\n";
                outputlist.add(str4);               
                
                String str5 = "DELETE pdb_seq_alignment FROM pdb_seq_alignment inner join pdb_entry on pdb_entry.pdb_no=pdb_seq_alignment.pdb_no WHERE pdb_seq_alignment.pdb_id='"
                        + pdbName + "';\n";
                outputlist.add(str5);
                
                String str6 = "DELETE FROM pdb_entry WHERE PDB_ID='" + pdbName + "';\n";
                outputlist.add(str6);
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
            int SNPNum = SNPfilelines.size() - 1;
            List<String> SNPMutationPos = new ArrayList<String>();
            List<String> SNPid = new ArrayList<String>();
            // Build SNP HashMap
            for (int i = 1; i <= SNPNum; i++) {
                SNPid.add(SNPfilelines.get(i).split("\\s+")[2]);
                SNPMutationPos.add(SNPfilelines.get(i).split("\\s+")[9] + "_" + SNPfilelines.get(i).split("\\s+")[10]
                        + "_" + SNPfilelines.get(i).split("\\s+")[12]);
                if (SNPHM.containsKey(SNPMutationPos.get(i - 1))) {
                    String SNPTempValue = SNPHM.get(SNPMutationPos.get(i - 1)) + ";" + SNPid.get(i - 1);
                    SNPHM.put(SNPMutationPos.get(i - 1), SNPTempValue);
                } else {
                    SNPHM.put(SNPMutationPos.get(i - 1), SNPid.get(i - 1));
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
            int SNPNum = SNPfilelines.size() - 1;
            List<String> SNPid = new ArrayList<String>();
            // Build Output HashMap
            for (int i = 1; i <= SNPNum; i++) {
                SNPid.add(SNPfilelines.get(i).split("\\s+")[2]);
                // log.info(SNPid.get(i-1));
                OutputSNPHM.put(SNPid.get(i - 1), SNPfilelines.get(i));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return OutputSNPHM;
    }

    public void compareMutation(int testcount) {
        try {
            log.info("********************The statistics result of the " + testcount + "th case********************");

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
                if (SNPHM.containsKey(pdbMutationPos.get(i - 1))) {
                    pdbMutationHM.put(pdbMutationNo.get(i - 1), SNPHM.get(pdbMutationPos.get(i - 1)));
                    for (int j = 0; j < pdbMutationHM.get(pdbMutationNo.get(i - 1)).split(";").length; j++) {
                        outputSNPSeq.add(OutputSNPHM.get(pdbMutationHM.get(pdbMutationNo.get(i - 1)).split(";")[j]));
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
            analyzefilelines.add("mutationresult" + testcount + ": ");
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
            outputTitle.add(
                    "chr	pos	snp_id	master_acc	master_gi	master_pos	master_res	master_var	pdb_gi	pdb	pdb_chain	pdb_res	pdb_pos	blast_ident	clinsig");
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

    /**
     * parse mutation result from inputFile and generate outputfile in SQL for
     * mutation_usage_table
     * 
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4MutatationUsageTable(String inputFilename, String outputFilename) {
        try {
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            File inFile = new File(inputFilename);
            List<String> list = FileUtils.readLines(inFile);
            for (int i = 1; i < list.size(); i++) {
                String[] str = list.get(i).split("\t");
                String strr = "INSERT INTO `mutation_usage_table` (`MUTATION_ID`,`MUTATION_NO`,`SEQ_ID`,`SEQ_NAME`,`SEQ_INDEX`,`SEQ_RESIDUE`,`PDB_NO`,`PDB_INDEX`,`PDB_RESIDUE`,`ALIGNMENT_ID`,`UPDATE_DATE`)VALUES('"
                        + str[0] + "','" + str[1] + "','" + str[2] + "','" + str[3] + "','" + str[4] + "','" + str[5]
                        + "','" + str[6] + "','" + str[7] + "','" + str[8] + "','" + str[9] + "',CURDATE());\n";
                outputlist.add(strr);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * parse mutation result from inputFile and generate outputfile in SQL for
     * mutation_location_entry
     * 
     * @param mUsageRecord
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4MutationLocationEntry(MutationUsageRecord mUsageRecord,
            String outputFilename) {
    	log.info("Start writing " + outputFilename);
        HashMap<Integer, String> mutationIdHm = mUsageRecord.getMutationIdHm();
        HashMap<Integer, String> mutationNoIdRHm = mUsageRecord.getMutationNoIdRHm();
        try {
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            
            for (int mutationId : mutationIdHm.keySet()) {
                String chr_pos = mutationIdHm.get(mutationId);
                // chr_pos may be "" if API return 500 error.

                String mutationNo = mutationNoIdRHm.get(mutationId);

                // Corner Case:
                // HSCHR6_MHC_MCF_29989718_29989720
                String[] strArray = chr_pos.split("_");
                // in case chr_pos == " "
                if (strArray.length>1) {
					String chr = strArray[0];
					for (int i = 1; i < strArray.length - 2; i++) {
						chr = chr + strArray[i] + "_";
					}

					String str = "INSERT INTO `mutation_location_entry` (`MUTATION_ID`,`MUTATION_NO`,`CHR_POS`,`CHR`,`POS_START`,`POS_END`)VALUES('"
							+ mutationId + "','" + mutationNo + "','" + chr_pos + "','" + chr + "','"
							+ strArray[strArray.length - 2] + "','" + strArray[strArray.length - 1] + "');\n";
					outputlist.add(str);
                }

            }
            outputlist.add("commit;");
            log.info(outputlist.size());
            FileUtils.writeLines(new File(outputFilename), outputlist);
        } catch (Exception ex) {
        	log.error(outputFilename + " has something wrong, please check!");
            ex.printStackTrace();
        }
    }

    /**
     * generate dbSNP annotation for dbsnp_entry
     * 
     * @param MutationUsageRecord
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4DbsnpEntry(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename) {
        HashMap<String, List<Integer>> mutationIdRHm = mUsageRecord.getMutationIdRHm(); // <chr_pos,List
                                                                                        // of
                                                                                        // mutationId>
        HashMap<Integer,String> mutationNoIdRHm = mUsageRecord.getMutationNoIdRHm();
        HashMap<String, String> rsHm = new HashMap<>();// <mutationNo,chr_pos_snpid>

        try {
            LineIterator it = FileUtils.lineIterator(new File(inputFilename));
            while (it.hasNext()) {
                String str = it.nextLine();
                String[] strArray = str.split("\t");
                String gpos = strArray[0] + "_" + strArray[1];
                if (mutationIdRHm.containsKey(gpos)) {
                    List<Integer> tmplist = mutationIdRHm.get(gpos);
                    for (Integer mutationId : tmplist) {
                        rsHm.put(mutationNoIdRHm.get(mutationId), gpos + "_" + strArray[2]);
                    }
                }
            }

            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            for (String mutationNo : rsHm.keySet()) {
                String chr_pos_snpid = rsHm.get(mutationNo);
                String[] strArray = chr_pos_snpid.split("_");
                String chr_pos = "";
                for (int i = 0; i < strArray.length - 2; i++) {
                    chr_pos = chr_pos + strArray[i] + "_";
                }
                chr_pos = chr_pos + strArray[strArray.length - 2];
                // System.out.println(chr_pos_snpid);
                String str = "INSERT INTO `dbsnp_entry` (`CHR_POS`,`MUTATION_NO`,`RS_ID`)VALUES('" + chr_pos + "','"
                        + mutationNo + "','" + strArray[strArray.length - 1] + "');\n";
                outputlist.add(str);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * generate clinvar annotation for clinvar_entry
     * 
     * @param MutationUsageRecord
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4ClinvarEntry(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename) {
        HashMap<String, List<Integer>> mutationIdRHm = mUsageRecord.getMutationIdRHm(); //key:chr_pos, value: List of MUTATION_ID
        HashMap<Integer, String> mutationNoIdRHm = mUsageRecord.getMutationNoIdRHm(); //key:MUTATION_ID, value: MUTATION_NO
        HashMap<String, String> rsHm = new HashMap<>();// <mutationNo,string as
                                                        // all information in
                                                        // the line>
        FileOperatingUtil fou = new FileOperatingUtil();
        StringUtil su = new StringUtil();
        try {
            LineIterator it = FileUtils.lineIterator(new File(inputFilename));
            while (it.hasNext()) {
                String str = it.nextLine();
                if (!str.startsWith("#")) {
                    String[] strArray = str.split("\t");
                    String gpos = strArray[0] + "_" + strArray[1];
                    if (mutationIdRHm.containsKey(gpos)) {
                        List<Integer> tmplist = mutationIdRHm.get(gpos);
                        for (Integer mutationId : tmplist) {
                            rsHm.put(mutationNoIdRHm.get(mutationId), str);
                        }
                    }
                }
            }

            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            for (String mutationNo : rsHm.keySet()) {
                String contentStr = rsHm.get(mutationNo);
                String[] strArray = contentStr.split("\t");
                String chr_pos = strArray[0] + "_" + strArray[1];
                HashMap<String, String> contentHm = fou.clinvarContentStr2Map(strArray[7]);
                // original: manually, we change accordingly
                // String str = "INSERT INTO `clinvar_entry`
                // (`CHR_POS`,`MUTATION_NO`,`CLINVAR_ID`,`REF`,`ALT`,`AF_ESP`,`AF_EXAC`,`AF_TGP`,"
                // +
                // "`ALLELEID`,`CLNDN`,`CLNDNINCL`,`CLNDISDB`,`CLNDISDBINCL`,`CLNHGVS`,`CLNREVSTAT`,`CLNSIG`,`CLNSIGCONF`,"
                // +
                // "`CLNSIGINCL`,`CLNVC`,`CLNVCSO`,`CLNVI`,`DBVARID`,`GENEINFO`,`MC`,`ORIGIN`,`RS`,`SSR`,`UPDATE_DATE`)VALUES('"
                // + chr_pos + "','" + mutationNo + "','" +
                // strArray[2] + "','"+ strArray[3] + "','" + strArray[4] +
                // "','"
                // + "',CURDATE());\n";
                String keystr = "INSERT INTO `clinvar_entry` (`CHR_POS`,`MUTATION_NO`,`CLINVAR_ID`,`REF`,`ALT`";
                String valstr = ",`UPDATE_DATE`)VALUES('" + chr_pos + "','" + mutationNo + "','"
                        + strArray[2] + "','" + strArray[3] + "','" + strArray[4] + "'";

                for (String key : contentHm.keySet()) {
                    keystr = keystr + ",`" + key + "`";
                    valstr = valstr + ",'" + su.toSQLstring(contentHm.get(key)) + "'";
                }
                valstr = valstr + ",CURDATE());\n";

                outputlist.add(keystr + valstr);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * generate cosmic annotation for cosmic_entry
     * 
     * @param MutationUsageRecord
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4CosmicEntry(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename) {
        HashMap<String, List<Integer>> mutationIdRHm = mUsageRecord.getMutationIdRHm();// key:chr_pos,
                                                                                       // value:
                                                                                       // List
                                                                                       // of
                                                                                       // MUTATION_ID
        HashMap<Integer,String> mutationNoIdRHm = mUsageRecord.getMutationNoIdRHm();
        HashMap<String, String> rsHm = new HashMap<>();// <mutationNo,string as
                                                        // all information in
                                                        // the line>
        HashMap<String, String> rsPosHm = new HashMap<>();// <mutationId,chr_pos>

        String keystr = "INSERT INTO `cosmic_entry` (`CHR_POS`,`MUTATION_NO`";
        try {
            LineIterator it = FileUtils.lineIterator(new File(inputFilename));
            int count = 0;
            int coorcount = 0;
            while (it.hasNext()) {
                String contentStr = it.nextLine();
                String[] strArray = contentStr.split("\t");
                
                if (count == 0) {
                    // header, convert header to inject key string
                    for (String str : strArray) {
                        str = str.replaceAll("\\s+", "_").toUpperCase();
                        if (str.equals("MUTATION_ID")) {// duplicate key in
                                                        // Cosmic, we changed
                                                        // accordingly
                            str = "COSMIC_MUTATION_ID";
                        }if (str.equals("GENOME-WIDE_SCREEN")) {// duplicate key in
                            // Cosmic, we changed accordingly
                        	str = "GENOME_WIDE_SCREEN";
                        }
                        keystr = keystr + ",`" + str + "`";
                    }
                    keystr = keystr + ",`COSMIC_VERSION`)VALUES('";
                } else {
                    //System.out.println(count + " * " + strArray[23]);
                    if (!strArray[23].equals("")) {                   	
                        String[] tmpArray = strArray[23].split(":");
                        String[] posArray = tmpArray[1].split("-");
                        int start = Integer.parseInt(posArray[0]);
                        int end = Integer.parseInt(posArray[1]);
                        for (int i = start; i <= end; i++) {
                            String gpos = tmpArray[0] + "_" + Integer.toString(i);
                            if (mutationIdRHm.containsKey(gpos)) {
                                List<Integer> tmplist = mutationIdRHm.get(gpos);
                                for (Integer mutationId : tmplist) {
                                    String mutationNo = mutationNoIdRHm.get(mutationId);
                                    rsHm.put(mutationNo, contentStr);
                                    rsPosHm.put(mutationNo, gpos);
                                }
                            }
                        }
                        coorcount++;
                    } else {
                        // if does not map to genomic location, we just neglect
                        // now,
                        // e.g. COSM5790 malignant_melanoma c.?del? p.?fs
                        // Deletion - Frameshift
                        // e.g. COSM12980 c.? p.E746_A750del Deletion -In frame
                        // Confirmed somatic variant ...
                        // Unknown;Substitution - Missense;Deletion - In
                        // frame;Insertion - In frame;Deletion -
                        // Frameshift;Complex - deletion inframe;Frameshift
                        // System.out.println(count + " * " + strArray[19]);
                    }
                    if (count % 1000000 == 0) {
                        log.info(Integer.toString(count) + " has procceeded");
                    }
                }
                count++;
            }

            log.info(Integer.toString(coorcount) + " has genomic coordinates");
            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            for (String mutationNo : rsHm.keySet()) {
                String contentStr = rsHm.get(mutationNo);
                String chr_pos = rsPosHm.get(mutationNo);
                String[] strArray = contentStr.split("\t");

                String valstr = chr_pos + "','" + mutationNo + "'";
                for (String str : strArray) {
                    valstr = valstr + ",'" + str + "'";
                }
                //For some reasons, the end of some cosmic annotation has been trimmed off
            	//So we have to complete them
            	int lengthNormal = 35; // 35 is the length of normal COSMIC annotation
            	if (strArray.length<lengthNormal) {
            		for(int i=strArray.length; i<lengthNormal; i++) {
            			valstr = valstr + ",''";
            		}
            	}
                valstr = valstr + ",'COSMIC v87, released 13-NOV-18');\n";

                outputlist.add(keystr + valstr);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * generate genie annotation for genie_entry
     * 
     * @param MutationUsageRecord
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4GenieEntry(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename) {
        parseGenerateMutationResultSQL4bothGenieTcga(mUsageRecord, inputFilename, outputFilename, "genie");
    }

    /**
     * generate tcga annotation for tcga_entry
     * 
     * @param MutationUsageRecord
     * @param inputFilename
     * @param outputFilename
     */
    public void parseGenerateMutationResultSQL4TcgaEntry(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename) {
        parseGenerateMutationResultSQL4bothGenieTcga(mUsageRecord, inputFilename, outputFilename, "tcga");
    }

    /**
     * As genie and tcga are mainly share with the same structure, this function
     * is designed for both othem.
     * 
     * @param mUsageRecord
     * @param inputFilename
     * @param outputFilename
     * @param annotationType
     */
    public void parseGenerateMutationResultSQL4bothGenieTcga(MutationUsageRecord mUsageRecord, String inputFilename,
            String outputFilename, String annotationType) {
        //
        String dbTableName = "";
        String dbTableVersion = "";
        String dbTableVersionUse = "";
        int commonMiss = 0;
        int commonAdd = 0;
        int ignoreLineNo = 0;
        int convertLineNo = 1;
        if (annotationType == "genie") {
            dbTableName = "genie_entry";
            dbTableVersion = "GENIE_VERSION";
            dbTableVersionUse = "5.0-public";
            commonMiss = 112;
            commonAdd = 11;
            ignoreLineNo = 0;
            convertLineNo = 1;
        } else if (annotationType == "tcga") {
            dbTableName = "tcga_entry";
            dbTableVersion = "TCGA_VERSION";
            dbTableVersionUse = "mc3.v0.2.8.PUBLIC";
            commonMiss = 114;
            commonAdd = 11;
            ignoreLineNo = -1;
            convertLineNo = 0;
        } else {
            log.error("Now only support genie and tcga");
        }

        // main function
        HashMap<String, List<Integer>> mutationIdRHm = mUsageRecord.getMutationIdRHm();// key:chr_pos,
                                                                                       // value:
                                                                                       // List
                                                                                       // of
                                                                                       // MUTATION_ID
        HashMap<Integer,String> mutationNoIdRHm = mUsageRecord.getMutationNoIdRHm();
        HashMap<String, String> rsHm = new HashMap<>();// <mutationNo,string as
                                                        // all information in
                                                        // the line>
        HashMap<String, String> rsPosHm = new HashMap<>();// <mutationNo,chr_pos>

        String keystr = "INSERT INTO `" + dbTableName + "` (`CHR_POS`,`MUTATION_No`";
        try {
            LineIterator it = FileUtils.lineIterator(new File(inputFilename));
            int count = 0;
            while (it.hasNext()) {
                String contentStr = it.nextLine();
                String[] strArray = contentStr.split("\t");
                if (count == ignoreLineNo) {
                    // header, ignore
                } else if (count == convertLineNo) {
                    // annotation header, convert header to inject key string
                    for (String str : strArray) {
                        if (annotationType.equals("tcga")) {
                            if (str.equals("STRAND")) {
                                str = "STRAND_VEP";
                            }
                        }
                        str = str.replaceAll("\\s+", "_").toUpperCase();
                        keystr = keystr + ",`" + str + "`";
                    }
                    keystr = keystr + ",`" + dbTableVersion + "`)VALUES('";
                } else {
                    int start = Integer.parseInt(strArray[5]);
                    int end = Integer.parseInt(strArray[6]);
                    for (int i = start; i <= end; i++) {
                        String gpos = strArray[4] + "_" + Integer.toString(i);
                        if (mutationIdRHm.containsKey(gpos)) {
                            List<Integer> tmplist = mutationIdRHm.get(gpos);
                            for (Integer mutationId : tmplist) {
                                String mutationNo = mutationNoIdRHm.get(mutationId);
                                rsHm.put(mutationNo, contentStr);
                                rsPosHm.put(mutationNo, gpos);
                            }
                        }
                    }

                    if (count % 1000000 == 0) {
                        log.info(Integer.toString(count) + " has procceeded");
                    }
                }
                count++;
            }

            List<String> outputlist = new ArrayList<String>();
            // Add transaction
            outputlist.add("SET autocommit = 0;");
            outputlist.add("start transaction;");
            for (String mutationNo : rsHm.keySet()) {
                String contentStr = rsHm.get(mutationNo);
                String chr_pos = rsPosHm.get(mutationNo);
                String[] strArray = contentStr.split("\t");

                String valstr = chr_pos + "','" + mutationNo + "'";
                for (String str : strArray) {
                    // in case have 3'UTR in the text
                    if (str.contains("'")) {
                        // System.out.println("* "+str);
                        str = str.replace("'", "\\'");
                        // System.out.println(str);
                    }
                    valstr = valstr + ",'" + str + "'";
                }
                // System.out.println(strArray.length);
                if (annotationType.equals("genie")) {// Only genie may have
                                                     // problem
                    // deal with the last characters, they may miss in EXAC
                    // part.
                    if (strArray.length == commonMiss) {// temp hardcode here
                                                        // related with
                        // the genie_entry;
                        for (int i = 0; i < commonAdd; i++) {// the complete one
                                                             // should be
                            // 123
                            valstr = valstr + ",''";
                        }
                    }
                }
                valstr = valstr + ",'" + dbTableVersionUse + "');\n";

                outputlist.add(keystr + valstr);
            }
            outputlist.add("commit;");
            FileUtils.writeLines(new File(outputFilename), outputlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

/**
 * Used for store mutation and alignment list for generating sql
 * 
 * @author Juexin Wang
 *
 */
class MutationAlignmentResult {
    List<BlastResult> alignmentList;
    List<MutationRecord> mutationList;
    int alignmentId;

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

	public int getAlignmentId() {
		return alignmentId;
	}

	public void setAlignmentId(int alignmentId) {
		this.alignmentId = alignmentId;
	}
}
