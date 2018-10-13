package cn.ff.burning.security;

import cn.ff.burning.entity.R;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT 生成 及 验证
 *
 * @author ff 20181013
 */
@Component
public class TokenAuthenticationService {

    @Autowired
    private SecurityProperties securityProperties;
    private static TokenAuthenticationService tokenAuthenticationService;

    @PostConstruct
    public void init() {
        tokenAuthenticationService = this;
        tokenAuthenticationService.securityProperties = this.securityProperties;
    }


    /**
     * JWT 生成方法
     */
    public static void addAuthentication(HttpServletResponse response, String username) {
        addAuthentication(response, username, null);
    }
    public static void addAuthentication(HttpServletResponse response, String username, Map<String, Object> claims) {
        //生成jwt
        String jwt = Jwts.builder()
                //.claim("authorities", "ROLE_ADMIN,ROLE_ADMIN")//保存权限（角色）
                .setClaims(claims) //todo 处理角色
                .setSubject(username)//用户名写入标题
                .setExpiration(new Date(System.currentTimeMillis() + tokenAuthenticationService.securityProperties.getJwtExpiration()))
                .signWith(SignatureAlgorithm.HS512, tokenAuthenticationService.securityProperties.getJwtSecret())// 签名设置
                .compact();
        // 将jwt写入body
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().println(new R(jwt).success().asJson());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * jwt 验证方法
     */
    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(tokenAuthenticationService.securityProperties.getJwtHeader());//从header中拿到token
        if (token != null) {// 解析token
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenAuthenticationService.securityProperties.getJwtSecret())
                    .parseClaimsJws(token.replace(tokenAuthenticationService.securityProperties.getJwtPrefix(), ""))
                    .getBody();
            // 用户名 和 权限（角色）
            String user = claims.getSubject();
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, authorities) : null;

        }
        return null;
    }


}
