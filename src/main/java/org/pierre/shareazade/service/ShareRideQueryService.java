package org.pierre.shareazade.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.pierre.shareazade.domain.*; // for static metamodels
import org.pierre.shareazade.domain.ShareRide;
import org.pierre.shareazade.repository.ShareRideRepository;
import org.pierre.shareazade.service.criteria.ShareRideCriteria;
import org.pierre.shareazade.service.dto.ShareRideDTO;
import org.pierre.shareazade.service.mapper.ShareRideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ShareRide} entities in the database.
 * The main input is a {@link ShareRideCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShareRideDTO} or a {@link Page} of {@link ShareRideDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShareRideQueryService extends QueryService<ShareRide> {

    private final Logger log = LoggerFactory.getLogger(ShareRideQueryService.class);

    private final ShareRideRepository shareRideRepository;

    private final ShareRideMapper shareRideMapper;

    public ShareRideQueryService(ShareRideRepository shareRideRepository, ShareRideMapper shareRideMapper) {
        this.shareRideRepository = shareRideRepository;
        this.shareRideMapper = shareRideMapper;
    }

    /**
     * Return a {@link List} of {@link ShareRideDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShareRideDTO> findByCriteria(ShareRideCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShareRide> specification = createSpecification(criteria);
        return shareRideMapper.toDto(shareRideRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShareRideDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareRideDTO> findByCriteria(ShareRideCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShareRide> specification = createSpecification(criteria);
        return shareRideRepository.findAll(specification, page).map(shareRideMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShareRideCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShareRide> specification = createSpecification(criteria);
        return shareRideRepository.count(specification);
    }

    /**
     * Function to convert {@link ShareRideCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShareRide> createSpecification(ShareRideCriteria criteria) {
        Specification<ShareRide> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShareRide_.id));
            }
            if (criteria.getRideDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRideDateTime(), ShareRide_.rideDateTime));
            }
            if (criteria.getRideType() != null) {
                specification = specification.and(buildSpecification(criteria.getRideType(), ShareRide_.rideType));
            }
            if (criteria.getRideCityFromId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRideCityFromId(),
                            root -> root.join(ShareRide_.rideCityFrom, JoinType.LEFT).get(ShareCity_.id)
                        )
                    );
            }
            if (criteria.getRideCityToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRideCityToId(),
                            root -> root.join(ShareRide_.rideCityTo, JoinType.LEFT).get(ShareCity_.id)
                        )
                    );
            }
            if (criteria.getRideUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRideUserId(),
                            root -> root.join(ShareRide_.rideUser, JoinType.LEFT).get(ShareUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
