package com.lzl.csservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.Linkman;
import com.lzl.csservice.service.LinkmanService;
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
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/linkman")
public class LinkmanController {

    @Autowired
    private LinkmanService linkmanService;

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/updateLinkmanInfo")
    public R updateLinkmanInfo(@RequestBody Linkman linkman){
        Boolean result = linkmanService.updateLinkman(linkman);
        if (!result){
            return R.error().message("更新客户联系人失败!");
        }
        return R.ok();
    }

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/deleteLinkman/{linkmanId}")
    public R deleteLinkman(@PathVariable String linkmanId){
        boolean result = linkmanService.removeById(linkmanId);
        if (!result){
            return R.error().message("删除客户联系人失败!");
        }
        return R.ok();
    }




    /**
     * 添加联系人
     *  若添加的是 主要联系人，则需要把其他全部主要联系人设置为 普通联系人
     *      即 主要联系人只有一个
     * @param linkman
     * @return
     */
    @PostMapping("/addLinkmanInfo")
    public R addLinkmanInfo(@RequestBody Linkman linkman){
        Boolean result = linkmanService.addLinkman(linkman);
        if (!result){
            return R.error().message("添加失败!");
        }
        return R.ok();
    }

    /**
     *    获取 主要联系人！---即 level=0
     * @param customerId
     * @return
     */
    @GetMapping("/getLinkmanByCustomerId/{customerId}")
    public R getLinkmanByCustomerId(@PathVariable String customerId){
        //根据客户的Id 得到客户所属的联系人-------目前就假设 一对一的关系
        Linkman linkman = linkmanService.getOne(new QueryWrapper<Linkman>().eq("customer_id", customerId).eq("level",0));
        return R.ok().data("linkmanInfo",linkman);
    }


    /**
     * 获取 当前客户id 所有的 联系人
     *      包括 普通联系人、主要联系人
     * @return
     */
    @GetMapping("/getAllLinkmanList/{customerId}")
    public R getAllLinkmanList(@PathVariable String customerId){
        List<Linkman> linkmanList = linkmanService.list(new QueryWrapper<Linkman>().eq("customer_id", customerId));
        return R.ok().data("linkmanList",linkmanList);
    }

}

