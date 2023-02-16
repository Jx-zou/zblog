package xyz.jxzou.zblog.common.core.util;

import lombok.extern.slf4j.Slf4j;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * The type Rsa utils.
 */
@Slf4j
public class RSAUtils {

    private static final String ALGORITHM = "RSA";

    private static final int SIZE = 2048;

    private static final KeyPairGenerator keyPairGenerator;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate rsa key pair rsa key pair.
     *
     * @return the rsa key pair
     */
    public static RSAKeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(SIZE);
    }

    /**
     * Generate rsa key pair rsa key pair.
     *
     * @param size the size
     * @return the rsa key pair
     */
    public static RSAKeyPair generateRSAKeyPair(int size) {
        if (size < 128) {
            return null;
        }
        keyPairGenerator.initialize(size);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new RSAKeyPair((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
    }
}
