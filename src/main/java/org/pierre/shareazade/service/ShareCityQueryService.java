package org.pierre.shareazade.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.pierre.shareazade.domain.*; // for static metamodels
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.repository.ShareCityRepository;
import org.pierre.shareazade.service.criteria.ShareCityCriteria;
import org.pierre.shareazade.service.dto.ShareCityDTO;
import org.pierre.shareazade.service.mapper.ShareCityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ShareCity} entities in the database.
 * The main input is a {@link ShareCityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShareCityDTO} or a {@link Page} of {@link ShareCityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShareCityQueryService extends QueryService<ShareCity> {

    private final Logger log = LoggerFactory.getLogger(ShareCityQueryService.class);

    private final ShareCityRepository shareCityRepository;

    private final ShareCityMapper shareCityMapper;

    public ShareCityQueryService(ShareCityRepository shareCityRepository, ShareCityMapper shareCityMapper) {
        this.shareCityRepository = shareCityRepository;
        this.shareCityMapper = shareCityMapper;
    }

    /**
     * Return a {@link List} of {@link ShareCityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShareCityDTO> findByCriteria(ShareCityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShareCity> specification = createSpecification(criteria);
        return shareCityMapper.toDto(shareCityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShareCityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareCityDTO> findByCriteria(ShareCityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShareCity> specification = createSpecification(criteria);
        return shareCityRepository.findAll(specification, page).map(shareCityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShareCityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShareCity> specification = createSpecification(criteria);
        return shareCityRepository.count(specification);
    }

    /**
     * Function to convert {@link ShareCityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShareCity> createSpecification(ShareCityCriteria criteria) {
        Specification<ShareCity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShareCity_.id));
            }
            if (criteria.getCityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCityName(), ShareCity_.cityName));
            }
            if (criteria.getCityCountry() != null) {
                specification = specification.and(buildSpecification(criteria.getCityCountry(), ShareCity_.cityCountry));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(ShareCity_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
