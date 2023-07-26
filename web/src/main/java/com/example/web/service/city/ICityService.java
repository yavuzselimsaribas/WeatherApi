package com.example.web.service.city;

import com.example.web.model.City;
import com.example.web.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface ICityService {
    void fetchHistoricalCityAirData(String cityName, LocalDate startDate, LocalDate endDate, String requestId);

    void saveCity(City result);

    Page<City> getHistoricalCityAirData(String cityName, LocalDate finalStartDate, LocalDate finalEndDate, Pageable pageable);

    void checkRequestStatus(Request request);
}
