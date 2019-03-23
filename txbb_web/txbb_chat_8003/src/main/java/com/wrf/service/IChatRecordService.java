package com.wrf.service;

import entity.ChatRecord;
import entity.WsMsg;

public interface IChatRecordService {
    Integer insert(ChatRecord chatRecord);

    Integer readMsg(WsMsg wsMsg);

    Integer msgReceived(Integer id);

    void unReceivedMsgs(Integer toId);
}
