package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table dbsnp_entry 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "dbsnp_entry")
public class Dbsnp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "CHR_POS")
    private String chrPos;
    
    @Column(name = "MUTATION_NO")
    private String mutationNo;
    
    @Column(name = "RS_ID")
    private int rsId;

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

	public int getRsId() {
        return rsId;
    }

    public void setRsId(int rsId) {
        this.rsId = rsId;
    }

    
    
}
