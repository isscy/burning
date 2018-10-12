package cn.ff.burning.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

/**
 * 统一返回结果类
 * @author ff 20181012
 */
@Data
public class R {

    /**
     * 状态码：1成功，其他为失败
     */
    private int code;

    /**
     * 成功为success，其他为失败原因
     */
    private String message;

    /**
     * 数据结果集
     */
    private Object data;

    public R(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public R(Object data) {
        this.data = data;
    }

    public R success(){
        this.code = 1;
        this.message = "success";
        return this;
    }
    public R fail(){
        this.code = 0;
        return this;
    }
    public R status(int code){
        if (code == 1)
            this.success();
        else{
            this.fail();
            this.code = code;
        }
        return this;
    }

    public String asJson(String format){
        Gson gson = new GsonBuilder().setDateFormat(format).create();
        return gson.toJson(this);
    }

}
