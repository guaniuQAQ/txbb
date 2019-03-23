package entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChatRecord implements Serializable {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private Integer msgType;
    private String content;
    private Date cteateTime;
    private Integer status;
    private Integer received;
}
