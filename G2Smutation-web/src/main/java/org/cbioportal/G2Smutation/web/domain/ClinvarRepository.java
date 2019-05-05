package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinvarRepository extends JpaRepository<Clinvar, Long>{
    
    public List<Clinvar> findByMutationId(int mutationId);
    
    public List<Clinvar> findByChrPos(String chrPos);

}