package com.lzl.csservice.service;

import com.lzl.csservice.entity.Linkman;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-26
 */
public interface LinkmanService extends IService<Linkman> {

    Boolean addLinkman(Linkman linkman);

    Boolean updateLinkman(Linkman linkman);
}
