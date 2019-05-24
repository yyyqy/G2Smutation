package org.cbioportal.G2Smutation.web.domain;

import java.util.List;


import org.cbioportal.G2Smutation.web.models.db.Genie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenieRepository extends JpaRepository<Genie, Long>{
    
    public List<Genie> findByMutationId(int mutationId);
    
    public List<Genie> findBychrPos(String chrPos);

}

