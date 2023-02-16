package xyz.jxzou.zblog.common.exception.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * GlobaConfiguration
 *
 * @author jx
 */
@Configuration
@AutoConfiguration
public class GlobalConfiguration {

    @Value("${spring.messages.basename}")
    private String messageBasename;

    @Value("${spring.messages.encoding}")
    private Charset messageEncoding;

    @Bean
    public MessageSourceProperties messageSourceProperties(){
        MessageSourceProperties messageSourceProperties = new MessageSourceProperties();
        messageSourceProperties.setBasename(messageBasename);
        messageSourceProperties.setEncoding(messageEncoding);
        return messageSourceProperties;
    }
}
