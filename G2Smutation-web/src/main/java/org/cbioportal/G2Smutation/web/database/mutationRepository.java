package org.cbioportal.G2Smutation.web.database;

import org.cbioportal.G2Smutation.web.database.mutation_usage_table;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface mutationRepository extends JpaRepository<mutation_usage_table, Integer> {
	
	List<mutation_usage_table> findTop20BymutationIdGreaterThan(Integer id);
	List<mutation_usage_table> findBypdbNoStartingWith(String id);
	List<mutation_usage_table> findByseqId(Integer id);
	
	/* 3*3 */
	List<mutation_usage_table> findBySeqResidueOrderByPdbIndex(String id);
	List<mutation_usage_table> findBypdbIndex(Integer id);
	List<mutation_usage_table> findBypdbResidueOrderByPdbIndex(String id);
	
	List<mutation_usage_table> findBypdbIndexAndPdbResidue(Integer id1,String id2);
	List<mutation_usage_table> findByseqResidueAndPdbResidue(String id1,String id2);
	List<mutation_usage_table> findBypdbIndexAndSeqResidue(Integer id1,String id2);
    
	List<mutation_usage_table> findBySeqResidueAndPdbResidueAndPdbIndex(String id1,String id2,Integer id3);
}
