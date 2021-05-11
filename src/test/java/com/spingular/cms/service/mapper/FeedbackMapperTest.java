package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackMapperTest {

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    public void setUp() {
        feedbackMapper = new FeedbackMapperImpl();
    }
}
