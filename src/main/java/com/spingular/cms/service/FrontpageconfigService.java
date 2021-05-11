package com.spingular.cms.service;

import com.spingular.cms.service.dto.FrontpageconfigDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Frontpageconfig}.
 */
public interface FrontpageconfigService {
    /**
     * Save a frontpageconfig.
     *
     * @param frontpageconfigDTO the entity to save.
     * @return the persisted entity.
     */
    FrontpageconfigDTO save(FrontpageconfigDTO frontpageconfigDTO);

    /**
     * Partially updates a frontpageconfig.
     *
     * @param frontpageconfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FrontpageconfigDTO> partialUpdate(FrontpageconfigDTO frontpageconfigDTO);

    /**
     * Get all the frontpageconfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrontpageconfigDTO> findAll(Pageable pageable);

    /**
     * Get the "id" frontpageconfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FrontpageconfigDTO> findOne(Long id);

    /**
     * Delete the "id" frontpageconfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
