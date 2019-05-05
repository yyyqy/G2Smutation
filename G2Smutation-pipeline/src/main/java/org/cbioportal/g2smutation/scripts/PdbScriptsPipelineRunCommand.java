package org.cbioportal.g2smutation.scripts;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.cbioportal.g2smutation.annotation.StructureAnnotation;
import org.cbioportal.g2smutation.util.CommandProcessUtil;
import org.cbioportal.g2smutation.util.FTPClientUtil;
import org.cbioportal.g2smutation.util.FileOperatingUtil;
import org.cbioportal.g2smutation.util.PdbSequenceUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.blast.BlastDataBase;
import org.cbioportal.g2smutation.util.models.MutationUsageRecord;
import org.cbioportal.g2smutation.util.models.SNPAnnotationType;
import org.cbioportal.g2smutation.util.models.StructureAnnotationRecord;

/**
 * Main function from entrance of G2Smutation pipeline
 * Shell-based command running in initialization, update and statistics 
 *
 * @author Juexin Wang
 *
 */

public class PdbScriptsPipelineRunCommand {
    final static Logger log = Logger.getLogger(PdbScriptsPipelineRunCommand.class);
    private BlastDataBase db;
    private int matches;
    private int seqFileCount;
    private boolean updateTag;
    private int allSqlCount;
    private String dateVersion;
    private String currentDir;

    /**
     * Constructor
     */
    public PdbScriptsPipelineRunCommand() {
        this.matches = 0;
        this.seqFileCount = -1;
        this.updateTag = false;
    }

    public BlastDataBase getDb() {
        return db;
    }

    public void setDb(BlastDataBase db) {
        this.db = db;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getSeqFileCount() {
        return seqFileCount;
    }

    public void setSeqFileCount(int seqFileCount) {
        this.seqFileCount = seqFileCount;
    }
    
    public boolean isUpdateTag() {
        return updateTag;
    }

    public void setUpdateTag(boolean updateTag) {
        this.updateTag = updateTag;
    }
    

    public int getAllSqlCount() {
        return allSqlCount;
    }

    public void setAllSqlCount(int allSqlCount) {
        this.allSqlCount = allSqlCount;
    }

    /**
     * main steps of init pipeline
     */
    public void runInit() {
    	PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(this);       
        PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
        this.currentDir = ReadConfig.workspace;

    	log.info("********************[Init STEP 0]********************");
        log.info("Delete old sql files in the workspace");
        //cleanupG2S();
    	       
        log.info("********************[Init STEP 1]********************");
        log.info("Initialize G2S service for alignments and residue mapping");
        //initializeG2S(preprocess, parseprocess);
        
        log.info("********************[Init STEP 2]********************");
        log.info("Find mutation using G2S service");
        //generateMutation(parseprocess);

        log.info("********************[Init STEP 3]********************");
        log.info("Annotate mutation");
        generateAnnotation(parseprocess, false);
        
        log.info("********************[Init STEP 4]********************");
        log.info("[FileSystem] Clean Up");
        //postInitialCleanup();
    }

    /**
     * main steps of update pipeline
     */
    public void runUpdatePDB() {      
        this.db = new BlastDataBase(ReadConfig.pdbSeqresDownloadFile);
        this.setUpdateTag(true);
        PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
        PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(this);
        
        log.info("********************[Update STEP 1]********************");
        log.info("Update G2S service for alignments and residue mapping");
        List<String> listOld = updateG2S(preprocess, parseprocess);
        
        log.info("********************[Update STEP 2]********************");
        log.info("Update mutation using G2S service");
        updateMutation(preprocess, parseprocess, listOld);
        
        log.info("********************[Update STEP 3]********************");
        log.info("Update Annotate mutation");
        generateAnnotation(parseprocess, this.updateTag);
        
        log.info("********************[Update STEP 4]********************");
        log.info("[FileSystem] Clean Up");
        updateCleanup();
    }
    
    List<String> updateG2S(PdbScriptsPipelinePreprocessing preprocess, PdbScriptsPipelineMakeSQL parseprocess){
    	CommandProcessUtil cu = new CommandProcessUtil();       
        this.seqFileCount = Integer.parseInt(ReadConfig.updateSeqFastaFileNum);

        log.info("********************[Update STEP 1.1]********************"); 
        log.info("Set dateVersion of updating and create a folder as YYYYMMDD under the main folder");
        String dateVersion = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());        
        String currentDir = ReadConfig.workspace + dateVersion + "/";
        this.dateVersion = dateVersion;
        this.currentDir = currentDir;
        log.info("Update G2S at "+ this.dateVersion);

        log.info("********************[Update STEP 1.2]********************");
        log.info("Down load and prepare new, obsolete and modified PDB in weekly update from PDB");
        List<String> listOld = preprocess.prepareUpdatePDBFile(currentDir, ReadConfig.pdbSeqresDownloadFile,
                ReadConfig.delPDB);
        preprocess.preprocessPDBsequencesUpdate(currentDir + ReadConfig.pdbSeqresDownloadFile,
                currentDir + ReadConfig.pdbSeqresFastaFile);

        log.info("********************[Update STEP 1.3]********************");
        log.info("Create new blast alignments in new and modified PDB");
        ArrayList<String> paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.pdbSeqresFastaFile);
        paralist.add(currentDir + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);

        // Modified for smaller disk usage, blast/parse/insert/delete
        log.info("********************[Update STEP 1.4]********************");
        log.info("Insert delete SQL of obsolete and modified alignments");
        parseprocess.generateDeleteSql(currentDir, listOld);
        paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.sqlDeleteFile);
        cu.runCommand("mysql", paralist);

        log.info("********************[Update STEP 1.5]********************");
        log.info("blastp ensembl genes against pdb; Create and insert SQL statements of new and modified alignments;");
        HashMap<String, String> pdbHm = new HashMap<String, String>();
        boolean mutationTag = false;
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile + "." + new Integer(i).toString());
                paralist.add(currentDir + this.db.resultfileName + "." + new Integer(i).toString());
                paralist.add(currentDir + this.db.dbName);
                cu.runCommand("blastp", paralist);

                parseprocess.parse2sqlPartition(false, currentDir, this.seqFileCount, i, pdbHm, mutationTag);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                cu.runCommand("mysql", paralist);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                cu.runCommand("gzip", paralist);
            }
        } else {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile);
            paralist.add(currentDir + this.db.resultfileName);
            paralist.add(currentDir + this.db.dbName);
            cu.runCommand("blastp", paralist);

            parseprocess.parse2sql(false, currentDir, this.seqFileCount, mutationTag);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.sqlInsertFile);
            cu.runCommand("mysql", paralist);
        }

        log.info("********************[Update STEP 1.6]********************");
        log.info("After update all the new alignments,Create complete PDB sequences for de novo sequence blast");
        preprocess.denovoPreprocessPDBsequencesUpdate(dateVersion, listOld, currentDir + ReadConfig.pdbSeqresFastaFile,
                currentDir + ReadConfig.pdbSeqresFastaFile);

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);
        paralist.add(ReadConfig.workspace + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);

        log.info("********************[Update STEP 1.7]********************");
        log.info("Change messages.properties in web module");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.releaseTag);
        paralist.add(currentDir + ReadConfig.releaseTagResult);
        cu.runCommand("releaseTag", paralist);

        log.info("********************[Update STEP 1.8]********************");
        log.info("Use MYSQL to update records");
        preprocess.releasTagUpdateSQL(currentDir + ReadConfig.releaseTagResult,
                currentDir + ReadConfig.updateStatisticsSQL);
        paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.updateStatisticsSQL);
        cu.runCommand("mysql", paralist);
       
        return listOld;
    }
    
    void updateMutation(PdbScriptsPipelinePreprocessing preprocess, PdbScriptsPipelineMakeSQL parseprocess, List<String> listOld){
    	CommandProcessUtil cu = new CommandProcessUtil();       
        ArrayList<String> paralist = new ArrayList<>();
        String currentDir = this.currentDir;
        
        log.info("********************[Update STEP 2.1]********************");
        log.info("Create and insert Mutation SQL statements of new and modified alignments;");
        HashMap<String, String> pdbHm = new HashMap<String, String>(); 
        boolean mutationTag = true;
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                //use blastp results from xml
                parseprocess.parse2sqlPartition(false, currentDir, this.seqFileCount, i, pdbHm, mutationTag);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlMutationInsertFile + "." + new Integer(i).toString());
                cu.runCommand("mysql", paralist);

                //delete xml results here
                paralist = new ArrayList<String>();
                paralist.add(currentDir + this.db.resultfileName + "." + new Integer(i).toString());
                cu.runCommand("rm", paralist);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlMutationInsertFile + "." + new Integer(i).toString());
                cu.runCommand("gzip", paralist);
            }
        } else {
            parseprocess.parse2sql(false, currentDir, this.seqFileCount, mutationTag);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.sqlMutationInsertFile);
            cu.runCommand("mysql", paralist);
        }
    	
        log.info("********************[Update STEP 2.2]********************");
        log.info("[SQL] Total update mutation_usage_table by Drop and create data schema");               
        generateMutationUsageTable(parseprocess);
    	
    }
    
    void updateCleanup(){
    	CommandProcessUtil cu = new CommandProcessUtil();       
        ArrayList<String> paralist = new ArrayList<>();
        String currentDir = this.currentDir;        
        
    	// Clean up
        if (ReadConfig.saveSpaceTag.equals("true")) {
            log.info("[PIPELINE] Start cleaning up in filesystem");

            if (this.seqFileCount == -1) {
                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlInsertFile);
                cu.runCommand("gzip", paralist);
            }

            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.sqlDeleteFile);
            cu.runCommand("gzip", paralist);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + this.db.dbphr);
            cu.runCommand("rm", paralist);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + this.db.dbpin);
            cu.runCommand("rm", paralist);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + this.db.dbpsq);
            cu.runCommand("rm", paralist);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + this.db.resultfileName);
            cu.runCommand("rm", paralist);

            // raw, delete one, zip one
            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.pdbSeqresDownloadFile);
            cu.runCommand("rm", paralist);
            
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + ".*");
            cu.runCommand("rm", paralist);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.pdbSeqresFastaFile);
            cu.runCommand("gzip", paralist);
        }
    	
    }

    /* main steps of statistics */
    /*
     * Could directly use for generate all the tables;
     */
    public void runStatistics() {
        this.db = new BlastDataBase(ReadConfig.pdbSeqresFastaFile);
        CommandProcessUtil cu = new CommandProcessUtil();
        ArrayList<String> paralist = new ArrayList<String>();
        
        //Test for thresholds
        // https://github.com/juexinwang/G2Smutation/issues/14
        //for (int testcount = 3; testcount <= 79; testcount++) {
        for (int testcount = 1; testcount <= 1; testcount++) {
            log.info("********************Start Test " + testcount + "th case ********************");
            // PdbScriptsPipelineMakeSQL parseprocess = new
            // PdbScriptsPipelineMakeSQL(this);
            // For Test Purpose:
            PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(this, testcount);
            this.seqFileCount = 10;

            // Step 0:
            log.info("********************[STEP 0]********************");
            log.info("Clean Up *.sql.*");
            if (this.seqFileCount != -1) {
                for (int i = 0; i < this.seqFileCount; i++) {
                    paralist = new ArrayList<String>();
                    paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                    cu.runCommand("rm", paralist);
                }
            } else {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile);
                cu.runCommand("rm", paralist);
            }

            // Step 1:
            log.info("********************[STEP 1]********************");
            log.info("[PrepareSQL] Parse xml results and output as input sql statments");
            parseprocess.parse2sqlMutation(false, ReadConfig.workspace, this.seqFileCount);

            
            // Step 2:
            log.info("********************[STEP 2]********************");
            log.info("[SQL] Create data schema. CAUTION: only on test from 1 to 79");
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.resourceDir + ReadConfig.dbNameScript);
            cu.runCommand("mysql", paralist);
            

            // Step 3:
            log.info("********************[STEP 3]********************");
            log.info("[SQL] Import gene sequence SQL statements into the database");
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.insertSequenceSQL);
            cu.runCommand("mysql", paralist);           

            // Step 4:
            log.info("********************[STEP 4]********************");
            log.info("[SQL] Import INSERT SQL statements into the database (Warning: This step takes time)");
            if (this.seqFileCount != -1) {
                for (int i = 0; i < this.seqFileCount; i++) {
                    paralist = new ArrayList<String>();
                    paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                    cu.runCommand("mysql", paralist);
                }
            } else {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile);
                cu.runCommand("mysql", paralist);
            }

            /*
            // Step 5:
            log.info("********************[STEP 5]********************");
            log.info("[SQL] Find Mutation Info)");
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.resourceDir + ReadConfig.alignFilterStatsSQL);
            paralist.add(ReadConfig.workspace + ReadConfig.alignFilterStatsResult + "." + testcount);
            cu.runCommand("releaseTag", paralist);
            */

        }
        
        //Analyze the mutation amount and rate
        log.info("********************Statistics Result********************");
//        for (int testcount = 1; testcount <= 79; testcount++) {
//        	PdbScriptsPipelineMakeSQL compareprocess = new PdbScriptsPipelineMakeSQL(this, testcount);
//        	compareprocess.compareMutation(testcount);
//        	
//        }
//        PdbScriptsPipelineApiToSQL generateSQLfile = new PdbScriptsPipelineApiToSQL();
//        generateSQLfile.generateRsSQLfile();
    }
    
    void cleanupG2S(){
    	CommandProcessUtil cu = new CommandProcessUtil();
    	ArrayList<String> paralist = new ArrayList<String>();
    	log.info("********************[STEP 0.1]********************");
        log.info("Clean Up g2s *.sql.*");
        //this.seqFileCount = 10;
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                cu.runCommand("rm", paralist);
            }
        } else {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile);
            cu.runCommand("rm", paralist);
        }
    }
    
    void initializeG2S(PdbScriptsPipelinePreprocessing preprocess, PdbScriptsPipelineMakeSQL parseprocess){
    	this.db = new BlastDataBase(ReadConfig.pdbSeqresFastaFile); 
    	parseprocess.setDb(this.db);
        ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        boolean mutationTag = false;
        
        // Step 1
        // Read Sequences from cloned whole PDB, need at least 24G free spaces
        // and at least 12 hours
        log.info("********************[STEP 1.1]********************");
        log.info("Download PDB and parse to sequences");
        log.info(
                "[Download] A cloned copy of whole PDB will be downloaded and parse to sequences, unziped and parsing to get the PDB sequences");
        PdbSequenceUtil pu = new PdbSequenceUtil();

		switch (ReadConfig.initChoice) {
		case "1":
			// Choice 1/3: Parsing from specific folder
			pu.initSequencefromFolder("/home/wangjue/gsoc/pdb_all/pdb",
					ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile);
		case "2":
			// Choice 2/3: Sync all the pdb in java code
			pu.initSequencefromFolder(ReadConfig.pdbRepo, ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile);
		case "3":
			// Choice 3/3: Sync and Parsing all the pdb from pdbRepo
			pu.initSequencefromAll(ReadConfig.pdbRepo, ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile);
		default:
			log.error("Init choice should be 1 or 2 or 3. Default should be 3");
		}
        	
        // Step 2:
        log.info("********************[STEP 1.2]********************");
        log.info("[Processing] Preprocess PDB sequence and sequence files");
        // Select only PDB files of proteins, parse PDB files to sequences
        preprocess.preprocessPDBsequences(ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile,
                ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);

        
        log.info("********************[STEP 1.3]********************");
        log.info("[Download] Download and unzip Ensembl, Uniprot and Isoform");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.ensemblWholeSource);
        paralist.add(ReadConfig.workspace
                + ReadConfig.ensemblWholeSource.substring(ReadConfig.ensemblWholeSource.lastIndexOf("/") + 1));
        cu.runCommand("wget", paralist);

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace
                + ReadConfig.ensemblWholeSource.substring(ReadConfig.ensemblWholeSource.lastIndexOf("/") + 1));
        paralist.add(ReadConfig.workspace + ReadConfig.ensemblDownloadFile);
        cu.runCommand("gunzip", paralist);

        FTPClientUtil fc = new FTPClientUtil();
        fc.downloadFilefromFTP(ReadConfig.swissprotWholeSource, ReadConfig.workspace
                + ReadConfig.swissprotWholeSource.substring(ReadConfig.swissprotWholeSource.lastIndexOf("/") + 1));

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace
                + ReadConfig.swissprotWholeSource.substring(ReadConfig.swissprotWholeSource.lastIndexOf("/") + 1));
        paralist.add(ReadConfig.workspace + ReadConfig.swissprotDownloadFile);
        cu.runCommand("gunzip", paralist);

        // TrEmbl, it is huge and needs to be careful Normally we do not encourage use this, for it is too huge
        if(Boolean.parseBoolean(ReadConfig.initTrembl)){
        	paralist = new ArrayList<String>();
            paralist.add(ReadConfig.tremblWholeSource);
            paralist.add(ReadConfig.workspace +
            ReadConfig.tremblWholeSource.substring(ReadConfig.tremblWholeSource.lastIndexOf("/") + 1));
            cu.runCommand("wgetftp", paralist);

            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace +
            ReadConfig.tremblWholeSource.substring(ReadConfig.tremblWholeSource.lastIndexOf("/") + 1));
            paralist.add(ReadConfig.workspace + ReadConfig.tremblDownloadFile);
            cu.runCommand("gunzip", paralist);       	
        }
        
        fc.downloadFilefromFTP(ReadConfig.isoformWholeSource, ReadConfig.workspace
                + ReadConfig.isoformWholeSource.substring(ReadConfig.isoformWholeSource.lastIndexOf("/") + 1));

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace
                + ReadConfig.isoformWholeSource.substring(ReadConfig.isoformWholeSource.lastIndexOf("/") + 1));
        paralist.add(ReadConfig.workspace + ReadConfig.isoformDownloadFile);
        cu.runCommand("gunzip", paralist);

        // Step 4:
        log.info("********************[STEP 1.4]********************");
        log.info("[Processing] Incorprate ensembl, swissprot, trembl and isoform together");
        log.info("[Processing] Different from G2S original version, we only use proteins of ensembl human and uniprot human");
        // This step takes memory, then split into small files to save the
        // running memory
        // Read Uniprot Files First, get HashMap<Uniprot, Accession>
        HashMap<String, String> accHm = preprocess
                .getUniProtAccHm(ReadConfig.workspace + ReadConfig.swissprotDownloadFile);

        HashMap<String, String> uniqSeqHm = new HashMap<String, String>();
        //Here we only choose HUMAN sequeunces in Uniprot, otherwise use function preprocessUniqSeqUniprot instead
        uniqSeqHm = preprocess.preprocessUniqSeqUniprotHuman(ReadConfig.workspace + ReadConfig.swissprotDownloadFile, accHm,
                uniqSeqHm);
        //uniqSeqHm = preprocess.preprocessUniqSeqUniprot(ReadConfig.workspace + ReadConfig.swissprotDownloadFile, accHm,
        //        uniqSeqHm);

        // Trembl, not used anymore
        // Caution:Not test here!
        if(Boolean.parseBoolean(ReadConfig.initTrembl)){
        	uniqSeqHm = preprocess.preprocessUniqSeqUniprot(ReadConfig.workspace + ReadConfig.tremblDownloadFile, accHm, uniqSeqHm);
        }
        
        uniqSeqHm = preprocess.preprocessUniqSeqUniprotHuman(ReadConfig.workspace + ReadConfig.isoformDownloadFile, accHm,
                uniqSeqHm);
        //uniqSeqHm = preprocess.preprocessUniqSeqUniprot(ReadConfig.workspace + ReadConfig.isoformDownloadFile, accHm,
        //        uniqSeqHm);

        uniqSeqHm = preprocess.preprocessUniqSeqEnsembl(ReadConfig.workspace + ReadConfig.ensemblDownloadFile,
                uniqSeqHm);

        this.seqFileCount = preprocess.preprocessGENEsequences(uniqSeqHm,
                ReadConfig.workspace + ReadConfig.seqFastaFile);
        
        // Step 5:
        log.info("********************[STEP 1.5]********************");
        log.info("[PrepareBlast] Build the database by makeblastdb");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);
        paralist.add(ReadConfig.workspace + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);
            
        //this.seqFileCount = 57;
        // Step 6:
        log.info("********************[STEP 1.6]********************");
        log.info("[Blast] blastp ensembl genes against pdb (Warning: This step takes time)");
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile + "." + new Integer(i).toString());
                paralist.add(ReadConfig.workspace + this.db.resultfileName + "." + new Integer(i).toString());
                paralist.add(ReadConfig.workspace + this.db.dbName);
                cu.runCommand("blastp", paralist);
            }
        } else {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile);
            paralist.add(ReadConfig.workspace + this.db.resultfileName);
            paralist.add(ReadConfig.workspace + this.db.dbName);
            cu.runCommand("blastp", paralist);
        }
               
        // Step 7:
        log.info("********************[STEP 1.7]********************");
        log.info("[PrepareSQL] Parse results and output as input sql statments");
        parseprocess.parse2sql(false, ReadConfig.workspace, this.seqFileCount, mutationTag);

        // Step 8:
        log.info("********************[STEP 1.8]********************");
        log.info("[SQL] Create data schema");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.dbNameScript);
        cu.runCommand("mysql", paralist);

        // Step 9:
        log.info("********************[STEP 1.9]********************");
        log.info("[SQL] Import gene sequence SQL statements into the database");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.insertSequenceSQL);
        cu.runCommand("mysql", paralist);

        // Step 10:
        log.info("********************[STEP 1.10]********************");
        log.info("[SQL] Import INSERT SQL statements into the database (Warning: This step takes time)");
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                cu.runCommand("mysql", paralist);
            }
        } else {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile);
            cu.runCommand("mysql", paralist);
        }
            	
    }

    void generateMutation(PdbScriptsPipelineMakeSQL parseprocess){
    	this.db = new BlastDataBase(ReadConfig.pdbSeqresFastaFile); 
    	parseprocess.setDb(this.db);
    	ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        this.seqFileCount =10;
        
    	// Step 2.1:
        log.info("********************[STEP 2.1]********************");
        log.info("[PrepareSQL] Parse xml results and output as input sql statments for mutation");
        parseprocess.parse2sqlMutation(false, ReadConfig.workspace, this.seqFileCount);   

        /*
        // Test if it needed
        // Step 2.2:
        log.info("********************[STEP 2.2]********************");
        log.info("[SQL] Import gene sequence SQL statements into the database");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.insertSequenceSQL);
        cu.runCommand("mysql", paralist);  
        */         

        // Step 2.3:
        log.info("********************[STEP 2.3]********************");
        log.info("[SQL] Import mutation INSERT SQL statements into the database (Warning: This step takes time)");
        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.sqlMutationInsertFile + "." + new Integer(i).toString());
                cu.runCommand("mysql", paralist);
            }
        } else {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.sqlMutationInsertFile);
            cu.runCommand("mysql", paralist);
        }   
        log.info("********************[Update STEP 2.4]********************");
        log.info("[SQL] Find Mutation Info, Output and Generate mutation_usage_table for Injection)");
        generateMutationUsageTable(parseprocess);
    }
    
    void generateMutationUsageTable(PdbScriptsPipelineMakeSQL parseprocess){
    	ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        String currentDir = this.currentDir;
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.mutationGenerateSQL);
        paralist.add(currentDir + ReadConfig.mutationResult);
        cu.runCommand("releaseTag", paralist);
        
        parseprocess.parseGenerateMutationResultSQL4MutatationUsageTable(currentDir + ReadConfig.mutationResult, ReadConfig.workspace + ReadConfig.mutationInjectSQLUsage);       
        
        paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.mutationInjectSQLUsage);
        cu.runCommand("mysql", paralist);
    }
    
    void generateAnnotation(PdbScriptsPipelineMakeSQL parseprocess, boolean updateTag){
    	ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        String currentDir = this.currentDir;
        
        log.info("********************[STEP 3.1]********************");
        log.info("[File] Read results from file, generate HashMap for usage"); 
        //FileOperatingUtil fou = new FileOperatingUtil();
        //MutationUsageRecord mUsageRecord = fou.readMutationResult2MutationUsageRecord(currentDir + ReadConfig.mutationResult);
        
        /**
        log.info("********************[STEP 3.1.1]********************");
        log.info("[DEBUG] Serialize and deserialize for debug usage");        
        // Serialize the MutationUsageRecord into the tmpfile!!!!
        String filename = ReadConfig.workspace + "mUsageRecord.ser";
        try{
            FileUtils.writeByteArrayToFile(new File(filename), SerializationUtils.serialize(mUsageRecord));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        */
        // Deserialize!!!!
        String filename = ReadConfig.workspace + "mUsageRecord.ser";
        MutationUsageRecord mUsageRecord = new MutationUsageRecord();
        // Deserialize the tmpfile to MutationUsageRecord
        try{           
        	mUsageRecord = (MutationUsageRecord)SerializationUtils.deserialize(FileUtils.readFileToByteArray(new File(filename)));
        }catch(Exception ex){
            ex.printStackTrace();
        }
         
                 
        log.info("********************[STEP 3.2]********************");
        log.info("[SQL] Use mutation results, update table mutation_location_entry)");        
//        parseprocess.parseGenerateMutationResultSQL4MutationLocationEntry(mUsageRecord, currentDir + ReadConfig.mutationInjectSQLLocation);       
        
//        paralist = new ArrayList<String>();
//        paralist.add(currentDir + ReadConfig.mutationInjectSQLLocation);
//        cu.runCommand("mysql", paralist);  
        
        log.info("********************[STEP 3.3]********************");
        log.info("[STRUCTURE] For residues from mutation info, running structure annotation and inject to table structure_annotation_entry");
        log.info("[STRUCTURE] Download weekly Cath");
        
	        FTPClientUtil fc = new FTPClientUtil();
	        fc.downloadFilefromFTP(ReadConfig.cathAllSource, currentDir
	        		+ ReadConfig.cathAllSource.substring(ReadConfig.cathAllSource.lastIndexOf("/") + 1));
	        System.out.println(currentDir
	        		+ ReadConfig.cathAllSource.substring(ReadConfig.cathAllSource.lastIndexOf("/") + 1));
	        paralist = new ArrayList<String>();
	        paralist.add(currentDir
	        		+ ReadConfig.cathAllSource.substring(ReadConfig.cathAllSource.lastIndexOf("/") + 1));
	        paralist.add(currentDir + ReadConfig.cathFile);
	        cu.runCommand("gunzip", paralist); 
	        
	        fc.downloadFilefromFTP(ReadConfig.cathNamesSource, currentDir
	        		+ ReadConfig.cathNamesSource.substring(ReadConfig.cathNamesSource.lastIndexOf("/") + 1));
	        System.out.println(currentDir
	        		+ ReadConfig.cathNamesSource.substring(ReadConfig.cathNamesSource.lastIndexOf("/") + 1));
	        paralist = new ArrayList<String>();
	        paralist.add(currentDir
	        		+ ReadConfig.cathNamesSource.substring(ReadConfig.cathNamesSource.lastIndexOf("/") + 1));
	        paralist.add(currentDir + ReadConfig.cathNamesFile);
	        cu.runCommand("gunzip", paralist); 
	        
        StructureAnnotation sanno = new StructureAnnotation();
        
        log.info("[STRUCTURE] Start running naccess"); 
//        sanno.generateNaccessResults(mUsageRecord);
        
        log.info("[STRUCTURE] Start processing naccess rsa results");
//        sanno.generateNaccessResultsBuried(mUsageRecord);
        
        log.info("[STRUCTURE] naccess complete and start parsing"); 
        sanno.parseGenerateMutationResultSQL4StructureAnnotationEntry(mUsageRecord,currentDir + ReadConfig.mutationInjectSQLStructure);       
//        paralist = new ArrayList<String>();
//        paralist.add(currentDir + ReadConfig.mutationInjectSQLStructure);
//        cu.runCommand("mysql", paralist);
        
//        //dbsnp, clinvar, cosmic, genie, tcga annotation         
//        if (!updateTag){
//        	//TODO Update Clinvar later, next version
//        	log.info("********************[STEP 3.4]********************");
//            log.info("[SQL] Clinvar: Download weekly Clinvar, parsing annotation file and inject to table clinvar_entry)");
//            
//            FTPClientUtil fc = new FTPClientUtil();
//            fc.downloadFilefromFTP(ReadConfig.clinvarWholeSource, currentDir
//                    + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
//            System.out.println(currentDir
//                    + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
//            
//            paralist = new ArrayList<String>();
//            paralist.add(currentDir
//                    + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
//            paralist.add(currentDir + ReadConfig.clinvarFile);
//            cu.runCommand("gunzip", paralist);      
//            
//            parseprocess.parseGenerateMutationResultSQL4ClinvarEntry(mUsageRecord, currentDir + ReadConfig.clinvarFile, currentDir + ReadConfig.mutationInjectSQLClinvar);       
//           
//            paralist = new ArrayList<String>();
//            paralist.add(ReadConfig.resourceDir + ReadConfig.annotationClinvarSQL);
//            cu.runCommand("mysql", paralist);
//            
//            paralist = new ArrayList<String>();
//            paralist.add(currentDir + ReadConfig.mutationInjectSQLClinvar);
//            cu.runCommand("mysql", paralist);
//        	
//			log.info("********************[STEP 3.5]********************");
//			log.info(
//					"[SQL] DBSNP: For residues from mutation info, parsing annotation file and inject to table dbsnp_entry)");
//			parseprocess.parseGenerateMutationResultSQL4DbsnpEntry(mUsageRecord,
//					currentDir + ReadConfig.dbsnpFile,
//					currentDir + ReadConfig.mutationInjectSQLDbsnp);
//
//			paralist = new ArrayList<String>();
//			paralist.add(ReadConfig.resourceDir + ReadConfig.annotationDbsnpSQL);
//			cu.runCommand("mysql", paralist);
//
//			paralist = new ArrayList<String>();
//			paralist.add(currentDir + ReadConfig.mutationInjectSQLDbsnp);
//			cu.runCommand("mysql", paralist);
//
//			log.info("********************[STEP 3.6]********************");
//			log.info(
//					"[SQL] For residues from mutation info, parsing annotation file and inject to table cosmic_entry)");
//			parseprocess.parseGenerateMutationResultSQL4CosmicEntry(mUsageRecord,
//					currentDir + ReadConfig.cosmicFile,
//					currentDir + ReadConfig.mutationInjectSQLCosmic);
//
//			paralist = new ArrayList<String>();
//			paralist.add(ReadConfig.resourceDir + ReadConfig.annotationCosmicSQL);
//			cu.runCommand("mysql", paralist);
//
//			paralist = new ArrayList<String>();
//			paralist.add(currentDir + ReadConfig.mutationInjectSQLCosmic);
//			cu.runCommand("mysql", paralist);
//
//			log.info("********************[STEP 3.7]********************");
//			log.info("[SQL] For residues from mutation info, parsing annotation file and inject to table genie_entry)");
//			parseprocess.parseGenerateMutationResultSQL4GenieEntry(mUsageRecord,
//					currentDir + ReadConfig.genieFile,
//					currentDir + ReadConfig.mutationInjectSQLGenie);
//
//			paralist = new ArrayList<String>();
//			paralist.add(ReadConfig.resourceDir + ReadConfig.annotationGenieSQL);
//			cu.runCommand("mysql", paralist);
//
//			paralist = new ArrayList<String>();
//			paralist.add(currentDir + ReadConfig.mutationInjectSQLGenie);
//			cu.runCommand("mysql", paralist);
//
//			log.info("********************[STEP 3.8]********************");
//			log.info("[SQL] For residues from mutation info, parsing annotation file and inject to table tcga_entry)");
//			parseprocess.parseGenerateMutationResultSQL4TcgaEntry(mUsageRecord,
//					currentDir + ReadConfig.tcgaFile,
//					currentDir + ReadConfig.mutationInjectSQLTcga);
//
//			paralist = new ArrayList<String>();
//			paralist.add(ReadConfig.resourceDir + ReadConfig.annotationTcgaSQL);
//			cu.runCommand("mysql", paralist);
//
//			paralist = new ArrayList<String>();
//			paralist.add(currentDir + ReadConfig.mutationInjectSQLTcga);
//			cu.runCommand("mysql", paralist);
//
//        }
        PdbScriptsPipelineApiToSQL generateSQLfile = new PdbScriptsPipelineApiToSQL();
        /*
         * Old implementation: 10days of mapping all dbSNP in millions of SNP, not use anymore
         *
         *
        log.info("********************[STEP 3.9]********************");
        log.info("[PrepareSQL] Call url and output as input rs sql statments on all possible rsSNPs Caution: Very Slow now");        

        this.rsSqlCount = generateSQLfile.generateRsSQLfile();
        
        //Add multiple threads on all rs mapping
        //this.rsSqlCount = generateSQLfile.generateRsSQLfileMT();
               
        this.rsSqlCount =237;
        // Step 13:
        log.info("********************[STEP 13]********************");
        log.info("[SQL] Import RS INSERT SQL statements into the database (Warning: This step takes time)");
        for (int i = 0; i < this.rsSqlCount; i++) {
            paralist = new ArrayList<String>();
            paralist.add(ReadConfig.workspace + ReadConfig.rsSqlInsertFile + "." + new Integer(i).toString());
            cu.runCommand("mysql", paralist);
        }
        */
                 
        //mapdb: https://github.com/jankotek/mapdb/ off-heap solutions, for it is so huge
        DB gpos2proHmdb = DBMaker.fileDB(ReadConfig.workspace+ReadConfig.gpos2proHmDbFile).make();
        Map gpos2proHm = gpos2proHmdb.hashMap("map").createOrOpen();
        // Serialize the MutationUsageRecord into the tmpfile!!!!
//        if(!this.updateTag){
//        	//Generate SNP information using dbsnp, clinvar, cosmic, genie, tcga
//            log.info("********************[STEP 3.9]********************");
//            log.info("[SQL] Generate and import into the table gpos_allmapping_entry");
//            HashMap<String,String> inputHm = new HashMap<String,String>();
//            //Test here
//            //inputHm = fou.collectAllSNPs2Map(inputHm, SNPAnnotationType.CLINVAR);
//            for(SNPAnnotationType snpCollectionName: SNPAnnotationType.values()){
//                inputHm = fou.collectAllSNPs2Map(inputHm, snpCollectionName);
//            }                
//                    
//            this.allSqlCount = generateSQLfile.generateGposAllMappingSQLfile(inputHm);
//            System.out.println("allSql Mapping Count:"+allSqlCount);                
//            
//            for (int i = 0; i <this.allSqlCount; i++) {
//                paralist = new ArrayList<String>();
//                paralist.add(currentDir + ReadConfig.rsSqlInsertFile + "." + new Integer(i).toString());
//                cu.runCommand("mysql", paralist);
//            }      
//            
//            log.info("********************[STEP 3.10]********************");
//            log.info("[SQL] Generate and import into the table gpos_protein_entry, this step is very slow for querying lots of API");
//            
//            if(Boolean.parseBoolean(ReadConfig.initConcurrent)){
//            	//Concurrent version, use POST
//                gpos2proHm = fou.convertgpso2proHmMT(inputHm, gpos2proHm, true);       
//            }else{
//            	//true for POST, false for GET, POST is better
//                gpos2proHm = fou.convertgpso2proHm(inputHm, gpos2proHm, true);         	
//            }
//      	
//        }else{
//			// Deserialize use mapdb!!!!
//            //mapdb: https://github.com/jankotek/mapdb/ off-heap solutions, for it is so huge
//            // gpos2proHmdb gpos2proHm are there
//        }
//        
//        this.allSqlCount = generateSQLfile.generateGposProteinSQLfile(gpos2proHm);
//        log.info("gpos to protein Count: "+allSqlCount);
//        
//        for (int i = 0; i < this.allSqlCount; i++) {
//            paralist = new ArrayList<String>();
//            paralist.add(currentDir + ReadConfig.gposSqlInsertFile + "." + new Integer(i).toString());
//            cu.runCommand("mysql", paralist);
//        }
//
        log.info("********************[STEP 3.11]********************");
        log.info("[SQL] Import All Mapping INSERT SQL statements into table gpos_allmapping_pdb_entry (Warning: This step takes time)");
        
        //call 12million SNP through inner API, should start G2Smutation-web
        this.allSqlCount = generateSQLfile.generateAllMappingSQLfileHuge(gpos2proHm);
        log.info("FileCount"+this.allSqlCount);
        gpos2proHmdb.close();
        
        //this.allSqlCount =12;
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.updateAllSnpSql);
        cu.runCommand("mysql", paralist);       
        
        for (int i = 0; i < this.allSqlCount; i++) {
            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.gposAlignSqlInsertFile + "." + new Integer(i).toString());
            cu.runCommand("mysql", paralist);
        }
    }
    
    void postInitialCleanup(){
    	ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        
        //Old clean Up
//    	if(ReadConfig.saveSpaceTag.equals("true")){
//        	log.info("[PIPELINE] Start cleaning up in filesystem"); 
//        	paralist = new ArrayList<String>();
//        	paralist.add(ReadConfig.workspace+ReadConfig.sqlEnsemblSQL);
//        	cu.runCommand("gzip", paralist);
//          
//        	if (this.ensemblFileCount != -1) { 
//        		for (int i = 0; i < this.ensemblFileCount; i++) {
//        			paralist = new ArrayList<String>();
//        			paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." + new Integer(i).toString()); 
//        			cu.runCommand("gzip", paralist); 
//        			paralist = new ArrayList<String>(); 
//        			paralist.add(ReadConfig.workspace + this.db.resultfileName + "." + new Integer(i).toString());
//        			cu.runCommand("rm", paralist); 
//        			} 
//        	}else{ 
//        		paralist = new ArrayList<String>();
//        		paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile);
//        		cu.runCommand("gzip", paralist);
//        		paralist = new ArrayList<String>();
//        		paralist.add(ReadConfig.workspace + this.db.resultfileName ); 
//        		cu.runCommand("rm", paralist);
//        	}
//          
//          paralist = new ArrayList<String>(); paralist.add(ReadConfig.pdbRepo);
//          cu.runCommand("rm", paralist);
//         }
    }
}

