package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.csservice.entity.FollowUp;
import com.lzl.csservice.entity.FollowUpVo;
import com.lzl.csservice.mapper.FollowUpMapper;
import com.lzl.csservice.service.CustomerService;
import com.lzl.csservice.service.FollowUpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.feign.clients.UserClient;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
@Service
public class FollowUpServiceImpl extends ServiceImpl<FollowUpMapper, FollowUp> implements FollowUpService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserClient userClient;

    /**
     * 根据 客户的Id 获取 客户的销售过程
     *      销售过程需要 返回 客户名称、经理名称！
     */
    @Override
    public List<FollowUpVo> getAllFollowUpList(String customerId) {

        //查询的销售过程----必须是 未删除的！--即 删除状态为 0
        List<FollowUp> followUpList = baseMapper.selectList(new QueryWrapper<FollowUp>().eq("customer_id", customerId).eq("delete_status",0).orderByDesc("create_time"));

        ArrayList<FollowUpVo> followUpVoList = new ArrayList<>();

        for (FollowUp followUp : followUpList) {

            FollowUpVo followUpVo = new FollowUpVo();

            BeanUtils.copyProperties(followUp,followUpVo);

            //设置 客户名称
            String customerName = customerService.getCustomerNameById(customerId);
            followUpVo.setCustomerName(customerName);

            // 设置 跟进人名称---当时所属客户经理名称
            String username = userClient.getUsernameByUserId(followUp.getManagerId());
            followUpVo.setManagerName(username);

            followUpVoList.add(followUpVo);
        }

        return followUpVoList;
    }

    /**
     * 在创建销售过程或者更新销售过程中，需要修改-同步更新 客户的 成熟度！
     * @param followUp
     * @return
     */
    @Override
    public Boolean addFollowUpInfo(FollowUp followUp) {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        //设置跟进人的ID
        followUp.setManagerId(userId);

        int insert = baseMapper.insert(followUp);
        if (insert>0){
            //当插入成功后，用户客户的成熟度！
            customerService.updateCustomerMaturity(followUp.getCustomerId(),followUp.getMaturity());
            return true;
        }
        throw new LZLException(ResultCode.ERROR, "添加销售过程失败！");
    }

    @Override
    public boolean updateFollowUpInfo(FollowUp followUp) {
        int update = baseMapper.updateById(followUp);
        if (update>0){
            //当插入成功后，用户客户的成熟度！
            customerService.updateCustomerMaturity(followUp.getCustomerId(),followUp.getMaturity());
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteFollowUpInfo(String followUpId) {

        //更新 删除状态即可
        Boolean result = baseMapper.deleteFollowUp(followUpId);

        return result;
    }


}
