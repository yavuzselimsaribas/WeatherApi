package com.example.web.service.city;


import com.example.common.dto.CityDto;
import com.example.web.model.City;
import com.example.web.service.request.IRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CityResultHandlerServiceTests {

    @InjectMocks
    private CityResultHandlerService cityResultHandlerService;

    @Mock
    private ICityService cityService;

    @Mock
    private IRequestService requestService;

    @Captor
    private ArgumentCaptor<City> cityCaptor;

    @Test
    public void savePost()
    {
        CityDto dto = CityDto.builder()
                .cityName("cityName")
                .date(LocalDate.of(2021, 5, 17))
                .co(32.21)
                .o3(1231.2)
                .so2(123.1)
                .requestId("32432412351431")
                .build();
        cityResultHandlerService.executeMessage(dto);
        verify(cityService).saveCity(cityCaptor.capture());
        assertThat(cityCaptor.getValue().getCityName()).isEqualTo("cityName");
    }
}

