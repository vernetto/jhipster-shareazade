package org.pierre.shareazade.repository;

import java.util.List;
import java.util.Optional;
import org.pierre.shareazade.domain.ShareRide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShareRide entity.
 */
@Repository
public interface ShareRideRepository extends JpaRepository<ShareRide, Long>, JpaSpecificationExecutor<ShareRide> {
    @Query("select shareRide from ShareRide shareRide where shareRide.user.login = ?#{principal.username}")
    List<ShareRide> findByUserIsCurrentUser();

    default Optional<ShareRide> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShareRide> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShareRide> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct shareRide from ShareRide shareRide left join fetch shareRide.rideCityFrom left join fetch shareRide.rideCityTo left join fetch shareRide.user",
        countQuery = "select count(distinct shareRide) from ShareRide shareRide"
    )
    Page<ShareRide> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct shareRide from ShareRide shareRide left join fetch shareRide.rideCityFrom left join fetch shareRide.rideCityTo left join fetch shareRide.user"
    )
    List<ShareRide> findAllWithToOneRelationships();

    @Query(
        "select shareRide from ShareRide shareRide left join fetch shareRide.rideCityFrom left join fetch shareRide.rideCityTo left join fetch shareRide.user where shareRide.id =:id"
    )
    Optional<ShareRide> findOneWithToOneRelationships(@Param("id") Long id);
}
