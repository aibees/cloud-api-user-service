package com.cloud.msa.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsaUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsaUserApplication.class, args);
    }
}
