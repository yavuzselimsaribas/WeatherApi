package com.example.web.service.coordinate;
import com.example.common.dto.CoordinateDto;
import com.example.web.model.CityCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinateResultHandlerService implements ICoordinateResultHandlerService {

    private final ICoordinateService coordinateService;
    @Override
    public void executeMessage(CoordinateDto result) {
        coordinateService.saveCityCoordinate(convertFrom(result));
    }


    private CityCoordinates convertFrom(CoordinateDto coordinateDto) {
        return CityCoordinates.builder()
                .cityName(coordinateDto.getName())
                .lat(coordinateDto.getLat())
                .lon(coordinateDto.getLon())
                .build();
    }
}
