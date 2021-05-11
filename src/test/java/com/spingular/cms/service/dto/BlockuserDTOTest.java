package com.spingular.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockuserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockuserDTO.class);
        BlockuserDTO blockuserDTO1 = new BlockuserDTO();
        blockuserDTO1.setId(1L);
        BlockuserDTO blockuserDTO2 = new BlockuserDTO();
        assertThat(blockuserDTO1).isNotEqualTo(blockuserDTO2);
        blockuserDTO2.setId(blockuserDTO1.getId());
        assertThat(blockuserDTO1).isEqualTo(blockuserDTO2);
        blockuserDTO2.setId(2L);
        assertThat(blockuserDTO1).isNotEqualTo(blockuserDTO2);
        blockuserDTO1.setId(null);
        assertThat(blockuserDTO1).isNotEqualTo(blockuserDTO2);
    }
}
