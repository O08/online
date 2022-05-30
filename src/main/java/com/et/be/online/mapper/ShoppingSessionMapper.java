package com.et.be.online.mapper;

import com.et.be.online.domain.mo.ShoppingSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * (shopping_session)数据Mapper
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface ShoppingSessionMapper extends BaseMapper<ShoppingSession> {
   @Update("update shopping_session set total = #{subtotal} where id= #{sessionId}")
    Integer updateShoppingSessionTotalById(@Param("sessionId") Long sessionId,@Param("subtotal") Double subTotal);

    /**
     *  query sessionId by user email
     * @param email
     * @return
     */
   @Select("select s.* from shopping_session s ,customer c where s.user_id = c.id and c.email= #{email}")
    ShoppingSession queryShoppingSession(@Param("email") String email);
}
