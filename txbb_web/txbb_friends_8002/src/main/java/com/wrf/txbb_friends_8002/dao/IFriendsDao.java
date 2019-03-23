package com.wrf.txbb_friends_8002.dao;

import entity.Friends;
import entity.FriendsRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFriendsDao {
    Integer selectOne(FriendsRequest friendsRequest);

    Integer isFriends(FriendsRequest friendsRequest);

    Integer insert(FriendsRequest friendsRequest);

    List<FriendsRequest> searchMyRequest(Integer toId);

    Integer handleRequest(FriendsRequest friendsRequest);

    FriendsRequest getById(Integer id);

    Integer usedToBeFriends(@Param("fromId") Integer fromId,@Param("toId") Integer toId);

    Integer toBeFriends(@Param("fromId")Integer fromId,@Param("toId") Integer toId);

    Integer updFriends(@Param("fromId")Integer fromId, @Param("toId") Integer toId);

    List<Friends> searchFriendsListByUid(Integer uid);
}
