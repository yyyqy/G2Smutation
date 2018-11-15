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
drop table IF EXISTS rs_mutation_entry;
drop table IF EXISTS mutation_usage_table;
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

CREATE TABLE `rs_mutation_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `RS_SNP_ID` int(255) NOT NULL,
  `SEQ_ID` int(255) NOT NULL,
  `SEQ_INDEX` int NOT NULL,
  `SEQ_RESIDUE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_NO` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PDB_INDEX` int NOT NULL,
  `PDB_RESIDUE` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ALIGNMENT_ID` int NOT NULL,
  PRIMARY KEY(`ID`),
  KEY(`RS_SNP_ID`,`SEQ_ID`,`PDB_NO`,`ALIGNMENT_ID`),
  FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`),
  FOREIGN KEY(`PDB_NO`) REFERENCES `pdb_entry` (`PDB_NO`),
  FOREIGN KEY(`ALIGNMENT_ID`) REFERENCES `pdb_seq_alignment` (`ALIGNMENT_ID`)
);

CREATE TABLE `mutation_usage_table` (
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
  `UPDATE_DATE` DATE,
  PRIMARY KEY(`MUTATION_ID`),
  KEY(`MUTATION_NO`,`SEQ_ID`,`PDB_NO`,`ALIGNMENT_ID`),
  FOREIGN KEY(`SEQ_ID`) REFERENCES `seq_entry` (`SEQ_ID`),
  FOREIGN KEY(`PDB_NO`) REFERENCES `pdb_entry` (`PDB_NO`),
  FOREIGN KEY(`ALIGNMENT_ID`) REFERENCES `pdb_seq_alignment` (`ALIGNMENT_ID`)
);

CREATE TABLE `update_record` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `UPDATE_DATE` DATE,
  `SEG_NUM` int(255),
  `PDB_NUM` int(255),
  `ALIGNMENT_NUM` int(255),
  PRIMARY KEY(`ID`)
);

DROP PROCEDURE IF EXISTS `InsertUpdate`;
DELIMITER //
CREATE PROCEDURE InsertUpdate(IN inPDB_NO VARCHAR(12), IN inPDB_ID VARCHAR(4), IN inCHAIN VARCHAR(4), IN inPDB_SEG VARCHAR(2), IN inSEG_START VARCHAR(4), IN inSEQ_ID int, IN inPDB_FROM int, IN inPDB_TO int, IN inSEQ_FROM int, IN inSEQ_TO int, IN inEVALUE double, IN inBITSCORE float, IN inIDENTITY float, IN inIDENTP float, IN inSEQ_ALIGN text, IN inPDB_ALIGN text, IN inMIDLINE_ALIGN text, IN inUPDATE_DATE DATE )
BEGIN
DECLARE minBITSCORE float;
DECLARE countBITSCORE int;
SELECT COUNT(*) INTO countBITSCORE FROM pdb_seq_alignment where SEQ_ID=inSEQ_ID;
SELECT MIN(BITSCORE) INTO minBITSCORE FROM pdb_seq_alignment where SEQ_ID=inSEQ_ID;
IF(inBITSCORE>minBITSCORE) THEN
  IF(countBITSCORE<50) THEN
    INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE);
  ELSE
    DELETE FROM `pdb_seq_alignment` WHERE (SEQ_ID=inSEQ_ID and BITSCORE=minBITSCORE);
    INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE);
  END IF;
ELSE
  IF(countBITSCORE<50) THEN
    INSERT INTO `pdb_seq_alignment` (`PDB_NO`,`PDB_ID`,`CHAIN`,`PDB_SEG`,`SEG_START`,`SEQ_ID`,`PDB_FROM`,`PDB_TO`,`SEQ_FROM`,`SEQ_TO`,`EVALUE`,`BITSCORE`,`IDENTITY`,`IDENTP`,`SEQ_ALIGN`,`PDB_ALIGN`,`MIDLINE_ALIGN`,`UPDATE_DATE`) VALUES (inPDB_NO,inPDB_ID,inCHAIN,inPDB_SEG,inSEG_START,inSEQ_ID,inPDB_FROM,inPDB_TO,inSEQ_FROM,inSEQ_TO,inEVALUE,inBITSCORE,inIDENTITY,inIDENTP,inSEQ_ALIGN,inPDB_ALIGN,inMIDLINE_ALIGN,inUPDATE_DATE);
  END IF;
END IF;
END //
DELIMITER ;
