package cn.ff.burning.controller;

import cn.ff.burning.entity.R;
import cn.ff.burning.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("/noAuth/sysRole")
@RestController
public class SysRoleController {
    @Autowired
    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    private final SysRoleService sysRoleService;



}
