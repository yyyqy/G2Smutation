package org.cbioportal.G2Smutation.scripts;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.util.CommandProcessUtil;
import org.cbioportal.G2Smutation.util.FTPClientUtil;
import org.cbioportal.G2Smutation.util.FileOperatingUtil;
import org.cbioportal.G2Smutation.util.PdbSequenceUtil;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.cbioportal.G2Smutation.util.blast.BlastDataBase;
import org.cbioportal.G2Smutation.util.models.MutationUsageRecord;

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
    private int rsSqlCount;

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
    
    public int getRsSqlCount() {
        return rsSqlCount;
    }

    public void setRsSqlCount(int rsSqlCount) {
        this.rsSqlCount = rsSqlCount;
    }
    
    public boolean isUpdateTag() {
        return updateTag;
    }

    public void setUpdateTag(boolean updateTag) {
        this.updateTag = updateTag;
    }

    /**
     * main steps of init pipeline
     */
    public void runInit() {

        this.db = new BlastDataBase(ReadConfig.pdbSeqresFastaFile);
        PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
        CommandProcessUtil cu = new CommandProcessUtil();
        PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(this);
        ArrayList<String> paralist = new ArrayList<String>();
        

        /*
        // Step 1
        // Read Sequences from cloned whole PDB, need at least 24G free spaces
        // and at least 12 hours
        log.info("********************[STEP 1]********************");
        log.info("Download PDB and parse to sequences");
        log.info(
                "[Download] A cloned copy of whole PDB will be downloaded and parse to sequences, unziped and parsing to get the PDB sequences");
        PdbSequenceUtil pu = new PdbSequenceUtil();

        
        //Choice 1/3: Parsing from specific folder
        //pu.initSequencefromFolder("/home/wangjue/gsoc/pdb_all/pdb",
        //       ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile);
        
        //Choice 2/3: Sync all the pdb in java code
//         pu.initSequencefromFolder(ReadConfig.pdbRepo,ReadConfig.workspace
//         + ReadConfig.pdbSeqresDownloadFile);
        
        //Choice 3/3: Sync and Parsing all the pdb from pdbRepo
        pu.initSequencefromAll(ReadConfig.pdbRepo, ReadConfig.workspace +
                ReadConfig.pdbSeqresDownloadFile);

        // Step 2:
        log.info("********************[STEP 2]********************");
        log.info("[Processing] Preprocess PDB sequence and sequence files");
        // Select only PDB files of proteins, parse PDB files to sequences
        preprocess.preprocessPDBsequences(ReadConfig.workspace + ReadConfig.pdbSeqresDownloadFile,
                ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);

        
        log.info("********************[STEP 3]********************");
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

        // TrTembl, it is huge and needs to be careful
        // Normally we do not encourage use this

        // paralist = new ArrayList<String>();
        // paralist.add(ReadConfig.tremblWholeSource);
        // paralist.add(ReadConfig.workspace +
        // ReadConfig.tremblWholeSource.substring(ReadConfig.tremblWholeSource.lastIndexOf("/")
        // + 1));
        // cu.runCommand("wgetftp", paralist);

        // paralist = new ArrayList<String>();
        // paralist.add(ReadConfig.workspace +
        // ReadConfig.tremblWholeSource.substring(ReadConfig.tremblWholeSource.lastIndexOf("/")
        // + 1));
        // paralist.add(ReadConfig.workspace + ReadConfig.tremblDownloadFile);
        // cu.runCommand("gunzip", paralist);

        fc.downloadFilefromFTP(ReadConfig.isoformWholeSource, ReadConfig.workspace
                + ReadConfig.isoformWholeSource.substring(ReadConfig.isoformWholeSource.lastIndexOf("/") + 1));

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace
                + ReadConfig.isoformWholeSource.substring(ReadConfig.isoformWholeSource.lastIndexOf("/") + 1));
        paralist.add(ReadConfig.workspace + ReadConfig.isoformDownloadFile);
        cu.runCommand("gunzip", paralist);

        // Step 4:
        log.info("********************[STEP 4]********************");
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
        // uniqSeqHm = preprocess.preprocessUniqSeq(ReadConfig.workspace +
        // ReadConfig.tremblDownloadFile,uniqSeqHm);
        
        uniqSeqHm = preprocess.preprocessUniqSeqUniprotHuman(ReadConfig.workspace + ReadConfig.isoformDownloadFile, accHm,
                uniqSeqHm);
        //uniqSeqHm = preprocess.preprocessUniqSeqUniprot(ReadConfig.workspace + ReadConfig.isoformDownloadFile, accHm,
        //        uniqSeqHm);

        uniqSeqHm = preprocess.preprocessUniqSeqEnsembl(ReadConfig.workspace + ReadConfig.ensemblDownloadFile,
                uniqSeqHm);

        this.seqFileCount = preprocess.preprocessGENEsequences(uniqSeqHm,
                ReadConfig.workspace + ReadConfig.seqFastaFile);

        
        // Step 5:
        log.info("********************[STEP 5]********************");
        log.info("[PrepareBlast] Build the database by makeblastdb");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);
        paralist.add(ReadConfig.workspace + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);

            
        //this.seqFileCount = 57;
        // Step 6:
        log.info("********************[STEP 6]********************");
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
        
        this.seqFileCount = 10;
        
        // Step 7:
        log.info("********************[STEP 7]********************");
        log.info("[PrepareSQL] Parse results and output as input sql statments");
        parseprocess.parse2sql(false, ReadConfig.workspace, this.seqFileCount);

        // Step 8:
        log.info("********************[STEP 8]********************");
        log.info("[SQL] Create data schema");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.dbNameScript);
        cu.runCommand("mysql", paralist);

        // Step 9:
        log.info("********************[STEP 9]********************");
        log.info("[SQL] Import gene sequence SQL statements into the database");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.insertSequenceSQL);
        cu.runCommand("mysql", paralist);

        // Step 10:
        log.info("********************[STEP 10]********************");
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
        
        
        
        
        /**
         * Check later
         */
        //this.seqFileCount = 10;
/*
        // Step 11:
        log.info("********************[STEP 11]********************");
        log.info("Clean Up g2s *.sql.*");
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

        // Step 12:
        log.info("********************[STEP 12]********************");
        log.info("[PrepareSQL] Parse xml results and output as input sql statments for mutation");
        parseprocess.parse2sqlMutation(false, ReadConfig.workspace, this.seqFileCount);

/*        
        // Step 13:
        log.info("********************[STEP 2]********************");
        log.info("[SQL] Test statistics, Create data schema. CAUTION: only on test from 1 to 79");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.dbNameScript);
        cu.runCommand("mysql", paralist);
        

        // Step 3:
        log.info("********************[STEP 3]********************");
        log.info("[SQL] Import gene sequence SQL statements into the database");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.insertSequenceSQL);
        cu.runCommand("mysql", paralist); 
          

        // Step 13:
        log.info("********************[STEP 13]********************");
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
        */
        
        
        
        
        
        

        /*
        // Step 11:
        log.info("********************[STEP 11]********************");
        log.info("[SQL] Usage Parts start. Find Mutation Info, Output and Generate Mutation SQL Injection Table)");
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.mutationGenerateSQL);
        paralist.add(ReadConfig.workspace + ReadConfig.mutationResult);
        cu.runCommand("releaseTag", paralist);
        
        parseprocess.parseGenerateMutationResultSQL4MutatationUsageTable(ReadConfig.workspace + ReadConfig.mutationResult, ReadConfig.workspace + ReadConfig.mutationInjectSQLUsage);       
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLUsage);
        cu.runCommand("mysql", paralist);
        */
        /**
         * Annotation Start
         */
        
        // Step 12:
        /*
        log.info("********************[STEP 12]********************");
        log.info("[SQL] Read results from file, generate HashMap for usage"); 
        FileOperatingUtil fou = new FileOperatingUtil();
        MutationUsageRecord mUsageRecord = fou.readMutationResult2MutationUsageRecord(ReadConfig.workspace + ReadConfig.mutationResult);
        
        // Serialize the MutationUsageRecord into the tmpfile
        String filename = ReadConfig.workspace + "mUsageRecord.ser";
        try{
            FileUtils.writeByteArrayToFile(new File(filename), SerializationUtils.serialize(mUsageRecord));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        */
        
        String filename = ReadConfig.workspace + "mUsageRecord.ser";
        MutationUsageRecord mUsageRecord = new MutationUsageRecord();
        // Deserialize the tmpfile to MutationUsageRecord
        try{           
            mUsageRecord = (MutationUsageRecord)SerializationUtils.deserialize(FileUtils.readFileToByteArray(new File(filename)));
        }catch(Exception ex){
            ex.printStackTrace();
        }    
        
        
        // Step 13: 
        /*
        log.info("********************[STEP 13]********************");
        log.info("[SQL] Use mutation results, update table mutation_location_entry)");        
        parseprocess.parseGenerateMutationResultSQL4MutationLocationEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.mutationInjectSQLLocation);       
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLLocation);
        cu.runCommand("mysql", paralist);
        
        
        /*
        // Step 14:
        // TODO: Structural annotation for table structure_annotation_entry
        // Qinyuan
        log.info("********************[STEP 14]********************");
        log.info("[SQL] For residues from mutation info, running structure annotation and inject to table structure_annotation_entry");
        
        parseprocess.parseGenerateMutationResultSQL4StructureAnnotationEntry(mUsageRecord,ReadConfig.workspace + ReadConfig.mutationInjectSQLStructure);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLStructure);
        cu.runCommand("mysql", paralist);
        */
        
        //TODO: dbsnp, clinvar, cosmic, genie, tcga annotation in Table 15-19
        /*
        // Step 15:  
        log.info("********************[STEP 15]********************");
        log.info("[SQL] DBSNP: For residues from mutation info, parsing annotation file and inject to table dbsnp_entry)");
        parseprocess.parseGenerateMutationResultSQL4DbsnpEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.dbsnpFile, ReadConfig.workspace + ReadConfig.mutationInjectSQLDbsnp);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.annotationDbsnpSQL);
        cu.runCommand("mysql", paralist);
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLDbsnp);
        cu.runCommand("mysql", paralist);
        
        // Step 16:  
        log.info("********************[STEP 16]********************");
        log.info("[SQL] Clinvar: Download weekly Clinvar, parsing annotation file and inject to table clinvar_entry)");
        
        FTPClientUtil fc = new FTPClientUtil();
        fc.downloadFilefromFTP(ReadConfig.clinvarWholeSource, ReadConfig.workspace
                + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
        System.out.println(ReadConfig.workspace
                + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace
                + ReadConfig.clinvarWholeSource.substring(ReadConfig.clinvarWholeSource.lastIndexOf("/") + 1));
        paralist.add(ReadConfig.workspace + ReadConfig.clinvarFile);
        cu.runCommand("gunzip", paralist);      
        
        parseprocess.parseGenerateMutationResultSQL4ClinvarEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.clinvarFile, ReadConfig.workspace + ReadConfig.mutationInjectSQLClinvar);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.annotationClinvarSQL);
        cu.runCommand("mysql", paralist);
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLClinvar);
        cu.runCommand("mysql", paralist);
        */
        
        // Step 17:  
        log.info("********************[STEP 17]********************");
        log.info("[SQL] For residues from mutation info, parsing annotation file and inject to table cosmic_entry)");
        parseprocess.parseGenerateMutationResultSQL4CosmicEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.cosmicFile, ReadConfig.workspace + ReadConfig.mutationInjectSQLCosmic);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.annotationCosmicSQL);
        cu.runCommand("mysql", paralist);
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLCosmic);
        cu.runCommand("mysql", paralist);
        
        /*
        // Step 18:  
        log.info("********************[STEP 18]********************");
        log.info("[SQL] For residues from mutation info, parsing annotation file and inject to table genie_entry)");
        parseprocess.parseGenerateMutationResultSQL4GenieEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.genieFile, ReadConfig.workspace + ReadConfig.mutationInjectSQLGenie);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.annotationGenieSQL);
        cu.runCommand("mysql", paralist);
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLGenie);
        cu.runCommand("mysql", paralist);
        
        // Step 19:  
        log.info("********************[STEP 19]********************");
        log.info("[SQL] For residues from mutation info, parsing annotation file and inject to table tcga_entry)");
        parseprocess.parseGenerateMutationResultSQL4TcgaEntry(mUsageRecord, ReadConfig.workspace + ReadConfig.tcgaFile, ReadConfig.workspace + ReadConfig.mutationInjectSQLTcga);       
       
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.annotationTcgaSQL);
        cu.runCommand("mysql", paralist);
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.mutationInjectSQLTcga);
        cu.runCommand("mysql", paralist);
        
        
        
        
        
          
        /** For further development on mapping all SNPs using g2s
         *  Good points are we have already got all dbsnp mapped
         *  But we need to rerun for the mapping may have some problems before 02/2019 fixing 
         *  Need to run dbsnp, cosmic, clinvar, genie, tcga
         */
        /*
        // Step 12:
        log.info("********************[STEP 12]********************");
        log.info("[PrepareSQL] Call url and output as input rs sql statments. Caution: Very Slow now");
        PdbScriptsPipelineApiToSQL generateSQLfile = new PdbScriptsPipelineApiToSQL();
        this.rsSqlCount = generateSQLfile.generateRsSQLfile();
        
        //Add multiple threads
        //TODO: Still does not work now
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
        
        // Step 14:
        log.info("********************[STEP 14]********************");
        log.info("[FileSystem] Clean Up");
        /*
         * if(ReadConfig.saveSpaceTag.equals("true")){ log.info(
         * "[PIPELINE] Start cleaning up in filesystem"); paralist = new
         * ArrayList<String>();
         * paralist.add(ReadConfig.workspace+ReadConfig.sqlEnsemblSQL);
         * cu.runCommand("gzip", paralist);
         * 
         * if (this.ensemblFileCount != -1) { for (int i = 0; i <
         * this.ensemblFileCount; i++) { paralist = new ArrayList<String>();
         * paralist.add(ReadConfig.workspace + ReadConfig.sqlInsertFile + "." +
         * new Integer(i).toString()); cu.runCommand("gzip", paralist); paralist
         * = new ArrayList<String>(); paralist.add(ReadConfig.workspace +
         * this.db.resultfileName + "." + new Integer(i).toString());
         * cu.runCommand("rm", paralist); } } else { paralist = new
         * ArrayList<String>(); paralist.add(ReadConfig.workspace +
         * ReadConfig.sqlInsertFile); cu.runCommand("gzip", paralist); paralist
         * = new ArrayList<String>(); paralist.add(ReadConfig.workspace +
         * this.db.resultfileName ); cu.runCommand("rm", paralist); }
         * 
         * paralist = new ArrayList<String>(); paralist.add(ReadConfig.pdbRepo);
         * cu.runCommand("rm", paralist); }
         */

    }

    /**
     * main steps of update pipeline
     */
    public void runUpdatePDB() {
        CommandProcessUtil cu = new CommandProcessUtil();
        this.db = new BlastDataBase(ReadConfig.pdbSeqresDownloadFile);
        this.setUpdateTag(true);
        PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
        PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(this);
        this.seqFileCount = Integer.parseInt(ReadConfig.updateSeqFastaFileNum);

        // Step 1: Set dateVersion of updating and create a folder as YYYYMMDD
        // under the main folder
        String dateVersion = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String currentDir = ReadConfig.workspace + dateVersion + "/";

        // Step 2: Download and prepare new, obsolete and modified PDB in weekly
        // update from PDB
        List<String> listOld = preprocess.prepareUpdatePDBFile(currentDir, ReadConfig.pdbSeqresDownloadFile,
                ReadConfig.delPDB);

        preprocess.preprocessPDBsequencesUpdate(currentDir + ReadConfig.pdbSeqresDownloadFile,
                currentDir + ReadConfig.pdbSeqresFastaFile);

        // Step 3: Create new blast alignments in new and modified PDB
        ArrayList<String> paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.pdbSeqresFastaFile);
        paralist.add(currentDir + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);

        // Original implementation, take some disk spaces
        /*
         * // Step 4: blastp ensembl genes against pdb; Use splited FASTA
         * results if (this.seqFileCount != -1) { for (int i = 0; i <
         * this.seqFileCount; i++) { paralist = new ArrayList<String>();
         * paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile + "." +
         * new Integer(i).toString()); paralist.add(currentDir +
         * this.db.resultfileName + "." + new Integer(i).toString());
         * paralist.add(currentDir + this.db.dbName); cu.runCommand("blastp",
         * paralist); } } else { paralist = new ArrayList<String>();
         * paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile);
         * paralist.add(currentDir + this.db.resultfileName);
         * paralist.add(currentDir + this.db.dbName); cu.runCommand("blastp",
         * paralist); }
         * 
         * // Step 4: Obsolete: blastp ensembl genes against pdb; Use one input,
         * // drawback is too huge xml results // The problem is too huge
         * results for one blast results file // paralist = new
         * ArrayList<String>(); // paralist.add(ReadConfig.workspace +
         * ReadConfig.seqFastaFile); // paralist.add(currentDir +
         * this.db.resultfileName); // paralist.add(currentDir +
         * this.db.dbName); // cu.runCommand("blastp", paralist);
         * 
         * // Step 5: Insert delete SQL of obsolete and modified alignments
         * parseprocess.generateDeleteSql(currentDir, listOld); paralist = new
         * ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.sqlDeleteFile); cu.runCommand("mysql", paralist);
         * 
         * // Step 6: Create and insert SQL statements of new and modified //
         * alignments; Use splited FASTA results parseprocess.parse2sql(false,
         * currentDir, this.seqFileCount);
         * 
         * if (this.seqFileCount != -1) { for (int i = 0; i < this.seqFileCount;
         * i++) { paralist = new ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
         * cu.runCommand("mysql", paralist); } } else { paralist = new
         * ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.sqlInsertFile); cu.runCommand("mysql", paralist); }
         * 
         * // Step 6: Obsolete: Create and insert SQL statements of new and //
         * modified alignments; Use one input, drawback is too huge xml results
         * // The problem is too huge results for one blast results file //
         * parseprocess.parse2sql(true, currentDir); // paralist = new
         * ArrayList<String>(); // paralist.add(currentDir +
         * ReadConfig.sqlInsertFile); // cu.runCommand("mysql", paralist);
         * 
         * // Step 7: After update all the new alignments, // Create complete
         * PDB sequences for de novo sequence blast
         * preprocess.denovoPreprocessPDBsequencesUpdate(dateVersion, listOld,
         * currentDir + ReadConfig.pdbSeqresFastaFile, currentDir +
         * ReadConfig.pdbSeqresFastaFile);
         * 
         * paralist = new ArrayList<String>(); paralist.add(ReadConfig.workspace
         * + ReadConfig.pdbSeqresFastaFile); paralist.add(ReadConfig.workspace +
         * this.db.dbName); cu.runCommand("makeblastdb", paralist);
         * 
         * // Step 8: Create release tags // Change messages.properties in web
         * module paralist = new ArrayList<String>();
         * paralist.add(ReadConfig.resourceDir + ReadConfig.releaseTag);
         * paralist.add(currentDir + ReadConfig.releaseTagResult);
         * cu.runCommand("releaseTag", paralist);
         * 
         * // Use MYSQL to update preprocess.releasTagUpdateSQL(currentDir +
         * ReadConfig.releaseTagResult, currentDir +
         * ReadConfig.updateStatisticsSQL); paralist = new ArrayList<String>();
         * paralist.add(currentDir + ReadConfig.updateStatisticsSQL);
         * cu.runCommand("mysql", paralist);
         * 
         * // Step 9: Clean up if (ReadConfig.saveSpaceTag.equals("true")) {
         * log.info("[PIPELINE] Start cleaning up in filesystem");
         * 
         * if (this.seqFileCount != -1) {
         * 
         * for (int i = 0; i < this.seqFileCount; i++) { paralist = new
         * ArrayList<String>(); paralist.add(currentDir + this.db.resultfileName
         * + "." + new Integer(i).toString()); cu.runCommand("rm", paralist); }
         * 
         * }
         * 
         * if (this.seqFileCount != -1) { for (int i = 0; i < this.seqFileCount;
         * i++) { paralist = new ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
         * cu.runCommand("gzip", paralist); }
         * 
         * } else { paralist = new ArrayList<String>(); paralist.add(currentDir
         * + ReadConfig.sqlInsertFile); cu.runCommand("gzip", paralist);
         * 
         * }
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.sqlDeleteFile); cu.runCommand("gzip", paralist);
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * this.db.dbphr); cu.runCommand("rm", paralist);
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * this.db.dbpin); cu.runCommand("rm", paralist);
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * this.db.dbpsq); cu.runCommand("rm", paralist);
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * this.db.resultfileName); cu.runCommand("rm", paralist);
         * 
         * // raw, delete one, zip one paralist = new ArrayList<String>();
         * paralist.add(currentDir + ReadConfig.pdbSeqresDownloadFile);
         * cu.runCommand("rm", paralist);
         * 
         * paralist = new ArrayList<String>(); paralist.add(currentDir +
         * ReadConfig.pdbSeqresFastaFile); cu.runCommand("gzip", paralist); }
         */

        // Modified for smaller disk usage, blast/parse/insert/delete
        // Step 4: Insert delete SQL of obsolete and modified alignments
        parseprocess.generateDeleteSql(currentDir, listOld);
        paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.sqlDeleteFile);
        cu.runCommand("mysql", paralist);

        HashMap<String, String> pdbHm = new HashMap<String, String>();
        // Step 5: Create and insert SQL statements of new and modified
        // alignments; Use splited FASTA results
        
        // Step 6: blastp ensembl genes against pdb; Use splited FASTA results

        if (this.seqFileCount != -1) {
            for (int i = 0; i < this.seqFileCount; i++) {
                paralist = new ArrayList<String>();
                paralist.add(ReadConfig.workspace + ReadConfig.seqFastaFile + "." + new Integer(i).toString());
                paralist.add(currentDir + this.db.resultfileName + "." + new Integer(i).toString());
                paralist.add(currentDir + this.db.dbName);
                cu.runCommand("blastp", paralist);

                parseprocess.parse2sqlPartition(false, currentDir, this.seqFileCount, i, pdbHm);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + ReadConfig.sqlInsertFile + "." + new Integer(i).toString());
                cu.runCommand("mysql", paralist);

                paralist = new ArrayList<String>();
                paralist.add(currentDir + this.db.resultfileName + "." + new Integer(i).toString());
                cu.runCommand("rm", paralist);

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

            parseprocess.parse2sql(false, currentDir, this.seqFileCount);

            paralist = new ArrayList<String>();
            paralist.add(currentDir + ReadConfig.sqlInsertFile);
            cu.runCommand("mysql", paralist);
        }

        // Step 7: After update all the new alignments,
        // Create complete PDB sequences for de novo sequence blast
        preprocess.denovoPreprocessPDBsequencesUpdate(dateVersion, listOld, currentDir + ReadConfig.pdbSeqresFastaFile,
                currentDir + ReadConfig.pdbSeqresFastaFile);

        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.workspace + ReadConfig.pdbSeqresFastaFile);
        paralist.add(ReadConfig.workspace + this.db.dbName);
        cu.runCommand("makeblastdb", paralist);

        // Step 8: Create release tags
        // Change messages.properties in web module
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.releaseTag);
        paralist.add(currentDir + ReadConfig.releaseTagResult);
        cu.runCommand("releaseTag", paralist);

        // Use MYSQL to update
        preprocess.releasTagUpdateSQL(currentDir + ReadConfig.releaseTagResult,
                currentDir + ReadConfig.updateStatisticsSQL);
        paralist = new ArrayList<String>();
        paralist.add(currentDir + ReadConfig.updateStatisticsSQL);
        cu.runCommand("mysql", paralist);

        // Step 9: Delete and create a new rsMutation table
        log.info("********************[STEP 9]********************");
        log.info("[SQL] Total update mutation_usage_table by Drop and create data schema");
        
        //TODO: update mutation_entry
        
        paralist = new ArrayList<String>();
        paralist.add(ReadConfig.resourceDir + ReadConfig.mutationGenerateSQL);
        paralist.add(ReadConfig.workspace + ReadConfig.mutationResult);
        cu.runCommand("releaseTag", paralist);

        //regenerate everything
        
        // Step 10: Clean up
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
}

