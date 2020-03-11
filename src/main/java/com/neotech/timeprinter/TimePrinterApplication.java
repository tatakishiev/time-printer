package com.neotech.timeprinter;

import com.neotech.timeprinter.entity.Date;
import com.neotech.timeprinter.repository.DateRepository;
import com.neotech.timeprinter.service.ConsumerService;
import com.neotech.timeprinter.service.ProducerService;
import com.neotech.timeprinter.service.impl.ConsumerServiceImpl;
import com.neotech.timeprinter.service.impl.ProducerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class TimePrinterApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimePrinterApplication.class);
    private static final int PRODUCING_INTERVAL_SEC = 1;

    private final ProducerService producerService;
    private final ConsumerService consumerService;
    private final DateRepository repository;

    @Autowired
    public TimePrinterApplication(ProducerServiceImpl producer,
                                  ConsumerServiceImpl consumer,
                                  DateRepository repository) {
        this.producerService = producer;
        this.consumerService = consumer;
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TimePrinterApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            LOGGER.info("Application started in writing mode");
            runApplication();
        } else if (args.length == 1 && "-p".equalsIgnoreCase(args[0])) {
            LOGGER.info("Application started in printing mode");
            print();
        } else {
            LOGGER.info("Invalid argument. Please provide -p or no arguments");
        }
    }

    private void runApplication() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(producerService, 0, PRODUCING_INTERVAL_SEC, TimeUnit.SECONDS);
        executor.execute(consumerService);
    }

    private void print() {
        List<Date> dates = repository.findAll();
        System.out.println("-------------------------------------------");
        System.out.println(formatResult(dates));
    }

    private String formatResult(List<Date> dates) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Date date : dates) {
            stringBuilder.append(String.format("{id: %d, time: %tT}\n", date.getId(), date.getTimestamp()));
        }
        return stringBuilder.toString();
    }
}
