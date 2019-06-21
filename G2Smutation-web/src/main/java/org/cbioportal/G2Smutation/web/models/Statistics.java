package org.cbioportal.G2Smutation.web.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Update record and statistics weekly
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "update_record")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Column(name = "UPDATE_DATE")
    private String updateDate;

    @Column(name = "SEG_NUM")
    private int segNum;

    @Column(name = "PDB_NUM")
    private int pdbNum;

    @Column(name = "ALIGNMENT_NUM")
    private int alignmentNum;
    
    //g2smutation added here
    @Column(name = "DBSNP_NUM")
    private int dbsnpNum;
    
    @Column(name = "CLINVAR_NUM")
    private int clinvarNum;
    
    @Column(name = "COSMIC_NUM")
    private int cosmicNum;
    
    @Column(name = "GENIE_NUM")
    private int genieNum;
    
    @Column(name = "TCGA_NUM")
    private int tcgaNum;
    
    @Column(name = "DBSNP_MAPPING_NUM")
    private int dbsnpMappingNum;
    
    @Column(name = "CLINVAR_MAPPING_NUM")
    private int clinvarMappingNum;
    
    @Column(name = "COSMIC_MAPPING_NUM")
    private int cosmicMappingNum;
    
    @Column(name = "GENIE_MAPPING_NUM")
    private int genieMappingNum;
    
    @Column(name = "TCGA_MAPPING_NUM")
    private int tcgaMappingNum;
    
    @Column(name = "MUTATION_NO_MAPPING_NUM")
    private int mutationNoMappingNum;
    
    @Column(name = "MUTATION_NO_MAPPING_UNIQUE_NUM")
    private int mutationNoMappingUniqueNum;
    
    @Column(name = "MUTATION_NO")
    private int mutationNo;
    
    @Column(name = "MUTATION_NO_UNIQUE")
    private int mutationNoUnique;
    
    @Column(name = "MUTATION_USAGE_NUM")
    private int mutationUsageNum;
    
    @Column(name = "MUTATION_LOCATION_NUM")
    private int mutationLocationNum;
    
    @Column(name = "A2R")
    private int a2R;
    
    @Column(name = "A2N")
    private int a2N;
    
    @Column(name = "A2D")
    private int a2D;
    
    @Column(name = "A2C")
    private int a2C;
    
    @Column(name = "A2Q")
    private int a2Q;
    
    @Column(name = "A2E")
    private int a2E;
    
    @Column(name = "A2G")
    private int a2G;
    
    @Column(name = "A2H")
    private int a2H;
    
    @Column(name = "A2I")
    private int a2I;
    
    @Column(name = "A2L")
    private int a2L;
    
    @Column(name = "A2K")
    private int a2K;
    
    @Column(name = "A2M")
    private int a2M;
    
    @Column(name = "A2F")
    private int a2F;
    
    @Column(name = "A2P")
    private int a2P;
    
    @Column(name = "A2S")
    private int a2S;
    
    @Column(name = "A2T")
    private int a2T;
    
    @Column(name = "A2W")
    private int a2W;
    
    @Column(name = "A2Y")
    private int a2Y;
    
    @Column(name = "A2V")
    private int a2V;
    
    
    @Column(name = "R2N")
    private int r2N;
    
    @Column(name = "R2D")
    private int r2D;
    
    @Column(name = "R2C")
    private int r2C;
    
    @Column(name = "R2Q")
    private int r2Q;
    
    @Column(name = "R2E")
    private int r2E;
    
    @Column(name = "R2G")
    private int r2G;
    
    @Column(name = "R2H")
    private int r2H;
    
    @Column(name = "R2I")
    private int r2I;
    
    @Column(name = "R2L")
    private int r2L;
    
    @Column(name = "R2K")
    private int r2K;
    
    @Column(name = "R2M")
    private int r2M;
    
    @Column(name = "R2F")
    private int r2F;
    
    @Column(name = "R2P")
    private int r2P;
    
    @Column(name = "R2S")
    private int r2S;
    
    @Column(name = "R2T")
    private int r2T;
    
    @Column(name = "R2W")
    private int r2W;
    
    @Column(name = "R2Y")
    private int r2Y;
    
    @Column(name = "R2V")
    private int r2V;
    
    
    
    @Column(name = "N2D")
    private int n2D;
    
    @Column(name = "N2C")
    private int n2C;
    
    @Column(name = "N2Q")
    private int n2Q;
    
    @Column(name = "N2E")
    private int n2E;
    
    @Column(name = "N2G")
    private int n2G;
    
    @Column(name = "N2H")
    private int n2H;
    
    @Column(name = "N2I")
    private int n2I;
    
    @Column(name = "N2L")
    private int n2L;
    
    @Column(name = "N2K")
    private int n2K;
    
    @Column(name = "N2M")
    private int n2M;
    
    @Column(name = "N2F")
    private int n2F;
    
    @Column(name = "N2P")
    private int n2P;
    
    @Column(name = "N2S")
    private int n2S;
    
    @Column(name = "N2T")
    private int n2T;
    
    @Column(name = "N2W")
    private int n2W;
    
    @Column(name = "N2Y")
    private int n2Y;
    
    @Column(name = "N2V")
    private int n2V;
    
    
    
    @Column(name = "D2C")
    private int d2C;
    
    @Column(name = "D2Q")
    private int d2Q;
    
    @Column(name = "D2E")
    private int d2E;
    
    @Column(name = "D2G")
    private int d2G;
    
    @Column(name = "D2H")
    private int d2H;
    
    @Column(name = "D2I")
    private int d2I;
    
    @Column(name = "D2L")
    private int d2L;
    
    @Column(name = "D2K")
    private int d2K;
    
    @Column(name = "D2M")
    private int d2M;
    
    @Column(name = "D2F")
    private int d2F;
    
    @Column(name = "D2P")
    private int d2P;
    
    @Column(name = "D2S")
    private int d2S;
    
    @Column(name = "D2T")
    private int d2T;
    
    @Column(name = "D2W")
    private int d2W;
    
    @Column(name = "D2Y")
    private int d2Y;
    
    @Column(name = "D2V")
    private int d2V;
    
    
    @Column(name = "C2Q")
    private int c2Q;
    
    @Column(name = "C2E")
    private int c2E;
    
    @Column(name = "C2G")
    private int c2G;
    
    @Column(name = "C2H")
    private int c2H;
    
    @Column(name = "C2I")
    private int c2I;
    
    @Column(name = "C2L")
    private int c2L;
    
    @Column(name = "C2K")
    private int c2K;
    
    @Column(name = "C2M")
    private int c2M;
    
    @Column(name = "C2F")
    private int c2F;
    
    @Column(name = "C2P")
    private int c2P;
    
    @Column(name = "C2S")
    private int c2S;
    
    @Column(name = "C2T")
    private int c2T;
    
    @Column(name = "C2W")
    private int c2W;
    
    @Column(name = "C2Y")
    private int c2Y;
    
    @Column(name = "C2V")
    private int c2V;
    
    
    @Column(name = "Q2E")
    private int q2E;
    
    @Column(name = "Q2G")
    private int q2G;
    
    @Column(name = "Q2H")
    private int q2H;
    
    @Column(name = "Q2I")
    private int q2I;
    
    @Column(name = "Q2L")
    private int q2L;
    
    @Column(name = "Q2K")
    private int q2K;
    
    @Column(name = "Q2M")
    private int q2M;
    
    @Column(name = "Q2F")
    private int q2F;
    
    @Column(name = "Q2P")
    private int q2P;
    
    @Column(name = "Q2S")
    private int q2S;
    
    @Column(name = "Q2T")
    private int q2T;
    
    @Column(name = "Q2W")
    private int q2W;
    
    @Column(name = "Q2Y")
    private int q2Y;
    
    @Column(name = "Q2V")
    private int q2V;
    
    
    
    @Column(name = "E2G")
    private int e2G;
    
    @Column(name = "E2H")
    private int e2H;
    
    @Column(name = "E2I")
    private int e2I;
    
    @Column(name = "E2L")
    private int e2L;
    
    @Column(name = "E2K")
    private int e2K;
    
    @Column(name = "E2M")
    private int e2M;
    
    @Column(name = "E2F")
    private int e2F;
    
    @Column(name = "E2P")
    private int e2P;
    
    @Column(name = "E2S")
    private int e2S;
    
    @Column(name = "E2T")
    private int e2T;
    
    @Column(name = "E2W")
    private int e2W;
    
    @Column(name = "E2Y")
    private int e2Y;
    
    @Column(name = "E2V")
    private int e2V;
    
    
    
    @Column(name = "G2H")
    private int g2H;
    
    @Column(name = "G2I")
    private int g2I;
    
    @Column(name = "G2L")
    private int g2L;
    
    @Column(name = "G2K")
    private int g2K;
    
    @Column(name = "G2M")
    private int g2M;
    
    @Column(name = "G2F")
    private int g2F;
    
    @Column(name = "G2P")
    private int g2P;
    
    @Column(name = "G2S")
    private int g2S;
    
    @Column(name = "G2T")
    private int g2T;
    
    @Column(name = "G2W")
    private int g2W;
    
    @Column(name = "G2Y")
    private int g2Y;
    
    @Column(name = "G2V")
    private int g2V;
    
    
    @Column(name = "H2I")
    private int h2I;
    
    @Column(name = "H2L")
    private int h2L;
    
    @Column(name = "H2K")
    private int h2K;
    
    @Column(name = "H2M")
    private int h2M;
    
    @Column(name = "H2F")
    private int h2F;
    
    @Column(name = "H2P")
    private int h2P;
    
    @Column(name = "H2S")
    private int h2S;
    
    @Column(name = "H2T")
    private int h2T;
    
    @Column(name = "H2W")
    private int h2W;
    
    @Column(name = "H2Y")
    private int h2Y;
    
    @Column(name = "H2V")
    private int h2V;
    
    
    @Column(name = "I2L")
    private int i2L;
    
    @Column(name = "I2K")
    private int i2K;
    
    @Column(name = "I2M")
    private int i2M;
    
    @Column(name = "I2F")
    private int i2F;
    
    @Column(name = "I2P")
    private int i2P;
    
    @Column(name = "I2S")
    private int i2S;
    
    @Column(name = "I2T")
    private int i2T;
    
    @Column(name = "I2W")
    private int i2W;
    
    @Column(name = "I2Y")
    private int i2Y;
    
    @Column(name = "I2V")
    private int i2V;
    
    
    
    @Column(name = "L2K")
    private int l2K;
    
    @Column(name = "L2M")
    private int l2M;
    
    @Column(name = "L2F")
    private int l2F;
    
    @Column(name = "L2P")
    private int l2P;
    
    @Column(name = "L2S")
    private int l2S;
    
    @Column(name = "L2T")
    private int l2T;
    
    @Column(name = "L2W")
    private int l2W;
    
    @Column(name = "L2Y")
    private int l2Y;
    
    @Column(name = "L2V")
    private int l2V;
    
    
    
    @Column(name = "K2M")
    private int k2M;
    
    @Column(name = "K2F")
    private int k2F;
    
    @Column(name = "K2P")
    private int k2P;
    
    @Column(name = "K2S")
    private int k2S;
    
    @Column(name = "K2T")
    private int k2T;
    
    @Column(name = "K2W")
    private int k2W;
    
    @Column(name = "K2Y")
    private int k2Y;
    
    @Column(name = "K2V")
    private int k2V;
    
    
    @Column(name = "M2F")
    private int m2F;
    
    @Column(name = "M2P")
    private int m2P;
    
    @Column(name = "M2S")
    private int m2S;
    
    @Column(name = "M2T")
    private int m2T;
    
    @Column(name = "M2W")
    private int m2W;
    
    @Column(name = "M2Y")
    private int m2Y;
    
    @Column(name = "M2V")
    private int m2V;
    
    
    @Column(name = "F2P")
    private int f2P;
    
    @Column(name = "F2S")
    private int f2S;
    
    @Column(name = "F2T")
    private int f2T;
    
    @Column(name = "F2W")
    private int f2W;
    
    @Column(name = "F2Y")
    private int f2Y;
    
    @Column(name = "F2V")
    private int f2V;
    
    
    @Column(name = "P2S")
    private int p2S;
    
    @Column(name = "P2T")
    private int p2T;
    
    @Column(name = "P2W")
    private int p2W;
    
    @Column(name = "P2Y")
    private int p2Y;
    
    @Column(name = "P2V")
    private int p2V;
    
    
    
    @Column(name = "S2T")
    private int s2T;
    
    @Column(name = "S2W")
    private int s2W;
    
    @Column(name = "S2Y")
    private int s2Y;
    
    @Column(name = "S2V")
    private int s2V;
    
    
    @Column(name = "T2W")
    private int t2W;
    
    @Column(name = "T2Y")
    private int t2Y;
    
    @Column(name = "T2V")
    private int t2V;

    
    @Column(name = "W2Y")
    private int w2Y;
    
    @Column(name = "W2V")
    private int w2V;
    
    @Column(name = "Y2V")
    private int y2V;




    
    


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getSegNum() {
        return segNum;
    }

    public void setSegNum(int segNum) {
        this.segNum = segNum;
    }

    public int getPdbNum() {
        return pdbNum;
    }

    public void setPdbNum(int pdbNum) {
        this.pdbNum = pdbNum;
    }

    public int getAlignmentNum() {
        return alignmentNum;
    }

    public void setAlignmentNum(int alignmentNum) {
        this.alignmentNum = alignmentNum;
    }

	public int getDbsnpNum() {
		return dbsnpNum;
	}

	public void setDbsnpNum(int dbsnpNum) {
		this.dbsnpNum = dbsnpNum;
	}

	public int getClinvarNum() {
		return clinvarNum;
	}

	public void setClinvarNum(int clinvarNum) {
		this.clinvarNum = clinvarNum;
	}

	public int getCosmicNum() {
		return cosmicNum;
	}

	public void setCosmicNum(int cosmicNum) {
		this.cosmicNum = cosmicNum;
	}

	public int getGenieNum() {
		return genieNum;
	}

	public void setGenieNum(int genieNum) {
		this.genieNum = genieNum;
	}

	public int getTcgaNum() {
		return tcgaNum;
	}

	public void setTcgaNum(int tcgaNum) {
		this.tcgaNum = tcgaNum;
	}

	public int getDbsnpMappingNum() {
		return dbsnpMappingNum;
	}

	public void setDbsnpMappingNum(int dbsnpMappingNum) {
		this.dbsnpMappingNum = dbsnpMappingNum;
	}

	public int getClinvarMappingNum() {
		return clinvarMappingNum;
	}

	public void setClinvarMappingNum(int clinvarMappingNum) {
		this.clinvarMappingNum = clinvarMappingNum;
	}

	public int getCosmicMappingNum() {
		return cosmicMappingNum;
	}

	public void setCosmicMappingNum(int cosmicMappingNum) {
		this.cosmicMappingNum = cosmicMappingNum;
	}

	public int getGenieMappingNum() {
		return genieMappingNum;
	}

	public void setGenieMappingNum(int genieMappingNum) {
		this.genieMappingNum = genieMappingNum;
	}

	public int getTcgaMappingNum() {
		return tcgaMappingNum;
	}

	public void setTcgaMappingNum(int tcgaMappingNum) {
		this.tcgaMappingNum = tcgaMappingNum;
	}

	public int getMutationNoMappingNum() {
		return mutationNoMappingNum;
	}

	public void setMutationNoMappingNum(int mutationNoMappingNum) {
		this.mutationNoMappingNum = mutationNoMappingNum;
	}

	public int getMutationNoMappingUniqueNum() {
		return mutationNoMappingUniqueNum;
	}

	public void setMutationNoMappingUniqueNum(int mutationNoMappingUniqueNum) {
		this.mutationNoMappingUniqueNum = mutationNoMappingUniqueNum;
	}

	public int getMutationNo() {
		return mutationNo;
	}

	public void setMutationNo(int mutationNo) {
		this.mutationNo = mutationNo;
	}

	public int getMutationNoUnique() {
		return mutationNoUnique;
	}

	public void setMutationNoUnique(int mutationNoUnique) {
		this.mutationNoUnique = mutationNoUnique;
	}

	public int getMutationUsageNum() {
		return mutationUsageNum;
	}

	public void setMutationUsageNum(int mutationUsageNum) {
		this.mutationUsageNum = mutationUsageNum;
	}

	public int getMutationLocationNum() {
		return mutationLocationNum;
	}

	public void setMutationLocationNum(int mutationLocationNum) {
		this.mutationLocationNum = mutationLocationNum;
	}

	public int getA2R() {
		return a2R;
	}

	public void setA2R(int a2r) {
		a2R = a2r;
	}

	public int getA2N() {
		return a2N;
	}

	public void setA2N(int a2n) {
		a2N = a2n;
	}

	public int getA2D() {
		return a2D;
	}

	public void setA2D(int a2d) {
		a2D = a2d;
	}

	public int getA2C() {
		return a2C;
	}

	public void setA2C(int a2c) {
		a2C = a2c;
	}

	public int getA2Q() {
		return a2Q;
	}

	public void setA2Q(int a2q) {
		a2Q = a2q;
	}

	public int getA2E() {
		return a2E;
	}

	public void setA2E(int a2e) {
		a2E = a2e;
	}

	public int getA2G() {
		return a2G;
	}

	public void setA2G(int a2g) {
		a2G = a2g;
	}

	public int getA2H() {
		return a2H;
	}

	public void setA2H(int a2h) {
		a2H = a2h;
	}

	public int getA2I() {
		return a2I;
	}

	public void setA2I(int a2i) {
		a2I = a2i;
	}

	public int getA2L() {
		return a2L;
	}

	public void setA2L(int a2l) {
		a2L = a2l;
	}

	public int getA2K() {
		return a2K;
	}

	public void setA2K(int a2k) {
		a2K = a2k;
	}

	public int getA2M() {
		return a2M;
	}

	public void setA2M(int a2m) {
		a2M = a2m;
	}

	public int getA2F() {
		return a2F;
	}

	public void setA2F(int a2f) {
		a2F = a2f;
	}

	public int getA2P() {
		return a2P;
	}

	public void setA2P(int a2p) {
		a2P = a2p;
	}

	public int getA2S() {
		return a2S;
	}

	public void setA2S(int a2s) {
		a2S = a2s;
	}

	public int getA2T() {
		return a2T;
	}

	public void setA2T(int a2t) {
		a2T = a2t;
	}

	public int getA2W() {
		return a2W;
	}

	public void setA2W(int a2w) {
		a2W = a2w;
	}

	public int getA2Y() {
		return a2Y;
	}

	public void setA2Y(int a2y) {
		a2Y = a2y;
	}

	public int getA2V() {
		return a2V;
	}

	public void setA2V(int a2v) {
		a2V = a2v;
	}

	public int getR2N() {
		return r2N;
	}

	public void setR2N(int r2n) {
		r2N = r2n;
	}

	public int getR2D() {
		return r2D;
	}

	public void setR2D(int r2d) {
		r2D = r2d;
	}

	public int getR2C() {
		return r2C;
	}

	public void setR2C(int r2c) {
		r2C = r2c;
	}

	public int getR2Q() {
		return r2Q;
	}

	public void setR2Q(int r2q) {
		r2Q = r2q;
	}

	public int getR2E() {
		return r2E;
	}

	public void setR2E(int r2e) {
		r2E = r2e;
	}

	public int getR2G() {
		return r2G;
	}

	public void setR2G(int r2g) {
		r2G = r2g;
	}

	public int getR2H() {
		return r2H;
	}

	public void setR2H(int r2h) {
		r2H = r2h;
	}

	public int getR2I() {
		return r2I;
	}

	public void setR2I(int r2i) {
		r2I = r2i;
	}

	public int getR2L() {
		return r2L;
	}

	public void setR2L(int r2l) {
		r2L = r2l;
	}

	public int getR2K() {
		return r2K;
	}

	public void setR2K(int r2k) {
		r2K = r2k;
	}

	public int getR2M() {
		return r2M;
	}

	public void setR2M(int r2m) {
		r2M = r2m;
	}

	public int getR2F() {
		return r2F;
	}

	public void setR2F(int r2f) {
		r2F = r2f;
	}

	public int getR2P() {
		return r2P;
	}

	public void setR2P(int r2p) {
		r2P = r2p;
	}

	public int getR2S() {
		return r2S;
	}

	public void setR2S(int r2s) {
		r2S = r2s;
	}

	public int getR2T() {
		return r2T;
	}

	public void setR2T(int r2t) {
		r2T = r2t;
	}

	public int getR2W() {
		return r2W;
	}

	public void setR2W(int r2w) {
		r2W = r2w;
	}

	public int getR2Y() {
		return r2Y;
	}

	public void setR2Y(int r2y) {
		r2Y = r2y;
	}

	public int getR2V() {
		return r2V;
	}

	public void setR2V(int r2v) {
		r2V = r2v;
	}

	public int getN2D() {
		return n2D;
	}

	public void setN2D(int n2d) {
		n2D = n2d;
	}

	public int getN2C() {
		return n2C;
	}

	public void setN2C(int n2c) {
		n2C = n2c;
	}

	public int getN2Q() {
		return n2Q;
	}

	public void setN2Q(int n2q) {
		n2Q = n2q;
	}

	public int getN2E() {
		return n2E;
	}

	public void setN2E(int n2e) {
		n2E = n2e;
	}

	public int getN2G() {
		return n2G;
	}

	public void setN2G(int n2g) {
		n2G = n2g;
	}

	public int getN2H() {
		return n2H;
	}

	public void setN2H(int n2h) {
		n2H = n2h;
	}

	public int getN2I() {
		return n2I;
	}

	public void setN2I(int n2i) {
		n2I = n2i;
	}

	public int getN2L() {
		return n2L;
	}

	public void setN2L(int n2l) {
		n2L = n2l;
	}

	public int getN2K() {
		return n2K;
	}

	public void setN2K(int n2k) {
		n2K = n2k;
	}

	public int getN2M() {
		return n2M;
	}

	public void setN2M(int n2m) {
		n2M = n2m;
	}

	public int getN2F() {
		return n2F;
	}

	public void setN2F(int n2f) {
		n2F = n2f;
	}

	public int getN2P() {
		return n2P;
	}

	public void setN2P(int n2p) {
		n2P = n2p;
	}

	public int getN2S() {
		return n2S;
	}

	public void setN2S(int n2s) {
		n2S = n2s;
	}

	public int getN2T() {
		return n2T;
	}

	public void setN2T(int n2t) {
		n2T = n2t;
	}

	public int getN2W() {
		return n2W;
	}

	public void setN2W(int n2w) {
		n2W = n2w;
	}

	public int getN2Y() {
		return n2Y;
	}

	public void setN2Y(int n2y) {
		n2Y = n2y;
	}

	public int getN2V() {
		return n2V;
	}

	public void setN2V(int n2v) {
		n2V = n2v;
	}

	public int getD2C() {
		return d2C;
	}

	public void setD2C(int d2c) {
		d2C = d2c;
	}

	public int getD2Q() {
		return d2Q;
	}

	public void setD2Q(int d2q) {
		d2Q = d2q;
	}

	public int getD2E() {
		return d2E;
	}

	public void setD2E(int d2e) {
		d2E = d2e;
	}

	public int getD2G() {
		return d2G;
	}

	public void setD2G(int d2g) {
		d2G = d2g;
	}

	public int getD2H() {
		return d2H;
	}

	public void setD2H(int d2h) {
		d2H = d2h;
	}

	public int getD2I() {
		return d2I;
	}

	public void setD2I(int d2i) {
		d2I = d2i;
	}

	public int getD2L() {
		return d2L;
	}

	public void setD2L(int d2l) {
		d2L = d2l;
	}

	public int getD2K() {
		return d2K;
	}

	public void setD2K(int d2k) {
		d2K = d2k;
	}

	public int getD2M() {
		return d2M;
	}

	public void setD2M(int d2m) {
		d2M = d2m;
	}

	public int getD2F() {
		return d2F;
	}

	public void setD2F(int d2f) {
		d2F = d2f;
	}

	public int getD2P() {
		return d2P;
	}

	public void setD2P(int d2p) {
		d2P = d2p;
	}

	public int getD2S() {
		return d2S;
	}

	public void setD2S(int d2s) {
		d2S = d2s;
	}

	public int getD2T() {
		return d2T;
	}

	public void setD2T(int d2t) {
		d2T = d2t;
	}

	public int getD2W() {
		return d2W;
	}

	public void setD2W(int d2w) {
		d2W = d2w;
	}

	public int getD2Y() {
		return d2Y;
	}

	public void setD2Y(int d2y) {
		d2Y = d2y;
	}

	public int getD2V() {
		return d2V;
	}

	public void setD2V(int d2v) {
		d2V = d2v;
	}

	public int getC2Q() {
		return c2Q;
	}

	public void setC2Q(int c2q) {
		c2Q = c2q;
	}

	public int getC2E() {
		return c2E;
	}

	public void setC2E(int c2e) {
		c2E = c2e;
	}

	public int getC2G() {
		return c2G;
	}

	public void setC2G(int c2g) {
		c2G = c2g;
	}

	public int getC2H() {
		return c2H;
	}

	public void setC2H(int c2h) {
		c2H = c2h;
	}

	public int getC2I() {
		return c2I;
	}

	public void setC2I(int c2i) {
		c2I = c2i;
	}

	public int getC2L() {
		return c2L;
	}

	public void setC2L(int c2l) {
		c2L = c2l;
	}

	public int getC2K() {
		return c2K;
	}

	public void setC2K(int c2k) {
		c2K = c2k;
	}

	public int getC2M() {
		return c2M;
	}

	public void setC2M(int c2m) {
		c2M = c2m;
	}

	public int getC2F() {
		return c2F;
	}

	public void setC2F(int c2f) {
		c2F = c2f;
	}

	public int getC2P() {
		return c2P;
	}

	public void setC2P(int c2p) {
		c2P = c2p;
	}

	public int getC2S() {
		return c2S;
	}

	public void setC2S(int c2s) {
		c2S = c2s;
	}

	public int getC2T() {
		return c2T;
	}

	public void setC2T(int c2t) {
		c2T = c2t;
	}

	public int getC2W() {
		return c2W;
	}

	public void setC2W(int c2w) {
		c2W = c2w;
	}

	public int getC2Y() {
		return c2Y;
	}

	public void setC2Y(int c2y) {
		c2Y = c2y;
	}

	public int getC2V() {
		return c2V;
	}

	public void setC2V(int c2v) {
		c2V = c2v;
	}

	public int getQ2E() {
		return q2E;
	}

	public void setQ2E(int q2e) {
		q2E = q2e;
	}

	public int getQ2G() {
		return q2G;
	}

	public void setQ2G(int q2g) {
		q2G = q2g;
	}

	public int getQ2H() {
		return q2H;
	}

	public void setQ2H(int q2h) {
		q2H = q2h;
	}

	public int getQ2I() {
		return q2I;
	}

	public void setQ2I(int q2i) {
		q2I = q2i;
	}

	public int getQ2L() {
		return q2L;
	}

	public void setQ2L(int q2l) {
		q2L = q2l;
	}

	public int getQ2K() {
		return q2K;
	}

	public void setQ2K(int q2k) {
		q2K = q2k;
	}

	public int getQ2M() {
		return q2M;
	}

	public void setQ2M(int q2m) {
		q2M = q2m;
	}

	public int getQ2F() {
		return q2F;
	}

	public void setQ2F(int q2f) {
		q2F = q2f;
	}

	public int getQ2P() {
		return q2P;
	}

	public void setQ2P(int q2p) {
		q2P = q2p;
	}

	public int getQ2S() {
		return q2S;
	}

	public void setQ2S(int q2s) {
		q2S = q2s;
	}

	public int getQ2T() {
		return q2T;
	}

	public void setQ2T(int q2t) {
		q2T = q2t;
	}

	public int getQ2W() {
		return q2W;
	}

	public void setQ2W(int q2w) {
		q2W = q2w;
	}

	public int getQ2Y() {
		return q2Y;
	}

	public void setQ2Y(int q2y) {
		q2Y = q2y;
	}

	public int getQ2V() {
		return q2V;
	}

	public void setQ2V(int q2v) {
		q2V = q2v;
	}

	public int getE2G() {
		return e2G;
	}

	public void setE2G(int e2g) {
		e2G = e2g;
	}

	public int getE2H() {
		return e2H;
	}

	public void setE2H(int e2h) {
		e2H = e2h;
	}

	public int getE2I() {
		return e2I;
	}

	public void setE2I(int e2i) {
		e2I = e2i;
	}

	public int getE2L() {
		return e2L;
	}

	public void setE2L(int e2l) {
		e2L = e2l;
	}

	public int getE2K() {
		return e2K;
	}

	public void setE2K(int e2k) {
		e2K = e2k;
	}

	public int getE2M() {
		return e2M;
	}

	public void setE2M(int e2m) {
		e2M = e2m;
	}

	public int getE2F() {
		return e2F;
	}

	public void setE2F(int e2f) {
		e2F = e2f;
	}

	public int getE2P() {
		return e2P;
	}

	public void setE2P(int e2p) {
		e2P = e2p;
	}

	public int getE2S() {
		return e2S;
	}

	public void setE2S(int e2s) {
		e2S = e2s;
	}

	public int getE2T() {
		return e2T;
	}

	public void setE2T(int e2t) {
		e2T = e2t;
	}

	public int getE2W() {
		return e2W;
	}

	public void setE2W(int e2w) {
		e2W = e2w;
	}

	public int getE2Y() {
		return e2Y;
	}

	public void setE2Y(int e2y) {
		e2Y = e2y;
	}

	public int getE2V() {
		return e2V;
	}

	public void setE2V(int e2v) {
		e2V = e2v;
	}

	public int getG2H() {
		return g2H;
	}

	public void setG2H(int g2h) {
		g2H = g2h;
	}

	public int getG2I() {
		return g2I;
	}

	public void setG2I(int g2i) {
		g2I = g2i;
	}

	public int getG2L() {
		return g2L;
	}

	public void setG2L(int g2l) {
		g2L = g2l;
	}

	public int getG2K() {
		return g2K;
	}

	public void setG2K(int g2k) {
		g2K = g2k;
	}

	public int getG2M() {
		return g2M;
	}

	public void setG2M(int g2m) {
		g2M = g2m;
	}

	public int getG2F() {
		return g2F;
	}

	public void setG2F(int g2f) {
		g2F = g2f;
	}

	public int getG2P() {
		return g2P;
	}

	public void setG2P(int g2p) {
		g2P = g2p;
	}

	public int getG2S() {
		return g2S;
	}

	public void setG2S(int g2s) {
		g2S = g2s;
	}

	public int getG2T() {
		return g2T;
	}

	public void setG2T(int g2t) {
		g2T = g2t;
	}

	public int getG2W() {
		return g2W;
	}

	public void setG2W(int g2w) {
		g2W = g2w;
	}

	public int getG2Y() {
		return g2Y;
	}

	public void setG2Y(int g2y) {
		g2Y = g2y;
	}

	public int getG2V() {
		return g2V;
	}

	public void setG2V(int g2v) {
		g2V = g2v;
	}

	public int getH2I() {
		return h2I;
	}

	public void setH2I(int h2i) {
		h2I = h2i;
	}

	public int getH2L() {
		return h2L;
	}

	public void setH2L(int h2l) {
		h2L = h2l;
	}

	public int getH2K() {
		return h2K;
	}

	public void setH2K(int h2k) {
		h2K = h2k;
	}

	public int getH2M() {
		return h2M;
	}

	public void setH2M(int h2m) {
		h2M = h2m;
	}

	public int getH2F() {
		return h2F;
	}

	public void setH2F(int h2f) {
		h2F = h2f;
	}

	public int getH2P() {
		return h2P;
	}

	public void setH2P(int h2p) {
		h2P = h2p;
	}

	public int getH2S() {
		return h2S;
	}

	public void setH2S(int h2s) {
		h2S = h2s;
	}

	public int getH2T() {
		return h2T;
	}

	public void setH2T(int h2t) {
		h2T = h2t;
	}

	public int getH2W() {
		return h2W;
	}

	public void setH2W(int h2w) {
		h2W = h2w;
	}

	public int getH2Y() {
		return h2Y;
	}

	public void setH2Y(int h2y) {
		h2Y = h2y;
	}

	public int getH2V() {
		return h2V;
	}

	public void setH2V(int h2v) {
		h2V = h2v;
	}

	public int getI2L() {
		return i2L;
	}

	public void setI2L(int i2l) {
		i2L = i2l;
	}

	public int getI2K() {
		return i2K;
	}

	public void setI2K(int i2k) {
		i2K = i2k;
	}

	public int getI2M() {
		return i2M;
	}

	public void setI2M(int i2m) {
		i2M = i2m;
	}

	public int getI2F() {
		return i2F;
	}

	public void setI2F(int i2f) {
		i2F = i2f;
	}

	public int getI2P() {
		return i2P;
	}

	public void setI2P(int i2p) {
		i2P = i2p;
	}

	public int getI2S() {
		return i2S;
	}

	public void setI2S(int i2s) {
		i2S = i2s;
	}

	public int getI2T() {
		return i2T;
	}

	public void setI2T(int i2t) {
		i2T = i2t;
	}

	public int getI2W() {
		return i2W;
	}

	public void setI2W(int i2w) {
		i2W = i2w;
	}

	public int getI2Y() {
		return i2Y;
	}

	public void setI2Y(int i2y) {
		i2Y = i2y;
	}

	public int getI2V() {
		return i2V;
	}

	public void setI2V(int i2v) {
		i2V = i2v;
	}

	public int getL2K() {
		return l2K;
	}

	public void setL2K(int l2k) {
		l2K = l2k;
	}

	public int getL2M() {
		return l2M;
	}

	public void setL2M(int l2m) {
		l2M = l2m;
	}

	public int getL2F() {
		return l2F;
	}

	public void setL2F(int l2f) {
		l2F = l2f;
	}

	public int getL2P() {
		return l2P;
	}

	public void setL2P(int l2p) {
		l2P = l2p;
	}

	public int getL2S() {
		return l2S;
	}

	public void setL2S(int l2s) {
		l2S = l2s;
	}

	public int getL2T() {
		return l2T;
	}

	public void setL2T(int l2t) {
		l2T = l2t;
	}

	public int getL2W() {
		return l2W;
	}

	public void setL2W(int l2w) {
		l2W = l2w;
	}

	public int getL2Y() {
		return l2Y;
	}

	public void setL2Y(int l2y) {
		l2Y = l2y;
	}

	public int getL2V() {
		return l2V;
	}

	public void setL2V(int l2v) {
		l2V = l2v;
	}

	public int getK2M() {
		return k2M;
	}

	public void setK2M(int k2m) {
		k2M = k2m;
	}

	public int getK2F() {
		return k2F;
	}

	public void setK2F(int k2f) {
		k2F = k2f;
	}

	public int getK2P() {
		return k2P;
	}

	public void setK2P(int k2p) {
		k2P = k2p;
	}

	public int getK2S() {
		return k2S;
	}

	public void setK2S(int k2s) {
		k2S = k2s;
	}

	public int getK2T() {
		return k2T;
	}

	public void setK2T(int k2t) {
		k2T = k2t;
	}

	public int getK2W() {
		return k2W;
	}

	public void setK2W(int k2w) {
		k2W = k2w;
	}

	public int getK2Y() {
		return k2Y;
	}

	public void setK2Y(int k2y) {
		k2Y = k2y;
	}

	public int getK2V() {
		return k2V;
	}

	public void setK2V(int k2v) {
		k2V = k2v;
	}

	public int getM2F() {
		return m2F;
	}

	public void setM2F(int m2f) {
		m2F = m2f;
	}

	public int getM2P() {
		return m2P;
	}

	public void setM2P(int m2p) {
		m2P = m2p;
	}

	public int getM2S() {
		return m2S;
	}

	public void setM2S(int m2s) {
		m2S = m2s;
	}

	public int getM2T() {
		return m2T;
	}

	public void setM2T(int m2t) {
		m2T = m2t;
	}

	public int getM2W() {
		return m2W;
	}

	public void setM2W(int m2w) {
		m2W = m2w;
	}

	public int getM2Y() {
		return m2Y;
	}

	public void setM2Y(int m2y) {
		m2Y = m2y;
	}

	public int getM2V() {
		return m2V;
	}

	public void setM2V(int m2v) {
		m2V = m2v;
	}

	public int getF2P() {
		return f2P;
	}

	public void setF2P(int f2p) {
		f2P = f2p;
	}

	public int getF2S() {
		return f2S;
	}

	public void setF2S(int f2s) {
		f2S = f2s;
	}

	public int getF2T() {
		return f2T;
	}

	public void setF2T(int f2t) {
		f2T = f2t;
	}

	public int getF2W() {
		return f2W;
	}

	public void setF2W(int f2w) {
		f2W = f2w;
	}

	public int getF2Y() {
		return f2Y;
	}

	public void setF2Y(int f2y) {
		f2Y = f2y;
	}

	public int getF2V() {
		return f2V;
	}

	public void setF2V(int f2v) {
		f2V = f2v;
	}

	public int getP2S() {
		return p2S;
	}

	public void setP2S(int p2s) {
		p2S = p2s;
	}

	public int getP2T() {
		return p2T;
	}

	public void setP2T(int p2t) {
		p2T = p2t;
	}

	public int getP2W() {
		return p2W;
	}

	public void setP2W(int p2w) {
		p2W = p2w;
	}

	public int getP2Y() {
		return p2Y;
	}

	public void setP2Y(int p2y) {
		p2Y = p2y;
	}

	public int getP2V() {
		return p2V;
	}

	public void setP2V(int p2v) {
		p2V = p2v;
	}

	public int getS2T() {
		return s2T;
	}

	public void setS2T(int s2t) {
		s2T = s2t;
	}

	public int getS2W() {
		return s2W;
	}

	public void setS2W(int s2w) {
		s2W = s2w;
	}

	public int getS2Y() {
		return s2Y;
	}

	public void setS2Y(int s2y) {
		s2Y = s2y;
	}

	public int getS2V() {
		return s2V;
	}

	public void setS2V(int s2v) {
		s2V = s2v;
	}

	public int getT2W() {
		return t2W;
	}

	public void setT2W(int t2w) {
		t2W = t2w;
	}

	public int getT2Y() {
		return t2Y;
	}

	public void setT2Y(int t2y) {
		t2Y = t2y;
	}

	public int getT2V() {
		return t2V;
	}

	public void setT2V(int t2v) {
		t2V = t2v;
	}

	public int getW2Y() {
		return w2Y;
	}

	public void setW2Y(int w2y) {
		w2Y = w2y;
	}

	public int getW2V() {
		return w2V;
	}

	public void setW2V(int w2v) {
		w2V = w2v;
	}

	public int getY2V() {
		return y2V;
	}

	public void setY2V(int y2v) {
		y2V = y2v;
	}
    
    

}
