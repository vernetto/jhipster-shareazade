package org.pierre.shareazade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareRideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareRide.class);
        ShareRide shareRide1 = new ShareRide();
        shareRide1.setId(1L);
        ShareRide shareRide2 = new ShareRide();
        shareRide2.setId(shareRide1.getId());
        assertThat(shareRide1).isEqualTo(shareRide2);
        shareRide2.setId(2L);
        assertThat(shareRide1).isNotEqualTo(shareRide2);
        shareRide1.setId(null);
        assertThat(shareRide1).isNotEqualTo(shareRide2);
    }
}
