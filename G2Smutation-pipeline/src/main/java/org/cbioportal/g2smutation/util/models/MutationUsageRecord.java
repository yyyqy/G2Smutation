package org.cbioportal.g2smutation.util.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Model mutationResult for return results containing Map of Mutation_ID, PDB residue
 * 
 * @author Juexin Wang
 *
 */
public class MutationUsageRecord implements Serializable{
    
    //private HashMap<String,List<Integer>> genomicCoorHm; //key:chr_start_end, value: list of MUTATION_ID
    //private HashMap<String, String> mutationHm;  //key:MUTATION_NO(SeqID_Index), value: chr_start_end
    private HashMap<Integer, String> mutationIdHm;//key:MUTATION_ID, value: chr_start_end
    private HashMap<String, List<Integer>> mutationIdRHm;//key:chr_pos, value: List of MUTATION_ID //reverse mutationIdHm key and value 
    private HashMap<Integer, String> residueHm;  //key:MUTATION_ID, value:XXXX_Chain_INDEX
    private HashMap<Integer, String> mutationNoIdRHm; //key:MUTATION_ID, value: MUTATION_NO
    
    public HashMap<Integer, String> getMutationIdHm() {
        return mutationIdHm;
    }
    public void setMutationIdHm(HashMap<Integer, String> mutationIdHm) {
        this.mutationIdHm = mutationIdHm;
    }
    public HashMap<String, List<Integer>> getMutationIdRHm() {
        return mutationIdRHm;
    }
    public void setMutationIdRHm(HashMap<String, List<Integer>> mutationIdRHm) {
        this.mutationIdRHm = mutationIdRHm;
    }
    public HashMap<Integer, String> getResidueHm() {
        return residueHm;
    }
    public void setResidueHm(HashMap<Integer, String> residueHm) {
        this.residueHm = residueHm;
    }
    public HashMap<Integer, String> getMutationNoIdRHm() {
        return mutationNoIdRHm;
    }
    public void setMutationNoIdRHm(HashMap<Integer, String> mutationNoIdRHm) {
        this.mutationNoIdRHm = mutationNoIdRHm;
    }
     

}
