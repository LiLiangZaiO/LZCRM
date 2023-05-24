package com.lzl.userservice.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.userservice.entity.Role;
import com.lzl.userservice.service.RoleService;
import com.lzl.common_utils.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 删除角色---不能简单删除
     *      需要 查询角色是否被 用户所用
     * @param roleId
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteRoleById/{roleId}")
    public R deleteRoleById(@PathVariable String roleId){
        roleService.deleteRoleById(roleId);
        return R.ok();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/roles")
    public R getRoles(){
        List<Role> list = roleService.list();
        return R.ok().data("roles",list);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/pageRoles/{current}/{limit}")
    public R pageRoleList(@PathVariable long current,
                          @PathVariable long limit){
        Page<Role> page = new Page<>(current,limit);
        IPage<Role> roleIPage = roleService.page(page);
        return R.ok().data("roleList",roleIPage);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/updateRoleInfo")
    public R updateRoleInfo(@RequestBody Role role){
        boolean b = roleService.updateRole(role);
        if (!b){
            return R.error().message("更新失败!");
        }
        return R.ok();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addRole")
    public R addRole(@RequestBody Role role){
        boolean result =  roleService.addRole(role);
        if (!result){
            return R.error().message("添加失败!");
        }
        return R.ok();
    }


    /**
     * 修改角色信息、根据id查询角色信息
     * @param roleId
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/getRoleById/{roleId}")
    public R getRoleById(@PathVariable String roleId){
        Role role = roleService.getById(roleId);
        return R.ok().data("roleInfo",role);
    }

}

