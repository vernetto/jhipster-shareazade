package org.pierre.shareazade.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.pierre.shareazade.domain.*; // for static metamodels
import org.pierre.shareazade.domain.Users;
import org.pierre.shareazade.repository.UsersRepository;
import org.pierre.shareazade.service.criteria.UsersCriteria;
import org.pierre.shareazade.service.dto.UsersDTO;
import org.pierre.shareazade.service.mapper.UsersMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Users} entities in the database.
 * The main input is a {@link UsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsersDTO} or a {@link Page} of {@link UsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsersQueryService extends QueryService<Users> {

    private final Logger log = LoggerFactory.getLogger(UsersQueryService.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersQueryService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Return a {@link List} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsersDTO> findByCriteria(UsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersMapper.toDto(usersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsersDTO> findByCriteria(UsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.findAll(specification, page).map(usersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.count(specification);
    }

    /**
     * Function to convert {@link UsersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Users> createSpecification(UsersCriteria criteria) {
        Specification<Users> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), Users_.userName));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), Users_.userEmail));
            }
            if (criteria.getUserRole() != null) {
                specification = specification.and(buildSpecification(criteria.getUserRole(), Users_.userRole));
            }
            if (criteria.getUserPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserPhone(), Users_.userPhone));
            }
            if (criteria.getUserStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStatus(), Users_.userStatus));
            }
        }
        return specification;
    }
}
