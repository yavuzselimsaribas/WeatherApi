package com.example.worker.queue;


import com.example.common.dto.QueueRequestCityDto;
import com.example.worker.service.ICityRequestHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityRequestQueueHandler {

    private final AmqpTemplate cityRequestProblemQueueTemplate;

    private final ICityRequestHandlerService cityRequestHandlerService;

    @RabbitListener(queues = "${messaging.queue.city.request}", containerFactory = "requestQueueListener")
    public void handleMessage(QueueRequestCityDto request)
    {
        try
        {
            cityRequestHandlerService.executeMessage(request);
        }
        catch (Exception e)
        {
            log.error("Could not handle request for cityName: {}", request.getName(), e);

            cityRequestProblemQueueTemplate.convertAndSend(request);
        }
    }
}
