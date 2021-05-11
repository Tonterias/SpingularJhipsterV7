package com.spingular.cms.service;

import com.spingular.cms.domain.*; // for static metamodels
import com.spingular.cms.domain.Cactivity;
import com.spingular.cms.repository.CactivityRepository;
import com.spingular.cms.service.criteria.CactivityCriteria;
import com.spingular.cms.service.dto.CactivityDTO;
import com.spingular.cms.service.mapper.CactivityMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cactivity} entities in the database.
 * The main input is a {@link CactivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CactivityDTO} or a {@link Page} of {@link CactivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CactivityQueryService extends QueryService<Cactivity> {

    private final Logger log = LoggerFactory.getLogger(CactivityQueryService.class);

    private final CactivityRepository cactivityRepository;

    private final CactivityMapper cactivityMapper;

    public CactivityQueryService(CactivityRepository cactivityRepository, CactivityMapper cactivityMapper) {
        this.cactivityRepository = cactivityRepository;
        this.cactivityMapper = cactivityMapper;
    }

    /**
     * Return a {@link List} of {@link CactivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CactivityDTO> findByCriteria(CactivityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cactivity> specification = createSpecification(criteria);
        return cactivityMapper.toDto(cactivityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CactivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CactivityDTO> findByCriteria(CactivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cactivity> specification = createSpecification(criteria);
        return cactivityRepository.findAll(specification, page).map(cactivityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CactivityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cactivity> specification = createSpecification(criteria);
        return cactivityRepository.count(specification);
    }

    /**
     * Function to convert {@link CactivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cactivity> createSpecification(CactivityCriteria criteria) {
        Specification<Cactivity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cactivity_.id));
            }
            if (criteria.getActivityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivityName(), Cactivity_.activityName));
            }
            if (criteria.getCommunityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityId(),
                            root -> root.join(Cactivity_.communities, JoinType.LEFT).get(Community_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
