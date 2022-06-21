package com.et.be.base.config;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.util.IOUtils;
import com.et.be.base.annotation.ApiSecutityPolicy;

import com.et.be.base.wrapper.ApiHttpServletRequestWrapper;
import com.et.be.util.SecureCheckUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiSecurityConfig {

    /**
     *  注解中为需要 校验的 api
     * @param request
     * @return
     */
    @ApiSecutityPolicy("/api/v1/inbox/displayContact")
    public boolean policyOne(HttpServletRequest request){
        String email = SecureCheckUtil.decryptIdentity(request.getHeader("identity"));

        return  email.equalsIgnoreCase(request.getParameter("email"));
    }
    /**
     *  注解中为需要 校验的 api
     * @param request
     * @return
     */
    @ApiSecutityPolicy("/api/v1/inbox/listMsg")
    public boolean policyTwo(ApiHttpServletRequestWrapper request) throws IOException {
        String email = SecureCheckUtil.decryptIdentity(request.getHeader("identity"));
        String requestBody= IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        String param = JSON.parseObject(requestBody).getString("sender");
        return  email.equalsIgnoreCase(param);

//          return true;
    }



}
