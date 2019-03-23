package com.wrf.txbb_server_10000;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TxbbServer10000Application {

    public static void main(String[] args) {
        SpringApplication.run(TxbbServer10000Application.class, args);
    }

}
