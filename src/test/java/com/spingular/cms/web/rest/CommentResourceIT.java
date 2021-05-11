package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Comment;
import com.spingular.cms.domain.Post;
import com.spingular.cms.repository.CommentRepository;
import com.spingular.cms.service.criteria.CommentCriteria;
import com.spingular.cms.service.dto.CommentDTO;
import com.spingular.cms.service.mapper.CommentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_OFFENSIVE = false;
    private static final Boolean UPDATED_IS_OFFENSIVE = true;

    private static final String ENTITY_API_URL = "/api/comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentMockMvc;

    private Comment comment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comment createEntity(EntityManager em) {
        Comment comment = new Comment()
            .creationDate(DEFAULT_CREATION_DATE)
            .commentText(DEFAULT_COMMENT_TEXT)
            .isOffensive(DEFAULT_IS_OFFENSIVE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        comment.setAppuser(appuser);
        // Add required entity
        Post post;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            post = PostResourceIT.createEntity(em);
            em.persist(post);
            em.flush();
        } else {
            post = TestUtil.findAll(em, Post.class).get(0);
        }
        comment.setPost(post);
        return comment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comment createUpdatedEntity(EntityManager em) {
        Comment comment = new Comment()
            .creationDate(UPDATED_CREATION_DATE)
            .commentText(UPDATED_COMMENT_TEXT)
            .isOffensive(UPDATED_IS_OFFENSIVE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        comment.setAppuser(appuser);
        // Add required entity
        Post post;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            post = PostResourceIT.createUpdatedEntity(em);
            em.persist(post);
            em.flush();
        } else {
            post = TestUtil.findAll(em, Post.class).get(0);
        }
        comment.setPost(post);
        return comment;
    }

    @BeforeEach
    public void initTest() {
        comment = createEntity(em);
    }

    @Test
    @Transactional
    void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();
        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);
        restCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testComment.getCommentText()).isEqualTo(DEFAULT_COMMENT_TEXT);
        assertThat(testComment.getIsOffensive()).isEqualTo(DEFAULT_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void createCommentWithExistingId() throws Exception {
        // Create the Comment with an existing ID
        comment.setId(1L);
        CommentDTO commentDTO = commentMapper.toDto(comment);

        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();
        // set the field null
        comment.setCreationDate(null);

        // Create the Comment, which fails.
        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommentTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();
        // set the field null
        comment.setCommentText(null);

        // Create the Comment, which fails.
        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT)))
            .andExpect(jsonPath("$.[*].isOffensive").value(hasItem(DEFAULT_IS_OFFENSIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.commentText").value(DEFAULT_COMMENT_TEXT))
            .andExpect(jsonPath("$.isOffensive").value(DEFAULT_IS_OFFENSIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getCommentsByIdFiltering() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        Long id = comment.getId();

        defaultCommentShouldBeFound("id.equals=" + id);
        defaultCommentShouldNotBeFound("id.notEquals=" + id);

        defaultCommentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where creationDate equals to DEFAULT_CREATION_DATE
        defaultCommentShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the commentList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommentShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommentsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultCommentShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the commentList where creationDate not equals to UPDATED_CREATION_DATE
        defaultCommentShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommentsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultCommentShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the commentList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommentShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommentsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where creationDate is not null
        defaultCommentShouldBeFound("creationDate.specified=true");

        // Get all the commentList where creationDate is null
        defaultCommentShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText equals to DEFAULT_COMMENT_TEXT
        defaultCommentShouldBeFound("commentText.equals=" + DEFAULT_COMMENT_TEXT);

        // Get all the commentList where commentText equals to UPDATED_COMMENT_TEXT
        defaultCommentShouldNotBeFound("commentText.equals=" + UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText not equals to DEFAULT_COMMENT_TEXT
        defaultCommentShouldNotBeFound("commentText.notEquals=" + DEFAULT_COMMENT_TEXT);

        // Get all the commentList where commentText not equals to UPDATED_COMMENT_TEXT
        defaultCommentShouldBeFound("commentText.notEquals=" + UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextIsInShouldWork() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText in DEFAULT_COMMENT_TEXT or UPDATED_COMMENT_TEXT
        defaultCommentShouldBeFound("commentText.in=" + DEFAULT_COMMENT_TEXT + "," + UPDATED_COMMENT_TEXT);

        // Get all the commentList where commentText equals to UPDATED_COMMENT_TEXT
        defaultCommentShouldNotBeFound("commentText.in=" + UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText is not null
        defaultCommentShouldBeFound("commentText.specified=true");

        // Get all the commentList where commentText is null
        defaultCommentShouldNotBeFound("commentText.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextContainsSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText contains DEFAULT_COMMENT_TEXT
        defaultCommentShouldBeFound("commentText.contains=" + DEFAULT_COMMENT_TEXT);

        // Get all the commentList where commentText contains UPDATED_COMMENT_TEXT
        defaultCommentShouldNotBeFound("commentText.contains=" + UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void getAllCommentsByCommentTextNotContainsSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where commentText does not contain DEFAULT_COMMENT_TEXT
        defaultCommentShouldNotBeFound("commentText.doesNotContain=" + DEFAULT_COMMENT_TEXT);

        // Get all the commentList where commentText does not contain UPDATED_COMMENT_TEXT
        defaultCommentShouldBeFound("commentText.doesNotContain=" + UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void getAllCommentsByIsOffensiveIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where isOffensive equals to DEFAULT_IS_OFFENSIVE
        defaultCommentShouldBeFound("isOffensive.equals=" + DEFAULT_IS_OFFENSIVE);

        // Get all the commentList where isOffensive equals to UPDATED_IS_OFFENSIVE
        defaultCommentShouldNotBeFound("isOffensive.equals=" + UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void getAllCommentsByIsOffensiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where isOffensive not equals to DEFAULT_IS_OFFENSIVE
        defaultCommentShouldNotBeFound("isOffensive.notEquals=" + DEFAULT_IS_OFFENSIVE);

        // Get all the commentList where isOffensive not equals to UPDATED_IS_OFFENSIVE
        defaultCommentShouldBeFound("isOffensive.notEquals=" + UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void getAllCommentsByIsOffensiveIsInShouldWork() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where isOffensive in DEFAULT_IS_OFFENSIVE or UPDATED_IS_OFFENSIVE
        defaultCommentShouldBeFound("isOffensive.in=" + DEFAULT_IS_OFFENSIVE + "," + UPDATED_IS_OFFENSIVE);

        // Get all the commentList where isOffensive equals to UPDATED_IS_OFFENSIVE
        defaultCommentShouldNotBeFound("isOffensive.in=" + UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void getAllCommentsByIsOffensiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList where isOffensive is not null
        defaultCommentShouldBeFound("isOffensive.specified=true");

        // Get all the commentList where isOffensive is null
        defaultCommentShouldNotBeFound("isOffensive.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);
        Appuser appuser = AppuserResourceIT.createEntity(em);
        em.persist(appuser);
        em.flush();
        comment.setAppuser(appuser);
        commentRepository.saveAndFlush(comment);
        Long appuserId = appuser.getId();

        // Get all the commentList where appuser equals to appuserId
        defaultCommentShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the commentList where appuser equals to (appuserId + 1)
        defaultCommentShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllCommentsByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);
        Post post = PostResourceIT.createEntity(em);
        em.persist(post);
        em.flush();
        comment.setPost(post);
        commentRepository.saveAndFlush(comment);
        Long postId = post.getId();

        // Get all the commentList where post equals to postId
        defaultCommentShouldBeFound("postId.equals=" + postId);

        // Get all the commentList where post equals to (postId + 1)
        defaultCommentShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentShouldBeFound(String filter) throws Exception {
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT)))
            .andExpect(jsonPath("$.[*].isOffensive").value(hasItem(DEFAULT_IS_OFFENSIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentShouldNotBeFound(String filter) throws Exception {
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        // Disconnect from session so that the updates on updatedComment are not directly saved in db
        em.detach(updatedComment);
        updatedComment.creationDate(UPDATED_CREATION_DATE).commentText(UPDATED_COMMENT_TEXT).isOffensive(UPDATED_IS_OFFENSIVE);
        CommentDTO commentDTO = commentMapper.toDto(updatedComment);

        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
        assertThat(testComment.getIsOffensive()).isEqualTo(UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void putNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentWithPatch() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment using partial update
        Comment partialUpdatedComment = new Comment();
        partialUpdatedComment.setId(comment.getId());

        partialUpdatedComment.creationDate(UPDATED_CREATION_DATE).commentText(UPDATED_COMMENT_TEXT).isOffensive(UPDATED_IS_OFFENSIVE);

        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComment))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
        assertThat(testComment.getIsOffensive()).isEqualTo(UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void fullUpdateCommentWithPatch() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment using partial update
        Comment partialUpdatedComment = new Comment();
        partialUpdatedComment.setId(comment.getId());

        partialUpdatedComment.creationDate(UPDATED_CREATION_DATE).commentText(UPDATED_COMMENT_TEXT).isOffensive(UPDATED_IS_OFFENSIVE);

        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComment))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
        assertThat(testComment.getIsOffensive()).isEqualTo(UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    void patchNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Delete the comment
        restCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, comment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
