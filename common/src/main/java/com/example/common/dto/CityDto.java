package com.example.common.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto {
    private String requestId;
    private String cityName;
    private LocalDate date;
    private   double co;
    private   double o3;
    private   double so2;
}
