package cn.ff.burning.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    private LocalDateTime createTime;
    private String delFlag;

    private List<SysRole> roles;


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
