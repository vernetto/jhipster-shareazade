package org.pierre.shareazade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RideMapperTest {

    private RideMapper rideMapper;

    @BeforeEach
    public void setUp() {
        rideMapper = new RideMapperImpl();
    }
}
