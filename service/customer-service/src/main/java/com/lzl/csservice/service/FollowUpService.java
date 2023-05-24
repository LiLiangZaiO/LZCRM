package com.lzl.csservice.service;

import com.lzl.csservice.entity.FollowUp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.csservice.entity.FollowUpVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
public interface FollowUpService extends IService<FollowUp> {

    List<FollowUpVo> getAllFollowUpList(String customerId);

    Boolean addFollowUpInfo(FollowUp followUp);

    Boolean deleteFollowUpInfo(String followUpId);

    boolean updateFollowUpInfo(FollowUp followUp);
}
