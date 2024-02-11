package xyz.jxzou.zblog.util.tool;

import lombok.extern.slf4j.Slf4j;
import xyz.jxzou.zblog.util.pojo.RSAKeyPair;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * The type Rsa utils.
 */
@Slf4j
public class RSAUtils {

    private static final String ALGORITHM = "RSA";

    private static final int SIZE = 2048;

    private static final KeyPairGenerator keyPairGenerator;
    private static final KeyFactory rsaKeyFactory;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            rsaKeyFactory = KeyFactory.getInstance("RSA");
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
        if (size < 1024) {
            size = SIZE;
        }
        keyPairGenerator.initialize(size);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new RSAKeyPair((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
    }

    /**
     * Byte private key rsa private key.
     *
     * @param privateKey the private key
     * @return the rsa private key
     * @throws InvalidKeySpecException the invalid key spec exception
     */
    public static RSAPrivateKey bytePrivateKey(byte[] privateKey) throws InvalidKeySpecException {
        return (RSAPrivateKey) rsaKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
    }

    /**
     * Byte public key rsa public key.
     *
     * @param publicKey the public key
     * @return the rsa public key
     * @throws InvalidKeySpecException the invalid key spec exception
     */
    public static RSAPublicKey bytePublicKey(byte[] publicKey) throws InvalidKeySpecException {
        return (RSAPublicKey) rsaKeyFactory.generatePublic(new PKCS8EncodedKeySpec(publicKey));
    }

    /**
     * Encrypt byte [ ].
     *
     * @param plaintext    the plaintext
     * @param rsapublicKey the rsapublic key
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public static byte[] encrypt(byte[] plaintext, RSAPublicKey rsapublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsapublicKey);
        return cipher.doFinal(plaintext);
    }

    /**
     * Base 64 encrypt string.
     *
     * @param plaintext    the plaintext
     * @param rsapublicKey the rsapublic key
     * @return the string
     * @throws Exception the exception
     */
    public static String base64Encrypt(String plaintext, RSAPublicKey rsapublicKey) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(plaintext.getBytes(StandardCharsets.UTF_8), rsapublicKey));
    }

    /**
     * Decrypt byte [ ].
     *
     * @param ciphertext    the ciphertext
     * @param rsaPrivateKey the rsa private key
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public static byte[] decrypt(byte[] ciphertext, RSAPrivateKey rsaPrivateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        return cipher.doFinal(ciphertext);
    }

    /**
     * Base 64 decrypt string.
     *
     * @param ciphertext    the ciphertext
     * @param rsaPrivateKey the rsa private key
     * @return the string
     * @throws Exception the exception
     */
    public static String base64Decrypt(String ciphertext, RSAPrivateKey rsaPrivateKey) throws Exception {
        return new String(decrypt(Base64.getDecoder().decode(ciphertext), rsaPrivateKey));
    }
}
