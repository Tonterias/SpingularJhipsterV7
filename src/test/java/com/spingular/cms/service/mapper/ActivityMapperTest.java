package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityMapperTest {

    private ActivityMapper activityMapper;

    @BeforeEach
    public void setUp() {
        activityMapper = new ActivityMapperImpl();
    }
}
