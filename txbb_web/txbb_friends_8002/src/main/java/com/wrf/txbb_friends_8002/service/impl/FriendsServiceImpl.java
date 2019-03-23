package com.wrf.txbb_friends_8002.service.impl;

import com.wrf.txbb_friends_8002.dao.IFriendsDao;
import com.wrf.txbb_friends_8002.service.IFriendsService;
import entity.Friends;
import entity.FriendsRequest;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import util.ResultUtil;

import java.util.List;

@Service
@Slf4j
public class FriendsServiceImpl implements IFriendsService {

    @Autowired
    private IFriendsDao friendsDao;


    @Override
    public boolean isRepeated(FriendsRequest friendsRequest) {
        Integer result = friendsDao.selectOne(friendsRequest);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFriends(FriendsRequest friendsRequest) {
        Integer result = friendsDao.isFriends(friendsRequest);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer insert(FriendsRequest friendsRequest) {
        return friendsDao.insert(friendsRequest);
    }

    @Override
    public List<FriendsRequest> searchMyRequest(Integer toId) {
        List<FriendsRequest> list = friendsDao.searchMyRequest(toId);
        for (FriendsRequest fr : list) {
            User fromUser = fr.getFromUser();
            ResultUtil.AddPre(fromUser);
        }

        return list;
    }

    @Override
    @Transactional
    public Integer handleRequest(FriendsRequest friendsRequest) {
        Integer result = 0;
        Integer result2 = 0;
        Integer result3 = 0;
        try {
            FriendsRequest target = friendsDao.getById(friendsRequest.getId());

            target.setStatus(friendsRequest.getStatus());

            result = friendsDao.handleRequest(target);

            if (friendsRequest.getStatus() == 1) {
                //在friends中建立好友关系
                //曾经是否是好友
                Integer count1 = friendsDao.usedToBeFriends(target.getFromId(),target.getToId());
                if (count1 == 0) {
                    result2 = friendsDao.toBeFriends(target.getFromId(),target.getToId());
                }else {
                    result2 = friendsDao.updFriends(target.getFromId(),target.getToId());
                }

                Integer count2 = friendsDao.usedToBeFriends(target.getToId(),target.getFromId());
                if (count2 == 0) {
                    result3 = friendsDao.toBeFriends(target.getToId(),target.getFromId());
                }else {
                    result3 = friendsDao.updFriends(target.getToId(),target.getFromId());
                }
            }
            return 1;
        } catch (Exception e) {
            log.error("处理好友请求发生异常,==>{}",e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 0;
    }

    @Override
    public List<Friends> searchFriendsList(Integer uid) {
        List<Friends> list = friendsDao.searchFriendsListByUid(uid);
        for (Friends f : list) {
            ResultUtil.AddPre(f.getFriends());
        }
        return list;
    }

}
