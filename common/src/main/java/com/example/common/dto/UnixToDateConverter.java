package com.example.common.dto;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
@Service
@RequiredArgsConstructor
public class UnixToDateConverter implements IUnixToDateConverter {
        @Override
        public LocalDate convertUnixToDate(Long unixTime)
        {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneOffset.UTC).toLocalDate();
        }

        @Override //date to unix
        public Long convertDateToUnix(LocalDate date)
        {
            return date.atTime(1, 0).toEpochSecond(ZoneOffset.UTC);
        }

    @Override
    public boolean isFirstHourOfDay(long dt)
    {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneOffset.UTC);
        return dateTime.getHour() == 1;
    }

    @Override
    public long calculateDateBetween(LocalDate startDate, LocalDate endDate)
    {
        return endDate.toEpochDay() - startDate.toEpochDay() + 1; //+1 because [startDate, endDate] is inclusive
    }


}
