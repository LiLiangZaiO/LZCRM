package com.lzl.userservice.service;

import com.lzl.userservice.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.userservice.entity.User;
import com.lzl.userservice.entity.UserInfoVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-14
 */
public interface RoleService extends IService<Role> {


    Role selectRoleByUserId(String id);

    List<UserInfoVo> selectUserInfoVoByUserId(List<User> userList);

    List<Role> selectRolesByUserId(String id);

    boolean addRole(Role role);

    boolean updateRole(Role role);

    void deleteRoleById(String roleId);
}
