package xyz.jxzou.zblog.common.core.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtils {

    private static final int DEFAULT_SIZE = 128;
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String encrypt(String plaintext, String salt) throws Exception {
        return encrypt(plaintext, salt, DEFAULT_SIZE);
    }

    public static String encrypt(String plaintext, String salt, int size) throws Exception {
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(size);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] bytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(bytes);
    }

    public static String decrypt(String ciphertext, String decryptKey) throws Exception {
        return decrypt(ciphertext, decryptKey, DEFAULT_SIZE);
    }

    public static String decrypt(String ciphertext, String decryptKey, int size) throws Exception {
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(size);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] encryptBytes = Base64Utils.decodeFromString(ciphertext);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
}
