package xyz.jxzou.zblog.core.pojo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.core.config.JwtProp;
import xyz.jxzou.zblog.util.tool.RSAUtils;
import xyz.jxzou.zblog.util.pojo.RSAKeyPair;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Getter
@Component
public class SafetyManager {

    private final RSAKeyPair rsaKeyPair;

    private final JwtProp jwtProp;

    public final RSAPublicKey getRsaPublicKey() {
        return this.rsaKeyPair.publicKey();
    }
    public final String getBase64RsaPublicKey() {
        return this.rsaKeyPair.getBase64PublicKey();
    }

    public final RSAPrivateKey getRsaPrivetKey() {
        return this.rsaKeyPair.privateKey();
    }
    public final String  getBase64RsaPrivetKey() {
        return this.rsaKeyPair.getBase64PrivateKey();
    }

    @Autowired
    public SafetyManager(JwtProp jwtProp) {
        this.jwtProp = jwtProp;
        rsaKeyPair = RSAUtils.generateRSAKeyPair();
    }
}
