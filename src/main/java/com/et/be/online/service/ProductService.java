package com.et.be.online.service;

import com.et.be.online.domain.ro.ProductRO;

import java.util.List;

public interface ProductService {
    void updateProductImage(String productCode,String images);

    /**
     *  keyword search
     * @param keyword
     * @return product list
     */
    List<ProductRO> searchProduct(String keyword);
}
