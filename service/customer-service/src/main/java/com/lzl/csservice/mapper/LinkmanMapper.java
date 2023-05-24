package com.lzl.csservice.mapper;

import com.lzl.csservice.entity.Linkman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-26
 */
public interface LinkmanMapper extends BaseMapper<Linkman> {

    Integer updateLinkmanLevel(String customerId);
}
