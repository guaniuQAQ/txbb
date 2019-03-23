package com.wrf.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TxbbChatServer implements CommandLineRunner {

    private static ServerBootstrap server = new ServerBootstrap();
    private static EventLoopGroup boosGroup = new NioEventLoopGroup();
    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    @Value("${ws.ip}")
    private String ip;

    @Value("${ws.port}")
    private String port;

    @Value("${zk.host}")
    private String zkHost;

    @Autowired
    private TxbbServerChannelInitializer txbbServerChannelInitializer;

    @Override
    public void run(String... args) throws Exception {
        startServer();
    }

    public void startServer() {
        try {
            server
                    .group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(txbbServerChannelInitializer);

            ChannelFuture f = server.bind(9000).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("服务器{}启动成功", port);
                        //将Ip和端口号注册到zookeeper上
                        if (!connectZk()) {
                            log.info("服务器地址保存zookeeper失败...");
                            WsDestroy();
                            return;
                        }
                        log.info("服务器地址保存zookeeper成功...");
                    } else {
                        log.info("服务器启动失败");
                    }
                }
            });

            //给通道设置关闭监听,并同步阻塞
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WsDestroy();
        }
    }


    private boolean connectZk() {
        try {
            //连接zookeeper
            ZooKeeper zk = new ZooKeeper(zkHost, 3000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                }
            });
            ZooKeeper.States state = zk.getState();


            if (state.isAlive()) {
                //已连接
                Stat exists = zk.exists("/netty", new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {

                    }
                });
                if (exists == null) {
                    //先创建父节点/netty
                    zk.create("/netty", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                String host = zk.create("/netty/server", (ip + ":" + port).getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                return host == null ? false : true;
            }
            return false;
        } catch (Exception e) {
            WsDestroy();
            log.error("连接zookeeper发生异常,异常信息为: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void WsDestroy() {
        log.info("服务关闭...");
        // Shut down all event loops to terminate all threads.
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public static void main(String[] args){
        try {
            ZooKeeper zk = new ZooKeeper("192.168.183.135:2181", 3000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("事件: " + watchedEvent.getType());
                }
            });
            zk.create("/aaa",null ,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {

        }
    }
}
