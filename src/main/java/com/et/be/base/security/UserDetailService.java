package com.et.be.base.security;

import com.et.be.online.domain.mo.Customer;
import com.et.be.online.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer customer = customerService.getCustomerByEmail(s);
        if(customer == null){
            throw new UsernameNotFoundException("not found");
        }
        //定义权限列表.
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User userDetails = new User(customer.getEmail(),customer.getPassword(),authorities);
        return userDetails;
    }
}
