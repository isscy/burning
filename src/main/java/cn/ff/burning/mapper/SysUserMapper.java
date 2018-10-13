package cn.ff.burning.mapper;
import cn.ff.burning.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {


    SysUser getByUserName(String userName);


}
