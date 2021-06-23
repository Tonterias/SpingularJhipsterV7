package com.spingular.cms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spingular.cms.repository.FeedbackRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.FeedbackQueryService;
import com.spingular.cms.service.FeedbackService;
import com.spingular.cms.service.criteria.FeedbackCriteria;
import com.spingular.cms.service.dto.FeedbackDTO;
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
 * REST controller for managing {@link com.spingular.cms.domain.Feedback}.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

    private static final String ENTITY_NAME = "feedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackService feedbackService;

    private final FeedbackRepository feedbackRepository;

    private final FeedbackQueryService feedbackQueryService;

    public FeedbackResource(FeedbackService feedbackService, FeedbackRepository feedbackRepository,
            FeedbackQueryService feedbackQueryService) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
        this.feedbackQueryService = feedbackQueryService;
    }

    /**
     * {@code POST  /feedbacks} : Create a new feedback.
     *
     * @param feedbackDTO the feedbackDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new feedbackDTO, or with status {@code 400 (Bad Request)} if
     *         the feedback has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedbacks")
    public ResponseEntity<FeedbackDTO> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO)
            throws URISyntaxException {
        log.debug("REST request to save Feedback : {}", feedbackDTO);
        if (feedbackDTO.getId() != null) {
            throw new BadRequestAlertException("A new feedback cannot already have an ID", ENTITY_NAME, "idexists");
        }

        FeedbackDTO result = feedbackService.save(feedbackDTO);
        log.debug("Feedback DTO to create, belongs to current user: {}", feedbackDTO.toString());

        return ResponseEntity
                .created(new URI("/api/feedbacks/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /feedbacks/:id} : Updates an existing feedback.
     *
     * @param id          the id of the feedbackDTO to save.
     * @param feedbackDTO the feedbackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated feedbackDTO, or with status {@code 400 (Bad Request)} if
     *         the feedbackDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the feedbackDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody FeedbackDTO feedbackDTO) throws URISyntaxException {
        log.debug("REST request to update Feedback : {}, {}", id, feedbackDTO);
        if (feedbackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        FeedbackDTO result = new FeedbackDTO();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = feedbackService.save(feedbackDTO);
            log.debug("Feedback DTO to create, belongs to current user: {}", feedbackDTO.toString());
        }

        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /feedbacks/:id} : Partial updates given fields of an existing
     * feedback, field will ignore if it is null
     *
     * @param id          the id of the feedbackDTO to save.
     * @param feedbackDTO the feedbackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated feedbackDTO, or with status {@code 400 (Bad Request)} if
     *         the feedbackDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the feedbackDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the feedbackDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedbacks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FeedbackDTO> partialUpdateFeedback(
            @PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody FeedbackDTO feedbackDTO)
            throws URISyntaxException {
        log.debug("REST request to partial update Feedback partially : {}, {}", id, feedbackDTO);
        if (feedbackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // NOTE Only for Admins
        Optional<FeedbackDTO> result = feedbackService.findOne(id);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = feedbackService.partialUpdate(feedbackDTO);
            log.debug("Feedback DTO to update, belongs to current user: {}", feedbackService.toString());
        }

        return ResponseUtil.wrapOrNotFound(result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDTO.getId().toString()));
    }

    /**
     * {@code GET  /feedbacks} : get all the feedbacks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of feedbacks in body.
     */
    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks(FeedbackCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Feedbacks by criteria: {}", criteria);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Page<FeedbackDTO> page = feedbackQueryService.findByCriteria(criteria, pageable);

            HttpHeaders headers = PaginationUtil
                    .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code GET  /feedbacks/count} : count all the feedbacks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/feedbacks/count")
    public ResponseEntity<Long> countFeedbacks(FeedbackCriteria criteria) {
        log.debug("REST request to count Feedbacks by criteria: {}", criteria);
        return ResponseEntity.ok().body(feedbackQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /feedbacks/:id} : get the "id" feedback.
     *
     * @param id the id of the feedbackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the feedbackDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackDTO> getFeedback(@PathVariable Long id) {
        log.debug("REST request to get Feedback : {}", id);
        Optional<FeedbackDTO> feedbackDTO = feedbackService.findOne(id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return ResponseUtil.wrapOrNotFound(feedbackDTO);
        }

        return ResponseUtil.wrapOrNotFound(null);
    }

    /**
     * {@code DELETE  /feedbacks/:id} : delete the "id" feedback.
     *
     * @param id the id of the feedbackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        log.debug("REST request to delete Feedback : {}", id);

        // NOTE Only for Admins
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            feedbackService.delete(id);
        }

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
