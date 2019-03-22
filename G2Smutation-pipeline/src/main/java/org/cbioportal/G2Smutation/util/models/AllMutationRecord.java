package org.cbioportal.G2Smutation.util.models;

/**
 * Model of All mutation record, aggregate of 5 types
 * 
 * @author Juexin Wang
 *
 */
public class AllMutationRecord {
    private String chr_pos;
    // chr_posstart

    // private String annotation_type_id;
    // AnnotationType:ID; e.g.dbsnp:12345;clnvar:54321

    // Inner seqId
    private int seqId;

    private int seqResidueIndex;

    private String seqResidueName;

    private String pdbNo;

    private int pdbResidueIndex;

    private String pdbResidueName;

    // alignmentId: id of Alignment
    private int alignmentId;

    public String getChr_pos() {
        return chr_pos;
    }

    public void setChr_pos(String chr_pos) {
        this.chr_pos = chr_pos;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public int getSeqResidueIndex() {
        return seqResidueIndex;
    }

    public void setSeqResidueIndex(int seqResidueIndex) {
        this.seqResidueIndex = seqResidueIndex;
    }

    public String getSeqResidueName() {
        return seqResidueName;
    }

    public void setSeqResidueName(String seqResidueName) {
        this.seqResidueName = seqResidueName;
    }

    public String getPdbNo() {
        return pdbNo;
    }

    public void setPdbNo(String pdbNo) {
        this.pdbNo = pdbNo;
    }

    public int getPdbResidueIndex() {
        return pdbResidueIndex;
    }

    public void setPdbResidueIndex(int pdbResidueIndex) {
        this.pdbResidueIndex = pdbResidueIndex;
    }

    public String getPdbResidueName() {
        return pdbResidueName;
    }

    public void setPdbResidueName(String pdbResidueName) {
        this.pdbResidueName = pdbResidueName;
    }

    public int getAlignmentId() {
        return alignmentId;
    }

    public void setAlignmentId(int alignmentId) {
        this.alignmentId = alignmentId;
    }

}
