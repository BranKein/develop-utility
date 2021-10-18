package com.yhkim.developutility.common;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShutdownManager {

    private final ApplicationContext applicationContext;

    public void initiateShutdown(int returnCode) {
        SpringApplication.exit(applicationContext, () -> returnCode);
    }
}
