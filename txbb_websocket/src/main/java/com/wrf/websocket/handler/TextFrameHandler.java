package com.wrf.websocket.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entity.WsMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Gson gson = new Gson();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        String text = textWebSocketFrame.text();

//        log.info("收到客户端消息: {}",text);
        WsMsg wsMsg = null;

        try {
            wsMsg = gson.fromJson(text, WsMsg.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        if (wsMsg == null) {
            //消息格式不规范
            log.info("不是规范的消息,关闭连接...");
            channel.close();
            return;
        }
        //向后继续传递
        ctx.fireChannelRead(wsMsg);

    }
}
