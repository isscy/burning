package cn.ff.burning.security;

import cn.ff.burning.entity.R;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截所有需要jwt的请求 然后调用TokenAuthenticationService类的静态方法去做jwt验证
 * @author ff 20181018
 */
public class JWTAuthenticationFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            Authentication authentication = TokenAuthenticationService.getAuthentication(req);
            if (authentication != null)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            else
                SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
        }catch (JwtException e){
            //resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
        }finally {
            SecurityContextHolder.getContext().setAuthentication(null);// 先走filter的传递 再finally
        }
    }



//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//
//        Authentication authentication = TokenAuthenticationService.getAuthentication(req);
//        /*SecurityContextHolder.getContext().setAuthentication(authentication);
//        filterChain.doFilter(request,response);*/
//        if (authentication != null) {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        } else {
//            /* 返回页面
//            res.setStatus(200);
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/backmanage/indexInSys");
//            dispatcher.forward(request, res);*/
//
//            /* 返回json
//            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
//            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//            resp.getOutputStream().println(new R(0, "not certified", null).asJson());*/
//            SecurityContextHolder.getContext().setAuthentication(null);
//            filterChain.doFilter(request, response);
//        }
//
//
//    }

}
