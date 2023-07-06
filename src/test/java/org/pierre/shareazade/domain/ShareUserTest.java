package org.pierre.shareazade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareUser.class);
        ShareUser shareUser1 = new ShareUser();
        shareUser1.setId(1L);
        ShareUser shareUser2 = new ShareUser();
        shareUser2.setId(shareUser1.getId());
        assertThat(shareUser1).isEqualTo(shareUser2);
        shareUser2.setId(2L);
        assertThat(shareUser1).isNotEqualTo(shareUser2);
        shareUser1.setId(null);
        assertThat(shareUser1).isNotEqualTo(shareUser2);
    }
}
