package com.wrf.rabbitlistener;

import com.google.gson.Gson;
import com.wrf.websocket.ChannelContainer;
import entity.WsMsg;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RabbitListener(queues = "queue_msg_${ws.ip}:${ws.port}")
@Slf4j
public class MsgListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void wsMsg(WsMsg wsMsg) {

        log.info("rabbitmq: {}",wsMsg);
        String uuid = wsMsg.getUuid();

        if (StringUtils.isEmpty(uuid)) {
            //通过toId去redis拿uuid
            uuid = (String) redisTemplate.opsForValue().get(wsMsg.getToId());
        }
        Channel channel = ChannelContainer.getChannel(uuid);
        if (channel != null) {
            //channel不为空才处理
            channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(wsMsg)));
        }

    }
}
