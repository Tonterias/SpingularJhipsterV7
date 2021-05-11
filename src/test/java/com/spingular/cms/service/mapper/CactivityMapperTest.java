package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CactivityMapperTest {

    private CactivityMapper cactivityMapper;

    @BeforeEach
    public void setUp() {
        cactivityMapper = new CactivityMapperImpl();
    }
}
