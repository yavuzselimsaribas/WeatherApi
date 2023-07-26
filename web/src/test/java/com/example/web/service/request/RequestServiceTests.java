package com.example.web.service.request;


import com.example.web.model.Request;
import com.example.web.repository.IRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.Optional;

@DataMongoTest
public class RequestServiceTests {

    @Autowired
    private IRequestService requestService;

    @Autowired
    private IRequestRepository requestRepository;


    @Test
    public void saveRequest()
    {
        Request request = Request.builder()
                .id("1")
                .cityName("Test")
                .startDate(LocalDate.ofEpochDay(123456789L))
                .endDate(LocalDate.ofEpochDay(123456789L))
                .build();
        requestService.saveRequest(request);
        assertThat(requestRepository.findById("1")).isNotNull();
    }

    @Test
    public void getRequestById()
    {
        Request request = Request.builder()
                .id("2")
                .cityName("Test")
                .startDate(LocalDate.ofEpochDay(123456789L))
                .endDate(LocalDate.ofEpochDay(123456789L))
                .build();
        requestService.saveRequest(request);
        Optional<Request> request1 = requestService.getRequestById("2");
        assumeTrue(request1.isPresent());
        assertThat(request1.get().getCityName()).isEqualTo("Test");
    }

    @Test
    public void existsByCityNameAndStartDateAndEndDate()
    {
        Request request = Request.builder()
                .id("3")
                .cityName("Test")
                .startDate(LocalDate.ofEpochDay(123456789L))
                .endDate(LocalDate.ofEpochDay(123456790L))
                .build();
        requestService.saveRequest(request);
        boolean exists = requestService.existsByCityNameAndStartDateAndEndDate("Test", LocalDate.ofEpochDay(123456789L), LocalDate.ofEpochDay(123456790L));
        assertThat(exists).isTrue();
    }

    @Test
    public void findByCityNameAndStartDateAndEndDate()
    {
        Request request = Request.builder()
                .id("4")
                .cityName("TestFindByCityNameAndStartDateAndEndDate")
                .startDate(LocalDate.ofEpochDay(123456789L))
                .endDate(LocalDate.ofEpochDay(123456790L))
                .build();
        requestService.saveRequest(request);
        Request request1 = requestService.findByCityNameAndStartDateAndEndDate("TestFindByCityNameAndStartDateAndEndDate", LocalDate.ofEpochDay(123456789L), LocalDate.ofEpochDay(123456790L));
        assertThat(request1.getCityName()).isEqualTo("TestFindByCityNameAndStartDateAndEndDate");
    }
}
