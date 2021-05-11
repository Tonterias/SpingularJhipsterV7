package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterestDTO.class);
        InterestDTO interestDTO1 = new InterestDTO();
        interestDTO1.setId(1L);
        InterestDTO interestDTO2 = new InterestDTO();
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
        interestDTO2.setId(interestDTO1.getId());
        assertThat(interestDTO1).isEqualTo(interestDTO2);
        interestDTO2.setId(2L);
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
        interestDTO1.setId(null);
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
    }
}
