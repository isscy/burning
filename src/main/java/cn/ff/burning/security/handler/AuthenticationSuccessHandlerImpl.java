package cn.ff.burning.security.handler;

import cn.ff.burning.entity.LogLogin;
import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.LogLoginMapper;
import cn.ff.burning.security.TokenAuthenticationService;
import cn.ff.burning.utils.BaseUtil;
import cn.ff.burning.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LogLoginMapper logLoginMapper;

    @Override
    // protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        //TokenAuthenticationService.addAuthentication(response, auth.getName());
        Map<String, Object> claims = new HashMap<>();
        SysUser user = (SysUser) authentication.getPrincipal();
        try {
            if (HttpMethod.POST.name().equals(request.getMethod())){
                new Thread( () -> {this.createLoginLog(request, user);}).start();
            }
        }catch (Exception e){
            logger.error("保存登陆日志错误！");
        }
        claims.put("userId", user.getId());
        claims.put("authorities", user.getRolesString());
        String token = TokenAuthenticationService.createJwtToken(authentication.getName(), claims);
        try {
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("userId", user.getId());
            result.put("userName", user.getUsername());
            result.put("nickName", user.getNickName());
            result.put("avatar", user.getAvatar());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
//            response.getOutputStream().println(new R(result).success().asJson());
            response.getOutputStream().write(new R(result).success().asJson().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        this.handle(request, response, authentication);
//        this.clearAuthenticationAttributes(request);

    }

    private void createLoginLog(HttpServletRequest request, SysUser user) {
        String browser = WebUtil.getBrowser(request);
        String ip = WebUtil.getIp(request);
        String os = WebUtil.getSysInfo(request);
        LogLogin log = new LogLogin();
        log.setUserId(user.getId());
        log.setUserType(user.getType());
        log.setBrowser(browser);
        log.setCreateTime(new Date());
        log.setIp(ip);
        log.setOs(os);
        log.setSource(request.getHeader("Referer"));//访问来源
        log.setId(BaseUtil.getUUID());
        logLoginMapper.insert(log);
    }
}
