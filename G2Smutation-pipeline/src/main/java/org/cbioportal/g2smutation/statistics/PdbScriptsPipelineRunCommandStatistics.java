package org.cbioportal.g2smutation.statistics;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.cbioportal.g2smutation.scripts.PdbScriptsPipelineMakeSQL;
import org.cbioportal.g2smutation.scripts.PdbScriptsPipelineRunCommand;
import org.cbioportal.g2smutation.util.CommandProcessUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.blast.BlastDataBase;

/**
 * 
 * Statistics here, used for analysis of the system
 * 
 * @author Juexin Wang
 *
 */
public class PdbScriptsPipelineRunCommandStatistics extends PdbScriptsPipelineRunCommand {
	
    final static Logger log = Logger.getLogger(PdbScriptsPipelineRunCommandStatistics.class);
    private BlastDataBase db;
    private int matches;
    private int seqFileCount;
    private boolean updateTag;
    private int allSqlCount;
    private String dateVersion;
    private String currentDir;
    PdbScriptsPipelineRunCommand app = new PdbScriptsPipelineRunCommand();
	
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
            PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(app, testcount);
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
