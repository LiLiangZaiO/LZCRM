package com.lzl.saleservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.R;
import com.lzl.saleservice.entity.Category;
import com.lzl.saleservice.entity.Product;
import com.lzl.saleservice.entity.subject.OneSubject;
import com.lzl.saleservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @GetMapping("/getProductInfo/{productId}")
    public R getProductInfo(@PathVariable String productId){
        Product product = productService.getById(productId);
        return R.ok().data("productInfo",product);
    }


    /**
     * 在订单详细信息查询  产品的单价
     * @param productId
     * @return
     */
    @GetMapping("/getProductPriceById/{productId}")
    public R getProductPriceById(@PathVariable String productId){
        Double price = productService.getProductPriceById(productId);
        return R.ok().data("productPrice",price);
    }



    /**
     * 删除产品------不能简单删除！！！！！若删除数据库的记录之后，在订单查询详情时无该产品信息
     * @param productId
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @GetMapping("/deleteProductById/{productId}")
    public R deleteProductById(@PathVariable String productId){

        boolean b = productService.deleteProductById(productId);
        if (b){
            return R.ok();
        }else {
            return R.error().message("删除产品失败!");
        }
    }


    /**
     * 发送ajax判断 当前产品名称是否存在
     * @param productName
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @GetMapping("/checkProductNameExist/{productName}")
    public R checkProductNameExist(@PathVariable String productName){
        int i = productService.count(new QueryWrapper<Product>().eq("name", productName).eq("delete_status",0));
        return R.ok().data("count",i);
    }

    /**
     * 更新产品
     * @param product
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @PostMapping("/updateProductInfo")
    public R updateProductInfo(@Validated @RequestBody Product product){
        productService.updateProductInfo(product);
        return R.ok();
    }


    /**
     * 添加产品
     * @param product
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @PostMapping("/addProductInfo")
    public R addProductInfo(@Validated @RequestBody Product product){
        productService.addProductInfo(product);
        return R.ok();
    }


    /**
     * 用于---在产品 中 可以按照 分类来 条件查询
     * @return
     */
    @GetMapping("/getAllCategoryList")
    public R getAllCategoryList(){
        List<Category> categoryList = productService.getAllCategoryList();
        return R.ok().data("categoryList",categoryList);
    }



    /**
     * 分页条件查询 产品
     *      根据 产品的分类 来查询
     * @param current
     * @param limit
     * @param product
     * @return
     */
    @PostMapping("/pageProductCondition/{current}/{limit}")
    public R pageProductCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) Product product){
        Page<Product> page = new Page<>(current,limit);
        IPage<Product> productPage = productService.pageProductCondition(page,product);

        return R.ok().data("productPage",productPage);
    }

    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @GetMapping("/deleteCategory/{categoryId}")
    public R deleteCategory(@PathVariable String categoryId){
        productService.deleteCategory(categoryId);
        return R.ok();
    }


    /**
     * 更新分类
     * @param category
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @PostMapping("/updateCategory")
    public R updateCategory(@RequestBody Category category){
        productService.updateCategory(category);
        return R.ok();
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @PostMapping("/addCategory")
    public R addCategory(@Validated @RequestBody Category category){
        productService.addCategory(category);
        return R.ok();
    }


    /**
     * 分页获取 分类列表
     * @param current
     * @param limit
     * @return
     */
    @Secured({"ROLE_PRODUCT","ROLE_ADMIN"})
    @GetMapping("/pageCategoryList/{current}/{limit}")
    public R pageCategoryList(@PathVariable long current,
                              @PathVariable long limit){
        Page<Category> categoryPage = new Page<>(current, limit);
        IPage<Category> categoryList = productService.pageCategoryList(categoryPage);
        return R.ok().data("categoryPage",categoryList);
    }




    /**
     * 远程调用--根据Id 得到 产品的名称
     * @param productId
     * @return
     */
    @GetMapping("/getProductNameById/{productId}")
    public String getProductNameById(@PathVariable String productId){
        String productName = productService.getProductNameById(productId);
        return productName;
    }

    /**
     *  意向产品--根据一级产品类型菜单 得到 产品二级菜单
     * @return
     */
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        List<OneSubject> subjectList = productService.getAllOneTwoSubject();
        return R.ok().data("subjectList",subjectList);
    }


    /**
     * 根据分类id 得到 产品列表
     * @param categoryId
     * @return
     */
    @GetMapping("/getProductListByCategoryId/{categoryId}")
    public R getProductListByCategoryId(@PathVariable String categoryId){

        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("category_id", categoryId));

        return R.ok().data("productList",productList);

    }

}

