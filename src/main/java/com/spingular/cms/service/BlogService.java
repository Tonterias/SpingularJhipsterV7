package com.spingular.cms.service;

import com.spingular.cms.service.dto.BlogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.spingular.cms.domain.Blog}.
 */
public interface BlogService {
    /**
     * Save a blog.
     *
     * @param blogDTO the entity to save.
     * @return the persisted entity.
     */
    BlogDTO save(BlogDTO blogDTO);

    /**
     * Partially updates a blog.
     *
     * @param blogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BlogDTO> partialUpdate(BlogDTO blogDTO);

    /**
     * Get all the blogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BlogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" blog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BlogDTO> findOne(Long id);

    /**
     * Delete the "id" blog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
