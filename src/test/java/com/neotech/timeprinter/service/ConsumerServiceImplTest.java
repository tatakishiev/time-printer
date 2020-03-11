package com.neotech.timeprinter.service;

import com.neotech.timeprinter.entity.Date;
import com.neotech.timeprinter.repository.DateRepository;
import com.neotech.timeprinter.service.impl.ConsumerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class ConsumerServiceImplTest {

    private TimeQueue queue;
    private DateRepository repository;
    private ConsumerService consumerService;

    @Before
    public void init() {
        queue = Mockito.mock(TimeQueue.class);
        repository = Mockito.mock(DateRepository.class);
        consumerService = new ConsumerServiceImpl(queue, repository);
    }

    @Test
    public void run_ShouldTakeTimestampFromQueueAndPassItToService() throws InterruptedException {
        Timestamp timestamp = new Timestamp(1000);
        Date entity = new Date(null, timestamp);

        when(queue.pop()).thenReturn(timestamp);

        Thread thread = new Thread(consumerService);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(100);
        thread.interrupt();

        verify(queue, atLeastOnce()).pop();
        verify(repository, atLeastOnce()).save(eq(entity));
    }
}
