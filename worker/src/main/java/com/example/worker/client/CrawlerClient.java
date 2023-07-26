package com.example.worker.client;

import com.example.common.dto.CityDto;
import com.example.common.dto.CoordinateDto;
import com.example.common.dto.IUnixToDateConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerClient implements ICrawlerClient{

    private final String apiKey = "885fb87562e9af60c0251ef7eaf8ec16";

    private final IHttpRequestExecutor httpRequestExecutor;

    private final IUnixToDateConverter unixToDateConverter;

    @Override
    public CoordinateDto fetchGeocodingData(String cityName)
    {
        String geocodingApiBaseUrl = "http://api.openweathermap.org/geo/1.0/direct?";
        String apiUrl = geocodingApiBaseUrl + "q=" + cityName + "&limit=1&appid=" + apiKey;
        try
        {
            ParameterizedTypeReference<List<CoordinateDto>> responseType = new ParameterizedTypeReference<>() {
            };
            List<CoordinateDto> resultList = httpRequestExecutor.executeGetRequest(apiUrl, responseType);
            if (resultList != null && resultList.size() > 0)
            {
                return resultList.get(0);
            }
            else
            {
                log.warn("No result found for city: {}", cityName);
                return null;
            }
        } catch (Exception e)
        {
            log.error("Error while fetching geocoding data for city: " + cityName, e);
            return null;
        }
    }

    @Override
    public List<CityDto> fetchHistoricalCityAirQuality(String cityName, double lat, double lon, Long start, Long end, String requestId)
    {
        String airQualityApiBaseUrl = "http://api.openweathermap.org/data/2.5/air_pollution/history?";
        String apiUrl = airQualityApiBaseUrl + "lat=" + lat + "&lon=" + lon + "&start=" + start + "&end=" + end + "&appid=" + apiKey;
        try
        {
            ParameterizedTypeReference<JsonNode> responseType = new ParameterizedTypeReference<>() {};
            JsonNode response = httpRequestExecutor.executeCityGetRequest(apiUrl, responseType);
            List<CityDto> cityDtoList = new ArrayList<>();
            if(response != null && response.has("list"))
            {
                JsonNode list = response.get("list");
                LocalDate currentDate = null;
                CityDto cityDto = null;

                for(JsonNode node : list)
                {
                    long dt = node.get("dt").asLong();
                    LocalDate date = unixToDateConverter.convertUnixToDate(dt);

                    // if date is different than current date, create new cityDto
                    if(currentDate == null || !currentDate.equals(date))
                    {
                        currentDate = date;
                        cityDto = CityDto.builder()
                                .requestId(requestId)
                                .cityName(cityName)
                                .date(date)
                                .build();
                        cityDtoList.add(cityDto);
                    }
                    JsonNode componentsNode = node.get("components");
                    double co = componentsNode.get("co").asDouble();
                    double o3 = componentsNode.get("o3").asDouble();
                    double so2 = componentsNode.get("so2").asDouble();

                    //if dt is the first hour of the day, then set the cityDto's co, o3, so2 values
                    if(unixToDateConverter.isFirstHourOfDay(dt))
                    {
                        cityDto.setCo(co);
                        cityDto.setO3(o3);
                        cityDto.setSo2(so2);
                    }
                }
            }
            else
            {
                log.warn("No result found for city: {}", cityName);
                return null;
            }
            return cityDtoList;
        }
        catch (Exception e)
        {
            log.error("Error while fetching air quality data for city: " + cityName, e);
            return null;
        }
    }
}
