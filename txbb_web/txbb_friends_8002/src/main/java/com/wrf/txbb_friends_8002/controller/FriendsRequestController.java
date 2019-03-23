package com.wrf.txbb_friends_8002.controller;

import com.wrf.txbb_friends_8002.service.IFriendsService;
import com.wrf.txbb_web_api.feign.ChatFeign;
import entity.Friends;
import entity.FriendsRequest;
import entity.WsMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.GlobalConstant;
import util.ResultData;

import java.util.List;

@RestController
@RequestMapping("/friends")
@Slf4j
public class FriendsRequestController {

    @Autowired
    private IFriendsService friendsService;

    @Autowired
    private ChatFeign chatFeign;

    /**
     * 申请好友
     * @param friendsRequest
     * @return
     */
    @RequestMapping("/requestFriends")
    public ResultData requestFriends(FriendsRequest friendsRequest) {
        log.info("{}请求加{}为好友",friendsRequest.getFromId(),friendsRequest.getToId());
        //是否重复请求
        boolean isRepeated = friendsService.isRepeated(friendsRequest);
        if (isRepeated) {
            return ResultData.errorResultData(GlobalConstant.REPEATED_REQUEST, "重复请求");
        }
        //是否已是好友关系
        boolean isFriends = friendsService.isFriends(friendsRequest);
        if (isFriends) {
            return ResultData.errorResultData(GlobalConstant.IS_FRIENDS, "已经是好友关系");
        }
        Integer result = friendsService.insert(friendsRequest);
        if (result > 0) {
            //通知对方
            WsMsg wsMsg = new WsMsg(friendsRequest.getFromId(), friendsRequest.getToId(), GlobalConstant.WS_MSG_REQUEST_FRIENDS,null, null, null,null,null);
            chatFeign.wsMsg(wsMsg);
            return ResultData.succResultData(null);
        }

        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.UNKNOW_ERROR_MSG);
    }

    /**
     * 查询我的好友请求
     * @param toId
     * @return
     */
    @RequestMapping("/searchMyRequest")
    public ResultData searchMyRequest(Integer toId) {
        List<FriendsRequest> list = friendsService.searchMyRequest(toId);
        return ResultData.succResultData(list);
    }

    /**
     * 处理好友申请
     * @param friendsRequest
     * @return
     */
    @RequestMapping("/handleRequest")
    public ResultData handleRequest(FriendsRequest friendsRequest) {
        Integer result = friendsService.handleRequest(friendsRequest);
        if (result > 0) {
            return ResultData.succResultData(null);
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, GlobalConstant.UNKNOW_ERROR_MSG);
    }

    /**
     * 查询好友列表
     * @param uid
     * @return
     */
    @RequestMapping("/searchFriendsList")
    public ResultData searchFriendsList(Integer uid) {
        List<Friends> list = friendsService.searchFriendsList(uid);
        return ResultData.succResultData(list);
    }
}
