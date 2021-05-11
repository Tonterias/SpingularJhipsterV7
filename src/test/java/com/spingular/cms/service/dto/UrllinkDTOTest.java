package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrllinkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrllinkDTO.class);
        UrllinkDTO urllinkDTO1 = new UrllinkDTO();
        urllinkDTO1.setId(1L);
        UrllinkDTO urllinkDTO2 = new UrllinkDTO();
        assertThat(urllinkDTO1).isNotEqualTo(urllinkDTO2);
        urllinkDTO2.setId(urllinkDTO1.getId());
        assertThat(urllinkDTO1).isEqualTo(urllinkDTO2);
        urllinkDTO2.setId(2L);
        assertThat(urllinkDTO1).isNotEqualTo(urllinkDTO2);
        urllinkDTO1.setId(null);
        assertThat(urllinkDTO1).isNotEqualTo(urllinkDTO2);
    }
}
