package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.csservice.entity.*;
import com.lzl.csservice.entity.Report.Report;
import com.lzl.csservice.entity.customerTransfer.CustomerTransfer;
import com.lzl.csservice.mapper.CustomerMapper;
import com.lzl.csservice.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.csservice.service.CustomerTransferService;
import com.lzl.csservice.service.LinkmanService;
import com.lzl.feign.clients.SaleClient;
import com.lzl.feign.clients.UserClient;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private SaleClient saleClient;

    @Autowired
    private LinkmanService linkmanService;

    @Autowired
    private CustomerTransferService customerTransferService;

    /**
     * 分页条件查询 无客户经理 的客户
     *  需要根据修改时间排序
     * 即  客户公海
     * @return
     */
    @Override
    public IPage<Customer> pageCustomerCondition(Page<Customer> page, Customer customer) {
        QueryWrapper<Customer> queryWrapper = queryCustomerCondition(customer);
        //判断是否为我创建
        if (StringUtils.hasText(customer.getCreaterId())){
            //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //根据当前用户名字---远程调用用户模块得到当前的用户Id
            String userId = userClient.getUserIdByUsername(username);
            queryWrapper.eq("creater_id",userId);
        }
        //查询 没有 客户经理的 客户
        queryWrapper.eq("manager_id","0");
        queryWrapper.eq("delete_status",0);
        queryWrapper.ne("status","成交客户");
        queryWrapper.ne("status","已流失客户");
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 从公海从中添加客户
     *  需要手动设置添加客户转移记录
     *      涉及两张表的操作---需求设置事务！
     * @param customerId
     * @return
     */
    @Override
    public Boolean addMyCustomer(String customerId) {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        //根据客户的Id  把当前所属客户经理Id 更新进表中
        int i = baseMapper.updateCustomerId(customerId,userId);

        if (i>0){
            // 添加客户转移记录
            CustomerTransfer customerTransfer = new CustomerTransfer();
            customerTransfer.setCustomerId(customerId);
            //第一次从公海中 拿客户，转移前的客户经理默认是0
            customerTransfer.setOldManagerId("0");
            customerTransfer.setNewManagerId(userId);
            customerTransfer.setReason("从公海池中拿取该客户！");
            Boolean Boolean = customerTransferService.save(customerTransfer);
            if (Boolean){
               return true;
            }else {
                throw new LZLException(ResultCode.ERROR, "更新客户所属客户经理失败！");
            }
        }
        throw new LZLException(ResultCode.ERROR, "添加为我的客户失败！");
    }

    /**
     * 添加客户进 公海池-----是无客户经理管理的 即customer_id=0
     * 创建人--需要录入当前登录的用户（客户经理）creater_id
     * 添加 联系人--- 为 主要联系人！--（不是普通联系人--若添加普通联系人 则需要在其他地方添加）
     *      主要联系人---则需要自己手动设置 等级为0
     * @return
     */
    @Override
    public Boolean addCustomerAndLinkman(CustomerLinkManVo customerLinkManVo) {
        Linkman linkman = customerLinkManVo.getLinkman();
        if (StringUtils.isEmpty(customerLinkManVo.getLinkman().getBirthday())){
            throw new LZLException(ResultCode.ERROR,"客户主要联系人生日不能为空！");
        }

        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerLinkManVo,customer);

        //默认设置客户状态为 意向客户
        customer.setStatus("意向客户");

        //设置创建Id
        customer.setCreaterId(userId);
        //设置客户所属的客户经理---默认无客户经理--0
        customer.setManagerId("0");

        int a = baseMapper.insert(customer);
        if (a==0){
            throw new LZLException(ResultCode.ERROR, "添加客户信息失败！");
        }

        //添加 客户所属联系人
        linkman.setCustomerId(customer.getId());
        //设置 主要联系人！
        linkman.setLevel("0");
        boolean b = linkmanService.save(linkman);
        if (!b){
            throw new LZLException(ResultCode.ERROR, "添加客户联系人信息失败！");
        }
        return true;
    }

    /**
     * 更新客户的信息--------只能更新属于 自己添加的客户，其他用户添加的客户是 不能随意修改的！
     *                  现在增加 所属的客户经理 也能 修改用户  但是这里不用加入
     *                          //根据客户的Id得到 所属客户经理的Id
     *         String managerId =baseMapper.getManagerIdByCustomerId(customerId);
     *         因为 该 修改 是在公海池中修改的
     * @param customerLinkManVo
     * @return
     */
    @Override
    public Boolean updateCustomerInfo(CustomerLinkManVo customerLinkManVo) {

        //判断该客户名称是否存在
        Customer customerOne = baseMapper.selectOne(new QueryWrapper<Customer>().eq("name", customerLinkManVo.getName())
                .ne("status","成交客户")
                .ne("status","失败客户")
                .eq("delete_status",0));
        Customer customerTwo = baseMapper.selectOne(new QueryWrapper<Customer>().eq("id", customerLinkManVo.getId()));

        if (!StringUtils.isEmpty(customerOne)){
            if (!customerOne.equals(customerTwo)){
                throw new LZLException(ResultCode.ERROR, "该客户名称已存在！");
            }
        }

        if (StringUtils.isEmpty(customerLinkManVo.getLinkman().getBirthday())){
            throw new LZLException(ResultCode.ERROR,"客户主要联系人生日不能为空！");
        }

        String customerId = customerLinkManVo.getId();
        //根据客户的Id得到 创建者的Id
        String createrId =baseMapper.getCreaterIdByCustomerId(customerId);
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        if (!userId.equals(createrId)){
            throw new LZLException(ResultCode.ERROR, "权限不足，只能操作属于自己创建的客户！");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerLinkManVo,customer);
        int i = baseMapper.updateById(customer);

        boolean b = linkmanService.updateById(customerLinkManVo.getLinkman());

        return i>0&&b;
    }

    /**
     * 查询我的客户------客户状态为--非成交客户
     * 成交客户、失败客户buxains
     * 根据修改时间排序
     */
    @Override
    public IPage<Customer> pageMyCustomerCondition(Page<Customer> page, Customer customer) {
        QueryWrapper<Customer> queryWrapper = queryCustomerCondition(customer);

        //查询 有客户经理的 客户
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        queryWrapper.eq("manager_id",userId);
        queryWrapper.ne("status","成交客户");
        queryWrapper.ne("status","失败客户");

        queryWrapper.eq("delete_status",0);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 判断客户条件查询条件返回
     * @param customer
     * @return
     */
    private QueryWrapper<Customer> queryCustomerCondition(Customer customer) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(customer.getName())){
            queryWrapper.eq("name", customer.getName());
        }

        if (StringUtils.hasText(customer.getStatus())){
            queryWrapper.eq("status",customer.getStatus());
        }

        if (StringUtils.hasText(customer.getType())){
            queryWrapper.eq("type",customer.getType());
        }

        if (StringUtils.hasText(customer.getSource())){
            queryWrapper.eq("source",customer.getSource());
        }

        if (StringUtils.hasText(customer.getLevel())){
            queryWrapper.eq("level",customer.getLevel());
        }

        if (StringUtils.hasText(customer.getCredit())){
            queryWrapper.eq("credit",customer.getCredit());
        }

        if (StringUtils.hasText(customer.getMaturity())){
            queryWrapper.eq("maturity",customer.getMaturity());
        }
        return queryWrapper;
    }

    /**
     * 在 我的客户 中删除客户，应该把属于的客户经理删除，然后该客户回到 客户公海池中
     */
    @Override
    public Boolean deleteMyCustomer(String customerId) {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        int i = baseMapper.updateCustomerId(customerId, "0");

        if (i>0){
            // 添加 客户转移记录
            CustomerTransfer customerTransfer = new CustomerTransfer();
            customerTransfer.setCustomerId(customerId);
            //第一次从公海中 拿客户，转移前的客户经理默认是0
            customerTransfer.setOldManagerId(userId);
            customerTransfer.setNewManagerId("0");
            customerTransfer.setReason("放弃该客户！");
            Boolean Boolean = customerTransferService.save(customerTransfer);
            if (Boolean){
                return true;
            }else {
                throw new LZLException(ResultCode.ERROR, "更新客户转移记录失败！");
            }
        }
        throw new LZLException(ResultCode.ERROR, "放弃该客户失败！");
    }

    /**
     * 查找 我的客户的详细信息
     * @param customerId
     * @return
     */
    @Override
    public MyCustomerVo getMyCustomerInfoById(String customerId) {
        Customer customer = baseMapper.selectById(customerId);

        MyCustomerVo myCustomerVo = new MyCustomerVo();
        BeanUtils.copyProperties(customer,myCustomerVo);

        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = userClient.getUsernameByUserId(customer.getManagerId());
        //设置 所属客户经理名称
        myCustomerVo.setManagerName(username);

        //设置 创建者名称
        String createrName = userClient.getUsernameByUserId(customer.getCreaterId());
        myCustomerVo.setCreaterName(createrName);

        //设置产品名称
        String productName = saleClient.getProductNameById(customer.getProductId());
        myCustomerVo.setProductName(productName);

        return myCustomerVo;
    }

    @Override
    public String getCustomerNameById(String customerId) {
        String customerName = baseMapper.getCustomerNameById(customerId);
        return customerName;
    }


    @Override
    public Boolean updateCustomerManagerId(String customerId, String newManagerId) {
        Integer update = baseMapper.updateCustomerId(customerId,newManagerId);
        if (update>0){
            return true;
        }
        return false;
    }

    /**
     * 远程调用
     * 更新客户状态---在数据库存储的是String字符串，但是中文字符串是不能在路径中传过来的
     * 只能用数值代替
     *  成交客户----0
     *  意向客户----1
     *  失败客户-----2
     *  已流失客户----3
     *  挽回客户----4
     * @return
     */
    @Override
    public Integer updateCustomerStatus(String customerId, Integer status) {
        if (status.equals(0)){
            return baseMapper.updateCustomerStatus(customerId,"成交客户");
        }else if (status.equals(1)){
            return baseMapper.updateCustomerStatus(customerId,"意向客户");
        }else if (status.equals(2)){
            return baseMapper.updateCustomerStatus(customerId,"失败客户");
        }else if (status.equals(3)){
            return baseMapper.updateCustomerStatus(customerId,"已流失客户");
        }else if (status.equals(4)){
            return baseMapper.updateCustomerStatus(customerId,"挽回客户");
        }
        throw new LZLException(ResultCode.ERROR,"更改客户状态失败！");
    }

    @Override
    public IPage<Customer> pageTdCustomerList(Page<Customer> page) {

        IPage<Customer> customerIPage = baseMapper.selectPage(page, new QueryWrapper<Customer>()
                .eq("status", "成交客户")
                .eq("delete_status",0)
                .orderByDesc("update_time"));
        return customerIPage;
    }

    /**
     * 更新客户的成熟度！！
     * @param customerId
     * @param maturity
     */
    @Override
    public void updateCustomerMaturity(String customerId, String maturity) {
        Integer  update = baseMapper.updateCustomerMaturity(customerId,maturity);
        if (update<=0){
            throw new LZLException(ResultCode.ERROR,"更改客户成熟度失败！");
        }
    }

    @Override
    public List<Customer> getEnteringCustomerLossList() {

        List<Customer> enteringCustomerLossList = baseMapper.getEnteringCustomerLossList();
        return enteringCustomerLossList;
    }

    @Override
    public void updateCustomerStatusByIds(List<String> customerIdList) {
        baseMapper.updateCustomerStatusByIds(customerIdList);
    }

    @Override
    public IPage<Customer> pageFailedCustomerList(Page<Customer> page) {
        IPage<Customer> customerIPage = baseMapper.selectPage(page, new QueryWrapper<Customer>()
                .eq("status", "失败客户")
                .eq("delete_status",0)
                .orderByDesc("update_time"));
        return customerIPage;
    }


    /**
     * 按月份统计 该月销售经理的营业额排名
     * @param report
     * @return
     */
    @Override
    public List<Map<String, Object>> countManagerCustomerRank(Report report) {
        return baseMapper.countManagerCustomerRank(report.getMonthDate());
    }

    @Override
    public List<Map<String, Object>> countCustomerSource() {
        return baseMapper.countCustomerSource();
    }

    @Override
    public List<Map<String, Object>> countCompanyPerformance(Report report) {
        return baseMapper.countCompanyPerformance(report.getYearDate());
    }

    @Override
    public List<Map<String, Object>> countCustomerType() {
        return baseMapper.countCustomerType();
    }

    /**
     * eq("delete_status",0)
     * @param customerId
     */
    @Override
    public void failedMyCustomer(String customerId) {
        baseMapper.failedMyCustomer(customerId);
    }

    /**
     * 假删除
     * @param customerId
     */
    @Override
    public void deleteCustomerById(String customerId) {
        //根据客户的Id得到 创建者的Id
        String createrId =baseMapper.getCreaterIdByCustomerId(customerId);
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        if (!userId.equals(createrId)){
            throw new LZLException(ResultCode.ERROR, "权限不足，只能操作属于自己创建的客户！");
        }

        Integer i = baseMapper.deleteCustomerById(customerId);
        if (i<=0){
            new LZLException(ResultCode.ERROR,"公海池客户删除失败！");
        }
    }


    @Override
    public void updateMyCustomerInfo(Customer customer) {
        //判断该客户名称是否存在
        Customer customerOne = baseMapper.selectOne(new QueryWrapper<Customer>().eq("name", customer.getName())
                .ne("status","成交客户")
                .ne("status","失败客户")
                .eq("delete_status",0));
        Customer customerTwo = baseMapper.selectOne(new QueryWrapper<Customer>().eq("id", customer.getId()));

        if (!StringUtils.isEmpty(customerOne)){
            if (!customerOne.equals(customerTwo)){
                throw new LZLException(ResultCode.ERROR, "该客户名称已存在！");
            }
        }

        int update = baseMapper.updateById(customer);
        if (update<=0){
            throw new LZLException(ResultCode.ERROR, "更新我的客户资料失败！");
        }

    }

    /**
     * 远程调用---统计该客户经理有多少个客户，在未完成阶段
     *      我的客户模块---里面包括完成，意向，等客户
     * @param userId
     * @return
     */
    @Override
    public Integer countMyCustomerNumber(String userId) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("manager_id",userId);
        queryWrapper.ne("status","成交客户");
        queryWrapper.ne("status","失败客户");
        queryWrapper.eq("delete_status",0);

        Integer count = baseMapper.selectCount(queryWrapper);

        return count;
    }

    @Override
    public IPage<Customer> pageMyFailedCustomerList(Page<Customer> page) {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        IPage<Customer> customerIPage = baseMapper.selectPage(page, new QueryWrapper<Customer>()
                .eq("status", "失败客户")
                .eq("delete_status",0)
                .eq("manager_id",userId)
                .orderByDesc("update_time"));
        return customerIPage;
    }

    @Override
    public IPage<Customer> pageMyTdCustomerList(Page<Customer> page) {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        IPage<Customer> customerIPage = baseMapper.selectPage(page, new QueryWrapper<Customer>()
                .eq("status", "成交客户")
                .eq("delete_status",0)
                .eq("manager_id",userId)
                .orderByDesc("update_time"));
        return customerIPage;
    }

}
