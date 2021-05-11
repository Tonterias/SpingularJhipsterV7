package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrontpageconfigMapperTest {

    private FrontpageconfigMapper frontpageconfigMapper;

    @BeforeEach
    public void setUp() {
        frontpageconfigMapper = new FrontpageconfigMapperImpl();
    }
}
