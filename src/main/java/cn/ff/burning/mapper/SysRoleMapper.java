package cn.ff.burning.mapper;

import cn.ff.burning.entity.SysRole;

public interface SysRoleMapper {
    int insert(SysRole record);

    int insertSelective(SysRole record);
}