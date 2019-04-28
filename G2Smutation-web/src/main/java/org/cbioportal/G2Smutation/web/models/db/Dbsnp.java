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
    
    @Column(name = "MUTATION_ID")
    private int mutationId;
    
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

    public int getMutationId() {
        return mutationId;
    }

    public void setMutationId(int mutationId) {
        this.mutationId = mutationId;
    }

    public int getRsId() {
        return rsId;
    }

    public void setRsId(int rsId) {
        this.rsId = rsId;
    }

    
    
}
