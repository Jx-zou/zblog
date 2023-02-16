package xyz.jxzou.zblog.common.core.pojo;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public final class RSAKeyPair {
    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    /**
     * Instantiates a new Rsa key pair.
     *
     * @param publicKey  the public key
     * @param privateKey the private key
     */
    public RSAKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Gets private key.
     *
     * @return the private key
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getBase64PublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getBase64PrivateKey() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
