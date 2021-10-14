package com.yhkim.developutility;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.Security;

@SpringBootApplication
public class YhkimDevelopUtilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(YhkimDevelopUtilityApplication.class, args);
    }

    @Bean
    InitializingBean initializingBean () {
        Security.addProvider(new BouncyCastleProvider());
        return null;
    }

}
