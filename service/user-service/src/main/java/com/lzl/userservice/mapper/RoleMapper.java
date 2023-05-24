package com.lzl.userservice.mapper;

import com.lzl.userservice.entity.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    Role selectRoleByUserId(String userId);

    List<Role> selectRolesByUserId(String userId);

    Integer countUserByRoleId(String roleId);

}
