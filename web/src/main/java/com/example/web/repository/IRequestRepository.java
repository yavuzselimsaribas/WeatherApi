package com.example.web.repository;

import com.example.web.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface IRequestRepository extends MongoRepository<Request, String> {

    Request findByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate);

    boolean existsByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate);

}
