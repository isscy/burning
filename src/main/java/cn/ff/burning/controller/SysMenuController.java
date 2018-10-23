package cn.ff.burning.controller;

import cn.ff.burning.entity.R;
import cn.ff.burning.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysMenuController {
    @Autowired
    public SysMenuController(SysMenuService sysMenuService){
        this.sysMenuService =sysMenuService;

    }
    private final SysMenuService sysMenuService;

    @GetMapping("/noAuth/sysMenu/urlAndRole")
    public R allMenuUrlAndRole(){

        return new R(sysMenuService.allMenuUrlAndRole()).success();

    }


}
