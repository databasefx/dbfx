package cn.navigational.dbfx.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Ase algorithm encode or decode
 *
 * @author yangkui
 * @since 1.0
 */
public class AseAlgorithm {
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) throws Exception {
        var cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        var byteContent = content.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
        var result = cipher.doFinal(byteContent);
        return Base64.encodeBase64String(result);
    }

    /**
     * AES 解密操作
     *
     * @param content 待解密内容
     * @param key     密钥
     * @throws Exception {@inheritDoc}
     */
    public static String decrypt(String content, String key) throws Exception {
        var cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
        var result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result, StandardCharsets.UTF_8);

    }

    /**
     * 生成加密秘钥
     *
     * @return {@link SecretKeySpec}
     * @throws NoSuchAlgorithmException {@inheritDoc}
     */
    private static SecretKeySpec getSecretKey(final String key) throws NoSuchAlgorithmException {
        var kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        var secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kg.init(128, secureRandom);
        var secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

}
