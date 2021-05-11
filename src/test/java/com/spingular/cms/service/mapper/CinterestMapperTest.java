package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinterestMapperTest {

    private CinterestMapper cinterestMapper;

    @BeforeEach
    public void setUp() {
        cinterestMapper = new CinterestMapperImpl();
    }
}
