package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppphotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appphoto.class);
        Appphoto appphoto1 = new Appphoto();
        appphoto1.setId(1L);
        Appphoto appphoto2 = new Appphoto();
        appphoto2.setId(appphoto1.getId());
        assertThat(appphoto1).isEqualTo(appphoto2);
        appphoto2.setId(2L);
        assertThat(appphoto1).isNotEqualTo(appphoto2);
        appphoto1.setId(null);
        assertThat(appphoto1).isNotEqualTo(appphoto2);
    }
}
