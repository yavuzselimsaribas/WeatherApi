package com.example.web.controller;


import com.example.web.model.City;
import com.example.web.model.CityResponse;
import com.example.web.model.Request;
import com.example.web.model.Status;
import com.example.web.service.city.ICityService;
import com.example.web.service.request.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("cities")
public class CityController {

    private final ICityService cityService;

    private final IRequestService requestService;

    @PostMapping("{cityName}")
    public ResponseEntity<CityResponse> createHistoricalCityAirDataRequest(@PathVariable String cityName,
                                                                           @RequestParam(required = false) LocalDate startDate,
                                                                           @RequestParam(required = false) LocalDate endDate) {
        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(7);
        }

        // First check if there is a request for the same city and dates
        if (requestService.existsByCityNameAndStartDateAndEndDate(cityName, startDate, endDate)) {
            Request request = requestService.findByCityNameAndStartDateAndEndDate(cityName, startDate, endDate);
            if (request.getStatus() == Status.PENDING) {
                // Return the status only if the request is pending
                return ResponseEntity.ok(CityResponse.builder().status(request.getStatus()).build());
            } else {
                // Return the data if the request is ready
                List<City> cities = cityService.getHistoricalCityAirData(cityName, startDate, endDate);
                return ResponseEntity.ok(CityResponse.builder()
                        .status(request.getStatus())
                        .cities(cities)
                        .build());
            }
        }

        // Create a new request entity
        Request request = Request.builder()
                .cityName(cityName)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.PENDING)
                .build();

        // Save the request to the repository
        Request savedRequest = requestService.saveRequest(request);

        // Trigger the data fetching process
        cityService.fetchHistoricalCityAirData(cityName, startDate, endDate, savedRequest.getId());

        // Return the status since the request is pending
        return ResponseEntity.ok(CityResponse.builder().status(Status.PENDING).build());
    }
}
