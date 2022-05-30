package com.et.be.inbox.utils;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Component
@Slf4j
public class SecureCheckUtil {


    private static String publicKey;

    private static String privateKey;

    @Value("${rsa.publicKey}")
    public  void setPublicKey(String publicKey) {
        SecureCheckUtil.publicKey = publicKey;
    }

    @Value("${rsa.privateKey}")
    public  void setPrivateKey(String privateKey) {
        SecureCheckUtil.privateKey = privateKey;
    }

    public static String getPublicKey(){
        return publicKey;
    }

    public static String getPrivateKey(){
        return privateKey;
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

//    public static void main(String[] args) {
//        KeyPair keyPair = SecureUtil.generateKeyPair("RSA");
//       PublicKey aPublic = keyPair.getPublic();
//        PrivateKey aPrivate = keyPair.getPrivate();
//        String pbk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMwxlFaUwQmHJceHsgowj9d47OX+XFRegNm4UJwHjtLA32M3gMhDCg3szSTPdLNtUCI+RBKIyL6ZpwSs1ogRsG7R2kaaQzsw48WG30wY1jtTcNJuISJoztoSSHKqyzFwwwx52VbPGozOKyMPcS/qoIQYsaK3AZ4B9UdxSMu4PuGwIDAQAB";
//        String prkey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIzDGUVpTBCYclx4eyCjCP13js5f5cVF6A2bhQnAeO0sDfYzeAyEMKDezNJM90s21QIj5EEojIvpmnBKzWiBGwbtHaRppDOzDjxYbfTBjWO1Nw0m4hImjO2hJIcqrLMXDDDHnZVs8ajM4rIw9xL+qghBixorcBngH1R3FIy7g+4bAgMBAAECgYAlpe7l5j5ed++qdNz1wzVd2bLb1sFVTJe+v/hKvex5/tzzGxwCOP7qn/ynSMehmdZJ/sCHfulsaHwSiac7ysAn6fgnxPgF1sZmprxj+pjpZ5Wk9agKEHNUbFsbKm8I0X/XBz+EuWxytE8Gd9ucdZEuZO2I7673TJ9EBjhdFhgEEQJBAMU3ct0wDn0Mtpk3y2usv501FcucCeQhYoiOxr4dLmbZFna4eAt4XNY4p6xKjT3RrKBRIo0MN7pj7YrQxdMhdysCQQC2t+hRhOR+KEq7+drsK26ccXODxcyMRU9Sv4ODo/u1oVqa3/WtsZTrAZmcQI8ruI57aIs9nsrDiwlBXHY/AuzRAkBhAkZpENN/pXpIYc6ZF29luBARiJ7A7b8XJwwRxi7l/lDzcwfxtHIOGe44bK+PQg0lCuPw95qN/8t/5fP5R9PjAkBbMXyUuxPjVy69lN1Lw/2HvSPfH6iQVcNaVFVKLVzicO6sn5yZnSjrBbhqANq0An+yXiIolgkzAUCUS5aEqmwRAkAHhspsgQD7ef+1mXcPzwjo+RWx3Sddm0IJ4+uaV3hVKCyGvznX6y8R+VFBtIHby8U0dw5IgkiMF9wNGY3KuXGp";
////        log.info("公钥："+ Base64Encoder.encode(aPublic.getEncoded()));
////        log.info("私钥："+Base64Encoder.encode(aPrivate.getEncoded()));
//        SecureCheckUtil.setPublicKey(pbk);
//        SecureCheckUtil.setPrivateKey(prkey);
//        String liuhao = "JRR5l87PKpe+TpFTniyRCTzfQ1NC4v2LAX+lL+ckAzd7EQVj58gHyc8B2Q1z0iQ6AkSzwkESm5ipJYV4jmwIrcsZQbxyTUt6ED3kwNTHOqSqjSbQjy66vDn8+2rOuSyxUMQhcJs7SiC14RVbp/acQCIe6Q+yG6zoBVHH9Vcy2n0=";
//                //SecureCheckUtil.encryptIdentity("liuhao");
//        String s = SecureCheckUtil.decryptIdentity(liuhao);
//        System.out.println(s);
//
//
//    }


}


