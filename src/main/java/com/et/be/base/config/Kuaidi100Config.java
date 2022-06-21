package com.et.be.base.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *快递100 配置
 */
@Configuration
@Data
public class Kuaidi100Config {
    /**
     * 密钥key
     */
    @Value("${kuaidi100.key}")
    private String key;

    /**
     * 授权码
     */
    @Value("${kuaidi100.customer}")
    private String customer;


}
