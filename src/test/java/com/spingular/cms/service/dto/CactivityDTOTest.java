package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CactivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CactivityDTO.class);
        CactivityDTO cactivityDTO1 = new CactivityDTO();
        cactivityDTO1.setId(1L);
        CactivityDTO cactivityDTO2 = new CactivityDTO();
        assertThat(cactivityDTO1).isNotEqualTo(cactivityDTO2);
        cactivityDTO2.setId(cactivityDTO1.getId());
        assertThat(cactivityDTO1).isEqualTo(cactivityDTO2);
        cactivityDTO2.setId(2L);
        assertThat(cactivityDTO1).isNotEqualTo(cactivityDTO2);
        cactivityDTO1.setId(null);
        assertThat(cactivityDTO1).isNotEqualTo(cactivityDTO2);
    }
}
