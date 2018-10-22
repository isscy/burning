package cn.ff.burning.service;

import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.SysUserMapper;
import cn.ff.burning.security.SecurityProperties;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    private final SysUserMapper sysUserMapper;
    private final SecurityProperties securityProperties;

    public List<SysUser> getList(){
        return sysUserMapper.selectList(null);
    }

    public SysUser getByName(String userName){
        return sysUserMapper.getByUserName(userName);

    }




    /**
     * 判断用户是否可以登陆此端
     */
    public boolean isUserTypeCorrect(SysUser userParam){
        String applyUserType=  "";
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


}
