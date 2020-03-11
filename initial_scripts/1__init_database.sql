CREATE DATABASE time_printer;

CREATE TABLE time_printer.times
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_time TIMESTAMP NOT NULL
);

CREATE DATABASE time_printer_test;
CREATE TABLE time_printer_test.times
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_time TIMESTAMP NOT NULL
);