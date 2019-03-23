package com.wrf.controller;

import com.wrf.service.IChatRecordService;
import entity.WsMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.GlobalConstant;
import util.ResultData;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private static final String nettyPath = "/netty";

    private AtomicInteger index = new AtomicInteger(0);

    private static final String exchangeName = "fanout_msg";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IChatRecordService chatRecordService;

    @Autowired
    private ZooKeeper zooKeeper;

    @RequestMapping("/getServer")
    public ResultData getServer() {
        try {
            List<String> children = zooKeeper.getChildren(nettyPath, null);

            if (children != null && children.size() > 0) {
                //获取节点名
                String path = loadBalance(children);
                //获取节点内容
                byte[] data = zooKeeper.getData(nettyPath + "/" + path, null, null);
                String serverHost = new String(data, "utf-8");
                log.info("返回的server地址:{}", serverHost);
                return ResultData.succResultData(serverHost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, "服务器地址获取异常");
    }

    /**
     * 负载均衡
     *
     * @param list
     * @return
     */
    private String loadBalance(List<String> list) {
        int i = index.getAndIncrement() % list.size();
        return list.get(i);
    }

    /**
     * 发给websocket的消息
     *
     * @param wsMsg
     */
    @RequestMapping("/wsMsg")
    public void wsMsg(@RequestBody WsMsg wsMsg) {
        log.info("发送给websocket的消息: {}", wsMsg);
        rabbitTemplate.convertAndSend(exchangeName, "", wsMsg);
    }

    /**
     * 标记消息已读 wsMsg.id为null则改所有,否则改单条
     *
     * @param wsMsg
     * @return
     */
    @RequestMapping("/readMsg")
    public ResultData readMsg(WsMsg wsMsg) {
        Integer result = chatRecordService.readMsg(wsMsg);
        return ResultData.succResultData(null);
    }

    /**
     * 消息接收
     * @param id
     * @return
     */
    @RequestMapping("/msgReceived")
    public ResultData msgReceived(Integer id) {
        Integer result = chatRecordService.msgReceived(id);
        return ResultData.succResultData(null);
    }

    /**
     * 获取未接收的消息
     * @param id
     * @return
     */
    @RequestMapping("/unReceivedMsgs")
    public ResultData unReceivedMsgs(Integer toId) {
        chatRecordService.unReceivedMsgs(toId);
        return ResultData.succResultData(null);
    }

}
