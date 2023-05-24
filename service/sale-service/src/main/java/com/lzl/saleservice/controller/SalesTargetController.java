package com.lzl.saleservice.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.R;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.saleservice.entity.sale.SalesTarget;
import com.lzl.saleservice.entity.sale.SalesTargetVo;
import com.lzl.saleservice.service.SalesTargetService;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/target")
public class SalesTargetController {

    @Autowired
    private SalesTargetService salesTargetService;

    /**
     * 查询我的 所有销售目标
     * @return
     */
    @PostMapping("/pageMySalesTargetCondition/{current}/{limit}")
    public R pageMySalesTargetCondition(@PathVariable Long current,
                                        @PathVariable Long limit,
                                        @RequestBody(required = false) SalesTarget salesTarget){
        Page<SalesTarget> salesTargetPage = new Page<>(current, limit);
        IPage<SalesTarget> salesTargetIPage = salesTargetService.pageMySalesTargetCondition(salesTargetPage,salesTarget);

        return R.ok().data("salesTargetIPage",salesTargetIPage);

    }

    /**
     * 查询我的 销售任务
     *  未完成的任务！--在首页显示
     *  只显示前三条
     * @return
     */
    @GetMapping("/getMySalesTarget")
    public R getMySalesTarget(){
        List<SalesTarget> salesTargetList = salesTargetService.getMySalesTarget();
        return R.ok().data("salesTargetList",salesTargetList);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteSalesTarget/{targetId}")
    public R deleteSalesTarget(@PathVariable String targetId){
        boolean b = salesTargetService.removeById(targetId);
        if (!b){
            throw new LZLException(ResultCode.ERROR," 删除销售目标失败！");
        }
        return R.ok();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/getSalesTargetInfo/{targetId}")
    public R getSalesTargetInfo(@PathVariable String targetId){
        SalesTarget salesTarget = salesTargetService.getById(targetId);
        return R.ok().data("salesTargetInfo",salesTarget);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/updateSalesTarget")
    public R updateSalesTarget(@Validated @RequestBody SalesTarget salesTarget){
        //收到判断时间是否非空
        if (StringUtils.isEmpty(salesTarget.getStartDate())){
            throw new LZLException(ResultCode.ERROR,"目标开始时间不能为空！");
        }
        if (StringUtils.isEmpty(salesTarget.getEndDate())){
            throw new LZLException(ResultCode.ERROR,"目标结束时间不能为空！");
        }
        boolean b = salesTargetService.updateById(salesTarget);
        if (!b){
            throw new LZLException(ResultCode.ERROR,"更改销售目标失败！");
        }
        return R.ok();
    }


    /**
     * 添加 销售任务
     * @param salesTarget
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/addSalesTarget")
    public R addSalesTarget(@Validated @RequestBody SalesTarget salesTarget){
        salesTargetService.addSalesTarget(salesTarget);
        return R.ok();
    }


    /**
     * 分页条件查询 销售目标
     * @param current
     * @param limit
     * @param salesTarget
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/pageSalesTargetCondition/{current}/{limit}")
    public R pageSalesTargetCondition(@PathVariable Long current,
                                      @PathVariable Long limit,
                                      @RequestBody(required = false) SalesTarget salesTarget){
        Page<SalesTarget> salesTargetPage = new Page<>(current, limit);
        IPage<SalesTargetVo> salesTargetIPage = salesTargetService.pageSalesTargetCondition(salesTargetPage,salesTarget);

        return R.ok().data("salesTargetIPage",salesTargetIPage);

    }

}

