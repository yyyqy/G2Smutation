package org.cbioportal.G2Smutation.web.models.mutation;

import org.cbioportal.G2Smutation.web.models.Alignment;

/**
* Model of PDB Residue Mapped to widetype protein, with details of updateDate and alignment from original G2S
* 
* @author Juexin Wang
*
*/

public class MutatedResidueAnnotationDetail extends MutatedResidueAnnotation {
    private String updateDate; //UPDATE_DATE
    private Alignment alignment; //linked from ALIGNMENT_ID
    
    public String getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public Alignment getAlignment() {
        return alignment;
    }
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }    

}
