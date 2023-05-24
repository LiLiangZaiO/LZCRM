package com.lzl.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.servicebase.exceptionHandler.LZLException;
import com.lzl.userservice.entity.Role;
import com.lzl.userservice.entity.User;
import com.lzl.userservice.entity.UserInfoVo;
import com.lzl.userservice.mapper.RoleMapper;
import com.lzl.userservice.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {



    @Override
    public Role selectRoleByUserId(String userId) {
        Role role = baseMapper.selectRoleByUserId(userId);
        return role;
    }

    @Override
    public List<Role> selectRolesByUserId(String userId) {
        List<Role> roles = baseMapper.selectRolesByUserId(userId);
        return roles;
    }



    /**
     * 根据用户的id 得到 角色id  并封装到新的 userInfoVo类中返回
     *
     * @param userList
     * @return
     */
    @Override
    public List<UserInfoVo> selectUserInfoVoByUserId(List<User> userList) {

        List<UserInfoVo> userInfoVoList = new ArrayList<>();
        for (User user : userList) {
            UserInfoVo userInfoVo = new UserInfoVo();
            user.setPassword(null);
            BeanUtils.copyProperties(user, userInfoVo);

            Role role = selectRoleByUserId(user.getId());
            if (!StringUtils.isEmpty(role)){
                userInfoVo.setRoleName(role.getName());
            }else {
                userInfoVo.setRoleName("无");
            }
           /*
            StringBuilder sb = new StringBuilder("");
            if (roles.size() > 0) {
                int i = 0;
                for (Role role : roles) {
                    i++;
                    sb.append(role.getName());
                    if (i == roles.size()) {
                        break;
                    }
                    sb.append("、");
                }
            } else {
                sb.append("无");
            }
            */

            userInfoVoList.add(userInfoVo);
        }

        return userInfoVoList;
    }


    @Override
    public boolean addRole(Role role) {
        if (StringUtils.isEmpty(role.getName())||StringUtils.isEmpty(role.getDescription())){
            throw new LZLException(ResultCode.ERROR, "职位名称或职位介绍不能为空！");
        }

        Integer count = baseMapper.selectCount(new QueryWrapper<Role>().eq("name", role.getName()));
        if(count>0){
            throw new LZLException(ResultCode.ERROR, "该职位已存在");
        }
        int insert = baseMapper.insert(role);
        return insert>0;
    }

    @Override
    public boolean updateRole(Role role) {
        if (StringUtils.isEmpty(role.getName())||StringUtils.isEmpty(role.getDescription())){
            throw new LZLException(ResultCode.ERROR, "职位名称或职位介绍不能为空！");
        }

        Integer count = baseMapper.selectCount(new QueryWrapper<Role>().eq("name", role.getName()));
        if(count>0){
            throw new LZLException(ResultCode.ERROR, "该职位已存在,请重新修改!");
        }
        int i = baseMapper.updateById(role);

        return i>0;
    }

    /**
     * 删除角色---不能简单删除
     *      需要 查询角色是否被 用户所用
     * @param roleId
     * @return
     */
    @Override
    public void deleteRoleById(String roleId) {
        Integer count = baseMapper.countUserByRoleId(roleId);
        if (count>0){
            throw new LZLException(ResultCode.ERROR,"该角色被部分用户所使用，不能简单删除！");
        }
        int i = baseMapper.deleteById(roleId);
        if (i<=0){
            throw new LZLException(ResultCode.ERROR,"删除角色失败！");
        }
    }
}
