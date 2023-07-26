package com.example.web.repository;

import com.example.web.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ICityRepository extends MongoRepository<City, String>{
    Page<City> findByCityNameAndCityResults_DateBetween(String cityName, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<City> getByCityNameAndCityResults_DateBetween(String cityName, LocalDate startDate, LocalDate endDate);

    City findByCityName(String cityName);
}
