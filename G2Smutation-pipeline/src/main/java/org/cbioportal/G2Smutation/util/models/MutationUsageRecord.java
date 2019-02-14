package org.cbioportal.G2Smutation.util.models;

import java.util.HashMap;
import java.util.List;

/**
 * Model mutationResult for return results containing genomic Coordinates and List of PDB residue
 * 
 * @author Juexin Wang
 *
 */
public class MutationUsageRecord {
    
    private HashMap<String,String> genomicCoorHm;
    private List<String> residueList;
    
    public HashMap<String, String> getGenomicCoorHm() {
        return genomicCoorHm;
    }
    public void setGenomicCoorHm(HashMap<String, String> genomicCoorHm) {
        this.genomicCoorHm = genomicCoorHm;
    }
    public List<String> getResidueList() {
        return residueList;
    }
    public void setResidueList(List<String> residueList) {
        this.residueList = residueList;
    }
    
    
    

}
