package org.cbioportal.G2Smutation.web.models.mutation;

import org.cbioportal.G2Smutation.web.models.StructureAnnotationInfo;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;

/**
 * indexed by MUTATION_ID in structure_annotation_entry
 * 
 * @author Juexin Wang
 *
 */

public class MutatedResidueInfo extends MutatedResidue{
    private StructureAnnotationInfo structureAnnotation;

	public StructureAnnotationInfo getStructureAnnotation() {
		return structureAnnotation;
	}

	public void setStructureAnnotationInfo(StructureAnnotationInfo structureAnnotation) {
		this.structureAnnotation = structureAnnotation;
	}
}
