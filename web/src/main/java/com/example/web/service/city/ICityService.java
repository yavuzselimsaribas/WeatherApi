package com.example.web.service.city;

import com.example.web.model.City;
import com.example.web.model.Request;
import com.example.web.model.Status;

import java.time.LocalDate;
import java.util.List;

public interface ICityService {
    void fetchHistoricalCityAirData(String cityName, LocalDate startDate, LocalDate endDate, String requestId);

    void saveCity(City result);

    List<City> getHistoricalCityAirData(String cityName, LocalDate finalStartDate, LocalDate finalEndDate);

    boolean checkRequestStatus(Request request);
}
