package com.ilongli.satoken.client1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SSO模式一，Client端 Demo 
 */
@SpringBootApplication
public class SaSso1ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaSso1ClientApplication.class, args);
        System.out.println("\nSa-Token SSO模式一 Client端启动成功");
    }
}
