package xyz.jxzou.zblog.util.pojo;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public final class RSAKeyPair {
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;


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
        return this.publicKey;
    }

    /**
     * Gets private key.
     *
     * @return the private key
     */
    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getBase64PublicKey() {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public String getBase64PrivateKey() {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }
}
