package com.et.be.online.mapper;

import com.et.be.online.domain.mo.CartItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.et.be.online.domain.vo.CartItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (cart_item)数据Mapper
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
   @Select("select sum(total) subtotal from cart_item where session_id = #{sessionId}")
    Double selectCartItemTotalBySessionId(Long sessionId);

    List<CartItemVO> cart(@Param("session_id") Long sessionId);



}
