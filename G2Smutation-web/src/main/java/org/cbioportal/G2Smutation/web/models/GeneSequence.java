package org.cbioportal.G2Smutation.web.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model of Gene Sequence,only contains SEQ_ID now
 * TODO: Add seq name and sequence information in pipeline
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "seq_entry")
public class GeneSequence {
    @Id
    @Column(name = "SEQ_ID")
    private String seqId;

    // ------------------------
    // Constructors
    // ------------------------

    public GeneSequence() {
    }

    public GeneSequence(String seqId) {
        this.seqId = seqId;
    }

    // ------------------------
    // Methods
    // ------------------------

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

}
