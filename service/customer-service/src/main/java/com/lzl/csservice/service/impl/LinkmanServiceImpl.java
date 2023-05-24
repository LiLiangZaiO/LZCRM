package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.csservice.entity.Linkman;
import com.lzl.csservice.mapper.LinkmanMapper;
import com.lzl.csservice.service.LinkmanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-26
 */
@Service
public class LinkmanServiceImpl extends ServiceImpl<LinkmanMapper, Linkman> implements LinkmanService {

    /**
     *  若添加的是 主要联系人，则需要把其他全部主要联系人设置为 普通联系人
     *      即 主要联系人只有一个
     * @param linkman
     * @return
     */
    @Override
    public Boolean addLinkman(Linkman linkman) {

        //嵌套校验失败----只能手动校验
        CheckLinkman(linkman);

        if (linkman.getLevel().equals("0".toString())){
            Integer count = baseMapper.selectCount(new QueryWrapper<Linkman>().eq("customer_id", linkman.getCustomerId()).eq("level", linkman.getLevel()));
            if (count>0){
               Integer update = baseMapper.updateLinkmanLevel(linkman.getCustomerId());
               if (update.equals(0)){
                   throw new LZLException(ResultCode.ERROR, "更新客户联系人的等级失败！");
               }
            }
        }
        int insert = baseMapper.insert(linkman);
        return insert>0;
    }

    @Override
    public Boolean updateLinkman(Linkman linkman) {
        //嵌套校验失败----只能手动校验
        CheckLinkman(linkman);

        if (linkman.getLevel().equals("0".toString())){
            Integer count = baseMapper.selectCount(new QueryWrapper<Linkman>().eq("customer_id", linkman.getCustomerId()).eq("level", linkman.getLevel()));
            if (count>0){
                Integer update = baseMapper.updateLinkmanLevel(linkman.getCustomerId());
                if (update.equals(0)){
                    throw new LZLException(ResultCode.ERROR, "更新客户联系人的等级失败！");
                }
            }
        }
        int i = baseMapper.updateById(linkman);
        return i>0;
    }

    private void CheckLinkman(Linkman linkman) {
        if (!StringUtils.hasText(linkman.getName())){
            throw new LZLException(ResultCode.ERROR, "客户联系人名称不能为空！");
        }
        if (!StringUtils.hasText(linkman.getPosition())){
            throw new LZLException(ResultCode.ERROR, "联系人职位不能为空！");
        }
        if (!StringUtils.hasText(linkman.getOfficePhone())){
            throw new LZLException(ResultCode.ERROR, "客户联系人办公司电话不能为空！");
        }
        if (!StringUtils.hasText(linkman.getMobilePhone())){
            throw new LZLException(ResultCode.ERROR, "客户联系人手机电话不能为空！");
        }
        if (StringUtils.isEmpty(linkman.getBirthday())){
            throw new LZLException(ResultCode.ERROR, "客户联系人生日不能为空！");
        }
        if (!StringUtils.hasText(linkman.getSex())){
            throw new LZLException(ResultCode.ERROR, "客户联系人性别不能为空！");
        }
        if (!StringUtils.hasText(linkman.getLevel())){
            throw new LZLException(ResultCode.ERROR, "客户联系级别不能为空！");
        }
        if (!StringUtils.hasText(linkman.getRemark())){
            throw new LZLException(ResultCode.ERROR, "客户联系人备注不能为空！");
        }
    }
}
