package cn.ff.burning.mapper;

import cn.ff.burning.entity.BaseKv;
import cn.ff.burning.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    List<BaseKv> getAllAuthority();


    /**
     * 获取所有目录和菜单及其角色
     */
    List<SysMenu> getUrlAndRole();

    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectMenusByUserId(String userId);
    /**
     * 获取管理员用的所有菜单
     */
    List<SysMenu> selectMenuNormalAll();
}