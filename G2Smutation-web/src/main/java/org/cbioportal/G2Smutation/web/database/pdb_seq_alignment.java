package org.cbioportal.G2Smutation.web.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
//@Table(name="pdb_seq_alignment")
//@SecondaryTable(name="mutation_usage_table")
public class pdb_seq_alignment {
	
	@Id	
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ALIGNMENT_ID")
    public Integer alignmentId;
	
	@Column(name = "SEQ_ALIGN")
	public String seqAlign;
	
	@Column(name = "EVALUE")
	public Double evalue;
	
	@Column(name = "BITSCORE")
	public Double bitscore;
	
	@Column(name = "IDENTITY")
	public Integer identity;
	
	@Column(name = "IDENTP")
	public double identp;
	
	/*
	@Column(name = "MUTATION_ID", table="mutation_usage_table")
    public Integer mutationId;
	
	@Column(name = "MUTATION_NO", table="mutation_usage_table")
    public String mutationNo;
	
	// SEQ
	@Column(name = "SEQ_ID", table="mutation_usage_table")
    public Integer seqId;
	
	@Column(name = "SEQ_NAME", table="mutation_usage_table")
	public String seqName;
	
	@Column(name = "SEQ_INDEX", table="mutation_usage_table")
	public Integer seqIndex;
	
	@Column(name = "SEQ_RESIDUE", table="mutation_usage_table")
	public String seqResidue;
	
	// PDB
	@Column(name = "PDB_NO", table="mutation_usage_table")
	public String pdbNo;
	
	@Column(name = "PDB_INDEX", table="mutation_usage_table")
	public Integer pdbIndex;
	
	@Column(name = "PDB_RESIDUE", table="mutation_usage_table")
	public String pdbResidue;
	*/
	

    public pdb_seq_alignment(){}

    
    public Double getevalue() {
        return evalue;
    }
    
    public Double getbitscore() {
        return bitscore;
    }
    
    public Integer getidentity() {
        return identity;
    }
    
    public double getidentp() {
        return identp;
    }
    
    public String getseqAlign() {
        return seqAlign;
    }
    
    public Integer getalignLength() {
        return seqAlign.length();
    }
    
    public Integer getalignmentId() {
        return alignmentId;
    }
    
    /*
    public Integer getmutationId() {
        return mutationId;
    }
    
    public String getmutationNo() {
        return mutationNo;
    }
    
    public Integer getseqId() {
        return seqId;
    }
    
    public String getseqName() {
        return seqName;
    }
    
    public Integer getseqIndex() {
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
	*/
}