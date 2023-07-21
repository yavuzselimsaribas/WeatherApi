package com.example.web.queue;


import com.example.common.dto.CoordinateDto;
import com.example.web.service.coordinate.ICoordinateResultHandlerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CoordinateResultQueueHandlerTests {

    @InjectMocks
    private CoordinateResultQueueHandler coordinateResultQueueHandler;

    @Mock
    private ICoordinateResultHandlerService service;

    @Mock(name = "coordinateResultProblemQueueTemplate")
    private AmqpTemplate coordinateResultProblemQueueTemplate;

    @Test
    public void handleMessage_success()
    {
        CoordinateDto dto = new CoordinateDto();

        coordinateResultQueueHandler.handleMessage(dto);

        verify(service).executeMessage(dto);
    }

    @Test
    public void handleMessage_fail()
    {
        CoordinateDto dto = new CoordinateDto();

        doThrow(RuntimeException.class).when(service).executeMessage(dto);

        coordinateResultQueueHandler.handleMessage(dto);

        verify(coordinateResultProblemQueueTemplate).convertAndSend(dto);
    }
}
