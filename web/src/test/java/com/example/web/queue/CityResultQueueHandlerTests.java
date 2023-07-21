package com.example.web.queue;

import com.example.common.dto.CityDto;
import com.example.web.service.city.ICityResultHandlerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CityResultQueueHandlerTests {

    @InjectMocks
    private CityResultQueueHandler cityResultQueueHandler;

    @Mock
    private ICityResultHandlerService service;

    @Mock(name = "cityResultProblemQueueTemplate")
    private AmqpTemplate cityResultProblemQueueTemplate;

    @Test
    public void handleMessage_success()
    {
        CityDto dto = new CityDto();

        cityResultQueueHandler.handleMessage(dto);

        verify(service).executeMessage(dto);
    }

    @Test
    public void handleMessage_fail()
    {
        CityDto dto = new CityDto();

        doThrow(RuntimeException.class).when(service).executeMessage(dto);

        cityResultQueueHandler.handleMessage(dto);

        verify(cityResultProblemQueueTemplate).convertAndSend(dto);
    }
}
