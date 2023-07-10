package org.pierre.shareazade.repository;

import java.util.List;
import java.util.Optional;
import org.pierre.shareazade.domain.ShareCity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShareCity entity.
 */
@Repository
public interface ShareCityRepository extends JpaRepository<ShareCity, Long>, JpaSpecificationExecutor<ShareCity> {
    @Query("select shareCity from ShareCity shareCity where shareCity.user.login = ?#{principal.username}")
    List<ShareCity> findByUserIsCurrentUser();

    default Optional<ShareCity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShareCity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShareCity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct shareCity from ShareCity shareCity left join fetch shareCity.user",
        countQuery = "select count(distinct shareCity) from ShareCity shareCity"
    )
    Page<ShareCity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct shareCity from ShareCity shareCity left join fetch shareCity.user")
    List<ShareCity> findAllWithToOneRelationships();

    @Query("select shareCity from ShareCity shareCity left join fetch shareCity.user where shareCity.id =:id")
    Optional<ShareCity> findOneWithToOneRelationships(@Param("id") Long id);
}
