package com.et.be.inbox.utils;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SecureCheckUtil {


    private static String publicKey;

    private static String privateKey;

    @Value("${rsa.publicKey}")
    public void setPublicKey(String publicKey) {
        SecureCheckUtil.publicKey = publicKey;
    }

    @Value("${rsa.privateKey}")
    public void setPrivateKey(String privateKey) {
        SecureCheckUtil.privateKey = privateKey;
    }


    /**
     * 平台签名生成
     *
     * @param identity 签名字符串
     * @return 平台签名
     */
    public static String encryptIdentity(String identity) {
        RSA rsa = new RSA(privateKey, publicKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(identity, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 解签
     *
     * @param identity 加密签名
     * @return 明文
     */
    public static String decryptIdentity(String identity) {
        RSA rsa = new RSA(privateKey, publicKey);
        return StrUtil.str(rsa.decrypt(Base64.getDecoder().decode(identity), KeyType.PrivateKey), CharsetUtil.UTF_8);
    }

}


