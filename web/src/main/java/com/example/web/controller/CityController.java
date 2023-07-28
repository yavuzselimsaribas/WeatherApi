package com.example.web.controller;
import com.example.web.model.City;
import com.example.web.model.CityResponse;
import com.example.web.model.Request;
import com.example.web.model.Status;
import com.example.web.service.city.ICityService;
import com.example.web.service.request.IRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Objects;


@Slf4j
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
            @RequestParam(required = false) int size,
            @RequestParam(required = false) Sort.Direction sortDirection)
    {
        if (startDate == null || endDate == null)
        {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(7);
        }

        if(Objects.isNull(sortDirection))
        {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "cityResults.date"));

        // Check if there is an existing request for the same city and dates
        if (requestService.existsByCityNameAndStartDateAndEndDate(cityName, startDate, endDate))
        {
            Request request = requestService.findByCityNameAndStartDateAndEndDate(cityName, startDate, endDate);
            Page<City> cityPage = cityService.getHistoricalCityAirData(cityName, startDate, endDate, pageable);
            if (request.getStatus() == Status.PENDING)
            {
                LocalDate finalStartDate = startDate;
                if(cityPage.stream().anyMatch(city -> city.getCityResults().getDate().isEqual(finalStartDate)))
                {
                        log.info("Even though the request is pending, requested pages are ready in the database. Returning the data.");
                        return ResponseEntity.ok(CityResponse.builder()
                                .status(Status.READY)
                                .cities(cityPage.getContent())
                                .totalCount(cityPage.getTotalElements())
                                .build());
               }
                return ResponseEntity.ok(CityResponse.builder().status(request.getStatus()).build());
            }
            else
            {
                // Return the data if the request is ready
                return ResponseEntity.ok(CityResponse.builder()
                        .status(request.getStatus())
                        .cities(cityPage.getContent())
                        .totalCount(cityPage.getTotalElements())
                        .build());
            }
        }
        else
        {
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
}
