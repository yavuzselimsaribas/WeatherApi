package com.example.web.service.coordinate;


import com.example.web.model.CityCoordinates;
import com.example.web.repository.ICoordinateRepository;
import com.example.web.service.city.ICityQueueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DataMongoTest
public class CoordinateServiceTests {

    @Autowired
    private ICoordinateService coordinateService;

    @Autowired
    private ICoordinateRepository coordinateRepository;

    @Mock(name = "coordinateRequestQueueTemplate")
    private AmqpTemplate coordinateRequestQueueTemplate;

    @Mock(name = "coordinateResultQueueTemplate")
    private AmqpTemplate coordinateResultQueueTemplate;


    @Mock
    private ICityQueueService cityQueueService;

    @Test
    public void getCityCoordinates_success()
    {
        CityCoordinates cityCoordinates = CityCoordinates.builder()
                .cityName("Test")
                .lat(10.0)
                .lon(20.0)
                .build();
        coordinateService.saveCityCoordinate(cityCoordinates);
        assertThat(coordinateRepository.findByCityName("Test")).isNotNull();
    }

    @Test
    public void saveCityCoordinate()
    {
        CityCoordinates cityCoordinates = CityCoordinates.builder()
                .cityName("Test1")
                .lat(10.0)
                .lon(20.0)
                .build();
        coordinateService.saveCityCoordinate(cityCoordinates);
        assertThat(coordinateRepository.findByCityName("Test1")).isNotNull();
    }

    @Test
    public void fetchGeocodingInfo()
    {
        String cityName = "London";
        coordinateService.fetchGeocodingInfo(cityName);
        assertThat(coordinateRepository.findByCityName(cityName)).isNotNull();
    }
}
