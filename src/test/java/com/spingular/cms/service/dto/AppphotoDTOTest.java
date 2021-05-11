package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppphotoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppphotoDTO.class);
        AppphotoDTO appphotoDTO1 = new AppphotoDTO();
        appphotoDTO1.setId(1L);
        AppphotoDTO appphotoDTO2 = new AppphotoDTO();
        assertThat(appphotoDTO1).isNotEqualTo(appphotoDTO2);
        appphotoDTO2.setId(appphotoDTO1.getId());
        assertThat(appphotoDTO1).isEqualTo(appphotoDTO2);
        appphotoDTO2.setId(2L);
        assertThat(appphotoDTO1).isNotEqualTo(appphotoDTO2);
        appphotoDTO1.setId(null);
        assertThat(appphotoDTO1).isNotEqualTo(appphotoDTO2);
    }
}
