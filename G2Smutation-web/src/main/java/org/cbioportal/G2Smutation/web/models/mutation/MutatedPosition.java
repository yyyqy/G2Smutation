package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

public class MutatedPosition {
    
    private int proteinPos;  //SEQ_INDEX
    private String proteinResidue; //SEQ_RESIDUE
    
    private List<MutatedResidue> mutatedResidue; //PDB_NO, PDB_INDEX

    public int getProteinPos() {
        return proteinPos;
    }

    public void setProteinPos(int proteinPos) {
        this.proteinPos = proteinPos;
    }

    public String getProteinResidue() {
        return proteinResidue;
    }

    public void setProteinResidue(String proteinResidue) {
        this.proteinResidue = proteinResidue;
    }

    public List<MutatedResidue> getMutatedResidue() {
        return mutatedResidue;
    }

    public void setMutatedResidue(List<MutatedResidue> mutatedResidue) {
        this.mutatedResidue = mutatedResidue;
    }
    
    

}
