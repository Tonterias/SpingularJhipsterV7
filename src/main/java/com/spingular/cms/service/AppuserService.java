package com.spingular.cms.service;

import com.spingular.cms.service.dto.AppuserDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Appuser}.
 */
public interface AppuserService {
    /**
     * Save a appuser.
     *
     * @param appuserDTO the entity to save.
     * @return the persisted entity.
     */
    AppuserDTO save(AppuserDTO appuserDTO);

    /**
     * Partially updates a appuser.
     *
     * @param appuserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppuserDTO> partialUpdate(AppuserDTO appuserDTO);

    /**
     * Get all the appusers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppuserDTO> findAll(Pageable pageable);
    /**
     * Get all the AppuserDTO where Appphoto is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AppuserDTO> findAllWhereAppphotoIsNull();

    /**
     * Get the "id" appuser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppuserDTO> findOne(Long id);

    /**
     * Delete the "id" appuser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
