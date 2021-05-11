package com.spingular.cms.service;

import com.spingular.cms.service.dto.CcelebDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Cceleb}.
 */
public interface CcelebService {
    /**
     * Save a cceleb.
     *
     * @param ccelebDTO the entity to save.
     * @return the persisted entity.
     */
    CcelebDTO save(CcelebDTO ccelebDTO);

    /**
     * Partially updates a cceleb.
     *
     * @param ccelebDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CcelebDTO> partialUpdate(CcelebDTO ccelebDTO);

    /**
     * Get all the ccelebs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CcelebDTO> findAll(Pageable pageable);

    /**
     * Get all the ccelebs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CcelebDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cceleb.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CcelebDTO> findOne(Long id);

    /**
     * Delete the "id" cceleb.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
