package org.cbioportal.g2smutation.util.models;

/**
 * Model in order to use mutation record
 * 
 * @author wangjue
 *
 */
public class MutationRecord {

    private String mutationNo;
    // Inner seqId
    private int seqId;

    private String seqName;

    private int seqResidueIndex;

    private String seqResidueName;

    private String pdbNo;

    private int pdbResidueIndex;

    private String pdbResidueName;
    
    private float identity;
    
    private float identityP;

    // alignmentId: id of Alignment
    private int alignmentId;

    /*
     * getter and setter methods
     */

    
    public int getSeqId() {
        return seqId;
    }

    public float getIdentity() {
		return identity;
	}

	public void setIdentity(float identity) {
		this.identity = identity;
	}

	public float getIdentityP() {
		return identityP;
	}

	public void setIdentityP(float identityP) {
		this.identityP = identityP;
	}

	public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
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

    public String getMutationNo() {
        return mutationNo;
    }

    public void setMutationNo(String mutation_No) {
        this.mutationNo = mutation_No;
    }

}
