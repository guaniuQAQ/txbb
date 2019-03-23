package util;

import entity.User;

public class ResultUtil {

    private static final String pre = "http://192.168.183.134/";

    public static void AddPre(User user) {
        user.setHeadUrl(user.getHeadUrl() == null ? null : pre + user.getHeadUrl());
        user.setHeadUrlSmall(user.getHeadUrlSmall() == null ? null : pre + user.getHeadUrlSmall());
        user.setQrCode((user.getQrCode() == null ? null : pre + user.getQrCode()));
    }
}
