package com.example.web.service.city;

import com.example.common.dto.QueueRequestCityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CityQueueService implements ICityQueueService {

    private final AmqpTemplate cityRequestQueueTemplate;


    @Override
    public CompletableFuture<Void> queueAirQualityData(String cityName, double lat, double lon, long startUnix, long endUnix, String requestId) {
        QueueRequestCityDto queueRequestCityDto = QueueRequestCityDto.builder()
                .name(cityName)
                .lat(lat)
                .lon(lon)
                .startUnix(startUnix)
                .endUnix(endUnix)
                .requestId(requestId)
                .build();

        CompletableFuture<Void> future = new CompletableFuture<>();
        try
        {
            cityRequestQueueTemplate.convertAndSend(queueRequestCityDto);
            // If the operation was successful, complete the future
            future.complete(null);
        }
        catch (Exception e)
        {
            // If there was an error, complete the future exceptionally with the error
            future.completeExceptionally(e);
        }

        return future;
    }

    @Override
    public void queueCityGeocodingAndAirQualityData(String cityName, long startUnix, long endUnix, String requestId) {
        QueueRequestCityDto queueRequestCityDto = QueueRequestCityDto.builder()
                .name(cityName)
                .startUnix(startUnix)
                .endUnix(endUnix)
                .requestId(requestId)
                .build();
        cityRequestQueueTemplate.convertAndSend(queueRequestCityDto);
    }


}
