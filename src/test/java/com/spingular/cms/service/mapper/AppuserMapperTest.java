package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppuserMapperTest {

    private AppuserMapper appuserMapper;

    @BeforeEach
    public void setUp() {
        appuserMapper = new AppuserMapperImpl();
    }
}
