package com.example.web.service.coordinate;
import com.example.web.model.CityCoordinates;

public interface ICoordinateService {

    void saveCityCoordinate(CityCoordinates cityCoordinates);

    void fetchGeocodingInfo(String cityName);

    CityCoordinates getCityCoordinates(String cityName);
}

    