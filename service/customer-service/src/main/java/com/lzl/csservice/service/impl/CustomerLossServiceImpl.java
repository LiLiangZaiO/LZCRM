package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.csservice.entity.*;
import com.lzl.csservice.entity.customerLoss.CustomerLoss;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieve;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieveVo;
import com.lzl.csservice.entity.customerLoss.CustomerLossVo;
import com.lzl.csservice.mapper.CustomerLossMapper;
import com.lzl.csservice.service.CustomerLossReprieveService;
import com.lzl.csservice.service.CustomerLossService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.csservice.service.CustomerService;
import com.lzl.csservice.utils.CustomerLossNoUtil;
import com.lzl.feign.clients.UserClient;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
@Service
public class CustomerLossServiceImpl extends ServiceImpl<CustomerLossMapper, CustomerLoss> implements CustomerLossService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerLossReprieveService customerLossReprieveService;

    @Autowired
    private UserClient userClient;

    /**
     * 分页条件查询 客户流失
     */
    @Override
    public IPage<CustomerLossVo> pageCustomerLossCondition(Page<CustomerLoss> page, CustomerLoss customerLoss) {
        QueryWrapper<CustomerLoss> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(customerLoss.getStatus())){
            queryWrapper.eq("status",customerLoss.getStatus());
        }
        queryWrapper.orderByDesc("loss_date");

        IPage<CustomerLoss> customerLossIPage = baseMapper.selectPage(page, queryWrapper);

        Page<CustomerLossVo> customerLossVoPage = new Page<>();

        BeanUtils.copyProperties(customerLossIPage,customerLossVoPage);

        List<CustomerLoss> records = customerLossIPage.getRecords();

        ArrayList<CustomerLossVo> clList = new ArrayList<>();

        for (CustomerLoss record : records) {
            CustomerLossVo customerLossVo = new CustomerLossVo();
            BeanUtils.copyProperties(record,customerLossVo);

            String customerName = customerService.getCustomerNameById(record.getCustomerId());
            customerLossVo.setCustomerName(customerName);

            clList.add(customerLossVo);
        }

        customerLossVoPage.setRecords(clList);

        return customerLossVoPage;
    }

    @Override
    public List<CustomerLossReprieveVo> getCustomerLossReprieveList(String lossId) {

        List<CustomerLossReprieve> list = customerLossReprieveService.list(new QueryWrapper<CustomerLossReprieve>().eq("loss_id",lossId).orderByDesc("create_time"));

        ArrayList<CustomerLossReprieveVo> customerLossReprieveList = new ArrayList<>();

        for (CustomerLossReprieve customerLossReprieve : list) {

            CustomerLossReprieveVo customerLossReprieveVo = new CustomerLossReprieveVo();
            BeanUtils.copyProperties(customerLossReprieve,customerLossReprieveVo);
            //设置流失客户名称
            String customerName = customerService.getCustomerNameById(customerLossReprieve.getLossId());
            customerLossReprieveVo.setLossName(customerName);
            //设置跟进人名称
            String userRealName = userClient.getUsernameByUserId(customerLossReprieve.getManagerId());
            customerLossReprieveVo.setManagerName(userRealName);

            customerLossReprieveList.add(customerLossReprieveVo);

        }

        return customerLossReprieveList;
    }

    /**
     * 添加 流失客户的 暂缓操作
     *  需要更改 客户流失表中的客户状态！
     *  若 流失状态的 确认流失或者挽回客户，则还需要更改 客户表中的状态--让其重新出现在公海池中！！！！
     */
    @Override
    public void addCustomerLossReprieve(CustomerLossReprieve customerLossReprieve) {

        List<CustomerLossReprieve> list = customerLossReprieveService.list(new QueryWrapper<CustomerLossReprieve>().orderByDesc("create_time").eq("loss_id", customerLossReprieve.getLossId()).last("limit 1"));
        int size = list.size();
        //1挽回客户
        if (size!=0){
            CustomerLossReprieve one = list.get(0);
            if (one.getLossStatus().equals("1")){
                throw new LZLException(ResultCode.ERROR,"该流失客户已经成功挽回！无需操作！");
            }

        }

        //获取更近人的ID
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        customerLossReprieve.setManagerId(userId);
        //添加流失措施
        customerLossReprieveService.save(customerLossReprieve);

        //确认挽回客户
        if (customerLossReprieve.getLossStatus().equals("1")){
            customerService.updateCustomerStatus(customerLossReprieve.getLossCustomerId(),4);
        }
        //确认流失客户--一般进入流失状态，在客户中的状态也是标记为已流失客户！ 此修改只适用其他状态改为确认流失起作用
        if (customerLossReprieve.getLossStatus().equals("2")){
            customerService.updateCustomerStatus(customerLossReprieve.getLossCustomerId(),3);
        }

        //更改流失表的状态
        Integer update = baseMapper.updateCustomerLossStatus(customerLossReprieve.getLossId(),customerLossReprieve.getLossStatus());
        if (update<=0){
            throw new LZLException(ResultCode.ERROR,"更改客户流失状态失败！");
        }
    }

    @Override
    public void updateCustomerLossReprieve(CustomerLossReprieve customerLossReprieve) {

        //更新 客户流失措施
        customerLossReprieveService.updateById(customerLossReprieve);

        //解决第八点Bug-修改第二条的状态而导致，其他两表数据状态改变！若为挽回客户-而不能再次做任何操作！！
        List<CustomerLossReprieve> list = customerLossReprieveService.list(new QueryWrapper<CustomerLossReprieve>().orderByDesc("create_time").eq("loss_id", customerLossReprieve.getLossId()).last("limit 1"));
        CustomerLossReprieve one = list.get(0);

        //1挽回客户
        if (one.getLossStatus().equals("1")){
            throw new LZLException(ResultCode.ERROR,"该流失客户已经成功挽回！无需操作！");
        }

        //若是第一条则修改
        if (customerLossReprieve.getId().equals(one.getId())){
            //确认挽回客户
            if (customerLossReprieve.getLossStatus().equals("1")){
                customerService.updateCustomerStatus(customerLossReprieve.getLossCustomerId(),4);
            }
            //确认流失客户--一般进入流失状态，在客户中的状态也是标记为已流失客户！ 此修改只适用其他状态改为确认流失起作用
            if (customerLossReprieve.getLossStatus().equals("2")){
                customerService.updateCustomerStatus(customerLossReprieve.getLossCustomerId(),3);
            }

            //更改流失表的状态
            Integer update = baseMapper.updateCustomerLossStatus(customerLossReprieve.getLossId(),customerLossReprieve.getLossStatus());
            if (update<=0){
                throw new LZLException(ResultCode.ERROR,"更改客户流失状态失败！");
            }
        }

    }

    /**
     * 删除流失客户暂缓措施记录
     * 删除客户流失的措施，由于在更新或者添加措施的时候，需要更改客户表中的状态和 更改
     * 	客户流失表中的状态！ 若轻易的删除---则状态会不统一
     * 	      情况1：删除的措施对应状态---确认流失状态2，则客户表-已流失状态3、流失表-确认流失状态2
     * 	     情况2：删除的措施对应状态---将要流失状态0，则客户表-已流失状态3、流失表-将要流失状态0
     *       情况3：删除的措施对应状态---挽回客户状态1，则客户表-挽回客户状态4、流失表-挽回客户状态1
     *
     * 	解决：在对应措施表中 按创建时间查询第一条数据，然后再修改 两个表中的数据！
     * 		若无数据-则两个表都以  已流失、将要流失更新状态！
     */
    @Override
    public void deleteCustomerLossReprieve(String customerLossReprieveId) {
        //根据流失措施Id查询到 流失客户的ID
        String customerLossId = customerLossReprieveService.getCustomerLossId(customerLossReprieveId);


        //解决第八点Bug-修改第二条的状态而导致，其他两表数据状态改变！若为挽回客户-而不能再次做任何操作！！
        List<CustomerLossReprieve> customerLossReprieveList = customerLossReprieveService.list(new QueryWrapper<CustomerLossReprieve>().orderByDesc("create_time").eq("id", customerLossReprieveId).last("limit 1"));
        CustomerLossReprieve one = customerLossReprieveList.get(0);

        //1挽回客户
        if (one.getLossStatus().equals("1")){
            throw new LZLException(ResultCode.ERROR,"该流失客户已经成功挽回！无需操作！");
        }

        //删除措施数据！
        customerLossReprieveService.removeById(customerLossReprieveId);
        //根据 流失客户的ID 查询到 第一条 措施数据
        List<CustomerLossReprieve> list = customerLossReprieveService.list(new QueryWrapper<CustomerLossReprieve>().eq("loss_customer_id", customerLossId)
                .orderByDesc("create_time"));

        if (list.size()==0){
            //若无数据、则两个表都以  已流失、将要流失更新状态！
            customerService.updateCustomerStatus(customerLossId,3);
            baseMapper.updateCustomerLossStatus(customerLossId, String.valueOf(0));
        }else {
            //存在数据、则只需 按时间排序第一条
            CustomerLossReprieve customerLossReprieve = list.get(0);
            String lossStatus = customerLossReprieve.getLossStatus();
            if (lossStatus.equals(String.valueOf(0))){
                //将要流失状态
                customerService.updateCustomerStatus(customerLossId,3);
                baseMapper.updateCustomerLossStatus(customerLossId, String.valueOf(0));
            }else if (lossStatus.equals(String.valueOf(1))){
                //挽回客户状态---这种状态基本不可能--因为成功挽回之后是不能操作的
                customerService.updateCustomerStatus(customerLossId,4);
                baseMapper.updateCustomerLossStatus(customerLossId, String.valueOf(1));
            }else if (lossStatus.equals(String.valueOf(2))){
                //确认流失状态
                customerService.updateCustomerStatus(customerLossId,3);
                baseMapper.updateCustomerLossStatus(customerLossId, String.valueOf(2));
            }
        }
    }

    @Override
    public CustomerLossReprieve getCustomerLossReprieveById(String customerLossReprieveId) {
        CustomerLossReprieve customerLossReprieve = customerLossReprieveService.getById(customerLossReprieveId);
        return customerLossReprieve;
    }

    /**
     * 定时任务----在公海池的客户若修改时间超过 10天 无人接管--即进入客户流失状态
     */
    @Transactional
    @Override
    public void enteringCustomerLoss() {

        //查询 在公海池的客户若修改时间超过 10天 无人接管
        List<Customer> enteringCustomerLossList = customerService.getEnteringCustomerLossList();

        if (enteringCustomerLossList != null && enteringCustomerLossList.size()>0){

            List<String> customerIdList = new ArrayList<>();
            List<CustomerLoss> customerLossList = new ArrayList<>();

            for (Customer customer : enteringCustomerLossList) {
                //向 客户流失表中添加数据
                CustomerLoss customerLoss = new CustomerLoss();
                customerLoss.setId(CustomerLossNoUtil.getOrderNo());
                customerLoss.setCustomerId(customer.getId());
                customerLoss.setReason("超过期限，无人接理！");
                // 0 ---将要流失状态
                customerLoss.setStatus("0");
                customerLoss.setLossDate(new Date());

                customerLossList.add(customerLoss);
                customerIdList.add(customer.getId());

            }
            //批量添加客户流失
            baseMapper.insertCustomerLossList(customerLossList);
            //批量更新客户状态
            customerService.updateCustomerStatusByIds(customerIdList);

        }


    }

}
