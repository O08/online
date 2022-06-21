package com.et.be.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.et.be.online.domain.mo.Scene;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.ProductManagementListVO;
import com.et.be.online.domain.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (scene)数据Mapper
 *
 * @author kk
 * @since 2022-05-10 10:36:42
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface SceneMapper extends BaseMapper<Scene> {
  /**
   *  查询商品
   * @param sceneType
   * @return
   */
  List<ProductRO> getProducts(@Param("scene_type") String sceneType);

  ProductRO getSingleProduct(@Param("product_code") String productCode);

  Page<ProductManagementListVO> getProductManagementList(Page page,@Param("productName") String productName);


  IPage<ProductRO> getOnlineProductList(Page<ProductVO> page,@Param("categoryId") String categoryId);
}
