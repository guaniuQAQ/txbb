package com.wrf.txbb_user_8000.controller;

import com.wrf.txbb_user_8000.service.IUserService;
import com.wrf.txbb_web_api.feign.ChatFeign;
import com.wrf.txbb_web_api.feign.ResFeign;
import entity.User;
import entity.WsMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.GlobalConstant;
import util.PinyinUtil;
import util.ResultData;
import util.ResultUtil;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ResFeign resFeign;

    @Autowired
    private ChatFeign chatFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public ResultData register(User user) {
        log.info("用户注册==>{}",user);
        //判断用户名是否被注册
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getNickname())) {
            return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.UNKNOW_ERROR_MSG);
        }
        //生成二维码
        String qrCode = resFeign.createQRCode(user.getUsername());
        if (!StringUtils.isEmpty(qrCode)) {
            user.setQrCode(qrCode);
        }
        //生成昵称拼音
        user.setNicknamePinyin(PinyinUtil.ToPinyin(user.getUsername()).toUpperCase());
        Integer result = userService.register(user);
        if (result > 0) {
            return ResultData.succResultData(null);
        } else if (result == -1) {
            return ResultData.errorResultData(GlobalConstant.USER_EXIST_CODE, GlobalConstant.USER_EXIST_MSG);
        }else {
            return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.UNKNOW_ERROR_MSG);
        }
    }

    /**
     * 登录
     * @param user
     * @param uuid
     * @return
     */
    @RequestMapping("/login")
    public ResultData login(User user,String uuid) {
        log.info("用户登录user==>{} uuid:{}",user,uuid);
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.UNKNOW_ERROR_MSG);
        }
        User result = userService.login(user);
        if (result != null) {
            ResultUtil.AddPre(result);
            //判断用户是否已经登录
            String oldUuid = (String) redisTemplate.opsForValue().get(result.getId());
            if (oldUuid != null && !oldUuid.equals(uuid)) {
                //当前用户已登录,强制对方下线
                log.info("用户{}已登录,强制下线...",result.getId());
                chatFeign.wsMsg(new WsMsg(-1,-1,GlobalConstant.WS_MSG_DOWNLINE,null,oldUuid,null,null,null));
            }
            //将本次登录的设备信息存入redis
            redisTemplate.opsForValue().set(result.getId(), uuid);

            return ResultData.succResultData(result);
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.LOGIN_INFO_WRONG_MSG);

    }

    /**
     * 修改头像
     * @param headMap
     * @return
     */
    @RequestMapping("/updHead")
    public ResultData updHead(@RequestBody Map<String,String> headMap) {
        Integer result = userService.updHead(headMap);
        if (result > 0) {
            return ResultData.succResultData(headMap);
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, "头像修改失败!");
    }

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    @RequestMapping("/searchByUsername")
    public ResultData searchByUsername(String username) {
        User user = userService.getByUsername(username);
        if (user != null) {
            ResultUtil.AddPre(user);
            return ResultData.succResultData(user);
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, "查无此人");
    }
}
