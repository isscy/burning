package cn.ff.burning.controller;

import cn.ff.aliyunspringbootstarter.help.AliyunMnsRequest;
import cn.ff.aliyunspringbootstarter.template.AliyunMnsTemplate;
import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.R;
import cn.ff.burning.utils.BaseUtil;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private AliyunMnsTemplate aliyunMnsTemplate;

    /**
     * 获取验证码 ： 前端登陆用
     */
    @GetMapping("/noAuth/test-1/{phone}")
    public R loginCode(@PathVariable String phone) {

        String code = BaseUtil.getSmsCode(6);
        try {
            aliyunMnsTemplate.sendMsg(new AliyunMnsRequest(BaseConstant.TEMP_SMS_LOGIN, phone).setParam("code", code));
            //redisTemplate.opsForValue().set(BaseConstant.REDIS_SMS_LOGIN + phone, code, BaseConstant.REDIS_SMS_OUTTIMW, TimeUnit.MINUTES);
        } catch (ClientException e) {
            LOGGER.error(e.getMessage());
            return new R().fail("发送短信失败！");
        }
        LOGGER.info("发短信-登陆 " + phone + " : " + code);
        return new R().success();
    }
}
