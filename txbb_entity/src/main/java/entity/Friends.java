package entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Friends implements Serializable {
    private Integer uid;
    private Integer fid;
    private Integer status;
    private Date createTime;

    private User friends;
}
