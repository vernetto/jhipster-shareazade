package org.pierre.shareazade.repository;

import org.pierre.shareazade.domain.ShareUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShareUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareUserRepository extends JpaRepository<ShareUser, Long>, JpaSpecificationExecutor<ShareUser> {}
