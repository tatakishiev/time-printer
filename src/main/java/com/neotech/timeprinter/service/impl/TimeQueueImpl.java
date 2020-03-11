package com.neotech.timeprinter.service.impl;

import com.neotech.timeprinter.service.TimeQueue;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TimeQueueImpl implements TimeQueue {

    private BlockingQueue<Timestamp> queue;

    public TimeQueueImpl() {
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void push(Timestamp timestamp) {
        queue.add(timestamp);
    }

    @Override
    public Timestamp pop() throws InterruptedException {
        return queue.take();
    }
}
