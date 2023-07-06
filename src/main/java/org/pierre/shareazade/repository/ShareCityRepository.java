package org.pierre.shareazade.repository;

import org.pierre.shareazade.domain.ShareCity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShareCity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareCityRepository extends JpaRepository<ShareCity, Long>, JpaSpecificationExecutor<ShareCity> {}
