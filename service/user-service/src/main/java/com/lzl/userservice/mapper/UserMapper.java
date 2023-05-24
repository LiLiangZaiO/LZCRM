package com.lzl.userservice.mapper;

import com.lzl.userservice.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-14
 */
public interface UserMapper extends BaseMapper<User> {

    List<String> selectUserIdByRoleIds(List<String> roleIds);

    List<String> selectUserIdByRoleId(String roleId);

    Boolean insertRoleUser(String userId, String roleId);

    Boolean updateUserRole(String userId, String roleId);

    Boolean selectUserRole(String id);

    String getUserIdByUsername(String username);

    String getUsernameByUserId(String userId);

    List<User> getUsernameList(String userId);

    void updateMyPassword(String username, String confirmPassword);

    Integer deleteUserById(String userId);
}
