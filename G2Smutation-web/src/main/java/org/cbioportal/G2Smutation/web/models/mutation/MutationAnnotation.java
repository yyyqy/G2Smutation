package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.cbioportal.G2Smutation.web.models.db.Dbsnp;
import org.cbioportal.G2Smutation.web.models.db.Genie;
import org.cbioportal.G2Smutation.web.models.db.Tcga;

/**
 * Mutation from dbsnp, clinvar, cosmic, genie, tcga; query by chromsome position
 * clinvar_entry... indexed by MUTATION_ID
 * 
 * @author Juexin Wang
 *
 */
public class MutationAnnotation extends Mutation{
    private List<Clinvar> clinvar;
    private List<Dbsnp> dbsnp;
    private List<Cosmic> cosmic;
    private List<Genie> genie;
    private List<Tcga> tcga;
    
    
    public List<Clinvar> getClinvar() {
        return clinvar;
    }
    public void setClinvar(List<Clinvar> clinvar) {
        this.clinvar = clinvar;
    }
    public List<Dbsnp> getDbsnp() {
        return dbsnp;
    }
    public void setDbsnp(List<Dbsnp> dbsnp) {
        this.dbsnp = dbsnp;
    }
    public List<Cosmic> getCosmic() {
        return cosmic;
    }
    public void setCosmic(List<Cosmic> cosmic) {
        this.cosmic = cosmic;
    }
    public List<Genie> getGenie() {
        return genie;
    }
    public void setGenie(List<Genie> genie) {
        this.genie = genie;
    }
    public List<Tcga> getTcga() {
        return tcga;
    }
    public void setTcga(List<Tcga> tcga) {
        this.tcga = tcga;
    }

}
