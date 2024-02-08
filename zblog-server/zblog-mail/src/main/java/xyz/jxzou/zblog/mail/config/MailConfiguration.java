package xyz.jxzou.zblog.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * MailConfiguration
 *
 * @author Jx
 **/
@Data
@Configuration
@ConfigurationProperties("mail")
public class MailConfiguration {

    private ThreadPool threadPool;
}
