package com.example.web.service.coordinate;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CoordinateQueueService implements ICoordinateQueueService {

    private final AmqpTemplate coordinateRequestQueueTemplate;

    @Override
    public CompletableFuture<Void> queueCityGeocodingInfo(String cityName) {
        coordinateRequestQueueTemplate.convertAndSend(cityName);
        return null;
    }

}
