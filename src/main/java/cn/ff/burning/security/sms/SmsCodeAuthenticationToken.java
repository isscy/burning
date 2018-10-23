package cn.ff.burning.security.sms;

import cn.ff.burning.constant.BaseConstant;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * 短信登录验证信息封装类
 *
 * @author ff 201810022
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {


    private final Object principal;  // 这是手机号啊
    public String credentials;         // 这是你验证码

    /**
     * SmsCodeAuthenticationFilter中构建的未认证的Authentication
     */
    public SmsCodeAuthenticationToken(Map<String, String> map) {
        super(null);
        String mobile = map.get(BaseConstant.DEFAULT_PARAMETER_NAME_MOBILE);
        this.principal = mobile == null ? "" : mobile.trim();
        credentials = map.get(BaseConstant.DEFAULT_PARAMETER_NAME_CKCODE);
        setAuthenticated(false);
    }

    /**
     * SmsCodeAuthenticationFilter中构建的未认证的Authentication
     * @deprecated
     */
    public SmsCodeAuthenticationToken(String mobile) {
        super(null);
        this.principal = mobile;
        setAuthenticated(false);
    }

    /**
     * SmsCodeAuthenticationProvider中构建已认证的Authentication
     */
    public SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
