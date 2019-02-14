--
-- database `g2smutation`
--
drop table IF EXISTS genie_entry;
CREATE TABLE `genie_entry` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHR_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL, --CHR_POSSTART_POSEND
  `MUTATION_ID` int,
  `HUGO_SYMBOL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ENTREZ_GENE_ID` int,
  `CENTER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `NCBI_BUILD` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,--Original File has 3 col chr pos
  `STRAND` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VARIANT_CLASSIFICATION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VARIANT_TYPE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `REF_ALLELE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_SEQ_ALLELE1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_SEQ_ALLELE2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `DBSNP_RS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `DBSNP_VAL_STATUS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_SAMPLE_BARCODE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MATCHED_NORM_SAMPLE_BARCODE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MATCH_NORM_SEQ_ALLELE1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MATCH_NORM_SEQ_ALLELE2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_VALIDATION_ALLELE1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_VALIDATION_ALLELE2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MATCH_NORM_VALIDATION_ALLELE1` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MATCH_NORM_VALIDATION_ALLELE2` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VERIFICATION_STATUS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VALIDATION_STATUS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MUTATION_STATUS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SEQUENCING_PHASE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SEQUENCE_SOURCE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VALIDATIO_METHOD` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SCORE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `BAM_FILE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SEQUENCER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TUMOR_SAMPLE_UUID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin, -- type?
  `MATCHED_NORM_SAMPLE_UUID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HGVSC` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HGVSP` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HGVSP_SHORT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TRANSCRIPT_ID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXON_NUMBER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `T_DEPTH` int,
  `T_REF_COUNT` int,
  `T_ALT_COUNT` int,
  `N_DEPTH` int,
  `N_REF_COUNT` int,
  `N_ALT_COUNT` int,
  `ALL_EFFECTS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ALLELE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `FEATURE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `FEATURE_TYPE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CONSEQUENCE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CDNA_POSITION` int,
  `CDS_POSITION` int,
  `PROTEIN_POSITION` int,
  `AMINO_ACIDS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CODONS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXISTING_VARIATION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ALLELE_NUM` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `DISTANCE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `STRAND` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin, -- STRAND in TCGA, STRAND-VEP here
  `SYMBOL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SYMBOL_SOURCE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HGNC_ID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `BIOTYPE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CANONICAL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CCDS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ENSP` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SWISSPROT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TREMBL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `UNIPARC` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `REFSEQ` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SIFT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `POLYPHEN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXON` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `INTRON` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `DOMAINS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GMAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `AFR_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `AMR_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `ASN_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EAS_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EUR_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SAS_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `AA_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EA_MAF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `CLIN_SIG` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `SOMATIC` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PUBMED` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MOTIF_NAME` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MOTIF_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HIGH_INF_POS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MOTIF_SCORE_CHANGE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `IMPACT` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PICK` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VARIANT_CLASS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `TSL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `HGVS_OFFSET` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `PHENO` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `MINIMISED` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_AFR` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_AMR` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_EAS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_FIN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_NFE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_OTH` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_SAS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENE_PHENO` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `FILTER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `FLANKING_BPS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VARIANT_ID` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `VARIANT_QUAL` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AF_ADJ` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_ADJ` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_AFR` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_AMR` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_EAS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_FIN` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_NFE` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_OTH` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_AC_AN_SAS` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `EXAC_FILTER` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  `GENIE_VERSION` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY(`ID`),
  KEY(`CHR_POS`,`MUTATION_ID`)
);
