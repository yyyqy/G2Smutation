--
-- database `g2smutation`
--
-- Build only when Init, may rebuild in weekly update later for clinvar update every week
drop table IF EXISTS clinvar_entry;
CREATE TABLE `clinvar_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, -- CHR_POSSTART_POSEND
  `MUTATION_NO` VARCHAR(50) NOT NULL,
  `CLINVAR_ID` int, -- key in clinvar
  `REF` TEXT,
  `ALT` TEXT,
  `AF_ESP` float DEFAULT -1,
  `AF_EXAC` float DEFAULT -1,
  `AF_TGP` float DEFAULT -1,
  `ALLELEID` int DEFAULT -1,
  `CLNDN` TEXT,   
  `CLNDNINCL` TEXT,
  `CLNDISDB` TEXT,
  `CLNDISDBINCL` TEXT,
  `CLNHGVS` TEXT,
  `CLNREVSTAT` TEXT,
  `CLNSIG` TEXT,
  `CLNSIGCONF` TEXT,
  `CLNSIGINCL` TEXT,
  `CLNVC` TEXT,
  `CLNVCSO` TEXT,
  `CLNVI` TEXT,
  `DBVARID` TEXT,
  `GENEINFO` TEXT,
  `MC` TEXT,
  `ORIGIN` TEXT,
  `RS` TEXT,
  `SSR` int DEFAULT -1,
  `UPDATE_DATE` DATE,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_NO`,`CLINVAR_ID`)
);

