package com.example.web.queue;

import com.example.common.dto.CoordinateDto;
import com.example.web.service.coordinate.ICoordinateResultHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoordinateResultQueueHandler
{
    private final AmqpTemplate coordinateResultProblemQueueTemplate;

    private final ICoordinateResultHandlerService coordinateResultHandlerService;

    @RabbitListener(queues = "${messaging.queue.coordinate.result}", containerFactory = "resultQueueListener")
    public void handleMessage(CoordinateDto result)
    {
        try
        {
             coordinateResultHandlerService.executeMessage(result);
        }
        catch (Exception e)
        {
            log.error("Could not handle result for cityName: {}", result.getName(), e);

            coordinateResultProblemQueueTemplate.convertAndSend(result);
        }
    }


}
