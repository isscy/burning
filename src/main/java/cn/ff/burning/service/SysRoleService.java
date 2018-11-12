package cn.ff.burning.service;

import cn.ff.burning.entity.SysRole;
import cn.ff.burning.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    /*public Page<Map> getList(Page<Map> mapPage) {
    }*/

    public Page<SysRole> getList(Page<SysRole> page){
        return sysRoleMapper.getList(page);
    }
}
