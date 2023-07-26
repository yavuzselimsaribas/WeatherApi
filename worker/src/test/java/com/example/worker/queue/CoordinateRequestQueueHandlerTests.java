package com.example.worker.queue;


import com.example.common.dto.QueueRequestCoordinateDto;
import com.example.worker.service.ICoordinateRequestHandlerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CoordinateRequestQueueHandlerTests {

    @InjectMocks
    private CoordinateRequestQueueHandler coordinateRequestQueueHandler;

    @Mock
    private ICoordinateRequestHandlerService service;

    @Mock(name = "coordinateRequestProblemQueueTemplate")
    private AmqpTemplate coordinateRequestProblemQueueTemplate;

    @Test
    public void handleMessage_success(){
        QueueRequestCoordinateDto dto = new QueueRequestCoordinateDto("CityName");
        coordinateRequestQueueHandler.handleMessage(dto);
        verify(service).executeMessage(dto);
    }

    @Test
    public void handleMessage_fail()
    {
        QueueRequestCoordinateDto dto = new QueueRequestCoordinateDto("CityName_Fail");

        doThrow(RuntimeException.class).when(service).executeMessage(dto);

        coordinateRequestQueueHandler.handleMessage(dto);

        verify(coordinateRequestProblemQueueTemplate).convertAndSend(dto);
    }
}
