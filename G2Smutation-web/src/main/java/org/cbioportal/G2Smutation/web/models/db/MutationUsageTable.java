package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table mutation_usage_table 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "mutation_usage_table")
public class MutationUsageTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MUTATION_ID")
    private int mutationId;
    
    @Column(name = "MUTATION_NO")
    private String mutationNo;
    
    @Column(name = "SEQ_ID")
    private int seqId;
    
    @Column(name = "SEQ_NAME")
    private String seqName;
    
    @Column(name = "SEQ_INDEX")
    private int seqIndex;
    
    @Column(name = "SEQ_RESIDUE")
    private String seqResidue;
    
    @Column(name = "PDB_NO")
    private String pdbNo;
    
    @Column(name = "PDB_INDEX")
    private int pdbIndex;
    
    @Column(name = "PDB_RESIDUE")
    private String pdbResidue;
    
    @Column(name = "ALIGNMENT_ID")
    private int alignmentId;
    
    @Column(name = "IDENTITY")
    private float identity;
    
    @Column(name = "IDENTITYP")
    private float identityP;
    
    @Column(name = "EVALUE")
    private String evalue;
    
    @Column(name = "BITSCORE")
    private float bitscore;
    
    @Column(name = "ALIGNLENGTH")
    private int alignLength;
    
    @Column(name = "UPDATE_DATE")
    private String updateDate;

    
    public int getMutationId() {
        return mutationId;
    }

    public void setMutationId(int mutationId) {
        this.mutationId = mutationId;
    }

    public String getMutationNo() {
        return mutationNo;
    }

    public void setMutationNo(String mutationNo) {
        this.mutationNo = mutationNo;
    }

    public int getSeqId() {
        return seqId;
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

    public int getSeqIndex() {
        return seqIndex;
    }

    public void setSeqIndex(int seqIndex) {
        this.seqIndex = seqIndex;
    }

    public String getSeqResidue() {
        return seqResidue;
    }

    public void setSeqResidue(String seqResidue) {
        this.seqResidue = seqResidue;
    }

    public String getPdbNo() {
        return pdbNo;
    }

    public void setPdbNo(String pdbNo) {
        this.pdbNo = pdbNo;
    }

    public int getPdbIndex() {
        return pdbIndex;
    }

    public void setPdbIndex(int pdbIndex) {
        this.pdbIndex = pdbIndex;
    }

    public String getPdbResidue() {
        return pdbResidue;
    }

    public void setPdbResidue(String pdbResidue) {
        this.pdbResidue = pdbResidue;
    }

    public int getAlignmentId() {
        return alignmentId;
    }

    public void setAlignmentId(int alignmentId) {
        this.alignmentId = alignmentId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

	public int getAlignLength() {
		return alignLength;
	}

	public void setAlignLength(int alignLength) {
		this.alignLength = alignLength;
	}
    
	
	
    

}
