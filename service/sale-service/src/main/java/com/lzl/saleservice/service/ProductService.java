package com.lzl.saleservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.saleservice.entity.Category;
import com.lzl.saleservice.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.saleservice.entity.subject.OneSubject;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
public interface ProductService extends IService<Product> {

    List<OneSubject> getAllOneTwoSubject();

    String getProductNameById(String productId);

    IPage<Category> pageCategoryList(Page<Category> categoryPage);

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(String categoryId);

    IPage<Product> pageProductCondition(Page<Product> page, Product product);

    List<Category> getAllCategoryList();

    void addProductInfo(Product product);

    void updateProductInfo(Product product);

    boolean deleteProductById(String productId);

    Double getProductPriceById(String productId);

    void updateProductRepertory(String productId, Integer num);
}
