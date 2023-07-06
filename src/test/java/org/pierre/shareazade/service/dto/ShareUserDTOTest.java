package org.pierre.shareazade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.pierre.shareazade.web.rest.TestUtil;

class ShareUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareUserDTO.class);
        ShareUserDTO shareUserDTO1 = new ShareUserDTO();
        shareUserDTO1.setId(1L);
        ShareUserDTO shareUserDTO2 = new ShareUserDTO();
        assertThat(shareUserDTO1).isNotEqualTo(shareUserDTO2);
        shareUserDTO2.setId(shareUserDTO1.getId());
        assertThat(shareUserDTO1).isEqualTo(shareUserDTO2);
        shareUserDTO2.setId(2L);
        assertThat(shareUserDTO1).isNotEqualTo(shareUserDTO2);
        shareUserDTO1.setId(null);
        assertThat(shareUserDTO1).isNotEqualTo(shareUserDTO2);
    }
}
