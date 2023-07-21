package com.example.common.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueRequestCityDto {
    private String name;
    private double lat;
    private double lon;
    private long startUnix;
    private long endUnix;
    private String requestId;
}
