package com.spingular.cms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommunityMapperTest {

    private CommunityMapper communityMapper;

    @BeforeEach
    public void setUp() {
        communityMapper = new CommunityMapperImpl();
    }
}
