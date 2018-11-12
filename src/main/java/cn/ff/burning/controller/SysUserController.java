package cn.ff.burning.controller;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.R;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.security.TokenAuthenticationService;
import cn.ff.burning.service.SysUserService;
import cn.ff.burning.utils.BaseUtil;
import cn.redsoft.aliyun.mns.bean.AliyunmnsRequest;
import cn.redsoft.aliyun.mns.template.AliyunmnsTemplate;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@RequestMapping("/noAuth/sysUser")
@RestController
public class SysUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    public SysUserController(SysUserService sysUserService, AliyunmnsTemplate aliyunmnsTemplate, RedisTemplate<String, String> redisTemplate, TokenAuthenticationService tokenAuthenticationService) {
        this.sysUserService = sysUserService;
        this.aliyunmnsTemplate = aliyunmnsTemplate;
        this.redisTemplate = redisTemplate;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    private final SysUserService sysUserService;
    private final AliyunmnsTemplate aliyunmnsTemplate;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final RedisTemplate<String, String> redisTemplate;


    @GetMapping("/sysUser/list/admin")
    public R list(@RequestParam(required = false, defaultValue = "1") Integer current, @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<Map> list = sysUserService.geBacktList(new Page<Map>(current, size));
        return new R(list).success();

    }

    /*@GetMapping("/name/{name}")
    public R list(@PathVariable String name) {
        return new R(sysUserService.getByName(name)).success();
    }*/

    /**
     * 获取用户信息
     */
    @GetMapping("sysUser/userInfo")
    public R userInfo(HttpServletRequest request) {
        try {
            String userId = TokenAuthenticationService.parsrToUserId(request);
            SysUser user = sysUserService.userInfo(userId);
            if (user == null)
                throw new Exception("找不到user");
            return new R(user).success();

        } catch (Exception e) {
            return new R().fail(e.getMessage());
        }
    }


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
        Map<String, String> map = sysUserService.regist(phone);
        return new R(map).success();

    }

    /**
     * 重置密码
     */
    @PostMapping("/noAuth/sysUser/rePwd")
    public R rePwd(@RequestParam String phone, @RequestParam String ckCode, @RequestParam String newPwd) {
        SysUser user = sysUserService.getByName(phone);
        if (user == null)
            return new R().fail("手机号错误！");
        String redisCode = redisTemplate.opsForValue().get(BaseConstant.REDIS_SMS_PWD + phone);
        if (!ckCode.equals(redisCode))
            return new R().fail("验证码错误！");
        sysUserService.rePwd(user.getId(), newPwd);
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

    /**
     * 获取重制密码验证码
     */
    @GetMapping("/noAuth/smsCode-rePwd/{phone}")
    public R rePwdCode(@PathVariable String phone) {
        SysUser user = sysUserService.getByName(phone);
        if (user == null || StringUtils.isEmpty(user.getId()))
            return new R().fail("当前用户无法发起重置密码请求");
        String code = BaseUtil.getSmsCode(6);
        try {
            aliyunmnsTemplate.sendMsg(new AliyunmnsRequest(BaseConstant.TEMP_SMS_PWD, phone).setParam("code", code));
            redisTemplate.opsForValue().set(BaseConstant.REDIS_SMS_PWD + phone, code, BaseConstant.REDIS_SMS_OUTTIMW, TimeUnit.MINUTES);
        } catch (ClientException e) {
            LOGGER.error(e.getMessage());
            return new R().fail("发送短信失败！");
        }
        LOGGER.info("发短信-重置密码 " + phone + " : " + code);
        return new R().success();
    }

}
