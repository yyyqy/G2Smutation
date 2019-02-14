package org.cbioportal.G2Smutation.web.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

/**
 * Model of mutation_usage_table
 * @author Juexin Wang
 *
 */
@Entity
@Table(name="mutation_usage_table")
@SecondaryTable(name="pdb_seq_alignment")
public class mutation_usage_table {	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ALIGNMENT_ID")
    public Integer alignmentId;
	
	@Column(name = "MUTATION_ID")
    public Integer mutationId;
	
	@Column(name = "MUTATION_NO")
    public String mutationNo;
	
	// SEQ
	@Column(name = "SEQ_ID")
    public Integer seqId;
	
	@Column(name = "SEQ_NAME")
	public String seqName;
	
	@Column(name = "SEQ_INDEX")
	public Integer seqIndex;
	
	@Column(name = "SEQ_RESIDUE")
	public String seqResidue;
	
	// PDB
	@Column(name = "PDB_NO")
	public String pdbNo;
	
	@Column(name = "PDB_INDEX")
	public Integer pdbIndex;
	
	@Column(name = "PDB_RESIDUE")
	public String pdbResidue;
	
	//@Column(name = "UPDATE_DATE")
    //public Integer updateDate;
	

	// EVALUE、BITSCORE、IDENTITY、IDENTP
	@Column(name = "SEQ_ALIGN", table="pdb_seq_alignment")
	public String seqAlign;
	
	@Column(name = "EVALUE", table="pdb_seq_alignment")
	public Integer evalue;
	
	@Column(name = "BITSCORE", table="pdb_seq_alignment")
	public Double bitscore;
	
	@Column(name = "IDENTITY", table="pdb_seq_alignment")
	public Integer identity;
	
	@Column(name = "IDENTP", table="pdb_seq_alignment")
	public double identp;
	
	public Integer getevalue() {
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

    public mutation_usage_table(){}
    
    
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

    public Integer getalignmentId() {
        return alignmentId;
    }
}
