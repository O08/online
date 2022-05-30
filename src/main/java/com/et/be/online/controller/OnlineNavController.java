package com.et.be.online.controller;

import cn.hutool.core.bean.BeanUtil;
import com.et.be.config.security.UserInfo;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.vo.CustomerVO;
import com.et.be.online.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "电商-导航栏", tags = "电商")
@RequestMapping("api/v1/nav")
@RestController
@Slf4j
public class OnlineNavController {

    @Autowired
    private CustomerService customerService;
    @ApiOperation("购物车")
    @ResponseBody
    @PostMapping(value = "miniCart")
    public ResponseVO cart() {
        return new ResponseVO("");
    }

    @ApiOperation("用户信息")
    @ResponseBody
    @PostMapping(value = "customer")
    public ResponseVO customer() {
        String email = UserInfo.getUsername() ;
        Customer customer = customerService.getCustomerByEmail(email);
        CustomerVO customerVO = new CustomerVO();
        if(!BeanUtil.isEmpty(customer)){
            BeanUtil.copyProperties(customer,customerVO);
        }
        return new ResponseVO(customerVO);
    }
}
