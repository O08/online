package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.online.domain.dto.ProductManagementDTO;
import com.et.be.online.domain.mo.Product;
import com.et.be.online.domain.mo.ProductInventory;
import com.et.be.online.domain.mo.Specification;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.ProductVO;
import com.et.be.online.domain.vo.SingleProductVO;
import com.et.be.online.mapper.ProductInventoryMapper;
import com.et.be.online.mapper.ProductMapper;
import com.et.be.online.mapper.SceneMapper;
import com.et.be.online.mapper.SpecificationMapper;
import com.et.be.online.service.SceneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * 查询商品
     * @param sceneType
     * @return
     */
    @Override
    public List<ProductVO> getProductList(String sceneType) {
        List<ProductRO> productro = sceneMapper.getProducts(sceneType);
        List<ProductVO> productVOList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(productro)) {
            BeanUtil.copyProperties(productro, productVOList);
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
                .setInventoryId(Long.valueOf(productInventory.getId()))
                .setDiscountId(discount)
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());

        productMapper.insert(product);

    }




}
