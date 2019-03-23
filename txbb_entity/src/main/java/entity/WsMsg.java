package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMsg implements Serializable {
    private Integer fromId;
    private Integer toId;
    /**
     * 1 - 握手
     * 2 - 心跳
     *
     *
     * 100 - 强制下线
     * 101 - 好友申请
     */
    private Integer type;
    /**
     * 消息内容
     * 1 - 文本
     * 2 - 语音
     * 3 - 图片
     */
    private Integer msgType;
    /**
     * 设备标识
     */
    private String uuid;
    private String content;
    /**
     * 数据库主键
     */
    private Integer id;
    /**
     * 状态
     * 0 - 未读
     * 1 - 已读
     */
    private Integer status;

}
