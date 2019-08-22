package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table genie_entry 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "genie_entry")
public class Genie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "CHR_POS")
    private String chrPos;
    
    @Column(name = "MUTATION_NO")
    private String mutationNo;
    
    @Column(name = "HUGO_SYMBOL")
    private String hugoSymbol;
    
    @Column(name = "ENTREZ_GENE_ID")
    private int entrezGeneId;
    
    @Column(name = "CENTER")
    private String center;
    
    @Column(name = "NCBI_BUILD")
    private String ncbiBuild;
    
    @Column(name = "CHROMOSOME")
    private String chromosome;
    
    @Column(name = "START_POSITION")
    private int startPosition;
    
    @Column(name = "END_POSITION")
    private int endPosition;
    
    @Column(name = "STRAND")
    private String strand;
    
    @Column(name = "VARIANT_CLASSIFICATION")
    private String variantClassification;
    
    @Column(name = "VARIANT_TYPE")
    private String variantType;
    
    @Column(name = "REFERENCE_ALLELE")
    private String referenceAllele;
    
    @Column(name = "TUMOR_SEQ_ALLELE1")
    private String tumorSeqAllele1;
    
    @Column(name = "TUMOR_SEQ_ALLELE2")
    private String tumorSeqAllele2;
    
    @Column(name = "DBSNP_RS")
    private String dbsnpRs;
    
    @Column(name = "DBSNP_VAL_STATUS")
    private String dbsnpValStatus;
    
    @Column(name = "TUMOR_SAMPLE_BARCODE")
    private String tumorSampleBarcode;
    
    @Column(name = "MATCHED_NORM_SAMPLE_BARCODE")
    private String matchedNormSampleBarcode;
    
    @Column(name = "MATCH_NORM_SEQ_ALLELE1")
    private String matchNormSeqAllele1;
    
    @Column(name = "MATCH_NORM_SEQ_ALLELE2")
    private String matchNormSeqAllele2;
    
    @Column(name = "TUMOR_VALIDATION_ALLELE1")
    private String tumorValidationAllele1;
    
    @Column(name = "TUMOR_VALIDATION_ALLELE2")
    private String tumorValidationAllele2;
    
    @Column(name = "MATCH_NORM_VALIDATION_ALLELE1")
    private String marchNormValidationAllele1;
    
    @Column(name = "MATCH_NORM_VALIDATION_ALLELE2")
    private String marchNormValidationAllele2;
    
    @Column(name = "VERIFICATION_STATUS")
    private String verificationStatus;
    
    @Column(name = "VALIDATION_STATUS")
    private String validationStatus;
    
    @Column(name = "MUTATION_STATUS")
    private String mutationStatus;
    
    @Column(name = "SEQUENCING_PHASE")
    private String sequenceingPhase;
    
    @Column(name = "SEQUENCE_SOURCE")
    private String sequenceSource;
    
    @Column(name = "VALIDATION_METHOD")
    private String validationMethod;
    
    @Column(name = "SCORE")
    private String score;
    
    @Column(name = "BAM_FILE")
    private String bamFile;
    
    @Column(name = "SEQUENCER")
    private String sequencer;
    
    @Column(name = "TUMOR_SAMPLE_UUID")
    private String tumorSampleUUID;
    
    @Column(name = "MATCHED_NORM_SAMPLE_UUID")
    private String matchedNormSampleUUID;
    
    @Column(name = "HGVSC")
    private String hgvsc;
    
    @Column(name = "HGVSP")
    private String hgvsp;
    
    @Column(name = "HGVSP_SHORT")
    private String hgvspShort;
    
    @Column(name = "TRANSCRIPT_ID")
    private String transcriptId;
    
    @Column(name = "EXON_NUMBER")
    private String exonNumber;
    
    @Column(name = "T_DEPTH")
    private String tDepth;
    
    @Column(name = "T_REF_COUNT")
    private String tRefCount;
    
    @Column(name = "T_ALT_COUNT")
    private String tAltCount;
    
    @Column(name = "N_DEPTH")
    private String nDepth;
    
    @Column(name = "N_REF_COUNT")
    private String nRefCount;
    
    @Column(name = "N_ALT_COUNT")
    private String nAltCount;
    
    @Column(name = "ALL_EFFECTS")
    private String allEffects;
    
    @Column(name = "ALLELE")
    private String allele;
    
    @Column(name = "GENE")
    private String gene;
    
    @Column(name = "FEATURE")
    private String feature;
    
    @Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "CONSEQUENCE")
    private String consequence;
    
    @Column(name = "CDNA_POSITION")
    private String cdnaPosition;
    
    @Column(name = "CDS_POSITION")
    private String cdsPosition;
    
    @Column(name = "PROTEIN_POSITION")
    private String proteinPosition;
    
    @Column(name = "AMINO_ACIDS")
    private String aminoAcids;
    
    @Column(name = "CODONS")
    private String codons;
    
    @Column(name = "EXISTING_VARIATION")
    private String existingVariation;
    
    @Column(name = "ALLELE_NUM")
    private String alleleNum;
    
    @Column(name = "DISTANCE")
    private String distance;
    
    @Column(name = "STRAND_VEP")
    private String strandVep;
    
    @Column(name = "SYMBOL")
    private String symbol;
    
    @Column(name = "SYMBOL_SOURCE")
    private String symbolSource;
    
    @Column(name = "HGNC_ID")
    private String hgncId;
    
    @Column(name = "BIOTYPE")
    private String biotype;
    
    @Column(name = "CANONICAL")
    private String cannonical;
    
    @Column(name = "CCDS")
    private String ccds;
    
    @Column(name = "ENSP")
    private String ensp;
    
    @Column(name = "SWISSPROT")
    private String swissprot;
    
    @Column(name = "TREMBL")
    private String trembl;
    
    @Column(name = "UNIPARC")
    private String uniparc;
    
    @Column(name = "REFSEQ")
    private String refseq;
    
    @Column(name = "SIFT")
    private String sift;
    
    @Column(name = "POLYPHEN")
    private String polyphen;
    
    @Column(name = "EXON")
    private String exon;
    
    @Column(name = "INTRON")
    private String intron;
    
    @Column(name = "DOMAINS")
    private String domains;
    
    @Column(name = "GMAF")
    private String gmaf;
    
    @Column(name = "AFR_MAF")
    private String afrMaf;
    
    @Column(name = "AMR_MAF")
    private String amrMaf;
    
    @Column(name = "ASN_MAF")
    private String asnMaf;
    
    @Column(name = "EAS_MAF")
    private String easMaf;
    
    @Column(name = "EUR_MAF")
    private String eurMaf;
    
    @Column(name = "SAS_MAF")
    private String sasMaf;
    
    @Column(name = "AA_MAF")
    private String aaMaf;
    
    @Column(name = "EA_MAF")
    private String eaMaf;
    
    @Column(name = "CLIN_SIG")
    private String clinSig;

    @Column(name = "SOMATIC")
    private String somatic;

    @Column(name = "PUBMED")
    private String pubmed;
    
    @Column(name = "MOTIF_NAME")
    private String motifName;
    
    @Column(name = "MOTIF_POS")
    private String motifPos;
    
    @Column(name = "HIGH_INF_POS")
    private String highInfPos;

    @Column(name = "MOTIF_SCORE_CHANGE")
    private String motifScoreChange;
    
    @Column(name = "IMPACT")
    private String impact;
    
    @Column(name = "PICK")
    private String pick;
    
    @Column(name = "VARIANT_CLASS")
    private String variantClass;
    
    @Column(name = "TSL")
    private String tsl;
    
    @Column(name = "HGVS_OFFSET")
    private String hgvsOffset;
    
    @Column(name = "PHENO")
    private String pheno;
    
    @Column(name = "MINIMISED")
    private String minimised;
    
    @Column(name = "EXAC_AF")
    private String exacAf;
    
    @Column(name = "EXAC_AF_AFR")
    private String exacAfAFR;
    
    @Column(name = "EXAC_AF_AMR")
    private String exacAfAMR;
    
    @Column(name = "EXAC_AF_EAS")
    private String exacAfEAS;
    
    @Column(name = "EXAC_AF_FIN")
    private String exacAfFIN;
    
    @Column(name = "EXAC_AF_NFE")
    private String exacAfNFE;
    
    @Column(name = "EXAC_AF_OTH")
    private String exacAfOTH;
    
    @Column(name = "EXAC_AF_SAS")
    private String exacAfSAS;
    
    @Column(name = "GENE_PHENO")
    private String genePheno;
    
    @Column(name = "FILTER")
    private String filter;
    
    @Column(name = "FLANKING_BPS")
    private String flankingBps;
    
    @Column(name = "VARIANT_ID")
    private String variantId;
    
    @Column(name = "VARIANT_QUAL")
    private String variantQual;
    
    @Column(name = "EXAC_AF_ADJ")
    private String exacAfAdj;
    
    @Column(name = "EXAC_AC_AN_ADJ")
    private String exacAcAnAdj;
    
    @Column(name = "EXAC_AC_AN")
    private String exacAcAn;
    
    @Column(name = "EXAC_AC_AN_AFR")
    private String exacAcAnAfr;
    
    @Column(name = "EXAC_AC_AN_AMR")
    private String exacAcAnAmr;
    
    @Column(name = "EXAC_AC_AN_EAS")
    private String exacAcAnEas;
    
    @Column(name = "EXAC_AC_AN_FIN")
    private String exacAcAnFin;
    
    @Column(name = "EXAC_AC_AN_NFE")
    private String exacAcAnNfe;
    
    @Column(name = "EXAC_AC_AN_OTH")
    private String exacAcAnOth;
    
    @Column(name = "EXAC_AC_AN_SAS")
    private String exacAcAnSas;
    
    @Column(name = "EXAC_FILTER")
    private String exacfilter;
    
    @Column(name = "GENIE_VERSION")
    private String genieVersion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChrPos() {
		return chrPos;
	}

	public void setChrPos(String chrPos) {
		this.chrPos = chrPos;
	}

	public String getMutationNo() {
		return mutationNo;
	}

	public void setMutationNo(String mutationNo) {
		this.mutationNo = mutationNo;
	}

	public String getHugoSymbol() {
		return hugoSymbol;
	}

	public void setHugoSymbol(String hugoSymbol) {
		this.hugoSymbol = hugoSymbol;
	}

	public int getEntrezGeneId() {
		return entrezGeneId;
	}

	public void setEntrezGeneId(int entrezGeneId) {
		this.entrezGeneId = entrezGeneId;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getNcbiBuild() {
		return ncbiBuild;
	}

	public void setNcbiBuild(String ncbiBuild) {
		this.ncbiBuild = ncbiBuild;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		this.strand = strand;
	}

	public String getVariantClassification() {
		return variantClassification;
	}

	public void setVariantClassification(String variantClassification) {
		this.variantClassification = variantClassification;
	}

	public String getVariantType() {
		return variantType;
	}

	public void setVariantType(String variantType) {
		this.variantType = variantType;
	}

	public String getReferenceAllele() {
		return referenceAllele;
	}

	public void setReferenceAllele(String referenceAllele) {
		this.referenceAllele = referenceAllele;
	}

	public String getTumorSeqAllele1() {
		return tumorSeqAllele1;
	}

	public void setTumorSeqAllele1(String tumorSeqAllele1) {
		this.tumorSeqAllele1 = tumorSeqAllele1;
	}

	public String getTumorSeqAllele2() {
		return tumorSeqAllele2;
	}

	public void setTumorSeqAllele2(String tumorSeqAllele2) {
		this.tumorSeqAllele2 = tumorSeqAllele2;
	}

	public String getDbsnpRs() {
		return dbsnpRs;
	}

	public void setDbsnpRs(String dbsnpRs) {
		this.dbsnpRs = dbsnpRs;
	}

	public String getDbsnpValStatus() {
		return dbsnpValStatus;
	}

	public void setDbsnpValStatus(String dbsnpValStatus) {
		this.dbsnpValStatus = dbsnpValStatus;
	}

	public String getTumorSampleBarcode() {
		return tumorSampleBarcode;
	}

	public void setTumorSampleBarcode(String tumorSampleBarcode) {
		this.tumorSampleBarcode = tumorSampleBarcode;
	}

	public String getMatchedNormSampleBarcode() {
		return matchedNormSampleBarcode;
	}

	public void setMatchedNormSampleBarcode(String matchedNormSampleBarcode) {
		this.matchedNormSampleBarcode = matchedNormSampleBarcode;
	}

	public String getMatchNormSeqAllele1() {
		return matchNormSeqAllele1;
	}

	public void setMatchNormSeqAllele1(String matchNormSeqAllele1) {
		this.matchNormSeqAllele1 = matchNormSeqAllele1;
	}

	public String getMatchNormSeqAllele2() {
		return matchNormSeqAllele2;
	}

	public void setMatchNormSeqAllele2(String matchNormSeqAllele2) {
		this.matchNormSeqAllele2 = matchNormSeqAllele2;
	}

	public String getTumorValidationAllele1() {
		return tumorValidationAllele1;
	}

	public void setTumorValidationAllele1(String tumorValidationAllele1) {
		this.tumorValidationAllele1 = tumorValidationAllele1;
	}

	public String getTumorValidationAllele2() {
		return tumorValidationAllele2;
	}

	public void setTumorValidationAllele2(String tumorValidationAllele2) {
		this.tumorValidationAllele2 = tumorValidationAllele2;
	}

	public String getMarchNormValidationAllele1() {
		return marchNormValidationAllele1;
	}

	public void setMarchNormValidationAllele1(String marchNormValidationAllele1) {
		this.marchNormValidationAllele1 = marchNormValidationAllele1;
	}

	public String getMarchNormValidationAllele2() {
		return marchNormValidationAllele2;
	}

	public void setMarchNormValidationAllele2(String marchNormValidationAllele2) {
		this.marchNormValidationAllele2 = marchNormValidationAllele2;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	public String getMutationStatus() {
		return mutationStatus;
	}

	public void setMutationStatus(String mutationStatus) {
		this.mutationStatus = mutationStatus;
	}

	public String getSequenceingPhase() {
		return sequenceingPhase;
	}

	public void setSequenceingPhase(String sequenceingPhase) {
		this.sequenceingPhase = sequenceingPhase;
	}

	public String getSequenceSource() {
		return sequenceSource;
	}

	public void setSequenceSource(String sequenceSource) {
		this.sequenceSource = sequenceSource;
	}

	public String getValidationMethod() {
		return validationMethod;
	}

	public void setValidationMethod(String validationMethod) {
		this.validationMethod = validationMethod;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getBamFile() {
		return bamFile;
	}

	public void setBamFile(String bamFile) {
		this.bamFile = bamFile;
	}

	public String getSequencer() {
		return sequencer;
	}

	public void setSequencer(String sequencer) {
		this.sequencer = sequencer;
	}

	public String getTumorSampleUUID() {
		return tumorSampleUUID;
	}

	public void setTumorSampleUUID(String tumorSampleUUID) {
		this.tumorSampleUUID = tumorSampleUUID;
	}

	public String getMatchedNormSampleUUID() {
		return matchedNormSampleUUID;
	}

	public void setMatchedNormSampleUUID(String matchedNormSampleUUID) {
		this.matchedNormSampleUUID = matchedNormSampleUUID;
	}

	public String getHgvsc() {
		return hgvsc;
	}

	public void setHgvsc(String hgvsc) {
		this.hgvsc = hgvsc;
	}

	public String getHgvsp() {
		return hgvsp;
	}

	public void setHgvsp(String hgvsp) {
		this.hgvsp = hgvsp;
	}

	public String getHgvspShort() {
		return hgvspShort;
	}

	public void setHgvspShort(String hgvspShort) {
		this.hgvspShort = hgvspShort;
	}

	public String getTranscriptId() {
		return transcriptId;
	}

	public void setTranscriptId(String transcriptId) {
		this.transcriptId = transcriptId;
	}

	public String getExonNumber() {
		return exonNumber;
	}

	public void setExonNumber(String exonNumber) {
		this.exonNumber = exonNumber;
	}

	public String gettDepth() {
		return tDepth;
	}

	public void settDepth(String tDepth) {
		this.tDepth = tDepth;
	}

	public String gettRefCount() {
		return tRefCount;
	}

	public void settRefCount(String tRefCount) {
		this.tRefCount = tRefCount;
	}

	public String gettAltCount() {
		return tAltCount;
	}

	public void settAltCount(String tAltCount) {
		this.tAltCount = tAltCount;
	}

	public String getnDepth() {
		return nDepth;
	}

	public void setnDepth(String nDepth) {
		this.nDepth = nDepth;
	}

	public String getnRefCount() {
		return nRefCount;
	}

	public void setnRefCount(String nRefCount) {
		this.nRefCount = nRefCount;
	}

	public String getnAltCount() {
		return nAltCount;
	}

	public void setnAltCount(String nAltCount) {
		this.nAltCount = nAltCount;
	}

	public String getAllEffects() {
		return allEffects;
	}

	public void setAllEffects(String allEffects) {
		this.allEffects = allEffects;
	}

	public String getAllele() {
		return allele;
	}

	public void setAllele(String allele) {
		this.allele = allele;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getConsequence() {
		return consequence;
	}

	public void setConsequence(String consequence) {
		this.consequence = consequence;
	}

	public String getCdnaPosition() {
		return cdnaPosition;
	}

	public void setCdnaPosition(String cdnaPosition) {
		this.cdnaPosition = cdnaPosition;
	}

	public String getCdsPosition() {
		return cdsPosition;
	}

	public void setCdsPosition(String cdsPosition) {
		this.cdsPosition = cdsPosition;
	}

	public String getProteinPosition() {
		return proteinPosition;
	}

	public void setProteinPosition(String proteinPosition) {
		this.proteinPosition = proteinPosition;
	}

	public String getAminoAcids() {
		return aminoAcids;
	}

	public void setAminoAcids(String aminoAcids) {
		this.aminoAcids = aminoAcids;
	}

	public String getCodons() {
		return codons;
	}

	public void setCodons(String codons) {
		this.codons = codons;
	}

	public String getExistingVariation() {
		return existingVariation;
	}

	public void setExistingVariation(String existingVariation) {
		this.existingVariation = existingVariation;
	}

	public String getAlleleNum() {
		return alleleNum;
	}

	public void setAlleleNum(String alleleNum) {
		this.alleleNum = alleleNum;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getStrandVep() {
		return strandVep;
	}

	public void setStrandVep(String strandVep) {
		this.strandVep = strandVep;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbolSource() {
		return symbolSource;
	}

	public void setSymbolSource(String symbolSource) {
		this.symbolSource = symbolSource;
	}

	public String getHgncId() {
		return hgncId;
	}

	public void setHgncId(String hgncId) {
		this.hgncId = hgncId;
	}

	public String getBiotype() {
		return biotype;
	}

	public void setBiotype(String biotype) {
		this.biotype = biotype;
	}

	public String getCannonical() {
		return cannonical;
	}

	public void setCannonical(String cannonical) {
		this.cannonical = cannonical;
	}

	public String getCcds() {
		return ccds;
	}

	public void setCcds(String ccds) {
		this.ccds = ccds;
	}

	public String getEnsp() {
		return ensp;
	}

	public void setEnsp(String ensp) {
		this.ensp = ensp;
	}

	public String getSwissprot() {
		return swissprot;
	}

	public void setSwissprot(String swissprot) {
		this.swissprot = swissprot;
	}

	public String getTrembl() {
		return trembl;
	}

	public void setTrembl(String trembl) {
		this.trembl = trembl;
	}

	public String getUniparc() {
		return uniparc;
	}

	public void setUniparc(String uniparc) {
		this.uniparc = uniparc;
	}

	public String getRefseq() {
		return refseq;
	}

	public void setRefseq(String refseq) {
		this.refseq = refseq;
	}

	public String getSift() {
		return sift;
	}

	public void setSift(String sift) {
		this.sift = sift;
	}

	public String getPolyphen() {
		return polyphen;
	}

	public void setPolyphen(String polyphen) {
		this.polyphen = polyphen;
	}

	public String getExon() {
		return exon;
	}

	public void setExon(String exon) {
		this.exon = exon;
	}

	public String getIntron() {
		return intron;
	}

	public void setIntron(String intron) {
		this.intron = intron;
	}

	public String getDomains() {
		return domains;
	}

	public void setDomains(String domains) {
		this.domains = domains;
	}

	public String getGmaf() {
		return gmaf;
	}

	public void setGmaf(String gmaf) {
		this.gmaf = gmaf;
	}

	public String getAfrMaf() {
		return afrMaf;
	}

	public void setAfrMaf(String afrMaf) {
		this.afrMaf = afrMaf;
	}

	public String getAmrMaf() {
		return amrMaf;
	}

	public void setAmrMaf(String amrMaf) {
		this.amrMaf = amrMaf;
	}

	public String getAsnMaf() {
		return asnMaf;
	}

	public void setAsnMaf(String asnMaf) {
		this.asnMaf = asnMaf;
	}

	public String getEasMaf() {
		return easMaf;
	}

	public void setEasMaf(String easMaf) {
		this.easMaf = easMaf;
	}

	public String getEurMaf() {
		return eurMaf;
	}

	public void setEurMaf(String eurMaf) {
		this.eurMaf = eurMaf;
	}

	public String getSasMaf() {
		return sasMaf;
	}

	public void setSasMaf(String sasMaf) {
		this.sasMaf = sasMaf;
	}

	public String getAaMaf() {
		return aaMaf;
	}

	public void setAaMaf(String aaMaf) {
		this.aaMaf = aaMaf;
	}

	public String getEaMaf() {
		return eaMaf;
	}

	public void setEaMaf(String eaMaf) {
		this.eaMaf = eaMaf;
	}

	public String getClinSig() {
		return clinSig;
	}

	public void setClinSig(String clinSig) {
		this.clinSig = clinSig;
	}

	public String getSomatic() {
		return somatic;
	}

	public void setSomatic(String somatic) {
		this.somatic = somatic;
	}

	public String getPubmed() {
		return pubmed;
	}

	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}

	public String getMotifName() {
		return motifName;
	}

	public void setMotifName(String motifName) {
		this.motifName = motifName;
	}

	public String getMotifPos() {
		return motifPos;
	}

	public void setMotifPos(String motifPos) {
		this.motifPos = motifPos;
	}

	public String getHighInfPos() {
		return highInfPos;
	}

	public void setHighInfPos(String highInfPos) {
		this.highInfPos = highInfPos;
	}

	public String getMotifScoreChange() {
		return motifScoreChange;
	}

	public void setMotifScoreChange(String motifScoreChange) {
		this.motifScoreChange = motifScoreChange;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getPick() {
		return pick;
	}

	public void setPick(String pick) {
		this.pick = pick;
	}

	public String getVariantClass() {
		return variantClass;
	}

	public void setVariantClass(String variantClass) {
		this.variantClass = variantClass;
	}

	public String getTsl() {
		return tsl;
	}

	public void setTsl(String tsl) {
		this.tsl = tsl;
	}

	public String getHgvsOffset() {
		return hgvsOffset;
	}

	public void setHgvsOffset(String hgvsOffset) {
		this.hgvsOffset = hgvsOffset;
	}

	public String getPheno() {
		return pheno;
	}

	public void setPheno(String pheno) {
		this.pheno = pheno;
	}

	public String getMinimised() {
		return minimised;
	}

	public void setMinimised(String minimised) {
		this.minimised = minimised;
	}

	public String getExacAf() {
		return exacAf;
	}

	public void setExacAf(String exacAf) {
		this.exacAf = exacAf;
	}

	public String getExacAfAFR() {
		return exacAfAFR;
	}

	public void setExacAfAFR(String exacAfAFR) {
		this.exacAfAFR = exacAfAFR;
	}

	public String getExacAfAMR() {
		return exacAfAMR;
	}

	public void setExacAfAMR(String exacAfAMR) {
		this.exacAfAMR = exacAfAMR;
	}

	public String getExacAfEAS() {
		return exacAfEAS;
	}

	public void setExacAfEAS(String exacAfEAS) {
		this.exacAfEAS = exacAfEAS;
	}

	public String getExacAfFIN() {
		return exacAfFIN;
	}

	public void setExacAfFIN(String exacAfFIN) {
		this.exacAfFIN = exacAfFIN;
	}

	public String getExacAfNFE() {
		return exacAfNFE;
	}

	public void setExacAfNFE(String exacAfNFE) {
		this.exacAfNFE = exacAfNFE;
	}

	public String getExacAfOTH() {
		return exacAfOTH;
	}

	public void setExacAfOTH(String exacAfOTH) {
		this.exacAfOTH = exacAfOTH;
	}

	public String getExacAfSAS() {
		return exacAfSAS;
	}

	public void setExacAfSAS(String exacAfSAS) {
		this.exacAfSAS = exacAfSAS;
	}

	public String getGenePheno() {
		return genePheno;
	}

	public void setGenePheno(String genePheno) {
		this.genePheno = genePheno;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFlankingBps() {
		return flankingBps;
	}

	public void setFlankingBps(String flankingBps) {
		this.flankingBps = flankingBps;
	}

	public String getVariantId() {
		return variantId;
	}

	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	public String getVariantQual() {
		return variantQual;
	}

	public void setVariantQual(String variantQual) {
		this.variantQual = variantQual;
	}

	public String getExacAfAdj() {
		return exacAfAdj;
	}

	public void setExacAfAdj(String exacAfAdj) {
		this.exacAfAdj = exacAfAdj;
	}

	public String getExacAcAnAdj() {
		return exacAcAnAdj;
	}

	public void setExacAcAnAdj(String exacAcAnAdj) {
		this.exacAcAnAdj = exacAcAnAdj;
	}

	public String getExacAcAn() {
		return exacAcAn;
	}

	public void setExacAcAn(String exacAcAn) {
		this.exacAcAn = exacAcAn;
	}

	public String getExacAcAnAfr() {
		return exacAcAnAfr;
	}

	public void setExacAcAnAfr(String exacAcAnAfr) {
		this.exacAcAnAfr = exacAcAnAfr;
	}

	public String getExacAcAnAmr() {
		return exacAcAnAmr;
	}

	public void setExacAcAnAmr(String exacAcAnAmr) {
		this.exacAcAnAmr = exacAcAnAmr;
	}

	public String getExacAcAnEas() {
		return exacAcAnEas;
	}

	public void setExacAcAnEas(String exacAcAnEas) {
		this.exacAcAnEas = exacAcAnEas;
	}

	public String getExacAcAnFin() {
		return exacAcAnFin;
	}

	public void setExacAcAnFin(String exacAcAnFin) {
		this.exacAcAnFin = exacAcAnFin;
	}

	public String getExacAcAnNfe() {
		return exacAcAnNfe;
	}
	
	
	
	
	
	
	
	
	
	

	public void setExacAcAnNfe(String exacAcAnNfe) {
		this.exacAcAnNfe = exacAcAnNfe;
	}

	public String getExacAcAnOth() {
		return exacAcAnOth;
	}

	public void setExacAcAnOth(String exacAcAnOth) {
		this.exacAcAnOth = exacAcAnOth;
	}

	public String getExacAcAnSas() {
		return exacAcAnSas;
	}

	public void setExacAcAnSas(String exacAcAnSas) {
		this.exacAcAnSas = exacAcAnSas;
	}

	public String getExacfilter() {
		return exacfilter;
	}

	public void setExacfilter(String exacfilter) {
		this.exacfilter = exacfilter;
	}

	public String getGenieVersion() {
		return genieVersion;
	}

	public void setGenieVersion(String genieVersion) {
		this.genieVersion = genieVersion;
	}
    
    


    
    
    
    
    

}
