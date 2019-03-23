package com.wrf.txbb_websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wrf")
public class TxbbWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxbbWebsocketApplication.class, args);
    }

}
