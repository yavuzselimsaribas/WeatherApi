package com.example.web.controller;

import com.example.web.service.city.ICityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;



public class CityControllerTests extends ControllerTestsBase{

    @MockBean
    private ICityService cityService;


}
