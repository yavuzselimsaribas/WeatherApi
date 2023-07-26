package com.example.web.service.coordinate;


import com.example.common.dto.CoordinateDto;
import com.example.web.model.CityCoordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CoordinateResultHandlerServiceTests {

    @InjectMocks
    private CoordinateResultHandlerService coordinateResultHandlerService;

    @Mock
    private ICoordinateService coordinateService;


    @Captor
    private ArgumentCaptor<CityCoordinates> coordinateCaptor;

    @Test
    public void saveCoordinate()
    {
        CoordinateDto dto = CoordinateDto.builder()
                .name("cityName")
                .lat(32.21)
                .lon(1231.2)
                .build();
    coordinateResultHandlerService.executeMessage(dto);
        verify(coordinateService).saveCityCoordinate(coordinateCaptor.capture());
        assertThat(coordinateCaptor.getValue().getCityName()).isEqualTo("cityName");
    }


}
