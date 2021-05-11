package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrontpageconfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrontpageconfigDTO.class);
        FrontpageconfigDTO frontpageconfigDTO1 = new FrontpageconfigDTO();
        frontpageconfigDTO1.setId(1L);
        FrontpageconfigDTO frontpageconfigDTO2 = new FrontpageconfigDTO();
        assertThat(frontpageconfigDTO1).isNotEqualTo(frontpageconfigDTO2);
        frontpageconfigDTO2.setId(frontpageconfigDTO1.getId());
        assertThat(frontpageconfigDTO1).isEqualTo(frontpageconfigDTO2);
        frontpageconfigDTO2.setId(2L);
        assertThat(frontpageconfigDTO1).isNotEqualTo(frontpageconfigDTO2);
        frontpageconfigDTO1.setId(null);
        assertThat(frontpageconfigDTO1).isNotEqualTo(frontpageconfigDTO2);
    }
}
