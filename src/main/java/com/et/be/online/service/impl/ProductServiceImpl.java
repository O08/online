package com.et.be.online.service.impl;

import com.et.be.online.mapper.ProductMapper;
import com.et.be.online.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public void updateProductImage(String productCode, String images) {
        productMapper.updateProductImage(productCode,images);
    }
}
