package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Urllink;
import com.spingular.cms.repository.UrllinkRepository;
import com.spingular.cms.service.criteria.UrllinkCriteria;
import com.spingular.cms.service.dto.UrllinkDTO;
import com.spingular.cms.service.mapper.UrllinkMapper;
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
 * Integration tests for the {@link UrllinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UrllinkResourceIT {

    private static final String DEFAULT_LINK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LINK_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_URL = "AAAAAAAAAA";
    private static final String UPDATED_LINK_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/urllinks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UrllinkRepository urllinkRepository;

    @Autowired
    private UrllinkMapper urllinkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUrllinkMockMvc;

    private Urllink urllink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urllink createEntity(EntityManager em) {
        Urllink urllink = new Urllink().linkText(DEFAULT_LINK_TEXT).linkURL(DEFAULT_LINK_URL);
        return urllink;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urllink createUpdatedEntity(EntityManager em) {
        Urllink urllink = new Urllink().linkText(UPDATED_LINK_TEXT).linkURL(UPDATED_LINK_URL);
        return urllink;
    }

    @BeforeEach
    public void initTest() {
        urllink = createEntity(em);
    }

    @Test
    @Transactional
    void createUrllink() throws Exception {
        int databaseSizeBeforeCreate = urllinkRepository.findAll().size();
        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);
        restUrllinkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeCreate + 1);
        Urllink testUrllink = urllinkList.get(urllinkList.size() - 1);
        assertThat(testUrllink.getLinkText()).isEqualTo(DEFAULT_LINK_TEXT);
        assertThat(testUrllink.getLinkURL()).isEqualTo(DEFAULT_LINK_URL);
    }

    @Test
    @Transactional
    void createUrllinkWithExistingId() throws Exception {
        // Create the Urllink with an existing ID
        urllink.setId(1L);
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        int databaseSizeBeforeCreate = urllinkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrllinkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLinkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = urllinkRepository.findAll().size();
        // set the field null
        urllink.setLinkText(null);

        // Create the Urllink, which fails.
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        restUrllinkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLinkURLIsRequired() throws Exception {
        int databaseSizeBeforeTest = urllinkRepository.findAll().size();
        // set the field null
        urllink.setLinkURL(null);

        // Create the Urllink, which fails.
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        restUrllinkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUrllinks() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urllink.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkText").value(hasItem(DEFAULT_LINK_TEXT)))
            .andExpect(jsonPath("$.[*].linkURL").value(hasItem(DEFAULT_LINK_URL)));
    }

    @Test
    @Transactional
    void getUrllink() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get the urllink
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL_ID, urllink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(urllink.getId().intValue()))
            .andExpect(jsonPath("$.linkText").value(DEFAULT_LINK_TEXT))
            .andExpect(jsonPath("$.linkURL").value(DEFAULT_LINK_URL));
    }

    @Test
    @Transactional
    void getUrllinksByIdFiltering() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        Long id = urllink.getId();

        defaultUrllinkShouldBeFound("id.equals=" + id);
        defaultUrllinkShouldNotBeFound("id.notEquals=" + id);

        defaultUrllinkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUrllinkShouldNotBeFound("id.greaterThan=" + id);

        defaultUrllinkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUrllinkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText equals to DEFAULT_LINK_TEXT
        defaultUrllinkShouldBeFound("linkText.equals=" + DEFAULT_LINK_TEXT);

        // Get all the urllinkList where linkText equals to UPDATED_LINK_TEXT
        defaultUrllinkShouldNotBeFound("linkText.equals=" + UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText not equals to DEFAULT_LINK_TEXT
        defaultUrllinkShouldNotBeFound("linkText.notEquals=" + DEFAULT_LINK_TEXT);

        // Get all the urllinkList where linkText not equals to UPDATED_LINK_TEXT
        defaultUrllinkShouldBeFound("linkText.notEquals=" + UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextIsInShouldWork() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText in DEFAULT_LINK_TEXT or UPDATED_LINK_TEXT
        defaultUrllinkShouldBeFound("linkText.in=" + DEFAULT_LINK_TEXT + "," + UPDATED_LINK_TEXT);

        // Get all the urllinkList where linkText equals to UPDATED_LINK_TEXT
        defaultUrllinkShouldNotBeFound("linkText.in=" + UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText is not null
        defaultUrllinkShouldBeFound("linkText.specified=true");

        // Get all the urllinkList where linkText is null
        defaultUrllinkShouldNotBeFound("linkText.specified=false");
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextContainsSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText contains DEFAULT_LINK_TEXT
        defaultUrllinkShouldBeFound("linkText.contains=" + DEFAULT_LINK_TEXT);

        // Get all the urllinkList where linkText contains UPDATED_LINK_TEXT
        defaultUrllinkShouldNotBeFound("linkText.contains=" + UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkTextNotContainsSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkText does not contain DEFAULT_LINK_TEXT
        defaultUrllinkShouldNotBeFound("linkText.doesNotContain=" + DEFAULT_LINK_TEXT);

        // Get all the urllinkList where linkText does not contain UPDATED_LINK_TEXT
        defaultUrllinkShouldBeFound("linkText.doesNotContain=" + UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLIsEqualToSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL equals to DEFAULT_LINK_URL
        defaultUrllinkShouldBeFound("linkURL.equals=" + DEFAULT_LINK_URL);

        // Get all the urllinkList where linkURL equals to UPDATED_LINK_URL
        defaultUrllinkShouldNotBeFound("linkURL.equals=" + UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL not equals to DEFAULT_LINK_URL
        defaultUrllinkShouldNotBeFound("linkURL.notEquals=" + DEFAULT_LINK_URL);

        // Get all the urllinkList where linkURL not equals to UPDATED_LINK_URL
        defaultUrllinkShouldBeFound("linkURL.notEquals=" + UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLIsInShouldWork() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL in DEFAULT_LINK_URL or UPDATED_LINK_URL
        defaultUrllinkShouldBeFound("linkURL.in=" + DEFAULT_LINK_URL + "," + UPDATED_LINK_URL);

        // Get all the urllinkList where linkURL equals to UPDATED_LINK_URL
        defaultUrllinkShouldNotBeFound("linkURL.in=" + UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL is not null
        defaultUrllinkShouldBeFound("linkURL.specified=true");

        // Get all the urllinkList where linkURL is null
        defaultUrllinkShouldNotBeFound("linkURL.specified=false");
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLContainsSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL contains DEFAULT_LINK_URL
        defaultUrllinkShouldBeFound("linkURL.contains=" + DEFAULT_LINK_URL);

        // Get all the urllinkList where linkURL contains UPDATED_LINK_URL
        defaultUrllinkShouldNotBeFound("linkURL.contains=" + UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void getAllUrllinksByLinkURLNotContainsSomething() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        // Get all the urllinkList where linkURL does not contain DEFAULT_LINK_URL
        defaultUrllinkShouldNotBeFound("linkURL.doesNotContain=" + DEFAULT_LINK_URL);

        // Get all the urllinkList where linkURL does not contain UPDATED_LINK_URL
        defaultUrllinkShouldBeFound("linkURL.doesNotContain=" + UPDATED_LINK_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUrllinkShouldBeFound(String filter) throws Exception {
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urllink.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkText").value(hasItem(DEFAULT_LINK_TEXT)))
            .andExpect(jsonPath("$.[*].linkURL").value(hasItem(DEFAULT_LINK_URL)));

        // Check, that the count call also returns 1
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUrllinkShouldNotBeFound(String filter) throws Exception {
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUrllinkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUrllink() throws Exception {
        // Get the urllink
        restUrllinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUrllink() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();

        // Update the urllink
        Urllink updatedUrllink = urllinkRepository.findById(urllink.getId()).get();
        // Disconnect from session so that the updates on updatedUrllink are not directly saved in db
        em.detach(updatedUrllink);
        updatedUrllink.linkText(UPDATED_LINK_TEXT).linkURL(UPDATED_LINK_URL);
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(updatedUrllink);

        restUrllinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urllinkDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
        Urllink testUrllink = urllinkList.get(urllinkList.size() - 1);
        assertThat(testUrllink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
        assertThat(testUrllink.getLinkURL()).isEqualTo(UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void putNonExistingUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urllinkDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUrllinkWithPatch() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();

        // Update the urllink using partial update
        Urllink partialUpdatedUrllink = new Urllink();
        partialUpdatedUrllink.setId(urllink.getId());

        partialUpdatedUrllink.linkText(UPDATED_LINK_TEXT).linkURL(UPDATED_LINK_URL);

        restUrllinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrllink.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUrllink))
            )
            .andExpect(status().isOk());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
        Urllink testUrllink = urllinkList.get(urllinkList.size() - 1);
        assertThat(testUrllink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
        assertThat(testUrllink.getLinkURL()).isEqualTo(UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void fullUpdateUrllinkWithPatch() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();

        // Update the urllink using partial update
        Urllink partialUpdatedUrllink = new Urllink();
        partialUpdatedUrllink.setId(urllink.getId());

        partialUpdatedUrllink.linkText(UPDATED_LINK_TEXT).linkURL(UPDATED_LINK_URL);

        restUrllinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrllink.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUrllink))
            )
            .andExpect(status().isOk());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
        Urllink testUrllink = urllinkList.get(urllinkList.size() - 1);
        assertThat(testUrllink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
        assertThat(testUrllink.getLinkURL()).isEqualTo(UPDATED_LINK_URL);
    }

    @Test
    @Transactional
    void patchNonExistingUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, urllinkDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUrllink() throws Exception {
        int databaseSizeBeforeUpdate = urllinkRepository.findAll().size();
        urllink.setId(count.incrementAndGet());

        // Create the Urllink
        UrllinkDTO urllinkDTO = urllinkMapper.toDto(urllink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrllinkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urllinkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urllink in the database
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUrllink() throws Exception {
        // Initialize the database
        urllinkRepository.saveAndFlush(urllink);

        int databaseSizeBeforeDelete = urllinkRepository.findAll().size();

        // Delete the urllink
        restUrllinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, urllink.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Urllink> urllinkList = urllinkRepository.findAll();
        assertThat(urllinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
