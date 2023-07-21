package com.example.web.service.city;

import com.example.common.dto.CityDto;
import com.example.web.model.*;
import com.example.web.service.request.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityResultHandlerService implements ICityResultHandlerService {

    private final ICityService cityService;

    private final IRequestService requestService;


    @Override
    public void executeMessage(CityDto result) {
        cityService.saveCity(convertFrom(result));
        Optional<Request> request = requestService.getRequestById(result.getRequestId());
        if(request.isPresent()){
           if(cityService.checkRequestStatus(request.get())) {
               if(request.get().getStatus().equals(Status.READY)) {
                   System.out.println("Request with id: " + result.getRequestId() + " is ready");
               }
           }
        }
    }

    private City convertFrom(CityDto cityDto) {
        return City.builder()
                .cityName(cityDto.getCityName())
                .cityResults(
                        CityResults.builder()
                                .date(cityDto.getDate())
                                .cityCategories(
                                        new CityCategories(cityDto.getCo(),cityDto.getSo2(),cityDto.getO3())
                                ).build()
                ).build();
    }

}
