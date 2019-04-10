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

CREATE TABLE `mutation_location_entry` (
  `MUTATION_ID` int,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART_POSEND
  `CHR` VARCHAR(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, 
  `POS_START` int(255) NOT NULL,
  `POS_END` int(255),
  PRIMARY KEY(`MUTATION_ID`),
  KEY(`CHR_POS`)
);

-- Annotation
CREATE TABLE `structure_annotation_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART_POSEND
  `MUTATION_ID` int,
  `PDB_NO` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_INDEX` int NOT NULL,
  `PDB_RESIDUE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BURIED` int, -- From NACCESS, 1 for true, 0 for false
  `ALL_ATOMS_ABS` float,
  `ALL_ATOMS_REL` float,
  `TOTAL_SIDE_ABS` float,
  `TOTAL_SIDE_REL` float,
  `MAIN_CHAIN_ABS` float,
  `MAIN_CHAIN_REL` float,
  `NON_POLAR_ABS` float,
  `NON_POLAR_REL` float,
  `ALL_POLAR_ABS` float,
  `ALL_POLAR_REL` float,
  `SEC_STRUCTURE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin, -- From DSSP
  `THREE_TURN_HELIX` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `FOUR_TURN_HELIX` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `FIVE_TURN_HELIX` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin, 
  `GEOMETRICAL_BEND` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `CHIRALITY` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `BETA_BRIDGE_LABELA` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `BETA_BRIDGE_LABELB` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `BPA` int,
  `BPB` int,
  `BETA_SHEET_LABEL` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin,
  `ACC` int,
  `LIGAND_BINDING_PROTEIN` int, -- From HET, LPC, 1 for true, 0 for false 
  `LIGAND_BINDING_DIRECT` int, -- Direct binding, 1 for true, 0 for false 
  `LIGAND_NAME` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin, 
  `INTERPRO_ID` VARCHAR(9) CHARACTER SET utf8 COLLATE utf8_bin,
  `INTERPRO_NAME` TEXT,
  `INTERPRO_IDENTIFIER` TEXT,
  `INTERPRO_START` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `INTERPRO_END` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PFAM_ID` VARCHAR(7) CHARACTER SET utf8 COLLATE utf8_bin,
  `PFAM_NAME` TEXT,
  `PFAM_DESCRIPTION` TEXT,
  `PFAM_IDENTIFIER` TEXT,
  `PFAM_START` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PFAM_END` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CATH_ID` VARCHAR(16) CHARACTER SET utf8 COLLATE utf8_bin,
  `CATH_ARCHITECTURE` TEXT,
  `CATH_CLASS` TEXT,
  `CATH_HOMOLOGY` TEXT,
  `CATH_IDENTIFIER` TEXT,
  `CATH_NAME` TEXT,
  `CATH_TOPOLOGY` TEXT,
  `CATH_DOMAIN_ID` TEXT,
  `CATH_DOMAIN_START` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin, 
  `CATH_DOMAIN_END` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_ID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_DESCRIPTION` TEXT, 
  `SCOP_IDENTIFIER` TEXT, 
  `SCOP_SCCS` VARCHAR(16) CHARACTER SET utf8 COLLATE utf8_bin, 
  `SCOP_CLASS_SUNID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_CLASS_DESCRIPTION` TEXT, 
  `SCOP_FOLD_SUNID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_FOLD_DESCRIPTION` TEXT,
  `SCOP_SUPERFAMILY_SUNID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_SUPERFAMILY_DESCRIPTION` TEXT,
  `SCOP_START` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCOP_END` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_ID`)
);

CREATE TABLE `gpos_allmapping_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART
  `DBSNP_ID` VARCHAR(50) default '',
  `CLINVAR_ID` VARCHAR(50) default '',
  `COSMIC_ID` VARCHAR(50) default '',
  `GENIE_ID` VARCHAR(50) default '',
  `TCGA_ID` VARCHAR(50) default '',
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`DBSNP_ID`,`CLINVAR_ID`,`COSMIC_ID`,`GENIE_ID`,`TCGA_ID`)
);


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
  `SEG_NUM` int(255),
  `PDB_NUM` int(255),
  `ALIGNMENT_NUM` int(255),
  PRIMARY KEY(`ID`)
);

