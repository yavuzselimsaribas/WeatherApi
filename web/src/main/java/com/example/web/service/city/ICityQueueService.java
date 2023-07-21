package com.example.web.service.city;

import java.util.concurrent.CompletableFuture;

public interface ICityQueueService {
    CompletableFuture<Void> queueAirQualityData(String cityName, double lat, double lon, long startUnix, long endUnix, String requestId);

    void queueCityGeocodingAndAirQualityData(String cityName, long startUnix, long endUnix, String requestId);
}
