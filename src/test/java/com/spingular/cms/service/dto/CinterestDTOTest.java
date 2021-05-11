package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CinterestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CinterestDTO.class);
        CinterestDTO cinterestDTO1 = new CinterestDTO();
        cinterestDTO1.setId(1L);
        CinterestDTO cinterestDTO2 = new CinterestDTO();
        assertThat(cinterestDTO1).isNotEqualTo(cinterestDTO2);
        cinterestDTO2.setId(cinterestDTO1.getId());
        assertThat(cinterestDTO1).isEqualTo(cinterestDTO2);
        cinterestDTO2.setId(2L);
        assertThat(cinterestDTO1).isNotEqualTo(cinterestDTO2);
        cinterestDTO1.setId(null);
        assertThat(cinterestDTO1).isNotEqualTo(cinterestDTO2);
    }
}
