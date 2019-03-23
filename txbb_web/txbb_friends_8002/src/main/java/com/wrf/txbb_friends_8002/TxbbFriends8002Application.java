package com.wrf.txbb_friends_8002;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.wrf.txbb_web_api.feign")
@MapperScan("com.wrf.txbb_friends_8002.dao")
public class TxbbFriends8002Application {

    public static void main(String[] args) {
        SpringApplication.run(TxbbFriends8002Application.class, args);
    }

}
