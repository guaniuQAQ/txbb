package com.wrf.txbb_web_api.feign;

import entity.WsMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("TXBB-CHAT")
public interface ChatFeign {

    @RequestMapping("/chat/wsMsg")
    void wsMsg(@RequestBody WsMsg wsMsg);
}
