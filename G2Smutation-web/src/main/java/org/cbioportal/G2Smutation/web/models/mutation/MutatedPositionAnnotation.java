package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

public class MutatedPositionAnnotation {
    
    private int proteinPos;  //SEQ_INDEX
    private String proteinResidue; //SEQ_RESIDUE
    
    private List<MutatedResidueAnnotation> mutatedResidueAnnotation; //PDB_NO, PDB_INDEX

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

    public List<MutatedResidueAnnotation> getMutatedResidueAnnotation() {
        return mutatedResidueAnnotation;
    }

    public void setMutatedResidueAnnotation(List<MutatedResidueAnnotation> mutatedResidueAnnotation) {
        this.mutatedResidueAnnotation = mutatedResidueAnnotation;
    }

    

}
