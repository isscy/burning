package cn.ff.burning.controller;

import cn.ff.burning.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/noAuth/hi")
    public R hi(){
        return new R("你好啊").success();
    }

    @GetMapping("/hello")
    public R hello(){
        Map<String, String> map = new HashMap<>();
        map.put("user", "dot");
        map.put("info", "你好哇");
        return new R(map).success();
    }

    @GetMapping("/goo/{name}")
    public R goo(@PathVariable String name){
        Map<String, String> map = new HashMap<>();
        map.put("user", name);
        map.put("info", "goooo");
        return new R(map).success();
    }

}
