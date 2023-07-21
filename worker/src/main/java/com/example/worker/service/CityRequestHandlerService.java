package com.example.worker.service;


import com.example.common.dto.CityDto;
import com.example.common.dto.CoordinateDto;
import com.example.common.dto.QueueRequestCityDto;
import com.example.worker.client.ICrawlerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityRequestHandlerService implements ICityRequestHandlerService{

    private final ICrawlerClient crawlerClient;

    private final AmqpTemplate cityResultQueueTemplate;

    private final AmqpTemplate coordinateResultQueueTemplate;

    @Override
    public void executeMessage(QueueRequestCityDto request) {
        if(request.getLon() == 0.0 && request.getLat() == 0.0)
        {
            CoordinateDto coordinates = crawlerClient.fetchGeocodingData(request.getName());
            coordinateResultQueueTemplate.convertAndSend(coordinates);
            request.setLat(coordinates.getLat());
            request.setLon(coordinates.getLon());
        }
        crawlerClient.fetchHistoricalCityAirQuality(request.getName(), request.getLat(), request.getLon(), request.getStartUnix(), request.getEndUnix(), request.getRequestId())
                .forEach(cityResultQueueTemplate::convertAndSend);
    }
}
