package com.spingular.cms.web.rest;

import com.spingular.cms.repository.UrllinkRepository;
import com.spingular.cms.service.UrllinkQueryService;
import com.spingular.cms.service.UrllinkService;
import com.spingular.cms.service.criteria.UrllinkCriteria;
import com.spingular.cms.service.dto.UrllinkDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Urllink}.
 */
@RestController
@RequestMapping("/api")
public class UrllinkResource {

    private final Logger log = LoggerFactory.getLogger(UrllinkResource.class);

    private static final String ENTITY_NAME = "urllink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UrllinkService urllinkService;

    private final UrllinkRepository urllinkRepository;

    private final UrllinkQueryService urllinkQueryService;

    public UrllinkResource(UrllinkService urllinkService, UrllinkRepository urllinkRepository, UrllinkQueryService urllinkQueryService) {
        this.urllinkService = urllinkService;
        this.urllinkRepository = urllinkRepository;
        this.urllinkQueryService = urllinkQueryService;
    }

    /**
     * {@code POST  /urllinks} : Create a new urllink.
     *
     * @param urllinkDTO the urllinkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new urllinkDTO, or with status {@code 400 (Bad Request)} if the urllink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/urllinks")
    public ResponseEntity<UrllinkDTO> createUrllink(@Valid @RequestBody UrllinkDTO urllinkDTO) throws URISyntaxException {
        log.debug("REST request to save Urllink : {}", urllinkDTO);
        if (urllinkDTO.getId() != null) {
            throw new BadRequestAlertException("A new urllink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UrllinkDTO result = urllinkService.save(urllinkDTO);
        return ResponseEntity
            .created(new URI("/api/urllinks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /urllinks/:id} : Updates an existing urllink.
     *
     * @param id the id of the urllinkDTO to save.
     * @param urllinkDTO the urllinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urllinkDTO,
     * or with status {@code 400 (Bad Request)} if the urllinkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the urllinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/urllinks/{id}")
    public ResponseEntity<UrllinkDTO> updateUrllink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UrllinkDTO urllinkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Urllink : {}, {}", id, urllinkDTO);
        if (urllinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urllinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urllinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UrllinkDTO result = urllinkService.save(urllinkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urllinkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /urllinks/:id} : Partial updates given fields of an existing urllink, field will ignore if it is null
     *
     * @param id the id of the urllinkDTO to save.
     * @param urllinkDTO the urllinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urllinkDTO,
     * or with status {@code 400 (Bad Request)} if the urllinkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the urllinkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the urllinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/urllinks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UrllinkDTO> partialUpdateUrllink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UrllinkDTO urllinkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Urllink partially : {}, {}", id, urllinkDTO);
        if (urllinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urllinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urllinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UrllinkDTO> result = urllinkService.partialUpdate(urllinkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urllinkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /urllinks} : get all the urllinks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of urllinks in body.
     */
    @GetMapping("/urllinks")
    public ResponseEntity<List<UrllinkDTO>> getAllUrllinks(UrllinkCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Urllinks by criteria: {}", criteria);
        Page<UrllinkDTO> page = urllinkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /urllinks/count} : count all the urllinks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/urllinks/count")
    public ResponseEntity<Long> countUrllinks(UrllinkCriteria criteria) {
        log.debug("REST request to count Urllinks by criteria: {}", criteria);
        return ResponseEntity.ok().body(urllinkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /urllinks/:id} : get the "id" urllink.
     *
     * @param id the id of the urllinkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the urllinkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/urllinks/{id}")
    public ResponseEntity<UrllinkDTO> getUrllink(@PathVariable Long id) {
        log.debug("REST request to get Urllink : {}", id);
        Optional<UrllinkDTO> urllinkDTO = urllinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(urllinkDTO);
    }

    /**
     * {@code DELETE  /urllinks/:id} : delete the "id" urllink.
     *
     * @param id the id of the urllinkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/urllinks/{id}")
    public ResponseEntity<Void> deleteUrllink(@PathVariable Long id) {
        log.debug("REST request to delete Urllink : {}", id);
        urllinkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
