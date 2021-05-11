package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FollowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowDTO.class);
        FollowDTO followDTO1 = new FollowDTO();
        followDTO1.setId(1L);
        FollowDTO followDTO2 = new FollowDTO();
        assertThat(followDTO1).isNotEqualTo(followDTO2);
        followDTO2.setId(followDTO1.getId());
        assertThat(followDTO1).isEqualTo(followDTO2);
        followDTO2.setId(2L);
        assertThat(followDTO1).isNotEqualTo(followDTO2);
        followDTO1.setId(null);
        assertThat(followDTO1).isNotEqualTo(followDTO2);
    }
}
