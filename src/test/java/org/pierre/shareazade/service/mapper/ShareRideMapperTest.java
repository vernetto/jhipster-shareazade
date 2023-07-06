package org.pierre.shareazade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShareRideMapperTest {

    private ShareRideMapper shareRideMapper;

    @BeforeEach
    public void setUp() {
        shareRideMapper = new ShareRideMapperImpl();
    }
}
