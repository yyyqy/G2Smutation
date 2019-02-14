--
-- database `g2smutation`
--
drop table IF EXISTS dbsnp_entry;
CREATE TABLE `dbsnp_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, --CHR_POSSTART_POSEND
  `MUTATION_ID` int,
  `RS_ID` int, --key
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_ID`,`RS_ID`)
);

