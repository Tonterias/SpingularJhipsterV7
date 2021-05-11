package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CelebTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Celeb.class);
        Celeb celeb1 = new Celeb();
        celeb1.setId(1L);
        Celeb celeb2 = new Celeb();
        celeb2.setId(celeb1.getId());
        assertThat(celeb1).isEqualTo(celeb2);
        celeb2.setId(2L);
        assertThat(celeb1).isNotEqualTo(celeb2);
        celeb1.setId(null);
        assertThat(celeb1).isNotEqualTo(celeb2);
    }
}
