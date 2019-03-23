package com.wrf.websocket.handler;

import com.wrf.txbb_web_api.feign.ChatFeign;
import entity.WsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.GlobalConstant;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<WsMsg> {

    private static final String queue_chat = "queue_chat";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WsMsg wsMsg) throws Exception {
        if (wsMsg.getType() == GlobalConstant.WS_MSG_CHAT) {
            //聊天消息,mq通知通讯服务保存进数据库
            log.info("收到消息==>{}",wsMsg);
            rabbitTemplate.convertAndSend(queue_chat,wsMsg);
        }else {
            ctx.fireChannelRead(wsMsg);
        }
    }
}
