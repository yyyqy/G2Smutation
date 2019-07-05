package org.cbioportal.G2Smutation.web.models;

import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;

public class MutationUsageTableVariantsInfo {
	
	private String geneName;	
	private String proteinName;	
    private int seqIndex;   
    private String seqResidue;    
    private String pdbInfo;//abcd_A_123    
    private String pdbResidue;    
    private String evalue;
    private float bitscore;    
    private float identityRatio;    
    private float identityPRatio;
    
    //Constructor
    public MutationUsageTableVariantsInfo(MutationUsageTable mut, Alignment ali){
    	String queryname = mut.getSeqName(); //P04637_1 P53_HUMAN;ENSP00000269305.4 ENSG00000141510.11 ENST00000269305.4;ENSP00000391478.2 ENSG00000141510.11 ENST00000445888.2
    	String geneName = "";
    	String proteinName = "";
    	if(queryname.contains(";")) {
    		String[] names = queryname.split(";");
    		for(String qname: names) {
    			String[] usenames = qname.split("\\s+");
    			for(String usename: usenames) {
    				if(usename.contains("_") || usename.startsWith("ENSP")) {
    					proteinName = proteinName + usename + " ";
    				}else if(usename.startsWith("ENSG")) {
    					geneName = geneName + usename + " ";
    				}
    			}
    		}
    	}else {
    			String qname = queryname;
    			String[] usenames = qname.split("\\s+");
    			for(String usename: usenames) {
    				if(usename.contains("_") || usename.startsWith("ENSP")) {
    					proteinName = proteinName + usename + " ";
    				}else if(usename.startsWith("ENSG")) {
    					geneName = geneName + usename + " ";
    				}
    			}
    	}
    	this.setGeneName(geneName.trim());
    	this.setProteinName(proteinName.trim());
    	this.setSeqIndex(mut.getSeqIndex());
    	this.setSeqResidue(mut.getSeqResidue());
    	String[] temp = mut.getPdbNo().split("_");
    	this.setPdbInfo(temp[0]+"_"+temp[1]+"_"+mut.getPdbIndex());
    	this.setPdbResidue(mut.getPdbResidue());
    	this.setEvalue(ali.getEvalue());
    	this.setBitscore(ali.getBitscore());
    	int length = ali.getMidlineAlign().length();
    	this.setIdentityRatio(ali.getIdentity()/length);
    	this.setIdentityPRatio(ali.getIdentityPositive()/length);   	
    	
    }

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String getProteinName() {
		return proteinName;
	}

	public void setProteinName(String proteinName) {
		this.proteinName = proteinName;
	}

	public int getSeqIndex() {
		return seqIndex;
	}

	public void setSeqIndex(int seqIndex) {
		this.seqIndex = seqIndex;
	}

	public String getSeqResidue() {
		return seqResidue;
	}

	public void setSeqResidue(String seqResidue) {
		this.seqResidue = seqResidue;
	}
	
	

	public String getPdbInfo() {
		return pdbInfo;
	}

	public void setPdbInfo(String pdbInfo) {
		this.pdbInfo = pdbInfo;
	}

	public String getPdbResidue() {
		return pdbResidue;
	}

	public void setPdbResidue(String pdbResidue) {
		this.pdbResidue = pdbResidue;
	}

	public String getEvalue() {
		return evalue;
	}

	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}

	public float getBitscore() {
		return bitscore;
	}

	public void setBitscore(float bitscore) {
		this.bitscore = bitscore;
	}

	public float getIdentityRatio() {
		return identityRatio;
	}

	public void setIdentityRatio(float identityRatio) {
		this.identityRatio = identityRatio;
	}

	public float getIdentityPRatio() {
		return identityPRatio;
	}

	public void setIdentityPRatio(float identityPRatio) {
		this.identityPRatio = identityPRatio;
	}

	

}
