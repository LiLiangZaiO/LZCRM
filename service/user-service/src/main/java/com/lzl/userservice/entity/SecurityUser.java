package com.lzl.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    //当前登录用户
    private transient User currentUserInfo;

    //当前权限
    private List<Role> Roles;


    //@JsonIgnore 很重要不然解析token 获取token中的用户信息 解析不成功
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Roles;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return currentUserInfo.getUsername();
    }

    /**
     * 是否账号已过期
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否账号已被锁
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 是否凭证已过期
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否账号已禁用
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
