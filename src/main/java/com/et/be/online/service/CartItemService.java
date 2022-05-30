package com.et.be.online.service;

import com.et.be.online.domain.dto.CartItemDTO;
import com.et.be.online.domain.mo.CartItem;
import com.et.be.online.domain.vo.CartItemVO;

import java.util.List;

public interface CartItemService {
    void addCartItems(CartItemDTO cartItemDTO);

    /**
     *
     * @return 购物车列表
     */
    List<CartItemVO> cart();

    int delCartItem(String productCode);

    int updateCartItem(CartItemDTO cartItemDTO);

    int removeItem();
}
