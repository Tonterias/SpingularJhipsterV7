package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CcelebMapperTest {

    private CcelebMapper ccelebMapper;

    @BeforeEach
    public void setUp() {
        ccelebMapper = new CcelebMapperImpl();
    }
}
