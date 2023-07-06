package org.pierre.shareazade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShareUserMapperTest {

    private ShareUserMapper shareUserMapper;

    @BeforeEach
    public void setUp() {
        shareUserMapper = new ShareUserMapperImpl();
    }
}
