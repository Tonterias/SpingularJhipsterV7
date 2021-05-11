package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Activity;
import com.spingular.cms.domain.Appphoto;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Blockuser;
import com.spingular.cms.domain.Blog;
import com.spingular.cms.domain.Celeb;
import com.spingular.cms.domain.Comment;
import com.spingular.cms.domain.Community;
import com.spingular.cms.domain.Follow;
import com.spingular.cms.domain.Interest;
import com.spingular.cms.domain.Notification;
import com.spingular.cms.domain.Post;
import com.spingular.cms.domain.User;
import com.spingular.cms.repository.AppuserRepository;
import com.spingular.cms.service.criteria.AppuserCriteria;
import com.spingular.cms.service.dto.AppuserDTO;
import com.spingular.cms.service.mapper.AppuserMapper;
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
 * Integration tests for the {@link AppuserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppuserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/appusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppuserRepository appuserRepository;

    @Autowired
    private AppuserMapper appuserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppuserMockMvc;

    private Appuser appuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createEntity(EntityManager em) {
        Appuser appuser = new Appuser()
            .creationDate(DEFAULT_CREATION_DATE)
            .bio(DEFAULT_BIO)
            .facebook(DEFAULT_FACEBOOK)
            .twitter(DEFAULT_TWITTER)
            .linkedin(DEFAULT_LINKEDIN)
            .instagram(DEFAULT_INSTAGRAM)
            .birthdate(DEFAULT_BIRTHDATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createUpdatedEntity(EntityManager em) {
        Appuser appuser = new Appuser()
            .creationDate(UPDATED_CREATION_DATE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .birthdate(UPDATED_BIRTHDATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    @BeforeEach
    public void initTest() {
        appuser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppuser() throws Exception {
        int databaseSizeBeforeCreate = appuserRepository.findAll().size();
        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);
        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate + 1);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppuser.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testAppuser.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testAppuser.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testAppuser.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testAppuser.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testAppuser.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    void createAppuserWithExistingId() throws Exception {
        // Create the Appuser with an existing ID
        appuser.setId(1L);
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        int databaseSizeBeforeCreate = appuserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appuserRepository.findAll().size();
        // set the field null
        appuser.setCreationDate(null);

        // Create the Appuser, which fails.
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppusers() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));
    }

    @Test
    @Transactional
    void getAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get the appuser
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL_ID, appuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appuser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()));
    }

    @Test
    @Transactional
    void getAppusersByIdFiltering() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        Long id = appuser.getId();

        defaultAppuserShouldBeFound("id.equals=" + id);
        defaultAppuserShouldNotBeFound("id.notEquals=" + id);

        defaultAppuserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.greaterThan=" + id);

        defaultAppuserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the appuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the appuserList where creationDate not equals to UPDATED_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the appuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate is not null
        defaultAppuserShouldBeFound("creationDate.specified=true");

        // Get all the appuserList where creationDate is null
        defaultAppuserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio equals to DEFAULT_BIO
        defaultAppuserShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the appuserList where bio equals to UPDATED_BIO
        defaultAppuserShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllAppusersByBioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio not equals to DEFAULT_BIO
        defaultAppuserShouldNotBeFound("bio.notEquals=" + DEFAULT_BIO);

        // Get all the appuserList where bio not equals to UPDATED_BIO
        defaultAppuserShouldBeFound("bio.notEquals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllAppusersByBioIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultAppuserShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the appuserList where bio equals to UPDATED_BIO
        defaultAppuserShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllAppusersByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio is not null
        defaultAppuserShouldBeFound("bio.specified=true");

        // Get all the appuserList where bio is null
        defaultAppuserShouldNotBeFound("bio.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByBioContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio contains DEFAULT_BIO
        defaultAppuserShouldBeFound("bio.contains=" + DEFAULT_BIO);

        // Get all the appuserList where bio contains UPDATED_BIO
        defaultAppuserShouldNotBeFound("bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllAppusersByBioNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where bio does not contain DEFAULT_BIO
        defaultAppuserShouldNotBeFound("bio.doesNotContain=" + DEFAULT_BIO);

        // Get all the appuserList where bio does not contain UPDATED_BIO
        defaultAppuserShouldBeFound("bio.doesNotContain=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook equals to DEFAULT_FACEBOOK
        defaultAppuserShouldBeFound("facebook.equals=" + DEFAULT_FACEBOOK);

        // Get all the appuserList where facebook equals to UPDATED_FACEBOOK
        defaultAppuserShouldNotBeFound("facebook.equals=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook not equals to DEFAULT_FACEBOOK
        defaultAppuserShouldNotBeFound("facebook.notEquals=" + DEFAULT_FACEBOOK);

        // Get all the appuserList where facebook not equals to UPDATED_FACEBOOK
        defaultAppuserShouldBeFound("facebook.notEquals=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook in DEFAULT_FACEBOOK or UPDATED_FACEBOOK
        defaultAppuserShouldBeFound("facebook.in=" + DEFAULT_FACEBOOK + "," + UPDATED_FACEBOOK);

        // Get all the appuserList where facebook equals to UPDATED_FACEBOOK
        defaultAppuserShouldNotBeFound("facebook.in=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook is not null
        defaultAppuserShouldBeFound("facebook.specified=true");

        // Get all the appuserList where facebook is null
        defaultAppuserShouldNotBeFound("facebook.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook contains DEFAULT_FACEBOOK
        defaultAppuserShouldBeFound("facebook.contains=" + DEFAULT_FACEBOOK);

        // Get all the appuserList where facebook contains UPDATED_FACEBOOK
        defaultAppuserShouldNotBeFound("facebook.contains=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllAppusersByFacebookNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where facebook does not contain DEFAULT_FACEBOOK
        defaultAppuserShouldNotBeFound("facebook.doesNotContain=" + DEFAULT_FACEBOOK);

        // Get all the appuserList where facebook does not contain UPDATED_FACEBOOK
        defaultAppuserShouldBeFound("facebook.doesNotContain=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter equals to DEFAULT_TWITTER
        defaultAppuserShouldBeFound("twitter.equals=" + DEFAULT_TWITTER);

        // Get all the appuserList where twitter equals to UPDATED_TWITTER
        defaultAppuserShouldNotBeFound("twitter.equals=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter not equals to DEFAULT_TWITTER
        defaultAppuserShouldNotBeFound("twitter.notEquals=" + DEFAULT_TWITTER);

        // Get all the appuserList where twitter not equals to UPDATED_TWITTER
        defaultAppuserShouldBeFound("twitter.notEquals=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter in DEFAULT_TWITTER or UPDATED_TWITTER
        defaultAppuserShouldBeFound("twitter.in=" + DEFAULT_TWITTER + "," + UPDATED_TWITTER);

        // Get all the appuserList where twitter equals to UPDATED_TWITTER
        defaultAppuserShouldNotBeFound("twitter.in=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter is not null
        defaultAppuserShouldBeFound("twitter.specified=true");

        // Get all the appuserList where twitter is null
        defaultAppuserShouldNotBeFound("twitter.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter contains DEFAULT_TWITTER
        defaultAppuserShouldBeFound("twitter.contains=" + DEFAULT_TWITTER);

        // Get all the appuserList where twitter contains UPDATED_TWITTER
        defaultAppuserShouldNotBeFound("twitter.contains=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    void getAllAppusersByTwitterNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where twitter does not contain DEFAULT_TWITTER
        defaultAppuserShouldNotBeFound("twitter.doesNotContain=" + DEFAULT_TWITTER);

        // Get all the appuserList where twitter does not contain UPDATED_TWITTER
        defaultAppuserShouldBeFound("twitter.doesNotContain=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin equals to DEFAULT_LINKEDIN
        defaultAppuserShouldBeFound("linkedin.equals=" + DEFAULT_LINKEDIN);

        // Get all the appuserList where linkedin equals to UPDATED_LINKEDIN
        defaultAppuserShouldNotBeFound("linkedin.equals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin not equals to DEFAULT_LINKEDIN
        defaultAppuserShouldNotBeFound("linkedin.notEquals=" + DEFAULT_LINKEDIN);

        // Get all the appuserList where linkedin not equals to UPDATED_LINKEDIN
        defaultAppuserShouldBeFound("linkedin.notEquals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin in DEFAULT_LINKEDIN or UPDATED_LINKEDIN
        defaultAppuserShouldBeFound("linkedin.in=" + DEFAULT_LINKEDIN + "," + UPDATED_LINKEDIN);

        // Get all the appuserList where linkedin equals to UPDATED_LINKEDIN
        defaultAppuserShouldNotBeFound("linkedin.in=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin is not null
        defaultAppuserShouldBeFound("linkedin.specified=true");

        // Get all the appuserList where linkedin is null
        defaultAppuserShouldNotBeFound("linkedin.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin contains DEFAULT_LINKEDIN
        defaultAppuserShouldBeFound("linkedin.contains=" + DEFAULT_LINKEDIN);

        // Get all the appuserList where linkedin contains UPDATED_LINKEDIN
        defaultAppuserShouldNotBeFound("linkedin.contains=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllAppusersByLinkedinNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where linkedin does not contain DEFAULT_LINKEDIN
        defaultAppuserShouldNotBeFound("linkedin.doesNotContain=" + DEFAULT_LINKEDIN);

        // Get all the appuserList where linkedin does not contain UPDATED_LINKEDIN
        defaultAppuserShouldBeFound("linkedin.doesNotContain=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram equals to DEFAULT_INSTAGRAM
        defaultAppuserShouldBeFound("instagram.equals=" + DEFAULT_INSTAGRAM);

        // Get all the appuserList where instagram equals to UPDATED_INSTAGRAM
        defaultAppuserShouldNotBeFound("instagram.equals=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram not equals to DEFAULT_INSTAGRAM
        defaultAppuserShouldNotBeFound("instagram.notEquals=" + DEFAULT_INSTAGRAM);

        // Get all the appuserList where instagram not equals to UPDATED_INSTAGRAM
        defaultAppuserShouldBeFound("instagram.notEquals=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram in DEFAULT_INSTAGRAM or UPDATED_INSTAGRAM
        defaultAppuserShouldBeFound("instagram.in=" + DEFAULT_INSTAGRAM + "," + UPDATED_INSTAGRAM);

        // Get all the appuserList where instagram equals to UPDATED_INSTAGRAM
        defaultAppuserShouldNotBeFound("instagram.in=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram is not null
        defaultAppuserShouldBeFound("instagram.specified=true");

        // Get all the appuserList where instagram is null
        defaultAppuserShouldNotBeFound("instagram.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram contains DEFAULT_INSTAGRAM
        defaultAppuserShouldBeFound("instagram.contains=" + DEFAULT_INSTAGRAM);

        // Get all the appuserList where instagram contains UPDATED_INSTAGRAM
        defaultAppuserShouldNotBeFound("instagram.contains=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllAppusersByInstagramNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where instagram does not contain DEFAULT_INSTAGRAM
        defaultAppuserShouldNotBeFound("instagram.doesNotContain=" + DEFAULT_INSTAGRAM);

        // Get all the appuserList where instagram does not contain UPDATED_INSTAGRAM
        defaultAppuserShouldBeFound("instagram.doesNotContain=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllAppusersByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where birthdate equals to DEFAULT_BIRTHDATE
        defaultAppuserShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the appuserList where birthdate equals to UPDATED_BIRTHDATE
        defaultAppuserShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllAppusersByBirthdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where birthdate not equals to DEFAULT_BIRTHDATE
        defaultAppuserShouldNotBeFound("birthdate.notEquals=" + DEFAULT_BIRTHDATE);

        // Get all the appuserList where birthdate not equals to UPDATED_BIRTHDATE
        defaultAppuserShouldBeFound("birthdate.notEquals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllAppusersByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultAppuserShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the appuserList where birthdate equals to UPDATED_BIRTHDATE
        defaultAppuserShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllAppusersByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where birthdate is not null
        defaultAppuserShouldBeFound("birthdate.specified=true");

        // Get all the appuserList where birthdate is null
        defaultAppuserShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = appuser.getUser();
        appuserRepository.saveAndFlush(appuser);
        Long userId = user.getId();

        // Get all the appuserList where user equals to userId
        defaultAppuserShouldBeFound("userId.equals=" + userId);

        // Get all the appuserList where user equals to (userId + 1)
        defaultAppuserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlogIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Blog blog = BlogResourceIT.createEntity(em);
        em.persist(blog);
        em.flush();
        appuser.addBlog(blog);
        appuserRepository.saveAndFlush(appuser);
        Long blogId = blog.getId();

        // Get all the appuserList where blog equals to blogId
        defaultAppuserShouldBeFound("blogId.equals=" + blogId);

        // Get all the appuserList where blog equals to (blogId + 1)
        defaultAppuserShouldNotBeFound("blogId.equals=" + (blogId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        appuser.addCommunity(community);
        appuserRepository.saveAndFlush(appuser);
        Long communityId = community.getId();

        // Get all the appuserList where community equals to communityId
        defaultAppuserShouldBeFound("communityId.equals=" + communityId);

        // Get all the appuserList where community equals to (communityId + 1)
        defaultAppuserShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Notification notification = NotificationResourceIT.createEntity(em);
        em.persist(notification);
        em.flush();
        appuser.addNotification(notification);
        appuserRepository.saveAndFlush(appuser);
        Long notificationId = notification.getId();

        // Get all the appuserList where notification equals to notificationId
        defaultAppuserShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the appuserList where notification equals to (notificationId + 1)
        defaultAppuserShouldNotBeFound("notificationId.equals=" + (notificationId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Comment comment = CommentResourceIT.createEntity(em);
        em.persist(comment);
        em.flush();
        appuser.addComment(comment);
        appuserRepository.saveAndFlush(appuser);
        Long commentId = comment.getId();

        // Get all the appuserList where comment equals to commentId
        defaultAppuserShouldBeFound("commentId.equals=" + commentId);

        // Get all the appuserList where comment equals to (commentId + 1)
        defaultAppuserShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Post post = PostResourceIT.createEntity(em);
        em.persist(post);
        em.flush();
        appuser.addPost(post);
        appuserRepository.saveAndFlush(appuser);
        Long postId = post.getId();

        // Get all the appuserList where post equals to postId
        defaultAppuserShouldBeFound("postId.equals=" + postId);

        // Get all the appuserList where post equals to (postId + 1)
        defaultAppuserShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByFollowedIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Follow followed = FollowResourceIT.createEntity(em);
        em.persist(followed);
        em.flush();
        appuser.addFollowed(followed);
        appuserRepository.saveAndFlush(appuser);
        Long followedId = followed.getId();

        // Get all the appuserList where followed equals to followedId
        defaultAppuserShouldBeFound("followedId.equals=" + followedId);

        // Get all the appuserList where followed equals to (followedId + 1)
        defaultAppuserShouldNotBeFound("followedId.equals=" + (followedId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByFollowingIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Follow following = FollowResourceIT.createEntity(em);
        em.persist(following);
        em.flush();
        appuser.addFollowing(following);
        appuserRepository.saveAndFlush(appuser);
        Long followingId = following.getId();

        // Get all the appuserList where following equals to followingId
        defaultAppuserShouldBeFound("followingId.equals=" + followingId);

        // Get all the appuserList where following equals to (followingId + 1)
        defaultAppuserShouldNotBeFound("followingId.equals=" + (followingId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlockeduserIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Blockuser blockeduser = BlockuserResourceIT.createEntity(em);
        em.persist(blockeduser);
        em.flush();
        appuser.addBlockeduser(blockeduser);
        appuserRepository.saveAndFlush(appuser);
        Long blockeduserId = blockeduser.getId();

        // Get all the appuserList where blockeduser equals to blockeduserId
        defaultAppuserShouldBeFound("blockeduserId.equals=" + blockeduserId);

        // Get all the appuserList where blockeduser equals to (blockeduserId + 1)
        defaultAppuserShouldNotBeFound("blockeduserId.equals=" + (blockeduserId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlockinguserIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Blockuser blockinguser = BlockuserResourceIT.createEntity(em);
        em.persist(blockinguser);
        em.flush();
        appuser.addBlockinguser(blockinguser);
        appuserRepository.saveAndFlush(appuser);
        Long blockinguserId = blockinguser.getId();

        // Get all the appuserList where blockinguser equals to blockinguserId
        defaultAppuserShouldBeFound("blockinguserId.equals=" + blockinguserId);

        // Get all the appuserList where blockinguser equals to (blockinguserId + 1)
        defaultAppuserShouldNotBeFound("blockinguserId.equals=" + (blockinguserId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByAppphotoIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Appphoto appphoto = AppphotoResourceIT.createEntity(em);
        em.persist(appphoto);
        em.flush();
        appuser.setAppphoto(appphoto);
        appphoto.setAppuser(appuser);
        appuserRepository.saveAndFlush(appuser);
        Long appphotoId = appphoto.getId();

        // Get all the appuserList where appphoto equals to appphotoId
        defaultAppuserShouldBeFound("appphotoId.equals=" + appphotoId);

        // Get all the appuserList where appphoto equals to (appphotoId + 1)
        defaultAppuserShouldNotBeFound("appphotoId.equals=" + (appphotoId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByInterestIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Interest interest = InterestResourceIT.createEntity(em);
        em.persist(interest);
        em.flush();
        appuser.addInterest(interest);
        appuserRepository.saveAndFlush(appuser);
        Long interestId = interest.getId();

        // Get all the appuserList where interest equals to interestId
        defaultAppuserShouldBeFound("interestId.equals=" + interestId);

        // Get all the appuserList where interest equals to (interestId + 1)
        defaultAppuserShouldNotBeFound("interestId.equals=" + (interestId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByActivityIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Activity activity = ActivityResourceIT.createEntity(em);
        em.persist(activity);
        em.flush();
        appuser.addActivity(activity);
        appuserRepository.saveAndFlush(appuser);
        Long activityId = activity.getId();

        // Get all the appuserList where activity equals to activityId
        defaultAppuserShouldBeFound("activityId.equals=" + activityId);

        // Get all the appuserList where activity equals to (activityId + 1)
        defaultAppuserShouldNotBeFound("activityId.equals=" + (activityId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCelebIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Celeb celeb = CelebResourceIT.createEntity(em);
        em.persist(celeb);
        em.flush();
        appuser.addCeleb(celeb);
        appuserRepository.saveAndFlush(appuser);
        Long celebId = celeb.getId();

        // Get all the appuserList where celeb equals to celebId
        defaultAppuserShouldBeFound("celebId.equals=" + celebId);

        // Get all the appuserList where celeb equals to (celebId + 1)
        defaultAppuserShouldNotBeFound("celebId.equals=" + (celebId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppuserShouldBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));

        // Check, that the count call also returns 1
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppuserShouldNotBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppuser() throws Exception {
        // Get the appuser
        restAppuserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser
        Appuser updatedAppuser = appuserRepository.findById(appuser.getId()).get();
        // Disconnect from session so that the updates on updatedAppuser are not directly saved in db
        em.detach(updatedAppuser);
        updatedAppuser
            .creationDate(UPDATED_CREATION_DATE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .birthdate(UPDATED_BIRTHDATE);
        AppuserDTO appuserDTO = appuserMapper.toDto(updatedAppuser);

        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppuser.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testAppuser.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testAppuser.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testAppuser.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testAppuser.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testAppuser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void putNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser.bio(UPDATED_BIO).twitter(UPDATED_TWITTER).instagram(UPDATED_INSTAGRAM).birthdate(UPDATED_BIRTHDATE);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppuser.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testAppuser.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testAppuser.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testAppuser.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testAppuser.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testAppuser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void fullUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser
            .creationDate(UPDATED_CREATION_DATE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .birthdate(UPDATED_BIRTHDATE);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppuser.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testAppuser.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testAppuser.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testAppuser.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testAppuser.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testAppuser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void patchNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeDelete = appuserRepository.findAll().size();

        // Delete the appuser
        restAppuserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appuser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
