package xyz.jxzou.zblog.common.core.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AESUtils {
    private static final String ALGORITHM = "AES";

    public static String encrypt(String plaintext, String salt) throws Exception {
        return encrypt(plaintext, salt, StandardCharsets.UTF_8);
    }

    public static String encrypt(String plaintext, String salt, Charset charset) throws Exception {
        return new String(encrypt(plaintext.getBytes(charset), salt.getBytes(charset)), charset);
    }

    public static String base64Encrypt(String plaintext, String salt) throws Exception {
        return base64Encrypt(plaintext, salt, StandardCharsets.UTF_8);
    }

    public static String base64Encrypt(String plaintext, String salt, Charset charset) throws Exception {
        return Base64Utils.encodeToString(encrypt(plaintext.getBytes(charset), salt.getBytes(charset)));
    }

    public static byte[] encrypt(byte[] plaintext, byte[] salt) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(salt, ALGORITHM));
        return cipher.doFinal(plaintext);
    }

    public static String decrypt(String ciphertext, String decryptKey) throws Exception {
        return decrypt(ciphertext, decryptKey, StandardCharsets.UTF_8);
    }

    public static String decrypt(String ciphertext, String decryptKey, Charset charset) throws Exception {
        return new String(decrypt(ciphertext.getBytes(charset), decryptKey.getBytes(charset)), charset);
    }

    public static String base64Decrypt(String ciphertext, String decryptKey) throws Exception {
        return base64Decrypt(ciphertext, decryptKey, StandardCharsets.UTF_8);
    }

    public static String base64Decrypt(String ciphertext, String decryptKey, Charset charset) throws Exception {
        return new String(decrypt(Base64Utils.decode(ciphertext.getBytes(charset)), Base64Utils.decode(decryptKey.getBytes(charset))));
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey, ALGORITHM));
        return cipher.doFinal(ciphertext);
    }
}
