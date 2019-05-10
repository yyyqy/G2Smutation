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
select count(DISTINCT(CHR_POS)) from gpos_allmapping_pdb_entry;
select count(*) from gpos_allmapping_pdb_entry;
select count(DISTINCT(CHR_POS)) from gpos_protein_entry;
select count(*) from gpos_protein_entry;
select count(*) from mutation_usage_table;
select count(DISTINCT(MUTATION_NO)) from mutation_usage_table;
