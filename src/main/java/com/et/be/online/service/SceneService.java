package com.et.be.online.service;

import com.et.be.online.domain.dto.ProductManagementDTO;
import com.et.be.online.domain.vo.ProductVO;
import com.et.be.online.domain.vo.SingleProductVO;

import java.util.List;

public interface SceneService {
    List<ProductVO> getProductList(String sceneType);

    SingleProductVO getSingleProduct(String productcode);

    void createProduct(ProductManagementDTO productManagementDTO);

}
