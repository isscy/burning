package cn.ff.burning.security.sms;

import cn.ff.burning.entity.SysUser;
import cn.ff.burning.security.SecurityProperties;
import cn.ff.burning.security.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 短信登陆过滤器
 * @author ff 20181022
 */

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final SecurityProperties securityProperties;
    private String mobileParameter;
    private boolean postOnly = true; // 仅支持post请求


    protected SmsCodeAuthenticationFilter(SecurityProperties securityProperties) {
        super(new AntPathRequestMatcher(securityProperties.getAuthenticationUrlMobile(), "POST"));
        this.securityProperties = securityProperties;
        this.mobileParameter = securityProperties.DEFAULT_PARAMETER_NAME_MOBILE;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断是是不是post请求
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //从请求中获取手机号码
        String mobile = obtainMobile(request);
        mobile = mobile == null ? "" : mobile.trim();


        //创建SmsCodeAuthenticationToken(未认证)
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        //设置用户信息
        setDetails(request, authRequest);
        //返回Authentication实例
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        try {
            HashMap<String, String> map = new ObjectMapper().readValue(request.getReader(), HashMap.class);
            return map.get(mobileParameter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }

}
