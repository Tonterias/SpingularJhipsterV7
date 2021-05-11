package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppuserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appuser.class);
        Appuser appuser1 = new Appuser();
        appuser1.setId(1L);
        Appuser appuser2 = new Appuser();
        appuser2.setId(appuser1.getId());
        assertThat(appuser1).isEqualTo(appuser2);
        appuser2.setId(2L);
        assertThat(appuser1).isNotEqualTo(appuser2);
        appuser1.setId(null);
        assertThat(appuser1).isNotEqualTo(appuser2);
    }
}
