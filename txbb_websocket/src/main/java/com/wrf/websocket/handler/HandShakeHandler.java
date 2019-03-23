package com.wrf.websocket.handler;

import com.wrf.websocket.ChannelContainer;
import entity.WsMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class HandShakeHandler extends SimpleChannelInboundHandler<WsMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WsMsg wsMsg) throws Exception {

        Channel channel = ctx.channel();
        if(wsMsg.getType() == 1){
            //握手消息,将设备id保存进map
            ChannelContainer.put(wsMsg.getUuid(), channel);
            log.info("握手成功,当前管理的channel数量: {}",ChannelContainer.size());

        }else{
            ctx.fireChannelRead(wsMsg);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端{}已连接",ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ChannelContainer.remove(ctx.channel());
        log.info("客户端{}已断开,当前管理的channel数量: {}",ctx.channel().remoteAddress(),ChannelContainer.size());

    }
}
