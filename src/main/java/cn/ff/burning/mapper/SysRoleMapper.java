package cn.ff.burning.mapper;

import cn.ff.burning.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    Page<SysRole> getList(Page<SysRole> page);

}
