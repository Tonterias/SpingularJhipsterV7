package com.spingular.cms.service;

import com.spingular.cms.service.dto.TopicDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Topic}.
 */
public interface TopicService {
    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     * Partially updates a topic.
     *
     * @param topicDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopicDTO> partialUpdate(TopicDTO topicDTO);

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicDTO> findAll(Pageable pageable);

    /**
     * Get all the topics with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopicDTO> findOne(Long id);

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
