--
-- database `g2smutation`
--
drop table IF EXISTS cosmic_entry;
CREATE TABLE `cosmic_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART_POSEND
  `MUTATION_ID` int,
  `GENE_NAME` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ACCESSION_NUMBER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENE_CDS_LENGTH` int,
  `HGNC_ID` int,
  `SAMPLE_NAME` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ID_SAMPLE` int,
  `ID_TUMOUR` int,
  `PRIMARY_SITE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SITE_SUBTYPE_1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SITE_SUBTYPE_2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SITE_SUBTYPE_3` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PRIMARY_HISTOLOGY` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HISTOLOGY_SUBTYPE_1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HISTOLOGY_SUBTYPE_2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HISTOLOGY_SUBTYPE_3` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENOME-WIDE_SCREEN` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `COSMIC_MUTATION_ID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin, -- key in COSMIC
  `MUTATION_CDS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MUTATION_AA` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MUTATION_DESCRIPTION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MUTATION_ZYGOSITY` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `LOH` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `GRCH` int,
  `MUTATION_GENOME_POSITION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MUTATION_STRAND` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `SNP` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `RESISTANCE_MUTATION` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `FATHMM_PREDICTION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `FATHMM_SCORE` float,
  `MUTATION_SOMATIC_STATUS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PUBMED_PMID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ID_STUDY` int,
  `SAMPLE_TYPE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOUR_ORIGIN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `AGE` VARCHAR(3) CHARACTER SET utf8 COLLATE utf8_bin,
  `COSMIC_VERSION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_ID`,`COSMIC_MUTATION_ID`)
);