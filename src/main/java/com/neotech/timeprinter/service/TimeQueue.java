package com.neotech.timeprinter.service;

import java.sql.Timestamp;

public interface TimeQueue {
    void push(Timestamp timestamp);
    Timestamp pop() throws InterruptedException;
}
