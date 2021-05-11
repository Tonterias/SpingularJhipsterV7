package com.spingular.cms.service;

import com.spingular.cms.service.dto.AppphotoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Appphoto}.
 */
public interface AppphotoService {
    /**
     * Save a appphoto.
     *
     * @param appphotoDTO the entity to save.
     * @return the persisted entity.
     */
    AppphotoDTO save(AppphotoDTO appphotoDTO);

    /**
     * Partially updates a appphoto.
     *
     * @param appphotoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppphotoDTO> partialUpdate(AppphotoDTO appphotoDTO);

    /**
     * Get all the appphotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppphotoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appphoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppphotoDTO> findOne(Long id);

    /**
     * Delete the "id" appphoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
