package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppuserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppuserDTO.class);
        AppuserDTO appuserDTO1 = new AppuserDTO();
        appuserDTO1.setId(1L);
        AppuserDTO appuserDTO2 = new AppuserDTO();
        assertThat(appuserDTO1).isNotEqualTo(appuserDTO2);
        appuserDTO2.setId(appuserDTO1.getId());
        assertThat(appuserDTO1).isEqualTo(appuserDTO2);
        appuserDTO2.setId(2L);
        assertThat(appuserDTO1).isNotEqualTo(appuserDTO2);
        appuserDTO1.setId(null);
        assertThat(appuserDTO1).isNotEqualTo(appuserDTO2);
    }
}
