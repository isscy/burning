package cn.ff.burning.security.sms;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.security.AuthMethodNotSupportedException;
import cn.ff.burning.security.SecurityProperties;
import cn.ff.burning.service.SysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsCodeAuthenticationFilter.class);

//    private final SecurityProperties securityProperties;
    //private String mobileParameter = BaseConstant.DEFAULT_PARAMETER_NAME_MOBILE;
    private boolean postOnly = true; // 仅支持post请求
    private final SysUserService sysUserService;


    protected SmsCodeAuthenticationFilter(SecurityProperties securityProperties, SysUserService sysUserService) {
        super(new AntPathRequestMatcher(securityProperties.getAuthenticationUrlMobile(), "POST"));
        //this.securityProperties = securityProperties;
        //this.mobileParameter = securityProperties.DEFAULT_PARAMETER_NAME_MOBILE;
        this.sysUserService = sysUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断是是不是post请求
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //从请求中获取手机号码
        HashMap<String, String> map = obtainRequestToMap(request);
        if (!sysUserService.isUserTypeCorrect(map))
            throw new AuthMethodNotSupportedException("当前用户无法在登陆端登陆");
        //创建SmsCodeAuthenticationToken(未认证)
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(map);
        //设置用户信息
        setDetails(request, authRequest);
        //返回Authentication实例
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取手机号
     */
    protected String obtainMobile(HashMap<String, String> map) {
        return map.get(BaseConstant.DEFAULT_PARAMETER_NAME_MOBILE);
    }

    /**
     * 获取验证码
     */
    protected String obtainCkCode(HashMap<String, String> map) {
        return map.get(BaseConstant.DEFAULT_PARAMETER_NAME_CKCODE);
    }

    private HashMap obtainRequestToMap(HttpServletRequest request){
        try {
            return new ObjectMapper().readValue(request.getReader(), HashMap.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return new HashMap();
        }
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /*public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }*/

}
