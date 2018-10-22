package cn.ff.burning.security;

import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.utils.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理登陆的过滤器
 *
 * @author ff 20181013
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(url);
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
            throw new AuthMethodNotSupportedException("登陆仅支持POST方法和Ajax请求");
        }
//        SysUser user = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);
        SysUser user = new ObjectMapper().readValue(request.getReader(), SysUser.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String browser = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "";
        String ip = request.getRemoteAddr();
        //TokenAuthenticationService.addAuthentication(response, auth.getName());
        Map<String, Object> claims = new HashMap<>();
        SysUser user = (SysUser) auth.getPrincipal();
        claims.put("userId", user.getId());
        claims.put("authorities", user.getRolesString());
        String token = TokenAuthenticationService.createJwtToken(auth.getName(), claims);
        try {
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("userId", user.getId());
            result.put("userName", user.getUsername());
            result.put("nickName", user.getNickName());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
//            response.getOutputStream().println(new R(result).success().asJson());
            response.getOutputStream().write(new R(result).success().asJson().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
//        response.getOutputStream().println(new R(500, "Login Error!" + failed.getMessage(), null).asJson());
        response.getOutputStream().write(new R(500, "Login Error!" + failed.getMessage(), null).asJson().getBytes(StandardCharsets.UTF_8));
    }


}
