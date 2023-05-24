package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.csservice.entity.customerTransfer.CustomerTransfer;
import com.lzl.csservice.entity.customerTransfer.CustomerTransferVo;
import com.lzl.csservice.mapper.CustomerTransferMapper;
import com.lzl.csservice.service.CustomerService;
import com.lzl.csservice.service.CustomerTransferService;
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
public class CustomerTransferServiceImpl extends ServiceImpl<CustomerTransferMapper, CustomerTransfer> implements CustomerTransferService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserClient userClient;

    /**
     * 根据客户的Id 得到客户的转移记录
     * @param customerId
     * @return
     */
    @Override
    public List<CustomerTransferVo> getCustomerTransferById(String customerId) {
        List<CustomerTransfer> ctList = baseMapper.selectList(new QueryWrapper<CustomerTransfer>().eq("customer_id", customerId).orderByDesc("create_time"));

        ArrayList<CustomerTransferVo> ctVoList = new ArrayList<>();

        for (CustomerTransfer customerTransfer : ctList) {

            CustomerTransferVo customerTransferVo = new CustomerTransferVo();

            BeanUtils.copyProperties(customerTransfer,customerTransferVo);

            //设置客户名称
            String customerName = customerService.getCustomerNameById(customerId);
            customerTransferVo.setCustomerName(customerName);
            //设置转移前客户经理名称
            String oldManagerName = userClient.getUsernameByUserId(customerTransfer.getOldManagerId());
            customerTransferVo.setOldManagerName(oldManagerName);
            //设置转移后客户经理名称
            String newManagerName = userClient.getUsernameByUserId(customerTransfer.getNewManagerId());
            customerTransferVo.setNewManagerName(newManagerName);

            ctVoList.add(customerTransferVo);

        }

        return ctVoList;
    }

    /**
     *   @Transactional 加了事务注解
     * @param customerTransfer
     * @return
     */
    @Override
    public Boolean addCustomerTransfer(CustomerTransfer customerTransfer) {

        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        customerTransfer.setOldManagerId(userId);

        int insert = baseMapper.insert(customerTransfer);

        if (insert>0){
            //当插入成功后，需要修改 客户表中的 所属客户经理的信息
            Boolean result = customerService.updateCustomerManagerId(customerTransfer.getCustomerId(),customerTransfer.getNewManagerId());
            if (result){
                return true;
            }else {
                throw new LZLException(ResultCode.ERROR, "更新客户所属客户经理失败！");
            }
        }
        throw new LZLException(ResultCode.ERROR, "客户转移失败！");
    }
}
