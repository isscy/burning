package cn.ff.burning.mapper;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {


    SysUser getByUserName(String userName);

    /**
     * 获取一个用户的所有角色
     */
    List<SysRole> getOneUserAllRoles(String userId);


}
