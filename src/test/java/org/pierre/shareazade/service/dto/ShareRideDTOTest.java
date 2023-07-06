package org.pierre.shareazade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareRideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareRideDTO.class);
        ShareRideDTO shareRideDTO1 = new ShareRideDTO();
        shareRideDTO1.setId(1L);
        ShareRideDTO shareRideDTO2 = new ShareRideDTO();
        assertThat(shareRideDTO1).isNotEqualTo(shareRideDTO2);
        shareRideDTO2.setId(shareRideDTO1.getId());
        assertThat(shareRideDTO1).isEqualTo(shareRideDTO2);
        shareRideDTO2.setId(2L);
        assertThat(shareRideDTO1).isNotEqualTo(shareRideDTO2);
        shareRideDTO1.setId(null);
        assertThat(shareRideDTO1).isNotEqualTo(shareRideDTO2);
    }
}
