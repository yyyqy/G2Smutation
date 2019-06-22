--
-- database `G2Smutation`
--
select MAX(CAST(UPDATE_DATE AS CHAR)) from pdb_seq_alignment;
select count(distinct PDB_ID) from pdb_entry;
select count(*) from pdb_entry;
select count(*) from pdb_seq_alignment;
select count(*) from dbsnp_entry;
select count(*) from clinvar_entry;
select count(*) from cosmic_entry;
select count(*) from genie_entry;
select count(*) from tcga_entry;
select count(DISTINCT(CHR_POS)) from gpos_allmapping_entry where DBSNP_ID<>'';
select count(DISTINCT(CHR_POS)) from gpos_allmapping_entry where CLINVAR_ID<>'';
select count(DISTINCT(CHR_POS)) from gpos_allmapping_entry where COSMIC_ID<>'';
select count(DISTINCT(CHR_POS)) from gpos_allmapping_entry where GENIE_ID<>'';
select count(DISTINCT(CHR_POS)) from gpos_allmapping_entry where TCGA_ID<>'';
select count(*) from gpos_allmapping_pdb_entry;
select count(DISTINCT(CHR_POS)) from gpos_allmapping_pdb_entry;
select count(*) from gpos_protein_entry;
select count(DISTINCT(CHR_POS)) from gpos_protein_entry;
select count(*) from mutation_usage_table;
select count(DISTINCT(MUTATION_NO)) from mutation_usage_table;
-- update table
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='R';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='N';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='D';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='C';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='Q';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'A' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='N';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='D';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='C';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='Q';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'R' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='D';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='C';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='Q';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'N' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='C';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='Q';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'D' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='Q';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'C' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='E';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Q' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='G';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'E' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='H';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'G' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='I';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'H' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='L';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'I' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='K';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'L' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='M';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'K' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='F';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'M' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='P';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'F' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'P' AND x.PDB_RESIDUE='S';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'P' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'P' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'P' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'P' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'S' AND x.PDB_RESIDUE='T';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'S' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'S' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'S' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'T' AND x.PDB_RESIDUE='W';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'T' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'T' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'W' AND x.PDB_RESIDUE='Y';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'W' AND x.PDB_RESIDUE='V';
SELECT count(DISTINCT x.MUTATION_NO)  FROM mutation_usage_table x JOIN mutation_usage_table y ON y.MUTATION_NO = x.MUTATION_NO AND y.PDB_RESIDUE = 'Y' AND x.PDB_RESIDUE='V';

