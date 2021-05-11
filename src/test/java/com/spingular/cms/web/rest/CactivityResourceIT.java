package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Cactivity;
import com.spingular.cms.domain.Community;
import com.spingular.cms.repository.CactivityRepository;
import com.spingular.cms.service.CactivityService;
import com.spingular.cms.service.criteria.CactivityCriteria;
import com.spingular.cms.service.dto.CactivityDTO;
import com.spingular.cms.service.mapper.CactivityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CactivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CactivityResourceIT {

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cactivities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CactivityRepository cactivityRepository;

    @Mock
    private CactivityRepository cactivityRepositoryMock;

    @Autowired
    private CactivityMapper cactivityMapper;

    @Mock
    private CactivityService cactivityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCactivityMockMvc;

    private Cactivity cactivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cactivity createEntity(EntityManager em) {
        Cactivity cactivity = new Cactivity().activityName(DEFAULT_ACTIVITY_NAME);
        return cactivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cactivity createUpdatedEntity(EntityManager em) {
        Cactivity cactivity = new Cactivity().activityName(UPDATED_ACTIVITY_NAME);
        return cactivity;
    }

    @BeforeEach
    public void initTest() {
        cactivity = createEntity(em);
    }

    @Test
    @Transactional
    void createCactivity() throws Exception {
        int databaseSizeBeforeCreate = cactivityRepository.findAll().size();
        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);
        restCactivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeCreate + 1);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(DEFAULT_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void createCactivityWithExistingId() throws Exception {
        // Create the Cactivity with an existing ID
        cactivity.setId(1L);
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        int databaseSizeBeforeCreate = cactivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCactivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActivityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cactivityRepository.findAll().size();
        // set the field null
        cactivity.setActivityName(null);

        // Create the Cactivity, which fails.
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        restCactivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCactivities() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cactivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCactivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cactivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCactivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cactivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCactivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cactivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCactivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cactivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get the cactivity
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL_ID, cactivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cactivity.getId().intValue()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME));
    }

    @Test
    @Transactional
    void getCactivitiesByIdFiltering() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        Long id = cactivity.getId();

        defaultCactivityShouldBeFound("id.equals=" + id);
        defaultCactivityShouldNotBeFound("id.notEquals=" + id);

        defaultCactivityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCactivityShouldNotBeFound("id.greaterThan=" + id);

        defaultCactivityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCactivityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName equals to DEFAULT_ACTIVITY_NAME
        defaultCactivityShouldBeFound("activityName.equals=" + DEFAULT_ACTIVITY_NAME);

        // Get all the cactivityList where activityName equals to UPDATED_ACTIVITY_NAME
        defaultCactivityShouldNotBeFound("activityName.equals=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName not equals to DEFAULT_ACTIVITY_NAME
        defaultCactivityShouldNotBeFound("activityName.notEquals=" + DEFAULT_ACTIVITY_NAME);

        // Get all the cactivityList where activityName not equals to UPDATED_ACTIVITY_NAME
        defaultCactivityShouldBeFound("activityName.notEquals=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameIsInShouldWork() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName in DEFAULT_ACTIVITY_NAME or UPDATED_ACTIVITY_NAME
        defaultCactivityShouldBeFound("activityName.in=" + DEFAULT_ACTIVITY_NAME + "," + UPDATED_ACTIVITY_NAME);

        // Get all the cactivityList where activityName equals to UPDATED_ACTIVITY_NAME
        defaultCactivityShouldNotBeFound("activityName.in=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName is not null
        defaultCactivityShouldBeFound("activityName.specified=true");

        // Get all the cactivityList where activityName is null
        defaultCactivityShouldNotBeFound("activityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameContainsSomething() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName contains DEFAULT_ACTIVITY_NAME
        defaultCactivityShouldBeFound("activityName.contains=" + DEFAULT_ACTIVITY_NAME);

        // Get all the cactivityList where activityName contains UPDATED_ACTIVITY_NAME
        defaultCactivityShouldNotBeFound("activityName.contains=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllCactivitiesByActivityNameNotContainsSomething() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList where activityName does not contain DEFAULT_ACTIVITY_NAME
        defaultCactivityShouldNotBeFound("activityName.doesNotContain=" + DEFAULT_ACTIVITY_NAME);

        // Get all the cactivityList where activityName does not contain UPDATED_ACTIVITY_NAME
        defaultCactivityShouldBeFound("activityName.doesNotContain=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllCactivitiesByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        cactivity.addCommunity(community);
        cactivityRepository.saveAndFlush(cactivity);
        Long communityId = community.getId();

        // Get all the cactivityList where community equals to communityId
        defaultCactivityShouldBeFound("communityId.equals=" + communityId);

        // Get all the cactivityList where community equals to (communityId + 1)
        defaultCactivityShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCactivityShouldBeFound(String filter) throws Exception {
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cactivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)));

        // Check, that the count call also returns 1
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCactivityShouldNotBeFound(String filter) throws Exception {
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCactivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCactivity() throws Exception {
        // Get the cactivity
        restCactivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();

        // Update the cactivity
        Cactivity updatedCactivity = cactivityRepository.findById(cactivity.getId()).get();
        // Disconnect from session so that the updates on updatedCactivity are not directly saved in db
        em.detach(updatedCactivity);
        updatedCactivity.activityName(UPDATED_ACTIVITY_NAME);
        CactivityDTO cactivityDTO = cactivityMapper.toDto(updatedCactivity);

        restCactivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cactivityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cactivityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCactivityWithPatch() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();

        // Update the cactivity using partial update
        Cactivity partialUpdatedCactivity = new Cactivity();
        partialUpdatedCactivity.setId(cactivity.getId());

        restCactivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCactivity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCactivity))
            )
            .andExpect(status().isOk());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(DEFAULT_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCactivityWithPatch() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();

        // Update the cactivity using partial update
        Cactivity partialUpdatedCactivity = new Cactivity();
        partialUpdatedCactivity.setId(cactivity.getId());

        partialUpdatedCactivity.activityName(UPDATED_ACTIVITY_NAME);

        restCactivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCactivity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCactivity))
            )
            .andExpect(status().isOk());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cactivityDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();
        cactivity.setId(count.incrementAndGet());

        // Create the Cactivity
        CactivityDTO cactivityDTO = cactivityMapper.toDto(cactivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCactivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cactivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeDelete = cactivityRepository.findAll().size();

        // Delete the cactivity
        restCactivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, cactivity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
