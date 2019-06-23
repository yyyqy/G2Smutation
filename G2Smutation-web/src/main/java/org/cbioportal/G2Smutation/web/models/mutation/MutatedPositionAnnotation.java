package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.cbioportal.G2Smutation.web.models.db.Dbsnp;
import org.cbioportal.G2Smutation.web.models.db.Genie;
import org.cbioportal.G2Smutation.web.models.db.Tcga;

public class MutatedPositionAnnotation {
    
    private int proteinPos;  //SEQ_INDEX
    private String proteinResidue; //SEQ_RESIDUE
    
    private List<MutatedResidueAnnotation> mutatedResidueAnnotation; //PDB_NO, PDB_INDEX
    
    private List<Dbsnp> dbsnpAnnotation;
    private List<Clinvar> clinvarAnnotation;
    private List<Cosmic> cosmicAnnotation;
    private List<Genie> genieAnnotation;
    private List<Tcga> tcgaAnnotation;

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

	public List<Dbsnp> getDbsnpAnnotation() {
		return dbsnpAnnotation;
	}

	public void setDbsnpAnnotation(List<Dbsnp> dbsnpAnnotation) {
		this.dbsnpAnnotation = dbsnpAnnotation;
	}

	public List<Clinvar> getClinvarAnnotation() {
		return clinvarAnnotation;
	}

	public void setClinvarAnnotation(List<Clinvar> clinvarAnnotation) {
		this.clinvarAnnotation = clinvarAnnotation;
	}

	public List<Cosmic> getCosmicAnnotation() {
		return cosmicAnnotation;
	}

	public void setCosmicAnnotation(List<Cosmic> cosmicAnnotation) {
		this.cosmicAnnotation = cosmicAnnotation;
	}

	public List<Genie> getGenieAnnotation() {
		return genieAnnotation;
	}

	public void setGenieAnnotation(List<Genie> genieAnnotation) {
		this.genieAnnotation = genieAnnotation;
	}

	public List<Tcga> getTcgaAnnotation() {
		return tcgaAnnotation;
	}

	public void setTcgaAnnotation(List<Tcga> tcgaAnnotation) {
		this.tcgaAnnotation = tcgaAnnotation;
	}
    
    

    

}
