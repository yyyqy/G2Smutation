package org.cbioportal.G2Smutation.web.models;

import java.util.List;

/**
 * Ensembl as the primary key for most of cases
 * 
 * @author wangjue
 *
 */

public class ResidueEnsembl {

    // Primary KEY: EnsemblId
    private String EnsemblId;

    private List<Residue> residues;

    public String getEnsemblId() {
        return EnsemblId;
    }

    public void setEnsemblId(String ensemblId) {
        EnsemblId = ensemblId;
    }

    public List<Residue> getResidues() {
        return residues;
    }

    public void setResidues(List<Residue> residues) {
        this.residues = residues;
    }

}
