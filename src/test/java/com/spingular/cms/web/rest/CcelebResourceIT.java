package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Cceleb;
import com.spingular.cms.domain.Community;
import com.spingular.cms.repository.CcelebRepository;
import com.spingular.cms.service.CcelebService;
import com.spingular.cms.service.criteria.CcelebCriteria;
import com.spingular.cms.service.dto.CcelebDTO;
import com.spingular.cms.service.mapper.CcelebMapper;
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
 * Integration tests for the {@link CcelebResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CcelebResourceIT {

    private static final String DEFAULT_CELEB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CELEB_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ccelebs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CcelebRepository ccelebRepository;

    @Mock
    private CcelebRepository ccelebRepositoryMock;

    @Autowired
    private CcelebMapper ccelebMapper;

    @Mock
    private CcelebService ccelebServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCcelebMockMvc;

    private Cceleb cceleb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cceleb createEntity(EntityManager em) {
        Cceleb cceleb = new Cceleb().celebName(DEFAULT_CELEB_NAME);
        return cceleb;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cceleb createUpdatedEntity(EntityManager em) {
        Cceleb cceleb = new Cceleb().celebName(UPDATED_CELEB_NAME);
        return cceleb;
    }

    @BeforeEach
    public void initTest() {
        cceleb = createEntity(em);
    }

    @Test
    @Transactional
    void createCceleb() throws Exception {
        int databaseSizeBeforeCreate = ccelebRepository.findAll().size();
        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);
        restCcelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeCreate + 1);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(DEFAULT_CELEB_NAME);
    }

    @Test
    @Transactional
    void createCcelebWithExistingId() throws Exception {
        // Create the Cceleb with an existing ID
        cceleb.setId(1L);
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        int databaseSizeBeforeCreate = ccelebRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCcelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCelebNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ccelebRepository.findAll().size();
        // set the field null
        cceleb.setCelebName(null);

        // Create the Cceleb, which fails.
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        restCcelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCcelebs() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cceleb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCcelebsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ccelebServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCcelebMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ccelebServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCcelebsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ccelebServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCcelebMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ccelebServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get the cceleb
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL_ID, cceleb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cceleb.getId().intValue()))
            .andExpect(jsonPath("$.celebName").value(DEFAULT_CELEB_NAME));
    }

    @Test
    @Transactional
    void getCcelebsByIdFiltering() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        Long id = cceleb.getId();

        defaultCcelebShouldBeFound("id.equals=" + id);
        defaultCcelebShouldNotBeFound("id.notEquals=" + id);

        defaultCcelebShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCcelebShouldNotBeFound("id.greaterThan=" + id);

        defaultCcelebShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCcelebShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName equals to DEFAULT_CELEB_NAME
        defaultCcelebShouldBeFound("celebName.equals=" + DEFAULT_CELEB_NAME);

        // Get all the ccelebList where celebName equals to UPDATED_CELEB_NAME
        defaultCcelebShouldNotBeFound("celebName.equals=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName not equals to DEFAULT_CELEB_NAME
        defaultCcelebShouldNotBeFound("celebName.notEquals=" + DEFAULT_CELEB_NAME);

        // Get all the ccelebList where celebName not equals to UPDATED_CELEB_NAME
        defaultCcelebShouldBeFound("celebName.notEquals=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameIsInShouldWork() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName in DEFAULT_CELEB_NAME or UPDATED_CELEB_NAME
        defaultCcelebShouldBeFound("celebName.in=" + DEFAULT_CELEB_NAME + "," + UPDATED_CELEB_NAME);

        // Get all the ccelebList where celebName equals to UPDATED_CELEB_NAME
        defaultCcelebShouldNotBeFound("celebName.in=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName is not null
        defaultCcelebShouldBeFound("celebName.specified=true");

        // Get all the ccelebList where celebName is null
        defaultCcelebShouldNotBeFound("celebName.specified=false");
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameContainsSomething() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName contains DEFAULT_CELEB_NAME
        defaultCcelebShouldBeFound("celebName.contains=" + DEFAULT_CELEB_NAME);

        // Get all the ccelebList where celebName contains UPDATED_CELEB_NAME
        defaultCcelebShouldNotBeFound("celebName.contains=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCcelebsByCelebNameNotContainsSomething() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList where celebName does not contain DEFAULT_CELEB_NAME
        defaultCcelebShouldNotBeFound("celebName.doesNotContain=" + DEFAULT_CELEB_NAME);

        // Get all the ccelebList where celebName does not contain UPDATED_CELEB_NAME
        defaultCcelebShouldBeFound("celebName.doesNotContain=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCcelebsByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        cceleb.addCommunity(community);
        ccelebRepository.saveAndFlush(cceleb);
        Long communityId = community.getId();

        // Get all the ccelebList where community equals to communityId
        defaultCcelebShouldBeFound("communityId.equals=" + communityId);

        // Get all the ccelebList where community equals to (communityId + 1)
        defaultCcelebShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCcelebShouldBeFound(String filter) throws Exception {
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cceleb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));

        // Check, that the count call also returns 1
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCcelebShouldNotBeFound(String filter) throws Exception {
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCcelebMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCceleb() throws Exception {
        // Get the cceleb
        restCcelebMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();

        // Update the cceleb
        Cceleb updatedCceleb = ccelebRepository.findById(cceleb.getId()).get();
        // Disconnect from session so that the updates on updatedCceleb are not directly saved in db
        em.detach(updatedCceleb);
        updatedCceleb.celebName(UPDATED_CELEB_NAME);
        CcelebDTO ccelebDTO = ccelebMapper.toDto(updatedCceleb);

        restCcelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ccelebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ccelebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCcelebWithPatch() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();

        // Update the cceleb using partial update
        Cceleb partialUpdatedCceleb = new Cceleb();
        partialUpdatedCceleb.setId(cceleb.getId());

        partialUpdatedCceleb.celebName(UPDATED_CELEB_NAME);

        restCcelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCceleb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCceleb))
            )
            .andExpect(status().isOk());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCcelebWithPatch() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();

        // Update the cceleb using partial update
        Cceleb partialUpdatedCceleb = new Cceleb();
        partialUpdatedCceleb.setId(cceleb.getId());

        partialUpdatedCceleb.celebName(UPDATED_CELEB_NAME);

        restCcelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCceleb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCceleb))
            )
            .andExpect(status().isOk());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ccelebDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();
        cceleb.setId(count.incrementAndGet());

        // Create the Cceleb
        CcelebDTO ccelebDTO = ccelebMapper.toDto(cceleb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCcelebMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ccelebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeDelete = ccelebRepository.findAll().size();

        // Delete the cceleb
        restCcelebMockMvc
            .perform(delete(ENTITY_API_URL_ID, cceleb.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
