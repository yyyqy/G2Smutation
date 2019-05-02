--
-- database `g2smutation`
--
-- Rebuilt in weekly update
SET FOREIGN_KEY_CHECKS = 0;
drop table IF EXISTS mutation_usage_table;
SET FOREIGN_KEY_CHECKS = 1;
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
SELECT DISTINCT x.*  FROM mutation_entry x JOIN mutation_entry y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_INDEX = x.PDB_INDEX AND y.PDB_RESIDUE <> x.PDB_RESIDUE ORDER BY MUTATION_NO;

