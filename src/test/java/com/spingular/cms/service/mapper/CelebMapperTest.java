package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CelebMapperTest {

    private CelebMapper celebMapper;

    @BeforeEach
    public void setUp() {
        celebMapper = new CelebMapperImpl();
    }
}
