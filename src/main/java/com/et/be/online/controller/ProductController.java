package com.et.be.online.controller;

import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.constant.SceneConstant;
import com.et.be.online.domain.dto.CategoryDTO;
import com.et.be.online.domain.dto.DiscountDTO;
import com.et.be.online.domain.dto.PageDTO;
import com.et.be.online.domain.dto.ProductManagementDTO;
import com.et.be.online.service.SceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "电商-产品", tags = "电商")
@RequestMapping("api/v1/online")
@RestController
@Slf4j
public class ProductController {

    @Autowired
    private SceneService sceneService;

    @ApiOperation("特色产品")
    @ResponseBody
    @PostMapping(value = "featuredProducts")
    public ResponseVO featuredProducts() {
        return new ResponseVO(sceneService.getProductList(SceneConstant.SCENE_TYPE_FEATURE));
    }

    @ApiOperation("商品推荐")
    @ResponseBody
    @PostMapping(value = "recommendProducts")
    public ResponseVO recommendProducts() {
        return new ResponseVO(sceneService.getProductList(SceneConstant.SCENE_TYPE_RECOMMEND));
    }

    @ApiOperation("商品明细")
    @ResponseBody
    @PostMapping(value = "singleProduct")
    public ResponseVO singleProduct( String productCode) {
        return new ResponseVO(sceneService.getSingleProduct(productCode));
    }


    @ApiOperation("商品定义")
    @ResponseBody
    @PostMapping(value = "createProduct")
    public ResponseVO createProduct(@Validated ProductManagementDTO productManagementDTO) {
        sceneService.createProduct(productManagementDTO);
        return new ResponseVO("");
    }

    @ApiOperation("商品管理列表")
    @ResponseBody
    @PostMapping(value = "productManagementList")
    public ResponseVO productManagementList(@RequestBody PageDTO pageDTO) {
        return new ResponseVO(sceneService.getProductManagementList(pageDTO));
    }

    @ApiOperation("商品分类列表")
    @ResponseBody
    @PostMapping(value = "productCategoryList")
    public ResponseVO productCategoryList(@RequestBody PageDTO pageDTO) {
        return new ResponseVO(sceneService.getproductCategoryList(pageDTO.getCurrent(), pageDTO.getSize()));
    }

    @ApiOperation("商品分类定义")
    @ResponseBody
    @PostMapping(value = "createCategory")
    public ResponseVO createCategory(@Validated CategoryDTO categoryDTO) {
        sceneService.createCategory(categoryDTO);
        return new ResponseVO("");
    }

    @ApiOperation("商品优惠定义")
    @ResponseBody
    @PostMapping(value = "createDiscount")
    public ResponseVO createDiscount(@Validated DiscountDTO discountDTO) {
        sceneService.createDiscount(discountDTO);
        return new ResponseVO("");
    }

    @ApiOperation("商品分类列表")
    @ResponseBody
    @PostMapping(value = "discountList")
    public ResponseVO discountList(@RequestBody PageDTO pageDTO) {
        return new ResponseVO(sceneService.getDiscountList(pageDTO.getCurrent(), pageDTO.getSize()));
    }

    @ApiOperation("商品分类下拉")
    @ResponseBody
    @PostMapping(value = "category")
    public ResponseVO category() {
        return new ResponseVO(sceneService.getCategory());
    }

    @ApiOperation("商品优惠下拉")
    @ResponseBody
    @PostMapping(value = "discount")
    public ResponseVO discount() {
        return new ResponseVO(sceneService.getDiscount());
    }


    @ApiOperation("删除商品")
    @ResponseBody
    @PostMapping(value = "deleteProduct")
    public ResponseVO deleteProduct(String productCode) {
        sceneService.deleteProduct(productCode);
        return new ResponseVO("success");
    }

    @ApiOperation("商品配置调整")
    @ResponseBody
    @PostMapping(value = "modifyProduct")
    public ResponseVO modifyProduct(@Validated ProductManagementDTO productManagementDTO) {
        sceneService.modifyProduct(productManagementDTO);
        return new ResponseVO("");
    }




    @ApiOperation("商品编码是否已使用")
    @ResponseBody
    @PostMapping(value = "checkProductCode")
    public ResponseVO checkProductCode( String productCode) {
        return new ResponseVO(sceneService.checkProductCode(productCode));
    }


    @ApiOperation("在线销售商品列表")
    @ResponseBody
    @PostMapping(value = "onlineProductList")
    public ResponseVO onlineProductList(@RequestBody PageDTO pageDTO) {
        return new ResponseVO(sceneService.onlineProductList(pageDTO));
    }



}
