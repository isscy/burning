package cn.ff.burning.controller;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysMenu;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.security.TokenAuthenticationService;
import cn.ff.burning.service.SysMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SysMenuController {
    @Autowired
    public SysMenuController(SysMenuService sysMenuService){
        this.sysMenuService =sysMenuService;

    }
    private final SysMenuService sysMenuService;

    /**
     * 后台获取所有树
     */
    @GetMapping("/sysMenu/tree/admin")
    public R tree() {
        List<SysMenu> list = sysMenuService.menusByUser(BaseConstant.USER_SUPER_ID);
        return new R(list).success();

    }

    @GetMapping("/noAuth/sysMenu/urlAndRole")
    public R allMenuUrlAndRole(){
        return new R(sysMenuService.allMenuUrlAndRole()).success();

    }

    @GetMapping("/sysMenu/menusByUser")
    public R menusByUser(HttpServletRequest request){
        try {
            String userId = TokenAuthenticationService.parsrToUserId(request);
            List<SysMenu> list = sysMenuService.menusByUser(userId);
            if (list == null || list .size() == 0)
                throw new Exception("无法获取菜单");
            return new R(list).success();

        } catch (Exception e) {
            return new R().fail(e.getMessage());
        }

    }

    @GetMapping("/noAuth/sysMenu/menusByUser")
    public R menusByUser(String userId){
        return new R(sysMenuService.menusByUser(userId)).success();

    }


}
