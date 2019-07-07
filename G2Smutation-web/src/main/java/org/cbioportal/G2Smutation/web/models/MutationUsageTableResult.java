package org.cbioportal.G2Smutation.web.models;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;

public class MutationUsageTableResult {
	private List<MutationUsageTable> data;
	private int recordsTotal;
	private int recordsFiltered;	

	public List<MutationUsageTable> getData() {
		return data;
	}

	public void setData(List<MutationUsageTable> data) {
		this.data = data;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}	
	

}
