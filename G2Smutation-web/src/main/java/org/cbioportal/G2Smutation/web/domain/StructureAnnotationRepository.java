package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.StructureAnnotationInfo;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StructureAnnotationRepository extends JpaRepository<StructureAnnotation, Long>{   
    
    public List<StructureAnnotation> findByChrPos(String chrPos);
    
    public List<StructureAnnotation> findByPdbNoStartingWith(String input); //2pcx_A_
    
    public StructureAnnotation findTopByPdbNo(String pdbNo); //2pcx_A_282

}
