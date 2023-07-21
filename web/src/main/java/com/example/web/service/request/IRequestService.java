package com.example.web.service.request;

import com.example.web.model.Request;
import com.example.web.model.Status;

import java.time.LocalDate;
import java.util.Optional;

public interface IRequestService {
    Request saveRequest(Request request);

    Optional<Request> getRequestById(String requestId);

    void setReady(String id);

    boolean existsByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate);

    Request findByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate);

}
