package com.lzl.saleservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.saleservice.entity.sale.SalesTarget;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.saleservice.entity.sale.SalesTargetVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-15
 */
public interface SalesTargetService extends IService<SalesTarget> {

    IPage<SalesTargetVo> pageSalesTargetCondition(Page<SalesTarget> salesTargetPage, SalesTarget salesTarget);

    void addSalesTarget(SalesTarget salesTarget);

    List<SalesTarget> getMySalesTarget();

    IPage<SalesTarget> pageMySalesTargetCondition(Page<SalesTarget> salesTargetPage, SalesTarget salesTarget);
}
