package com.spingular.cms.web.rest;

import com.spingular.cms.repository.FollowRepository;
import com.spingular.cms.service.FollowQueryService;
import com.spingular.cms.service.FollowService;
import com.spingular.cms.service.criteria.FollowCriteria;
import com.spingular.cms.service.dto.FollowDTO;
import com.spingular.cms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.spingular.cms.domain.Follow}.
 */
@RestController
@RequestMapping("/api")
public class FollowResource {

    private final Logger log = LoggerFactory.getLogger(FollowResource.class);

    private static final String ENTITY_NAME = "follow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowService followService;

    private final FollowRepository followRepository;

    private final FollowQueryService followQueryService;

    public FollowResource(FollowService followService, FollowRepository followRepository, FollowQueryService followQueryService) {
        this.followService = followService;
        this.followRepository = followRepository;
        this.followQueryService = followQueryService;
    }

    /**
     * {@code POST  /follows} : Create a new follow.
     *
     * @param followDTO the followDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new followDTO, or with status {@code 400 (Bad Request)} if the follow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/follows")
    public ResponseEntity<FollowDTO> createFollow(@RequestBody FollowDTO followDTO) throws URISyntaxException {
        log.debug("REST request to save Follow : {}", followDTO);
        if (followDTO.getId() != null) {
            throw new BadRequestAlertException("A new follow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowDTO result = followService.save(followDTO);
        return ResponseEntity
            .created(new URI("/api/follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /follows/:id} : Updates an existing follow.
     *
     * @param id the id of the followDTO to save.
     * @param followDTO the followDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated followDTO,
     * or with status {@code 400 (Bad Request)} if the followDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the followDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/follows/{id}")
    public ResponseEntity<FollowDTO> updateFollow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FollowDTO followDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Follow : {}, {}", id, followDTO);
        if (followDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, followDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!followRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FollowDTO result = followService.save(followDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, followDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /follows/:id} : Partial updates given fields of an existing follow, field will ignore if it is null
     *
     * @param id the id of the followDTO to save.
     * @param followDTO the followDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated followDTO,
     * or with status {@code 400 (Bad Request)} if the followDTO is not valid,
     * or with status {@code 404 (Not Found)} if the followDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the followDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/follows/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FollowDTO> partialUpdateFollow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FollowDTO followDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Follow partially : {}, {}", id, followDTO);
        if (followDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, followDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!followRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FollowDTO> result = followService.partialUpdate(followDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, followDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /follows} : get all the follows.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of follows in body.
     */
    @GetMapping("/follows")
    public ResponseEntity<List<FollowDTO>> getAllFollows(FollowCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Follows by criteria: {}", criteria);
        Page<FollowDTO> page = followQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /follows/count} : count all the follows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/follows/count")
    public ResponseEntity<Long> countFollows(FollowCriteria criteria) {
        log.debug("REST request to count Follows by criteria: {}", criteria);
        return ResponseEntity.ok().body(followQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /follows/:id} : get the "id" follow.
     *
     * @param id the id of the followDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the followDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/follows/{id}")
    public ResponseEntity<FollowDTO> getFollow(@PathVariable Long id) {
        log.debug("REST request to get Follow : {}", id);
        Optional<FollowDTO> followDTO = followService.findOne(id);
        return ResponseUtil.wrapOrNotFound(followDTO);
    }

    /**
     * {@code DELETE  /follows/:id} : delete the "id" follow.
     *
     * @param id the id of the followDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/follows/{id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable Long id) {
        log.debug("REST request to delete Follow : {}", id);
        followService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
