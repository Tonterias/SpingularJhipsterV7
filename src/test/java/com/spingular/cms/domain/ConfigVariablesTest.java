package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigVariablesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigVariables.class);
        ConfigVariables configVariables1 = new ConfigVariables();
        configVariables1.setId(1L);
        ConfigVariables configVariables2 = new ConfigVariables();
        configVariables2.setId(configVariables1.getId());
        assertThat(configVariables1).isEqualTo(configVariables2);
        configVariables2.setId(2L);
        assertThat(configVariables1).isNotEqualTo(configVariables2);
        configVariables1.setId(null);
        assertThat(configVariables1).isNotEqualTo(configVariables2);
    }
}
