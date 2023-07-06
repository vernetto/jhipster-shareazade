package org.pierre.shareazade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class RideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideDTO.class);
        RideDTO rideDTO1 = new RideDTO();
        rideDTO1.setId(1L);
        RideDTO rideDTO2 = new RideDTO();
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO2.setId(rideDTO1.getId());
        assertThat(rideDTO1).isEqualTo(rideDTO2);
        rideDTO2.setId(2L);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO1.setId(null);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
    }
}
