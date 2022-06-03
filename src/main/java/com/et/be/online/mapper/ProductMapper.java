package com.et.be.online.mapper;

import com.et.be.online.domain.mo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.et.be.online.domain.ro.ProductRO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * (product)数据Mapper
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Update("update product set images = #{images} where product_code = #{productCode}")
    void updateProductImage(@Param("productCode") String productCode, @Param("images")String images);

    /**
     *  通过关键词匹配产品名称或者分类，返回命中产品清单
     * @param keyword
     * @return
     */
    List<ProductRO> searchProduct(@Param("keyword") String keyword);


}
