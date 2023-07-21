package com.example.web.queue;


import com.example.common.dto.CityDto;
import com.example.web.service.city.ICityResultHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityResultQueueHandler {

    private final AmqpTemplate cityResultProblemQueueTemplate;

    private final ICityResultHandlerService cityResultHandlerService;

    @RabbitListener(queues = "${messaging.queue.city.result}", containerFactory = "resultQueueListener")
    public void handleMessage(CityDto result) {
        try {
            cityResultHandlerService.executeMessage(result);
        } catch (Exception e) {
            log.error("Could not handle result for cityName: {}", result, e);

            cityResultProblemQueueTemplate.convertAndSend(result);
        }
    }
}
