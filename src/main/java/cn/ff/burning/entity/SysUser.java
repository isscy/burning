package cn.ff.burning.entity;

import java.util.*;

import cn.ff.burning.utils.BaseUtil;
import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * @author ff
 * @since 2018-10-12
 */
@Data
public class SysUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String id;
    private String userName;
    private String password;
    private String nickName;
    private String phone;
    private String status; // 1-正常；2-lock;
    private String type;    // 0 通用 1 前端用户 2后台管理用户
    private Date createTime;
    private String delFlag;


    private String idCard;
    private String email;
    private String sex; // 0 未知 1男 2女
    private String provice;
    private String city;
    private String county;
    private String address;
    private String school;

    private List<SysRole> roles;

    private String loginSource; // 登陆来源


    public SysUser(){}
    public SysUser(boolean isFed, String userName){
        this.userName = userName;
        if (isFed){ // 前端注册的用户
            this.id = BaseUtil.getUUID();
            this.phone = userName;
            this.status = "1";
            this.type = "1";
            this.createTime = new Date();
            this.delFlag = "0";
            this.sex = "0";
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> roles = this.getRoles();
        Optional.ofNullable(roles).orElse(new ArrayList<>()).forEach( e -> authorities.add(new SimpleGrantedAuthority(e.getCode())));
        return authorities;
    }

    public String getRolesString(){
        List<String> codes = new ArrayList<>();
        Optional.ofNullable(roles).orElse(new ArrayList<>()).forEach( e -> codes.add(e.getCode()));
        String str = Joiner.on(",").join(codes);
        return str;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"2".equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "0".equals(delFlag);
    }
}
