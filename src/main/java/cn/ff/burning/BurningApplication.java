package cn.ff.burning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("cn.ff.burning")
public class BurningApplication {

    public static void main(String[] args) {
        SpringApplication.run(BurningApplication.class, args);
    }

    /* TODO List
        1. 每个角色对应的数据权限 ： 全部 部门 个人
        2. 按钮的权限

     */
}
