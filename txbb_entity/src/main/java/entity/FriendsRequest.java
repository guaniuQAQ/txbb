package entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FriendsRequest implements Serializable {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String content;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private User fromUser;

}
