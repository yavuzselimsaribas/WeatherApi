package com.example.worker.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpRequestExecutor implements IHttpRequestExecutor{

    private final RestOperations restOperations;

    @Override
    public <T> List<T> executeGetRequest(String url, ParameterizedTypeReference<List<T>> responseType)
    {
        try
        {
            URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
            ResponseEntity<List<T>> responseEntity = restOperations.exchange(requestEntity, responseType);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException ex)
        {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                log.info("Requested resource was not found url: {}", url);
            }
            log.warn("Couldn't get successful result from http request status:{} url: {}", ex.getStatusCode(), url);
            throw new RestClientException("Couldn't get successful result from http request", ex);
        }
        catch (Exception ex)
        {
            log.error("Unknown error occurred while executing http request for url: {}", url, ex);
            throw new IllegalStateException("Unknown error occurred while executing http request", ex);
        }
    }

    @Override
    public <T> T executeCityGetRequest(String apiUrl, ParameterizedTypeReference<T> responseType) {
        try{
            URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl).build().toUri();
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
            ResponseEntity<T> responseEntity = restOperations.exchange(requestEntity, responseType);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException ex)
        {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                log.info("Requested resource was not found url: {}", apiUrl);
            }
            log.warn("Couldn't get successful result from http request status:{} url: {}", ex.getStatusCode(), apiUrl);
            throw new RestClientException("Couldn't get successful result from http request", ex);
        }
        catch (Exception ex)
        {
            log.error("Unknown error occurred while executing http request for url: {}", apiUrl, ex);
            throw new IllegalStateException("Unknown error occurred while executing http request", ex);
        }
    }
}
