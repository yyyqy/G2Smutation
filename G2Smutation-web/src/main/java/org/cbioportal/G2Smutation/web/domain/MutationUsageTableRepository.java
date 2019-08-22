package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import javax.transaction.Transactional;

import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MutationUsageTableRepository extends JpaRepository<MutationUsageTable, Integer> {
    
    public List<MutationUsageTable> findByMutationId (int mutationId);
    
    public List<MutationUsageTable> findBySeqId (int seqId);
    
    public List<MutationUsageTable> findByMutationNo(String mutationNo);
    
    public List<MutationUsageTable> findAll();
    public List<MutationUsageTable> findBySeqIdOrderBySeqIndex(int seqIndex);
    
    //public List<MutationUsageTable> findTop100ByMutationId (int mutationId);

}
