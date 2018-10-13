package cn.ff.burning.security;

import cn.ff.burning.utils.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 当调用需要接口而不带token的时候，返回的结果
 * @author ff 20181013
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        if (WebUtil.isAjax(request))
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "自定义未授权错误： " + e.getMessage());
        else
            response.sendRedirect("/login");

    }



}
