package cn.ff.burning.security.sms;

import cn.ff.burning.security.SecurityProperties;
import cn.ff.burning.security.handler.AuthenticationSuccessHandlerImpl;
import cn.ff.burning.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信登录配置
 *
 * @author ff 20181022
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    SecurityProperties securityProperties;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //自定义SmsCodeAuthenticationFilter过滤器
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter(securityProperties, sysUserService);
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        //设置自定义SmsCodeAuthenticationProvider的认证器userDetailsService
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider(userDetailsService, redisTemplate);
        //在UsernamePasswordAuthenticationFilter过滤前执行
        http.authenticationProvider(smsCodeAuthenticationProvider).addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
