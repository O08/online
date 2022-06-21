package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.base.exception.ErrorCodeException;
import com.et.be.base.security.UserInfo;
import com.et.be.online.constant.OptConstant;
import com.et.be.online.constant.RichTextConfigConstant;
import com.et.be.online.constant.SysConfigConstant;
import com.et.be.online.domain.dto.CustomerDTO;
import com.et.be.online.domain.dto.PasswordResetDTO;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.mo.RichTextConfig;
import com.et.be.online.domain.mo.ShoppingSession;
import com.et.be.online.domain.mo.SignUpInfo;
import com.et.be.online.enums.OnlineCodeEnum;
import com.et.be.online.mapper.CustomerMapper;
import com.et.be.online.mapper.ShoppingSessionMapper;
import com.et.be.online.service.CustomerService;
import com.et.be.online.service.RichTextConfigService;
import com.et.be.util.EmailUtil;
import com.et.be.util.SecureCheckUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ShoppingSessionMapper shoppingSessionMapper;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    RichTextConfigService textConfigService;


    @Override
    public boolean doSignUp(SignUpInfo signUpInfo) {
        String password = SecureCheckUtil.decryptIdentity(signUpInfo.getPassword());
        boolean expired = System.currentTimeMillis() - Long.parseLong(signUpInfo.getTime()) > OptConstant.TIME_OUT;
        if(expired){
            return false;
        }
        // if customer already sign up return
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email",signUpInfo.getEmail());
        Integer cnt = customerMapper.selectCount(wrapper);
        if(cnt > 0){
            return false;
        }
        // register account
       String encryptedPassword= new BCryptPasswordEncoder().encode(password);
        Customer customer = new Customer();
        customer.setEmail(signUpInfo.getEmail())
                .setUsername(signUpInfo.getFullname())
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

    @Override
    public int doCloseAccount() {
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email",UserInfo.getUsername());
        int res = customerMapper.delete(wrapper);
        return res;
    }

    @Override
    public boolean doSendPasswordResetEmail(String email) {
        Customer customer = getCustomerByEmail(email);
        if(BeanUtil.isEmpty(customer)){
            throw new ErrorCodeException(OnlineCodeEnum.NOT_SIGN_UP);
        }
        // 查询模版
        RichTextConfig emailHtmlConfig = textConfigService.getRichTextConfig(RichTextConfigConstant.TYPE_EMAILHTML, RichTextConfigConstant.TYPE_EMAILHTML_PASSWORD_RESET_T);
        if(StrUtil.isEmptyIfStr(emailHtmlConfig)){
            return false;
        }
        // 签发token
        Map<String, Object> identityMap = new HashMap<>();
        identityMap.put("email",email);
        identityMap.put("time",System.currentTimeMillis());
        String token = SecureCheckUtil.encryptIdentity(new Gson().toJson(identityMap));
        // 生成邮件内容
        String content = emailHtmlConfig.getContent()
                .replace("#{webaddress}", SysConfigConstant.WEB_SITE)
                .replace("#{username}",customer.getUsername())
                .replace("#{resetPasswordLink}",SysConfigConstant.WEB_SITE+"password-change.html?token="+token);
        // 发送邮件 1 html 2 发送对象 3 主题
        emailUtil.sendHtmlEmail(content,email,"Reset your password");
        return true;
    }

    @Override
    public boolean doPasswordReset(PasswordResetDTO passwordResetDTO) {
        String decryptToken = SecureCheckUtil.decryptIdentity(passwordResetDTO.getToken());
        String decryptPassword = SecureCheckUtil.decryptIdentity(passwordResetDTO.getPassword());
        JsonObject identity = JsonParser.parseString(decryptToken).getAsJsonObject();
        long time =  identity.get("time").getAsLong();
        String email = identity.get("email").getAsString();
        // if token expired ,return. interval 1h
        boolean expired = System.currentTimeMillis() - time > OptConstant.TOKEN_OUT;
        if(expired){
            throw new ErrorCodeException(OnlineCodeEnum.INVALID_TOKEN);
        }
        // reset password
        String encryptedPassword= new BCryptPasswordEncoder().encode(decryptPassword);
        Customer customer = new Customer();
        customer.setPassword(encryptedPassword)
                .setModifiedAt(new Date());
        QueryWrapper<Customer> wrapper = Wrappers.query();
        wrapper.eq("email", email);
        customerMapper.update(customer,wrapper);

        // send password updated email
        sendPasswordUpdatedEmail(email);
        return true;
    }

    private void sendPasswordUpdatedEmail(String email){
        // 查询模版
        RichTextConfig emailHtmlConfig = textConfigService.getRichTextConfig(RichTextConfigConstant.TYPE_EMAILHTML, RichTextConfigConstant.TYPE_EMAILHTML_PASSWORD_UPDATED_T);
        if(StrUtil.isEmptyIfStr(emailHtmlConfig)){
            return ;
        }

        // 生成邮件内容
        String content = emailHtmlConfig.getContent()
               .replace("#{webaddress}", SysConfigConstant.WEB_SITE)
               .replace("#{supportLink}",SysConfigConstant.WEB_SITE+"contact.html");
        // 发送邮件 1 html 2 发送对象 3 主题
        emailUtil.sendHtmlEmail(content,email,"Password updated");
    }
}
