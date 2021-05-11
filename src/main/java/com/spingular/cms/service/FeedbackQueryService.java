package com.spingular.cms.service;

import com.spingular.cms.domain.*; // for static metamodels
import com.spingular.cms.domain.Feedback;
import com.spingular.cms.repository.FeedbackRepository;
import com.spingular.cms.service.criteria.FeedbackCriteria;
import com.spingular.cms.service.dto.FeedbackDTO;
import com.spingular.cms.service.mapper.FeedbackMapper;
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
 * Service for executing complex queries for {@link Feedback} entities in the database.
 * The main input is a {@link FeedbackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FeedbackDTO} or a {@link Page} of {@link FeedbackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeedbackQueryService extends QueryService<Feedback> {

    private final Logger log = LoggerFactory.getLogger(FeedbackQueryService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    public FeedbackQueryService(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }

    /**
     * Return a {@link List} of {@link FeedbackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FeedbackDTO> findByCriteria(FeedbackCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackMapper.toDto(feedbackRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FeedbackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> findByCriteria(FeedbackCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification, page).map(feedbackMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeedbackCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.count(specification);
    }

    /**
     * Function to convert {@link FeedbackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Feedback> createSpecification(FeedbackCriteria criteria) {
        Specification<Feedback> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Feedback_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Feedback_.creationDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Feedback_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Feedback_.email));
            }
            if (criteria.getFeedback() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeedback(), Feedback_.feedback));
            }
        }
        return specification;
    }
}
