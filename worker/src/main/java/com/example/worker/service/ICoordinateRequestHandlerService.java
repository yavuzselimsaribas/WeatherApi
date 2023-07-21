package com.example.worker.service;

import com.example.common.dto.QueueRequestCoordinateDto;

public interface ICoordinateRequestHandlerService {
    void executeMessage(QueueRequestCoordinateDto request);

}
