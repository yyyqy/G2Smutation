package org.cbioportal.G2Smutation.util;

import java.util.*;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineStarter;

/**
 *
 * Read application.properties by singleton design pattern.
 * 
 * All the parameters should be dealt here
 *
 * @author Juexin Wang
 *
 */
public class ReadConfig {
    static final Logger log = Logger.getLogger(ReadConfig.class);

    private static ReadConfig rcObj;

    public static String makeblastdb;
    public static String blastp;
    public static String workspace;
    public static String resourceDir;
    public static String tmpdir;
    public static String initChoice;
    public static String initTrembl;
    public static String initConcurrent;
    public static String pdbRepo;
    public static String pdbSegMinLengthMulti;
    public static String pdbSegMinLengthSingle;
    public static String pdbSegGapThreshold;
    public static String pdbSeqresDownloadFile;
    public static String pdbSeqresFastaFile;
    public static String ensemblDownloadFile;
    public static String swissprotDownloadFile;
    public static String tremblDownloadFile;
    public static String isoformDownloadFile;
    public static String seqFastaFile;
    public static String sqlInsertFile;
    public static String sqlDeleteFile;
    public static String rsSqlInsertFile;
    public static String gposSqlInsertFile;
    public static String gposAlignSqlInsertFile;
    public static String blastParaEvalue;
    public static String blastParaMaxTargetSeqs;
    public static String blastParaWordSize;
    public static String blastParaThreads;
    public static String ensemblInputInterval;
    public static String sqlInsertOutputInterval;
    public static String mysql;
    public static String username;
    public static String password;
    public static String dbHost;
    public static String dbName;
    public static String dbNameScript;
    public static String releaseTag;
    public static String releaseTagResult;
    public static String updateStatisticsSQL;
    public static String updateAllSnpSql;
    public static String pdbWholeSource;
    public static String ensemblWholeSource;
    public static String swissprotWholeSource;
    public static String tremblWholeSource;
    public static String isoformWholeSource;
    public static String clinvarWholeSource;
    public static String updateTxt;
    public static String updateFasta;
    public static String delPDB;
    public static String updateDAY;
    public static String updateHOUR;
    public static String updateMINUTE;
    public static String updateSECOND;
    public static String updateMILLISECOND;
    public static String updateDELAY;
    public static String updateSeqFastaFileNum;
    public static String updateRsSqlFileNum;
    public static String insertSequenceSQL;
    public static String updateAdded;
    public static String updateModified;
    public static String updateObsolete;
    public static String pdbFastaService;
    public static String mysqlMaxAllowedPacket;
    public static String mutationGenerateSQL;
    public static String mutationResult;
    public static String mutationInjectSQLUsage;
    public static String mutationInjectSQLLocation;
    public static String mutationInjectSQLStructure;
    public static String mutationInjectSQLDbsnp;
    public static String mutationInjectSQLClinvar;
    public static String mutationInjectSQLCosmic;
    public static String mutationInjectSQLGenie;
    public static String mutationInjectSQLTcga;
    public static String annotationDbsnpSQL;
    public static String annotationClinvarSQL;
    public static String annotationCosmicSQL;
    public static String annotationGenieSQL;
    public static String annotationTcgaSQL;
    public static String alignFilterDiffT;
    public static String alignFilterDiffP;
    public static String alignFilterRatio;
    public static String alignFilterStatsSQL;
    public static String alignFilterStatsResult;
    public static String protein2GenomicURL;
    public static String gnApiGnUrl;
    public static String gnApiGnPostUrl;
    public static String callPostSize;
    public static String dbsnpFile;
    public static String clinvarFile;
    public static String cosmicFile;
    public static String genieFile;
    public static String tcgaFile;
    public static String gnApiDbsnpInnerUrl;
    public static String gnApiDbsnpInnerGposUrl;
    public static String gnApiAlignInnerGposUrl;
    public static String callThreadsNum;
    public static String saveSpaceTag;
    public static String naccessInstallFile;
    public static String naccessRunFile;
    public static String relativeRatio;
    public static String dsspLocalDataFile;
    public static String insertStructureAnnotationSQL;
    public static String naccessFileSuffix;
    public static String dsspFileSuffix;
    public static String sequenceDomainsUrl;
    public static String structureDomainsUrl;

    public static boolean isPositiveInteger(String str) {
        return str.matches("\\d+"); // match a number with positive integer.
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
        // '-' and decimal.
    }

    public static boolean isFolder(String str) {
        return str.matches("/.+/"); // match a folder start with / and end with
        // /
    }

    // TODO:
    // Check value of application.properties
    public void checkValue(String inStr) {

    }

    private ReadConfig() {

        try {
            Properties prop = new Properties();
            prop.load(PdbScriptsPipelineStarter.class.getClassLoader().getResourceAsStream("application.properties"));

            // Set all constants
            ReadConfig.makeblastdb = prop.getProperty("makeblastdb").trim();
            ReadConfig.blastp = prop.getProperty("blastp").trim();
            ReadConfig.workspace = prop.getProperty("workspace").trim();
            ReadConfig.resourceDir = prop.getProperty("resource_dir").trim();
            ReadConfig.tmpdir = prop.getProperty("tmpdir").trim();
            ReadConfig.initChoice = prop.getProperty("init.choice").trim();
            ReadConfig.initTrembl = prop.getProperty("init.trembl").trim();
            ReadConfig.initConcurrent = prop.getProperty("init.concurrent").trim();
            ReadConfig.pdbRepo = prop.getProperty("pdbRepo").trim();
            ReadConfig.pdbSegMinLengthMulti = prop.getProperty("pdb.seg.minLength.multi").trim();
            ReadConfig.pdbSegMinLengthSingle = prop.getProperty("pdb.seg.minLength.single").trim();
            ReadConfig.pdbSegGapThreshold = prop.getProperty("pdb.seg.gapThreshold").trim();
            ReadConfig.pdbSeqresDownloadFile = prop.getProperty("pdb_seqres_download_file").trim();
            ReadConfig.pdbSeqresFastaFile = prop.getProperty("pdb_seqres_fasta_file").trim();
            ReadConfig.ensemblDownloadFile = prop.getProperty("ensembl_download_file").trim();
            ReadConfig.swissprotDownloadFile = prop.getProperty("swissprot_download_file").trim();
            ReadConfig.tremblDownloadFile = prop.getProperty("trembl_download_file").trim();
            ReadConfig.isoformDownloadFile = prop.getProperty("isoform_download_file").trim();
            ReadConfig.seqFastaFile = prop.getProperty("seq_fasta_file").trim();
            ReadConfig.sqlInsertFile = prop.getProperty("sql_insert_file").trim();
            ReadConfig.sqlDeleteFile = prop.getProperty("sql_delete_file").trim();
            ReadConfig.rsSqlInsertFile = prop.getProperty("rs_sql_insert_file").trim();
            ReadConfig.gposSqlInsertFile = prop.getProperty("gpos_sql_insert_file").trim();
            ReadConfig.gposAlignSqlInsertFile = prop.getProperty("gpos_align_sql_insert_file").trim();
            ReadConfig.blastParaEvalue = prop.getProperty("blast_para_evalue").trim();
            ReadConfig.blastParaMaxTargetSeqs = prop.getProperty("blast_para_max_target_seqs").trim();
            ReadConfig.blastParaWordSize = prop.getProperty("blast_para_word_size").trim();
            ReadConfig.blastParaThreads = prop.getProperty("blast_para_threads").trim();
            ReadConfig.ensemblInputInterval = prop.getProperty("ensembl_input_interval").trim();
            ReadConfig.sqlInsertOutputInterval = prop.getProperty("sql_insert_output_interval").trim();
            ReadConfig.mysql = prop.getProperty("mysql").trim();
            ReadConfig.username = prop.getProperty("username").trim();
            ReadConfig.password = prop.getProperty("password").trim();
            ReadConfig.dbHost = prop.getProperty("db_host").trim();
            ReadConfig.dbName = prop.getProperty("db_name").trim();
            ReadConfig.dbNameScript = prop.getProperty("db_name_script").trim();
            ReadConfig.releaseTag = prop.getProperty("update.releaseTag_script").trim();
            ReadConfig.releaseTagResult = prop.getProperty("update.release_result").trim();
            ReadConfig.updateAllSnpSql = prop.getProperty("update.allSnpMutation.sql").trim();
            ReadConfig.updateStatisticsSQL = prop.getProperty("update.statistics.sql").trim();
            ReadConfig.pdbWholeSource = prop.getProperty("pdb.wholeSource").trim();
            ReadConfig.ensemblWholeSource = prop.getProperty("ensembl.wholeSource").trim();
            ReadConfig.swissprotWholeSource = prop.getProperty("swissprot.wholeSource").trim();
            ReadConfig.tremblWholeSource = prop.getProperty("trembl.wholeSource").trim();
            ReadConfig.isoformWholeSource = prop.getProperty("isoform.wholeSource").trim();
            ReadConfig.clinvarWholeSource = prop.getProperty("clinvar.wholeSource").trim();
            ReadConfig.updateTxt = prop.getProperty("update.updateTxt").trim();
            ReadConfig.updateFasta = prop.getProperty("update.updateFasta").trim();
            ReadConfig.delPDB = prop.getProperty("update.delPDB").trim();
            ReadConfig.updateDAY = prop.getProperty("update.DAY_OF_WEEK").trim();
            ReadConfig.updateHOUR = prop.getProperty("update.HOUR_OF_DAY").trim();
            ReadConfig.updateMINUTE = prop.getProperty("update.MINUTE").trim();
            ReadConfig.updateSECOND = prop.getProperty("update.SECOND").trim();
            ReadConfig.updateMILLISECOND = prop.getProperty("update.MILLISECOND").trim();
            ReadConfig.updateDELAY = prop.getProperty("update.DELAY").trim();
            ReadConfig.updateSeqFastaFileNum = prop.getProperty("update.seq_fasta_file_num").trim();
            ReadConfig.updateRsSqlFileNum = prop.getProperty("update.rs_sql_file_num").trim();
            ReadConfig.insertSequenceSQL = prop.getProperty("insert_sequence_SQL").trim();
            ReadConfig.updateAdded = prop.getProperty("update.added").trim();
            ReadConfig.updateModified = prop.getProperty("update.modified").trim();
            ReadConfig.updateObsolete = prop.getProperty("update.obsolete").trim();
            ReadConfig.pdbFastaService = prop.getProperty("pdb.fastaService").trim();
            ReadConfig.mysqlMaxAllowedPacket = prop.getProperty("mysql_max_allowed_packet").trim();
            ReadConfig.mutationGenerateSQL = prop.getProperty("mutation_generate_SQL").trim();
            ReadConfig.mutationResult = prop.getProperty("mutation_result").trim();
            ReadConfig.mutationInjectSQLUsage = prop.getProperty("mutation_inject_SQL_mutation_usage_table").trim();
            ReadConfig.mutationInjectSQLLocation = prop.getProperty("mutation_inject_SQL_mutation_location_entry")
                    .trim();
            ReadConfig.mutationInjectSQLStructure = prop.getProperty("mutation_inject_SQL_structure_annotation_entry")
                    .trim();
            ReadConfig.mutationInjectSQLDbsnp = prop.getProperty("mutation_inject_SQL_dbsnp_entry").trim();
            ReadConfig.mutationInjectSQLClinvar = prop.getProperty("mutation_inject_SQL_clinvar_entry").trim();
            ReadConfig.mutationInjectSQLCosmic = prop.getProperty("mutation_inject_SQL_cosmic_entry").trim();
            ReadConfig.mutationInjectSQLGenie = prop.getProperty("mutation_inject_SQL_genie_entry").trim();
            ReadConfig.mutationInjectSQLTcga = prop.getProperty("mutation_inject_SQL_tcga_entry").trim();
            ReadConfig.annotationDbsnpSQL = prop.getProperty("annotation_dbsnp_SQL").trim();
            ReadConfig.annotationClinvarSQL = prop.getProperty("annotation_clinvar_SQL").trim();
            ReadConfig.annotationCosmicSQL = prop.getProperty("annotation_cosmic_SQL").trim();
            ReadConfig.annotationGenieSQL = prop.getProperty("annotation_genie_SQL").trim();
            ReadConfig.annotationTcgaSQL = prop.getProperty("annotation_tcga_SQL").trim();
            ReadConfig.alignFilterDiffT = prop.getProperty("align.filter.diffT").trim();
            ReadConfig.alignFilterDiffP = prop.getProperty("align.filter.diffP").trim();
            ReadConfig.alignFilterRatio = prop.getProperty("align.filter.ratio").trim();
            ReadConfig.alignFilterStatsSQL = prop.getProperty("align.filter.stats.sql").trim();
            ReadConfig.alignFilterStatsResult = prop.getProperty("align.filter.stats.result").trim();
            ReadConfig.protein2GenomicURL = prop.getProperty("protein2genomic.url").trim();
            ReadConfig.gnApiGnUrl = prop.getProperty("gn.api.genomenexus.url").trim();
            ReadConfig.gnApiGnPostUrl = prop.getProperty("gn.api.genomenexus.post.url").trim();
            ReadConfig.callPostSize = prop.getProperty("call.post.size").trim();
            ReadConfig.dbsnpFile = prop.getProperty("dbsnp.file").trim();
            ReadConfig.clinvarFile = prop.getProperty("clinvar.file").trim();
            ReadConfig.cosmicFile = prop.getProperty("cosmic.file").trim();
            ReadConfig.genieFile = prop.getProperty("genie.file").trim();
            ReadConfig.tcgaFile = prop.getProperty("tcga.file").trim();
            ReadConfig.gnApiDbsnpInnerUrl = prop.getProperty("gn.api.dbsnp.inner.url").trim();
            ReadConfig.gnApiDbsnpInnerGposUrl = prop.getProperty("gn.api.dbsnp.inner.gpos.url").trim();
            ReadConfig.gnApiAlignInnerGposUrl = prop.getProperty("gn.api.align.inner.gpos.url").trim();
            ReadConfig.callThreadsNum = prop.getProperty("call.threads.num").trim();
            ReadConfig.saveSpaceTag = prop.getProperty("saveSpaceTag").trim();
            ReadConfig.naccessInstallFile = prop.getProperty("naccess_install_file").trim();
            ReadConfig.naccessRunFile = prop.getProperty("naccess_run_file").trim();
            ReadConfig.relativeRatio = prop.getProperty("relative_ratio").trim();
            ReadConfig.dsspLocalDataFile = prop.getProperty("dssp_localData_file").trim();
            ReadConfig.insertStructureAnnotationSQL = prop.getProperty("insert_structureAnnotation_SQL").trim();
            ReadConfig.naccessFileSuffix = prop.getProperty("naccess_file_suffix").trim();
            ReadConfig.dsspFileSuffix = prop.getProperty("deep_file_suffix").trim();
            ReadConfig.sequenceDomainsUrl = prop.getProperty("sequence.domains.url").trim();
            ReadConfig.structureDomainsUrl = prop.getProperty("structure.domains.url").trim();
        } catch (Exception ex) {
            log.error("[CONFIG] Error in Reading application.properties");
            ex.printStackTrace();
        }
    }

    /**
     * Get Methods
     */

    public static ReadConfig getInstance() {
        if (rcObj == null) {
            rcObj = new ReadConfig();
        }
        return rcObj;
    }
    
    

    public static String getInitChoice() {
		return initChoice;
	}

	public static void setInitChoice(String initChoice) {
		ReadConfig.initChoice = initChoice;
	}

	public static String getInitTrembl() {
		return initTrembl;
	}

	public static void setInitTrembl(String initTrembl) {
		ReadConfig.initTrembl = initTrembl;
	}

	public static String getInitConcurrent() {
		return initConcurrent;
	}

	public static void setInitConcurrent(String initConcurrent) {
		ReadConfig.initConcurrent = initConcurrent;
	}

	public static String getGnApiDbsnpInnerGposUrl() {
        return gnApiDbsnpInnerGposUrl;
    }

    public static void setGnApiDbsnpInnerGposUrl(String gnApiDbsnpInnerGposUrl) {
        ReadConfig.gnApiDbsnpInnerGposUrl = gnApiDbsnpInnerGposUrl;
    }

    public static String getClinvarWholeSource() {
        return clinvarWholeSource;
    }

    public static void setClinvarWholeSource(String clinvarWholeSource) {
        ReadConfig.clinvarWholeSource = clinvarWholeSource;
    }

    public static String getClinvarFile() {
        return clinvarFile;
    }

    public static void setClinvarFile(String clinvarFile) {
        ReadConfig.clinvarFile = clinvarFile;
    }

    public static String getCosmicFile() {
        return cosmicFile;
    }

    public static void setCosmicFile(String cosmicFile) {
        ReadConfig.cosmicFile = cosmicFile;
    }

    public static String getGenieFile() {
        return genieFile;
    }

    public static void setGenieFile(String genieFile) {
        ReadConfig.genieFile = genieFile;
    }

    public static String getTcgaFile() {
        return tcgaFile;
    }

    public static void setTcgaFile(String tcgaFile) {
        ReadConfig.tcgaFile = tcgaFile;
    }

    public static String getMutationInjectSQLStructure() {
        return mutationInjectSQLStructure;
    }

    public static void setMutationInjectSQLStructure(String mutationInjectSQLStructure) {
        ReadConfig.mutationInjectSQLStructure = mutationInjectSQLStructure;
    }

    public static String getMutationInjectSQLDbsnp() {
        return mutationInjectSQLDbsnp;
    }

    public static void setMutationInjectSQLDbsnp(String mutationInjectSQLDbsnp) {
        ReadConfig.mutationInjectSQLDbsnp = mutationInjectSQLDbsnp;
    }

    public static String getMutationInjectSQLClinvar() {
        return mutationInjectSQLClinvar;
    }

    public static void setMutationInjectSQLClinvar(String mutationInjectSQLClinvar) {
        ReadConfig.mutationInjectSQLClinvar = mutationInjectSQLClinvar;
    }

    public static String getMutationInjectSQLCosmic() {
        return mutationInjectSQLCosmic;
    }

    public static void setMutationInjectSQLCosmic(String mutationInjectSQLCosmic) {
        ReadConfig.mutationInjectSQLCosmic = mutationInjectSQLCosmic;
    }

    public static String getMutationInjectSQLGenie() {
        return mutationInjectSQLGenie;
    }

    public static void setMutationInjectSQLGenie(String mutationInjectSQLGenie) {
        ReadConfig.mutationInjectSQLGenie = mutationInjectSQLGenie;
    }

    public static String getMutationInjectSQLTcga() {
        return mutationInjectSQLTcga;
    }

    public static void setMutationInjectSQLTcga(String mutationInjectSQLTcga) {
        ReadConfig.mutationInjectSQLTcga = mutationInjectSQLTcga;
    }

    public static String getAnnotationDbsnpSQL() {
        return annotationDbsnpSQL;
    }

    public static void setAnnotationDbsnpSQL(String annotationDbsnpSQL) {
        ReadConfig.annotationDbsnpSQL = annotationDbsnpSQL;
    }

    public static String getMutationInjectSQLUsage() {
        return mutationInjectSQLUsage;
    }

    public static void setMutationInjectSQLUsage(String mutationInjectSQLUsage) {
        ReadConfig.mutationInjectSQLUsage = mutationInjectSQLUsage;
    }

    public static String getMutationInjectSQLLocation() {
        return mutationInjectSQLLocation;
    }

    public static void setMutationInjectSQLLocation(String mutationInjectSQLLocation) {
        ReadConfig.mutationInjectSQLLocation = mutationInjectSQLLocation;
    }

    public static String getProtein2GenomicURL() {
        return protein2GenomicURL;
    }

    public static void setProtein2GenomicURL(String protein2GenomicURL) {
        ReadConfig.protein2GenomicURL = protein2GenomicURL;
    }

    public static String getAnnotationClinvarSQL() {
        return annotationClinvarSQL;
    }

    public static void setAnnotationClinvarSQL(String annotationClinvarSQL) {
        ReadConfig.annotationClinvarSQL = annotationClinvarSQL;
    }

    public static String getAnnotationCosmicSQL() {
        return annotationCosmicSQL;
    }

    public static void setAnnotationCosmicSQL(String annotationCosmicSQL) {
        ReadConfig.annotationCosmicSQL = annotationCosmicSQL;
    }

    public static String getAnnotationGenieSQL() {
        return annotationGenieSQL;
    }

    public static void setAnnotationGenieSQL(String annotationGenieSQL) {
        ReadConfig.annotationGenieSQL = annotationGenieSQL;
    }

    public static String getAnnotationTcgaSQL() {
        return annotationTcgaSQL;
    }

    public static void setAnnotationTcgaSQL(String annotationTcgaSQL) {
        ReadConfig.annotationTcgaSQL = annotationTcgaSQL;
    }

    public static String getGnApiDbsnpInnerUrl() {
        return gnApiDbsnpInnerUrl;
    }

    public static void setGnApiDbsnpInnerUrl(String gnApiDbsnpInnerUrl) {
        ReadConfig.gnApiDbsnpInnerUrl = gnApiDbsnpInnerUrl;
    }

    public static String getDbsnpFile() {
        return dbsnpFile;
    }

    public static void setDbsnpFile(String dbsnpFile) {
        ReadConfig.dbsnpFile = dbsnpFile;
    }

    public static String getUpdateWebProperties() {
        return updateStatisticsSQL;
    }

    public static void setUpdateWebProperties(String updateWebProperties) {
        ReadConfig.updateStatisticsSQL = updateWebProperties;
    }

    public static String getReleaseTagResult() {
        return releaseTagResult;
    }

    public static void setReleaseTagResult(String releaseTagResult) {
        ReadConfig.releaseTagResult = releaseTagResult;
    }

    public static String getReleaseTag() {
        return releaseTag;
    }

    public static void setReleaseTag(String releaseTag) {
        ReadConfig.releaseTag = releaseTag;
    }

    public static String getUpdateSeqFastaFileNum() {
        return updateSeqFastaFileNum;
    }

    public static void setUpdateSeqFastaFileNum(String updateSeqFastaFileNum) {
        ReadConfig.updateSeqFastaFileNum = updateSeqFastaFileNum;
    }

    public static String getBlastParaMaxTargetSeqs() {
        return blastParaMaxTargetSeqs;
    }

    public static void setBlastParaMaxTargetSeqs(String blastParaMaxTargetSeqs) {
        ReadConfig.blastParaMaxTargetSeqs = blastParaMaxTargetSeqs;
    }

    public static ReadConfig getRcObj() {
        return rcObj;
    }

    public static String getSwissprotDownloadFile() {
        return swissprotDownloadFile;
    }

    public static String getTremblDownloadFile() {
        return tremblDownloadFile;
    }

    public static String getIsoformDownloadFile() {
        return isoformDownloadFile;
    }

    public static String getSeqFastaFile() {
        return seqFastaFile;
    }

    public static String getSwissprotWholeSource() {
        return swissprotWholeSource;
    }

    public static String getTremblWholeSource() {
        return tremblWholeSource;
    }

    public static String getIsoformWholeSource() {
        return isoformWholeSource;
    }

    public static String getMakeblastdb() {
        return makeblastdb;
    }

    public static String getBlastp() {
        return blastp;
    }

    public static String getWorkspace() {
        return workspace;
    }

    public static String getResourceDir() {
        return resourceDir;
    }

    public static String getTmpdir() {
        return tmpdir;
    }

    public static String getPdbRepo() {
        return pdbRepo;
    }

    public static String getPdbSegMinLengthMulti() {
        return pdbSegMinLengthMulti;
    }

    public static void setPdbSegMinLengthMulti(String pdbSegMinLengthMulti) {
        ReadConfig.pdbSegMinLengthMulti = pdbSegMinLengthMulti;
    }

    public static String getPdbSegMinLengthSingle() {
        return pdbSegMinLengthSingle;
    }

    public static void setPdbSegMinLengthSingle(String pdbSegMinLengthSingle) {
        ReadConfig.pdbSegMinLengthSingle = pdbSegMinLengthSingle;
    }

    public static String getPdbSegGapThreshold() {
        return pdbSegGapThreshold;
    }

    public static void setPdbSegGapThreshold(String pdbSegGapThreshold) {
        ReadConfig.pdbSegGapThreshold = pdbSegGapThreshold;
    }

    public static String getPdbSeqresDownloadFile() {
        return pdbSeqresDownloadFile;
    }

    public static String getPdbSeqresFastaFile() {
        return pdbSeqresFastaFile;
    }

    public static String getEnsemblDownloadFile() {
        return ensemblDownloadFile;
    }

    public static String getSqlInsertFile() {
        return sqlInsertFile;
    }

    public static String getSqlDeleteFile() {
        return sqlDeleteFile;
    }

    public static String getRsSqlInsertFile() {
        return rsSqlInsertFile;
    }

    public static String getBlastParaEvalue() {
        return blastParaEvalue;
    }

    public static String getBlastParaWordSize() {
        return blastParaWordSize;
    }

    public static String getBlastParaThreads() {
        return blastParaThreads;
    }

    public static String getEnsemblInputInterval() {
        return ensemblInputInterval;
    }

    public static String getSqlInsertOutputInterval() {
        return sqlInsertOutputInterval;
    }

    public static String getMysql() {
        return mysql;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getDbNameScript() {
        return dbNameScript;
    }

    public static String getPdbWholeSource() {
        return pdbWholeSource;
    }

    public static String getEnsemblWholeSource() {
        return ensemblWholeSource;
    }

    public static String getUpdateTxt() {
        return updateTxt;
    }

    public static String getUpdateFasta() {
        return updateFasta;
    }

    public static String getDelPDB() {
        return delPDB;
    }

    public static String getUpdateDAY() {
        return updateDAY;
    }

    public static String getUpdateHOUR() {
        return updateHOUR;
    }

    public static String getUpdateMINUTE() {
        return updateMINUTE;
    }

    public static String getUpdateSECOND() {
        return updateSECOND;
    }

    public static String getUpdateMILLISECOND() {
        return updateMILLISECOND;
    }

    public static String getUpdateDELAY() {
        return updateDELAY;
    }

    public static String getInsertSequenceSQL() {
        return insertSequenceSQL;
    }

    public static String getUpdateAdded() {
        return updateAdded;
    }

    public static String getUpdateModified() {
        return updateModified;
    }

    public static String getUpdateObsolete() {
        return updateObsolete;
    }

    public static String getPdbFastaService() {
        return pdbFastaService;
    }

    public static String getMysqlMaxAllowedPacket() {
        return mysqlMaxAllowedPacket;
    }

    public static String getSaveSpaceTag() {
        return saveSpaceTag;
    }

    /**
     * Set Methods
     */
    public static void setRcObj(ReadConfig rcObj) {
        ReadConfig.rcObj = rcObj;
    }

    public static void setSwissprotDownloadFile(String swissprotDownloadFile) {
        ReadConfig.swissprotDownloadFile = swissprotDownloadFile;
    }

    public static void setTremblDownloadFile(String tremblDownloadFile) {
        ReadConfig.tremblDownloadFile = tremblDownloadFile;
    }

    public static void setIsoformDownloadFile(String isoformDownloadFile) {
        ReadConfig.isoformDownloadFile = isoformDownloadFile;
    }

    public static void setSeqFastaFile(String seqFastaFile) {
        ReadConfig.seqFastaFile = seqFastaFile;
    }

    public static void setSwissprotWholeSource(String swissprotWholeSource) {
        ReadConfig.swissprotWholeSource = swissprotWholeSource;
    }

    public static void setTremblWholeSource(String tremblWholeSource) {
        ReadConfig.tremblWholeSource = tremblWholeSource;
    }

    public static void setIsoformWholeSource(String isoformWholeSource) {
        ReadConfig.isoformWholeSource = isoformWholeSource;
    }

    public static void setMakeblastdb(String makeblastdb) {
        ReadConfig.makeblastdb = makeblastdb;
    }

    public static void setBlastp(String blastp) {
        ReadConfig.blastp = blastp;
    }

    public static void setWorkspace(String workspace) {
        ReadConfig.workspace = workspace;
    }

    public static void setResourceDir(String resourceDir) {
        ReadConfig.resourceDir = resourceDir;
    }

    public static void setTmpdir(String tmpdir) {
        ReadConfig.tmpdir = tmpdir;
    }

    public static void setPdbRepo(String pdbRepo) {
        ReadConfig.pdbRepo = pdbRepo;
    }

    public static void setPdbSeqresDownloadFile(String pdbSeqresDownloadFile) {
        ReadConfig.pdbSeqresDownloadFile = pdbSeqresDownloadFile;
    }

    public static void setPdbSeqresFastaFile(String pdbSeqresFastaFile) {
        ReadConfig.pdbSeqresFastaFile = pdbSeqresFastaFile;
    }

    public static void setEnsemblDownloadFile(String ensemblDownloadFile) {
        ReadConfig.ensemblDownloadFile = ensemblDownloadFile;
    }

    public static void setSqlInsertFile(String sqlInsertFile) {
        ReadConfig.sqlInsertFile = sqlInsertFile;
    }

    public static void setSqlDeleteFile(String sqlDeleteFile) {
        ReadConfig.sqlDeleteFile = sqlDeleteFile;
    }

    public static void setRsSqlInsertFile(String rsSqlInsertFile) {
        ReadConfig.rsSqlInsertFile = rsSqlInsertFile;
    }

    public static void setBlastParaEvalue(String blastParaEvalue) {
        ReadConfig.blastParaEvalue = blastParaEvalue;
    }

    public static void setBlastParaWordSize(String blastParaWordSize) {
        ReadConfig.blastParaWordSize = blastParaWordSize;
    }

    public static void setBlastParaThreads(String blastParaThreads) {
        ReadConfig.blastParaThreads = blastParaThreads;
    }

    public static void setEnsemblInputInterval(String ensemblInputInterval) {
        ReadConfig.ensemblInputInterval = ensemblInputInterval;
    }

    public static void setSqlInsertOutputInterval(String sqlInsertOutputInterval) {
        ReadConfig.sqlInsertOutputInterval = sqlInsertOutputInterval;
    }

    public static void setMysql(String mysql) {
        ReadConfig.mysql = mysql;
    }

    public static void setUsername(String username) {
        ReadConfig.username = username;
    }

    public static void setPassword(String password) {
        ReadConfig.password = password;
    }

    public static void setDbName(String dbName) {
        ReadConfig.dbName = dbName;
    }

    public static void setDbNameScript(String dbNameScript) {
        ReadConfig.dbNameScript = dbNameScript;
    }

    public static void setPdbWholeSource(String pdbWholeSource) {
        ReadConfig.pdbWholeSource = pdbWholeSource;
    }

    public static void setEnsemblWholeSource(String ensemblWholeSource) {
        ReadConfig.ensemblWholeSource = ensemblWholeSource;
    }

    public static void setUpdateTxt(String updateTxt) {
        ReadConfig.updateTxt = updateTxt;
    }

    public static void setUpdateFasta(String updateFasta) {
        ReadConfig.updateFasta = updateFasta;
    }

    public static void setDelPDB(String delPDB) {
        ReadConfig.delPDB = delPDB;
    }

    public static void setUpdateDAY(String updateDAY) {
        ReadConfig.updateDAY = updateDAY;
    }

    public static void setUpdateHOUR(String updateHOUR) {
        ReadConfig.updateHOUR = updateHOUR;
    }

    public static void setUpdateMINUTE(String updateMINUTE) {
        ReadConfig.updateMINUTE = updateMINUTE;
    }

    public static void setUpdateSECOND(String updateSECOND) {
        ReadConfig.updateSECOND = updateSECOND;
    }

    public static void setUpdateMILLISECOND(String updateMILLISECOND) {
        ReadConfig.updateMILLISECOND = updateMILLISECOND;
    }

    public static void setUpdateDELAY(String updateDELAY) {
        ReadConfig.updateDELAY = updateDELAY;
    }

    public static void setInsertSequenceSQL(String insertSequenceSQL) {
        ReadConfig.insertSequenceSQL = insertSequenceSQL;
    }

    public static void setUpdateAdded(String updateAdded) {
        ReadConfig.updateAdded = updateAdded;
    }

    public static void setUpdateModified(String updateModified) {
        ReadConfig.updateModified = updateModified;
    }

    public static void setUpdateObsolete(String updateObsolete) {
        ReadConfig.updateObsolete = updateObsolete;
    }

    public static void setPdbFastaService(String pdbFastaService) {
        ReadConfig.pdbFastaService = pdbFastaService;
    }

    public static void setMysqlMaxAllowedPacket(String mysqlMaxAllowedPacket) {
        ReadConfig.mysqlMaxAllowedPacket = mysqlMaxAllowedPacket;
    }

    public static void setSaveSpaceTag(String saveSpaceTag) {
        ReadConfig.saveSpaceTag = saveSpaceTag;
    }

    public static String getDbHost() {
        return dbHost;
    }

    public static void setDbHost(String dbHost) {
        ReadConfig.dbHost = dbHost;
    }

    public static String getAlignFilterDiffT() {
        return alignFilterDiffT;
    }

    public static void setAlignFilterDiffT(String alignFilterDiffT) {
        ReadConfig.alignFilterDiffT = alignFilterDiffT;
    }

    public static String getAlignFilterDiffP() {
        return alignFilterDiffP;
    }

    public static void setAlignFilterDiffP(String alignFilterDiffP) {
        ReadConfig.alignFilterDiffP = alignFilterDiffP;
    }

    public static String getAlignFilterRatio() {
        return alignFilterRatio;
    }

    public static void setAlignFilterRatio(String alignFilterRatio) {
        ReadConfig.alignFilterRatio = alignFilterRatio;
    }

    public static String getAlignFilterStatsSQL() {
        return alignFilterStatsSQL;
    }

    public static void setAlignFilterStatsSQL(String alignFilterStatsSQL) {
        ReadConfig.alignFilterStatsSQL = alignFilterStatsSQL;
    }

    public static String getAlignFilterStatsResult() {
        return alignFilterStatsResult;
    }

    public static void setAlignFilterStatsResult(String alignFilterStatsResult) {
        ReadConfig.alignFilterStatsResult = alignFilterStatsResult;
    }

    public static String getMutationGenerateSQL() {
        return mutationGenerateSQL;
    }

    public static void setMutationGenerateSQL(String mutationGenerateSQL) {
        ReadConfig.mutationGenerateSQL = mutationGenerateSQL;
    }

    public static String getMutationResult() {
        return mutationResult;
    }

    public static void setMutationResult(String mutationResult) {
        ReadConfig.mutationResult = mutationResult;
    }

    public static String getUpdateAllSnpSql() {
        return updateAllSnpSql;
    }

    public static void setUpdateAllSnpSql(String updateAllSnpSql) {
        ReadConfig.updateAllSnpSql = updateAllSnpSql;
    }

    public static String getUpdateRsSqlFileNum() {
        return updateRsSqlFileNum;
    }

    public static void setUpdateRsSqlFileNum(String updateRsSqlFileNum) {
        ReadConfig.updateRsSqlFileNum = updateRsSqlFileNum;
    }

	public static String getGnApiGnUrl() {
		return gnApiGnUrl;
	}

	public static void setGnApiGnUrl(String gnApiGnUrl) {
		ReadConfig.gnApiGnUrl = gnApiGnUrl;
	}

	public static String getGposSqlInsertFile() {
		return gposSqlInsertFile;
	}

	public static void setGposSqlInsertFile(String gposSqlInsertFile) {
		ReadConfig.gposSqlInsertFile = gposSqlInsertFile;
	}

	public static String getCallThreadsNum() {
		return callThreadsNum;
	}

	public static void setCallThreadsNum(String callThreadsNum) {
		ReadConfig.callThreadsNum = callThreadsNum;
	}

    public static String getGnApiGnPostUrl() {
        return gnApiGnPostUrl;
    }

    public static void setGnApiGnPostUrl(String gnApiGnPostUrl) {
        ReadConfig.gnApiGnPostUrl = gnApiGnPostUrl;
    }

    public static String getCallPostSize() {
        return callPostSize;
    }

    public static void setCallPostSize(String callPostSize) {
        ReadConfig.callPostSize = callPostSize;
    }

    public static String getGposAlignSqlInsertFile() {
        return gposAlignSqlInsertFile;
    }

    public static void setGposAlignSqlInsertFile(String gposAlignSqlInsertFile) {
        ReadConfig.gposAlignSqlInsertFile = gposAlignSqlInsertFile;
    }

    public static String getGnApiAlignInnerGposUrl() {
        return gnApiAlignInnerGposUrl;
    }

    public static void setGnApiAlignInnerGposUrl(String gnApiAlignInnerGposUrl) {
        ReadConfig.gnApiAlignInnerGposUrl = gnApiAlignInnerGposUrl;
    }
    
	public static String getNaccessInstallFile() {
		return naccessInstallFile;
	}

	public static void setNaccessInstallFile(String naccessInstallFile) {
		ReadConfig.naccessInstallFile = naccessInstallFile;
	}

	public static String getNaccessRunFile() {
		return naccessRunFile;
	}

	public static void setNaccessRunFile(String naccessRunFile) {
		ReadConfig.naccessRunFile = naccessRunFile;
	}

	public static String getRelativeRatio() {
		return relativeRatio;
	}

	public static void setRelativeRatio(String relativeRatio) {
		ReadConfig.relativeRatio = relativeRatio;
	}

	public static String getDsspLocalDataFile() {
		return dsspLocalDataFile;
	}

	public static void setDsspLocalDataFile(String dsspLocalDataFile) {
		ReadConfig.dsspLocalDataFile = dsspLocalDataFile;
	}

	public static String getInsertStructureAnnotationSQL() {
		return insertStructureAnnotationSQL;
	}

	public static void setInsertStructureAnnotationSQL(String insertStructureAnnotationSQL) {
		ReadConfig.insertStructureAnnotationSQL = insertStructureAnnotationSQL;
	}

	public static String getNaccessFileSuffix() {
		return naccessFileSuffix;
	}

	public static void setNaccessFileSuffix(String naccessFileSuffix) {
		ReadConfig.naccessFileSuffix = naccessFileSuffix;
	}

	public static String getDsspFileSuffix() {
		return dsspFileSuffix;
	}

	public static void setDsspFileSuffix(String dsspFileSuffix) {
		ReadConfig.dsspFileSuffix = dsspFileSuffix;
	}

	public static String getSequenceDomainsUrl() {
		return sequenceDomainsUrl;
	}

	public static void setSequenceDomainsUrl(String sequenceDomainsUrl) {
		ReadConfig.sequenceDomainsUrl = sequenceDomainsUrl;
	}

	public static String getStructureDomainsUrl() {
		return structureDomainsUrl;
	}

	public static void setStructureDomainsUrl(String structureDomainsUrl) {
		ReadConfig.structureDomainsUrl = structureDomainsUrl;
	}
	
}
