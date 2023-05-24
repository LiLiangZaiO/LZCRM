package com.lzl.csservice.mapper;

import com.lzl.csservice.entity.FollowUp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
public interface FollowUpMapper extends BaseMapper<FollowUp> {

    Boolean deleteFollowUp(String followUpId);
}
