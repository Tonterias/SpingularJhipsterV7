package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Feedback;
import com.spingular.cms.repository.FeedbackRepository;
import com.spingular.cms.service.criteria.FeedbackCriteria;
import com.spingular.cms.service.dto.FeedbackDTO;
import com.spingular.cms.service.mapper.FeedbackMapper;
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
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .creationDate(DEFAULT_CREATION_DATE)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .feedback(DEFAULT_FEEDBACK);
        return feedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .creationDate(UPDATED_CREATION_DATE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .feedback(UPDATED_FEEDBACK);
        return feedback;
    }

    @BeforeEach
    public void initTest() {
        feedback = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedback() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();
        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);
        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate + 1);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFeedback.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFeedback.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFeedback.getFeedback()).isEqualTo(DEFAULT_FEEDBACK);
    }

    @Test
    @Transactional
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setCreationDate(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setName(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setEmail(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeedbackIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setFeedback(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbacks() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK)));
    }

    @Test
    @Transactional
    void getFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK));
    }

    @Test
    @Transactional
    void getFeedbacksByIdFiltering() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        Long id = feedback.getId();

        defaultFeedbackShouldBeFound("id.equals=" + id);
        defaultFeedbackShouldNotBeFound("id.notEquals=" + id);

        defaultFeedbackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFeedbackShouldNotBeFound("id.greaterThan=" + id);

        defaultFeedbackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFeedbackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeedbacksByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where creationDate equals to DEFAULT_CREATION_DATE
        defaultFeedbackShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the feedbackList where creationDate equals to UPDATED_CREATION_DATE
        defaultFeedbackShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultFeedbackShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the feedbackList where creationDate not equals to UPDATED_CREATION_DATE
        defaultFeedbackShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultFeedbackShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the feedbackList where creationDate equals to UPDATED_CREATION_DATE
        defaultFeedbackShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where creationDate is not null
        defaultFeedbackShouldBeFound("creationDate.specified=true");

        // Get all the feedbackList where creationDate is null
        defaultFeedbackShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name equals to DEFAULT_NAME
        defaultFeedbackShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the feedbackList where name equals to UPDATED_NAME
        defaultFeedbackShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name not equals to DEFAULT_NAME
        defaultFeedbackShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the feedbackList where name not equals to UPDATED_NAME
        defaultFeedbackShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFeedbackShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the feedbackList where name equals to UPDATED_NAME
        defaultFeedbackShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name is not null
        defaultFeedbackShouldBeFound("name.specified=true");

        // Get all the feedbackList where name is null
        defaultFeedbackShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name contains DEFAULT_NAME
        defaultFeedbackShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the feedbackList where name contains UPDATED_NAME
        defaultFeedbackShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where name does not contain DEFAULT_NAME
        defaultFeedbackShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the feedbackList where name does not contain UPDATED_NAME
        defaultFeedbackShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email equals to DEFAULT_EMAIL
        defaultFeedbackShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the feedbackList where email equals to UPDATED_EMAIL
        defaultFeedbackShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email not equals to DEFAULT_EMAIL
        defaultFeedbackShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the feedbackList where email not equals to UPDATED_EMAIL
        defaultFeedbackShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultFeedbackShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the feedbackList where email equals to UPDATED_EMAIL
        defaultFeedbackShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email is not null
        defaultFeedbackShouldBeFound("email.specified=true");

        // Get all the feedbackList where email is null
        defaultFeedbackShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email contains DEFAULT_EMAIL
        defaultFeedbackShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the feedbackList where email contains UPDATED_EMAIL
        defaultFeedbackShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFeedbacksByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where email does not contain DEFAULT_EMAIL
        defaultFeedbackShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the feedbackList where email does not contain UPDATED_EMAIL
        defaultFeedbackShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackIsEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback equals to DEFAULT_FEEDBACK
        defaultFeedbackShouldBeFound("feedback.equals=" + DEFAULT_FEEDBACK);

        // Get all the feedbackList where feedback equals to UPDATED_FEEDBACK
        defaultFeedbackShouldNotBeFound("feedback.equals=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback not equals to DEFAULT_FEEDBACK
        defaultFeedbackShouldNotBeFound("feedback.notEquals=" + DEFAULT_FEEDBACK);

        // Get all the feedbackList where feedback not equals to UPDATED_FEEDBACK
        defaultFeedbackShouldBeFound("feedback.notEquals=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackIsInShouldWork() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback in DEFAULT_FEEDBACK or UPDATED_FEEDBACK
        defaultFeedbackShouldBeFound("feedback.in=" + DEFAULT_FEEDBACK + "," + UPDATED_FEEDBACK);

        // Get all the feedbackList where feedback equals to UPDATED_FEEDBACK
        defaultFeedbackShouldNotBeFound("feedback.in=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackIsNullOrNotNull() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback is not null
        defaultFeedbackShouldBeFound("feedback.specified=true");

        // Get all the feedbackList where feedback is null
        defaultFeedbackShouldNotBeFound("feedback.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback contains DEFAULT_FEEDBACK
        defaultFeedbackShouldBeFound("feedback.contains=" + DEFAULT_FEEDBACK);

        // Get all the feedbackList where feedback contains UPDATED_FEEDBACK
        defaultFeedbackShouldNotBeFound("feedback.contains=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllFeedbacksByFeedbackNotContainsSomething() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where feedback does not contain DEFAULT_FEEDBACK
        defaultFeedbackShouldNotBeFound("feedback.doesNotContain=" + DEFAULT_FEEDBACK);

        // Get all the feedbackList where feedback does not contain UPDATED_FEEDBACK
        defaultFeedbackShouldBeFound("feedback.doesNotContain=" + UPDATED_FEEDBACK);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeedbackShouldBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK)));

        // Check, that the count call also returns 1
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeedbackShouldNotBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).get();
        // Disconnect from session so that the updates on updatedFeedback are not directly saved in db
        em.detach(updatedFeedback);
        updatedFeedback.creationDate(UPDATED_CREATION_DATE).name(UPDATED_NAME).email(UPDATED_EMAIL).feedback(UPDATED_FEEDBACK);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(updatedFeedback);

        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeedback.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFeedback.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFeedback.getFeedback()).isEqualTo(UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void putNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback.creationDate(UPDATED_CREATION_DATE).name(UPDATED_NAME).email(UPDATED_EMAIL);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeedback.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFeedback.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFeedback.getFeedback()).isEqualTo(DEFAULT_FEEDBACK);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback.creationDate(UPDATED_CREATION_DATE).name(UPDATED_NAME).email(UPDATED_EMAIL).feedback(UPDATED_FEEDBACK);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeedback.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFeedback.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFeedback.getFeedback()).isEqualTo(UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void patchNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(count.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeDelete = feedbackRepository.findAll().size();

        // Delete the feedback
        restFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedback.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
