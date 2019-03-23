package com.wrf.config;

import com.wrf.service.IChatRecordService;
import entity.ChatRecord;
import entity.WsMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue_chat")
@Slf4j
public class RabbitMsgListener {

    private static final String exchangeName = "fanout_msg";

    @Autowired
    private IChatRecordService chatRecordService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void msgDispatcher(WsMsg wsMsg) {
        //收到消息
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setFromId(wsMsg.getFromId());
        chatRecord.setToId(wsMsg.getToId());
        chatRecord.setMsgType(wsMsg.getMsgType());
        chatRecord.setContent(wsMsg.getContent());
        Integer result = chatRecordService.insert(chatRecord);


        if (result > 0) {
            //将消息发给rabbitmq以中转给websocket服务器
//            log.info("回填的ID{}",chatRecord.getId());
            wsMsg.setId(chatRecord.getId());
            wsMsg.setUuid(null);
            rabbitTemplate.convertAndSend(exchangeName,"",wsMsg);
        }
    }
}
