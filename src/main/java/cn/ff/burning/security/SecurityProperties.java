package cn.ff.burning.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security 配置类
 */
@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private String jwtHeader;
    private String jwtPrefix;
    private String jwtSecret;
    private long jwtExpiration;
    private String authenticationUrlPassword; // 登陆url - 密码登陆
    private String authenticationUrlMobile; // 登陆url - 手机号登陆
    private String authenticationUrlIdcard; // 登陆url - 身份证登陆
    private List<String> unCheckUrl;

    public String[] unCheckUrlArray() {
        String[] arr = new String[unCheckUrl.size()];
        return unCheckUrl.toArray(arr);
    }
}
