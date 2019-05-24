package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MutationUsageTableRepository extends JpaRepository<MutationUsageTable, Long>{
    
    public List<MutationUsageTable> findByMutationId (int mutationId);
    
    public List<MutationUsageTable> findBySeqId (int seqId);

}
