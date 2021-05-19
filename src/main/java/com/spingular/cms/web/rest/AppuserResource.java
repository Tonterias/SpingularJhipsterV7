package com.spingular.cms.web.rest;

import com.spingular.cms.domain.Appuser;
import com.spingular.cms.repository.AppuserRepository;
import com.spingular.cms.repository.UserRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
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
import tech.jhipster.service.filter.LongFilter;
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

    private final UserRepository userRepository;

    public AppuserResource(
        AppuserService appuserService,
        AppuserRepository appuserRepository,
        AppuserQueryService appuserQueryService,
        UserRepository userRepository
    ) {
        this.appuserService = appuserService;
        this.appuserRepository = appuserRepository;
        this.appuserQueryService = appuserQueryService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /appusers} : Create a new appuser.
     *
     * @param appuserDTO the appuserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new appuserDTO, or with status {@code 400 (Bad Request)} if
     *         the appuser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appusers")
    public ResponseEntity<AppuserDTO> createAppuser(@Valid @RequestBody AppuserDTO appuserDTO) throws URISyntaxException {
        log.debug("REST request to save Appuser : {}", appuserDTO);
        if (appuserDTO.getId() != null) {
            throw new BadRequestAlertException("A new appuser cannot already have an ID", ENTITY_NAME, "idexists");
        }

        AppuserDTO result = new AppuserDTO();
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appuserDTO.getId().equals(appuser.getId())) {
                    result = appuserService.save(appuserDTO);
                    log.debug("Appuser DTO to create, belongs to current user: {}", appuserDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appuserService.save(appuserDTO);
                log.debug("Appuser DTO to create, belongs to current user: {}", appuserDTO.toString());
            }
        }

        return ResponseEntity
            .created(new URI("/api/appusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appusers/:id} : Updates an existing appuser.
     *
     * @param id         the id of the appuserDTO to save.
     * @param appuserDTO the appuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated appuserDTO, or with status {@code 400 (Bad Request)} if
     *         the appuserDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the appuserDTO couldn't be
     *         updated.
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

        AppuserDTO result = appuserService.findOne(id).orElse(appuserDTO);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appuserDTO.getId().equals(appuser.getId())) {
                    result = appuserService.save(appuserDTO);
                    log.debug("Appuser DTO to update, belongs to current user: {}", appuserDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appuserService.save(appuserDTO);
                log.debug("Appuser DTO to update, belongs to current user: {}", appuserDTO.toString());
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appuserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appusers/:id} : Partial updates given fields of an existing
     * appuser, field will ignore if it is null
     *
     * @param id         the id of the appuserDTO to save.
     * @param appuserDTO the appuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated appuserDTO, or with status {@code 400 (Bad Request)} if
     *         the appuserDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the appuserDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the appuserDTO couldn't be
     *         updated.
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

        Optional<AppuserDTO> result = appuserService.findOne(id);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appuserDTO.getId().equals(appuser.getId())) {
                    result = appuserService.partialUpdate(appuserDTO);
                    log.debug("Appuser DTO to partial update, belongs to current user: {}", appuserDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appuserService.partialUpdate(appuserDTO);
                log.debug("Appuser DTO to partial update, belongs to current user: {}", appuserDTO.toString());
            }
        }

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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of appusers in body.
     */
    @GetMapping("/appusers")
    public ResponseEntity<List<AppuserDTO>> getAllAppusers(AppuserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appusers by criteria: {}", criteria);

        Page<AppuserDTO> page;
        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)
        ) {
            AppuserCriteria loggedCriteria = new AppuserCriteria();
            LongFilter longFilter = new LongFilter();
            if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
                Appuser loggedAppuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                loggedCriteria.setUserId((LongFilter) longFilter.setEquals(loggedAppuser.getUser().getId()));
            }
            page = appuserQueryService.findByCriteria(loggedCriteria, pageable);
        } else {
            page = appuserQueryService.findByCriteria(criteria, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appusers/count} : count all the appusers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the appuserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appusers/{id}")
    public ResponseEntity<AppuserDTO> getAppuser(@PathVariable Long id) {
        log.debug("REST request to get Appuser : {}", id);
        Optional<AppuserDTO> appuserDTO = appuserService.findOne(id);

        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)
        ) {
            return ResponseUtil.wrapOrNotFound(appuserDTO);
        }

        return ResponseUtil.wrapOrNotFound(null);
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

        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser loggedAppuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                AppuserDTO appuserDTO = appuserService.findOne(id).orElse(new AppuserDTO());
                if (appuserDTO.getId().equals(loggedAppuser.getId())) {
                    appuserService.delete(id);
                    log.debug("Blog DTO to delete, belongs to current user: {}", appuserDTO.toString());
                    log.debug("Blog DTO to delete, belongs to Appuser: {}", loggedAppuser.getId());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                appuserService.delete(id);
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
