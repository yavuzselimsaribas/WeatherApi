package com.example.worker.queue;

import com.example.common.dto.QueueRequestCoordinateDto;
import com.example.worker.service.ICoordinateRequestHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoordinateRequestQueueHandler {

    private final AmqpTemplate coordinateRequestProblemQueueTemplate;

    private final ICoordinateRequestHandlerService coordinateRequestHandlerService;


    @RabbitListener(queues = "${messaging.queue.coordinate.request}", containerFactory = "requestQueueListener")
    public void handleMessage(QueueRequestCoordinateDto request)
    {
        try
        {
            coordinateRequestHandlerService.executeMessage(request);
        }
        catch (Exception e)
        {
            log.error("Could not handle request for cityName: {}", request.getName(), e);

            coordinateRequestProblemQueueTemplate.convertAndSend(request);
        }
    }
}
