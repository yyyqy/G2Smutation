--
-- database `g2smutation`
--
SET FOREIGN_KEY_CHECKS = 0;
drop table IF EXISTS gpos_allmapping_entry;
drop table IF EXISTS gpos_protein_entry;
SET FOREIGN_KEY_CHECKS = 1;

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
