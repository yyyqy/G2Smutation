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

    private char pdbResidueName;
    
    private int buried;
    
    private float allAtomsABS;
    
    private float allAtomsREL;
    
    private float totalSideABS;
    
    private float totalSideREL;
    
    private float mainChainABS;
    
    private float mainChainREL;
    
    private float nonPolarABS;
    
    private float nonPolarREL;
    
    private float allPolarABS;
    
    private float allPolarREL;
    
    private char secStructure;
    
    private char threeTurnHelix;
    
    private char fourTurnHelix;
    
    private char fiveTurnHelix;
    
    private char geometricalBend;
    
    private char chirality;
    
    private char betaBridgeLabela;
    
    private char betaBridgeLabelb;
    
    private int bpa;
    
    private int bpb;
    
    private char betaSheetLabel;
    
    private int acc;
    
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

	public char getPdbResidueName() {
		return pdbResidueName;
	}

	public void setPdbResidueName(char pdbResidueName) {
		this.pdbResidueName = pdbResidueName;
	}

	public int getBuried() {
		return buried;
	}

	public void setBuried(int buried) {
		this.buried = buried;
	}

	public float getAllAtomsABS() {
		return allAtomsABS;
	}

	public void setAllAtomsABS(float allAtomsABS) {
		this.allAtomsABS = allAtomsABS;
	}

	public float getAllAtomsREL() {
		return allAtomsREL;
	}

	public void setAllAtomsREL(float allAtomsREL) {
		this.allAtomsREL = allAtomsREL;
	}

	public float getTotalSideABS() {
		return totalSideABS;
	}

	public void setTotalSideABS(float totalSideABS) {
		this.totalSideABS = totalSideABS;
	}

	public float getTotalSideREL() {
		return totalSideREL;
	}

	public void setTotalSideREL(float totalSideREL) {
		this.totalSideREL = totalSideREL;
	}

	public float getMainChainABS() {
		return mainChainABS;
	}

	public void setMainChainABS(float mainChainABS) {
		this.mainChainABS = mainChainABS;
	}

	public float getMainChainREL() {
		return mainChainREL;
	}

	public void setMainChainREL(float mainChainREL) {
		this.mainChainREL = mainChainREL;
	}

	public float getNonPolarABS() {
		return nonPolarABS;
	}

	public void setNonPolarABS(float nonPolarABS) {
		this.nonPolarABS = nonPolarABS;
	}

	public float getNonPolarREL() {
		return nonPolarREL;
	}

	public void setNonPolarREL(float nonPolarREL) {
		this.nonPolarREL = nonPolarREL;
	}

	public float getAllPolarABS() {
		return allPolarABS;
	}

	public void setAllPolarABS(float allPolarABS) {
		this.allPolarABS = allPolarABS;
	}

	public float getAllPolarREL() {
		return allPolarREL;
	}

	public void setAllPolarREL(float allPolarREL) {
		this.allPolarREL = allPolarREL;
	}

	public char getSecStructure() {
		return secStructure;
	}

	public void setSecStructure(char secStructure) {
		this.secStructure = secStructure;
	}

	public char getThreeTurnHelix() {
		return threeTurnHelix;
	}

	public void setThreeTurnHelix(char threeTurnHelix) {
		this.threeTurnHelix = threeTurnHelix;
	}

	public char getFourTurnHelix() {
		return fourTurnHelix;
	}

	public void setFourTurnHelix(char fourTurnHelix) {
		this.fourTurnHelix = fourTurnHelix;
	}

	public char getFiveTurnHelix() {
		return fiveTurnHelix;
	}

	public void setFiveTurnHelix(char fiveTurnHelix) {
		this.fiveTurnHelix = fiveTurnHelix;
	}

	public char getGeometricalBend() {
		return geometricalBend;
	}

	public void setGeometricalBend(char geometricalBend) {
		this.geometricalBend = geometricalBend;
	}

	public char getChirality() {
		return chirality;
	}

	public void setChirality(char chirality) {
		this.chirality = chirality;
	}

	public char getBetaBridgeLabela() {
		return betaBridgeLabela;
	}

	public void setBetaBridgeLabela(char betaBridgeLabela) {
		this.betaBridgeLabela = betaBridgeLabela;
	}

	public char getBetaBridgeLabelb() {
		return betaBridgeLabelb;
	}

	public void setBetaBridgeLabelb(char betaBridgeLabelb) {
		this.betaBridgeLabelb = betaBridgeLabelb;
	}

	public int getBpa() {
		return bpa;
	}

	public void setBpa(int bpa) {
		this.bpa = bpa;
	}

	public int getBpb() {
		return bpb;
	}

	public void setBpb(int bpb) {
		this.bpb = bpb;
	}

	public char getBetaSheetLabel() {
		return betaSheetLabel;
	}

	public void setBetaSheetLabel(char betaSheetLabel) {
		this.betaSheetLabel = betaSheetLabel;
	}

	public int getAcc() {
		return acc;
	}

	public void setAcc(int acc) {
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
