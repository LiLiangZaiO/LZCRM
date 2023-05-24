package com.lzl.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.common_utils.utils.MD5;
import com.lzl.feign.clients.CustomerClient;
import com.lzl.servicebase.exceptionHandler.LZLException;
import com.lzl.userservice.entity.*;
import com.lzl.userservice.mapper.UserMapper;
import com.lzl.userservice.service.RoleService;
import com.lzl.userservice.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.userservice.utils.DefaultPasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private CustomerClient customerClient;

    /**
     * SpringSecurity 根据账号获取用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在！");
        }
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(user);
        //比较特殊，目前是角色和用户一对一的关系  因此新建了一个方法
        List<Role> authorities = roleService.selectRolesByUserId(user.getId());
        securityUser.setRoles(authorities);
        return securityUser;
    }

    @Override
    public IPage<User> pageQuery(Page<User> page, UserVo userVo) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String realName = userVo.getRealName();
        String status = userVo.getStatus();
        String lastLoginTimeStart = userVo.getLastLoginTimeStart();
        String lastLoginTimeEnd = userVo.getLastLoginTimeEnd();
        String roleId = userVo.getRoleId();

        if (StringUtils.hasText(realName)) {
            queryWrapper.eq("real_name", realName);
        }
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        if (StringUtils.hasText(lastLoginTimeStart) && StringUtils.hasText(lastLoginTimeEnd)) {
            queryWrapper.between("last_login_time", lastLoginTimeStart, lastLoginTimeEnd);
        }
        if (!StringUtils.isEmpty(roleId)) {
//            List<String> userIds = baseMapper.selectUserIdByRoleIds(roleIds);
            List<String> userIds = baseMapper.selectUserIdByRoleId(roleId);
            if (userIds.size() > 0) {
                queryWrapper.in("id", userIds);
            } else {
                //如果根据角色id发现没有匹配到的 用户id，则需要返回 空值
                //即 LZLZLZ 当作不存在的值
                queryWrapper.eq("real_name", "LZLZLZ");
            }
        }

        queryWrapper.eq("delete_status", 0);
        queryWrapper.orderByDesc("last_login_time");
        return baseMapper.selectPage(page, queryWrapper);
    }


    @Override
    public Boolean addUserInfo(UserVo userVo) {

        Integer count = baseMapper.selectCount(new QueryWrapper<User>().eq("username", userVo.getUsername()));
        if (count > 0) {
            throw new LZLException(ResultCode.ERROR, "该用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setPassword(MD5.encrypt(userVo.getPassword()));
        int insert = baseMapper.insert(user);

        if (userVo.getRoleId() != null) {
            //需要把 角色和用户  插入 角色用户关系表中
            //1.需要得到刚刚插入用户的id
            User user1 = baseMapper.selectOne(new QueryWrapper<User>().eq("username", userVo.getUsername()));

            Boolean result = baseMapper.insertRoleUser(user1.getId(), userVo.getRoleId());
            if (!result) {
                throw new LZLException(ResultCode.ERROR, "插入用户角色关系表失败！");
            }
        }

        return insert > 0;
    }

    @Override
    public Boolean updateUserInfo(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        int i = baseMapper.updateById(user);
        //判断表中是否有数据，有数据则是修改，无数据则是插入
        Boolean result = baseMapper.selectUserRole(user.getId());
        Boolean b = false;
        if (result) {
            //更新用户角色表的内容
            b = baseMapper.updateUserRole(user.getId(), userVo.getRoleId());
        } else {
            //插入
            b = baseMapper.insertRoleUser(user.getId(), userVo.getRoleId());
        }
        return i > 0 && b;
    }

    @Override
    public String getUserIdByUsername(String username) {
        return baseMapper.getUserIdByUsername(username);
    }

    @Override
    public String getUsernameByUserId(String userId) {
        return baseMapper.getUsernameByUserId(userId);
    }

    /**
     *  需要排除 当前客户经理Id，获取其他客户经理id
     * @return
     */
    @Override
    public List<User> getUsernameList() {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = baseMapper.getUserIdByUsername(username);
        List<User> usernameList = baseMapper.getUsernameList(userId);
        return usernameList;
    }

    @Override
    public List<User> getAllRealNameList() {
        List<User> userList = baseMapper.selectList(new QueryWrapper<User>().select("id", "real_name").eq("status", '0'));
        return userList;
    }

    /**
     * 查询 自己的个人资料
     * @return
     */
    @Override
    public UserInfoVo getMyUserInfo() {
        //需-从当前线程取之前保存的用户信息Id--得到用户账户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("status", 0));

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user,userInfoVo);

        //查询职位名称
        Role role = roleService.selectRoleByUserId(user.getId());
        userInfoVo.setRoleName(role.getName());
        return userInfoVo;
    }

    @Override
    public void updateMyUserInfo(UserVo userVo) {

        if (!StringUtils.hasText(userVo.getRealName())) {
            throw new LZLException(ResultCode.ERROR,"用户真实姓名不能为空！");
        }
        if (!StringUtils.hasText(userVo.getEmail())) {
            throw new LZLException(ResultCode.ERROR,"用户邮箱不能为空！");
        }

        if (!StringUtils.hasText(userVo.getPhoneNumber())) {
            throw new LZLException(ResultCode.ERROR,"用户手机号码不能为空！");
        }
        User user = new User();
        BeanUtils.copyProperties(userVo,user);

        baseMapper.updateById(user);

    }

    @Override
    public void updateMyPassword(UserPassword userPassword) {
        //需-从当前线程取之前保存的用户信息Id--得到用户账户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("status", 0));
        DefaultPasswordEncoder defaultPasswordEncoder = new DefaultPasswordEncoder();
        String oldPassword = defaultPasswordEncoder.encode(userPassword.getOldPassword());

        if (!user.getPassword().equals(oldPassword)){
            throw new LZLException(ResultCode.ERROR,"原密码不正确！");
        }

        if (!userPassword.getNewPassword().equals(userPassword.getConfirmPassword())){
            throw new LZLException(ResultCode.ERROR,"两次密码不正确！");
        }

        String confirmPassword = defaultPasswordEncoder.encode(userPassword.getConfirmPassword());

        baseMapper.updateMyPassword(username,confirmPassword);


    }

    /**
     * 删除用户--不能简单删除
     *  需要判断该用户什么职位，若是客户经理则需要 查询该用户下是否有客户
     * @param userId
     * @return
     */
    @Override
    public void deleteUserById(String userId) {
        //查询该用户 是什么角色
        Role role = roleService.selectRoleByUserId(userId);
        //如果是 客户经理、则需要 查询 是否有 客户
        if (!StringUtils.isEmpty(role)){
            if (role.getName().equals("客户经理")){
                //远程调用
                Integer count = customerClient.countMyCustomerNumber(userId);
                if (count>0){
                    throw new LZLException(ResultCode.ERROR,"该用户还接管着部分客户，不能简单删除");
                }
            }
        }


        Integer delete = baseMapper.deleteUserById(userId);

        if (delete<=0){
            throw new LZLException(ResultCode.ERROR,"删除用户失败！");
        }

    }


}
