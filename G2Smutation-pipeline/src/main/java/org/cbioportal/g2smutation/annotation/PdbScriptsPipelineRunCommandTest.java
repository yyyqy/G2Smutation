package org.cbioportal.g2smutation.annotation;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.cbioportal.g2smutation.scripts.PdbScriptsPipelineMakeSQL;
import org.cbioportal.g2smutation.scripts.PdbScriptsPipelinePreprocessing;
import org.cbioportal.g2smutation.scripts.PdbScriptsPipelineRunCommand;
import org.cbioportal.g2smutation.util.CommandProcessUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.blast.BlastDataBase;

public class PdbScriptsPipelineRunCommandTest {
	
	final static Logger log = Logger.getLogger(PdbScriptsPipelineRunCommandTest.class);
	
	public void generateMutation(){
		/*
		 * For test
		PdbScriptsPipelineRunCommand app = new PdbScriptsPipelineRunCommand();
		PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(app);
        PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
        app.setCurrentDir(ReadConfig.workspace);
    	app.setDb(new BlastDataBase(ReadConfig.pdbSeqresFastaFile)); 
    	app.setSeqFileCount(10);
    	parseprocess.setDb(app.getDb());
    	ArrayList<String> paralist = new ArrayList<String>();
        CommandProcessUtil cu = new CommandProcessUtil();
        //this.seqFileCount =10;
        
    	// Step 2.1:
        log.info("********************[STEP 2.1]********************");
        log.info("[PrepareSQL] Parse xml results and output as input sql statments for mutation");
        parseprocess.parse2sqlMutation(false, ReadConfig.workspace, app.getSeqFileCount());
        */
		PdbScriptsPipelinePreprocessing preprocess = new PdbScriptsPipelinePreprocessing();
		PdbScriptsPipelineRunCommand app = new PdbScriptsPipelineRunCommand();
		//app.generateWeeklyTag(preprocess);
		
		
		PdbScriptsPipelineMakeSQL parseprocess = new PdbScriptsPipelineMakeSQL(app);
		parseprocess.parseGenerateMutationResultSQL4MutatationUsageTable(ReadConfig.workspace + ReadConfig.mutationResult, ReadConfig.workspace + ReadConfig.mutationInjectSQLUsage); 
	}

}
