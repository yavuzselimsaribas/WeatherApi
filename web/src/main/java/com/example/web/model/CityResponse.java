package com.example.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponse {
    private Status status;
    private List<City> cities;
}
