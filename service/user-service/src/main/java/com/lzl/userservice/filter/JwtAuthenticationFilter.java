package com.lzl.userservice.filter;

import com.lzl.userservice.entity.SecurityUser;
import com.lzl.userservice.entity.User;
import com.lzl.common_utils.domain.R;
import com.lzl.common_utils.utils.JwtUtils;
import com.lzl.common_utils.utils.RequestUtils;
import com.lzl.common_utils.utils.ResponseUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.setFilterProcessesUrl("/user/login");
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        User user = RequestUtils.read(request, User.class);
        String username = user.getUsername();
        username = username!=null ? username:"";
        String password = user.getPassword();
        password = password!=null ? password:"";
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authRequest);

    }

    /**
     * 认证成功所执行的方法
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //直接new一个新的对象，除去敏感信息
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();

        String token = JwtUtils.generateTokenExpireInMinutes(securityUser,24 * 60);
//        response.addHeader("Authorization",token);
//        ResponseUtils.write(response, HttpServletResponse.SC_OK, "用户认证通过！");
        ResponseUtils.out(response, R.ok().data("token", token));
    }

    /**
     * 认证失败所执行的方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //清理上下文
        SecurityContextHolder.clearContext();
        System.out.println(failed);
        //判断异常类
        if (failed instanceof InternalAuthenticationServiceException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "认证服务不正常！");
            ResponseUtils.out(response, R.error().code(201).message("认证服务不正常！"));
        } else if (failed instanceof UsernameNotFoundException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户账户不存在！");
            ResponseUtils.out(response, R.error().code(201).message("用户账户不存在！"));
        } else if (failed instanceof BadCredentialsException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户密码是错的！");
            ResponseUtils.out(response, R.error().code(201).message("用户密码是错的！"));
        } else if (failed instanceof AccountExpiredException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户账户已过期！");
            ResponseUtils.out(response, R.error().code(201).message("用户账户已过期！"));
        } else if (failed instanceof LockedException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户账户已被锁！");
            ResponseUtils.out(response, R.error().code(201).message("用户账户已被锁！"));
        } else if (failed instanceof CredentialsExpiredException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户密码已失效！");
            ResponseUtils.out(response, R.error().code(201).message("用户密码已失效！"));
        } else if (failed instanceof DisabledException) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户账户已被锁！");
            ResponseUtils.out(response, R.error().code(201).message("用户账户已被锁！"));
        }
    }
}
