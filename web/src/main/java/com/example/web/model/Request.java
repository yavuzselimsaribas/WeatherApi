package com.example.web.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Document(collection = "requests")
public class Request {
    @Id
    private String id;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
}

