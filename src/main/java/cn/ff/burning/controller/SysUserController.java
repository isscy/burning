package cn.ff.burning.controller;

import cn.ff.burning.entity.R;
import cn.ff.burning.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sysUser")
@RestController
public class SysUserController {
    @Autowired
    public SysUserController(SysUserService sysUserService){
        this.sysUserService = sysUserService;
    }
    private final SysUserService sysUserService;


    @GetMapping("/list")
    public R list(){
        return new R(sysUserService.getList()).success();

    }
}
