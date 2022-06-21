package com.et.be.online.controller;

import com.et.be.base.vo.ResponseVO;
import com.et.be.online.constant.SceneConstant;
import com.et.be.online.domain.dto.PageDTO;
import com.et.be.online.service.SceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @ApiOperation("商品分类列表")
    @ResponseBody
    @PostMapping(value = "productCategoryList")
    public ResponseVO productCategoryList(@RequestBody PageDTO pageDTO) {
        return new ResponseVO(sceneService.getproductCategoryList(pageDTO.getCurrent(), pageDTO.getSize()));
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

    @ApiOperation("商品分类")
    @ResponseBody
    @PostMapping(value = "shopCategories")
    public ResponseVO shopCategories() {
        return new ResponseVO(sceneService.shopCategories());
    }



}
