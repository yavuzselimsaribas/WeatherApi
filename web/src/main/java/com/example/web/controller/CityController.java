package com.example.web.controller;
import com.example.web.model.City;
import com.example.web.model.CityResponse;
import com.example.web.model.Request;
import com.example.web.model.Status;
import com.example.web.service.city.ICityService;
import com.example.web.service.request.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
@RestController
@RequiredArgsConstructor
@RequestMapping("cities")
public class CityController {

    private final ICityService cityService;

    private final IRequestService requestService;

    @GetMapping("{cityName}")
    public ResponseEntity<CityResponse> getHistoricalCityAirData(
            @PathVariable String cityName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size)
    {
        if (startDate == null || endDate == null)
        {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(7);
        }
        Pageable pageable = PageRequest.of(page, size);

        // Check if there is an existing request for the same city and dates
        if (requestService.existsByCityNameAndStartDateAndEndDate(cityName, startDate, endDate))
        {
            Request request = requestService.findByCityNameAndStartDateAndEndDate(cityName, startDate, endDate);
            if (request.getStatus() == Status.PENDING)
            {
                // Return the status only if the request is pending
                return ResponseEntity.ok(CityResponse.builder().status(request.getStatus()).build());
            }
            else
            {
                // Return the data if the request is ready
                Page<City> cityPage = cityService.getHistoricalCityAirData(cityName, startDate, endDate, pageable);
                return ResponseEntity.ok(CityResponse.builder()
                        .status(request.getStatus())
                        .cities(cityPage.getContent())
                        .totalCount(cityPage.getTotalElements())
                        .build());
            }
        }

        // If there is no existing request, create a new one and save it to the repository
        Request newRequest = Request.builder()
                .cityName(cityName)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.PENDING)
                .build();
        Request savedRequest = requestService.saveRequest(newRequest);

        // Queue the request for data retrieval
        cityService.fetchHistoricalCityAirData(cityName, startDate, endDate, savedRequest.getId());

        // Check if the requested data is ready, if found in the database, then return the data
        if (savedRequest.getStatus() == Status.READY)
        {
            Page<City> cityPage = cityService.getHistoricalCityAirData(cityName, startDate, endDate, pageable);
            return ResponseEntity.ok(CityResponse.builder()
                    .status(savedRequest.getStatus())
                    .cities(cityPage.getContent())
                    .totalCount(cityPage.getTotalElements())
                    .build());
        }
        else
        {
            // If the requested data is not ready, return the status
            return ResponseEntity.ok(CityResponse.builder().status(savedRequest.getStatus()).build());
        }
    }
}
