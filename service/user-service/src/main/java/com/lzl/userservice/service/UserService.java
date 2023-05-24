package com.lzl.userservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.userservice.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.userservice.entity.UserInfoVo;
import com.lzl.userservice.entity.UserPassword;
import com.lzl.userservice.entity.UserVo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-14
 */
public interface UserService extends IService<User> , UserDetailsService {

    IPage<User> pageQuery(Page<User> page, UserVo userVo);

    Boolean addUserInfo(UserVo userVo);

    Boolean updateUserInfo(UserVo userVo);

    String getUserIdByUsername(String username);

    String getUsernameByUserId(String userId);

    List<User> getUsernameList();

    List<User> getAllRealNameList();

    UserInfoVo getMyUserInfo();

    void updateMyUserInfo(UserVo user);

    void updateMyPassword(UserPassword userPassword);

    void deleteUserById(String userId);
}
