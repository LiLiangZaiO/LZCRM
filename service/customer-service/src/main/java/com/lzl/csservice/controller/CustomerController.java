package com.lzl.csservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.Customer;
import com.lzl.csservice.entity.CustomerLinkManVo;
import com.lzl.csservice.entity.MyCustomerVo;
import com.lzl.csservice.service.CustomerService;
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
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 远程调用---统计该客户经理有多少个客户，在未完成阶段
     * @param userId
     * @return
     */
    @GetMapping("/countMyCustomerNumber/{userId}")
    public Integer countMyCustomerNumber(@PathVariable String userId){
        Integer count = customerService.countMyCustomerNumber(userId);
        return count;
    }


    /**
     * 我的客户--更新客户的资料
     * @param customer
     * @return
     */
    @PostMapping("/updateMyCustomerInfo")
    public R updateMyCustomerInfo(@Validated @RequestBody Customer customer){
        customerService.updateMyCustomerInfo(customer);
        return R.ok();
    }

    /**
     * 公海池---删除客户
     *  需要 修改客户删除状态即可
     *      只能 删除属于自己创建的客户!
     * @param customerId
     * @return
     */
    @GetMapping("/deleteCustomerById/{customerId}")
    public R deleteCustomerById(@PathVariable String customerId){
        customerService.deleteCustomerById(customerId);
        return R.ok();
    }

    /**
     * 客户详情--客户失败
     * @param customerId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/failedMyCustomer/{customerId}")
    public R failedMyCustomer(@PathVariable String customerId){
        customerService.failedMyCustomer(customerId);
        return R.ok();
    }

    /**
     * 查询 我的失败客户
     * @param current
     * @param limit
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/pageMyFailedCustomerList/{current}/{limit}")
    public R pageMyFailedCustomerList(@PathVariable long current,
                                    @PathVariable long limit){
        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> failedCustomerList =  customerService.pageMyFailedCustomerList(page);
        return R.ok().data("failedCustomerList",failedCustomerList);


    }

    /**
     * 查询 失败客户
     * @param current
     * @param limit
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/pageFailedCustomerList/{current}/{limit}")
    public R pageFailedCustomerList(@PathVariable long current,
                                    @PathVariable long limit){
        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> failedCustomerList =  customerService.pageFailedCustomerList(page);
        return R.ok().data("failedCustomerList",failedCustomerList);


    }


    /**
     * 远程调用
     * 更新客户状态---在数据库存储的是String字符串，但是中文字符串是不能在路径中传过来的
     * 只能用数值代替
     *  成交客户----0
     *  意向客户----1
     *  失败客户-----2
     *  已流失客户----3
     * @return
     */
    @GetMapping("/updateCustomerStatus/{customerId}/{status}")
    public Integer updateCustomerStatus(@PathVariable String customerId,
                                        @PathVariable Integer status){
        Integer i = customerService.updateCustomerStatus(customerId,status);
        return i;
    }


    /**
     * 远程调用
     * @param customerId
     * @return
     */
    @GetMapping("/getCustomerNameById/{customerId}")
    public String getCustomerNameById(@PathVariable("customerId") String customerId){
        String customerName = customerService.getCustomerNameById(customerId);
        return customerName;
    }

    /**
     * 查找 我的客户的详细信息
     * @param customerId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getMyCustomerInfoById/{customerId}")
    public R getMyCustomerInfoById(@PathVariable String customerId){
        MyCustomerVo myCustomer = customerService.getMyCustomerInfoById(customerId);
        return R.ok().data("myCustomerInfo",myCustomer);
    }

    /**
     * 在 我的客户 中删除客户，应该把属于的客户经理删除，然后该客户回到 客户公海池中
     * @param customerId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @Transactional
    @GetMapping("/deleteMyCustomer/{customerId}")
    public R deleteMyCustomer(@PathVariable String customerId){
        Boolean result = customerService.deleteMyCustomer(customerId);
        return R.ok();
    }


    /**
     * ！查询我的成交客户------客户状态为--成交客户
     * 根据修改时间排序
     * @param current
     * @param limit
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("pageMyTdCustomerList/{current}/{limit}")
    public R pageMyTdCustomerList(@PathVariable long current,
                                @PathVariable long limit){
        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> tradingCustomerList =  customerService.pageMyTdCustomerList(page);
        return R.ok().data("tradingCustomerList",tradingCustomerList);
    }

    /**
     * 所有人！查询成交客户------客户状态为--成交客户
     * 根据修改时间排序
     * @param current
     * @param limit
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("pageTdCustomerList/{current}/{limit}")
    public R pageTdCustomerList(@PathVariable long current,
                                     @PathVariable long limit){
        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> tradingCustomerList =  customerService.pageTdCustomerList(page);
        return R.ok().data("tradingCustomerList",tradingCustomerList);
    }

    /**
     * 查询我的客户------客户状态为--非成交客户
     * 成交客户、失败客户buxains
     * 根据修改时间排序
     * @param current
     * @param limit
     * @param customer
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("pageMyCustomerCondition/{current}/{limit}")
    public R pageMyCustomerCondition(@PathVariable long current,
                                     @PathVariable long limit,
                                     @RequestBody(required = false) Customer customer){
        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> myCustomerPage =  customerService.pageMyCustomerCondition(page,customer);
        return R.ok().data("myCustomerPage",myCustomerPage);
    }

    /**
     * 更新客户的信息--------只能更新属于 自己添加的客户，其他用户添加的客户是 不能随意修改的！
     * 但是 最高级的管理员要修改该客户信息 怎么搞？？？？
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @Transactional
    @PostMapping("/updateCustomerInfo")
    public R updateCustomerInfo(@Validated @RequestBody CustomerLinkManVo customerLinkManVo){
        Boolean result = customerService.updateCustomerInfo(customerLinkManVo);
        if (!result){
            return R.error().message("修改失败!");
        }
        return R.ok();
    }

    /**
     * 添加客户进 公海池-----是无客户经理管理的 即customer_id=0
     * 创建人--需要录入当前登录的用户（客户经理）creater_id
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @Transactional
    @PostMapping("/addCustomerAndLinkman")
    public R addCustomerAndLinkman(@Validated @RequestBody CustomerLinkManVo customerLinkManVo){

        Boolean result = customerService.addCustomerAndLinkman(customerLinkManVo);

        if (!result){
            return R.error().message("添加失败!");
        }
        return R.ok();
    }


    /**
     * 发送ajax判断 当前客户名称是否存在
     *      不能简单判断---客户状态为成交状态，不需要再查询
     * @param customerName
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/checkCustomerNameExist/{customerName}")
    public R checkCustomerNameExist(@PathVariable String customerName){
        int i = customerService.count(new QueryWrapper<Customer>().eq("name", customerName)
                .ne("status","成交客户")
                .ne("status","失败客户")
                .eq("delete_status",0));
        return R.ok().data("count",i);
    }


    /**
     * 添加为我的客户
     *  需求 手动添加 客户转移记录！！！
     *   涉及两张表的操作---需求设置事务！
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @Transactional
    @GetMapping("/addMyCustomer/{customerId}")
    public R addMyCustomer(@PathVariable String customerId){

        Boolean result = customerService.addMyCustomer(customerId);
        if (!result) {
            return R.error().message("添加我的客户失败！");
        }

        return R.ok();
    }


    /**
     * 分页条件查询 无客户经理 的客户
     *  需要根据修改时间排序
     * 即  客户公海
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/pageCustomerCondition/{current}/{limit}")
    public R pageCustomerCondition(@PathVariable long current,
                                   @PathVariable long limit,
                                   @RequestBody(required = false) Customer customer){

        Page<Customer> page = new Page<>(current, limit);
        IPage<Customer> customerIPage =  customerService.pageCustomerCondition(page,customer);
        return R.ok().data("customerPage",customerIPage);
    }

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getCustomerInfoById/{customerId}")
    public R getCustomerInfoById(@PathVariable String customerId){
        Customer customer = customerService.getById(customerId);

        return R.ok().data("customerInfo",customer);
    }





}

