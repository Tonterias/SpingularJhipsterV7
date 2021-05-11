package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Interest;
import com.spingular.cms.repository.InterestRepository;
import com.spingular.cms.service.InterestService;
import com.spingular.cms.service.criteria.InterestCriteria;
import com.spingular.cms.service.dto.InterestDTO;
import com.spingular.cms.service.mapper.InterestMapper;
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
 * Integration tests for the {@link InterestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterestResourceIT {

    private static final String DEFAULT_INTEREST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterestRepository interestRepository;

    @Mock
    private InterestRepository interestRepositoryMock;

    @Autowired
    private InterestMapper interestMapper;

    @Mock
    private InterestService interestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterestMockMvc;

    private Interest interest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interest createEntity(EntityManager em) {
        Interest interest = new Interest().interestName(DEFAULT_INTEREST_NAME);
        return interest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interest createUpdatedEntity(EntityManager em) {
        Interest interest = new Interest().interestName(UPDATED_INTEREST_NAME);
        return interest;
    }

    @BeforeEach
    public void initTest() {
        interest = createEntity(em);
    }

    @Test
    @Transactional
    void createInterest() throws Exception {
        int databaseSizeBeforeCreate = interestRepository.findAll().size();
        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);
        restInterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeCreate + 1);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getInterestName()).isEqualTo(DEFAULT_INTEREST_NAME);
    }

    @Test
    @Transactional
    void createInterestWithExistingId() throws Exception {
        // Create the Interest with an existing ID
        interest.setId(1L);
        InterestDTO interestDTO = interestMapper.toDto(interest);

        int databaseSizeBeforeCreate = interestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInterestNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interestRepository.findAll().size();
        // set the field null
        interest.setInterestName(null);

        // Create the Interest, which fails.
        InterestDTO interestDTO = interestMapper.toDto(interest);

        restInterestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterests() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList
        restInterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestName").value(hasItem(DEFAULT_INTEREST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(interestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(interestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(interestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(interestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get the interest
        restInterestMockMvc
            .perform(get(ENTITY_API_URL_ID, interest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interest.getId().intValue()))
            .andExpect(jsonPath("$.interestName").value(DEFAULT_INTEREST_NAME));
    }

    @Test
    @Transactional
    void getInterestsByIdFiltering() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        Long id = interest.getId();

        defaultInterestShouldBeFound("id.equals=" + id);
        defaultInterestShouldNotBeFound("id.notEquals=" + id);

        defaultInterestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterestShouldNotBeFound("id.greaterThan=" + id);

        defaultInterestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameIsEqualToSomething() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName equals to DEFAULT_INTEREST_NAME
        defaultInterestShouldBeFound("interestName.equals=" + DEFAULT_INTEREST_NAME);

        // Get all the interestList where interestName equals to UPDATED_INTEREST_NAME
        defaultInterestShouldNotBeFound("interestName.equals=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName not equals to DEFAULT_INTEREST_NAME
        defaultInterestShouldNotBeFound("interestName.notEquals=" + DEFAULT_INTEREST_NAME);

        // Get all the interestList where interestName not equals to UPDATED_INTEREST_NAME
        defaultInterestShouldBeFound("interestName.notEquals=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameIsInShouldWork() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName in DEFAULT_INTEREST_NAME or UPDATED_INTEREST_NAME
        defaultInterestShouldBeFound("interestName.in=" + DEFAULT_INTEREST_NAME + "," + UPDATED_INTEREST_NAME);

        // Get all the interestList where interestName equals to UPDATED_INTEREST_NAME
        defaultInterestShouldNotBeFound("interestName.in=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName is not null
        defaultInterestShouldBeFound("interestName.specified=true");

        // Get all the interestList where interestName is null
        defaultInterestShouldNotBeFound("interestName.specified=false");
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameContainsSomething() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName contains DEFAULT_INTEREST_NAME
        defaultInterestShouldBeFound("interestName.contains=" + DEFAULT_INTEREST_NAME);

        // Get all the interestList where interestName contains UPDATED_INTEREST_NAME
        defaultInterestShouldNotBeFound("interestName.contains=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllInterestsByInterestNameNotContainsSomething() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList where interestName does not contain DEFAULT_INTEREST_NAME
        defaultInterestShouldNotBeFound("interestName.doesNotContain=" + DEFAULT_INTEREST_NAME);

        // Get all the interestList where interestName does not contain UPDATED_INTEREST_NAME
        defaultInterestShouldBeFound("interestName.doesNotContain=" + UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void getAllInterestsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);
        Appuser appuser = AppuserResourceIT.createEntity(em);
        em.persist(appuser);
        em.flush();
        interest.addAppuser(appuser);
        interestRepository.saveAndFlush(interest);
        Long appuserId = appuser.getId();

        // Get all the interestList where appuser equals to appuserId
        defaultInterestShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the interestList where appuser equals to (appuserId + 1)
        defaultInterestShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterestShouldBeFound(String filter) throws Exception {
        restInterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestName").value(hasItem(DEFAULT_INTEREST_NAME)));

        // Check, that the count call also returns 1
        restInterestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterestShouldNotBeFound(String filter) throws Exception {
        restInterestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterest() throws Exception {
        // Get the interest
        restInterestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Update the interest
        Interest updatedInterest = interestRepository.findById(interest.getId()).get();
        // Disconnect from session so that the updates on updatedInterest are not directly saved in db
        em.detach(updatedInterest);
        updatedInterest.interestName(UPDATED_INTEREST_NAME);
        InterestDTO interestDTO = interestMapper.toDto(updatedInterest);

        restInterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterestWithPatch() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Update the interest using partial update
        Interest partialUpdatedInterest = new Interest();
        partialUpdatedInterest.setId(interest.getId());

        restInterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterest))
            )
            .andExpect(status().isOk());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getInterestName()).isEqualTo(DEFAULT_INTEREST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateInterestWithPatch() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Update the interest using partial update
        Interest partialUpdatedInterest = new Interest();
        partialUpdatedInterest.setId(interest.getId());

        partialUpdatedInterest.interestName(UPDATED_INTEREST_NAME);

        restInterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterest))
            )
            .andExpect(status().isOk());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();
        interest.setId(count.incrementAndGet());

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeDelete = interestRepository.findAll().size();

        // Delete the interest
        restInterestMockMvc
            .perform(delete(ENTITY_API_URL_ID, interest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
