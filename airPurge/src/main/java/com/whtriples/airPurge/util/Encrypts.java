package com.whtriples.airPurge.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class Encrypts {

    private static final Charset CHARSET_ENCODING = Charsets.UTF_8;

    private static final byte[] DEFAULT_KEY = "phonefu                 ".getBytes();

    public enum EncryptType {
        // DES加密引擎(8位密钥)
        DES,

        // 3DES加密引擎(24位密钥)
        DESede,

        // AES加密引擎(16位密钥)
        AES
    }

    public static byte[] encrypt(byte[] src, byte[] key, EncryptType encryptType) {
        String type = encryptType.name();
        try {
            Cipher cipher = Cipher.getInstance(type);
            SecretKey securekey = new SecretKeySpec(key, type);
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptDesCbcNoPadding(byte[] src, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKey securekey = new SecretKeySpec(key, "DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptDesCbcNoPadding(byte[] src, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKey securekey = new SecretKeySpec(key, "DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] src, byte[] key, EncryptType encryptType) {
        String type = encryptType.name();
        try {
            Cipher cipher = Cipher.getInstance(type);
            SecretKey securekey = new SecretKeySpec(key, type);
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            return cipher.doFinal(src);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encrypt(String src, String key, EncryptType encryptType) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(key) || encryptType == null) {
            return null;
        }
        return Encodes.encodeHex(encrypt(src.getBytes(CHARSET_ENCODING), key.getBytes(CHARSET_ENCODING), encryptType));
    }

    public static String decrypt(String src, String key, EncryptType encryptType) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(key) || encryptType == null) {
            return null;
        }
        return new String(decrypt(Encodes.decodeHex(src), key.getBytes(CHARSET_ENCODING), encryptType));
    }

    public static String encrypt(String src, EncryptType encryptType) {
        byte[] array = encrypt(src.getBytes(CHARSET_ENCODING), DEFAULT_KEY, encryptType);
        return array == null ? null : Encodes.encodeHex(array);
    }

    public static String decrypt(String src, EncryptType encryptType) {
        byte[] array = decrypt(Encodes.decodeHex(src), DEFAULT_KEY, encryptType);
        return array == null ? null : new String(array);
    }

    public static void main(String[] args) {
        String encrypted = encrypt("123456", "phonefu                 ", EncryptType.DESede);
        System.out.println(encrypted);
        String src = decrypt(encrypted, "phonefu                 ", EncryptType.DESede);
        System.out.println(src);
    }

}
