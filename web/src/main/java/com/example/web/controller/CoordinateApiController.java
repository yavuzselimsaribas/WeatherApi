package com.example.web.controller;


import com.example.web.model.CityCoordinates;
import com.example.web.service.coordinate.ICoordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("coordinates")
public class CoordinateApiController {

    private final ICoordinateService cityCoordinateService;


    @GetMapping("queue/{cityName}")
    public ResponseEntity<Boolean> queueCityGeocodingInfo(@PathVariable String cityName) {
        try {
            cityCoordinateService.fetchGeocodingInfo(cityName);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("{cityName}")
    public ResponseEntity<CityCoordinates> getCityGeocodingInfo(@PathVariable String cityName) {
        try {
            return ResponseEntity.ok(cityCoordinateService.getCityCoordinates(cityName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
