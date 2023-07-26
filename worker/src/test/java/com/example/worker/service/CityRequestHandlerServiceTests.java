package com.example.worker.service;


import com.example.common.dto.CityDto;
import com.example.common.dto.QueueRequestCityDto;
import com.example.worker.client.ICrawlerClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityRequestHandlerServiceTests {

    @InjectMocks
    private CityRequestHandlerService cityRequestHandlerService;

    @Mock
    private ICrawlerClient crawlerClient;

    @Mock
    private AmqpTemplate cityResultQueueTemplate;


//    @Override
//    public void executeMessage(QueueRequestCityDto request) {
//        if(request.getLon() == 0.0 && request.getLat() == 0.0)
//        {
//            CoordinateDto coordinates = crawlerClient.fetchGeocodingData(request.getName());
//            coordinateResultQueueTemplate.convertAndSend(coordinates);
//            request.setLat(coordinates.getLat());
//            request.setLon(coordinates.getLon());
//        }
//        crawlerClient.fetchHistoricalCityAirQuality(request.getName(), request.getLat(), request.getLon(), request.getStartUnix(), request.getEndUnix(), request.getRequestId())
//                .forEach(cityResultQueueTemplate::convertAndSend);
//    }

    //test the executeMessage method in test_crawlAppForAllCities_missingApp function
    @Test
    public void test_executeMessage() {
        // Create a QueueRequestCityDto with the required data
        CityDto city1 = CityDto.builder()
                .cityName("cityName1")
                .requestId("WorkerRequestHandlerTestReq1")
                .date(LocalDate.of(2021, 5, 18))
                .co(2.0)
                .o3(2.0)
                .so2(2.0)
                .build();

        List<CityDto> city = List.of(city1);
        // Mock the response from crawlerClient.fetchHistoricalCityAirQuality() with specific arguments
        when(crawlerClient.fetchHistoricalCityAirQuality(eq("cityName1"), eq(1L), eq(1L), eq(1L), eq(1L), eq("WorkerRequestHandlerTestReq"))).thenReturn(city);

        QueueRequestCityDto dto = new QueueRequestCityDto("cityName1", 1L, 1L, 1L, 1L, "WorkerRequestHandlerTestReq");

        cityRequestHandlerService.executeMessage(dto);

        verify(crawlerClient).fetchHistoricalCityAirQuality("cityName1", 1L, 1L, 1L, 1L, "WorkerRequestHandlerTestReq");

        verify(cityResultQueueTemplate).convertAndSend(city1);
    }

}
