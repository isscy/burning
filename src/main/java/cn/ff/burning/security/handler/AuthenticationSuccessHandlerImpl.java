package cn.ff.burning.security.handler;

import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.security.TokenAuthenticationService;
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
import java.util.HashMap;
import java.util.Map;
@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {




    @Override
   // protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String browser = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "";
        String ip = request.getRemoteAddr();
        //TokenAuthenticationService.addAuthentication(response, auth.getName());
        Map<String, Object> claims = new HashMap<>();
        SysUser user = (SysUser) authentication.getPrincipal();
        claims.put("userId", user.getId());
        claims.put("authorities", user.getRolesString());
        String token = TokenAuthenticationService.createJwtToken(authentication.getName(), claims);
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

//        this.handle(request, response, authentication);
//        this.clearAuthenticationAttributes(request);

    }
}
