package com.example.web.service.coordinate;

import java.util.concurrent.CompletableFuture;

public interface ICoordinateQueueService {
    CompletableFuture<Void> queueCityGeocodingInfo(String cityName);
}
