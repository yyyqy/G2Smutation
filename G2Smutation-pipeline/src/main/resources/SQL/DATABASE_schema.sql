--
-- database `g2smutation`
--
SET FOREIGN_KEY_CHECKS = 0;
drop table IF EXISTS pdb_seq_alignment;
drop table IF EXISTS uniprot_entry;
drop table IF EXISTS ensembl_entry;
drop table IF EXISTS pdb_entry;
drop table IF EXISTS seq_entry;
drop table IF EXISTS mutation_entry;
drop table IF EXISTS gpos_allmapping_entry;
drop table IF EXISTS gpos_protein_entry;
drop table IF EXISTS gpos_allmapping_pdb_entry;
drop table IF EXISTS mutation_usage_table;
drop table IF EXISTS mutation_location_entry;
drop table IF EXISTS structure_annotation_entry;
drop table IF EXISTS dbsnp_entry;
drop table IF EXISTS clinvar_entry;
drop table IF EXISTS cosmic_entry;
drop table IF EXISTS genie_entry;
drop table IF EXISTS tcga_entry;
drop table IF EXISTS update_record;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `seq_entry`(
    `SEQ_ID` int(255) NOT NULL,
    `SEQENCE` text,
    PRIMARY KEY(`SEQ_ID`)
);
CREATE TABLE `uniprot_entry`(
    `UNIPROT_ID_ISO` VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `UNIPROT_ID` VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `NAME` VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `ISOFORM` VARCHAR(2) NOT NULL,
    `SEQ_ID` int(255),
    PRIMARY KEY(`UNIPROT_ID_ISO`),
    FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`)
);
CREATE TABLE `ensembl_entry` (
    `ENSEMBL_ID` VARCHAR(20) NOT NULL,
    `ENSEMBL_GENE` VARCHAR(20),
    `ENSEMBL_TRANSCRIPT` VARCHAR(20),
    `SEQ_ID` int(255),
    PRIMARY KEY(`ENSEMBL_ID`),
    FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`)
);
CREATE TABLE `pdb_entry` (
    `PDB_NO` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `PDB_ID` VARCHAR(4) NOT NULL,
    `CHAIN` VARCHAR(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `PDB_SEG` VARCHAR(2) NOT NULL,
    `SEG_START` VARCHAR(4) NOT NULL,
    PRIMARY KEY(`PDB_NO`)
);
CREATE TABLE `pdb_seq_alignment` (
  `ALIGNMENT_ID` int NOT NULL AUTO_INCREMENT,
  `PDB_NO` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_ID` VARCHAR(4) NOT NULL,
  `CHAIN` VARCHAR(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_SEG` VARCHAR(2) NOT NULL,
  `SEG_START` VARCHAR(4) NOT NULL,
  `SEQ_ID` int(255) NOT NULL,
  `PDB_FROM` int NOT NULL,
  `PDB_TO` int NOT NULL,
  `SEQ_FROM` int NOT NULL,
  `SEQ_TO` int NOT NULL,
  `EVALUE` double,
  `BITSCORE` float,
  `IDENTITY` float,
  `IDENTP` float,
  `SEQ_ALIGN` text,
  `PDB_ALIGN` text,
  `MIDLINE_ALIGN` text,
  `UPDATE_DATE` DATE,
  PRIMARY KEY (`ALIGNMENT_ID`),
  KEY(`SEQ_ID`),
  KEY(`PDB_ID`, `CHAIN`, `PDB_SEG`),
  FOREIGN KEY(`PDB_NO`) REFERENCES `pdb_entry` (`PDB_NO`),
  FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`)
);
CREATE TABLE `mutation_entry` (
  `MUTATION_ID` int NOT NULL AUTO_INCREMENT,
  `MUTATION_NO` VARCHAR(50) NOT NULL,
  `SEQ_ID` int(255) NOT NULL,
  `SEQ_NAME` text,
  `SEQ_INDEX` int NOT NULL,
  `SEQ_RESIDUE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_NO` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_INDEX` int NOT NULL,
  `PDB_RESIDUE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ALIGNMENT_ID` int NOT NULL,
  PRIMARY KEY(`MUTATION_ID`),
  KEY(`MUTATION_NO`,`SEQ_ID`,`PDB_NO`,`ALIGNMENT_ID`),
  FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`),
  FOREIGN KEY(`PDB_NO`) REFERENCES `pdb_entry` (`PDB_NO`),
  FOREIGN KEY(`ALIGNMENT_ID`) REFERENCES `pdb_seq_alignment` (`ALIGNMENT_ID`)
);

-- Build only when Init
CREATE TABLE `gpos_allmapping_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART
  `DBSNP_ID` VARCHAR(255) default '',
  `CLINVAR_ID` VARCHAR(255) default '',
  `COSMIC_ID` VARCHAR(255) default '',
  `GENIE_ID` VARCHAR(255) default '',
  `TCGA_ID` VARCHAR(255) default '',
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`DBSNP_ID`,`CLINVAR_ID`,`COSMIC_ID`,`GENIE_ID`,`TCGA_ID`)
);

-- Build only when Init
CREATE TABLE `gpos_protein_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART
  `MUTATION_NO` VARCHAR(50) NOT NULL, -- same as MUTATION_NO in table mutation_usage_table: SEQID_INDEX
  `SEQ_ID` int NOT NULL,
  `SEQ_INDEX` int NOT NULL,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_NO`,`SEQ_ID`),
  FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`) 
);

CREATE TABLE `update_record` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `UPDATE_DATE` DATE,
  `SEG_NUM` int,
  `PDB_NUM` int,
  `ALIGNMENT_NUM` int,
  `DBSNP_NUM` int,
  `CLINVAR_NUM` int,
  `COSMIC_NUM` int,
  `GENIE_NUM` int,
  `TCGA_NUM` int,
  `DBSNP_MAPPING_NUM` int,
  `CLINVAR_MAPPING_NUM` int,
  `COSMIC_MAPPING_NUM` int,
  `GENIE_MAPPING_NUM` int,
  `TCGA_MAPPING_NUM` int,
  `MUTATION_NO_MAPPING_NUM` int,
  `MUTATION_NO_MAPPING_UNIQUE_NUM` int,
  `MUTATION_NO` int,
  `MUTATION_NO_UNIQUE` int,
  `MUTATION_USAGE_NUM` int,
  `MUTATION_LOCATION_NUM` int,
  PRIMARY KEY(`ID`)
);

