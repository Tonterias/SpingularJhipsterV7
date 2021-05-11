package com.spingular.cms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.spingular.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        Feedback feedback2 = new Feedback();
        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);
        feedback2.setId(2L);
        assertThat(feedback1).isNotEqualTo(feedback2);
        feedback1.setId(null);
        assertThat(feedback1).isNotEqualTo(feedback2);
    }
}
