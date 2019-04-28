package org.cbioportal.G2Smutation.web.models.mutation;

/**
 * Model of PDB Residue Mapped to widetype protein 
 * 
 * @author Juexin Wang
 *
 */
public class MutatedResidue {
    
    private String pdbNo;  //PDB_NO, Be careful! only PDB and chain
    private int pdbPos; //PDB_INDEX
    private String pdbResidue; //PDB_RESIDUE
    
    public String getPdbNo() {
        return pdbNo;
    }
    public void setPdbNo(String pdbNo) {
        this.pdbNo = pdbNo;
    }
    public int getPdbPos() {
        return pdbPos;
    }
    public void setPdbPos(int pdbPos) {
        this.pdbPos = pdbPos;
    }
    public String getPdbResidue() {
        return pdbResidue;
    }
    public void setPdbResidue(String pdbResidue) {
        this.pdbResidue = pdbResidue;
    }

}
