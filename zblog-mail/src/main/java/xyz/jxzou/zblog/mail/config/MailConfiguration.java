package xyz.jxzou.zblog.mail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MailConfiguration
 *
 * @author Jx
 **/
@Configuration
@ConfigurationProperties("mail")
public class MailConfiguration {

    private ThreadPoolConfig threadPool;

    public ThreadPoolConfig getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolConfig threadPool) {
        this.threadPool = threadPool;
    }
}
