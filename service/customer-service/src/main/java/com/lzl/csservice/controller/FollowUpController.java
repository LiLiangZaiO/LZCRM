package com.lzl.csservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.FollowUp;
import com.lzl.csservice.entity.FollowUpVo;
import com.lzl.csservice.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
@RestController
@RequestMapping("/followUp")
public class FollowUpController {

    @Autowired
    private FollowUpService followUpService;

    /**
     * 假删--即修改删除状态为 1
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/deleteFollowUpInfo/{followUpId}")
    public R deleteFollowUpInfo(@PathVariable String followUpId){
        Boolean result = followUpService.deleteFollowUpInfo(followUpId);
        if (!result){
            return R.error().message("删除销售过程失败!");
        }
        return R.ok();

    }

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/updateFollowUpInfo")
    public R updateFollowUpInfo(@Validated @RequestBody FollowUp followUp){
        boolean update = followUpService.updateFollowUpInfo(followUp);
        if (!update){
            return R.error().message("更新销售过程失败!");
        }
        return R.ok();
    }

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/addFollowUpInfo")
    public R addFollowUpInfo(@Validated @RequestBody FollowUp followUp){
        Boolean result = followUpService.addFollowUpInfo(followUp);
        if (!result){
            return R.error().message("添加销售过程失败!");
        }
        return R.ok();
    }



    /**
     * 根据 客户的Id 获取 客户的销售过程
     *      销售过程需要 返回 客户名称、经理名称！
     * @param customerId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getAllFollowUpList/{customerId}")
    public R getAllFollowUpList(@PathVariable String customerId){
        List<FollowUpVo> followUpList = followUpService.getAllFollowUpList(customerId);
        return R.ok().data("followUpList",followUpList);
    }

}

