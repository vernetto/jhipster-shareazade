package org.pierre.shareazade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareCityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareCityDTO.class);
        ShareCityDTO shareCityDTO1 = new ShareCityDTO();
        shareCityDTO1.setId(1L);
        ShareCityDTO shareCityDTO2 = new ShareCityDTO();
        assertThat(shareCityDTO1).isNotEqualTo(shareCityDTO2);
        shareCityDTO2.setId(shareCityDTO1.getId());
        assertThat(shareCityDTO1).isEqualTo(shareCityDTO2);
        shareCityDTO2.setId(2L);
        assertThat(shareCityDTO1).isNotEqualTo(shareCityDTO2);
        shareCityDTO1.setId(null);
        assertThat(shareCityDTO1).isNotEqualTo(shareCityDTO2);
    }
}
