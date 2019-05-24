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
    
    private List<MutatedPosition> mutatedPosition;

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public List<MutatedPosition> getMutatedPosition() {
        return mutatedPosition;
    }

    public void setMutatedPosition(List<MutatedPosition> mutatedPosition) {
        this.mutatedPosition = mutatedPosition;
    }
    
    

    
    

}
