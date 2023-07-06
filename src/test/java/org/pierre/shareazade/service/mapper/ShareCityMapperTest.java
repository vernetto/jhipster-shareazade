package org.pierre.shareazade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShareCityMapperTest {

    private ShareCityMapper shareCityMapper;

    @BeforeEach
    public void setUp() {
        shareCityMapper = new ShareCityMapperImpl();
    }
}
