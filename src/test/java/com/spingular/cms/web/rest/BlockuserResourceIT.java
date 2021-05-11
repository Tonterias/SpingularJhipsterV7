package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Blockuser;
import com.spingular.cms.domain.Community;
import com.spingular.cms.repository.BlockuserRepository;
import com.spingular.cms.service.criteria.BlockuserCriteria;
import com.spingular.cms.service.dto.BlockuserDTO;
import com.spingular.cms.service.mapper.BlockuserMapper;
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
 * Integration tests for the {@link BlockuserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlockuserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/blockusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlockuserRepository blockuserRepository;

    @Autowired
    private BlockuserMapper blockuserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockuserMockMvc;

    private Blockuser blockuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blockuser createEntity(EntityManager em) {
        Blockuser blockuser = new Blockuser().creationDate(DEFAULT_CREATION_DATE);
        return blockuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blockuser createUpdatedEntity(EntityManager em) {
        Blockuser blockuser = new Blockuser().creationDate(UPDATED_CREATION_DATE);
        return blockuser;
    }

    @BeforeEach
    public void initTest() {
        blockuser = createEntity(em);
    }

    @Test
    @Transactional
    void createBlockuser() throws Exception {
        int databaseSizeBeforeCreate = blockuserRepository.findAll().size();
        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);
        restBlockuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeCreate + 1);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createBlockuserWithExistingId() throws Exception {
        // Create the Blockuser with an existing ID
        blockuser.setId(1L);
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        int databaseSizeBeforeCreate = blockuserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBlockusers() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get the blockuser
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL_ID, blockuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blockuser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getBlockusersByIdFiltering() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        Long id = blockuser.getId();

        defaultBlockuserShouldBeFound("id.equals=" + id);
        defaultBlockuserShouldNotBeFound("id.notEquals=" + id);

        defaultBlockuserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBlockuserShouldNotBeFound("id.greaterThan=" + id);

        defaultBlockuserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBlockuserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBlockusersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultBlockuserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the blockuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultBlockuserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlockusersByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultBlockuserShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the blockuserList where creationDate not equals to UPDATED_CREATION_DATE
        defaultBlockuserShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlockusersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultBlockuserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the blockuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultBlockuserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlockusersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList where creationDate is not null
        defaultBlockuserShouldBeFound("creationDate.specified=true");

        // Get all the blockuserList where creationDate is null
        defaultBlockuserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBlockusersByBlockeduserIsEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);
        Appuser blockeduser = AppuserResourceIT.createEntity(em);
        em.persist(blockeduser);
        em.flush();
        blockuser.setBlockeduser(blockeduser);
        blockuserRepository.saveAndFlush(blockuser);
        Long blockeduserId = blockeduser.getId();

        // Get all the blockuserList where blockeduser equals to blockeduserId
        defaultBlockuserShouldBeFound("blockeduserId.equals=" + blockeduserId);

        // Get all the blockuserList where blockeduser equals to (blockeduserId + 1)
        defaultBlockuserShouldNotBeFound("blockeduserId.equals=" + (blockeduserId + 1));
    }

    @Test
    @Transactional
    void getAllBlockusersByBlockinguserIsEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);
        Appuser blockinguser = AppuserResourceIT.createEntity(em);
        em.persist(blockinguser);
        em.flush();
        blockuser.setBlockinguser(blockinguser);
        blockuserRepository.saveAndFlush(blockuser);
        Long blockinguserId = blockinguser.getId();

        // Get all the blockuserList where blockinguser equals to blockinguserId
        defaultBlockuserShouldBeFound("blockinguserId.equals=" + blockinguserId);

        // Get all the blockuserList where blockinguser equals to (blockinguserId + 1)
        defaultBlockuserShouldNotBeFound("blockinguserId.equals=" + (blockinguserId + 1));
    }

    @Test
    @Transactional
    void getAllBlockusersByCblockeduserIsEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);
        Community cblockeduser = CommunityResourceIT.createEntity(em);
        em.persist(cblockeduser);
        em.flush();
        blockuser.setCblockeduser(cblockeduser);
        blockuserRepository.saveAndFlush(blockuser);
        Long cblockeduserId = cblockeduser.getId();

        // Get all the blockuserList where cblockeduser equals to cblockeduserId
        defaultBlockuserShouldBeFound("cblockeduserId.equals=" + cblockeduserId);

        // Get all the blockuserList where cblockeduser equals to (cblockeduserId + 1)
        defaultBlockuserShouldNotBeFound("cblockeduserId.equals=" + (cblockeduserId + 1));
    }

    @Test
    @Transactional
    void getAllBlockusersByCblockinguserIsEqualToSomething() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);
        Community cblockinguser = CommunityResourceIT.createEntity(em);
        em.persist(cblockinguser);
        em.flush();
        blockuser.setCblockinguser(cblockinguser);
        blockuserRepository.saveAndFlush(blockuser);
        Long cblockinguserId = cblockinguser.getId();

        // Get all the blockuserList where cblockinguser equals to cblockinguserId
        defaultBlockuserShouldBeFound("cblockinguserId.equals=" + cblockinguserId);

        // Get all the blockuserList where cblockinguser equals to (cblockinguserId + 1)
        defaultBlockuserShouldNotBeFound("cblockinguserId.equals=" + (cblockinguserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBlockuserShouldBeFound(String filter) throws Exception {
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBlockuserShouldNotBeFound(String filter) throws Exception {
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlockuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBlockuser() throws Exception {
        // Get the blockuser
        restBlockuserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();

        // Update the blockuser
        Blockuser updatedBlockuser = blockuserRepository.findById(blockuser.getId()).get();
        // Disconnect from session so that the updates on updatedBlockuser are not directly saved in db
        em.detach(updatedBlockuser);
        updatedBlockuser.creationDate(UPDATED_CREATION_DATE);
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(updatedBlockuser);

        restBlockuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isOk());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlockuserWithPatch() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();

        // Update the blockuser using partial update
        Blockuser partialUpdatedBlockuser = new Blockuser();
        partialUpdatedBlockuser.setId(blockuser.getId());

        restBlockuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlockuser))
            )
            .andExpect(status().isOk());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBlockuserWithPatch() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();

        // Update the blockuser using partial update
        Blockuser partialUpdatedBlockuser = new Blockuser();
        partialUpdatedBlockuser.setId(blockuser.getId());

        partialUpdatedBlockuser.creationDate(UPDATED_CREATION_DATE);

        restBlockuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlockuser))
            )
            .andExpect(status().isOk());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blockuserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();
        blockuser.setId(count.incrementAndGet());

        // Create the Blockuser
        BlockuserDTO blockuserDTO = blockuserMapper.toDto(blockuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockuserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blockuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeDelete = blockuserRepository.findAll().size();

        // Delete the blockuser
        restBlockuserMockMvc
            .perform(delete(ENTITY_API_URL_ID, blockuser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
