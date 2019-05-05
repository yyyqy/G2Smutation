package org.cbioportal.G2Smutation.web.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table structure_annotation_entry 
 * 
 * @author Juexin Wang
 *
 */
@Entity
@Table(name = "structure_annotation_entry")
public class StructureAnnotation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "CHR_POS")
    private String chrPos;
    
    @Column(name = "MUTATION_ID")
    private int mutationId;
    
    @Column(name = "PDB_NO")
    private String pdbNo;
    
    @Column(name = "PDB_INDEX")
    private int pdbIndex;
    
    @Column(name = "PDB_RESIDUE")
    private String pdbResidue;
    
    @Column(name = "BURIED")
    private String buried;
    
    @Column(name = "ALL_ATOMS_ABS")
    private String allAtomsAbs;
    
    @Column(name = "ALL_ATOMS_REL")
    private String allAtomsRel;
    
    @Column(name = "TOTAL_SIDE_ABS")
    private String totalSideAbs;
    
    @Column(name = "TOTAL_SIDE_REL")
    private String totalSideRel;
    
    @Column(name = "MAIN_CHAIN_ABS")
    private String mainChainAbs;
    
    @Column(name = "MAIN_CHAIN_REL")
    private String mainChianRel;
    
    @Column(name = "NON_POLAR_ABS")
    private String nonPolarAbs;
    
    @Column(name = "NON_POLAR_REL")
    private String nonPolarRel;
    
    @Column(name = "ALL_POLAR_ABS")
    private String allPolarAbs;
    
    @Column(name = "ALL_POLAR_REL")
    private String allpolarRel;
    
    @Column(name = "SEC_STRUCTURE")
    private String secStructure;
    
    @Column(name = "THREE_TURN_HELIX")
    private String threeTurnHelix;
    
    @Column(name = "FOUR_TURN_HELIX")
    private String fourTurnHelix;
    
    @Column(name = "FIVE_TURN_HELIX")
    private String fiveTurnHelix;
    
    @Column(name = "GEOMETRICAL_BEND")
    private String geometricalBend;
    
    @Column(name = "CHIRALITY")
    private String chirality;
    
    @Column(name = "BETA_BRIDGE_LABELA")
    private String betaBridgeLabelA;
    
    @Column(name = "BETA_BRIDGE_LABELB")
    private String betaBridgeLabelB;
    
    @Column(name = "BPA")
    private String bpa;
    
    @Column(name = "BPB")
    private String bpb;
    
    @Column(name = "BETA_SHEET_LABEL")
    private String betaSheetLabel;
    
    @Column(name = "ACC")
    private String acc;
    
    @Column(name = "LIGAND_BINDING_PROTEIN")
    private int ligandBindingProtein;
    
    @Column(name = "LIGAND_BINDING_DIRECT")
    private int ligandBindingDirect;
    
    @Column(name = "LIGAND_NAME")
    private String ligandName;
    
    @Column(name = "INTERPRO_ID")
    private String interproId;
    
    @Column(name = "INTERPRO_NAME")
    private String interproName;
    
    @Column(name = "INTERPRO_IDENTIFIER")
    private String interproIdentifier;
    
    @Column(name = "INTERPRO_START")
    private String interproStart;
    
    @Column(name = "INTERPRO_END")
    private String interproEnd;
    
    @Column(name = "PFAM_ID")
    private String pfamId;
    
    @Column(name = "PFAM_NAME")
    private String pfamName;
    
    @Column(name = "PFAM_DESCRIPTION")
    private String pfamDescription;
    
    @Column(name = "PFAM_IDENTIFIER")
    private String pfamIdentifier;
    
    @Column(name = "PFAM_START")
    private String pfamStart;
    
    @Column(name = "PFAM_END")
    private String pfamEnd;
    
    @Column(name = "CATH_ID")
    private String cathId;
    
    @Column(name = "CATH_ARCHITECTURE")
    private String cathArchitecture;
    
    @Column(name = "CATH_CLASS")
    private String cathClass;
    
    @Column(name = "CATH_HOMOLOGY")
    private String cathHomology;
    
    @Column(name = "CATH_IDENTIFIER")
    private String cathIdentifier;
    
    @Column(name = "CATH_NAME")
    private String cathName;
    
    @Column(name = "CATH_TOPOLOGY")
    private String cathTopology;
    
    @Column(name = "CATH_DOMAIN_ID")
    private String cathDomainId;
    
    @Column(name = "CATH_DOMAIN_START")
    private String cathDomainStart;
    
    @Column(name = "CATH_DOMAIN_END")
    private String cathDomainEnd;
    
    @Column(name = "SCOP_ID")
    private String scopId;
    
    @Column(name = "SCOP_DESCRIPTION")
    private String scopDescription;
    
    @Column(name = "SCOP_IDENTIFIER")
    private String scopIdentifier;
    
    @Column(name = "SCOP_SCCS")
    private String scopSccs;
    
    @Column(name = "SCOP_CLASS_SUNID")
    private String scopClassSunid;
    
    @Column(name = "SCOP_CLASS_DESCRIPTION")
    private String scopClassDescription;
    
    @Column(name = "SCOP_FOLD_SUNID")
    private String scopFoldSunid;
    
    @Column(name = "SCOP_FOLD_DESCRIPTION")
    private String scopFoldDescription;
    
    @Column(name = "SCOP_SUPERFAMILY_SUNID")
    private String scopSuperfamilySunid;
    
    @Column(name = "SCOP_SUPERFAMILY_DESCRIPTION")
    private String scopSuperfamilyDescription;
    
    @Column(name = "SCOP_START")
    private String scopStart;
    
    @Column(name = "SCOP_END")
    private String scopEnd;

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

    public int getMutationId() {
        return mutationId;
    }

    public void setMutationId(int mutationId) {
        this.mutationId = mutationId;
    }

    public String getPdbNo() {
        return pdbNo;
    }

    public void setPdbNo(String pdbNo) {
        this.pdbNo = pdbNo;
    }

    public int getPdbIndex() {
        return pdbIndex;
    }

    public void setPdbIndex(int pdbIndex) {
        this.pdbIndex = pdbIndex;
    }

    public String getPdbResidue() {
        return pdbResidue;
    }

    public void setPdbResidue(String pdbResidue) {
        this.pdbResidue = pdbResidue;
    }

    public String getBuried() {
        return buried;
    }

    public void setBuried(String buried) {
        this.buried = buried;
    }

    public String getAllAtomsAbs() {
        return allAtomsAbs;
    }

    public void setAllAtomsAbs(String allAtomsAbs) {
        this.allAtomsAbs = allAtomsAbs;
    }

    public String getAllAtomsRel() {
        return allAtomsRel;
    }

    public void setAllAtomsRel(String allAtomsRel) {
        this.allAtomsRel = allAtomsRel;
    }

    public String getTotalSideAbs() {
        return totalSideAbs;
    }

    public void setTotalSideAbs(String totalSideAbs) {
        this.totalSideAbs = totalSideAbs;
    }

    public String getTotalSideRel() {
        return totalSideRel;
    }

    public void setTotalSideRel(String totalSideRel) {
        this.totalSideRel = totalSideRel;
    }

    public String getMainChainAbs() {
        return mainChainAbs;
    }

    public void setMainChainAbs(String mainChainAbs) {
        this.mainChainAbs = mainChainAbs;
    }

    public String getMainChianRel() {
        return mainChianRel;
    }

    public void setMainChianRel(String mainChianRel) {
        this.mainChianRel = mainChianRel;
    }

    public String getNonPolarAbs() {
        return nonPolarAbs;
    }

    public void setNonPolarAbs(String nonPolarAbs) {
        this.nonPolarAbs = nonPolarAbs;
    }

    public String getNonPolarRel() {
        return nonPolarRel;
    }

    public void setNonPolarRel(String nonPolarRel) {
        this.nonPolarRel = nonPolarRel;
    }

    public String getAllPolarAbs() {
        return allPolarAbs;
    }

    public void setAllPolarAbs(String allPolarAbs) {
        this.allPolarAbs = allPolarAbs;
    }

    public String getAllpolarRel() {
        return allpolarRel;
    }

    public void setAllpolarRel(String allpolarRel) {
        this.allpolarRel = allpolarRel;
    }

    public String getSecStructure() {
        return secStructure;
    }

    public void setSecStructure(String secStructure) {
        this.secStructure = secStructure;
    }

    public String getThreeTurnHelix() {
        return threeTurnHelix;
    }

    public void setThreeTurnHelix(String threeTurnHelix) {
        this.threeTurnHelix = threeTurnHelix;
    }

    public String getFourTurnHelix() {
        return fourTurnHelix;
    }

    public void setFourTurnHelix(String fourTurnHelix) {
        this.fourTurnHelix = fourTurnHelix;
    }

    public String getFiveTurnHelix() {
        return fiveTurnHelix;
    }

    public void setFiveTurnHelix(String fiveTurnHelix) {
        this.fiveTurnHelix = fiveTurnHelix;
    }

    public String getGeometricalBend() {
        return geometricalBend;
    }

    public void setGeometricalBend(String geometricalBend) {
        this.geometricalBend = geometricalBend;
    }

    public String getChirality() {
        return chirality;
    }

    public void setChirality(String chirality) {
        this.chirality = chirality;
    }

    public String getBetaBridgeLabelA() {
        return betaBridgeLabelA;
    }

    public void setBetaBridgeLabelA(String betaBridgeLabelA) {
        this.betaBridgeLabelA = betaBridgeLabelA;
    }

    public String getBetaBridgeLabelB() {
        return betaBridgeLabelB;
    }

    public void setBetaBridgeLabelB(String betaBridgeLabelB) {
        this.betaBridgeLabelB = betaBridgeLabelB;
    }

    public String getBpa() {
        return bpa;
    }

    public void setBpa(String bpa) {
        this.bpa = bpa;
    }

    public String getBpb() {
        return bpb;
    }

    public void setBpb(String bpb) {
        this.bpb = bpb;
    }

    public String getBetaSheetLabel() {
        return betaSheetLabel;
    }

    public void setBetaSheetLabel(String betaSheetLabel) {
        this.betaSheetLabel = betaSheetLabel;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public int getLigandBindingProtein() {
        return ligandBindingProtein;
    }

    public void setLigandBindingProtein(int ligandBindingProtein) {
        this.ligandBindingProtein = ligandBindingProtein;
    }

    public int getLigandBindingDirect() {
        return ligandBindingDirect;
    }

    public void setLigandBindingDirect(int ligandBindingDirect) {
        this.ligandBindingDirect = ligandBindingDirect;
    }

    public String getLigandName() {
        return ligandName;
    }

    public void setLigandName(String ligandName) {
        this.ligandName = ligandName;
    }

    public String getInterproId() {
        return interproId;
    }

    public void setInterproId(String interproId) {
        this.interproId = interproId;
    }

    public String getInterproName() {
        return interproName;
    }

    public void setInterproName(String interproName) {
        this.interproName = interproName;
    }

    public String getInterproIdentifier() {
        return interproIdentifier;
    }

    public void setInterproIdentifier(String interproIdentifier) {
        this.interproIdentifier = interproIdentifier;
    }

    public String getInterproStart() {
        return interproStart;
    }

    public void setInterproStart(String interproStart) {
        this.interproStart = interproStart;
    }

    public String getInterproEnd() {
        return interproEnd;
    }

    public void setInterproEnd(String interproEnd) {
        this.interproEnd = interproEnd;
    }

    public String getPfamId() {
        return pfamId;
    }

    public void setPfamId(String pfamId) {
        this.pfamId = pfamId;
    }

    public String getPfamName() {
        return pfamName;
    }

    public void setPfamName(String pfamName) {
        this.pfamName = pfamName;
    }

    public String getPfamDescription() {
        return pfamDescription;
    }

    public void setPfamDescription(String pfamDescription) {
        this.pfamDescription = pfamDescription;
    }

    public String getPfamIdentifier() {
        return pfamIdentifier;
    }

    public void setPfamIdentifier(String pfamIdentifier) {
        this.pfamIdentifier = pfamIdentifier;
    }

    public String getPfamStart() {
        return pfamStart;
    }

    public void setPfamStart(String pfamStart) {
        this.pfamStart = pfamStart;
    }

    public String getPfamEnd() {
        return pfamEnd;
    }

    public void setPfamEnd(String pfamEnd) {
        this.pfamEnd = pfamEnd;
    }

    public String getCathId() {
        return cathId;
    }

    public void setCathId(String cathId) {
        this.cathId = cathId;
    }

    public String getCathArchitecture() {
        return cathArchitecture;
    }

    public void setCathArchitecture(String cathArchitecture) {
        this.cathArchitecture = cathArchitecture;
    }

    public String getCathClass() {
        return cathClass;
    }

    public void setCathClass(String cathClass) {
        this.cathClass = cathClass;
    }

    public String getCathHomology() {
        return cathHomology;
    }

    public void setCathHomology(String cathHomology) {
        this.cathHomology = cathHomology;
    }

    public String getCathIdentifier() {
        return cathIdentifier;
    }

    public void setCathIdentifier(String cathIdentifier) {
        this.cathIdentifier = cathIdentifier;
    }

    public String getCathName() {
        return cathName;
    }

    public void setCathName(String cathName) {
        this.cathName = cathName;
    }

    public String getCathTopology() {
        return cathTopology;
    }

    public void setCathTopology(String cathTopology) {
        this.cathTopology = cathTopology;
    }

    public String getCathDomainId() {
        return cathDomainId;
    }

    public void setCathDomainId(String cathDomainId) {
        this.cathDomainId = cathDomainId;
    }

    public String getCathDomainStart() {
        return cathDomainStart;
    }

    public void setCathDomainStart(String cathDomainStart) {
        this.cathDomainStart = cathDomainStart;
    }

    public String getCathDomainEnd() {
        return cathDomainEnd;
    }

    public void setCathDomainEnd(String cathDomainEnd) {
        this.cathDomainEnd = cathDomainEnd;
    }

    public String getScopId() {
        return scopId;
    }

    public void setScopId(String scopId) {
        this.scopId = scopId;
    }

    public String getScopDescription() {
        return scopDescription;
    }

    public void setScopDescription(String scopDescription) {
        this.scopDescription = scopDescription;
    }

    public String getScopIdentifier() {
        return scopIdentifier;
    }

    public void setScopIdentifier(String scopIdentifier) {
        this.scopIdentifier = scopIdentifier;
    }

    public String getScopSccs() {
        return scopSccs;
    }

    public void setScopSccs(String scopSccs) {
        this.scopSccs = scopSccs;
    }

    public String getScopClassSunid() {
        return scopClassSunid;
    }

    public void setScopClassSunid(String scopClassSunid) {
        this.scopClassSunid = scopClassSunid;
    }

    public String getScopClassDescription() {
        return scopClassDescription;
    }

    public void setScopClassDescription(String scopClassDescription) {
        this.scopClassDescription = scopClassDescription;
    }

    public String getScopFoldSunid() {
        return scopFoldSunid;
    }

    public void setScopFoldSunid(String scopFoldSunid) {
        this.scopFoldSunid = scopFoldSunid;
    }

    public String getScopFoldDescription() {
        return scopFoldDescription;
    }

    public void setScopFoldDescription(String scopFoldDescription) {
        this.scopFoldDescription = scopFoldDescription;
    }

    public String getScopSuperfamilySunid() {
        return scopSuperfamilySunid;
    }

    public void setScopSuperfamilySunid(String scopSuperfamilySunid) {
        this.scopSuperfamilySunid = scopSuperfamilySunid;
    }

    public String getScopSuperfamilyDescription() {
        return scopSuperfamilyDescription;
    }

    public void setScopSuperfamilyDescription(String scopSuperfamilyDescription) {
        this.scopSuperfamilyDescription = scopSuperfamilyDescription;
    }

    public String getScopStart() {
        return scopStart;
    }

    public void setScopStart(String scopStart) {
        this.scopStart = scopStart;
    }

    public String getScopEnd() {
        return scopEnd;
    }

    public void setScopEnd(String scopEnd) {
        this.scopEnd = scopEnd;
    }
    
    
    
    

}
