package util;

public interface GlobalConstant {

    /**
     * CODE
     */
    String SUCC_CODE = "8888";
    String ERROR_CODE = "4444";
    String USER_EXIST_CODE = "4445";
    String REPEATED_REQUEST = "4446";
    String IS_FRIENDS = "4447";

    /**
     * MSG
     */
    String SUCC_MSG = "SUCCESS";
    String REG_MSG_SUCC = "注册成功!";
    String REG_MSG_ERROR = "注册失败!";
    String LOGIN_MSG_SUCC = "登录成功";
    String USER_EXIST_MSG = "用户已存在!";
    String UNKNOW_ERROR_MSG = "未知错误!";
    String LOGIN_INFO_WRONG_MSG = "用户名或密码错误";

    /**
     * CONSTANT
     */
    String QR_CODE_PRE = "txbb:";

    Integer WS_MSG_HANDSHAKE = 1;
    Integer WS_MSG_HEART = 2;
    Integer WS_MSG_DOWNLINE = 100;
    Integer WS_MSG_REQUEST_FRIENDS = 101;
    Integer WS_MSG_CHAT = 102;

}
