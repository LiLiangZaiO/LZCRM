package com.lzl.saleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.feign.clients.UserClient;
import com.lzl.saleservice.entity.sale.SalesTarget;
import com.lzl.saleservice.entity.sale.SalesTargetVo;
import com.lzl.saleservice.mapper.SalesTargetMapper;
import com.lzl.saleservice.service.SalesTargetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-15
 */
@Service
public class SalesTargetServiceImpl extends ServiceImpl<SalesTargetMapper, SalesTarget> implements SalesTargetService {

    @Autowired
    private UserClient userClient;

    /**
     * 分页条件查询 销售目标
     */
    @Override
    public IPage<SalesTargetVo> pageSalesTargetCondition(Page<SalesTarget> salesTargetPage, SalesTarget salesTarget) {

        QueryWrapper<SalesTarget> queryWrapper = new QueryWrapper<>();

        //目标执行者ID
        if (StringUtils.hasText(salesTarget.getExecutorId())){
            queryWrapper.eq("executor_id",salesTarget.getExecutorId());
        }
        //目标完成状态
        if (StringUtils.hasText(salesTarget.getStatus())){
            queryWrapper.eq("status",salesTarget.getStatus());
        }

        queryWrapper.orderByAsc("end_date");
        IPage<SalesTarget> salesTargetIPage = baseMapper.selectPage(salesTargetPage, queryWrapper);

        //修改 返回的记录
        Page<SalesTargetVo> salesTargetVoPage = new Page<>();
        BeanUtils.copyProperties(salesTargetIPage,salesTargetVoPage);

        ArrayList<SalesTargetVo> salesTargetVos = new ArrayList<>();
        List<SalesTarget> salesTargetList = salesTargetIPage.getRecords();

        for (SalesTarget target : salesTargetList) {
            SalesTargetVo salesTargetVo = new SalesTargetVo();
            BeanUtils.copyProperties(target,salesTargetVo);

            //设置目标设定者名称
            String setterId = target.getSetterId();
            String setterName = userClient.getUsernameByUserId(setterId);
            salesTargetVo.setSetterName(setterName);
            //设置目标执行者名称
            String executorId = target.getExecutorId();
            String executorName = userClient.getUsernameByUserId(executorId);
            salesTargetVo.setExecutorName(executorName);

            salesTargetVos.add(salesTargetVo);
        }
        salesTargetVoPage.setRecords(salesTargetVos);
        return salesTargetVoPage;
    }


    @Override
    public void addSalesTarget(SalesTarget salesTarget) {

        //收到判断时间是否非空
        if (StringUtils.isEmpty(salesTarget.getStartDate())){
            throw new LZLException(ResultCode.ERROR,"目标开始时间不能为空！");
        }
        if (StringUtils.isEmpty(salesTarget.getEndDate())){
            throw new LZLException(ResultCode.ERROR,"目标结束时间不能为空！");
        }

        //设置目标设定者ID
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        salesTarget.setSetterId(userId);

        int insert = baseMapper.insert(salesTarget);
        if (insert<=0){
            throw new LZLException(ResultCode.ERROR,"添加销售目标失败！");
        }

    }

    /**
     * 查询我的 销售任务
     *  未完成的任务！--在首页显示
     *  只显示前三条
     * @return
     */
    @Override
    public List<SalesTarget> getMySalesTarget() {
        //设置目标设定者ID
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户账户---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        QueryWrapper<SalesTarget> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("end_date");
        queryWrapper.eq("executor_id",userId);
        queryWrapper.eq("status",'0');
        //使用last方法拼接sql语句
        queryWrapper.last("limit 3");
        List<SalesTarget> salesTargetList = baseMapper.selectList(queryWrapper);
        return salesTargetList;
    }

    @Override
    public IPage<SalesTarget> pageMySalesTargetCondition(Page<SalesTarget> salesTargetPage, SalesTarget salesTarget) {
        QueryWrapper<SalesTarget> queryWrapper = new QueryWrapper<>();
        //目标完成状态
        if (StringUtils.hasText(salesTarget.getStatus())){
            queryWrapper.eq("status",salesTarget.getStatus());
        }
        //设置目标设定者ID
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        queryWrapper.eq("executor_id",userId);

        queryWrapper.orderByAsc("end_date");
        IPage<SalesTarget> salesTargetIPage = baseMapper.selectPage(salesTargetPage, queryWrapper);

        return salesTargetIPage;
    }


}
