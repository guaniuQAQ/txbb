package entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String headUrl;
    private String headUrlSmall;
    private String nicknamePinyin;
    private String qrCode;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
