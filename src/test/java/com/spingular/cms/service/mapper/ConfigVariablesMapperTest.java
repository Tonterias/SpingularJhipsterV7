package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigVariablesMapperTest {

    private ConfigVariablesMapper configVariablesMapper;

    @BeforeEach
    public void setUp() {
        configVariablesMapper = new ConfigVariablesMapperImpl();
    }
}
