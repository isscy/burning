package cn.ff.burning.security;

import cn.ff.burning.entity.BaseKv;
import cn.ff.burning.entity.SysMenu;
import cn.ff.burning.mapper.SysMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Map<String, Collection<ConfigAttribute>> urlRoleMap = null;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();    //String httpMethod = fi.getRequest().getMethod();
        if (urlRoleMap == null)
            loadResourceDefine();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {
                return entry.getValue();//return SecurityConfig.createList(entry.getValue());
            }
        }
//        return null;
        return SecurityConfig.createList("ROLE_FULLY");//没有匹配到,默认是要登录才能访问



    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    /**
     * 加载权限表中所有权限
     */
    public void loadResourceDefine() {
        urlRoleMap = new HashMap<>();
        List<BaseKv> menus = sysMenuMapper.getAllAuthority();
        for (BaseKv kv : menus) {
            Collection<ConfigAttribute> array = new ArrayList<>();
            ConfigAttribute cfg = new SecurityConfig(kv.getValue());
            //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
            array.add(cfg);
            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
            urlRoleMap.put(kv.getKey(), array);
        }

    }


}
