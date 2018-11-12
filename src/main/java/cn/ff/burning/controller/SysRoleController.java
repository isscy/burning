package cn.ff.burning.controller;

import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysRole;
import cn.ff.burning.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RequestMapping("/noAuth/sysRole")
@RestController
public class SysRoleController {
    @Autowired
    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    private final SysRoleService sysRoleService;


    @GetMapping("/sysRole/list/admin")
    public R list(@RequestParam(required = false, defaultValue = "1") Integer current, @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<SysRole> list = sysRoleService.getList(new Page<SysRole>(current, size));
        return new R(list).success();

    }



}
