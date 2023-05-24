package com.lzl.saleservice.service.impl;

import com.lzl.saleservice.entity.Category;
import com.lzl.saleservice.mapper.CategoryMapper;
import com.lzl.saleservice.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
