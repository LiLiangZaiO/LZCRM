package com.lzl.userservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.userservice.entity.*;
import com.lzl.userservice.service.RoleService;
import com.lzl.userservice.service.UserService;
import com.lzl.common_utils.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 删除用户--不能简单删除
     *  需要判断该用户什么职位，若是客户经理则需要 查询该用户下是否有客户
     * @param userId
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteUserById/{userId}")
    public R deleteUserById(@PathVariable String userId){
        userService.deleteUserById(userId);
        return R.ok();
    }


    /**
     * 更新 我的密码
     * @param userPassword
     * @return
     */
    @PostMapping("/updateMyPassword")
    public R updateMyPassword(@Validated @RequestBody UserPassword userPassword){
        userService.updateMyPassword(userPassword);
        return R.ok();
    }


    /**
     * 个人资料--修改我的个人资料
     * @param user
     * @return
     */
    @PostMapping("/updateMyUserInfo")
    public R updateMyUserInfo(@RequestBody UserVo user){
        userService.updateMyUserInfo(user);
        return R.ok();
    }

    /**
     * 个人资料--查询我的信息
     * @return
     */
    @GetMapping("/getMyUserInfo")
    public R getMyUserInfo(){
        UserInfoVo myUserInfo = userService.getMyUserInfo();
        return R.ok().data("myUserInfo",myUserInfo);
    }

    /**
     * 创建销售目标--选择目标执行者（目前-包括自己）
     * 查询 所有客户经理的 id 和真实姓名
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/getAllRealNameList")
    public R getAllRealNameList(){
        List<User> realNameList = userService.getAllRealNameList();
        return R.ok().data("realNameList",realNameList);
    }


    /**
     * 转移客户时 用到！
     * 获取用户名字列表---只有 id 和 名字
     * 用户是 非 封禁的
     * @return
     */
    @GetMapping("/getUsernameList")
    public R getUsernameList(){
        List<User> usernameList = userService.getUsernameList();
        return R.ok().data("usernameList",usernameList);
    }


    /**
     * 远程调用---根据Id  查找 名字
     *  ---查询 所属客户名称
     *   该用户名字是-----用户的真实名字
     * @param userId
     * @return
     */
    @GetMapping("/getUsernameByUserId/{userId}")
    public String getUsernameByUserId(@PathVariable String userId){
        String username = userService.getUsernameByUserId(userId);
        return username;
    }


    /**
     * 远程调用---根据用户名字 获取当前用户的Id
     * @param username
     * @return
     */
    @GetMapping("/getUserIdByUsername/{username}")
    public String getUserIdByUsername(@PathVariable("username") String username){
        String userId = userService.getUserIdByUsername(username);
        return userId;
    }

    /**
     * 根据token获取用户信息
     * @return
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(){
        ///获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        //直接在这里更新最后登录时间
        user.setLastLoginTime(new Date());
        userService.updateById(user);
        return R.ok().data("name",user.getRealName()).data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }


    @ApiOperation(value = "注销")
    @PostMapping("/logout")
    public R logout(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization");
//        Boolean result = userService.logout(jwtToken);
        return R.ok().message("注销成功！");
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/pageUserCondition/{current}/{limit}")
    public R pageUserCondition(@PathVariable long current,
                               @PathVariable long limit,
                               @RequestBody(required = false)UserVo userVo){

        Page<User> page = new Page<>(current, limit);
        IPage<User> userPage =  userService.pageQuery(page,userVo);

        List<User> userList = userPage.getRecords();
        //根据用户的id 得到 角色id  并封装到新的 userInfoVo类中返回
        List<UserInfoVo> userInfoVoList = roleService.selectUserInfoVoByUserId(userList);
        userPage.setRecords(null);
        return R.ok().data("userPage",userPage).data("userInfoVoList",userInfoVoList);
    }


    @Secured("ROLE_ADMIN")
    @GetMapping("/getUserInfoById/{userId}")
    public R getUserInfoById(@PathVariable String userId){
        User user = userService.getById(userId);
        Role role = roleService.selectRoleByUserId(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setPassword("");
        if (role==null){
            userVo.setRoleId(null);
        }else {
            userVo.setRoleId(role.getId());
        }
        return R.ok().data("userInfo",userVo);
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/addUserInfo")
    public R addUserInfo(@Validated @RequestBody UserVo userVo){
        Boolean b = userService.addUserInfo(userVo);
        if (!b){
            return R.error().message("添加失败!");
        }
        return R.ok();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@Validated @RequestBody UserVo userVo){
        Boolean result =userService.updateUserInfo(userVo);
        if (!result){
            return R.error().message("更新失败!");
        }
        return R.ok();
    }


}

