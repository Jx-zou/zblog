package xyz.jxzou.zblog.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jwt prop.
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "safety.jwt")
public class JwtProp {

    private Salt salt;

    private Token accessToken;

    /**
     * Gets salt size.
     *
     * @return the salt size
     */
    public Integer getSaltSize() {
        return getSalt().getSize();
    }

    /**
     * Gets token expire.
     *
     * @return the token expire
     */
    public Integer getAccessExpire() {
        return this.getAccessToken().getExpire();
    }

    /**
     * Gets token prefix.
     *
     * @return the token prefix
     */
    public String getAccessPrefix() {
        return this.getAccessToken().getPrefix();
    }

    /**
     * Gets token iss.
     *
     * @return the token iss
     */
    public String getAccessIss() {
        return this.getAccessToken().getIss();
    }

    /**
     * Gets token sub.
     *
     * @return the token sub
     */
    public String getAccessSub() {
        return this.getAccessToken().getSub();
    }

    /**
     * The type Salt.
     */
    @Setter
    @Getter
    public static class Salt {
        private Integer size;
    }

    /**
     * The type Token.
     */
    @Setter
    @Getter
    public static class Token {
        private Integer expire;
        private String prefix;
        private String iss;
        private String sub;
    }
}
