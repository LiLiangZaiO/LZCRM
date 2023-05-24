package com.lzl.csservice.config;

import com.lzl.csservice.filter.JwtVerificationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public void configure(HttpSecurity http) throws Exception {
        //禁用csrf保护机制
        http.csrf().disable();
        //禁用cors保护机制
        http.cors().disable();
        //禁用session会话
        http.sessionManagement().disable();
        //禁用form表单登录
        http.formLogin().disable();
        //配置拦截
        http.authorizeRequests().antMatchers("/front/**").permitAll();

        //增加自定义验证过滤器（资源服务需要配置）
        http.addFilter(new JwtVerificationFilter(super.authenticationManager()));
    }

    /**
     * 配置哪些请求不拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
        );
    }

}
