package cn.ff.burning.mapper;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<Map> getBackUsers(Page<Map> page);


    SysUser getByUserName(String userName);

    SysUser getByPhone(String phone);

    void updatePwdById(@Param("id") String id, @Param("pwd") String pwd);


    /**
     * 获取一个用户的所有角色
     */
    List<SysRole> getOneUserAllRoles(String userId);

    /**
     * 获取用户信息
     */
    SysUser getUserInfo(String userId);


    /**
     * 获取普通用户的列表
     */
    Page<SysUser> getCustomerList(Page<SysUser> page, @Param("status")String status,@Param("search")String search );
    /**
     * 更改用户的状态
     */
    void updateCustomerStatus(@Param("id") String id, @Param("status") String status);



}
