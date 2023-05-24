package com.lzl.saleservice.mapper;

import com.lzl.saleservice.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
public interface ProductMapper extends BaseMapper<Product> {

    String getProductNameById(String productId);

    boolean deleteProductById(String productId);

    Integer updateProductRepertory(String productId, Integer num);
}
