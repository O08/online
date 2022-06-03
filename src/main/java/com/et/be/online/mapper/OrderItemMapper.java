package com.et.be.online.mapper;

import com.et.be.online.domain.mo.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.et.be.online.domain.vo.OrderHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (order_item)数据Mapper
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderHistoryVO> orderHistory(@Param("user_id") Long userId);
}
