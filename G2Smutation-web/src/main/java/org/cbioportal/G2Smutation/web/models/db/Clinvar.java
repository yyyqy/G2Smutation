package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table clinvar_entry 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "clinvar_entry")
public class Clinvar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "CHR_POS")
    private String chrPos;
    
    @Column(name = "MUTATION_NO")
    private String mutationNo;
    
    @Column(name = "CLINVAR_ID")
    private int clinvarId;
    
    @Column(name = "REF")
    private String ref;
    
    @Column(name = "ALT")
    private String alt;
    
    @Column(name = "AF_ESP")
    private float afESP;
    
    @Column(name = "AF_EXAC")
    private float afEXAC;
    
    @Column(name = "AF_TGP")
    private float afTGP;
    
    @Column(name = "ALLELEID")
    private int alleleId;
    
    @Column(name = "CLNDN")
    private String clndn;
    
    @Column(name = "CLNDNINCL")
    private String clndnincl;
    
    @Column(name = "CLNDISDB")
    private String clndisdb;
    
    @Column(name = "CLNDISDBINCL")
    private String clndisdbincl;
    
    @Column(name = "CLNHGVS")
    private String clnhgvs;
    
    @Column(name = "CLNREVSTAT")
    private String clnrevstat;
    
    @Column(name = "CLNSIG")
    private String clnsig;
    
    @Column(name = "CLNSIGCONF")
    private String clnsigconf;
    
    @Column(name = "CLNSIGINCL")
    private String clnsigincl;
    
    @Column(name = "CLNVC")
    private String clnvc;
    
    @Column(name = "CLNVCSO")
    private String clnvcso;
    
    @Column(name = "CLNVI")
    private String clnvi;
    
    @Column(name = "DBVARID")
    private String dbvarid;
    
    @Column(name = "GENEINFO")
    private String geneinfo;
    
    @Column(name = "MC")
    private String mc;
    
    @Column(name = "ORIGIN")
    private String origin;
    
    @Column(name = "RS")
    private String rs;
    
    @Column(name = "SSR")
    private int ssr;
    
    @Column(name = "UPDATE_DATE")
    private String updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChrPos() {
        return chrPos;
    }

    public void setChrPos(String chrPos) {
        this.chrPos = chrPos;
    }

    public String getMutationNo() {
		return mutationNo;
	}

	public void setMutationNo(String mutationNo) {
		this.mutationNo = mutationNo;
	}

	public int getClinvarId() {
        return clinvarId;
    }

    public void setClinvarId(int clinvarId) {
        this.clinvarId = clinvarId;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public float getAfESP() {
        return afESP;
    }

    public void setAfESP(float afESP) {
        this.afESP = afESP;
    }

    public float getAfEXAC() {
        return afEXAC;
    }

    public void setAfEXAC(float afEXAC) {
        this.afEXAC = afEXAC;
    }

    public float getAfTGP() {
        return afTGP;
    }

    public void setAfTGP(float afTGP) {
        this.afTGP = afTGP;
    }

    public int getAlleleId() {
        return alleleId;
    }

    public void setAlleleId(int alleleId) {
        this.alleleId = alleleId;
    }

    public String getClndn() {
        return clndn;
    }

    public void setClndn(String clndn) {
        this.clndn = clndn;
    }

    public String getClndnincl() {
        return clndnincl;
    }

    public void setClndnincl(String clndnincl) {
        this.clndnincl = clndnincl;
    }

    public String getClndisdb() {
        return clndisdb;
    }

    public void setClndisdb(String clndisdb) {
        this.clndisdb = clndisdb;
    }

    public String getClndisdbincl() {
        return clndisdbincl;
    }

    public void setClndisdbincl(String clndisdbincl) {
        this.clndisdbincl = clndisdbincl;
    }

    public String getClnhgvs() {
        return clnhgvs;
    }

    public void setClnhgvs(String clnhgvs) {
        this.clnhgvs = clnhgvs;
    }

    public String getClnrevstat() {
        return clnrevstat;
    }

    public void setClnrevstat(String clnrevstat) {
        this.clnrevstat = clnrevstat;
    }

    public String getClnsig() {
        return clnsig;
    }

    public void setClnsig(String clnsig) {
        this.clnsig = clnsig;
    }

    public String getClnsigconf() {
        return clnsigconf;
    }

    public void setClnsigconf(String clnsigconf) {
        this.clnsigconf = clnsigconf;
    }

    public String getClnsigincl() {
        return clnsigincl;
    }

    public void setClnsigincl(String clnsigincl) {
        this.clnsigincl = clnsigincl;
    }

    public String getClnvc() {
        return clnvc;
    }

    public void setClnvc(String clnvc) {
        this.clnvc = clnvc;
    }

    public String getClnvcso() {
        return clnvcso;
    }

    public void setClnvcso(String clnvcso) {
        this.clnvcso = clnvcso;
    }

    public String getClnvi() {
        return clnvi;
    }

    public void setClnvi(String clnvi) {
        this.clnvi = clnvi;
    }

    public String getDbvarid() {
        return dbvarid;
    }

    public void setDbvarid(String dbvarid) {
        this.dbvarid = dbvarid;
    }

    public String getGeneinfo() {
        return geneinfo;
    }

    public void setGeneinfo(String geneinfo) {
        this.geneinfo = geneinfo;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public int getSsr() {
        return ssr;
    }

    public void setSsr(int ssr) {
        this.ssr = ssr;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    
    

}
