package cn.ff.burning.security.sms;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.SysUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

/**
 * 短信登录验证逻辑
 *
 * @author ff 20181022
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    public SmsCodeAuthenticationProvider(UserDetailsService userDetailsService, RedisTemplate<String, String> redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        // 验证码正确
        String redisCode = redisTemplate.opsForValue().get(BaseConstant.REDIS_SMS_LOGIN + mobile);
        if (authenticationToken.credentials == null || !authenticationToken.credentials.equals(redisCode))
            throw new InternalAuthenticationServiceException("验证码错误！");
        //调用自定义的userDetailsService认证
        SysUser user = (SysUser) userDetailsService.loadUserByUsername(mobile);
        if (user == null || StringUtils.isEmpty(user.getUsername()))
            throw new InternalAuthenticationServiceException("无法获取用户信息");
            //如果user不为空重新构建SmsCodeAuthenticationToken（已认证）
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;



    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}