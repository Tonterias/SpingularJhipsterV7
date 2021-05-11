package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CactivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cactivity.class);
        Cactivity cactivity1 = new Cactivity();
        cactivity1.setId(1L);
        Cactivity cactivity2 = new Cactivity();
        cactivity2.setId(cactivity1.getId());
        assertThat(cactivity1).isEqualTo(cactivity2);
        cactivity2.setId(2L);
        assertThat(cactivity1).isNotEqualTo(cactivity2);
        cactivity1.setId(null);
        assertThat(cactivity1).isNotEqualTo(cactivity2);
    }
}
