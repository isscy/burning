package cn.ff.burning.entity;

import lombok.Data;

import java.util.Date;

/**
 * 登陆日志表
 */
@Data
public class LogLogin {
    private String id;
    private String userType;
    private String userId;
    private String ip;
    private String browser;
    private String os;
    private String source;
    private Date createTime;
}
