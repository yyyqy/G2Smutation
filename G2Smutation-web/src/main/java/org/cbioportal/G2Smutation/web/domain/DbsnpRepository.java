package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.Dbsnp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbsnpRepository extends JpaRepository<Dbsnp, String>{
    
    public List<Dbsnp> findBychrPos(String chrPos);
    
    public List<Dbsnp> findByMutationNo(String seqIdpos);

}

