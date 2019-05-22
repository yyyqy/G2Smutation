--
-- database `g2smutation`
--
-- Rebuilt in weekly update
drop table IF EXISTS mutation_location_entry;
CREATE TABLE `mutation_location_entry` (
  `MUTATION_ID` int,
  `MUTATION_NO` VARCHAR(50) NOT NULL,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin, -- CHR_POSSTART_POSEND
  `CHR` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, 
  `POS_START` int(255) NOT NULL,
  `POS_END` int(255),
  PRIMARY KEY(`MUTATION_ID`),
  KEY(`MUTATION_NO`,`CHR_POS`)
);
