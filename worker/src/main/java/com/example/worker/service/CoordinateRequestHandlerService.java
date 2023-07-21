package com.example.worker.service;

import com.example.common.dto.CoordinateDto;
import com.example.common.dto.QueueRequestCoordinateDto;
import com.example.worker.client.ICrawlerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinateRequestHandlerService implements ICoordinateRequestHandlerService {

    private final AmqpTemplate coordinateResultQueueTemplate;

    private final ICrawlerClient crawlerClient;

    @Override
    public void executeMessage(QueueRequestCoordinateDto request) {
        CoordinateDto city = crawlerClient.fetchGeocodingData(request.getName());
        coordinateResultQueueTemplate.convertAndSend(city);
    }
}
