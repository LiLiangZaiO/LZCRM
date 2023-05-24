package com.lzl.saleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.saleservice.entity.Category;
import com.lzl.saleservice.entity.Product;
import com.lzl.saleservice.entity.subject.OneSubject;
import com.lzl.saleservice.entity.subject.TwoSubject;
import com.lzl.saleservice.mapper.ProductMapper;
import com.lzl.saleservice.service.CategoryService;
import com.lzl.saleservice.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //得到类型列表
        List<Category> categoryList = categoryService.list();
        //最终返回封装数据
        ArrayList<OneSubject> finalSubjectList = new ArrayList<>();

        for (Category category : categoryList) {
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(category,oneSubject);
            //根据类型id 查询对应
            List<Product> productList = baseMapper.selectList(new QueryWrapper<Product>().eq("category_id", category.getId()).eq("delete_status",0));

            ArrayList<TwoSubject> twoSubjectList = new ArrayList<>();
            for (Product product : productList) {
                TwoSubject twoSubject = new TwoSubject();
                BeanUtils.copyProperties(product,twoSubject);
                twoSubjectList.add(twoSubject);
            }
            //把 二级菜单放到 oneSubject种
            oneSubject.setChildren(twoSubjectList);
            finalSubjectList.add(oneSubject);
        }
        return finalSubjectList;
    }

    @Override
    public String getProductNameById(String productId) {
        String productName = baseMapper.getProductNameById(productId);
        return productName;
    }

    /**
     * 分页获取 分页列表
     * @param categoryPage
     * @return
     */
    @Override
    public IPage<Category> pageCategoryList(Page<Category> categoryPage) {
        IPage<Category> categoryIPage = categoryService.page(categoryPage);
        return categoryIPage;
    }

    @Override
    public void addCategory(Category category) {
        //判断分类名称是否已存在
        int count = categoryService.count(new QueryWrapper<Category>().eq("name", category.getName()));
        if (count>0){
            throw new LZLException(ResultCode.ERROR, "该分类名称已存在！");
        }
        categoryService.save(category);

    }

    @Override
    public void updateCategory(Category category) {
        //判断分类名称是否已存在
        int count = categoryService.count(new QueryWrapper<Category>().eq("name", category.getName()));
        if (count>0){
            throw new LZLException(ResultCode.ERROR, "该分类名称已存在！");
        }
        categoryService.updateById(category);

    }

    /**
     * 删除分类--需要查询该分类下 是否存在 产品
     *      存在--即不能简单删除
     * @param categoryId
     */
    @Override
    public void deleteCategory(String categoryId) {
        List<Product> productList = baseMapper.selectList(new QueryWrapper<Product>().eq("category_id", categoryId));
        if (!productList.isEmpty()){
            throw new LZLException(ResultCode.ERROR, "该分类下存在产品，不能简单删除！");
        }
       categoryService.removeById(categoryId);
    }

    /**
     * 分页条件查询 产品
     *      根据 产品的分类 来查询
     */
    @Override
    public IPage<Product> pageProductCondition(Page<Product> page, Product product) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(product.getCategoryId())){
            queryWrapper.eq("category_id",product.getCategoryId());
        }
        queryWrapper.eq("delete_status",0);
        IPage<Product> productIPage = baseMapper.selectPage(page, queryWrapper);

        return productIPage;
    }

    @Override
    public List<Category> getAllCategoryList() {
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().select("id","name"));
        return categoryList;
    }


    @Override
    public void addProductInfo(Product product) {
        int insert = baseMapper.insert(product);
        if (insert<=0){
            throw new LZLException(ResultCode.ERROR, "添加产品失败！");
        }
    }

    @Override
    public void updateProductInfo(Product product) {

        //判断该产品名称是否存在
        Product productOne = baseMapper.selectOne(new QueryWrapper<Product>().eq("name", product.getName()).eq("delete_status",0));
        Product productTwo = baseMapper.selectOne(new QueryWrapper<Product>().eq("id", product.getId()));

        if (!StringUtils.isEmpty(productOne)){
            if (!productOne.equals(productTwo)){
                throw new LZLException(ResultCode.ERROR, "该分类名称已存在！");
            }
        }


        int update = baseMapper.updateById(product);
        if (update<=0){
            throw new LZLException(ResultCode.ERROR, "更新产品信息失败！");
        }
    }

    @Override
    public boolean deleteProductById(String productId) {
        return  baseMapper.deleteProductById(productId);
    }

    @Override
    public Double getProductPriceById(String productId) {
        Product product = baseMapper.selectOne(new QueryWrapper<Product>().select("id", "price").eq("id", productId));
        return product.getPrice();
    }

    //更新库存
    @Override
    public void updateProductRepertory(String productId, Integer num) {

        Integer count = baseMapper.updateProductRepertory(productId,num);
        if (count<=0){
            throw new LZLException(ResultCode.ERROR,"更新产品库存失败！");
        }

    }
}
