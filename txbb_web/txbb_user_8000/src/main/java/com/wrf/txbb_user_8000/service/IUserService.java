package com.wrf.txbb_user_8000.service;

import entity.User;

import java.util.Map;

public interface IUserService {
    Integer register(User user);

    User login(User user);

    Integer updHead(Map<String,String> headMap);

    User getByUsername(String username);
}
