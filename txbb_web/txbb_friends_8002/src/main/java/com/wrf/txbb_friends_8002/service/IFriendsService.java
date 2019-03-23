package com.wrf.txbb_friends_8002.service;

import entity.Friends;
import entity.FriendsRequest;

import java.util.List;

public interface IFriendsService {
    boolean isRepeated(FriendsRequest friendsRequest);

    boolean isFriends(FriendsRequest friendsRequest);

    Integer insert(FriendsRequest friendsRequest);

    List<FriendsRequest> searchMyRequest(Integer toId);

    Integer handleRequest(FriendsRequest friendsRequest);

    List<Friends> searchFriendsList(Integer uid);
}
