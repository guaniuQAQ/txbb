package com.wrf.dao;

import entity.ChatRecord;
import entity.WsMsg;

import java.util.List;

public interface IChatRecordDao {
    Integer insert(ChatRecord chatRecord);

    Integer readMsg(WsMsg wsMsg);

    Integer msgReceived(Integer id);

    List<ChatRecord> unReceivedMsgs(Integer toId);
}
