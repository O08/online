package com.et.be.online.controller;

import cn.hutool.core.util.CharsetUtil;
import com.et.be.base.vo.ResponseVO;
import com.et.be.online.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Api(value = "电商-产品", tags = "电商")
@RequestMapping("api/v1/online")
@RestController
@Slf4j
public class SearchController {

    @Autowired
    private ProductService productService;

    @ApiOperation("商品搜索")
    @ResponseBody
    @PostMapping(value = "search")
    public ResponseVO searchProduct(String keyword) throws UnsupportedEncodingException {
        keyword= URLDecoder.decode(keyword, CharsetUtil.UTF_8);
        return new ResponseVO(productService.searchProduct(keyword));
    }
}
