package com.example.web.service.city;


import com.example.web.model.City;
import com.example.web.model.CityCategories;
import com.example.web.model.CityResults;
import com.example.web.repository.ICityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
public class CityServiceTests {

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICityRepository cityRepository;

    @Test
    public void fetchHistoricalCityAirData_success()
    {
        //TODO: Implement
    }

    @Test
    public void fetchHistoricalCityAirData_fail()
    {
        //TODO: Implement
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
        List<City> cities = cityRepository.findByCityNameAndCityResults_DateBetween("Test", LocalDate.of(2021, 5, 16), LocalDate.of(2021, 5, 19));
        assertThat(cities).isNotNull();
    }

    @Test
    public void checkRequestStatus_success()
    {
        //TODO: Implement
    }
}
