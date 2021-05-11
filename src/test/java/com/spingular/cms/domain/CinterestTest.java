package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CinterestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cinterest.class);
        Cinterest cinterest1 = new Cinterest();
        cinterest1.setId(1L);
        Cinterest cinterest2 = new Cinterest();
        cinterest2.setId(cinterest1.getId());
        assertThat(cinterest1).isEqualTo(cinterest2);
        cinterest2.setId(2L);
        assertThat(cinterest1).isNotEqualTo(cinterest2);
        cinterest1.setId(null);
        assertThat(cinterest1).isNotEqualTo(cinterest2);
    }
}
