package cn.ff.burning.entity;

import lombok.Data;

import java.util.*;

@Data
public class SysMenu {
    private String id;

    private String type;

    private String name;

    private String title;

    private String parentId;

    private Integer level;

    private String url;

    private String visible;

    private Integer orderNum;

    private String perms;

    private String icon;

    private String createBy;

    private Date createTime;

    /**
     * 这个菜单能被那些角色使用 逗号隔开的角色code字符串
     */
    private String ofRoles;

    private List<SysMenu> children = new ArrayList<SysMenu>();

    private MenuMeta meta = new MenuMeta();



}