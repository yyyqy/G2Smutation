--
-- database `g2smutation`
--
drop table IF EXISTS clinvar_entry;
CREATE TABLE `clinvar_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART_POSEND
  `MUTATION_ID` int,
  `CLINVAR_ID` int, -- key
  `REF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ALT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `AF_ESP` float,
  `AF_EXAC` float,
  `AF_TGP` float,
  `ALLELEID` int,
  `CLNDN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,   
  `CLNDNINCL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNDISDB` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNDISDBINCL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNHGVS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNREVSTAT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNSIG` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNSIGCONF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNSIGINCL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNVC` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNVCSO` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLNVI` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `DBVARID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENEINFO` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MC` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ORIGIN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `RS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SSR` int,
  `UPDATE_DATE` DATE,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_ID`,`CLINVAR_ID`)
);

