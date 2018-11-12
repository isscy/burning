package cn.ff.burning;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityPwdTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void endoce(){

        String source = "123456";
        String encode = new BCryptPasswordEncoder().encode(source);

        logger.warn(encode);
    }
}
