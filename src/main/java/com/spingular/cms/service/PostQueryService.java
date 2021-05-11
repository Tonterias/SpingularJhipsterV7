package com.spingular.cms.service;

import com.spingular.cms.domain.*; // for static metamodels
import com.spingular.cms.domain.Post;
import com.spingular.cms.repository.PostRepository;
import com.spingular.cms.service.criteria.PostCriteria;
import com.spingular.cms.service.dto.PostDTO;
import com.spingular.cms.service.mapper.PostMapper;
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
 * Service for executing complex queries for {@link Post} entities in the database.
 * The main input is a {@link PostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostDTO} or a {@link Page} of {@link PostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostQueryService extends QueryService<Post> {

    private final Logger log = LoggerFactory.getLogger(PostQueryService.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostQueryService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    /**
     * Return a {@link List} of {@link PostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostDTO> findByCriteria(PostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Post> specification = createSpecification(criteria);
        return postMapper.toDto(postRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostDTO> findByCriteria(PostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Post> specification = createSpecification(criteria);
        return postRepository.findAll(specification, page).map(postMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Post> specification = createSpecification(criteria);
        return postRepository.count(specification);
    }

    /**
     * Function to convert {@link PostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Post> createSpecification(PostCriteria criteria) {
        Specification<Post> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Post_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Post_.creationDate));
            }
            if (criteria.getPublicationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublicationDate(), Post_.publicationDate));
            }
            if (criteria.getHeadline() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeadline(), Post_.headline));
            }
            if (criteria.getLeadtext() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeadtext(), Post_.leadtext));
            }
            if (criteria.getBodytext() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBodytext(), Post_.bodytext));
            }
            if (criteria.getQuote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuote(), Post_.quote));
            }
            if (criteria.getConclusion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConclusion(), Post_.conclusion));
            }
            if (criteria.getLinkText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkText(), Post_.linkText));
            }
            if (criteria.getLinkURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkURL(), Post_.linkURL));
            }
            if (criteria.getCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommentId(), root -> root.join(Post_.comments, JoinType.LEFT).get(Comment_.id))
                    );
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Post_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
            if (criteria.getBlogId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getBlogId(), root -> root.join(Post_.blog, JoinType.LEFT).get(Blog_.id)));
            }
            if (criteria.getTagId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTagId(), root -> root.join(Post_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getTopicId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTopicId(), root -> root.join(Post_.topics, JoinType.LEFT).get(Topic_.id))
                    );
            }
        }
        return specification;
    }
}
