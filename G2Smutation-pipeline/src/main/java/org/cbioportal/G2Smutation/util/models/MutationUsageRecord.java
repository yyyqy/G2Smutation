package org.cbioportal.G2Smutation.util.models;

import java.util.HashMap;
import java.util.List;

/**
 * Model mutationResult for return results containing Map of Mutation_ID, PDB residue
 * 
 * @author Juexin Wang
 *
 */
public class MutationUsageRecord {
    
    //private HashMap<String,List<Integer>> genomicCoorHm; //key:chr_start_end, value: list of MUTATION_ID
    //private HashMap<String, String> mutationHm;  //key:MUTATION_NO(SeqID_Index), value: chr_start_end
    private HashMap<Integer, String> mutationIdHm;//key:MUTATION_ID, value: chr_start_end
    private HashMap<Integer, String> residueHm;  //key:MUTATION_ID, value:XXXX_Chain_INDEX
    
    public HashMap<Integer, String> getMutationIdHm() {
        return mutationIdHm;
    }
    public void setMutationIdHm(HashMap<Integer, String> mutationIdHm) {
        this.mutationIdHm = mutationIdHm;
    }
    public HashMap<Integer, String> getResidueHm() {
        return residueHm;
    }
    public void setResidueHm(HashMap<Integer, String> residueHm) {
        this.residueHm = residueHm;
    }
    
    
    
    
    

    
    
    

    
    
    

}
