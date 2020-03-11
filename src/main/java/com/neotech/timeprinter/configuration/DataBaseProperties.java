package com.neotech.timeprinter.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DataBaseProperties {
    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    @Value("${database.reconnection-interval-sec}")
    private Integer reconnectionTime;

    @Value("${database.connection-validate-timeout-sec}")
    private Integer timeout;
}
