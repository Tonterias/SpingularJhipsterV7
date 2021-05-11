package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CcelebTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cceleb.class);
        Cceleb cceleb1 = new Cceleb();
        cceleb1.setId(1L);
        Cceleb cceleb2 = new Cceleb();
        cceleb2.setId(cceleb1.getId());
        assertThat(cceleb1).isEqualTo(cceleb2);
        cceleb2.setId(2L);
        assertThat(cceleb1).isNotEqualTo(cceleb2);
        cceleb1.setId(null);
        assertThat(cceleb1).isNotEqualTo(cceleb2);
    }
}
