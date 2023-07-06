package org.pierre.shareazade.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.pierre.shareazade.domain.*; // for static metamodels
import org.pierre.shareazade.domain.ShareUser;
import org.pierre.shareazade.repository.ShareUserRepository;
import org.pierre.shareazade.service.criteria.ShareUserCriteria;
import org.pierre.shareazade.service.dto.ShareUserDTO;
import org.pierre.shareazade.service.mapper.ShareUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ShareUser} entities in the database.
 * The main input is a {@link ShareUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShareUserDTO} or a {@link Page} of {@link ShareUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShareUserQueryService extends QueryService<ShareUser> {

    private final Logger log = LoggerFactory.getLogger(ShareUserQueryService.class);

    private final ShareUserRepository shareUserRepository;

    private final ShareUserMapper shareUserMapper;

    public ShareUserQueryService(ShareUserRepository shareUserRepository, ShareUserMapper shareUserMapper) {
        this.shareUserRepository = shareUserRepository;
        this.shareUserMapper = shareUserMapper;
    }

    /**
     * Return a {@link List} of {@link ShareUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShareUserDTO> findByCriteria(ShareUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShareUser> specification = createSpecification(criteria);
        return shareUserMapper.toDto(shareUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShareUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareUserDTO> findByCriteria(ShareUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShareUser> specification = createSpecification(criteria);
        return shareUserRepository.findAll(specification, page).map(shareUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShareUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShareUser> specification = createSpecification(criteria);
        return shareUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ShareUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShareUser> createSpecification(ShareUserCriteria criteria) {
        Specification<ShareUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShareUser_.id));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), ShareUser_.userName));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), ShareUser_.userEmail));
            }
            if (criteria.getUserRole() != null) {
                specification = specification.and(buildSpecification(criteria.getUserRole(), ShareUser_.userRole));
            }
            if (criteria.getUserPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserPhone(), ShareUser_.userPhone));
            }
            if (criteria.getUserStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStatus(), ShareUser_.userStatus));
            }
        }
        return specification;
    }
}
