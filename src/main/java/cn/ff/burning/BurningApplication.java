package cn.ff.burning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.ff.burning.mapper")
public class BurningApplication {

    public static void main(String[] args) {
        SpringApplication.run(BurningApplication.class, args);
    }
}
