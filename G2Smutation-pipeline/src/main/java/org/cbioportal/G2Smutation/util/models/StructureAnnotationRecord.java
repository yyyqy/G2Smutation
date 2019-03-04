package org.cbioportal.G2Smutation.util.models;

import java.util.HashMap;
import java.util.List;

/**
 * Model of structure annotation
 * 
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
}
