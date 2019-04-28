package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table cosmic_entry 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "cosmic_entry")
public class Cosmic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "CHR_POS")
    private String chrPos;
    
    @Column(name = "MUTATION_ID")
    private int mutationId;
    
    @Column(name = "GENE_NAME")
    private String geneName;
    
    @Column(name = "ACCESSION_NUMBER")
    private String accessionNumber;
    
    @Column(name = "GENE_CDS_LENGTH")
    private int geneCDSLength;
    
    @Column(name = "HGNC_ID")
    private String hgncId;
    
    @Column(name = "SAMPLE_NAME")
    private String sampleName;
    
    @Column(name = "ID_SAMPLE")
    private int idSample;
    
    @Column(name = "ID_TUMOUR")
    private int idTumour;
    
    @Column(name = "PRIMARY_SITE")
    private String primarySite;
    
    @Column(name = "SITE_SUBTYPE_1")
    private String siteSubtype1;
    
    @Column(name = "SITE_SUBTYPE_2")
    private String siteSubtype2;
    
    @Column(name = "SITE_SUBTYPE_3")
    private String siteSubtype3;
    
    @Column(name = "PRIMARY_HISTOLOGY")
    private String primaryHistology;
    
    @Column(name = "HISTOLOGY_SUBTYPE_1")
    private String histologySubtype1;
    
    @Column(name = "HISTOLOGY_SUBTYPE_2")
    private String histologySubtype2;
    
    @Column(name = "HISTOLOGY_SUBTYPE_3")
    private String histologySubtype3;
    
    @Column(name = "GENOME-WIDE_SCREEN")
    private String genomewideScreen;
    
    @Column(name = "COSMIC_MUTATION_ID")
    private String cosmicMutationId;
    
    @Column(name = "MUTATION_CDS")
    private String mutationCDS;
    
    @Column(name = "MUTATION_AA")
    private String mutationAA;
    
    @Column(name = "MUTATION_DESCRIPTION")
    private String mutationDescription;
    
    @Column(name = "MUTATION_ZYGOSITY")
    private String mutationZygosity;
    
    @Column(name = "LOH")
    private String loh;
    
    @Column(name = "GRCH")
    private int grch;
    
    @Column(name = "MUTATION_GENOME_POSITION")
    private String mutationGenomePosition;
    
    @Column(name = "MUTATION_STRAND")
    private String mutationStrand;
    
    @Column(name = "SNP")
    private String snp;
    
    @Column(name = "RESISTANCE_MUTATION")
    private String resistanceMutation;
    
    @Column(name = "FATHMM_PREDICTION")
    private String fathmmPrediction;
    
    @Column(name = "FATHMM_SCORE")
    private String fathmmScore;
    
    @Column(name = "MUTATION_SOMATIC_STATUS")
    private String mutationSomaticStatus;
    
    @Column(name = "PUBMED_PMID")
    private String pubmedPMID;
    
    @Column(name = "ID_STUDY")
    private String idStudy;
    
    @Column(name = "SAMPLE_TYPE")
    private String sampleType;
    
    @Column(name = "TUMOUR_ORIGIN")
    private String tumourOrigin;
    
    @Column(name = "AGE")
    private String age;
    
    @Column(name = "COSMIC_VERSION")
    private String cosmicVersion;

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

    public int getMutationId() {
        return mutationId;
    }

    public void setMutationId(int mutationId) {
        this.mutationId = mutationId;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public int getGeneCDSLength() {
        return geneCDSLength;
    }

    public void setGeneCDSLength(int geneCDSLength) {
        this.geneCDSLength = geneCDSLength;
    }

    public String getHgncId() {
        return hgncId;
    }

    public void setHgncId(String hgncId) {
        this.hgncId = hgncId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getIdSample() {
        return idSample;
    }

    public void setIdSample(int idSample) {
        this.idSample = idSample;
    }

    public int getIdTumour() {
        return idTumour;
    }

    public void setIdTumour(int idTumour) {
        this.idTumour = idTumour;
    }

    public String getPrimarySite() {
        return primarySite;
    }

    public void setPrimarySite(String primarySite) {
        this.primarySite = primarySite;
    }

    public String getSiteSubtype1() {
        return siteSubtype1;
    }

    public void setSiteSubtype1(String siteSubtype1) {
        this.siteSubtype1 = siteSubtype1;
    }

    public String getSiteSubtype2() {
        return siteSubtype2;
    }

    public void setSiteSubtype2(String siteSubtype2) {
        this.siteSubtype2 = siteSubtype2;
    }

    public String getSiteSubtype3() {
        return siteSubtype3;
    }

    public void setSiteSubtype3(String siteSubtype3) {
        this.siteSubtype3 = siteSubtype3;
    }

    public String getPrimaryHistology() {
        return primaryHistology;
    }

    public void setPrimaryHistology(String primaryHistology) {
        this.primaryHistology = primaryHistology;
    }

    public String getHistologySubtype1() {
        return histologySubtype1;
    }

    public void setHistologySubtype1(String histologySubtype1) {
        this.histologySubtype1 = histologySubtype1;
    }

    public String getHistologySubtype2() {
        return histologySubtype2;
    }

    public void setHistologySubtype2(String histologySubtype2) {
        this.histologySubtype2 = histologySubtype2;
    }

    public String getHistologySubtype3() {
        return histologySubtype3;
    }

    public void setHistologySubtype3(String histologySubtype3) {
        this.histologySubtype3 = histologySubtype3;
    }

    public String getGenomewideScreen() {
        return genomewideScreen;
    }

    public void setGenomewideScreen(String genomewideScreen) {
        this.genomewideScreen = genomewideScreen;
    }

    public String getCosmicMutationId() {
        return cosmicMutationId;
    }

    public void setCosmicMutationId(String cosmicMutationId) {
        this.cosmicMutationId = cosmicMutationId;
    }

    public String getMutationCDS() {
        return mutationCDS;
    }

    public void setMutationCDS(String mutationCDS) {
        this.mutationCDS = mutationCDS;
    }

    public String getMutationAA() {
        return mutationAA;
    }

    public void setMutationAA(String mutationAA) {
        this.mutationAA = mutationAA;
    }

    public String getMutationDescription() {
        return mutationDescription;
    }

    public void setMutationDescription(String mutationDescription) {
        this.mutationDescription = mutationDescription;
    }

    public String getMutationZygosity() {
        return mutationZygosity;
    }

    public void setMutationZygosity(String mutationZygosity) {
        this.mutationZygosity = mutationZygosity;
    }

    public String getLoh() {
        return loh;
    }

    public void setLoh(String loh) {
        this.loh = loh;
    }

    public int getGrch() {
        return grch;
    }

    public void setGrch(int grch) {
        this.grch = grch;
    }

    public String getMutationGenomePosition() {
        return mutationGenomePosition;
    }

    public void setMutationGenomePosition(String mutationGenomePosition) {
        this.mutationGenomePosition = mutationGenomePosition;
    }

    public String getMutationStrand() {
        return mutationStrand;
    }

    public void setMutationStrand(String mutationStrand) {
        this.mutationStrand = mutationStrand;
    }

    public String getSnp() {
        return snp;
    }

    public void setSnp(String snp) {
        this.snp = snp;
    }

    public String getResistanceMutation() {
        return resistanceMutation;
    }

    public void setResistanceMutation(String resistanceMutation) {
        this.resistanceMutation = resistanceMutation;
    }

    public String getFathmmPrediction() {
        return fathmmPrediction;
    }

    public void setFathmmPrediction(String fathmmPrediction) {
        this.fathmmPrediction = fathmmPrediction;
    }

    public String getFathmmScore() {
        return fathmmScore;
    }

    public void setFathmmScore(String fathmmScore) {
        this.fathmmScore = fathmmScore;
    }

    public String getMutationSomaticStatus() {
        return mutationSomaticStatus;
    }

    public void setMutationSomaticStatus(String mutationSomaticStatus) {
        this.mutationSomaticStatus = mutationSomaticStatus;
    }

    public String getPubmedPMID() {
        return pubmedPMID;
    }

    public void setPubmedPMID(String pubmedPMID) {
        this.pubmedPMID = pubmedPMID;
    }

    public String getIdStudy() {
        return idStudy;
    }

    public void setIdStudy(String idStudy) {
        this.idStudy = idStudy;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getTumourOrigin() {
        return tumourOrigin;
    }

    public void setTumourOrigin(String tumourOrigin) {
        this.tumourOrigin = tumourOrigin;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCosmicVersion() {
        return cosmicVersion;
    }

    public void setCosmicVersion(String cosmicVersion) {
        this.cosmicVersion = cosmicVersion;
    }
    
    
    
    
    
    

}
