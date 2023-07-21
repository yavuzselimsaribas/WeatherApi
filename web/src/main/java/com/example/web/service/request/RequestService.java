package com.example.web.service.request;

import com.example.web.model.Request;
import com.example.web.model.Status;
import com.example.web.repository.IRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService implements IRequestService{

    private final IRequestRepository requestRepository;

    @Override
    public Request saveRequest(Request request) {
        requestRepository.save(request);
        return request;
    }

    @Override
    public Optional<Request> getRequestById(String requestId) {
        return requestRepository.findById(requestId);
    }

    @Override
    public void setReady(String id) {
        Optional<Request> request = requestRepository.findById(id);
        if(request.isPresent()){
            request.get().setStatus(Status.READY);
            requestRepository.save(request.get());
        }
    }

    @Override
    public boolean existsByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate) {
        return requestRepository.existsByCityNameAndStartDateAndEndDate(cityName, startDate, endDate);
    }

    @Override
    public Request findByCityNameAndStartDateAndEndDate(String cityName, LocalDate startDate, LocalDate endDate) {
        return requestRepository.findByCityNameAndStartDateAndEndDate(cityName, startDate, endDate);
    }

}
