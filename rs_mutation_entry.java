package org.cbioportal.G2Smutation.web.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//This tells Hibernate to make a table out of this class
@Entity
public class rs_mutation_entry {
	@Id	
    @GeneratedValue(strategy=GenerationType.AUTO)
	
	@Column(name = "ID")
    public Integer rsId;
	
	@Column(name = "RS_SNP_ID")
    private Integer rsSnpId;
	
	@Column(name = "SEQ_ID")
	public String seqId;
	
	@Column(name = "SEQ_INDEX")
	public String seqIndex;
	
	@Column(name = "SEQ_RESIDUE")
	public String seqResidue;
	
	@Column(name = "PDB_NO")
	public String pdbNo;
	
	@Column(name = "PDB_INDEX")
	public Integer pdbIndex;
	
	@Column(name = "PDB_RESIDUE")
	public String pdbResidue;
	
	@Column(name = "ALIGNMENT_ID")
    public Integer alignmentId;


    public rs_mutation_entry(){}

    
    public Integer getId() {
        return rsId;
    }
    
    public Integer getRsSnpId() {
        return rsSnpId;
    }
    
    public String getSeqId() {
        return seqId;
    }
    
    public String getseqIndex() {
        return seqIndex;
    }
    
    public String getseqResidue() {
        return seqResidue;
    }
    
    public String getpdbNo() {
        return pdbNo;
    }
    
    public Integer getpdbIndex() {
        return pdbIndex;
    }

    public String getpdbResidue() {
        return pdbResidue;
    }

    public Integer getalignmentId() {
        return alignmentId;
    }

}
