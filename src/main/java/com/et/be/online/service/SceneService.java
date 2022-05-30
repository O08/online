package com.et.be.online.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.et.be.online.domain.dto.CategoryDTO;
import com.et.be.online.domain.dto.DiscountDTO;
import com.et.be.online.domain.dto.PageDTO;
import com.et.be.online.domain.dto.ProductManagementDTO;
import com.et.be.online.domain.mo.Discount;
import com.et.be.online.domain.mo.ProductCategory;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.ProductManagementListVO;
import com.et.be.online.domain.vo.ProductVO;
import com.et.be.online.domain.vo.SingleProductVO;

import java.util.List;

public interface SceneService {
    List<ProductVO> getProductList(String sceneType);

    SingleProductVO getSingleProduct(String productcode);

    void createProduct(ProductManagementDTO productManagementDTO);
    Page<ProductManagementListVO> getProductManagementList(PageDTO pageDTO);

    IPage<ProductCategory> getproductCategoryList(Long current, Long size);

    void createCategory(CategoryDTO categoryDTO);

    void createDiscount(DiscountDTO discountDTO);

    IPage<Discount> getDiscountList(Long current, Long size);

    List<ProductCategory> getCategory();

    List<Discount> getDiscount();

    void deleteProduct(String productCode);

    void modifyProduct(ProductManagementDTO productManagementDTO);

    Boolean checkProductCode(String productCode);

    IPage<ProductRO> onlineProductList(PageDTO pageDTO);
}
