package com.wrf.websocket.handler;

import entity.WsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HeartHandler extends SimpleChannelInboundHandler<WsMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WsMsg wsMsg) throws Exception {
        if (wsMsg.getType() == 2) {
            //心跳
        }else {
            ctx.fireChannelRead(wsMsg);
        }
    }
}
