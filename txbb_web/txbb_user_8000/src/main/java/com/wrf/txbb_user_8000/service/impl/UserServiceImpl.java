package com.wrf.txbb_user_8000.service.impl;

import com.wrf.txbb_user_8000.dao.IUserDao;
import com.wrf.txbb_user_8000.service.IUserService;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.MD5Util;

import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public Integer register(User user) {
        User u = userDao.getByUsername(user.getUsername());
        if (u != null) {
            return -1;
        }
        try {
            user.setPassword(MD5Util.md5(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDao.insert(user);
    }

    @Override
    public User login(User user) {
        User u = userDao.getByUsername(user.getUsername());
        try {
            if (u != null && MD5Util.verify(user.getPassword(),u.getPassword())) {
                //succ
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer updHead(Map<String, String> headMap) {
        Integer result = 0;
        try {
            String headUrl = headMap.get("headUrl");
            String headUrlSmall = headMap.get("headUrlSmall");
            String id = headMap.get("id");
            User user = new User();
            user.setHeadUrl(headUrl);
            user.setHeadUrlSmall(headUrlSmall);
            user.setId(Integer.parseInt(id));
            result = userDao.updHead(user);
        } catch (NumberFormatException e) {
            log.error("修改头像出现异常==>{}",e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }
}
