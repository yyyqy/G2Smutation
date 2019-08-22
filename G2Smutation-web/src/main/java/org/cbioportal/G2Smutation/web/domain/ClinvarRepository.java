package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinvarRepository extends JpaRepository<Clinvar, String>{
    
    public List<Clinvar> findByChrPos(String chrPos);
    
    public List<Clinvar> findByMutationNo(String seqIdpos);
    
    public Clinvar findTopByClinvarId(int clinvarId);
    
    public List<Clinvar> findByClinvarId(int clinvarId);

}

