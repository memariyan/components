package com.memariyan.components.security.util;

import lombok.extern.slf4j.Slf4j;
import com.memariyan.components.common.function.CheckedFunction;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Slf4j
public class CredentialPropertyEncryptorUtil {

    private static final String SIGNATURE = "cipher:";

    private static final String IV = "iv@12345678";
    private static final String CRYPTOGRAPHY_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CRYPTOGRAPHY_ALGORITHM = "AES";

    public static String encrypt(String plainText, String encryptionKey) throws Exception {
        try {
            return SIGNATURE + encrypt(plainText, encryptionKey, IV);
        } catch (Exception e) {
            log.error("unable to encrypt plainText", e);
            throw e;
        }
    }

    public static String decrypt(String cipherText, String encryptionKey) throws Exception {
        try {
            if (!cipherText.contains(SIGNATURE))
                return cipherText;
            cipherText = cipherText.replace(SIGNATURE, "");
            return decrypt(cipherText, encryptionKey, IV);
        } catch (Exception e) {
            log.error("unable to decrypt cipherText", e);
            throw e;
        }
    }

    public static boolean isEncrypted(String text) {
        return Objects.nonNull(text) && text.contains(SIGNATURE);
    }

    private static String encrypt(String plainText, String key, String iv) throws Exception {
        return cryptography(key, iv, Cipher.ENCRYPT_MODE, (cipher ->
                Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8))))
        );
    }

    private static String decrypt(String cipherText, String key, String iv) throws Exception {
        return cryptography(key, iv, Cipher.DECRYPT_MODE, (cipher ->
                new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8))
        );
    }

    private static String cryptography(
            String key,
            String iv,
            int cryptographyMode,
            CheckedFunction<Cipher, String> function
    ) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(CRYPTOGRAPHY_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), CRYPTOGRAPHY_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(cryptographyMode, secretKeySpec, ivParameterSpec);
            return function.apply(cipher);
        } catch (Exception e) {
            if (cryptographyMode == Cipher.DECRYPT_MODE) {
                log.error("unable to decrypt credential property error: ", e);
            } else {
                log.error("unable to encrypt credential property error: ", e);
            }
            throw e;
        }
    }

}
