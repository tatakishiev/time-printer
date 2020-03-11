package com.neotech.timeprinter.service.impl;

import com.neotech.timeprinter.repository.DateRepository;
import com.neotech.timeprinter.service.ConsumerService;
import com.neotech.timeprinter.service.TimeQueue;
import com.neotech.timeprinter.entity.Date;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final TimeQueue queue;
    private final DateRepository repository;

    public ConsumerServiceImpl(TimeQueue queue, DateRepository repository) {
        this.queue = queue;
        this.repository = repository;
    }

    private void save() throws InterruptedException {
        Timestamp timestamp = queue.pop();
        repository.save(new Date(null, timestamp));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                save();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
