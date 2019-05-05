package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CosmicRepository extends JpaRepository<Cosmic, Long>{
    
    public List<Cosmic> findByMutationId(int mutationId);
    
    public List<Cosmic> findBychrPos(String chrPos);

}
