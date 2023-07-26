package com.example.web.service.city;


import com.example.common.dto.IUnixToDateConverter;
import com.example.web.model.*;
import com.example.web.repository.ICityRepository;
import com.example.web.service.coordinate.ICoordinateService;
import com.example.web.service.request.IRequestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataMongoTest
public class CityServiceTests {

    @Autowired
    private ICityService cityService;

    @Mock
    private ICityRepository cityRepository;

    @Autowired
    private IRequestService requestService;

    @Mock
    private ICityQueueService cityQueueService;

    @Mock
    private ICoordinateService coordinateService;

    @Mock
    private IUnixToDateConverter unixToDateConverter;


    @Test
    public void fetchHistoricalCityAirData_success() {
        String cityName = "TestCity";
        LocalDate startDate = LocalDate.of(2023, 7, 10);
        LocalDate endDate = LocalDate.of(2023, 7, 15);
        String requestId = "testRequestId";

        // Mock the response from coordinateService.getCityCoordinates()
        CityCoordinates cityCoordinates = CityCoordinates.builder()
                .cityName(cityName)
                .lat(10.0)
                .lon(20.0)
                .build();
        coordinateService.saveCityCoordinate(cityCoordinates);

        // Mock the response from cityRepository.getByCityNameAndCityResults_DateBetween()
        List<City> existingData = Collections.singletonList(
                City.builder()
                        .cityName(cityName)
                        .cityResults(
                                CityResults.builder()
                                        .date(LocalDate.of(2023, 7, 11))
                                        .cityCategories(
                                                new CityCategories(10.0, 222.0, 332.0)
                                        ).build()
                        ).build()
        );

        when(cityRepository.getByCityNameAndCityResults_DateBetween(cityName, startDate.minusDays(1), endDate.plusDays(1))).thenReturn(existingData);

        // Mock the response from unixToDateConverter.calculateDateBetween()
        when(unixToDateConverter.calculateDateBetween(startDate, endDate)).thenReturn(6L);

        // Call the method to be tested
        cityService.fetchHistoricalCityAirData(cityName, startDate, endDate, requestId);

        // Verify that the queueCityGeocodingAndAirQualityData method was called with the correct parameters
        verify(cityQueueService).queueCityGeocodingAndAirQualityData(cityName, unixToDateConverter.convertDateToUnix(startDate), unixToDateConverter.convertDateToUnix(endDate), requestId);
    }



    @Test
    public void saveCity()
    {
        City city = City.builder()
                .cityName("Test")
                .cityResults(
                        CityResults.builder()
                                .date(LocalDate.of(2021, 5, 17))
                                .cityCategories(
                                        new CityCategories(10.0,222.0,332.0)
                                ).build()
                ).build();

        cityService.saveCity(city);

        assertThat(cityRepository.findByCityName("Test")).isNotNull();
    }

    @Test
    public void getHistoricalCityAirData_success()
    {
        City city = City.builder()
                .cityName("Test")
                .cityResults(
                        CityResults.builder()
                                .date(LocalDate.of(2021, 5, 17))
                                .cityCategories(
                                        new CityCategories(10.0,222.0,332.0)
                                ).build()
                ).build();
        City city1 = City.builder()
                .cityName("Test")
                .cityResults(
                        CityResults.builder()
                                .date(LocalDate.of(2021, 5, 18))
                                .cityCategories(
                                        new CityCategories(10.0,22.0,332.0)
                                ).build()
                ).build();
        cityService.saveCity(city);
        cityService.saveCity(city1);
        Pageable pageable = PageRequest.of(0, 2);
        Page<City> cities = cityRepository.findByCityNameAndCityResults_DateBetween("Test", LocalDate.of(2021, 5, 17), LocalDate.of(2021, 5, 18), pageable);
                assertThat(cities).isNotNull();
    }

    @Test
    public void checkRequestStatus_success() {

        Request request = Request.builder()
                .cityName("Test")
                .id("1234")
                .startDate(LocalDate.of(2023, 7, 10))
                .endDate(LocalDate.of(2023, 7, 10))
                .status(Status.PENDING)
                .build();

        requestService.saveRequest(request);

        City city = City.builder()
                .cityName("Test")
                .cityResults(
                        CityResults.builder()
                                .date(LocalDate.of(2023, 7, 10))
                                .cityCategories(
                                        new CityCategories(10.0, 222.0, 332.0)
                                ).build()
                ).build();
        cityService.saveCity(city);


        cityService.checkRequestStatus(request);

        Optional<Request> request1 = requestService.getRequestById("1234");

        assumeTrue(request1.isPresent());
        assertThat(request1.get().getStatus()).isEqualTo(Status.READY);
    }
}
