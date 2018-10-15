package cn.ff.burning.service;

import cn.ff.burning.entity.SysUser;
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
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    private final SysUserMapper sysUserMapper;

    public List<SysUser> getList(){
        return sysUserMapper.selectList(null);
    }

    public SysUser getByName(String userName){
        return sysUserMapper.getByUserName(userName);

    }


}
