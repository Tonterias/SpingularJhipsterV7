package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockuserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Blockuser.class);
        Blockuser blockuser1 = new Blockuser();
        blockuser1.setId(1L);
        Blockuser blockuser2 = new Blockuser();
        blockuser2.setId(blockuser1.getId());
        assertThat(blockuser1).isEqualTo(blockuser2);
        blockuser2.setId(2L);
        assertThat(blockuser1).isNotEqualTo(blockuser2);
        blockuser1.setId(null);
        assertThat(blockuser1).isNotEqualTo(blockuser2);
    }
}
