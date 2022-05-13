package com.et.be.online.controller;

import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.constant.SceneConstant;
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
    public ResponseVO singleProduct( @RequestBody String productCode) {
        return new ResponseVO(sceneService.getSingleProduct(productCode));
    }


    @ApiOperation("商品明细")
    @ResponseBody
    @PostMapping(value = "createProduct")
    public ResponseVO createProduct(@Validated ProductManagementDTO productManagementDTO) {
        sceneService.createProduct(productManagementDTO);
        return new ResponseVO("");
    }





}
