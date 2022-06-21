package com.et.be.base.security.config;

import com.et.be.base.security.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
@EnableWebSecurity
@Profile("!https")
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserAuthenticationProvider authProvider;
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()  // 授权接口放行
                .antMatchers("/api/v1/online/**").permitAll()  // 普通接口放行
                .antMatchers("/api/v1/nav/customer").permitAll() //
                .antMatchers("/payPalV2/returnUrl").permitAll() // pay pal success url 放行
                .antMatchers("/payPalV2/cancel").permitAll() // pay pal cancel url 放行
//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/authentication.html") // authentication
                .loginProcessingUrl("/user/login")
                .usernameParameter("email")
                .permitAll()

                .and()
                //默认注销登录URL是/logout  可以通过logoutUrl方法来修改默认的注销URL
                .logout()
                .logoutUrl("/api/v1/auth/logout")

                .and()
                .csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //allow Swagger URL to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs",//swagger api json
                "/swagger-resources/configuration/ui",//用来获取支持的动作
                "/swagger-resources",//用来获取api-docs的URI
                "/swagger-resources/configuration/security",//安全选项
                "/swagger-ui.html")
                .antMatchers("/assets/**")
                .antMatchers("/images/**")
                .antMatchers("/js/**")
                .antMatchers("/libs/**")
                .antMatchers("/themes/**")
                .antMatchers("/")
                .antMatchers("/index.html")
                .antMatchers("/404.html")
                .antMatchers("/about.html")
                .antMatchers("/contact.html")
                .antMatchers("/discover.html")
                .antMatchers("/search-result.html")
                .antMatchers("/shop.html")
                .antMatchers("/single-product.html")
                .antMatchers("/single-post.html")
                .antMatchers("/authentication.html")
                .antMatchers("/pay-result.html")
                .antMatchers("/password-reset.html")
                .antMatchers("/password-change.html")
                .antMatchers("/terms-of-use.html")
                .antMatchers("/product/**")
                .antMatchers("/media/**")
                .antMatchers("/discover/**")
        ;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
