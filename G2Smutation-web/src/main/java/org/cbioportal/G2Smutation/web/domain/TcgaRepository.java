package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Tcga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcgaRepository extends JpaRepository<Tcga, Long>{
    
    public List<Tcga> findByMutationId(int mutationId);
    
    public List<Tcga> findBychrPos(String chrPos);

}
