package org.cbioportal.G2Smutation.util.models;



/**
 *
 * @author Juexin Wang
 *
 */

public class Alignment {
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

    private String evalue;

    private float bitscore;

    private float identity;

    private float identityPositive;

    private String seqAlign;

    private String pdbAlign;

    private String midlineAlign;

    private String updateDate;

    // ------------------------
    // Constructors
    // ------------------------

    public Alignment() {
    }

    public Alignment(int alignmentid) {
        this.alignmentId = alignmentid;
    }

    // ------------------------
    // Methods
    // ------------------------

    public int getAlignmentId() {
        return alignmentId;
    }

    public String getSegStart() {
        return segStart;
    }

    public void setSegStart(String segStart) {
        this.segStart = segStart;
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

    public String getEvalue() {
        return evalue;
    }

    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }

    public float getBitscore() {
        return bitscore;
    }

    public void setBitscore(float bitscore) {
        this.bitscore = bitscore;
    }

    public float getIdentity() {
        return identity;
    }

    public void setIdentity(float identity) {
        this.identity = identity;
    }

    public float getIdentityPositive() {
        return identityPositive;
    }

    public void setIdentityPositive(float identityPositive) {
        this.identityPositive = identityPositive;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
