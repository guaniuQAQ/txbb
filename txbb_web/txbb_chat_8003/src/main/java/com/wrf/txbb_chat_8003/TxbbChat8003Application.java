package com.wrf.txbb_chat_8003;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.wrf")
@EnableEurekaClient
@MapperScan("com.wrf.dao")
public class TxbbChat8003Application {

    public static void main(String[] args) {
        SpringApplication.run(TxbbChat8003Application.class, args);
    }

}
