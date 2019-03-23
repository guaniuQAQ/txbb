package com.wrf.txbb_user_8000.dao;

import entity.User;

public interface IUserDao {
    Integer insert(User user);

    User getByUsername(String username);

    Integer updHead(User user);
}
