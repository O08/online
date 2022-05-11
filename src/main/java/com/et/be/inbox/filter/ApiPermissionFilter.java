package com.et.be.inbox.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.et.be.inbox.config.ApiSecurityConfig;
import com.et.be.inbox.enums.CodeEnum;
import com.et.be.inbox.helper.ApiSecurityPolicyHelper;
import com.et.be.inbox.wrapper.ApiHttpServletRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * api 安全校验
 */
@Component
public class ApiPermissionFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        ApiHttpServletRequestWrapper wrappedRequest = null;

        // 安全策略校验
        ApiSecurityPolicyHelper helper=new ApiSecurityPolicyHelper(currentRequest,ApiSecurityConfig.class);

        if(helper.needExecuteSecurityPolicy()){

            if(!helper.executePolicy()){
                writeFailure(servletResponse);
                return;
            }
            wrappedRequest = helper.getRequest();
        }


        chain.doFilter(ObjectUtil.isNull(wrappedRequest)? currentRequest : wrappedRequest, servletResponse);


    }

    /**
     * 校验失败返回
     * @param response
     * @throws IOException
     */
    private void writeFailure(ServletResponse response) throws IOException {
        JSONObject result = new JSONObject();
        result.put("code",CodeEnum.INVALID_TOKEN.getCode());
        result.put("errMsg",CodeEnum.INVALID_TOKEN.getDesc());
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }
}
