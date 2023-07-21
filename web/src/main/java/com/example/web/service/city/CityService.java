package com.example.web.service.city;

import com.example.common.dto.IUnixToDateConverter;
import com.example.web.model.*;
import com.example.web.repository.ICityRepository;
import com.example.web.repository.IRequestRepository;
import com.example.web.service.coordinate.ICoordinateService;
import com.example.web.service.request.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class CityService implements ICityService {

    private final ICityQueueService cityQueueService;

    private final ICityRepository cityRepository;

    private final ICoordinateService coordinateService;

    private final IUnixToDateConverter unixToDateConverter;

    private final IRequestService requestService;

    @Override
    public void fetchHistoricalCityAirData(String cityName, LocalDate startDate, LocalDate endDate, String requestId) {

        try {
            CityCoordinates cityCoordinates = coordinateService.getCityCoordinates(cityName);
            if(cityCoordinates == null){
                System.out.println("City coordinates not found in database for city: " + cityName + ". Queueing request for geocoding info.");
                cityQueueService.queueCityGeocodingAndAirQualityData(cityName, unixToDateConverter.convertDateToUnix(startDate), unixToDateConverter.convertDateToUnix(endDate), requestId);
            }
            else
            {
                if(cityRepository.existsByCityNameAndCityResults_DateBetween(cityName, startDate.minusDays(1), endDate.plusDays(1))){
                    System.out.println("City data found in database for city: " + cityName + " from " + startDate + " to " + endDate + ". Skipping queueing.");
                    //make the request as Ready
                    requestService.setReady(requestId);
                    return;
                }
                List<DateRange> missingDateRanges = getMissingDateRanges(cityName, startDate, endDate);
                List<CompletableFuture<Void>> queueFutures = new ArrayList<>();
                for (DateRange dateRange : missingDateRanges) {
                    long startUnix = unixToDateConverter.convertDateToUnix(dateRange.getStartDate());
                    long endUnix = unixToDateConverter.convertDateToUnix(dateRange.getEndDate());
                    System.out.println("Queueing data for " + cityName + " from " + dateRange.getStartDate() + " to " + dateRange.getEndDate() + " because it is missing in the database");

                    CompletableFuture<Void> queueFuture = cityQueueService.queueAirQualityData(cityName, cityCoordinates.getLat(), cityCoordinates.getLon(), startUnix, endUnix, requestId);
                    queueFutures.add(queueFuture);
                }
                CompletableFuture<Void> allQueueFutures = CompletableFuture.allOf(queueFutures.toArray(new CompletableFuture[0]));
                allQueueFutures.join();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    private List<DateRange> getMissingDateRanges(String cityName, LocalDate startDate, LocalDate endDate) {
        List<City> existingData = getHistoricalCityAirData(cityName, startDate, endDate);
        List<DateRange> missingDateRanges = new ArrayList<>();

        LocalDate currentDate = startDate;
        LocalDate missingStartDate = null;

        while (currentDate.isBefore(endDate)) {
            if (hasDataForDate(existingData, currentDate)) {
                if (missingStartDate != null) {
                    LocalDate missingEndDate = currentDate.minusDays(1);
                    missingDateRanges.add(new DateRange(missingStartDate, missingEndDate));
                    missingStartDate = null;
                }
            } else {
                if (missingStartDate == null) {
                    missingStartDate = currentDate;
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        if (missingStartDate != null) {
            missingDateRanges.add(new DateRange(missingStartDate, endDate));
        }

        return missingDateRanges;
    }


    private boolean hasDataForDate(List<City> existingData, LocalDate date) {
        return existingData.stream()
                .anyMatch(city -> city.getCityResults().getDate().isEqual(date));
    }

    @Override
    public void saveCity(City result) {
        cityRepository.save(result);
    }


    @Override
    public List<City> getHistoricalCityAirData(String cityName, LocalDate finalStartDate, LocalDate finalEndDate) {
        return cityRepository.findByCityNameAndCityResults_DateBetween(cityName, finalStartDate.minusDays(1), finalEndDate.plusDays(1));
    }

    @Override
    public boolean checkRequestStatus(Request request) {
        //first, check if the requested datas are in the database
        if(cityRepository.existsByCityNameAndCityResults_DateBetween(request.getCityName(), request.getStartDate().minusDays(1), request.getEndDate().plusDays(1)))
        {
            requestService.setReady(request.getId());
            return true;
        }
        else
        {
            return false;
        }

    }


}
