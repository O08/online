package com.et.be.config.security;

import com.et.be.inbox.utils.SecureCheckUtil;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserDetailsService authUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if(StringUtils.isBlank(username)){
            throw new UsernameNotFoundException("username Not Found");
        }
        if(StringUtils.isBlank(password)){
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails user = authUserDetailsService.loadUserByUsername(username);
        String decryptedPassword = SecureCheckUtil.decryptIdentity(password);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(decryptedPassword, user.getPassword())){
            throw new BadCredentialsException("wrong password");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, decryptedPassword, authorities);

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
