package org.pierre.shareazade.repository;

import java.util.List;
import java.util.Optional;
import org.pierre.shareazade.domain.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ride entity.
 */
@Repository
public interface RideRepository extends JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {
    default Optional<Ride> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ride> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ride> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ride from Ride ride left join fetch ride.rideUser left join fetch ride.rideCityFrom left join fetch ride.rideCityTo",
        countQuery = "select count(distinct ride) from Ride ride"
    )
    Page<Ride> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct ride from Ride ride left join fetch ride.rideUser left join fetch ride.rideCityFrom left join fetch ride.rideCityTo"
    )
    List<Ride> findAllWithToOneRelationships();

    @Query(
        "select ride from Ride ride left join fetch ride.rideUser left join fetch ride.rideCityFrom left join fetch ride.rideCityTo where ride.id =:id"
    )
    Optional<Ride> findOneWithToOneRelationships(@Param("id") Long id);
}
