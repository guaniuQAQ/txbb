package com.wrf.txbb_web_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("TXBB-RES")
public interface ResFeign {

    @RequestMapping("/res/createQRCode")
    String createQRCode(@RequestParam("username") String username);
}
