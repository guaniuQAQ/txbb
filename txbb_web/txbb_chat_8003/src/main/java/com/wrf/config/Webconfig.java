package com.wrf.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Webconfig {

    @Value("${zk.host}")
    private String zkHost;

    @Bean
    public ZooKeeper zooKeeper() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(zkHost, 3000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }

}
