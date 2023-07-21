package com.example.web.service.coordinate;


import com.example.web.model.CityCoordinates;
import com.example.web.repository.ICoordinateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CoordinateService implements ICoordinateService {

    private final ICoordinateRepository cityCoordinateRepository;

    private final ICoordinateQueueService cityQueueService;

    @Override
    public void saveCityCoordinate(CityCoordinates cityCoordinates) {
        cityCoordinateRepository.save(cityCoordinates);
    }

    @Override
    public void fetchGeocodingInfo(String cityName) {
        CityCoordinates cityCoordinates = cityCoordinateRepository.findByCityName(cityName);
        if (cityCoordinates == null) {
            System.out.println("City coordinates not found in database for city: " + cityName + ". Queueing request for geocoding info.");
            cityQueueService.queueCityGeocodingInfo(cityName);
        }
    }

    @Override
    public CityCoordinates getCityCoordinates(String cityName) {
        return cityCoordinateRepository.findByCityName(cityName);
    }
}

