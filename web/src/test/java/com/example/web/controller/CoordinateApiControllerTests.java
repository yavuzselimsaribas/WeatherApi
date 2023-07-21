package com.example.web.controller;

import com.example.web.service.coordinate.ICoordinateService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.verify;

@WebMvcTest(controllers = CoordinateApiController.class)
@ContextConfiguration(classes = CoordinateApiController.class)
public class CoordinateApiControllerTests extends ControllerTestsBase{

    @MockBean
    private ICoordinateService coordinateService;

    @Test
    public void queueCityGeocodingInfo() throws Exception
    {

        String cityName = "London";
        mockMvc.perform(get("/coordinates/" + cityName))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(200);
                });
        verify(coordinateService).fetchGeocodingInfo(cityName);

    }
}
