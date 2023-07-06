package org.pierre.shareazade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareCityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareCity.class);
        ShareCity shareCity1 = new ShareCity();
        shareCity1.setId(1L);
        ShareCity shareCity2 = new ShareCity();
        shareCity2.setId(shareCity1.getId());
        assertThat(shareCity1).isEqualTo(shareCity2);
        shareCity2.setId(2L);
        assertThat(shareCity1).isNotEqualTo(shareCity2);
        shareCity1.setId(null);
        assertThat(shareCity1).isNotEqualTo(shareCity2);
    }
}
