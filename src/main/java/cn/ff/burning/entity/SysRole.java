package cn.ff.burning.entity;

import lombok.Data;

import java.util.Date;
@Data
public class SysRole {
    private String id;

    private String code;

    private String name;

    private Integer displayNo;

    private String status;

    private String dataScope;

    private String createBy;

    private Date createTime;

    private String delFlag;

    private String remark;


}