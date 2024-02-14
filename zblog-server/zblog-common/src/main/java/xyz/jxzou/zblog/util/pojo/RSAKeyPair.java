package xyz.jxzou.zblog.util.pojo;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * @param publicKey  -- GETTER --
 *                   Gets public key.
 * @param privateKey -- GETTER --
 *                   Gets private key.
 */
public record RSAKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    /**
     * Instantiates a new Rsa key pair.
     *
     * @param publicKey  the public key
     * @param privateKey the private key
     */
    public RSAKeyPair {}

    public RSAPublicKey publicKey() {
        return publicKey;
    }

    public RSAPrivateKey privateKey() {
        return privateKey;
    }

    public String getBase64PublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getBase64PrivateKey() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
