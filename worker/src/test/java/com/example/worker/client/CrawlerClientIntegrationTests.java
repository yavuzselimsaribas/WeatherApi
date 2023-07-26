package com.example.worker.client;


import com.example.common.dto.CityDto;
import com.example.common.dto.CoordinateDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Disabled
public class CrawlerClientIntegrationTests {

    @Autowired
    private ICrawlerClient crawlerClient;


    @Test
    public void fetchGeocodingData_success()
    {
        CoordinateDto dto = crawlerClient.fetchGeocodingData("London");
        assertThat(dto.getName()).isEqualTo("London");
        assertThat(dto.getLat()).isEqualTo(51.5073219);
        assertThat(dto.getLon()).isEqualTo(-0.1276474);
    }

    @Test
    public void fetchHistoricalCityAirQuality()
    {
        List<CityDto> dto = crawlerClient.fetchHistoricalCityAirQuality("London", 51.5073219, -0.1276474, 1621346400L, 1621346400L, "TestRequest");
        assertThat(dto.size()).isEqualTo(1);
        assertThat(dto.get(0).getCityName()).isEqualTo("London");
        assertThat(dto.get(0).getCo()).isNotNull();
        assertThat(dto.get(0).getO3()).isNotNull();
        assertThat(dto.get(0).getSo2()).isNotNull();
        assertThat(dto.get(0).getRequestId()).isEqualTo("TestRequest");
    }
}
