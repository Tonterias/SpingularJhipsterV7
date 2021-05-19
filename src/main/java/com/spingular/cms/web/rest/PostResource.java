package com.spingular.cms.web.rest;

import com.spingular.cms.domain.Appuser;
import com.spingular.cms.repository.AppuserRepository;
import com.spingular.cms.repository.PostRepository;
import com.spingular.cms.repository.UserRepository;
import com.spingular.cms.security.AuthoritiesConstants;
import com.spingular.cms.security.SecurityUtils;
import com.spingular.cms.service.PostQueryService;
import com.spingular.cms.service.PostService;
import com.spingular.cms.service.criteria.PostCriteria;
import com.spingular.cms.service.dto.PostDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.spingular.cms.domain.Post}.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostService postService;

    private final PostRepository postRepository;

    private final PostQueryService postQueryService;

    private final UserRepository userRepository;

    private final AppuserRepository appuserRepository;

    public PostResource(
        PostService postService,
        PostRepository postRepository,
        PostQueryService postQueryService,
        UserRepository userRepository,
        AppuserRepository appuserRepository
    ) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.postQueryService = postQueryService;
        this.userRepository = userRepository;
        this.appuserRepository = appuserRepository;
    }

    /**
     * {@code POST  /posts} : Create a new post.
     *
     * @param postDTO the postDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new postDTO, or with status {@code 400 (Bad Request)} if the
     *         post has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDTO);
        if (postDTO.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }

        PostDTO result = new PostDTO();
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (postDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = postService.save(postDTO);
                    log.debug("Post DTO to create, belongs to current user: {}", postDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = postService.save(postDTO);
                log.debug("Post DTO to create, belongs to current user: {}", postDTO.toString());
            }
        }

        return ResponseEntity
            .created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing post.
     *
     * @param id      the id of the postDTO to save.
     * @param postDTO the postDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated postDTO, or with status {@code 400 (Bad Request)} if the
     *         postDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the postDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDTO> updatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostDTO postDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Post : {}, {}", id, postDTO);
        if (postDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostDTO result = postService.findOne(id).orElse(postDTO);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (postDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = postService.save(postDTO);
                    log.debug("Post DTO to update, belongs to current user: {}", postDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = postService.save(postDTO);
                log.debug("Post DTO to update, belongs to current user: {}", postDTO.toString());
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing post,
     * field will ignore if it is null
     *
     * @param id      the id of the postDTO to save.
     * @param postDTO the postDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated postDTO, or with status {@code 400 (Bad Request)} if the
     *         postDTO is not valid, or with status {@code 404 (Not Found)} if the
     *         postDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the postDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/posts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PostDTO> partialUpdatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostDTO postDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Post partially : {}, {}", id, postDTO);
        if (postDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostDTO> result = postService.findOne(id);
        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser appuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                if (postDTO.getAppuser().getId().equals(appuser.getId())) {
                    result = postService.partialUpdate(postDTO);
                    log.debug("Post DTO to partial update, belongs to current user: {}", postDTO.toString());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                result = postService.partialUpdate(postDTO);
                log.debug("Post DTO to partial update, belongs to current user: {}", postDTO.toString());
            }
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of posts in body.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(PostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Posts by criteria: {}", criteria);

        // Note: Any visitor can see them all
        Page<PostDTO> page = postQueryService.findByCriteria(criteria, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posts/count} : count all the posts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/posts/count")
    public ResponseEntity<Long> countPosts(PostCriteria criteria) {
        log.debug("REST request to count Posts by criteria: {}", criteria);
        return ResponseEntity.ok().body(postQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the postDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the postDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<PostDTO> postDTO = postService.findOne(id);

        // Note: Any visitor can see them all
        return ResponseUtil.wrapOrNotFound(postDTO);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the postDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);

        if (userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
                Appuser loggedAppuser = appuserRepository.findByUserId(
                    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId()
                );
                PostDTO postDTO = postService.findOne(id).orElse(new PostDTO());
                if (postDTO.getAppuser().getId().equals(loggedAppuser.getId())) {
                    postService.delete(id);
                    log.debug("Post DTO to delete, belongs to current user: {}", postDTO.toString());
                    log.debug("Post DTO to delete, belongs to Appuser: {}", loggedAppuser.getId());
                }
            }
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                postService.delete(id);
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
