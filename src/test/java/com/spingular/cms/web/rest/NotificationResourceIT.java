package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Notification;
import com.spingular.cms.domain.enumeration.NotificationReason;
import com.spingular.cms.repository.NotificationRepository;
import com.spingular.cms.service.criteria.NotificationCriteria;
import com.spingular.cms.service.dto.NotificationDTO;
import com.spingular.cms.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NOTIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NOTIFICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NotificationReason DEFAULT_NOTIFICATION_REASON = NotificationReason.FOLLOWING;
    private static final NotificationReason UPDATED_NOTIFICATION_REASON = NotificationReason.UNFOLLOWING;

    private static final String DEFAULT_NOTIFICATION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELIVERED = false;
    private static final Boolean UPDATED_IS_DELIVERED = true;

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .creationDate(DEFAULT_CREATION_DATE)
            .notificationDate(DEFAULT_NOTIFICATION_DATE)
            .notificationReason(DEFAULT_NOTIFICATION_REASON)
            .notificationText(DEFAULT_NOTIFICATION_TEXT)
            .isDelivered(DEFAULT_IS_DELIVERED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        notification.setAppuser(appuser);
        return notification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .creationDate(UPDATED_CREATION_DATE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .notificationReason(UPDATED_NOTIFICATION_REASON)
            .notificationText(UPDATED_NOTIFICATION_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        notification.setAppuser(appuser);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(DEFAULT_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(DEFAULT_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(DEFAULT_NOTIFICATION_TEXT);
        assertThat(testNotification.getIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setCreationDate(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setNotificationReason(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationReason").value(hasItem(DEFAULT_NOTIFICATION_REASON.toString())))
            .andExpect(jsonPath("$.[*].notificationText").value(hasItem(DEFAULT_NOTIFICATION_TEXT)))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));
    }

    @Test
    @Transactional
    void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.notificationDate").value(DEFAULT_NOTIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.notificationReason").value(DEFAULT_NOTIFICATION_REASON.toString()))
            .andExpect(jsonPath("$.notificationText").value(DEFAULT_NOTIFICATION_TEXT))
            .andExpect(jsonPath("$.isDelivered").value(DEFAULT_IS_DELIVERED.booleanValue()));
    }

    @Test
    @Transactional
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where creationDate equals to DEFAULT_CREATION_DATE
        defaultNotificationShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the notificationList where creationDate equals to UPDATED_CREATION_DATE
        defaultNotificationShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultNotificationShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the notificationList where creationDate not equals to UPDATED_CREATION_DATE
        defaultNotificationShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultNotificationShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the notificationList where creationDate equals to UPDATED_CREATION_DATE
        defaultNotificationShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where creationDate is not null
        defaultNotificationShouldBeFound("creationDate.specified=true");

        // Get all the notificationList where creationDate is null
        defaultNotificationShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationDate equals to DEFAULT_NOTIFICATION_DATE
        defaultNotificationShouldBeFound("notificationDate.equals=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the notificationList where notificationDate equals to UPDATED_NOTIFICATION_DATE
        defaultNotificationShouldNotBeFound("notificationDate.equals=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationDate not equals to DEFAULT_NOTIFICATION_DATE
        defaultNotificationShouldNotBeFound("notificationDate.notEquals=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the notificationList where notificationDate not equals to UPDATED_NOTIFICATION_DATE
        defaultNotificationShouldBeFound("notificationDate.notEquals=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationDate in DEFAULT_NOTIFICATION_DATE or UPDATED_NOTIFICATION_DATE
        defaultNotificationShouldBeFound("notificationDate.in=" + DEFAULT_NOTIFICATION_DATE + "," + UPDATED_NOTIFICATION_DATE);

        // Get all the notificationList where notificationDate equals to UPDATED_NOTIFICATION_DATE
        defaultNotificationShouldNotBeFound("notificationDate.in=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationDate is not null
        defaultNotificationShouldBeFound("notificationDate.specified=true");

        // Get all the notificationList where notificationDate is null
        defaultNotificationShouldNotBeFound("notificationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationReason equals to DEFAULT_NOTIFICATION_REASON
        defaultNotificationShouldBeFound("notificationReason.equals=" + DEFAULT_NOTIFICATION_REASON);

        // Get all the notificationList where notificationReason equals to UPDATED_NOTIFICATION_REASON
        defaultNotificationShouldNotBeFound("notificationReason.equals=" + UPDATED_NOTIFICATION_REASON);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationReasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationReason not equals to DEFAULT_NOTIFICATION_REASON
        defaultNotificationShouldNotBeFound("notificationReason.notEquals=" + DEFAULT_NOTIFICATION_REASON);

        // Get all the notificationList where notificationReason not equals to UPDATED_NOTIFICATION_REASON
        defaultNotificationShouldBeFound("notificationReason.notEquals=" + UPDATED_NOTIFICATION_REASON);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationReasonIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationReason in DEFAULT_NOTIFICATION_REASON or UPDATED_NOTIFICATION_REASON
        defaultNotificationShouldBeFound("notificationReason.in=" + DEFAULT_NOTIFICATION_REASON + "," + UPDATED_NOTIFICATION_REASON);

        // Get all the notificationList where notificationReason equals to UPDATED_NOTIFICATION_REASON
        defaultNotificationShouldNotBeFound("notificationReason.in=" + UPDATED_NOTIFICATION_REASON);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationReason is not null
        defaultNotificationShouldBeFound("notificationReason.specified=true");

        // Get all the notificationList where notificationReason is null
        defaultNotificationShouldNotBeFound("notificationReason.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText equals to DEFAULT_NOTIFICATION_TEXT
        defaultNotificationShouldBeFound("notificationText.equals=" + DEFAULT_NOTIFICATION_TEXT);

        // Get all the notificationList where notificationText equals to UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldNotBeFound("notificationText.equals=" + UPDATED_NOTIFICATION_TEXT);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText not equals to DEFAULT_NOTIFICATION_TEXT
        defaultNotificationShouldNotBeFound("notificationText.notEquals=" + DEFAULT_NOTIFICATION_TEXT);

        // Get all the notificationList where notificationText not equals to UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldBeFound("notificationText.notEquals=" + UPDATED_NOTIFICATION_TEXT);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText in DEFAULT_NOTIFICATION_TEXT or UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldBeFound("notificationText.in=" + DEFAULT_NOTIFICATION_TEXT + "," + UPDATED_NOTIFICATION_TEXT);

        // Get all the notificationList where notificationText equals to UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldNotBeFound("notificationText.in=" + UPDATED_NOTIFICATION_TEXT);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText is not null
        defaultNotificationShouldBeFound("notificationText.specified=true");

        // Get all the notificationList where notificationText is null
        defaultNotificationShouldNotBeFound("notificationText.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText contains DEFAULT_NOTIFICATION_TEXT
        defaultNotificationShouldBeFound("notificationText.contains=" + DEFAULT_NOTIFICATION_TEXT);

        // Get all the notificationList where notificationText contains UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldNotBeFound("notificationText.contains=" + UPDATED_NOTIFICATION_TEXT);
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTextNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationText does not contain DEFAULT_NOTIFICATION_TEXT
        defaultNotificationShouldNotBeFound("notificationText.doesNotContain=" + DEFAULT_NOTIFICATION_TEXT);

        // Get all the notificationList where notificationText does not contain UPDATED_NOTIFICATION_TEXT
        defaultNotificationShouldBeFound("notificationText.doesNotContain=" + UPDATED_NOTIFICATION_TEXT);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsDeliveredIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isDelivered equals to DEFAULT_IS_DELIVERED
        defaultNotificationShouldBeFound("isDelivered.equals=" + DEFAULT_IS_DELIVERED);

        // Get all the notificationList where isDelivered equals to UPDATED_IS_DELIVERED
        defaultNotificationShouldNotBeFound("isDelivered.equals=" + UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsDeliveredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isDelivered not equals to DEFAULT_IS_DELIVERED
        defaultNotificationShouldNotBeFound("isDelivered.notEquals=" + DEFAULT_IS_DELIVERED);

        // Get all the notificationList where isDelivered not equals to UPDATED_IS_DELIVERED
        defaultNotificationShouldBeFound("isDelivered.notEquals=" + UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsDeliveredIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isDelivered in DEFAULT_IS_DELIVERED or UPDATED_IS_DELIVERED
        defaultNotificationShouldBeFound("isDelivered.in=" + DEFAULT_IS_DELIVERED + "," + UPDATED_IS_DELIVERED);

        // Get all the notificationList where isDelivered equals to UPDATED_IS_DELIVERED
        defaultNotificationShouldNotBeFound("isDelivered.in=" + UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsDeliveredIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isDelivered is not null
        defaultNotificationShouldBeFound("isDelivered.specified=true");

        // Get all the notificationList where isDelivered is null
        defaultNotificationShouldNotBeFound("isDelivered.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        Appuser appuser = AppuserResourceIT.createEntity(em);
        em.persist(appuser);
        em.flush();
        notification.setAppuser(appuser);
        notificationRepository.saveAndFlush(notification);
        Long appuserId = appuser.getId();

        // Get all the notificationList where appuser equals to appuserId
        defaultNotificationShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the notificationList where appuser equals to (appuserId + 1)
        defaultNotificationShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationReason").value(hasItem(DEFAULT_NOTIFICATION_REASON.toString())))
            .andExpect(jsonPath("$.[*].notificationText").value(hasItem(DEFAULT_NOTIFICATION_TEXT)))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));

        // Check, that the count call also returns 1
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .creationDate(UPDATED_CREATION_DATE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .notificationReason(UPDATED_NOTIFICATION_REASON)
            .notificationText(UPDATED_NOTIFICATION_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(UPDATED_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(UPDATED_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(UPDATED_NOTIFICATION_TEXT);
        assertThat(testNotification.getIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    void putNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(DEFAULT_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(DEFAULT_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(DEFAULT_NOTIFICATION_TEXT);
        assertThat(testNotification.getIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .creationDate(UPDATED_CREATION_DATE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .notificationReason(UPDATED_NOTIFICATION_REASON)
            .notificationText(UPDATED_NOTIFICATION_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(UPDATED_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(UPDATED_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(UPDATED_NOTIFICATION_TEXT);
        assertThat(testNotification.getIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    void patchNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notification.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
