package com.spingular.cms.web.rest;

import com.spingular.cms.repository.CcelebRepository;
import com.spingular.cms.service.CcelebQueryService;
import com.spingular.cms.service.CcelebService;
import com.spingular.cms.service.criteria.CcelebCriteria;
import com.spingular.cms.service.dto.CcelebDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Cceleb}.
 */
@RestController
@RequestMapping("/api")
public class CcelebResource {

    private final Logger log = LoggerFactory.getLogger(CcelebResource.class);

    private static final String ENTITY_NAME = "cceleb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CcelebService ccelebService;

    private final CcelebRepository ccelebRepository;

    private final CcelebQueryService ccelebQueryService;

    public CcelebResource(CcelebService ccelebService, CcelebRepository ccelebRepository, CcelebQueryService ccelebQueryService) {
        this.ccelebService = ccelebService;
        this.ccelebRepository = ccelebRepository;
        this.ccelebQueryService = ccelebQueryService;
    }

    /**
     * {@code POST  /ccelebs} : Create a new cceleb.
     *
     * @param ccelebDTO the ccelebDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ccelebDTO, or with status {@code 400 (Bad Request)} if the cceleb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ccelebs")
    public ResponseEntity<CcelebDTO> createCceleb(@Valid @RequestBody CcelebDTO ccelebDTO) throws URISyntaxException {
        log.debug("REST request to save Cceleb : {}", ccelebDTO);
        if (ccelebDTO.getId() != null) {
            throw new BadRequestAlertException("A new cceleb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CcelebDTO result = ccelebService.save(ccelebDTO);
        return ResponseEntity
            .created(new URI("/api/ccelebs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ccelebs/:id} : Updates an existing cceleb.
     *
     * @param id the id of the ccelebDTO to save.
     * @param ccelebDTO the ccelebDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ccelebDTO,
     * or with status {@code 400 (Bad Request)} if the ccelebDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ccelebDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ccelebs/{id}")
    public ResponseEntity<CcelebDTO> updateCceleb(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CcelebDTO ccelebDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cceleb : {}, {}", id, ccelebDTO);
        if (ccelebDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ccelebDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ccelebRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CcelebDTO result = ccelebService.save(ccelebDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ccelebDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ccelebs/:id} : Partial updates given fields of an existing cceleb, field will ignore if it is null
     *
     * @param id the id of the ccelebDTO to save.
     * @param ccelebDTO the ccelebDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ccelebDTO,
     * or with status {@code 400 (Bad Request)} if the ccelebDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ccelebDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ccelebDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ccelebs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CcelebDTO> partialUpdateCceleb(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CcelebDTO ccelebDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cceleb partially : {}, {}", id, ccelebDTO);
        if (ccelebDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ccelebDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ccelebRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CcelebDTO> result = ccelebService.partialUpdate(ccelebDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ccelebDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ccelebs} : get all the ccelebs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ccelebs in body.
     */
    @GetMapping("/ccelebs")
    public ResponseEntity<List<CcelebDTO>> getAllCcelebs(CcelebCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ccelebs by criteria: {}", criteria);
        Page<CcelebDTO> page = ccelebQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ccelebs/count} : count all the ccelebs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ccelebs/count")
    public ResponseEntity<Long> countCcelebs(CcelebCriteria criteria) {
        log.debug("REST request to count Ccelebs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ccelebQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ccelebs/:id} : get the "id" cceleb.
     *
     * @param id the id of the ccelebDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ccelebDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ccelebs/{id}")
    public ResponseEntity<CcelebDTO> getCceleb(@PathVariable Long id) {
        log.debug("REST request to get Cceleb : {}", id);
        Optional<CcelebDTO> ccelebDTO = ccelebService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ccelebDTO);
    }

    /**
     * {@code DELETE  /ccelebs/:id} : delete the "id" cceleb.
     *
     * @param id the id of the ccelebDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ccelebs/{id}")
    public ResponseEntity<Void> deleteCceleb(@PathVariable Long id) {
        log.debug("REST request to delete Cceleb : {}", id);
        ccelebService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
