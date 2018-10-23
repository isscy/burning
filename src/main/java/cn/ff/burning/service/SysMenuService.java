package cn.ff.burning.service;

import cn.ff.burning.entity.SysMenu;
import cn.ff.burning.mapper.SysMenuMapper;
import cn.ff.burning.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {
    private final SysMenuMapper sysMenuMapper;

    /**
     * 获取所有目录和菜单的url 和角色
     */
    public List<SysMenu> allMenuUrlAndRole(){
        return sysMenuMapper.getUrlAndRole();

    }

}
