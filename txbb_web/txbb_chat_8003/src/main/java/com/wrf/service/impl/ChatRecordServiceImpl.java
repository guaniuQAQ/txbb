package com.wrf.service.impl;

import com.wrf.dao.IChatRecordDao;
import com.wrf.service.IChatRecordService;
import entity.ChatRecord;
import entity.WsMsg;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.GlobalConstant;

import java.util.List;

@Service
public class ChatRecordServiceImpl implements IChatRecordService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchangeName = "fanout_msg";

    @Autowired
    private IChatRecordDao chatRecordDao;

    @Override
    public Integer insert(ChatRecord chatRecord) {
        return chatRecordDao.insert(chatRecord);
    }

    @Override
    public Integer readMsg(WsMsg wsMsg) {
        return chatRecordDao.readMsg(wsMsg);
    }

    @Override
    public Integer msgReceived(Integer id) {
        return chatRecordDao.msgReceived(id);
    }

    @Override
    public void unReceivedMsgs(Integer toId) {
        try {
            List<ChatRecord> list = chatRecordDao.unReceivedMsgs(toId);
            if (list != null && list.size() > 0) {
                //转成WsMsg对象并丢到rabbitmq
                WsMsg wsMsg = new WsMsg();
                for (ChatRecord c : list) {
                    wsMsg.setId(c.getId());
                    wsMsg.setContent(c.getContent());
                    wsMsg.setFromId(c.getFromId());
                    wsMsg.setToId(c.getToId());
                    wsMsg.setType(GlobalConstant.WS_MSG_CHAT);
                    wsMsg.setMsgType(c.getMsgType());
                    wsMsg.setStatus(c.getStatus());

                    rabbitTemplate.convertAndSend(exchangeName,"",wsMsg);
                }
            }
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }
}
