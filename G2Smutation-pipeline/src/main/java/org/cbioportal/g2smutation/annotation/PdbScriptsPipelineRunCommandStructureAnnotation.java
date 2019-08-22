package org.cbioportal.g2smutation.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.cbioportal.g2smutation.util.CommandProcessUtil;
import org.cbioportal.g2smutation.util.FileOperatingUtil;
import org.cbioportal.g2smutation.util.ReadConfig;
import org.cbioportal.g2smutation.util.models.MutationUsageRecord;
import org.cbioportal.g2smutation.util.models.StructureAnnotationRecord;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Run tedious structure annotation Part of copying from main function in
 * generateAnnotation
 * 
 * @author juexin wang
 *
 */
public class PdbScriptsPipelineRunCommandStructureAnnotation {
	final static Logger log = Logger.getLogger(PdbScriptsPipelineRunCommandStructureAnnotation.class);

	/**
	 * Used for update structure information, it is so huge,so we have to run
	 * seperately
	 */
	public void runStructureAnnotation() {
		StructureAnnotation sanno = new StructureAnnotation();
		ArrayList<String> paralist = new ArrayList<String>();
		CommandProcessUtil cu = new CommandProcessUtil();
		FileOperatingUtil fou = new FileOperatingUtil();
		// String currentDir = ReadConfig.workspace + "20190606/"; // Hardcode now
		String currentDir = ReadConfig.workspace;

		log.info("[File] Read results from file, generate HashMap for usage");
		MutationUsageRecord mUsageRecord = new MutationUsageRecord();
		HashMap<String, String> mutationHm = new HashMap<>();
		
        DB structureAnnoHmdb = DBMaker.fileDB(ReadConfig.workspace+ReadConfig.structureAnnoHmFile).checksumHeaderBypass().make();        
        Map structureAnnoHm = structureAnnoHmdb.hashMap("map").createOrOpen();

		String filename = ReadConfig.workspace + ReadConfig.mutationHmFile;
		// Deserialize
		try {
			log.info("Deserialize " + filename);
			mutationHm = (HashMap<String, String>) SerializationUtils
					.deserialize(FileUtils.readFileToByteArray(new File(filename)));
			log.info("Size of " + mutationHm.size());
			log.info("Serialization completed");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// <mutation_NO, gpos>: saving time for API calling
		mUsageRecord = fou.readMutationResult2MutationUsageRecord(currentDir + ReadConfig.mutationResult, mutationHm);

		// usually the flag is set as false in init, true in update
		sanno.generateNaccessResults(mUsageRecord, new HashSet<>(), false);
		log.info("[STRUCTURE] Start processing naccess rsa results from scratch");
		sanno.generateNaccessResultsBuried(mUsageRecord, new HashSet<>(), false);

		/*
		// Deserialize
		try {
			log.info("Deserialize " + filename);
			filename = ReadConfig.workspace + ReadConfig.structureAnnoHmFile;
			structureAnnoHm = (HashMap<String, StructureAnnotationRecord>) SerializationUtils
					.deserialize(FileUtils.readFileToByteArray(new File(filename)));
			log.info("Size of " + structureAnnoHm.size());
			log.info("Serialization completed");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		*/

		log.info("[STRUCTURE] naccess complete and start parsing from scratch");
		sanno.parseGenerateMutationResultSQL4StructureAnnotationEntry(mUsageRecord,
				currentDir + ReadConfig.mutationInjectSQLStructure, structureAnnoHm, true);
		
		structureAnnoHmdb.close();

		// Careful
		// log.info("[STRUCTURE] Dump mutation_inject_structure.sql to
		// structure_annotation_entry");
		// //rebuild table structure_annotation_entry
		// paralist = new ArrayList<String>();
		// paralist.add(ReadConfig.resourceDir +
		// ReadConfig.annotationStrctureSQL);
		// cu.runCommand("mysql", paralist);
		//
		// paralist = new ArrayList<String>();
		// paralist.add(ReadConfig.workspace +
		// ReadConfig.mutationInjectSQLStructure);
		// cu.runCommand("mysql", paralist);

	}

}
