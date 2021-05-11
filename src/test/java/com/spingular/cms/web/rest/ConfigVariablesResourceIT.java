package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.ConfigVariables;
import com.spingular.cms.repository.ConfigVariablesRepository;
import com.spingular.cms.service.criteria.ConfigVariablesCriteria;
import com.spingular.cms.service.dto.ConfigVariablesDTO;
import com.spingular.cms.service.mapper.ConfigVariablesMapper;
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
 * Integration tests for the {@link ConfigVariablesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigVariablesResourceIT {

    private static final Long DEFAULT_CONFIG_VAR_LONG_1 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_1 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_1 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_2 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_2 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_2 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_3 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_3 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_3 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_4 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_4 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_4 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_5 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_5 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_5 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_6 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_6 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_6 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_7 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_7 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_7 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_8 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_8 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_8 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_9 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_9 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_9 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_10 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_10 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_10 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_11 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_11 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_11 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_12 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_12 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_12 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_13 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_13 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_13 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_14 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_14 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_14 = 1L - 1L;

    private static final Long DEFAULT_CONFIG_VAR_LONG_15 = 1L;
    private static final Long UPDATED_CONFIG_VAR_LONG_15 = 2L;
    private static final Long SMALLER_CONFIG_VAR_LONG_15 = 1L - 1L;

    private static final Boolean DEFAULT_CONFIG_VAR_BOOLEAN_16 = false;
    private static final Boolean UPDATED_CONFIG_VAR_BOOLEAN_16 = true;

    private static final Boolean DEFAULT_CONFIG_VAR_BOOLEAN_17 = false;
    private static final Boolean UPDATED_CONFIG_VAR_BOOLEAN_17 = true;

    private static final Boolean DEFAULT_CONFIG_VAR_BOOLEAN_18 = false;
    private static final Boolean UPDATED_CONFIG_VAR_BOOLEAN_18 = true;

    private static final String DEFAULT_CONFIG_VAR_STRING_19 = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VAR_STRING_19 = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VAR_STRING_20 = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VAR_STRING_20 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/config-variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigVariablesRepository configVariablesRepository;

    @Autowired
    private ConfigVariablesMapper configVariablesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigVariablesMockMvc;

    private ConfigVariables configVariables;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigVariables createEntity(EntityManager em) {
        ConfigVariables configVariables = new ConfigVariables()
            .configVarLong1(DEFAULT_CONFIG_VAR_LONG_1)
            .configVarLong2(DEFAULT_CONFIG_VAR_LONG_2)
            .configVarLong3(DEFAULT_CONFIG_VAR_LONG_3)
            .configVarLong4(DEFAULT_CONFIG_VAR_LONG_4)
            .configVarLong5(DEFAULT_CONFIG_VAR_LONG_5)
            .configVarLong6(DEFAULT_CONFIG_VAR_LONG_6)
            .configVarLong7(DEFAULT_CONFIG_VAR_LONG_7)
            .configVarLong8(DEFAULT_CONFIG_VAR_LONG_8)
            .configVarLong9(DEFAULT_CONFIG_VAR_LONG_9)
            .configVarLong10(DEFAULT_CONFIG_VAR_LONG_10)
            .configVarLong11(DEFAULT_CONFIG_VAR_LONG_11)
            .configVarLong12(DEFAULT_CONFIG_VAR_LONG_12)
            .configVarLong13(DEFAULT_CONFIG_VAR_LONG_13)
            .configVarLong14(DEFAULT_CONFIG_VAR_LONG_14)
            .configVarLong15(DEFAULT_CONFIG_VAR_LONG_15)
            .configVarBoolean16(DEFAULT_CONFIG_VAR_BOOLEAN_16)
            .configVarBoolean17(DEFAULT_CONFIG_VAR_BOOLEAN_17)
            .configVarBoolean18(DEFAULT_CONFIG_VAR_BOOLEAN_18)
            .configVarString19(DEFAULT_CONFIG_VAR_STRING_19)
            .configVarString20(DEFAULT_CONFIG_VAR_STRING_20);
        return configVariables;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigVariables createUpdatedEntity(EntityManager em) {
        ConfigVariables configVariables = new ConfigVariables()
            .configVarLong1(UPDATED_CONFIG_VAR_LONG_1)
            .configVarLong2(UPDATED_CONFIG_VAR_LONG_2)
            .configVarLong3(UPDATED_CONFIG_VAR_LONG_3)
            .configVarLong4(UPDATED_CONFIG_VAR_LONG_4)
            .configVarLong5(UPDATED_CONFIG_VAR_LONG_5)
            .configVarLong6(UPDATED_CONFIG_VAR_LONG_6)
            .configVarLong7(UPDATED_CONFIG_VAR_LONG_7)
            .configVarLong8(UPDATED_CONFIG_VAR_LONG_8)
            .configVarLong9(UPDATED_CONFIG_VAR_LONG_9)
            .configVarLong10(UPDATED_CONFIG_VAR_LONG_10)
            .configVarLong11(UPDATED_CONFIG_VAR_LONG_11)
            .configVarLong12(UPDATED_CONFIG_VAR_LONG_12)
            .configVarLong13(UPDATED_CONFIG_VAR_LONG_13)
            .configVarLong14(UPDATED_CONFIG_VAR_LONG_14)
            .configVarLong15(UPDATED_CONFIG_VAR_LONG_15)
            .configVarBoolean16(UPDATED_CONFIG_VAR_BOOLEAN_16)
            .configVarBoolean17(UPDATED_CONFIG_VAR_BOOLEAN_17)
            .configVarBoolean18(UPDATED_CONFIG_VAR_BOOLEAN_18)
            .configVarString19(UPDATED_CONFIG_VAR_STRING_19)
            .configVarString20(UPDATED_CONFIG_VAR_STRING_20);
        return configVariables;
    }

    @BeforeEach
    public void initTest() {
        configVariables = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigVariables() throws Exception {
        int databaseSizeBeforeCreate = configVariablesRepository.findAll().size();
        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);
        restConfigVariablesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigVariables testConfigVariables = configVariablesList.get(configVariablesList.size() - 1);
        assertThat(testConfigVariables.getConfigVarLong1()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_1);
        assertThat(testConfigVariables.getConfigVarLong2()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_2);
        assertThat(testConfigVariables.getConfigVarLong3()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_3);
        assertThat(testConfigVariables.getConfigVarLong4()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_4);
        assertThat(testConfigVariables.getConfigVarLong5()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_5);
        assertThat(testConfigVariables.getConfigVarLong6()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_6);
        assertThat(testConfigVariables.getConfigVarLong7()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_7);
        assertThat(testConfigVariables.getConfigVarLong8()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_8);
        assertThat(testConfigVariables.getConfigVarLong9()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_9);
        assertThat(testConfigVariables.getConfigVarLong10()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_10);
        assertThat(testConfigVariables.getConfigVarLong11()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_11);
        assertThat(testConfigVariables.getConfigVarLong12()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_12);
        assertThat(testConfigVariables.getConfigVarLong13()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_13);
        assertThat(testConfigVariables.getConfigVarLong14()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_14);
        assertThat(testConfigVariables.getConfigVarLong15()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_15);
        assertThat(testConfigVariables.getConfigVarBoolean16()).isEqualTo(DEFAULT_CONFIG_VAR_BOOLEAN_16);
        assertThat(testConfigVariables.getConfigVarBoolean17()).isEqualTo(DEFAULT_CONFIG_VAR_BOOLEAN_17);
        assertThat(testConfigVariables.getConfigVarBoolean18()).isEqualTo(DEFAULT_CONFIG_VAR_BOOLEAN_18);
        assertThat(testConfigVariables.getConfigVarString19()).isEqualTo(DEFAULT_CONFIG_VAR_STRING_19);
        assertThat(testConfigVariables.getConfigVarString20()).isEqualTo(DEFAULT_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void createConfigVariablesWithExistingId() throws Exception {
        // Create the ConfigVariables with an existing ID
        configVariables.setId(1L);
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        int databaseSizeBeforeCreate = configVariablesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigVariablesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigVariables() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configVariables.getId().intValue())))
            .andExpect(jsonPath("$.[*].configVarLong1").value(hasItem(DEFAULT_CONFIG_VAR_LONG_1.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong2").value(hasItem(DEFAULT_CONFIG_VAR_LONG_2.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong3").value(hasItem(DEFAULT_CONFIG_VAR_LONG_3.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong4").value(hasItem(DEFAULT_CONFIG_VAR_LONG_4.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong5").value(hasItem(DEFAULT_CONFIG_VAR_LONG_5.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong6").value(hasItem(DEFAULT_CONFIG_VAR_LONG_6.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong7").value(hasItem(DEFAULT_CONFIG_VAR_LONG_7.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong8").value(hasItem(DEFAULT_CONFIG_VAR_LONG_8.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong9").value(hasItem(DEFAULT_CONFIG_VAR_LONG_9.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong10").value(hasItem(DEFAULT_CONFIG_VAR_LONG_10.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong11").value(hasItem(DEFAULT_CONFIG_VAR_LONG_11.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong12").value(hasItem(DEFAULT_CONFIG_VAR_LONG_12.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong13").value(hasItem(DEFAULT_CONFIG_VAR_LONG_13.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong14").value(hasItem(DEFAULT_CONFIG_VAR_LONG_14.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong15").value(hasItem(DEFAULT_CONFIG_VAR_LONG_15.intValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean16").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_16.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean17").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_17.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean18").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_18.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarString19").value(hasItem(DEFAULT_CONFIG_VAR_STRING_19)))
            .andExpect(jsonPath("$.[*].configVarString20").value(hasItem(DEFAULT_CONFIG_VAR_STRING_20)));
    }

    @Test
    @Transactional
    void getConfigVariables() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get the configVariables
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL_ID, configVariables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configVariables.getId().intValue()))
            .andExpect(jsonPath("$.configVarLong1").value(DEFAULT_CONFIG_VAR_LONG_1.intValue()))
            .andExpect(jsonPath("$.configVarLong2").value(DEFAULT_CONFIG_VAR_LONG_2.intValue()))
            .andExpect(jsonPath("$.configVarLong3").value(DEFAULT_CONFIG_VAR_LONG_3.intValue()))
            .andExpect(jsonPath("$.configVarLong4").value(DEFAULT_CONFIG_VAR_LONG_4.intValue()))
            .andExpect(jsonPath("$.configVarLong5").value(DEFAULT_CONFIG_VAR_LONG_5.intValue()))
            .andExpect(jsonPath("$.configVarLong6").value(DEFAULT_CONFIG_VAR_LONG_6.intValue()))
            .andExpect(jsonPath("$.configVarLong7").value(DEFAULT_CONFIG_VAR_LONG_7.intValue()))
            .andExpect(jsonPath("$.configVarLong8").value(DEFAULT_CONFIG_VAR_LONG_8.intValue()))
            .andExpect(jsonPath("$.configVarLong9").value(DEFAULT_CONFIG_VAR_LONG_9.intValue()))
            .andExpect(jsonPath("$.configVarLong10").value(DEFAULT_CONFIG_VAR_LONG_10.intValue()))
            .andExpect(jsonPath("$.configVarLong11").value(DEFAULT_CONFIG_VAR_LONG_11.intValue()))
            .andExpect(jsonPath("$.configVarLong12").value(DEFAULT_CONFIG_VAR_LONG_12.intValue()))
            .andExpect(jsonPath("$.configVarLong13").value(DEFAULT_CONFIG_VAR_LONG_13.intValue()))
            .andExpect(jsonPath("$.configVarLong14").value(DEFAULT_CONFIG_VAR_LONG_14.intValue()))
            .andExpect(jsonPath("$.configVarLong15").value(DEFAULT_CONFIG_VAR_LONG_15.intValue()))
            .andExpect(jsonPath("$.configVarBoolean16").value(DEFAULT_CONFIG_VAR_BOOLEAN_16.booleanValue()))
            .andExpect(jsonPath("$.configVarBoolean17").value(DEFAULT_CONFIG_VAR_BOOLEAN_17.booleanValue()))
            .andExpect(jsonPath("$.configVarBoolean18").value(DEFAULT_CONFIG_VAR_BOOLEAN_18.booleanValue()))
            .andExpect(jsonPath("$.configVarString19").value(DEFAULT_CONFIG_VAR_STRING_19))
            .andExpect(jsonPath("$.configVarString20").value(DEFAULT_CONFIG_VAR_STRING_20));
    }

    @Test
    @Transactional
    void getConfigVariablesByIdFiltering() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        Long id = configVariables.getId();

        defaultConfigVariablesShouldBeFound("id.equals=" + id);
        defaultConfigVariablesShouldNotBeFound("id.notEquals=" + id);

        defaultConfigVariablesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfigVariablesShouldNotBeFound("id.greaterThan=" + id);

        defaultConfigVariablesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfigVariablesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 equals to DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.equals=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 equals to UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.equals=" + UPDATED_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 not equals to DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.notEquals=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 not equals to UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.notEquals=" + UPDATED_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 in DEFAULT_CONFIG_VAR_LONG_1 or UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.in=" + DEFAULT_CONFIG_VAR_LONG_1 + "," + UPDATED_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 equals to UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.in=" + UPDATED_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 is not null
        defaultConfigVariablesShouldBeFound("configVarLong1.specified=true");

        // Get all the configVariablesList where configVarLong1 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong1.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 is greater than or equal to UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 is less than or equal to DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 is less than or equal to SMALLER_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 is less than DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.lessThan=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 is less than UPDATED_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.lessThan=" + UPDATED_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong1IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong1 is greater than DEFAULT_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldNotBeFound("configVarLong1.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_1);

        // Get all the configVariablesList where configVarLong1 is greater than SMALLER_CONFIG_VAR_LONG_1
        defaultConfigVariablesShouldBeFound("configVarLong1.greaterThan=" + SMALLER_CONFIG_VAR_LONG_1);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 equals to DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.equals=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 equals to UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.equals=" + UPDATED_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 not equals to DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.notEquals=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 not equals to UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.notEquals=" + UPDATED_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 in DEFAULT_CONFIG_VAR_LONG_2 or UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.in=" + DEFAULT_CONFIG_VAR_LONG_2 + "," + UPDATED_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 equals to UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.in=" + UPDATED_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 is not null
        defaultConfigVariablesShouldBeFound("configVarLong2.specified=true");

        // Get all the configVariablesList where configVarLong2 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong2.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 is greater than or equal to UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 is less than or equal to DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 is less than or equal to SMALLER_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 is less than DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.lessThan=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 is less than UPDATED_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.lessThan=" + UPDATED_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong2IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong2 is greater than DEFAULT_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldNotBeFound("configVarLong2.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_2);

        // Get all the configVariablesList where configVarLong2 is greater than SMALLER_CONFIG_VAR_LONG_2
        defaultConfigVariablesShouldBeFound("configVarLong2.greaterThan=" + SMALLER_CONFIG_VAR_LONG_2);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 equals to DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.equals=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 equals to UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.equals=" + UPDATED_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 not equals to DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.notEquals=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 not equals to UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.notEquals=" + UPDATED_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 in DEFAULT_CONFIG_VAR_LONG_3 or UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.in=" + DEFAULT_CONFIG_VAR_LONG_3 + "," + UPDATED_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 equals to UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.in=" + UPDATED_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 is not null
        defaultConfigVariablesShouldBeFound("configVarLong3.specified=true");

        // Get all the configVariablesList where configVarLong3 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong3.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 is greater than or equal to UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 is less than or equal to DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 is less than or equal to SMALLER_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 is less than DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.lessThan=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 is less than UPDATED_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.lessThan=" + UPDATED_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong3IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong3 is greater than DEFAULT_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldNotBeFound("configVarLong3.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_3);

        // Get all the configVariablesList where configVarLong3 is greater than SMALLER_CONFIG_VAR_LONG_3
        defaultConfigVariablesShouldBeFound("configVarLong3.greaterThan=" + SMALLER_CONFIG_VAR_LONG_3);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 equals to DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.equals=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 equals to UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.equals=" + UPDATED_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 not equals to DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.notEquals=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 not equals to UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.notEquals=" + UPDATED_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 in DEFAULT_CONFIG_VAR_LONG_4 or UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.in=" + DEFAULT_CONFIG_VAR_LONG_4 + "," + UPDATED_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 equals to UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.in=" + UPDATED_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 is not null
        defaultConfigVariablesShouldBeFound("configVarLong4.specified=true");

        // Get all the configVariablesList where configVarLong4 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong4.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 is greater than or equal to UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 is less than or equal to DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 is less than or equal to SMALLER_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 is less than DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.lessThan=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 is less than UPDATED_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.lessThan=" + UPDATED_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong4IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong4 is greater than DEFAULT_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldNotBeFound("configVarLong4.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_4);

        // Get all the configVariablesList where configVarLong4 is greater than SMALLER_CONFIG_VAR_LONG_4
        defaultConfigVariablesShouldBeFound("configVarLong4.greaterThan=" + SMALLER_CONFIG_VAR_LONG_4);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 equals to DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.equals=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 equals to UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.equals=" + UPDATED_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 not equals to DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.notEquals=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 not equals to UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.notEquals=" + UPDATED_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 in DEFAULT_CONFIG_VAR_LONG_5 or UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.in=" + DEFAULT_CONFIG_VAR_LONG_5 + "," + UPDATED_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 equals to UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.in=" + UPDATED_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 is not null
        defaultConfigVariablesShouldBeFound("configVarLong5.specified=true");

        // Get all the configVariablesList where configVarLong5 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong5.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 is greater than or equal to UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 is less than or equal to DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 is less than or equal to SMALLER_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 is less than DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.lessThan=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 is less than UPDATED_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.lessThan=" + UPDATED_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong5IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong5 is greater than DEFAULT_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldNotBeFound("configVarLong5.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_5);

        // Get all the configVariablesList where configVarLong5 is greater than SMALLER_CONFIG_VAR_LONG_5
        defaultConfigVariablesShouldBeFound("configVarLong5.greaterThan=" + SMALLER_CONFIG_VAR_LONG_5);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 equals to DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.equals=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 equals to UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.equals=" + UPDATED_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 not equals to DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.notEquals=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 not equals to UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.notEquals=" + UPDATED_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 in DEFAULT_CONFIG_VAR_LONG_6 or UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.in=" + DEFAULT_CONFIG_VAR_LONG_6 + "," + UPDATED_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 equals to UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.in=" + UPDATED_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 is not null
        defaultConfigVariablesShouldBeFound("configVarLong6.specified=true");

        // Get all the configVariablesList where configVarLong6 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong6.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 is greater than or equal to UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 is less than or equal to DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 is less than or equal to SMALLER_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 is less than DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.lessThan=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 is less than UPDATED_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.lessThan=" + UPDATED_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong6IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong6 is greater than DEFAULT_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldNotBeFound("configVarLong6.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_6);

        // Get all the configVariablesList where configVarLong6 is greater than SMALLER_CONFIG_VAR_LONG_6
        defaultConfigVariablesShouldBeFound("configVarLong6.greaterThan=" + SMALLER_CONFIG_VAR_LONG_6);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 equals to DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.equals=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 equals to UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.equals=" + UPDATED_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 not equals to DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.notEquals=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 not equals to UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.notEquals=" + UPDATED_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 in DEFAULT_CONFIG_VAR_LONG_7 or UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.in=" + DEFAULT_CONFIG_VAR_LONG_7 + "," + UPDATED_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 equals to UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.in=" + UPDATED_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 is not null
        defaultConfigVariablesShouldBeFound("configVarLong7.specified=true");

        // Get all the configVariablesList where configVarLong7 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong7.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 is greater than or equal to UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 is less than or equal to DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 is less than or equal to SMALLER_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 is less than DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.lessThan=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 is less than UPDATED_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.lessThan=" + UPDATED_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong7IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong7 is greater than DEFAULT_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldNotBeFound("configVarLong7.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_7);

        // Get all the configVariablesList where configVarLong7 is greater than SMALLER_CONFIG_VAR_LONG_7
        defaultConfigVariablesShouldBeFound("configVarLong7.greaterThan=" + SMALLER_CONFIG_VAR_LONG_7);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 equals to DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.equals=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 equals to UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.equals=" + UPDATED_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 not equals to DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.notEquals=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 not equals to UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.notEquals=" + UPDATED_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 in DEFAULT_CONFIG_VAR_LONG_8 or UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.in=" + DEFAULT_CONFIG_VAR_LONG_8 + "," + UPDATED_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 equals to UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.in=" + UPDATED_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 is not null
        defaultConfigVariablesShouldBeFound("configVarLong8.specified=true");

        // Get all the configVariablesList where configVarLong8 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong8.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 is greater than or equal to UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 is less than or equal to DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 is less than or equal to SMALLER_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 is less than DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.lessThan=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 is less than UPDATED_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.lessThan=" + UPDATED_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong8IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong8 is greater than DEFAULT_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldNotBeFound("configVarLong8.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_8);

        // Get all the configVariablesList where configVarLong8 is greater than SMALLER_CONFIG_VAR_LONG_8
        defaultConfigVariablesShouldBeFound("configVarLong8.greaterThan=" + SMALLER_CONFIG_VAR_LONG_8);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 equals to DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.equals=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 equals to UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.equals=" + UPDATED_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 not equals to DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.notEquals=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 not equals to UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.notEquals=" + UPDATED_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 in DEFAULT_CONFIG_VAR_LONG_9 or UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.in=" + DEFAULT_CONFIG_VAR_LONG_9 + "," + UPDATED_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 equals to UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.in=" + UPDATED_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 is not null
        defaultConfigVariablesShouldBeFound("configVarLong9.specified=true");

        // Get all the configVariablesList where configVarLong9 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong9.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 is greater than or equal to UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 is less than or equal to DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 is less than or equal to SMALLER_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 is less than DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.lessThan=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 is less than UPDATED_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.lessThan=" + UPDATED_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong9IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong9 is greater than DEFAULT_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldNotBeFound("configVarLong9.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_9);

        // Get all the configVariablesList where configVarLong9 is greater than SMALLER_CONFIG_VAR_LONG_9
        defaultConfigVariablesShouldBeFound("configVarLong9.greaterThan=" + SMALLER_CONFIG_VAR_LONG_9);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 equals to DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.equals=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 equals to UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.equals=" + UPDATED_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 not equals to DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.notEquals=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 not equals to UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.notEquals=" + UPDATED_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 in DEFAULT_CONFIG_VAR_LONG_10 or UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.in=" + DEFAULT_CONFIG_VAR_LONG_10 + "," + UPDATED_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 equals to UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.in=" + UPDATED_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 is not null
        defaultConfigVariablesShouldBeFound("configVarLong10.specified=true");

        // Get all the configVariablesList where configVarLong10 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong10.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 is greater than or equal to UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 is less than or equal to DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 is less than or equal to SMALLER_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 is less than DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.lessThan=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 is less than UPDATED_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.lessThan=" + UPDATED_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong10IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong10 is greater than DEFAULT_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldNotBeFound("configVarLong10.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_10);

        // Get all the configVariablesList where configVarLong10 is greater than SMALLER_CONFIG_VAR_LONG_10
        defaultConfigVariablesShouldBeFound("configVarLong10.greaterThan=" + SMALLER_CONFIG_VAR_LONG_10);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 equals to DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.equals=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 equals to UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.equals=" + UPDATED_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 not equals to DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.notEquals=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 not equals to UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.notEquals=" + UPDATED_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 in DEFAULT_CONFIG_VAR_LONG_11 or UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.in=" + DEFAULT_CONFIG_VAR_LONG_11 + "," + UPDATED_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 equals to UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.in=" + UPDATED_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 is not null
        defaultConfigVariablesShouldBeFound("configVarLong11.specified=true");

        // Get all the configVariablesList where configVarLong11 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong11.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 is greater than or equal to UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 is less than or equal to DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 is less than or equal to SMALLER_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 is less than DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.lessThan=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 is less than UPDATED_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.lessThan=" + UPDATED_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong11IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong11 is greater than DEFAULT_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldNotBeFound("configVarLong11.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_11);

        // Get all the configVariablesList where configVarLong11 is greater than SMALLER_CONFIG_VAR_LONG_11
        defaultConfigVariablesShouldBeFound("configVarLong11.greaterThan=" + SMALLER_CONFIG_VAR_LONG_11);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 equals to DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.equals=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 equals to UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.equals=" + UPDATED_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 not equals to DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.notEquals=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 not equals to UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.notEquals=" + UPDATED_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 in DEFAULT_CONFIG_VAR_LONG_12 or UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.in=" + DEFAULT_CONFIG_VAR_LONG_12 + "," + UPDATED_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 equals to UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.in=" + UPDATED_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 is not null
        defaultConfigVariablesShouldBeFound("configVarLong12.specified=true");

        // Get all the configVariablesList where configVarLong12 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong12.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 is greater than or equal to UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 is less than or equal to DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 is less than or equal to SMALLER_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 is less than DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.lessThan=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 is less than UPDATED_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.lessThan=" + UPDATED_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong12IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong12 is greater than DEFAULT_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldNotBeFound("configVarLong12.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_12);

        // Get all the configVariablesList where configVarLong12 is greater than SMALLER_CONFIG_VAR_LONG_12
        defaultConfigVariablesShouldBeFound("configVarLong12.greaterThan=" + SMALLER_CONFIG_VAR_LONG_12);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 equals to DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.equals=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 equals to UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.equals=" + UPDATED_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 not equals to DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.notEquals=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 not equals to UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.notEquals=" + UPDATED_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 in DEFAULT_CONFIG_VAR_LONG_13 or UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.in=" + DEFAULT_CONFIG_VAR_LONG_13 + "," + UPDATED_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 equals to UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.in=" + UPDATED_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 is not null
        defaultConfigVariablesShouldBeFound("configVarLong13.specified=true");

        // Get all the configVariablesList where configVarLong13 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong13.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 is greater than or equal to UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 is less than or equal to DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 is less than or equal to SMALLER_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 is less than DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.lessThan=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 is less than UPDATED_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.lessThan=" + UPDATED_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong13IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong13 is greater than DEFAULT_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldNotBeFound("configVarLong13.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_13);

        // Get all the configVariablesList where configVarLong13 is greater than SMALLER_CONFIG_VAR_LONG_13
        defaultConfigVariablesShouldBeFound("configVarLong13.greaterThan=" + SMALLER_CONFIG_VAR_LONG_13);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 equals to DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.equals=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 equals to UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.equals=" + UPDATED_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 not equals to DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.notEquals=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 not equals to UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.notEquals=" + UPDATED_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 in DEFAULT_CONFIG_VAR_LONG_14 or UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.in=" + DEFAULT_CONFIG_VAR_LONG_14 + "," + UPDATED_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 equals to UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.in=" + UPDATED_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 is not null
        defaultConfigVariablesShouldBeFound("configVarLong14.specified=true");

        // Get all the configVariablesList where configVarLong14 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong14.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 is greater than or equal to UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 is less than or equal to DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 is less than or equal to SMALLER_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 is less than DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.lessThan=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 is less than UPDATED_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.lessThan=" + UPDATED_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong14IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong14 is greater than DEFAULT_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldNotBeFound("configVarLong14.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_14);

        // Get all the configVariablesList where configVarLong14 is greater than SMALLER_CONFIG_VAR_LONG_14
        defaultConfigVariablesShouldBeFound("configVarLong14.greaterThan=" + SMALLER_CONFIG_VAR_LONG_14);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 equals to DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.equals=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 equals to UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.equals=" + UPDATED_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 not equals to DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.notEquals=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 not equals to UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.notEquals=" + UPDATED_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 in DEFAULT_CONFIG_VAR_LONG_15 or UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.in=" + DEFAULT_CONFIG_VAR_LONG_15 + "," + UPDATED_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 equals to UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.in=" + UPDATED_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 is not null
        defaultConfigVariablesShouldBeFound("configVarLong15.specified=true");

        // Get all the configVariablesList where configVarLong15 is null
        defaultConfigVariablesShouldNotBeFound("configVarLong15.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 is greater than or equal to DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.greaterThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 is greater than or equal to UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.greaterThanOrEqual=" + UPDATED_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 is less than or equal to DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.lessThanOrEqual=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 is less than or equal to SMALLER_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.lessThanOrEqual=" + SMALLER_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsLessThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 is less than DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.lessThan=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 is less than UPDATED_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.lessThan=" + UPDATED_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarLong15IsGreaterThanSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarLong15 is greater than DEFAULT_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldNotBeFound("configVarLong15.greaterThan=" + DEFAULT_CONFIG_VAR_LONG_15);

        // Get all the configVariablesList where configVarLong15 is greater than SMALLER_CONFIG_VAR_LONG_15
        defaultConfigVariablesShouldBeFound("configVarLong15.greaterThan=" + SMALLER_CONFIG_VAR_LONG_15);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean16IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean16 equals to DEFAULT_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldBeFound("configVarBoolean16.equals=" + DEFAULT_CONFIG_VAR_BOOLEAN_16);

        // Get all the configVariablesList where configVarBoolean16 equals to UPDATED_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldNotBeFound("configVarBoolean16.equals=" + UPDATED_CONFIG_VAR_BOOLEAN_16);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean16IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean16 not equals to DEFAULT_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldNotBeFound("configVarBoolean16.notEquals=" + DEFAULT_CONFIG_VAR_BOOLEAN_16);

        // Get all the configVariablesList where configVarBoolean16 not equals to UPDATED_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldBeFound("configVarBoolean16.notEquals=" + UPDATED_CONFIG_VAR_BOOLEAN_16);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean16IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean16 in DEFAULT_CONFIG_VAR_BOOLEAN_16 or UPDATED_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldBeFound("configVarBoolean16.in=" + DEFAULT_CONFIG_VAR_BOOLEAN_16 + "," + UPDATED_CONFIG_VAR_BOOLEAN_16);

        // Get all the configVariablesList where configVarBoolean16 equals to UPDATED_CONFIG_VAR_BOOLEAN_16
        defaultConfigVariablesShouldNotBeFound("configVarBoolean16.in=" + UPDATED_CONFIG_VAR_BOOLEAN_16);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean16IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean16 is not null
        defaultConfigVariablesShouldBeFound("configVarBoolean16.specified=true");

        // Get all the configVariablesList where configVarBoolean16 is null
        defaultConfigVariablesShouldNotBeFound("configVarBoolean16.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean17IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean17 equals to DEFAULT_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldBeFound("configVarBoolean17.equals=" + DEFAULT_CONFIG_VAR_BOOLEAN_17);

        // Get all the configVariablesList where configVarBoolean17 equals to UPDATED_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldNotBeFound("configVarBoolean17.equals=" + UPDATED_CONFIG_VAR_BOOLEAN_17);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean17IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean17 not equals to DEFAULT_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldNotBeFound("configVarBoolean17.notEquals=" + DEFAULT_CONFIG_VAR_BOOLEAN_17);

        // Get all the configVariablesList where configVarBoolean17 not equals to UPDATED_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldBeFound("configVarBoolean17.notEquals=" + UPDATED_CONFIG_VAR_BOOLEAN_17);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean17IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean17 in DEFAULT_CONFIG_VAR_BOOLEAN_17 or UPDATED_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldBeFound("configVarBoolean17.in=" + DEFAULT_CONFIG_VAR_BOOLEAN_17 + "," + UPDATED_CONFIG_VAR_BOOLEAN_17);

        // Get all the configVariablesList where configVarBoolean17 equals to UPDATED_CONFIG_VAR_BOOLEAN_17
        defaultConfigVariablesShouldNotBeFound("configVarBoolean17.in=" + UPDATED_CONFIG_VAR_BOOLEAN_17);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean17IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean17 is not null
        defaultConfigVariablesShouldBeFound("configVarBoolean17.specified=true");

        // Get all the configVariablesList where configVarBoolean17 is null
        defaultConfigVariablesShouldNotBeFound("configVarBoolean17.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean18IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean18 equals to DEFAULT_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldBeFound("configVarBoolean18.equals=" + DEFAULT_CONFIG_VAR_BOOLEAN_18);

        // Get all the configVariablesList where configVarBoolean18 equals to UPDATED_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldNotBeFound("configVarBoolean18.equals=" + UPDATED_CONFIG_VAR_BOOLEAN_18);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean18IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean18 not equals to DEFAULT_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldNotBeFound("configVarBoolean18.notEquals=" + DEFAULT_CONFIG_VAR_BOOLEAN_18);

        // Get all the configVariablesList where configVarBoolean18 not equals to UPDATED_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldBeFound("configVarBoolean18.notEquals=" + UPDATED_CONFIG_VAR_BOOLEAN_18);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean18IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean18 in DEFAULT_CONFIG_VAR_BOOLEAN_18 or UPDATED_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldBeFound("configVarBoolean18.in=" + DEFAULT_CONFIG_VAR_BOOLEAN_18 + "," + UPDATED_CONFIG_VAR_BOOLEAN_18);

        // Get all the configVariablesList where configVarBoolean18 equals to UPDATED_CONFIG_VAR_BOOLEAN_18
        defaultConfigVariablesShouldNotBeFound("configVarBoolean18.in=" + UPDATED_CONFIG_VAR_BOOLEAN_18);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarBoolean18IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarBoolean18 is not null
        defaultConfigVariablesShouldBeFound("configVarBoolean18.specified=true");

        // Get all the configVariablesList where configVarBoolean18 is null
        defaultConfigVariablesShouldNotBeFound("configVarBoolean18.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 equals to DEFAULT_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldBeFound("configVarString19.equals=" + DEFAULT_CONFIG_VAR_STRING_19);

        // Get all the configVariablesList where configVarString19 equals to UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldNotBeFound("configVarString19.equals=" + UPDATED_CONFIG_VAR_STRING_19);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 not equals to DEFAULT_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldNotBeFound("configVarString19.notEquals=" + DEFAULT_CONFIG_VAR_STRING_19);

        // Get all the configVariablesList where configVarString19 not equals to UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldBeFound("configVarString19.notEquals=" + UPDATED_CONFIG_VAR_STRING_19);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 in DEFAULT_CONFIG_VAR_STRING_19 or UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldBeFound("configVarString19.in=" + DEFAULT_CONFIG_VAR_STRING_19 + "," + UPDATED_CONFIG_VAR_STRING_19);

        // Get all the configVariablesList where configVarString19 equals to UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldNotBeFound("configVarString19.in=" + UPDATED_CONFIG_VAR_STRING_19);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 is not null
        defaultConfigVariablesShouldBeFound("configVarString19.specified=true");

        // Get all the configVariablesList where configVarString19 is null
        defaultConfigVariablesShouldNotBeFound("configVarString19.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19ContainsSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 contains DEFAULT_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldBeFound("configVarString19.contains=" + DEFAULT_CONFIG_VAR_STRING_19);

        // Get all the configVariablesList where configVarString19 contains UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldNotBeFound("configVarString19.contains=" + UPDATED_CONFIG_VAR_STRING_19);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString19NotContainsSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString19 does not contain DEFAULT_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldNotBeFound("configVarString19.doesNotContain=" + DEFAULT_CONFIG_VAR_STRING_19);

        // Get all the configVariablesList where configVarString19 does not contain UPDATED_CONFIG_VAR_STRING_19
        defaultConfigVariablesShouldBeFound("configVarString19.doesNotContain=" + UPDATED_CONFIG_VAR_STRING_19);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20IsEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 equals to DEFAULT_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldBeFound("configVarString20.equals=" + DEFAULT_CONFIG_VAR_STRING_20);

        // Get all the configVariablesList where configVarString20 equals to UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldNotBeFound("configVarString20.equals=" + UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20IsNotEqualToSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 not equals to DEFAULT_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldNotBeFound("configVarString20.notEquals=" + DEFAULT_CONFIG_VAR_STRING_20);

        // Get all the configVariablesList where configVarString20 not equals to UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldBeFound("configVarString20.notEquals=" + UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20IsInShouldWork() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 in DEFAULT_CONFIG_VAR_STRING_20 or UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldBeFound("configVarString20.in=" + DEFAULT_CONFIG_VAR_STRING_20 + "," + UPDATED_CONFIG_VAR_STRING_20);

        // Get all the configVariablesList where configVarString20 equals to UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldNotBeFound("configVarString20.in=" + UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20IsNullOrNotNull() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 is not null
        defaultConfigVariablesShouldBeFound("configVarString20.specified=true");

        // Get all the configVariablesList where configVarString20 is null
        defaultConfigVariablesShouldNotBeFound("configVarString20.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20ContainsSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 contains DEFAULT_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldBeFound("configVarString20.contains=" + DEFAULT_CONFIG_VAR_STRING_20);

        // Get all the configVariablesList where configVarString20 contains UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldNotBeFound("configVarString20.contains=" + UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void getAllConfigVariablesByConfigVarString20NotContainsSomething() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        // Get all the configVariablesList where configVarString20 does not contain DEFAULT_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldNotBeFound("configVarString20.doesNotContain=" + DEFAULT_CONFIG_VAR_STRING_20);

        // Get all the configVariablesList where configVarString20 does not contain UPDATED_CONFIG_VAR_STRING_20
        defaultConfigVariablesShouldBeFound("configVarString20.doesNotContain=" + UPDATED_CONFIG_VAR_STRING_20);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigVariablesShouldBeFound(String filter) throws Exception {
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configVariables.getId().intValue())))
            .andExpect(jsonPath("$.[*].configVarLong1").value(hasItem(DEFAULT_CONFIG_VAR_LONG_1.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong2").value(hasItem(DEFAULT_CONFIG_VAR_LONG_2.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong3").value(hasItem(DEFAULT_CONFIG_VAR_LONG_3.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong4").value(hasItem(DEFAULT_CONFIG_VAR_LONG_4.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong5").value(hasItem(DEFAULT_CONFIG_VAR_LONG_5.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong6").value(hasItem(DEFAULT_CONFIG_VAR_LONG_6.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong7").value(hasItem(DEFAULT_CONFIG_VAR_LONG_7.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong8").value(hasItem(DEFAULT_CONFIG_VAR_LONG_8.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong9").value(hasItem(DEFAULT_CONFIG_VAR_LONG_9.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong10").value(hasItem(DEFAULT_CONFIG_VAR_LONG_10.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong11").value(hasItem(DEFAULT_CONFIG_VAR_LONG_11.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong12").value(hasItem(DEFAULT_CONFIG_VAR_LONG_12.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong13").value(hasItem(DEFAULT_CONFIG_VAR_LONG_13.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong14").value(hasItem(DEFAULT_CONFIG_VAR_LONG_14.intValue())))
            .andExpect(jsonPath("$.[*].configVarLong15").value(hasItem(DEFAULT_CONFIG_VAR_LONG_15.intValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean16").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_16.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean17").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_17.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarBoolean18").value(hasItem(DEFAULT_CONFIG_VAR_BOOLEAN_18.booleanValue())))
            .andExpect(jsonPath("$.[*].configVarString19").value(hasItem(DEFAULT_CONFIG_VAR_STRING_19)))
            .andExpect(jsonPath("$.[*].configVarString20").value(hasItem(DEFAULT_CONFIG_VAR_STRING_20)));

        // Check, that the count call also returns 1
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigVariablesShouldNotBeFound(String filter) throws Exception {
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigVariablesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConfigVariables() throws Exception {
        // Get the configVariables
        restConfigVariablesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConfigVariables() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();

        // Update the configVariables
        ConfigVariables updatedConfigVariables = configVariablesRepository.findById(configVariables.getId()).get();
        // Disconnect from session so that the updates on updatedConfigVariables are not directly saved in db
        em.detach(updatedConfigVariables);
        updatedConfigVariables
            .configVarLong1(UPDATED_CONFIG_VAR_LONG_1)
            .configVarLong2(UPDATED_CONFIG_VAR_LONG_2)
            .configVarLong3(UPDATED_CONFIG_VAR_LONG_3)
            .configVarLong4(UPDATED_CONFIG_VAR_LONG_4)
            .configVarLong5(UPDATED_CONFIG_VAR_LONG_5)
            .configVarLong6(UPDATED_CONFIG_VAR_LONG_6)
            .configVarLong7(UPDATED_CONFIG_VAR_LONG_7)
            .configVarLong8(UPDATED_CONFIG_VAR_LONG_8)
            .configVarLong9(UPDATED_CONFIG_VAR_LONG_9)
            .configVarLong10(UPDATED_CONFIG_VAR_LONG_10)
            .configVarLong11(UPDATED_CONFIG_VAR_LONG_11)
            .configVarLong12(UPDATED_CONFIG_VAR_LONG_12)
            .configVarLong13(UPDATED_CONFIG_VAR_LONG_13)
            .configVarLong14(UPDATED_CONFIG_VAR_LONG_14)
            .configVarLong15(UPDATED_CONFIG_VAR_LONG_15)
            .configVarBoolean16(UPDATED_CONFIG_VAR_BOOLEAN_16)
            .configVarBoolean17(UPDATED_CONFIG_VAR_BOOLEAN_17)
            .configVarBoolean18(UPDATED_CONFIG_VAR_BOOLEAN_18)
            .configVarString19(UPDATED_CONFIG_VAR_STRING_19)
            .configVarString20(UPDATED_CONFIG_VAR_STRING_20);
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(updatedConfigVariables);

        restConfigVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configVariablesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
        ConfigVariables testConfigVariables = configVariablesList.get(configVariablesList.size() - 1);
        assertThat(testConfigVariables.getConfigVarLong1()).isEqualTo(UPDATED_CONFIG_VAR_LONG_1);
        assertThat(testConfigVariables.getConfigVarLong2()).isEqualTo(UPDATED_CONFIG_VAR_LONG_2);
        assertThat(testConfigVariables.getConfigVarLong3()).isEqualTo(UPDATED_CONFIG_VAR_LONG_3);
        assertThat(testConfigVariables.getConfigVarLong4()).isEqualTo(UPDATED_CONFIG_VAR_LONG_4);
        assertThat(testConfigVariables.getConfigVarLong5()).isEqualTo(UPDATED_CONFIG_VAR_LONG_5);
        assertThat(testConfigVariables.getConfigVarLong6()).isEqualTo(UPDATED_CONFIG_VAR_LONG_6);
        assertThat(testConfigVariables.getConfigVarLong7()).isEqualTo(UPDATED_CONFIG_VAR_LONG_7);
        assertThat(testConfigVariables.getConfigVarLong8()).isEqualTo(UPDATED_CONFIG_VAR_LONG_8);
        assertThat(testConfigVariables.getConfigVarLong9()).isEqualTo(UPDATED_CONFIG_VAR_LONG_9);
        assertThat(testConfigVariables.getConfigVarLong10()).isEqualTo(UPDATED_CONFIG_VAR_LONG_10);
        assertThat(testConfigVariables.getConfigVarLong11()).isEqualTo(UPDATED_CONFIG_VAR_LONG_11);
        assertThat(testConfigVariables.getConfigVarLong12()).isEqualTo(UPDATED_CONFIG_VAR_LONG_12);
        assertThat(testConfigVariables.getConfigVarLong13()).isEqualTo(UPDATED_CONFIG_VAR_LONG_13);
        assertThat(testConfigVariables.getConfigVarLong14()).isEqualTo(UPDATED_CONFIG_VAR_LONG_14);
        assertThat(testConfigVariables.getConfigVarLong15()).isEqualTo(UPDATED_CONFIG_VAR_LONG_15);
        assertThat(testConfigVariables.getConfigVarBoolean16()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_16);
        assertThat(testConfigVariables.getConfigVarBoolean17()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_17);
        assertThat(testConfigVariables.getConfigVarBoolean18()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_18);
        assertThat(testConfigVariables.getConfigVarString19()).isEqualTo(UPDATED_CONFIG_VAR_STRING_19);
        assertThat(testConfigVariables.getConfigVarString20()).isEqualTo(UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void putNonExistingConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configVariablesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigVariablesWithPatch() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();

        // Update the configVariables using partial update
        ConfigVariables partialUpdatedConfigVariables = new ConfigVariables();
        partialUpdatedConfigVariables.setId(configVariables.getId());

        partialUpdatedConfigVariables
            .configVarLong1(UPDATED_CONFIG_VAR_LONG_1)
            .configVarLong3(UPDATED_CONFIG_VAR_LONG_3)
            .configVarLong4(UPDATED_CONFIG_VAR_LONG_4)
            .configVarLong5(UPDATED_CONFIG_VAR_LONG_5)
            .configVarLong6(UPDATED_CONFIG_VAR_LONG_6)
            .configVarLong7(UPDATED_CONFIG_VAR_LONG_7)
            .configVarLong8(UPDATED_CONFIG_VAR_LONG_8)
            .configVarLong10(UPDATED_CONFIG_VAR_LONG_10)
            .configVarLong12(UPDATED_CONFIG_VAR_LONG_12)
            .configVarBoolean16(UPDATED_CONFIG_VAR_BOOLEAN_16)
            .configVarBoolean17(UPDATED_CONFIG_VAR_BOOLEAN_17)
            .configVarBoolean18(UPDATED_CONFIG_VAR_BOOLEAN_18)
            .configVarString19(UPDATED_CONFIG_VAR_STRING_19)
            .configVarString20(UPDATED_CONFIG_VAR_STRING_20);

        restConfigVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigVariables.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigVariables))
            )
            .andExpect(status().isOk());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
        ConfigVariables testConfigVariables = configVariablesList.get(configVariablesList.size() - 1);
        assertThat(testConfigVariables.getConfigVarLong1()).isEqualTo(UPDATED_CONFIG_VAR_LONG_1);
        assertThat(testConfigVariables.getConfigVarLong2()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_2);
        assertThat(testConfigVariables.getConfigVarLong3()).isEqualTo(UPDATED_CONFIG_VAR_LONG_3);
        assertThat(testConfigVariables.getConfigVarLong4()).isEqualTo(UPDATED_CONFIG_VAR_LONG_4);
        assertThat(testConfigVariables.getConfigVarLong5()).isEqualTo(UPDATED_CONFIG_VAR_LONG_5);
        assertThat(testConfigVariables.getConfigVarLong6()).isEqualTo(UPDATED_CONFIG_VAR_LONG_6);
        assertThat(testConfigVariables.getConfigVarLong7()).isEqualTo(UPDATED_CONFIG_VAR_LONG_7);
        assertThat(testConfigVariables.getConfigVarLong8()).isEqualTo(UPDATED_CONFIG_VAR_LONG_8);
        assertThat(testConfigVariables.getConfigVarLong9()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_9);
        assertThat(testConfigVariables.getConfigVarLong10()).isEqualTo(UPDATED_CONFIG_VAR_LONG_10);
        assertThat(testConfigVariables.getConfigVarLong11()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_11);
        assertThat(testConfigVariables.getConfigVarLong12()).isEqualTo(UPDATED_CONFIG_VAR_LONG_12);
        assertThat(testConfigVariables.getConfigVarLong13()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_13);
        assertThat(testConfigVariables.getConfigVarLong14()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_14);
        assertThat(testConfigVariables.getConfigVarLong15()).isEqualTo(DEFAULT_CONFIG_VAR_LONG_15);
        assertThat(testConfigVariables.getConfigVarBoolean16()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_16);
        assertThat(testConfigVariables.getConfigVarBoolean17()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_17);
        assertThat(testConfigVariables.getConfigVarBoolean18()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_18);
        assertThat(testConfigVariables.getConfigVarString19()).isEqualTo(UPDATED_CONFIG_VAR_STRING_19);
        assertThat(testConfigVariables.getConfigVarString20()).isEqualTo(UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void fullUpdateConfigVariablesWithPatch() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();

        // Update the configVariables using partial update
        ConfigVariables partialUpdatedConfigVariables = new ConfigVariables();
        partialUpdatedConfigVariables.setId(configVariables.getId());

        partialUpdatedConfigVariables
            .configVarLong1(UPDATED_CONFIG_VAR_LONG_1)
            .configVarLong2(UPDATED_CONFIG_VAR_LONG_2)
            .configVarLong3(UPDATED_CONFIG_VAR_LONG_3)
            .configVarLong4(UPDATED_CONFIG_VAR_LONG_4)
            .configVarLong5(UPDATED_CONFIG_VAR_LONG_5)
            .configVarLong6(UPDATED_CONFIG_VAR_LONG_6)
            .configVarLong7(UPDATED_CONFIG_VAR_LONG_7)
            .configVarLong8(UPDATED_CONFIG_VAR_LONG_8)
            .configVarLong9(UPDATED_CONFIG_VAR_LONG_9)
            .configVarLong10(UPDATED_CONFIG_VAR_LONG_10)
            .configVarLong11(UPDATED_CONFIG_VAR_LONG_11)
            .configVarLong12(UPDATED_CONFIG_VAR_LONG_12)
            .configVarLong13(UPDATED_CONFIG_VAR_LONG_13)
            .configVarLong14(UPDATED_CONFIG_VAR_LONG_14)
            .configVarLong15(UPDATED_CONFIG_VAR_LONG_15)
            .configVarBoolean16(UPDATED_CONFIG_VAR_BOOLEAN_16)
            .configVarBoolean17(UPDATED_CONFIG_VAR_BOOLEAN_17)
            .configVarBoolean18(UPDATED_CONFIG_VAR_BOOLEAN_18)
            .configVarString19(UPDATED_CONFIG_VAR_STRING_19)
            .configVarString20(UPDATED_CONFIG_VAR_STRING_20);

        restConfigVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigVariables.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigVariables))
            )
            .andExpect(status().isOk());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
        ConfigVariables testConfigVariables = configVariablesList.get(configVariablesList.size() - 1);
        assertThat(testConfigVariables.getConfigVarLong1()).isEqualTo(UPDATED_CONFIG_VAR_LONG_1);
        assertThat(testConfigVariables.getConfigVarLong2()).isEqualTo(UPDATED_CONFIG_VAR_LONG_2);
        assertThat(testConfigVariables.getConfigVarLong3()).isEqualTo(UPDATED_CONFIG_VAR_LONG_3);
        assertThat(testConfigVariables.getConfigVarLong4()).isEqualTo(UPDATED_CONFIG_VAR_LONG_4);
        assertThat(testConfigVariables.getConfigVarLong5()).isEqualTo(UPDATED_CONFIG_VAR_LONG_5);
        assertThat(testConfigVariables.getConfigVarLong6()).isEqualTo(UPDATED_CONFIG_VAR_LONG_6);
        assertThat(testConfigVariables.getConfigVarLong7()).isEqualTo(UPDATED_CONFIG_VAR_LONG_7);
        assertThat(testConfigVariables.getConfigVarLong8()).isEqualTo(UPDATED_CONFIG_VAR_LONG_8);
        assertThat(testConfigVariables.getConfigVarLong9()).isEqualTo(UPDATED_CONFIG_VAR_LONG_9);
        assertThat(testConfigVariables.getConfigVarLong10()).isEqualTo(UPDATED_CONFIG_VAR_LONG_10);
        assertThat(testConfigVariables.getConfigVarLong11()).isEqualTo(UPDATED_CONFIG_VAR_LONG_11);
        assertThat(testConfigVariables.getConfigVarLong12()).isEqualTo(UPDATED_CONFIG_VAR_LONG_12);
        assertThat(testConfigVariables.getConfigVarLong13()).isEqualTo(UPDATED_CONFIG_VAR_LONG_13);
        assertThat(testConfigVariables.getConfigVarLong14()).isEqualTo(UPDATED_CONFIG_VAR_LONG_14);
        assertThat(testConfigVariables.getConfigVarLong15()).isEqualTo(UPDATED_CONFIG_VAR_LONG_15);
        assertThat(testConfigVariables.getConfigVarBoolean16()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_16);
        assertThat(testConfigVariables.getConfigVarBoolean17()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_17);
        assertThat(testConfigVariables.getConfigVarBoolean18()).isEqualTo(UPDATED_CONFIG_VAR_BOOLEAN_18);
        assertThat(testConfigVariables.getConfigVarString19()).isEqualTo(UPDATED_CONFIG_VAR_STRING_19);
        assertThat(testConfigVariables.getConfigVarString20()).isEqualTo(UPDATED_CONFIG_VAR_STRING_20);
    }

    @Test
    @Transactional
    void patchNonExistingConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configVariablesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigVariables() throws Exception {
        int databaseSizeBeforeUpdate = configVariablesRepository.findAll().size();
        configVariables.setId(count.incrementAndGet());

        // Create the ConfigVariables
        ConfigVariablesDTO configVariablesDTO = configVariablesMapper.toDto(configVariables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configVariablesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigVariables in the database
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigVariables() throws Exception {
        // Initialize the database
        configVariablesRepository.saveAndFlush(configVariables);

        int databaseSizeBeforeDelete = configVariablesRepository.findAll().size();

        // Delete the configVariables
        restConfigVariablesMockMvc
            .perform(delete(ENTITY_API_URL_ID, configVariables.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigVariables> configVariablesList = configVariablesRepository.findAll();
        assertThat(configVariablesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
