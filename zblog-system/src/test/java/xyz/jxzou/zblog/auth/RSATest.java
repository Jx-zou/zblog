package xyz.jxzou.zblog.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import xyz.jxzou.zblog.common.core.domain.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.util.RSAUtils;

@Slf4j
public class RSATest {

    @Test
    public void rsaTest() throws Exception {
        RSAKeyPair rsaKeyPair = RSAUtils.generateRSAKeyPair(2048);
        String encrypt = RSAUtils.base64Encrypt("123", rsaKeyPair.getPublicKey());
        log.info("encrypt: " + encrypt);
        String decrypt = RSAUtils.base64Decrypt(encrypt, rsaKeyPair.getPrivateKey());
        log.info("decrypt: " + decrypt);
    }
}
