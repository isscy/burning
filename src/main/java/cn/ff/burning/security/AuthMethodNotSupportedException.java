package cn.ff.burning.security;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * 权限验证时方法不支持错误
 * @author ff 20181013
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
