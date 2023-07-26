package com.example.worker.queue;


import com.example.common.dto.QueueRequestCityDto;
import com.example.worker.service.ICityRequestHandlerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CityRequestQueueHandlerTests {
    @InjectMocks
    private CityRequestQueueHandler cityRequestQueueHandler;

    @Mock
    private ICityRequestHandlerService service;

    @Mock(name = "cityRequestProblemQueueTemplate")
    private AmqpTemplate cityRequestProblemQueueTemplate;

    @Test
    public void handleMessage_success()
    {
        QueueRequestCityDto dto = new QueueRequestCityDto("cityName", 1L, 1L, 1L,1L,"WorkerRequestHandlerTestReq");

        cityRequestQueueHandler.handleMessage(dto);

        verify(service).executeMessage(dto);
    }

    @Test
    public void handleMessage_fail()
    {
        QueueRequestCityDto dto = new QueueRequestCityDto("cityName", 1L, 1L, 1L,1L,"WorkerRequestHandlerTestReq");

        doThrow(RuntimeException.class).when(service).executeMessage(dto);

        cityRequestQueueHandler.handleMessage(dto);

        verify(cityRequestProblemQueueTemplate).convertAndSend(dto);
    }


}
