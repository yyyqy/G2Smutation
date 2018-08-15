package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import javax.transaction.Transactional;

import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Juexin Wang
 *
 */
@Transactional
public interface EnsemblRepository extends JpaRepository<Ensembl, Long> {
    public List<Ensembl> findByEnsemblId(String ensemblId);

    public List<Ensembl> findByEnsemblIdStartingWith(String ensemblId);

}
