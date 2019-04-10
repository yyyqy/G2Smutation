package org.cbioportal.G2Smutation.util.models;

import java.util.HashMap;
import java.util.List;

/**
 * Model of structure annotation
 * 
 * @author Qingyuan Yang
 *
 */

public class StructureAnnotationRecord {
	private String chrPos;
	
	private int mutationId;

    private String pdbNo;

    private int pdbResidueIndex;

    private String pdbResidueName;
    
    private String buried;
    
    private String allAtomsABS;
    
    private String allAtomsREL;
    
    private String totalSideABS;
    
    private String totalSideREL;
    
    private String mainChainABS;
    
    private String mainChainREL;
    
    private String nonPolarABS;
    
    private String nonPolarREL;
    
    private String allPolarABS;
    
    private String allPolarREL;
    
    private String secStructure;
    
    private String threeTurnHelix;
    
    private String fourTurnHelix;
    
    private String fiveTurnHelix;
    
    private String geometricalBend;
    
    private String chirality;
    
    private String betaBridgeLabela;
    
    private String betaBridgeLabelb;
    
    private String bpa;
    
    private String bpb;
    
    private String betaSheetLabel;
    
    private String acc;
    
    private int ligandBindingProtein;
    
    private int ligandBindingdirect;
    
    private String ligandName;
    
    private String interproId;
    
    private String interproName;
    
    private String interproIdentifier;
    
    private String interproStart;
    
    private String interproEnd;
    
    private String pfamId;
    
    private String pfamName;
    
    private String pfamDescription;
    
    private String pfamIdentifier;
    
    private String pfamStart;
    
    private String pfamEnd;
    
    private String cathId;
    
    private String cathArchitecture;
    
    private String cathClass;
    
    private String cathHomology;
    
    private String cathIdentifier;
    
    private String cathName;
    
    private String cathTopology;
    
    private String cathDomainId;
    
    private String cathDomainStart;
    
    private String cathDomainEnd;
    
    private String scopId;
    
    private String scopDescription;
    
    private String scopIdentifier;
    
    private String scopSccs;
    
    private String scopClassSunid;
    
    private String scopClassDescription;
    
    private String scopFoldSunid;
    
    private String scopFoldDescription;
    
    private String scopSuperfamilySunid;
    
    private String scopSuperfamilyDescription;
    
    private String scopStart;
    
    private String scopEnd;
    

    /*
     * getter and setter methods
     */

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

	public int getPdbResidueIndex() {
		return pdbResidueIndex;
	}

	public void setPdbResidueIndex(int pdbResidueIndex) {
		this.pdbResidueIndex = pdbResidueIndex;
	}

	public String getPdbResidueName() {
		return pdbResidueName;
	}

	public void setPdbResidueName(String pdbResidueName) {
		this.pdbResidueName = pdbResidueName;
	}

	public String getBuried() {
		return buried;
	}

	public void setBuried(String buried) {
		this.buried = buried;
	}

	public String getAllAtomsABS() {
		return allAtomsABS;
	}

	public void setAllAtomsABS(String allAtomsABS) {
		this.allAtomsABS = allAtomsABS;
	}

	public String getAllAtomsREL() {
		return allAtomsREL;
	}

	public void setAllAtomsREL(String allAtomsREL) {
		this.allAtomsREL = allAtomsREL;
	}

	public String getTotalSideABS() {
		return totalSideABS;
	}

	public void setTotalSideABS(String totalSideABS) {
		this.totalSideABS = totalSideABS;
	}

	public String getTotalSideREL() {
		return totalSideREL;
	}

	public void setTotalSideREL(String totalSideREL) {
		this.totalSideREL = totalSideREL;
	}

	public String getMainChainABS() {
		return mainChainABS;
	}

	public void setMainChainABS(String mainChainABS) {
		this.mainChainABS = mainChainABS;
	}

	public String getMainChainREL() {
		return mainChainREL;
	}

	public void setMainChainREL(String mainChainREL) {
		this.mainChainREL = mainChainREL;
	}

	public String getNonPolarABS() {
		return nonPolarABS;
	}

	public void setNonPolarABS(String nonPolarABS) {
		this.nonPolarABS = nonPolarABS;
	}

	public String getNonPolarREL() {
		return nonPolarREL;
	}

	public void setNonPolarREL(String nonPolarREL) {
		this.nonPolarREL = nonPolarREL;
	}

	public String getAllPolarABS() {
		return allPolarABS;
	}

	public void setAllPolarABS(String allPolarABS) {
		this.allPolarABS = allPolarABS;
	}

	public String getAllPolarREL() {
		return allPolarREL;
	}

	public void setAllPolarREL(String allPolarREL) {
		this.allPolarREL = allPolarREL;
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

	public String getBetaBridgeLabela() {
		return betaBridgeLabela;
	}

	public void setBetaBridgeLabela(String betaBridgeLabela) {
		this.betaBridgeLabela = betaBridgeLabela;
	}

	public String getBetaBridgeLabelb() {
		return betaBridgeLabelb;
	}

	public void setBetaBridgeLabelb(String betaBridgeLabelb) {
		this.betaBridgeLabelb = betaBridgeLabelb;
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

	public int getLigandBindingdirect() {
		return ligandBindingdirect;
	}

	public void setLigandBindingdirect(int ligandBindingdirect) {
		this.ligandBindingdirect = ligandBindingdirect;
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
