package cn.ff.burning.security;

import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.getByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException("用户 "+username+" 找不到");
        List<SysRole> roles = sysUserMapper.getOneUserAllRoles(user.getId());
        SysRole loginRole = new SysRole();
        loginRole.setCode("ROLE_FULLY");
        roles.add(0, loginRole);
        user.setRoles(roles);

        return user;
    }



}
