package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.et.be.online.constant.OptConstant;
import com.et.be.online.domain.dto.CategoryDTO;
import com.et.be.online.domain.dto.DiscountDTO;
import com.et.be.online.domain.dto.PageDTO;
import com.et.be.online.domain.dto.ProductManagementDTO;
import com.et.be.online.domain.mo.*;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.ProductManagementListVO;
import com.et.be.online.domain.vo.ProductVO;
import com.et.be.online.domain.vo.SingleProductVO;
import com.et.be.online.mapper.*;
import com.et.be.online.service.SceneService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SceneServiceImpl implements SceneService {

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private DiscountMapper discountMapper;

    /**
     * 查询商品
     * @param sceneType
     * @return
     */
    @Override
    public List<ProductVO> getProductList(String sceneType) {
        List<ProductRO> productList = sceneMapper.getProducts(sceneType);
        List<ProductVO> productVOList = new ArrayList<>();

        ProductVO productVO = null;
        for (ProductRO productRO : productList) {
             productVO= new ProductVO();
            BeanUtil.copyProperties(productRO,productVO);
            productVOList.add(productVO);
        }

        return productVOList;
    }

    /**
     * 产品明细
     * @param productcode
     * @return
     */
    @Override
    public SingleProductVO getSingleProduct(String productcode) {

        // 产品基本信息
        ProductRO singleProduct = sceneMapper.getSingleProduct(productcode);

        // 产品规格
        QueryWrapper<Specification> wrapper = Wrappers.query();
        wrapper.eq("product_code",productcode);
        Specification specification = specificationMapper.selectOne(wrapper);

        SingleProductVO singleProductVO = new SingleProductVO();
        singleProductVO.setProduct(singleProduct);
        singleProductVO.setSpecification(specification);


        return singleProductVO;
    }

    /**
     * 产品定义
     * @param productManagementDTO
     */
    @Override
    public void createProduct(ProductManagementDTO productManagementDTO) {

        // 商品库存
        ProductInventory productInventory = new ProductInventory();
        productInventory.setQuantity(Integer.valueOf(productManagementDTO.getProductInventory()))
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());

       productInventoryMapper.insert(productInventory);

        // 商品
        Long discount = StringUtils.isEmpty(productManagementDTO.getProductDiscount()) ? null : Long.valueOf(productManagementDTO.getProductDiscount());
        Product product = new Product();
        product.setCategoryId(Long.valueOf(productManagementDTO.getProductCategory()))
                .setProductCode(productManagementDTO.getProductCode())
                .setProductName(productManagementDTO.getProductName())
                .setProductDesc(productManagementDTO.getProductDesc())
                .setSku(productManagementDTO.getSKU())
                .setPrice(Double.valueOf(productManagementDTO.getProductPrice()))
                .setInventoryId(productInventory.getId())
                .setDiscountId(discount)
                .setFeatureImage(productManagementDTO.getFeatureImage())
                .setProductUrl(productManagementDTO.getProductUrl())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());

        productMapper.insert(product);

    }

    /**
     * 产品列表查询
     * @param pageDTO
     * @return
     */
    @Override
    public Page<ProductManagementListVO> getProductManagementList(PageDTO pageDTO ) {
        Page<ProductManagementListVO> page = new Page<>();
        page.setCurrent(pageDTO.getCurrent());
        page.setSize(pageDTO.getSize());
         return sceneMapper.getProductManagementList(page,pageDTO.getQueryParams());
    }

    /**
     * 产品目录列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public IPage<ProductCategory> getproductCategoryList(Long current, Long size) {
        Page<ProductCategory> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return productCategoryMapper.selectPage(page,Wrappers.query());
    }

    /**
     *产品分类定义
     * @param categoryDTO
     */
    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName(categoryDTO.getCategoryName())
                .setCategoryDesc(categoryDTO.getCategoryDesc())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());

        productCategoryMapper.insert(productCategory);

    }

    /**
     * 优惠定义
     * @param discountDTO
     */
    @Override
    public void createDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setDiscountName(discountDTO.getDiscountName())
                .setDiscountDesc(discountDTO.getDiscountDesc())
                .setDiscountPercent(discountDTO.getDiscountPercent())
                .setActive(discountDTO.getActive())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
       discountMapper.insert(discount);
    }

    /**
     * 优惠配置列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public IPage<Discount> getDiscountList(Long current, Long size) {
        Page<Discount> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);

        return discountMapper.selectPage(page,Wrappers.query());
    }

    /**
     * 分类下拉
     * @return
     */
    @Override
    public List<ProductCategory> getCategory() {
        return productCategoryMapper.selectList(Wrappers.query());
    }

    /**
     * 优惠下拉
     * @return
     */
    @Override
    public List<Discount> getDiscount() {
        return discountMapper.selectList(Wrappers.query());
    }

    @Autowired
    private HistoryProductMapper historyProductMapper;

    /**
     * 删除产品
     * @param productCode
     */
    @Override
    public void deleteProduct(String productCode) {
        QueryWrapper<Product> productQueryWrapper  = Wrappers.query();
        productQueryWrapper.eq("product_code",productCode);
        Product product = productMapper.selectOne(productQueryWrapper);

        HistoryProduct historyProduct = new HistoryProduct();

        BeanUtil.copyProperties(product,historyProduct);
        historyProduct.setDeletedAt(new Date());

        // 备份产品数据
        historyProductMapper.insert(historyProduct);

        // 删除库存
        QueryWrapper<ProductInventory> inventoryQueryWrapper = Wrappers.query();
          inventoryQueryWrapper.eq("id",product.getInventoryId());
          productInventoryMapper.delete(inventoryQueryWrapper);
        // 删除产品
         productMapper.delete(productQueryWrapper);
    }

    @Override
    public void modifyProduct(ProductManagementDTO productManagementDTO) {
        QueryWrapper<Product> productQueryWrapper  = Wrappers.query();
        productQueryWrapper.eq("product_code",productManagementDTO.getProductCode());
        Product product = productMapper.selectOne(productQueryWrapper);

        // 商品库存更新
        ProductInventory productInventory = new ProductInventory();
        productInventory.setId(product.getInventoryId())
                .setQuantity(Integer.valueOf(productManagementDTO.getProductInventory()))
                .setModifiedAt(new Date());
        productInventoryMapper.updateById(productInventory);


        // 商品
        Long discount = StringUtils.isEmpty(productManagementDTO.getProductDiscount()) ? null : Long.valueOf(productManagementDTO.getProductDiscount());
        product.setCategoryId(Long.valueOf(productManagementDTO.getProductCategory()))
                .setProductCode(productManagementDTO.getProductCode())
                .setProductName(productManagementDTO.getProductName())
                .setProductDesc(productManagementDTO.getProductDesc())
                .setSku(productManagementDTO.getSKU())
                .setPrice(Double.valueOf(productManagementDTO.getProductPrice()))
                .setInventoryId(productInventory.getId())
                .setFeatureImage(productManagementDTO.getFeatureImage())
                .setProductUrl(productManagementDTO.getProductUrl())
                .setDiscountId(discount)
                .setModifiedAt(new Date());
        productMapper.updateById(product);
    }

    /**
     * 判断数据库中相应产品代码是否已经录入
     * @param productCode
     * @return true 已录入 false 未录入
     */
    @Override
    public Boolean checkProductCode(String productCode) {
        QueryWrapper<Product> wrapper = Wrappers.query();
        wrapper.eq("product_code",productCode);

        return productMapper.selectCount(wrapper)>0;
    }

    @Override
    public IPage<ProductRO> onlineProductList(PageDTO pageDTO) {
        Page<ProductVO> page = new Page<>();
        page.setSize(pageDTO.getSize());
        page.setCurrent(pageDTO.getCurrent());
        JsonObject queryParams = JsonParser.parseString(pageDTO.getQueryParams()).getAsJsonObject();
        String orderBy = queryParams.get("orderBy").getAsString();
        String category = queryParams.get("category").getAsString();
        // Newest to oldes
        if(OptConstant.NEWEST_TO_OLDEST.equals(orderBy)){
            page.setAsc("p.modified_at");
        }
        // Oldest to newest
        if(OptConstant.OLDEST_TO_NEWEST.equals(pageDTO.getQueryParams())){
           page.setDesc("p.modified_at");
        }


        return  sceneMapper.getOnlineProductList(page,category);

    }

    @Override
    public List<ProductCategory> shopCategories() {
        return productCategoryMapper.selectList(Wrappers.query());
    }


}
