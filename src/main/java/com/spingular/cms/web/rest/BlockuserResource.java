package com.spingular.cms.web.rest;

import com.spingular.cms.repository.BlockuserRepository;
import com.spingular.cms.service.BlockuserQueryService;
import com.spingular.cms.service.BlockuserService;
import com.spingular.cms.service.criteria.BlockuserCriteria;
import com.spingular.cms.service.dto.BlockuserDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Blockuser}.
 */
@RestController
@RequestMapping("/api")
public class BlockuserResource {

    private final Logger log = LoggerFactory.getLogger(BlockuserResource.class);

    private static final String ENTITY_NAME = "blockuser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockuserService blockuserService;

    private final BlockuserRepository blockuserRepository;

    private final BlockuserQueryService blockuserQueryService;

    public BlockuserResource(
        BlockuserService blockuserService,
        BlockuserRepository blockuserRepository,
        BlockuserQueryService blockuserQueryService
    ) {
        this.blockuserService = blockuserService;
        this.blockuserRepository = blockuserRepository;
        this.blockuserQueryService = blockuserQueryService;
    }

    /**
     * {@code POST  /blockusers} : Create a new blockuser.
     *
     * @param blockuserDTO the blockuserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blockuserDTO, or with status {@code 400 (Bad Request)} if the blockuser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blockusers")
    public ResponseEntity<BlockuserDTO> createBlockuser(@RequestBody BlockuserDTO blockuserDTO) throws URISyntaxException {
        log.debug("REST request to save Blockuser : {}", blockuserDTO);
        if (blockuserDTO.getId() != null) {
            throw new BadRequestAlertException("A new blockuser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlockuserDTO result = blockuserService.save(blockuserDTO);
        return ResponseEntity
            .created(new URI("/api/blockusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blockusers/:id} : Updates an existing blockuser.
     *
     * @param id the id of the blockuserDTO to save.
     * @param blockuserDTO the blockuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockuserDTO,
     * or with status {@code 400 (Bad Request)} if the blockuserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockuserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blockusers/{id}")
    public ResponseEntity<BlockuserDTO> updateBlockuser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlockuserDTO blockuserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Blockuser : {}, {}", id, blockuserDTO);
        if (blockuserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockuserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BlockuserDTO result = blockuserService.save(blockuserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockuserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /blockusers/:id} : Partial updates given fields of an existing blockuser, field will ignore if it is null
     *
     * @param id the id of the blockuserDTO to save.
     * @param blockuserDTO the blockuserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockuserDTO,
     * or with status {@code 400 (Bad Request)} if the blockuserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the blockuserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the blockuserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/blockusers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BlockuserDTO> partialUpdateBlockuser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlockuserDTO blockuserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Blockuser partially : {}, {}", id, blockuserDTO);
        if (blockuserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockuserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlockuserDTO> result = blockuserService.partialUpdate(blockuserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockuserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /blockusers} : get all the blockusers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blockusers in body.
     */
    @GetMapping("/blockusers")
    public ResponseEntity<List<BlockuserDTO>> getAllBlockusers(BlockuserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Blockusers by criteria: {}", criteria);
        Page<BlockuserDTO> page = blockuserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blockusers/count} : count all the blockusers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/blockusers/count")
    public ResponseEntity<Long> countBlockusers(BlockuserCriteria criteria) {
        log.debug("REST request to count Blockusers by criteria: {}", criteria);
        return ResponseEntity.ok().body(blockuserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /blockusers/:id} : get the "id" blockuser.
     *
     * @param id the id of the blockuserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blockuserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blockusers/{id}")
    public ResponseEntity<BlockuserDTO> getBlockuser(@PathVariable Long id) {
        log.debug("REST request to get Blockuser : {}", id);
        Optional<BlockuserDTO> blockuserDTO = blockuserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blockuserDTO);
    }

    /**
     * {@code DELETE  /blockusers/:id} : delete the "id" blockuser.
     *
     * @param id the id of the blockuserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blockusers/{id}")
    public ResponseEntity<Void> deleteBlockuser(@PathVariable Long id) {
        log.debug("REST request to delete Blockuser : {}", id);
        blockuserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
