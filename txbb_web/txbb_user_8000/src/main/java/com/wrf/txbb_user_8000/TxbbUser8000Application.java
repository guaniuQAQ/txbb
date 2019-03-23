package com.wrf.txbb_user_8000;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.wrf.txbb_user_8000.dao")
@EnableFeignClients(basePackages = "com.wrf.txbb_web_api.feign")
public class TxbbUser8000Application {

    public static void main(String[] args) {
        SpringApplication.run(TxbbUser8000Application.class, args);
    }

}
