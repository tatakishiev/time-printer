package com.neotech.timeprinter.service.impl;

import com.neotech.timeprinter.service.TimeQueue;
import com.neotech.timeprinter.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ProducerServiceImpl implements ProducerService {

    private final TimeQueue timeQueue;

    @Autowired
    public ProducerServiceImpl(TimeQueue timeQueue) {
        this.timeQueue = timeQueue;
    }

    @Override
    public void run() {
        timeQueue.push(new Timestamp(System.currentTimeMillis()));
    }
}