package com.ilongli.satoken.client3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaSso3ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaSso3ClientApplication.class, args);
        System.out.println("\nSa-Token-SSO Client端启动成功");
    }
}
