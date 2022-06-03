package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.config.security.UserInfo;
import com.et.be.online.constant.OptConstant;
import com.et.be.online.domain.dto.CustomerDTO;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.mo.LoginInfo;
import com.et.be.online.domain.mo.ShoppingSession;
import com.et.be.online.domain.mo.SignUpInfo;
import com.et.be.online.mapper.CustomerMapper;
import com.et.be.online.mapper.ShoppingSessionMapper;
import com.et.be.online.service.CustomerService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ShoppingSessionMapper shoppingSessionMapper;


    @Override
    public boolean doSignUp(String signUpInfo) {
        Gson gson = new Gson();
        SignUpInfo signUpInfoObj = gson.fromJson(signUpInfo, SignUpInfo.class);
        boolean expired = System.currentTimeMillis() - Long.parseLong(signUpInfoObj.getTime()) > OptConstant.TIME_OUT;
        if(expired){
            return false;
        }
        // if customer already sign up return
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email",signUpInfoObj.getEmail());
        Integer cnt = customerMapper.selectCount(wrapper);
        if(cnt > 0){
            return false;
        }
        // register account
       String encryptedPassword= new BCryptPasswordEncoder().encode(signUpInfoObj.getPassword());
        Customer customer = new Customer();
        customer.setEmail(signUpInfoObj.getEmail())
                .setUsername(signUpInfoObj.getFullname())
                .setPassword(encryptedPassword)
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        customerMapper.insert(customer);
        // register shopping session
        ShoppingSession shoppingSession = new ShoppingSession();
        shoppingSession.setUserId(customer.getId())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        shoppingSessionMapper.insert(shoppingSession);
        return true;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email",email);
        Customer customer = customerMapper.selectOne(wrapper);
        return customer;
    }

    @Override
    public int doEditName(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        String email = UserInfo.getUsername();
        customer.setUsername(customerDTO.getName())
                .setModifiedAt(new Date());
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email", email);
       return customerMapper.update(customer,wrapper);
    }

    @Override
    public int doEditPassword(String customerDTO) {
        Gson gson = new Gson();
        CustomerDTO customerDTOObj = gson.fromJson(customerDTO, CustomerDTO.class);
        boolean expired = System.currentTimeMillis() - Long.parseLong(customerDTOObj.getTime()) > OptConstant.TIME_OUT;
        if(expired){
            return 0;
        }
        String encryptedPassword= new BCryptPasswordEncoder().encode(customerDTOObj.getPassword());
        Customer customer = new Customer();
        String email = UserInfo.getUsername();
        customer.setPassword(encryptedPassword)
                .setModifiedAt(new Date());
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email", email);
        return customerMapper.update(customer,wrapper);
    }

    @Override
    public int doEditPhone(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        String email = UserInfo.getUsername();
        customer.setMobile(customerDTO.getPhone())
                .setModifiedAt(new Date());
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email", email);
        return customerMapper.update(customer,wrapper);
    }
}
