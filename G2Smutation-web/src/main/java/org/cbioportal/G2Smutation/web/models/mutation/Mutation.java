package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

/**
 * Model of Mutation information of Residue Mapping
 * 
 * @author Juexin Wang
 *
 */

public class Mutation {
    
    //MUTATION_NO and MUTATION_ID is not here
    private String proteinName; //SEQ_NAME
    private int proteinPos;  //SEQ_INDEX
    private String proteinResidue; //SEQ_RESIDUE
    
    private List<MutatedResidue> mutatedResidue; //PDB_NO, PDB_INDEX

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public int getProteinPos() {
        return proteinPos;
    }

    public void setProteinPos(int proteinPos) {
        this.proteinPos = proteinPos;
    }

    public List<MutatedResidue> getMutatedResidue() {
        return mutatedResidue;
    }

    public void setMutatedResidue(List<MutatedResidue> mutatedResidue) {
        this.mutatedResidue = mutatedResidue;
    }

    public String getProteinResidue() {
        return proteinResidue;
    }

    public void setProteinResidue(String proteinResidue) {
        this.proteinResidue = proteinResidue;
    }
    
    

}
