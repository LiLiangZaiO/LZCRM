package com.lzl.userservice.filter;

import com.lzl.userservice.entity.SecurityUser;
import com.lzl.common_utils.domain.Payload;
import com.lzl.common_utils.domain.R;
import com.lzl.common_utils.utils.JwtUtils;
import com.lzl.common_utils.utils.ResponseUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证过滤器
 */
public class JwtVerificationFilter extends BasicAuthenticationFilter {


    public JwtVerificationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String token = request.getHeader("Authorization");
            if (token == null) {
                //如果token的格式错误，则提示用户非法登录
//                chain.doFilter(request, response);
//                ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户非法登录！");
                ResponseUtils.out(response, R.error().code(HttpServletResponse.SC_FORBIDDEN).message("用户非法登录！"));
            } else {
                //使用公钥进行解密然后来验证token是否正确
                Payload<SecurityUser> payload = JwtUtils.getInfoFromToken(token, SecurityUser.class);
                SecurityUser securityUser = payload.getUserInfo();
                if (securityUser != null) {
                    UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(securityUser.getUsername(), null, securityUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    chain.doFilter(request, response);
                } else {
//                    ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "用户验证失败！");
                    ResponseUtils.out(response, R.error().code(HttpServletResponse.SC_FORBIDDEN).message("用户验证失败！"));
                }
            }
        } catch (ExpiredJwtException e) {
//            ResponseUtils.write(response, HttpServletResponse.SC_FORBIDDEN, "请您重新登录！");
            ResponseUtils.out(response, R.error().code(HttpServletResponse.SC_FORBIDDEN).message("请您重新登录！"));
        }
    }
}
