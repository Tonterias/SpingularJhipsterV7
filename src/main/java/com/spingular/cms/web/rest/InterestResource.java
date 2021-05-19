package com.spingular.cms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spingular.cms.repository.InterestRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.InterestQueryService;
import com.spingular.cms.service.InterestService;
import com.spingular.cms.service.criteria.InterestCriteria;
import com.spingular.cms.service.dto.InterestDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Interest}.
 */
@RestController
@RequestMapping("/api")
public class InterestResource {

    private final Logger log = LoggerFactory.getLogger(InterestResource.class);

    private static final String ENTITY_NAME = "interest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterestService interestService;

    private final InterestRepository interestRepository;

    private final InterestQueryService interestQueryService;

    public InterestResource(InterestService interestService, InterestRepository interestRepository,
            InterestQueryService interestQueryService) {
        this.interestService = interestService;
        this.interestRepository = interestRepository;
        this.interestQueryService = interestQueryService;
    }

    /**
     * {@code POST  /interests} : Create a new interest.
     *
     * @param interestDTO the interestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new interestDTO, or with status {@code 400 (Bad Request)} if
     *         the interest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interests")
    public ResponseEntity<InterestDTO> createInterest(@Valid @RequestBody InterestDTO interestDTO)
            throws URISyntaxException {
        log.debug("REST request to save Interest : {}", interestDTO);
        if (interestDTO.getId() != null) {
            throw new BadRequestAlertException("A new interest cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // NOTE: If anyone can use this Object, anyone can Create & Read any object
        InterestDTO result = new InterestDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)
                || SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = interestService.save(interestDTO);
        }

        return ResponseEntity
                .created(new URI("/api/interests/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /interests/:id} : Updates an existing interest.
     *
     * @param id          the id of the interestDTO to save.
     * @param interestDTO the interestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated interestDTO, or with status {@code 400 (Bad Request)} if
     *         the interestDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the interestDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interests/{id}")
    public ResponseEntity<InterestDTO> updateInterest(@PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody InterestDTO interestDTO) throws URISyntaxException {
        log.debug("REST request to update Interest : {}, {}", id, interestDTO);
        if (interestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

       // NOTE: Only admins can change an Object (that belongs to everyone)
        InterestDTO result = new InterestDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = interestService.save(interestDTO);
        }

        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interestDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /interests/:id} : Partial updates given fields of an existing
     * interest, field will ignore if it is null
     *
     * @param id          the id of the interestDTO to save.
     * @param interestDTO the interestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated interestDTO, or with status {@code 400 (Bad Request)} if
     *         the interestDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the interestDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the interestDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<InterestDTO> partialUpdateInterest(
            @PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody InterestDTO interestDTO)
            throws URISyntaxException {
        log.debug("REST request to partial update Interest partially : {}, {}", id, interestDTO);
        if (interestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

       // NOTE: Only admins can change an Object (that belongs to everyone)
        Optional<InterestDTO> result = interestService.partialUpdate(interestDTO);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            if (result.isPresent()) {
                interestService.save(interestDTO);
            }
        }

        return ResponseUtil.wrapOrNotFound(result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interestDTO.getId().toString()));
    }

    /**
     * {@code GET  /interests} : get all the interests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of interests in body.
     */
    @GetMapping("/interests")
    public ResponseEntity<List<InterestDTO>> getAllInterests(InterestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Interests by criteria: {}", criteria);

        // NOTE: If anyone can use this Object, anyone can Create & Read any object
        Page<InterestDTO> page = interestQueryService.findByCriteria(criteria, pageable);

        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interests/count} : count all the interests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/interests/count")
    public ResponseEntity<Long> countInterests(InterestCriteria criteria) {
        log.debug("REST request to count Interests by criteria: {}", criteria);
        return ResponseEntity.ok().body(interestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interests/:id} : get the "id" interest.
     *
     * @param id the id of the interestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the interestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interests/{id}")
    public ResponseEntity<InterestDTO> getInterest(@PathVariable Long id) {
        log.debug("REST request to get Interest : {}", id);

        // NOTE: Nobody can change this Object, create a new one
        Optional<InterestDTO> interestDTO = interestService.findOne(id);

        return ResponseUtil.wrapOrNotFound(interestDTO);
    }

    /**
     * {@code DELETE  /interests/:id} : delete the "id" interest.
     *
     * @param id the id of the interestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interests/{id}")
    public ResponseEntity<Void> deleteInterest(@PathVariable Long id) {
        log.debug("REST request to delete Interest : {}", id);

        // NOTE: Only admins can delete an Object (that belongs to everyone)
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            interestService.delete(id);
        }

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
