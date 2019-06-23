package org.cbioportal.G2Smutation.web.models.mutation;

import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;

/**
 * indexed by MUTATION_ID in structure_annotation_entry
 * 
 * @author Juexin Wang
 *
 */

public class MutatedResidueInfo extends MutatedResidue{
    private StructureAnnotation structureAnnotation;

	public StructureAnnotation getStructureAnnotation() {
		return structureAnnotation;
	}

	public void setStructureAnnotation(StructureAnnotation structureAnnotation) {
		this.structureAnnotation = structureAnnotation;
	}
}
