package com.wrf.websocket;

import com.wrf.websocket.handler.ChatHandler;
import com.wrf.websocket.handler.HandShakeHandler;
import com.wrf.websocket.handler.HeartHandler;
import com.wrf.websocket.handler.TextFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TxbbServerChannelInitializer extends ChannelInitializer {


    @Autowired
    private TextFrameHandler textFrameHandler;

    @Autowired
    private HandShakeHandler handShakeHandler;

    @Autowired
    private HeartHandler heartHandler;

    @Autowired
    private ChatHandler chatHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //http编解码
        pipeline.addLast(new HttpServerCodec());
        //分块写,防止文件过大撑爆内存
        pipeline.addLast(new ChunkedWriteHandler());
        //http消息聚合器
        pipeline.addLast(new HttpObjectAggregator(1024 * 512));
        //websocket升级握手链接
        pipeline.addLast(new WebSocketServerProtocolHandler("/txbb"));
        //心跳监听,10S没收到断开连接
        pipeline.addLast(new ReadTimeoutHandler(10000,TimeUnit.MILLISECONDS));
        //消息格式校验
        pipeline.addLast(textFrameHandler);
        //握手消息
        pipeline.addLast(handShakeHandler);
        //心跳消息
        pipeline.addLast(heartHandler);
        //单聊消息
        pipeline.addLast(chatHandler);


    }
}
