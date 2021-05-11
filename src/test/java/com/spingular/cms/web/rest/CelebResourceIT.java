package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Celeb;
import com.spingular.cms.repository.CelebRepository;
import com.spingular.cms.service.CelebService;
import com.spingular.cms.service.criteria.CelebCriteria;
import com.spingular.cms.service.dto.CelebDTO;
import com.spingular.cms.service.mapper.CelebMapper;
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
 * Integration tests for the {@link CelebResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CelebResourceIT {

    private static final String DEFAULT_CELEB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CELEB_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/celebs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CelebRepository celebRepository;

    @Mock
    private CelebRepository celebRepositoryMock;

    @Autowired
    private CelebMapper celebMapper;

    @Mock
    private CelebService celebServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCelebMockMvc;

    private Celeb celeb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Celeb createEntity(EntityManager em) {
        Celeb celeb = new Celeb().celebName(DEFAULT_CELEB_NAME);
        return celeb;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Celeb createUpdatedEntity(EntityManager em) {
        Celeb celeb = new Celeb().celebName(UPDATED_CELEB_NAME);
        return celeb;
    }

    @BeforeEach
    public void initTest() {
        celeb = createEntity(em);
    }

    @Test
    @Transactional
    void createCeleb() throws Exception {
        int databaseSizeBeforeCreate = celebRepository.findAll().size();
        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);
        restCelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeCreate + 1);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(DEFAULT_CELEB_NAME);
    }

    @Test
    @Transactional
    void createCelebWithExistingId() throws Exception {
        // Create the Celeb with an existing ID
        celeb.setId(1L);
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        int databaseSizeBeforeCreate = celebRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCelebNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = celebRepository.findAll().size();
        // set the field null
        celeb.setCelebName(null);

        // Create the Celeb, which fails.
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        restCelebMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCelebs() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList
        restCelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(celeb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCelebsWithEagerRelationshipsIsEnabled() throws Exception {
        when(celebServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCelebMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(celebServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCelebsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(celebServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCelebMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(celebServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get the celeb
        restCelebMockMvc
            .perform(get(ENTITY_API_URL_ID, celeb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(celeb.getId().intValue()))
            .andExpect(jsonPath("$.celebName").value(DEFAULT_CELEB_NAME));
    }

    @Test
    @Transactional
    void getCelebsByIdFiltering() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        Long id = celeb.getId();

        defaultCelebShouldBeFound("id.equals=" + id);
        defaultCelebShouldNotBeFound("id.notEquals=" + id);

        defaultCelebShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCelebShouldNotBeFound("id.greaterThan=" + id);

        defaultCelebShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCelebShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameIsEqualToSomething() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName equals to DEFAULT_CELEB_NAME
        defaultCelebShouldBeFound("celebName.equals=" + DEFAULT_CELEB_NAME);

        // Get all the celebList where celebName equals to UPDATED_CELEB_NAME
        defaultCelebShouldNotBeFound("celebName.equals=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName not equals to DEFAULT_CELEB_NAME
        defaultCelebShouldNotBeFound("celebName.notEquals=" + DEFAULT_CELEB_NAME);

        // Get all the celebList where celebName not equals to UPDATED_CELEB_NAME
        defaultCelebShouldBeFound("celebName.notEquals=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameIsInShouldWork() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName in DEFAULT_CELEB_NAME or UPDATED_CELEB_NAME
        defaultCelebShouldBeFound("celebName.in=" + DEFAULT_CELEB_NAME + "," + UPDATED_CELEB_NAME);

        // Get all the celebList where celebName equals to UPDATED_CELEB_NAME
        defaultCelebShouldNotBeFound("celebName.in=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName is not null
        defaultCelebShouldBeFound("celebName.specified=true");

        // Get all the celebList where celebName is null
        defaultCelebShouldNotBeFound("celebName.specified=false");
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameContainsSomething() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName contains DEFAULT_CELEB_NAME
        defaultCelebShouldBeFound("celebName.contains=" + DEFAULT_CELEB_NAME);

        // Get all the celebList where celebName contains UPDATED_CELEB_NAME
        defaultCelebShouldNotBeFound("celebName.contains=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCelebsByCelebNameNotContainsSomething() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList where celebName does not contain DEFAULT_CELEB_NAME
        defaultCelebShouldNotBeFound("celebName.doesNotContain=" + DEFAULT_CELEB_NAME);

        // Get all the celebList where celebName does not contain UPDATED_CELEB_NAME
        defaultCelebShouldBeFound("celebName.doesNotContain=" + UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void getAllCelebsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);
        Appuser appuser = AppuserResourceIT.createEntity(em);
        em.persist(appuser);
        em.flush();
        celeb.addAppuser(appuser);
        celebRepository.saveAndFlush(celeb);
        Long appuserId = appuser.getId();

        // Get all the celebList where appuser equals to appuserId
        defaultCelebShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the celebList where appuser equals to (appuserId + 1)
        defaultCelebShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCelebShouldBeFound(String filter) throws Exception {
        restCelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(celeb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));

        // Check, that the count call also returns 1
        restCelebMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCelebShouldNotBeFound(String filter) throws Exception {
        restCelebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCelebMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCeleb() throws Exception {
        // Get the celeb
        restCelebMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeUpdate = celebRepository.findAll().size();

        // Update the celeb
        Celeb updatedCeleb = celebRepository.findById(celeb.getId()).get();
        // Disconnect from session so that the updates on updatedCeleb are not directly saved in db
        em.detach(updatedCeleb);
        updatedCeleb.celebName(UPDATED_CELEB_NAME);
        CelebDTO celebDTO = celebMapper.toDto(updatedCeleb);

        restCelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, celebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isOk());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, celebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCelebWithPatch() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeUpdate = celebRepository.findAll().size();

        // Update the celeb using partial update
        Celeb partialUpdatedCeleb = new Celeb();
        partialUpdatedCeleb.setId(celeb.getId());

        partialUpdatedCeleb.celebName(UPDATED_CELEB_NAME);

        restCelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeleb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeleb))
            )
            .andExpect(status().isOk());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCelebWithPatch() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeUpdate = celebRepository.findAll().size();

        // Update the celeb using partial update
        Celeb partialUpdatedCeleb = new Celeb();
        partialUpdatedCeleb.setId(celeb.getId());

        partialUpdatedCeleb.celebName(UPDATED_CELEB_NAME);

        restCelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeleb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeleb))
            )
            .andExpect(status().isOk());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, celebDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();
        celeb.setId(count.incrementAndGet());

        // Create the Celeb
        CelebDTO celebDTO = celebMapper.toDto(celeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelebMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(celebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeDelete = celebRepository.findAll().size();

        // Delete the celeb
        restCelebMockMvc
            .perform(delete(ENTITY_API_URL_ID, celeb.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
