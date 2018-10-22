package cn.ff.burning.config;

import cn.ff.burning.security.*;
import cn.ff.burning.security.handler.AuthenticationSuccessHandlerImpl;
import cn.ff.burning.security.sms.SmsCodeAuthenticationSecurityConfig;
import cn.ff.burning.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Order(1)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSourceImpl;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 不创建session
                //.authorizeRequests()
                //.antMatchers(securityProperties.unCheckUrlArray()).permitAll() //配在这里无效 需要放在WebSecurity里
                //.anyRequest().authenticated()

                //.and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .addFilterBefore(new GlobalCorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jWTLoginFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSourceImpl);
                        fsi.setAccessDecisionManager(myAccessDecisionManager());
                        return fsi;
                    }
                });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(securityProperties.unCheckUrlArray());
    }

    @Bean
    public JWTLoginFilter jWTLoginFilter() throws Exception {
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(securityProperties.getAuthenticationUrlPassword(), authenticationManager(), sysUserService);
        jwtLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        jwtLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return jwtLoginFilter;
    }

    @Bean
    public AccessDecisionManager myAccessDecisionManager() {
        return new AccessDecisionManagerImpl();
    }


}
