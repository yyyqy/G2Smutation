package org.cbioportal.G2Smutation.web.domain;

import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

//Do not need it
//TODO will delete later
public interface StructureRepository extends JpaRepository<StructureAnnotation, Integer> {
	List<StructureAnnotation> findBymutationId(Integer id);
}