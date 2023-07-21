package com.example.web.repository;

import com.example.web.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ICityRepository extends MongoRepository<City, String>{
    List<City> findByCityNameAndCityResults_DateBetween(String cityName, LocalDate startDate, LocalDate endDate);

    boolean existsByCityNameAndCityResults_DateBetween(String cityName, LocalDate startDate, LocalDate endDate);

    boolean existsByCityNameAndCityResults_Date(String cityName, LocalDate date);

    City findByCityNameAndCityResults_Date(String cityName, LocalDate key);

    City findByCityName(String cityName);
}
