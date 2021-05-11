package com.spingular.cms.web.rest;

import com.spingular.cms.repository.CinterestRepository;
import com.spingular.cms.service.CinterestQueryService;
import com.spingular.cms.service.CinterestService;
import com.spingular.cms.service.criteria.CinterestCriteria;
import com.spingular.cms.service.dto.CinterestDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.spingular.cms.domain.Cinterest}.
 */
@RestController
@RequestMapping("/api")
public class CinterestResource {

    private final Logger log = LoggerFactory.getLogger(CinterestResource.class);

    private static final String ENTITY_NAME = "cinterest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CinterestService cinterestService;

    private final CinterestRepository cinterestRepository;

    private final CinterestQueryService cinterestQueryService;

    public CinterestResource(
        CinterestService cinterestService,
        CinterestRepository cinterestRepository,
        CinterestQueryService cinterestQueryService
    ) {
        this.cinterestService = cinterestService;
        this.cinterestRepository = cinterestRepository;
        this.cinterestQueryService = cinterestQueryService;
    }

    /**
     * {@code POST  /cinterests} : Create a new cinterest.
     *
     * @param cinterestDTO the cinterestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cinterestDTO, or with status {@code 400 (Bad Request)} if the cinterest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cinterests")
    public ResponseEntity<CinterestDTO> createCinterest(@Valid @RequestBody CinterestDTO cinterestDTO) throws URISyntaxException {
        log.debug("REST request to save Cinterest : {}", cinterestDTO);
        if (cinterestDTO.getId() != null) {
            throw new BadRequestAlertException("A new cinterest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CinterestDTO result = cinterestService.save(cinterestDTO);
        return ResponseEntity
            .created(new URI("/api/cinterests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cinterests/:id} : Updates an existing cinterest.
     *
     * @param id the id of the cinterestDTO to save.
     * @param cinterestDTO the cinterestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cinterestDTO,
     * or with status {@code 400 (Bad Request)} if the cinterestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cinterestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cinterests/{id}")
    public ResponseEntity<CinterestDTO> updateCinterest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CinterestDTO cinterestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cinterest : {}, {}", id, cinterestDTO);
        if (cinterestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cinterestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinterestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CinterestDTO result = cinterestService.save(cinterestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cinterestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cinterests/:id} : Partial updates given fields of an existing cinterest, field will ignore if it is null
     *
     * @param id the id of the cinterestDTO to save.
     * @param cinterestDTO the cinterestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cinterestDTO,
     * or with status {@code 400 (Bad Request)} if the cinterestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cinterestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cinterestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cinterests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CinterestDTO> partialUpdateCinterest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CinterestDTO cinterestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cinterest partially : {}, {}", id, cinterestDTO);
        if (cinterestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cinterestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinterestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CinterestDTO> result = cinterestService.partialUpdate(cinterestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cinterestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cinterests} : get all the cinterests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cinterests in body.
     */
    @GetMapping("/cinterests")
    public ResponseEntity<List<CinterestDTO>> getAllCinterests(CinterestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cinterests by criteria: {}", criteria);
        Page<CinterestDTO> page = cinterestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cinterests/count} : count all the cinterests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cinterests/count")
    public ResponseEntity<Long> countCinterests(CinterestCriteria criteria) {
        log.debug("REST request to count Cinterests by criteria: {}", criteria);
        return ResponseEntity.ok().body(cinterestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cinterests/:id} : get the "id" cinterest.
     *
     * @param id the id of the cinterestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cinterestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cinterests/{id}")
    public ResponseEntity<CinterestDTO> getCinterest(@PathVariable Long id) {
        log.debug("REST request to get Cinterest : {}", id);
        Optional<CinterestDTO> cinterestDTO = cinterestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cinterestDTO);
    }

    /**
     * {@code DELETE  /cinterests/:id} : delete the "id" cinterest.
     *
     * @param id the id of the cinterestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cinterests/{id}")
    public ResponseEntity<Void> deleteCinterest(@PathVariable Long id) {
        log.debug("REST request to delete Cinterest : {}", id);
        cinterestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
