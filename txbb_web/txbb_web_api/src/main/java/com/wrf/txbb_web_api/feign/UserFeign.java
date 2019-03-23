package com.wrf.txbb_web_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import util.ResultData;

import java.util.Map;

@FeignClient("TXBB-USER")
public interface UserFeign {

    @RequestMapping("/user/updHead")
    ResultData updHead(@RequestBody Map<String, String> headMap);
}
