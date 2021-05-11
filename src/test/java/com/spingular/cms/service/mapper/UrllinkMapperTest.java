package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrllinkMapperTest {

    private UrllinkMapper urllinkMapper;

    @BeforeEach
    public void setUp() {
        urllinkMapper = new UrllinkMapperImpl();
    }
}
