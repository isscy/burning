package cn.ff.burning.service;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.SysUserMapper;
import cn.ff.burning.security.SecurityProperties;
import cn.ff.burning.security.TokenAuthenticationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    private final SysUserMapper sysUserMapper;
    private final SecurityProperties securityProperties;

    /**
     * 获取后台对用户列表
     * @return
     */
    public Page<Map> geBacktList(Page<Map> page) {
        return sysUserMapper.getBackUsers(page);
    }

    /**
     * 通过用户名获取用户
     */
    public SysUser getByName(String userName) {
        return sysUserMapper.getByUserName(userName);

    }

    /**
     * 通过手机号获取用户
     */
    public SysUser getByPhone(String phone) {
        return sysUserMapper.getByPhone(phone);

    }
    /**
     * 注册
     */
    public Map<String, String> regist(String phone){
        SysUser user = new SysUser(true, phone);
        user.setAvatar("/public/USER_DEFAULT.jpeg");
        sysUserMapper.insert(user);
        // 返回用户信息免登陆
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("authorities", "ROLE_FULLY");
        String token = TokenAuthenticationService.createJwtToken(user.getUsername(), claims);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("userName", user.getUsername());
        result.put("nickName", user.getNickName());
        result.put("avatar", user.getAvatar());
        return result;

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


    /**
     * 重置密码
     */
    public void rePwd(String userId, String newPwd) {
        String encode = new BCryptPasswordEncoder().encode(newPwd);
        sysUserMapper.updatePwdById(userId, encode);

    }
}
