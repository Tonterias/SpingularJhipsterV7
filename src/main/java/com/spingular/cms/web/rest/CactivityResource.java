package com.spingular.cms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spingular.cms.repository.CactivityRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.CactivityQueryService;
import com.spingular.cms.service.CactivityService;
import com.spingular.cms.service.criteria.CactivityCriteria;
import com.spingular.cms.service.dto.CactivityDTO;
import com.spingular.cms.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.spingular.cms.domain.Cactivity}.
 */
@RestController
@RequestMapping("/api")
public class CactivityResource {

    private final Logger log = LoggerFactory.getLogger(CactivityResource.class);

    private static final String ENTITY_NAME = "cactivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CactivityService cactivityService;

    private final CactivityRepository cactivityRepository;

    private final CactivityQueryService cactivityQueryService;

    public CactivityResource(CactivityService cactivityService, CactivityRepository cactivityRepository,
            CactivityQueryService cactivityQueryService) {
        this.cactivityService = cactivityService;
        this.cactivityRepository = cactivityRepository;
        this.cactivityQueryService = cactivityQueryService;
    }

    /**
     * {@code POST  /cactivities} : Create a new cactivity.
     *
     * @param cactivityDTO the cactivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new cactivityDTO, or with status {@code 400 (Bad Request)}
     *         if the cactivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cactivities")
    public ResponseEntity<CactivityDTO> createCactivity(@Valid @RequestBody CactivityDTO cactivityDTO)
            throws URISyntaxException {
        log.debug("REST request to save Cactivity : {}", cactivityDTO);
        if (cactivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new cactivity cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // NOTE: If anyone can use this Object, anyone can Create & Read any object
        CactivityDTO result = new CactivityDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)
                || SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = cactivityService.save(cactivityDTO);
        }

        return ResponseEntity
                .created(new URI("/api/cactivities/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /cactivities/:id} : Updates an existing cactivity.
     *
     * @param id           the id of the cactivityDTO to save.
     * @param cactivityDTO the cactivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated cactivityDTO, or with status {@code 400 (Bad Request)} if
     *         the cactivityDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the cactivityDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cactivities/{id}")
    public ResponseEntity<CactivityDTO> updateCactivity(@PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody CactivityDTO cactivityDTO) throws URISyntaxException {
        log.debug("REST request to update Cactivity : {}, {}", id, cactivityDTO);
        if (cactivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cactivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cactivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE: Only admins can change an Object (that belongs to everyone)
        CactivityDTO result = new CactivityDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = cactivityService.save(cactivityDTO);
        }

        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cactivityDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /cactivities/:id} : Partial updates given fields of an existing
     * cactivity, field will ignore if it is null
     *
     * @param id           the id of the cactivityDTO to save.
     * @param cactivityDTO the cactivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated cactivityDTO, or with status {@code 400 (Bad Request)} if
     *         the cactivityDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the cactivityDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the cactivityDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cactivities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CactivityDTO> partialUpdateCactivity(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody CactivityDTO cactivityDTO) throws URISyntaxException {
        log.debug("REST request to partial update Cactivity partially : {}, {}", id, cactivityDTO);
        if (cactivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cactivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cactivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE: Only admins can change an Object (that belongs to everyone)
        Optional<CactivityDTO> result = cactivityService.partialUpdate(cactivityDTO);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            if (result.isPresent()) {
                cactivityService.save(cactivityDTO);
            }
        }

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
                ENTITY_NAME, cactivityDTO.getId().toString()));
    }

    /**
     * {@code GET  /cactivities} : get all the cactivities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of cactivities in body.
     */
    @GetMapping("/cactivities")
    public ResponseEntity<List<CactivityDTO>> getAllCactivities(CactivityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cactivities by criteria: {}", criteria);

        // NOTE: If anyone can use this Object, anyone can Create & Read any object
        Page<CactivityDTO> page = cactivityQueryService.findByCriteria(criteria, pageable);

        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cactivities/count} : count all the cactivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/cactivities/count")
    public ResponseEntity<Long> countCactivities(CactivityCriteria criteria) {
        log.debug("REST request to count Cactivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(cactivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cactivities/:id} : get the "id" cactivity.
     *
     * @param id the id of the cactivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the cactivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cactivities/{id}")
    public ResponseEntity<CactivityDTO> getCactivity(@PathVariable Long id) {
        log.debug("REST request to get Cactivity : {}", id);

        // NOTE: If anyone can use this Object, anyone can Create & Read any object
        Optional<CactivityDTO> cactivityDTO = cactivityService.findOne(id);

        return ResponseUtil.wrapOrNotFound(cactivityDTO);
    }

    /**
     * {@code DELETE  /cactivities/:id} : delete the "id" cactivity.
     *
     * @param id the id of the cactivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cactivities/{id}")
    public ResponseEntity<Void> deleteCactivity(@PathVariable Long id) {
        log.debug("REST request to delete Cactivity : {}", id);

        // NOTE: Only admins can delete an Object (that belongs to everyone)
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            cactivityService.delete(id);
        }

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
