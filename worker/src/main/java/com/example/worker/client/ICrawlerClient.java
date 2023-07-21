package com.example.worker.client;

import com.example.common.dto.CityDto;
import com.example.common.dto.CoordinateDto;

import java.util.List;

public interface ICrawlerClient {
    CoordinateDto fetchGeocodingData(String cityName);

    List<CityDto> fetchHistoricalCityAirQuality(String cityName, double lat, double lon, Long start, Long end, String requestId);
}
