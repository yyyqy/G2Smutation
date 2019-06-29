package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.StructureAnnotationInfo;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StructureAnnotationRepository extends JpaRepository<StructureAnnotation, Long>{ 
	public List<StructureAnnotation> findTopByPdbAnnoKeyStartingWith(String pdbAnnoKeyPart);//2pcx_A_
    
    public StructureAnnotation findTopByPdbAnnoKey(String pdbAnnoKey); //2pcx_A_282

}
