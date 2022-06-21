package com.et.be.online.controller;

import com.et.be.base.vo.ResponseVO;
import com.et.be.online.domain.dto.CartItemDTO;
import com.et.be.online.service.CartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "电商-购物车", tags = "电商")
@RequestMapping("api/v1/shop")
@RestController
@Slf4j
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @ApiOperation("添加商品到购物车")
    @ResponseBody
    @PostMapping(value = "addCartItem")
    public ResponseVO addCartItem(@Validated CartItemDTO cartItemDTO) {
         cartItemService.addCartItems(cartItemDTO);
        return new ResponseVO("success");
    }

    @ApiOperation("购物车")
    @ResponseBody
    @PostMapping(value = "cart")
    public ResponseVO cart() {
        return new ResponseVO(cartItemService.cart());
    }

    @ApiOperation("购物车删除商品")
    @ResponseBody
    @PostMapping(value = "delCartItem")
    public ResponseVO delCartItem( String productCode) {
        cartItemService.delCartItem(productCode);
        return new ResponseVO("success");
    }

    @ApiOperation("购物车删除商品")
    @ResponseBody
    @PostMapping(value = "updateCartItem")
    public ResponseVO updateCartItem(@Validated CartItemDTO cartItemDTO) {
        cartItemService.updateCartItem(cartItemDTO);
        return new ResponseVO("success");
    }


}
