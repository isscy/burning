package cn.ff.burning.constant;

public interface BaseConstant {

    /**
     * Redis
     */
    String REDIS_SMS_LOGIN = "sms_login_";
    String REDIS_SMS_REGIST = "sms_regist_";
    String REDIS_SMS_CONFIRM = "sms_confirm_";
    String REDIS_SMS_PWD = "sms_pwd_";
    int REDIS_SMS_OUTTIMW = 10;


    /**
     * 短信模版
     */
    String TEMP_SMS_LOGIN = "SMS_147035038";    //登陆
    String TEMP_SMS_REGIST = "SMS_147035036";    //注册
    String TEMP_SMS_CONFIRM = "SMS_147035039";    //身份验证
    String TEMP_SMS_PWD = "SMS_147035035";    //修改密码
    String APPLY_FILL = "SMS_149421561";    //报名审核失败
    String APPLY_SUCCESS = "SMS_149416306";    //报名审核成功

    /**
     * 短信登陆过滤器中
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";//请求参数： 手机号
    String DEFAULT_PARAMETER_NAME_CKCODE = "ckCode";//请求参数： 验证码
    String DEFAULT_PARAMETER_NAME_SOURCE = "loginSource";//请求参数： 来源

    /**
     * 超级管理员的用户id
     */
    String USER_SUPER_ID = "10000";



}
