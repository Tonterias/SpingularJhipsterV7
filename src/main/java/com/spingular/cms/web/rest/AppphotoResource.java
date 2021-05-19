package com.spingular.cms.web.rest;

import com.spingular.cms.domain.Appuser;
import com.spingular.cms.repository.AppphotoRepository;
import com.spingular.cms.repository.AppuserRepository;
import com.spingular.cms.repository.UserRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.AppphotoQueryService;
import com.spingular.cms.service.AppphotoService;
import com.spingular.cms.service.criteria.AppphotoCriteria;
import com.spingular.cms.service.dto.AppphotoDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Appphoto}.
 */
@RestController
@RequestMapping("/api")
public class AppphotoResource {

    private final Logger log = LoggerFactory.getLogger(AppphotoResource.class);

    private static final String ENTITY_NAME = "appphoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppphotoService appphotoService;

    private final AppphotoRepository appphotoRepository;

    private final AppphotoQueryService appphotoQueryService;

    private final UserRepository userRepository;

    private final AppuserRepository appuserRepository;

    public AppphotoResource(
        AppphotoService appphotoService,
        AppphotoRepository appphotoRepository,
        AppphotoQueryService appphotoQueryService,
        UserRepository userRepository,
        AppuserRepository appuserRepository
    ) {
        this.appphotoService = appphotoService;
        this.appphotoRepository = appphotoRepository;
        this.appphotoQueryService = appphotoQueryService;
        this.userRepository = userRepository;
        this.appuserRepository = appuserRepository;
    }

    /**
     * {@code POST  /appphotos} : Create a new appphoto.
     *
     * @param appphotoDTO the appphotoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new appphotoDTO, or with status {@code 400 (Bad Request)} if
     *         the appphoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appphotos")
    public ResponseEntity<AppphotoDTO> createAppphoto(@Valid @RequestBody AppphotoDTO appphotoDTO) throws URISyntaxException {
        log.debug("REST request to save Appphoto : {}", appphotoDTO);
        if (appphotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new appphoto cannot already have an ID", ENTITY_NAME, "idexists");
        }

        AppphotoDTO result = new AppphotoDTO();
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appphotoDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = appphotoService.save(appphotoDTO);
                    log.debug("Appphoto DTO to create, belongs to current user: {}", appphotoDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appphotoService.save(appphotoDTO);
                log.debug("Appphoto DTO to create, belongs to current user: {}", appphotoDTO.toString());
            }
        }

        return ResponseEntity
            .created(new URI("/api/appphotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appphotos/:id} : Updates an existing appphoto.
     *
     * @param id          the id of the appphotoDTO to save.
     * @param appphotoDTO the appphotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated appphotoDTO, or with status {@code 400 (Bad Request)} if
     *         the appphotoDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the appphotoDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appphotos/{id}")
    public ResponseEntity<AppphotoDTO> updateAppphoto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppphotoDTO appphotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Appphoto : {}, {}", id, appphotoDTO);
        if (appphotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appphotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appphotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppphotoDTO result = appphotoService.findOne(id).orElse(appphotoDTO);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appphotoDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = appphotoService.save(appphotoDTO);
                    log.debug("Appphoto DTO to update, belongs to current user: {}", appphotoDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appphotoService.save(appphotoDTO);
                log.debug("Appphoto DTO to update, belongs to current user: {}", appphotoDTO.toString());
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appphotoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appphotos/:id} : Partial updates given fields of an existing
     * appphoto, field will ignore if it is null
     *
     * @param id          the id of the appphotoDTO to save.
     * @param appphotoDTO the appphotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated appphotoDTO, or with status {@code 400 (Bad Request)} if
     *         the appphotoDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the appphotoDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the appphotoDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appphotos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppphotoDTO> partialUpdateAppphoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppphotoDTO appphotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appphoto partially : {}, {}", id, appphotoDTO);
        if (appphotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appphotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appphotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppphotoDTO> result = appphotoService.findOne(id);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (appphotoDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = appphotoService.partialUpdate(appphotoDTO);
                    log.debug("Appphoto DTO to partial update, belongs to current user: {}", appphotoDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = appphotoService.partialUpdate(appphotoDTO);
                log.debug("Appphoto DTO to partial update, belongs to current user: {}", appphotoDTO.toString());
            }
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appphotoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appphotos} : get all the appphotos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of appphotos in body.
     */
    @GetMapping("/appphotos")
    public ResponseEntity<List<AppphotoDTO>> getAllAppphotos(AppphotoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appphotos by criteria: {}", criteria);

        Page<AppphotoDTO> page;
        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)
        ) {
            AppphotoCriteria loggedCriteria = new AppphotoCriteria();
            LongFilter longFilter = new LongFilter();
            if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
                Appuser loggedAppuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                loggedCriteria.setAppuserId((LongFilter) longFilter.setEquals(loggedAppuser.getId()));
            }
            page = appphotoQueryService.findByCriteria(loggedCriteria, pageable);
        } else {
            page = appphotoQueryService.findByCriteria(criteria, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appphotos/count} : count all the appphotos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/appphotos/count")
    public ResponseEntity<Long> countAppphotos(AppphotoCriteria criteria) {
        log.debug("REST request to count Appphotos by criteria: {}", criteria);
        return ResponseEntity.ok().body(appphotoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /appphotos/:id} : get the "id" appphoto.
     *
     * @param id the id of the appphotoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the appphotoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appphotos/{id}")
    public ResponseEntity<AppphotoDTO> getAppphoto(@PathVariable Long id) {
        log.debug("REST request to get Appphoto : {}", id);
        Optional<AppphotoDTO> appphotoDTO = appphotoService.findOne(id);

        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)
        ) {
            return ResponseUtil.wrapOrNotFound(appphotoDTO);
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code DELETE  /appphotos/:id} : delete the "id" appphoto.
     *
     * @param id the id of the appphotoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appphotos/{id}")
    public ResponseEntity<Void> deleteAppphoto(@PathVariable Long id) {
        log.debug("REST request to delete Appphoto : {}", id);

        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser loggedAppuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                AppphotoDTO appphotoDTO = appphotoService.findOne(id).orElse(new AppphotoDTO());
                if (appphotoDTO.getAppuser().getId().equals(loggedAppuser.getId())) {
                    appphotoService.delete(id);
                    log.debug("Appphoto DTO to delete, belongs to current user: {}", appphotoDTO.toString());
                    log.debug("Appphoto DTO to delete, belongs to Appuser: {}", loggedAppuser.getId());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                appphotoService.delete(id);
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
