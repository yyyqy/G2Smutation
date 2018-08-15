package org.cbioportal.G2Smutation.web.domain;

import javax.transaction.Transactional;
import org.cbioportal.G2Smutation.web.models.Alignment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Juexin Wang
 *
 */
@Transactional
public interface PdbRepository extends JpaRepository<Alignment, Long> {
}
