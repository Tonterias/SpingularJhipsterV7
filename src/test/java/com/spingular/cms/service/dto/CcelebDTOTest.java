package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CcelebDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CcelebDTO.class);
        CcelebDTO ccelebDTO1 = new CcelebDTO();
        ccelebDTO1.setId(1L);
        CcelebDTO ccelebDTO2 = new CcelebDTO();
        assertThat(ccelebDTO1).isNotEqualTo(ccelebDTO2);
        ccelebDTO2.setId(ccelebDTO1.getId());
        assertThat(ccelebDTO1).isEqualTo(ccelebDTO2);
        ccelebDTO2.setId(2L);
        assertThat(ccelebDTO1).isNotEqualTo(ccelebDTO2);
        ccelebDTO1.setId(null);
        assertThat(ccelebDTO1).isNotEqualTo(ccelebDTO2);
    }
}
