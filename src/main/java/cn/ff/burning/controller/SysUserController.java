package cn.ff.burning.controller;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.service.SysUserService;
import cn.ff.burning.utils.BaseUtil;
import cn.redsoft.aliyun.mns.bean.AliyunmnsRequest;
import cn.redsoft.aliyun.mns.template.AliyunmnsTemplate;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@RequestMapping("/noAuth/sysUser")
@RestController
public class SysUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    public SysUserController(SysUserService sysUserService, AliyunmnsTemplate aliyunmnsTemplate, RedisTemplate<String, String> redisTemplate) {
        this.sysUserService = sysUserService;
        this.aliyunmnsTemplate = aliyunmnsTemplate;
        this.redisTemplate = redisTemplate;
    }

    private final SysUserService sysUserService;
    private final AliyunmnsTemplate aliyunmnsTemplate;
    private final RedisTemplate<String, String> redisTemplate;


    /*@GetMapping("/list")
    public R list() {
        return new R(sysUserService.getList()).success();

    }

    @GetMapping("/name/{name}")
    public R list(@PathVariable String name) {
        return new R(sysUserService.getByName(name)).success();
    }*/


    /**
     * 注册
     */
    @PostMapping("/noAuth/sysUser/regist")
    public R regist(@RequestParam String phone, @RequestParam String ckCode) {
        SysUser user = sysUserService.getByName(phone);
        if (user != null)
            return new R().fail("手机号已被占用！");
        String redisCode = redisTemplate.opsForValue().get(BaseConstant.REDIS_SMS_REGIST + phone);
        if (!ckCode.equals(redisCode))
            return new R().fail("验证码错误！");
        sysUserService.regist(phone);
        return new R().success();

    }


    /**
     * 获取验证码 ： 前端登陆用
     */
    @GetMapping("/noAuth/smsCode-login/{phone}")
    public R loginCode(@PathVariable String phone) {
        SysUser user = sysUserService.getByName(phone);
        if (user == null || StringUtils.isEmpty(user.getId()))
            return new R().fail("当前用户无法发起登陆请求");
        String code = BaseUtil.getSmsCode(6);
        try {
            aliyunmnsTemplate.sendMsg(new AliyunmnsRequest(BaseConstant.TEMP_SMS_LOGIN, phone).setParam("code", code));
            redisTemplate.opsForValue().set(BaseConstant.REDIS_SMS_LOGIN + phone, code, BaseConstant.REDIS_SMS_OUTTIMW, TimeUnit.MINUTES);
        } catch (ClientException e) {
            LOGGER.error(e.getMessage());
            return new R().fail("发送短信失败！");
        }
        LOGGER.info("发短信-登陆 " + phone + " : " + code);
        return new R().success();
    }


    /**
     * 获取验证码 ： 前端注册用
     */
    @GetMapping("/noAuth/smsCode-regist/{phone}")
    public R registCode(@PathVariable String phone) {
        SysUser user = sysUserService.getByName(phone);
        if (user != null)
            return new R().fail("手机号已被占用！");
        String code = BaseUtil.getSmsCode(6);
        try {
            aliyunmnsTemplate.sendMsg(new AliyunmnsRequest(BaseConstant.TEMP_SMS_REGIST, phone).setParam("code", code));
            redisTemplate.opsForValue().set(BaseConstant.REDIS_SMS_REGIST + phone, code, BaseConstant.REDIS_SMS_OUTTIMW, TimeUnit.MINUTES);
        } catch (ClientException e) {
            LOGGER.error(e.getMessage());
            return new R().fail("发送短信失败！");
        }
        LOGGER.info("发短信-注册 " + phone + " : " + code);
        return new R().success();
    }

}
