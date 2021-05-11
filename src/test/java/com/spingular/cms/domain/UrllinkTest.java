package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrllinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Urllink.class);
        Urllink urllink1 = new Urllink();
        urllink1.setId(1L);
        Urllink urllink2 = new Urllink();
        urllink2.setId(urllink1.getId());
        assertThat(urllink1).isEqualTo(urllink2);
        urllink2.setId(2L);
        assertThat(urllink1).isNotEqualTo(urllink2);
        urllink1.setId(null);
        assertThat(urllink1).isNotEqualTo(urllink2);
    }
}
