package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StructureAnnotationRepository extends JpaRepository<StructureAnnotation, Long>{
    
    public StructureAnnotation findByMutationId(int mutationId);
    
    public List<StructureAnnotation> findByChrPos(String chrPos);

}
