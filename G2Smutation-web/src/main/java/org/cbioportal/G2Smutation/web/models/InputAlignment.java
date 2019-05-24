package org.cbioportal.G2Smutation.web.models;

/**
 * This is for Input Web Interfaces
 * 
 * @author wangjue
 *
 */
public class InputAlignment {

    private int alignmentId;

    private String pdbNo;

    private String pdbId;

    private String chain;

    private String pdbSeg;

    private String segStart;

    private String seqId;

    private int pdbFrom;

    private int pdbTo;

    private int seqFrom;

    private int seqTo;

    private double evalue;

    private double bitscore;

    private int identity;

    private int identp;

    private String seqAlign;

    private String pdbAlign;

    private String midlineAlign;

    // Blast Version and Results;
    private String blast_version;

    private String blast_reference;

    // Results related statistics;
    private String blast_dblen;

    private String blast_dbnum;

    private String blast_effspace;

    private String blast_entropy;

    private String blast_hsplen;

    private String blast_kappa;

    private String blast_lambda;

    // Other parameters and output statistics
    // default 1e-10
    // different with evalue got
    private String paraEvalue;

    // 2,3,6
    // default 3
    private String word_size;

    // default 11
    private String gapopen;

    // default 1
    private String gapextend;

    // default BLOSUM62
    private String matrix;

    // 0,1,2,3 default 2
    private String comp_based_stats;

    // default 11
    // Neighboring words threshold
    private String threshold;

    // default 40
    // Window for multiple hits
    private String window_size;

    private String timenow;

    // For input
    private String sequence;
    private String inputResidueNum;

    // For Percentage
    private String identityPercentage;
    private String positivePercentage;
    private String gapPercentage;
    private int gap;
    private int length;
    private String identityPercentageStr;
    private String positivePercentageStr;
    private String gapPercentageStr;

    // ------------------------
    // Constructors
    // ------------------------

    public InputAlignment() {
    }

    public InputAlignment(int alignmentid) {
        this.alignmentId = alignmentid;
    }

    // Set and get

    public String getParaEvalue() {
        return paraEvalue;
    }

    public String getInputResidueNum() {
        return inputResidueNum;
    }

    public void setInputResidueNum(String inputResidueNum) {
        this.inputResidueNum = inputResidueNum;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSegStart() {
        return segStart;
    }

    public void setSegStart(String segStart) {
        this.segStart = segStart;
    }

    public String getTimenow() {
        return timenow;
    }

    public void setTimenow(String timenow) {
        this.timenow = timenow;
    }

    public void setParaEvalue(String paraEvalue) {
        this.paraEvalue = paraEvalue;
    }

    public String getBlast_version() {
        return blast_version;
    }

    public void setBlast_version(String blast_version) {
        this.blast_version = blast_version;
    }

    public String getBlast_reference() {
        return blast_reference;
    }

    public void setBlast_reference(String blast_reference) {
        this.blast_reference = blast_reference;
    }

    public String getBlast_dblen() {
        return blast_dblen;
    }

    public void setBlast_dblen(String blast_dblen) {
        this.blast_dblen = blast_dblen;
    }

    public String getBlast_dbnum() {
        return blast_dbnum;
    }

    public void setBlast_dbnum(String blast_dbnum) {
        this.blast_dbnum = blast_dbnum;
    }

    public String getBlast_effspace() {
        return blast_effspace;
    }

    public void setBlast_effspace(String blast_effspace) {
        this.blast_effspace = blast_effspace;
    }

    public String getBlast_entropy() {
        return blast_entropy;
    }

    public void setBlast_entropy(String blast_entropy) {
        this.blast_entropy = blast_entropy;
    }

    public String getBlast_hsplen() {
        return blast_hsplen;
    }

    public void setBlast_hsplen(String blast_hsplen) {
        this.blast_hsplen = blast_hsplen;
    }

    public String getBlast_kappa() {
        return blast_kappa;
    }

    public void setBlast_kappa(String blast_kappa) {
        this.blast_kappa = blast_kappa;
    }

    public String getBlast_lambda() {
        return blast_lambda;
    }

    public void setBlast_lambda(String blast_lambda) {
        this.blast_lambda = blast_lambda;
    }

    public String getWord_size() {
        return word_size;
    }

    public void setWord_size(String word_size) {
        this.word_size = word_size;
    }

    public String getGapopen() {
        return gapopen;
    }

    public void setGapopen(String gapopen) {
        this.gapopen = gapopen;
    }

    public String getGapextend() {
        return gapextend;
    }

    public void setGapextend(String gapextend) {
        this.gapextend = gapextend;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getComp_based_stats() {
        return comp_based_stats;
    }

    public void setComp_based_stats(String comp_based_stats) {
        this.comp_based_stats = comp_based_stats;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getWindow_size() {
        return window_size;
    }

    public void setWindow_size(String window_size) {
        this.window_size = window_size;
    }

    public int getAlignmentId() {
        return alignmentId;
    }

    public void setAlignmentId(int alignmentId) {
        this.alignmentId = alignmentId;
    }

    public String getPdbNo() {
        return pdbNo;
    }

    public void setPdbNo(String pdbNo) {
        this.pdbNo = pdbNo;
    }

    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getPdbSeg() {
        return pdbSeg;
    }

    public void setPdbSeg(String pdbSeg) {
        this.pdbSeg = pdbSeg;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public int getPdbFrom() {
        return pdbFrom;
    }

    public void setPdbFrom(int pdbFrom) {
        this.pdbFrom = pdbFrom;
    }

    public int getPdbTo() {
        return pdbTo;
    }

    public void setPdbTo(int pdbTo) {
        this.pdbTo = pdbTo;
    }

    public int getSeqFrom() {
        return seqFrom;
    }

    public void setSeqFrom(int seqFrom) {
        this.seqFrom = seqFrom;
    }

    public int getSeqTo() {
        return seqTo;
    }

    public void setSeqTo(int seqTo) {
        this.seqTo = seqTo;
    }

    public double getEvalue() {
        return evalue;
    }

    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    public double getBitscore() {
        return bitscore;
    }

    public void setBitscore(double bitscore) {
        this.bitscore = bitscore;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getIdentp() {
        return identp;
    }

    public void setIdentp(int identp) {
        this.identp = identp;
    }

    public String getSeqAlign() {
        return seqAlign;
    }

    public void setSeqAlign(String seqAlign) {
        this.seqAlign = seqAlign;
    }

    public String getPdbAlign() {
        return pdbAlign;
    }

    public void setPdbAlign(String pdbAlign) {
        this.pdbAlign = pdbAlign;
    }

    public String getMidlineAlign() {
        return midlineAlign;
    }

    public void setMidlineAlign(String midlineAlign) {
        this.midlineAlign = midlineAlign;
    }

    public String getIdentityPercentage() {
        return identityPercentage;
    }

    public void setIdentityPercentage(String identityPercentage) {
        this.identityPercentage = identityPercentage;
    }

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public String getGapPercentage() {
        return gapPercentage;
    }

    public void setGapPercentage(String gapPercentage) {
        this.gapPercentage = gapPercentage;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public String getIdentityPercentageStr() {
        return identityPercentageStr;
    }

    public void setIdentityPercentageStr(String identityPercentageStr) {
        this.identityPercentageStr = identityPercentageStr;
    }

    public String getPositivePercentageStr() {
        return positivePercentageStr;
    }

    public void setPositivePercentageStr(String positivePercentageStr) {
        this.positivePercentageStr = positivePercentageStr;
    }

    public String getGapPercentageStr() {
        return gapPercentageStr;
    }

    public void setGapPercentageStr(String gapPercentageStr) {
        this.gapPercentageStr = gapPercentageStr;
    }

}
