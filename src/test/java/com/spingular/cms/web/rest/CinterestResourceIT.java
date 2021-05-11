package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Cinterest;
import com.spingular.cms.domain.Community;
import com.spingular.cms.repository.CinterestRepository;
import com.spingular.cms.service.CinterestService;
import com.spingular.cms.service.criteria.CinterestCriteria;
import com.spingular.cms.service.dto.CinterestDTO;
import com.spingular.cms.service.mapper.CinterestMapper;
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
 * Integration tests for the {@link CinterestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CinterestResourceIT {

    private static final String DEFAULT_INTEREST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cinterests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CinterestRepository cinterestRepository;

    @Mock
    private CinterestRepository cinterestRepositoryMock;

    @Autowired
    private CinterestMapper cinterestMapper;

    @Mock
    private CinterestService cinterestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCinterestMockMvc;

    private Cinterest cinterest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinterest createEntity(EntityManager em) {
        Cinterest cinterest = new Cinterest().interestName(DEFAULT_INTEREST_NAME);
        return cinterest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinterest createUpdatedEntity(EntityManager em) {
        Cinterest cinterest = new Cinterest().interestName(UPDATED_INTEREST_NAME);
        return cinterest;
    }

    @BeforeEach
    public void initTest() {
        cinterest = createEntity(em);
    }

    @Test
    @Transactional
    void createCinterest() throws Exception {
        int databaseSizeBeforeCreate = cinterestRepository.findAll().size();
        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);
        restCinterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeCreate + 1);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(DEFAULT_INTEREST_NAME);
    }

    @Test
    @Transactional
    void createCinterestWithExistingId() throws Exception {
        // Create the Cinterest with an existing ID
        cinterest.setId(1L);
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        int databaseSizeBeforeCreate = cinterestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCinterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInterestNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cinterestRepository.findAll().size();
        // set the field null
        cinterest.setInterestName(null);

        // Create the Cinterest, which fails.
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        restCinterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCinterests() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cinterest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestName").value(hasItem(DEFAULT_INTEREST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCinterestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cinterestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCinterestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cinterestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCinterestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cinterestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCinterestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cinterestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get the cinterest
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL_ID, cinterest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cinterest.getId().intValue()))
            .andExpect(jsonPath("$.interestName").value(DEFAULT_INTEREST_NAME));
    }

    @Test
    @Transactional
    void getCinterestsByIdFiltering() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        Long id = cinterest.getId();

        defaultCinterestShouldBeFound("id.equals=" + id);
        defaultCinterestShouldNotBeFound("id.notEquals=" + id);

        defaultCinterestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCinterestShouldNotBeFound("id.greaterThan=" + id);

        defaultCinterestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCinterestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName equals to DEFAULT_INTEREST_NAME
        defaultCinterestShouldBeFound("interestName.equals=" + DEFAULT_INTEREST_NAME);

        // Get all the cinterestList where interestName equals to UPDATED_INTEREST_NAME
        defaultCinterestShouldNotBeFound("interestName.equals=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName not equals to DEFAULT_INTEREST_NAME
        defaultCinterestShouldNotBeFound("interestName.notEquals=" + DEFAULT_INTEREST_NAME);

        // Get all the cinterestList where interestName not equals to UPDATED_INTEREST_NAME
        defaultCinterestShouldBeFound("interestName.notEquals=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameIsInShouldWork() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName in DEFAULT_INTEREST_NAME or UPDATED_INTEREST_NAME
        defaultCinterestShouldBeFound("interestName.in=" + DEFAULT_INTEREST_NAME + "," + UPDATED_INTEREST_NAME);

        // Get all the cinterestList where interestName equals to UPDATED_INTEREST_NAME
        defaultCinterestShouldNotBeFound("interestName.in=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName is not null
        defaultCinterestShouldBeFound("interestName.specified=true");

        // Get all the cinterestList where interestName is null
        defaultCinterestShouldNotBeFound("interestName.specified=false");
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameContainsSomething() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName contains DEFAULT_INTEREST_NAME
        defaultCinterestShouldBeFound("interestName.contains=" + DEFAULT_INTEREST_NAME);

        // Get all the cinterestList where interestName contains UPDATED_INTEREST_NAME
        defaultCinterestShouldNotBeFound("interestName.contains=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllCinterestsByInterestNameNotContainsSomething() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList where interestName does not contain DEFAULT_INTEREST_NAME
        defaultCinterestShouldNotBeFound("interestName.doesNotContain=" + DEFAULT_INTEREST_NAME);

        // Get all the cinterestList where interestName does not contain UPDATED_INTEREST_NAME
        defaultCinterestShouldBeFound("interestName.doesNotContain=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllCinterestsByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        cinterest.addCommunity(community);
        cinterestRepository.saveAndFlush(cinterest);
        Long communityId = community.getId();

        // Get all the cinterestList where community equals to communityId
        defaultCinterestShouldBeFound("communityId.equals=" + communityId);

        // Get all the cinterestList where community equals to (communityId + 1)
        defaultCinterestShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCinterestShouldBeFound(String filter) throws Exception {
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cinterest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestName").value(hasItem(DEFAULT_INTEREST_NAME)));

        // Check, that the count call also returns 1
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCinterestShouldNotBeFound(String filter) throws Exception {
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCinterestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCinterest() throws Exception {
        // Get the cinterest
        restCinterestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();

        // Update the cinterest
        Cinterest updatedCinterest = cinterestRepository.findById(cinterest.getId()).get();
        // Disconnect from session so that the updates on updatedCinterest are not directly saved in db
        em.detach(updatedCinterest);
        updatedCinterest.interestName(UPDATED_INTEREST_NAME);
        CinterestDTO cinterestDTO = cinterestMapper.toDto(updatedCinterest);

        restCinterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cinterestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cinterestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCinterestWithPatch() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();

        // Update the cinterest using partial update
        Cinterest partialUpdatedCinterest = new Cinterest();
        partialUpdatedCinterest.setId(cinterest.getId());

        partialUpdatedCinterest.interestName(UPDATED_INTEREST_NAME);

        restCinterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCinterest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCinterest))
            )
            .andExpect(status().isOk());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCinterestWithPatch() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();

        // Update the cinterest using partial update
        Cinterest partialUpdatedCinterest = new Cinterest();
        partialUpdatedCinterest.setId(cinterest.getId());

        partialUpdatedCinterest.interestName(UPDATED_INTEREST_NAME);

        restCinterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCinterest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCinterest))
            )
            .andExpect(status().isOk());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cinterestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();
        cinterest.setId(count.incrementAndGet());

        // Create the Cinterest
        CinterestDTO cinterestDTO = cinterestMapper.toDto(cinterest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinterestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinterestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeDelete = cinterestRepository.findAll().size();

        // Delete the cinterest
        restCinterestMockMvc
            .perform(delete(ENTITY_API_URL_ID, cinterest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
