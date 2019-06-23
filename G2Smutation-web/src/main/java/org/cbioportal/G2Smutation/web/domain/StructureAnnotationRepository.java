package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StructureAnnotationRepository extends JpaRepository<StructureAnnotation, Long>{
    
	//This function does not work for updating...
    //public StructureAnnotation findByMutationId(int mutationId);
    
    public StructureAnnotation findTopByPdbNo(String pdbNo);
    
    public List<StructureAnnotation> findByChrPos(String chrPos);

}
