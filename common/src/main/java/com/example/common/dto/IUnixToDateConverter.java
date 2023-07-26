package com.example.common.dto;

import java.time.LocalDate;

public interface IUnixToDateConverter {
    LocalDate convertUnixToDate(Long unixTime);

    Long convertDateToUnix(LocalDate date);

    boolean isFirstHourOfDay(long dt);

    long calculateDateBetween(LocalDate startDate, LocalDate endDate);

}
