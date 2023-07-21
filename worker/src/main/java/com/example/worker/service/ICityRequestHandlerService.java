package com.example.worker.service;

import com.example.common.dto.QueueRequestCityDto;

public interface ICityRequestHandlerService{

    void executeMessage(QueueRequestCityDto request);
}
