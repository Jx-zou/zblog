package xyz.jxzou.zblog;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableEncryptableProperties
public class SystemApplication {

    private static final Logger log = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

    @PreDestroy
    public void shutdownSystem() {
        log.info("关闭系统...");
    }
}
