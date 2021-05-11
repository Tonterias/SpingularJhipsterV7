package com.spingular.cms.web.rest;

import com.spingular.cms.repository.AppuserRepository;
import com.spingular.cms.service.AppuserQueryService;
import com.spingular.cms.service.AppuserService;
import com.spingular.cms.service.criteria.AppuserCriteria;
import com.spingular.cms.service.dto.AppuserDTO;
import com.spingular.cms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.spingular.cms.domain.Appuser}.
 */
@RestController
@RequestMapping("/api")
public class AppuserResource {

    private final Logger log = LoggerFactory.getLogger(AppuserResource.class);

    private static final String ENTITY_NAME = "appuser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppuserService appuserService;

    private final AppuserRepository appuserRepository;

    private final AppuserQueryService appuserQueryService;

    public AppuserResource(AppuserService appuserService, AppuserRepository appuserRepository, AppuserQueryService appuserQueryService) {
        this.appuserService = appuserService;
        this.appuserRepository = appuserRepository;
        this.appuserQueryService = appuserQueryService;
    }

    /**
     * {@code POST  /appusers} : Create a new appuser.
     *
     * @param appuserDTO the appuserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appuserDTO, or with status {@code 400 (Bad Request)} if the appuser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appusers")
    public ResponseEntity<AppuserDTO> createAppuser(@Valid @RequestBody AppuserDTO appuserDTO) throws URISyntaxException {
        log.debug("REST request to save Appuser : {}", appuserDTO);
        if (appuserDTO.getId() != null) {
            throw new BadRequestAlertException("A new appuser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppuserDTO result = appuserService.save(appuserDTO);
        return ResponseEntity
            .created(new URI("/api/appusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appusers/:id} : Updates an existing appuser.
     *
     * @param id the id of the appuserDTO to save.
     * @param appuserDTO the appuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appuserDTO,
     * or with status {@code 400 (Bad Request)} if the appuserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appuserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appusers/{id}")
    public ResponseEntity<AppuserDTO> updateAppuser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppuserDTO appuserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Appuser : {}, {}", id, appuserDTO);
        if (appuserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appuserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppuserDTO result = appuserService.save(appuserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appuserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appusers/:id} : Partial updates given fields of an existing appuser, field will ignore if it is null
     *
     * @param id the id of the appuserDTO to save.
     * @param appuserDTO the appuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appuserDTO,
     * or with status {@code 400 (Bad Request)} if the appuserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appuserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appuserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appusers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppuserDTO> partialUpdateAppuser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppuserDTO appuserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appuser partially : {}, {}", id, appuserDTO);
        if (appuserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appuserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppuserDTO> result = appuserService.partialUpdate(appuserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appuserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appusers} : get all the appusers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appusers in body.
     */
    @GetMapping("/appusers")
    public ResponseEntity<List<AppuserDTO>> getAllAppusers(AppuserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appusers by criteria: {}", criteria);
        Page<AppuserDTO> page = appuserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appusers/count} : count all the appusers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/appusers/count")
    public ResponseEntity<Long> countAppusers(AppuserCriteria criteria) {
        log.debug("REST request to count Appusers by criteria: {}", criteria);
        return ResponseEntity.ok().body(appuserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /appusers/:id} : get the "id" appuser.
     *
     * @param id the id of the appuserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appuserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appusers/{id}")
    public ResponseEntity<AppuserDTO> getAppuser(@PathVariable Long id) {
        log.debug("REST request to get Appuser : {}", id);
        Optional<AppuserDTO> appuserDTO = appuserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appuserDTO);
    }

    /**
     * {@code DELETE  /appusers/:id} : delete the "id" appuser.
     *
     * @param id the id of the appuserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appusers/{id}")
    public ResponseEntity<Void> deleteAppuser(@PathVariable Long id) {
        log.debug("REST request to delete Appuser : {}", id);
        appuserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
