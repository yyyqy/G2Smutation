package org.cbioportal.G2Smutation.web.domain;


import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CosmicRepository extends JpaRepository<Cosmic, String>{
    
    public List<Cosmic> findBychrPos(String chrPos);
    
    public List<Cosmic> findByMutationNo(String seqIdpos);
    
    public List<Cosmic> findByCosmicMutationId(String cosmicMutationId);

}

