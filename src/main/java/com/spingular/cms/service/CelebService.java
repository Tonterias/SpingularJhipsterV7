package com.spingular.cms.service;

import com.spingular.cms.service.dto.CelebDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Celeb}.
 */
public interface CelebService {
    /**
     * Save a celeb.
     *
     * @param celebDTO the entity to save.
     * @return the persisted entity.
     */
    CelebDTO save(CelebDTO celebDTO);

    /**
     * Partially updates a celeb.
     *
     * @param celebDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CelebDTO> partialUpdate(CelebDTO celebDTO);

    /**
     * Get all the celebs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CelebDTO> findAll(Pageable pageable);

    /**
     * Get all the celebs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CelebDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" celeb.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CelebDTO> findOne(Long id);

    /**
     * Delete the "id" celeb.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
