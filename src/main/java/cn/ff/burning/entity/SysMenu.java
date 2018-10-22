package cn.ff.burning.entity;

import lombok.Data;

import java.util.Date;
@Data
public class SysMenu {
    private String id;

    private String type;

    private String menuName;

    private String parentId;

    private Integer level;

    private String url;

    private String visible;

    private Integer orderNum;

    private String perms;

    private String icon;

    private String createBy;

    private Date createTime;


}