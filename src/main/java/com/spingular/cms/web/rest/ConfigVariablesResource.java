package com.spingular.cms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.spingular.cms.repository.ConfigVariablesRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.ConfigVariablesQueryService;
import com.spingular.cms.service.ConfigVariablesService;
import com.spingular.cms.service.criteria.ConfigVariablesCriteria;
import com.spingular.cms.service.dto.ConfigVariablesDTO;
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
 * REST controller for managing
 * {@link com.spingular.cms.domain.ConfigVariables}.
 */
@RestController
@RequestMapping("/api")
public class ConfigVariablesResource {

    private final Logger log = LoggerFactory.getLogger(ConfigVariablesResource.class);

    private static final String ENTITY_NAME = "configVariables";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigVariablesService configVariablesService;

    private final ConfigVariablesRepository configVariablesRepository;

    private final ConfigVariablesQueryService configVariablesQueryService;

    public ConfigVariablesResource(ConfigVariablesService configVariablesService,
            ConfigVariablesRepository configVariablesRepository,
            ConfigVariablesQueryService configVariablesQueryService) {
        this.configVariablesService = configVariablesService;
        this.configVariablesRepository = configVariablesRepository;
        this.configVariablesQueryService = configVariablesQueryService;
    }

    /**
     * {@code POST  /config-variables} : Create a new configVariables.
     *
     * @param configVariablesDTO the configVariablesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new configVariablesDTO, or with status
     *         {@code 400 (Bad Request)} if the configVariables has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-variables")
    public ResponseEntity<ConfigVariablesDTO> createConfigVariables(@RequestBody ConfigVariablesDTO configVariablesDTO)
            throws URISyntaxException {
        log.debug("REST request to save ConfigVariables : {}", configVariablesDTO);
        if (configVariablesDTO.getId() != null) {
            throw new BadRequestAlertException("A new configVariables cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }

        // NOTE Only for Admins
        ConfigVariablesDTO result = new ConfigVariablesDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = configVariablesService.save(configVariablesDTO);
            log.debug("ConfigVariables DTO to create, belongs to current user: {}", configVariablesDTO.toString());
        }

        return ResponseEntity
                .created(new URI("/api/config-variables/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /config-variables/:id} : Updates an existing configVariables.
     *
     * @param id                 the id of the configVariablesDTO to save.
     * @param configVariablesDTO the configVariablesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated configVariablesDTO, or with status
     *         {@code 400 (Bad Request)} if the configVariablesDTO is not valid, or
     *         with status {@code 500 (Internal Server Error)} if the
     *         configVariablesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-variables/{id}")
    public ResponseEntity<ConfigVariablesDTO> updateConfigVariables(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody ConfigVariablesDTO configVariablesDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigVariables : {}, {}", id, configVariablesDTO);
        if (configVariablesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configVariablesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        ConfigVariablesDTO result = new ConfigVariablesDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = configVariablesService.save(configVariablesDTO);
            log.debug("ConfigVariables DTO to create, belongs to current user: {}", configVariablesDTO.toString());
        }

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                configVariablesDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /config-variables/:id} : Partial updates given fields of an
     * existing configVariables, field will ignore if it is null
     *
     * @param id                 the id of the configVariablesDTO to save.
     * @param configVariablesDTO the configVariablesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated configVariablesDTO, or with status
     *         {@code 400 (Bad Request)} if the configVariablesDTO is not valid, or
     *         with status {@code 404 (Not Found)} if the configVariablesDTO is not
     *         found, or with status {@code 500 (Internal Server Error)} if the
     *         configVariablesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/config-variables/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ConfigVariablesDTO> partialUpdateConfigVariables(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody ConfigVariablesDTO configVariablesDTO) throws URISyntaxException {
        log.debug("REST request to partial update ConfigVariables partially : {}, {}", id, configVariablesDTO);
        if (configVariablesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configVariablesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        Optional<ConfigVariablesDTO> result = configVariablesService.findOne(id);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = configVariablesService.partialUpdate(configVariablesDTO);
            log.debug("ConfigVariables DTO to update, belongs to current user: {}", configVariablesDTO.toString());
        }

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
                ENTITY_NAME, configVariablesDTO.getId().toString()));
    }

    /**
     * {@code GET  /config-variables} : get all the configVariables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of configVariables in body.
     */
    @GetMapping("/config-variables")
    public ResponseEntity<List<ConfigVariablesDTO>> getAllConfigVariables(ConfigVariablesCriteria criteria,
            Pageable pageable) {
        log.debug("REST request to get ConfigVariables by criteria: {}", criteria);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Page<ConfigVariablesDTO> page = configVariablesQueryService.findByCriteria(criteria, pageable);

            HttpHeaders headers = PaginationUtil
                    .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }

        return ResponseUtil.wrapOrNotFound(null);

    }

    /**
     * {@code GET  /config-variables/count} : count all the configVariables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/config-variables/count")
    public ResponseEntity<Long> countConfigVariables(ConfigVariablesCriteria criteria) {
        log.debug("REST request to count ConfigVariables by criteria: {}", criteria);
        return ResponseEntity.ok().body(configVariablesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /config-variables/:id} : get the "id" configVariables.
     *
     * @param id the id of the configVariablesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the configVariablesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-variables/{id}")
    public ResponseEntity<ConfigVariablesDTO> getConfigVariables(@PathVariable Long id) {
        log.debug("REST request to get ConfigVariables : {}", id);
        Optional<ConfigVariablesDTO> configVariablesDTO = configVariablesService.findOne(id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return ResponseUtil.wrapOrNotFound(configVariablesDTO);
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code DELETE  /config-variables/:id} : delete the "id" configVariables.
     *
     * @param id the id of the configVariablesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-variables/{id}")
    public ResponseEntity<Void> deleteConfigVariables(@PathVariable Long id) {
        log.debug("REST request to delete ConfigVariables : {}", id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            configVariablesService.delete(id);
        }

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
