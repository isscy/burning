package cn.ff.burning.service;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.SysUserMapper;
import cn.ff.burning.security.SecurityProperties;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    private final SysUserMapper sysUserMapper;
    private final SecurityProperties securityProperties;

    public List<SysUser> getList() {
        return sysUserMapper.selectList(null);
    }

    /**
     * 通过用户名获取用户
     */
    public SysUser getByName(String userName) {
        return sysUserMapper.getByUserName(userName);

    }
    /**
     * 注册
     */
    public void regist(String phone){
        SysUser user = new SysUser(true, phone);
        sysUserMapper.insert(user);

    }

    /**
     * 获取用户信息
     */
    public SysUser userInfo(String userId){
        SysUser user = sysUserMapper.getUserInfo(userId);
        List<SysRole> roles = sysUserMapper.getOneUserAllRoles(userId);
        user.setRoles(roles);
        return user;

    }


    /**
     * 判断用户是否可以登陆此端
     */
    public boolean isUserTypeCorrect(SysUser userParam) {
        String applyUserType = "";
        if (securityProperties.getLoginsourceWeb().equals(userParam.getLoginSource()))
            applyUserType = "1";
        else if (securityProperties.getLoginsourceBgm().equals(userParam.getLoginSource()))
            applyUserType = "2";
        else
            return false;
        SysUser db = sysUserMapper.getByUserName(userParam.getUsername());
        if (db == null)
            return false;
        return applyUserType.equals(db.getType());

    }

    public boolean isUserTypeCorrect(Map<String, String> map) {
        SysUser user = new SysUser();
        user.setUserName(map.get(BaseConstant.DEFAULT_PARAMETER_NAME_MOBILE));
        user.setLoginSource(map.get(BaseConstant.DEFAULT_PARAMETER_NAME_SOURCE));
        return isUserTypeCorrect(user);

    }


}
