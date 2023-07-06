package org.pierre.shareazade.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.pierre.shareazade.domain.*; // for static metamodels
import org.pierre.shareazade.domain.Ride;
import org.pierre.shareazade.repository.RideRepository;
import org.pierre.shareazade.service.criteria.RideCriteria;
import org.pierre.shareazade.service.dto.RideDTO;
import org.pierre.shareazade.service.mapper.RideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ride} entities in the database.
 * The main input is a {@link RideCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RideDTO} or a {@link Page} of {@link RideDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RideQueryService extends QueryService<Ride> {

    private final Logger log = LoggerFactory.getLogger(RideQueryService.class);

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    public RideQueryService(RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
    }

    /**
     * Return a {@link List} of {@link RideDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RideDTO> findByCriteria(RideCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ride> specification = createSpecification(criteria);
        return rideMapper.toDto(rideRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RideDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RideDTO> findByCriteria(RideCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ride> specification = createSpecification(criteria);
        return rideRepository.findAll(specification, page).map(rideMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RideCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ride> specification = createSpecification(criteria);
        return rideRepository.count(specification);
    }

    /**
     * Function to convert {@link RideCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ride> createSpecification(RideCriteria criteria) {
        Specification<Ride> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ride_.id));
            }
            if (criteria.getRideDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRideDateTime(), Ride_.rideDateTime));
            }
            if (criteria.getRideCityFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRideCityFrom(), Ride_.rideCityFrom));
            }
            if (criteria.getRideCityTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRideCityTo(), Ride_.rideCityTo));
            }
            if (criteria.getRideType() != null) {
                specification = specification.and(buildSpecification(criteria.getRideType(), Ride_.rideType));
            }
            if (criteria.getRideUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRideUserId(), root -> root.join(Ride_.rideUser, JoinType.LEFT).get(Users_.id))
                    );
            }
            if (criteria.getRideCityFromId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRideCityFromId(), root -> root.join(Ride_.rideCityFrom, JoinType.LEFT).get(City_.id))
                    );
            }
            if (criteria.getRideCityToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRideCityToId(), root -> root.join(Ride_.rideCityTo, JoinType.LEFT).get(City_.id))
                    );
            }
        }
        return specification;
    }
}
