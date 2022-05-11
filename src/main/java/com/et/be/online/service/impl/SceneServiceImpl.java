package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.online.domain.mo.Specification;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.ProductVO;
import com.et.be.online.domain.vo.SingleProductVO;
import com.et.be.online.mapper.SceneMapper;
import com.et.be.online.mapper.SpecificationMapper;
import com.et.be.online.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SceneServiceImpl implements SceneService {

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

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


}
