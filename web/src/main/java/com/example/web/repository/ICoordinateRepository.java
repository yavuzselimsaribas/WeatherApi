package com.example.web.repository;


import com.example.web.model.CityCoordinates;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICoordinateRepository extends MongoRepository<CityCoordinates, String> {
    CityCoordinates findByCityName(String cityName);

    boolean existsByCityName(String cityName);
}
