package org.cbioportal.G2Smutation.web.models.mutation;

import java.util.List;

public class MutationAnnotation {
  //MUTATION_NO and MUTATION_ID is not here
    private String proteinName; //SEQ_NAME
    
    private List<MutatedPositionInfo> mutatedPositionInfo;

	public String getProteinName() {
		return proteinName;
	}

	public void setProteinName(String proteinName) {
		this.proteinName = proteinName;
	}

	public List<MutatedPositionInfo> getMutatedPositionInfo() {
		return mutatedPositionInfo;
	}

	public void setMutatedPositionInfo(List<MutatedPositionInfo> mutatedPositionInfo) {
		this.mutatedPositionInfo = mutatedPositionInfo;
	}

}
