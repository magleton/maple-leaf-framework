package com.geoxus;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableRabbit
@EnableAspectJAutoProxy
public class GXCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(GXCoreApplication.class, args);
    }
}
