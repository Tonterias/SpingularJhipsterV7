package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrontpageconfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frontpageconfig.class);
        Frontpageconfig frontpageconfig1 = new Frontpageconfig();
        frontpageconfig1.setId(1L);
        Frontpageconfig frontpageconfig2 = new Frontpageconfig();
        frontpageconfig2.setId(frontpageconfig1.getId());
        assertThat(frontpageconfig1).isEqualTo(frontpageconfig2);
        frontpageconfig2.setId(2L);
        assertThat(frontpageconfig1).isNotEqualTo(frontpageconfig2);
        frontpageconfig1.setId(null);
        assertThat(frontpageconfig1).isNotEqualTo(frontpageconfig2);
    }
}
