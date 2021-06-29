package com.spingular.cms.web.rest;

import com.spingular.cms.repository.FrontpageconfigRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.FrontpageconfigQueryService;
import com.spingular.cms.service.FrontpageconfigService;
import com.spingular.cms.service.criteria.FrontpageconfigCriteria;
import com.spingular.cms.service.dto.CustomFrontpageconfigDTO;
import com.spingular.cms.service.dto.FrontpageconfigDTO;
import com.spingular.cms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.spingular.cms.domain.Frontpageconfig}.
 */
@RestController
@RequestMapping("/api")
public class FrontpageconfigResource {

    private final Logger log = LoggerFactory.getLogger(FrontpageconfigResource.class);

    private static final String ENTITY_NAME = "frontpageconfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrontpageconfigService frontpageconfigService;

    private final FrontpageconfigRepository frontpageconfigRepository;

    private final FrontpageconfigQueryService frontpageconfigQueryService;

    public FrontpageconfigResource(
        FrontpageconfigService frontpageconfigService,
        FrontpageconfigRepository frontpageconfigRepository,
        FrontpageconfigQueryService frontpageconfigQueryService
    ) {
        this.frontpageconfigService = frontpageconfigService;
        this.frontpageconfigRepository = frontpageconfigRepository;
        this.frontpageconfigQueryService = frontpageconfigQueryService;
    }

    /**
     * {@code POST  /frontpageconfigs} : Create a new frontpageconfig.
     *
     * @param frontpageconfigDTO the frontpageconfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new frontpageconfigDTO, or with status
     *         {@code 400 (Bad Request)} if the frontpageconfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frontpageconfigs")
    public ResponseEntity<FrontpageconfigDTO> createFrontpageconfig(@Valid @RequestBody FrontpageconfigDTO frontpageconfigDTO)
        throws URISyntaxException {
        log.debug("REST request to save Frontpageconfig : {}", frontpageconfigDTO);
        if (frontpageconfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new frontpageconfig cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // NOTE Only for Admins
        FrontpageconfigDTO result = new FrontpageconfigDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = frontpageconfigService.save(frontpageconfigDTO);
            log.debug("Frontpageconfig DTO to create, belongs to current user: {}", frontpageconfigDTO.toString());
        }

        return ResponseEntity
            .created(new URI("/api/frontpageconfigs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frontpageconfigs/:id} : Updates an existing frontpageconfig.
     *
     * @param id                 the id of the frontpageconfigDTO to save.
     * @param frontpageconfigDTO the frontpageconfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated frontpageconfigDTO, or with status
     *         {@code 400 (Bad Request)} if the frontpageconfigDTO is not valid, or
     *         with status {@code 500 (Internal Server Error)} if the
     *         frontpageconfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frontpageconfigs/{id}")
    public ResponseEntity<FrontpageconfigDTO> updateFrontpageconfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrontpageconfigDTO frontpageconfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Frontpageconfig : {}, {}", id, frontpageconfigDTO);
        if (frontpageconfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frontpageconfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frontpageconfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        FrontpageconfigDTO result = new FrontpageconfigDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = frontpageconfigService.save(frontpageconfigDTO);
            log.debug("Frontpageconfig DTO to create, belongs to current user: {}", frontpageconfigDTO.toString());
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frontpageconfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frontpageconfigs/:id} : Partial updates given fields of an
     * existing frontpageconfig, field will ignore if it is null
     *
     * @param id                 the id of the frontpageconfigDTO to save.
     * @param frontpageconfigDTO the frontpageconfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated frontpageconfigDTO, or with status
     *         {@code 400 (Bad Request)} if the frontpageconfigDTO is not valid, or
     *         with status {@code 404 (Not Found)} if the frontpageconfigDTO is not
     *         found, or with status {@code 500 (Internal Server Error)} if the
     *         frontpageconfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frontpageconfigs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FrontpageconfigDTO> partialUpdateFrontpageconfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrontpageconfigDTO frontpageconfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frontpageconfig partially : {}, {}", id, frontpageconfigDTO);
        if (frontpageconfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frontpageconfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frontpageconfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        Optional<FrontpageconfigDTO> result = frontpageconfigService.findOne(id);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = frontpageconfigService.partialUpdate(frontpageconfigDTO);
            log.debug("Frontpageconfig DTO to partial update, belongs to current user: {}", frontpageconfigService.toString());
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frontpageconfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frontpageconfigs} : get all the frontpageconfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of frontpageconfigs in body.
     */
    @GetMapping("/frontpageconfigs")
    public ResponseEntity<List<FrontpageconfigDTO>> getAllFrontpageconfigs(FrontpageconfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Frontpageconfigs by criteria: {}", criteria);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Page<FrontpageconfigDTO> page = frontpageconfigQueryService.findByCriteria(criteria, pageable);

            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code GET  /frontpageconfigs/count} : count all the frontpageconfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/frontpageconfigs/count")
    public ResponseEntity<Long> countFrontpageconfigs(FrontpageconfigCriteria criteria) {
        log.debug("REST request to count Frontpageconfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(frontpageconfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /frontpageconfigs/:id} : get the "id" frontpageconfig.
     *
     * @param id the id of the frontpageconfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the frontpageconfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frontpageconfigs/{id}")
    public ResponseEntity<FrontpageconfigDTO> getFrontpageconfig(@PathVariable Long id) {
        log.debug("REST request to get Frontpageconfig : {}", id);
        Optional<FrontpageconfigDTO> frontpageconfigDTO = frontpageconfigService.findOne(id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return ResponseUtil.wrapOrNotFound(frontpageconfigDTO);
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code DELETE  /frontpageconfigs/:id} : delete the "id" frontpageconfig.
     *
     * @param id the id of the frontpageconfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frontpageconfigs/{id}")
    public ResponseEntity<Void> deleteFrontpageconfig(@PathVariable Long id) {
        log.debug("REST request to delete Frontpageconfig : {}", id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            frontpageconfigService.delete(id);
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /frontpageconfigs/:id/posts : get the "id" frontpageconfig, including posts
     *
     * @param id the id of the frontpageconfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frontpageconfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frontpageconfigs/{id}/posts")
    // @Timed
    public ResponseEntity<CustomFrontpageconfigDTO> getFrontpageconfigIncludingPosts(@PathVariable Long id) {
        log.debug("REST request to get Frontpageconfig : {}", id);
        Optional<CustomFrontpageconfigDTO> frontpageconfigDTO = frontpageconfigService.findOneIncludingPosts(id);
        return ResponseUtil.wrapOrNotFound(frontpageconfigDTO);
    }
}
