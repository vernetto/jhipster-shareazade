package org.pierre.shareazade.repository;

import org.pierre.shareazade.domain.Users;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Users entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {}
