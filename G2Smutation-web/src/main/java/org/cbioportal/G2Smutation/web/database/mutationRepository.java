package org.cbioportal.G2Smutation.web.database;

import org.cbioportal.G2Smutation.web.database.mutation_usage_table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete
public interface mutationRepository extends JpaRepository<mutation_usage_table, Integer> {
	
	List<mutation_usage_table> findBydatabaseIdBetween(Integer id1,Integer id2);
	List<mutation_usage_table> findByalignmentId(Integer id);

	List<mutation_usage_table> findBypdbNoStartingWith(String id);
	//List<mutation_usage_table> findBypdbNo(String id);
	
	/* 3*3 */
	List<mutation_usage_table> findBySeqResidueOrderByPdbIndex(String id);
	List<mutation_usage_table> findBypdbIndex(Integer id);
	List<mutation_usage_table> findBypdbResidueOrderByPdbIndex(String id);
	
	List<mutation_usage_table> findBypdbIndexAndPdbResidue(Integer id1,String id2);
	List<mutation_usage_table> findByseqResidueAndPdbResidue(String id1,String id2);
	List<mutation_usage_table> findBypdbIndexAndSeqResidue(Integer id1,String id2);
    
	List<mutation_usage_table> findBySeqResidueAndPdbResidueAndPdbIndex(String id1,String id2,Integer id3);
	// EVALUE、BITSCORE、IDENTITY、IDENTP
	//List<mutation_usage_table> findByevalueBetween(Integer id1,Integer id2);
	//List<mutation_usage_table> findBybitscoreBetween(Double id1,Double id2);
	//List<mutation_usage_table> findByidentityBetween(Integer id1,Integer id2);
	//List<mutation_usage_table> findByidentpBetween(Double id1,Double id2);	
}
