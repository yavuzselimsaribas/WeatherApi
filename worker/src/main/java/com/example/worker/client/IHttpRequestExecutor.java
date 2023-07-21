package com.example.worker.client;

import com.example.common.dto.CityDto;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public interface IHttpRequestExecutor {

    <T> List<T> executeGetRequest(String url, ParameterizedTypeReference<List<T>> responseType);

    <T>  T executeCityGetRequest(String apiUrl, ParameterizedTypeReference<T> responseType);
}
