package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.config.security.UserInfo;
import com.et.be.inbox.exception.ErrorCodeException;
import com.et.be.online.domain.dto.CartItemDTO;
import com.et.be.online.domain.mo.CartItem;
import com.et.be.online.domain.ro.ProductRO;
import com.et.be.online.domain.vo.CartItemVO;
import com.et.be.online.enums.OnlineCodeEnum;
import com.et.be.online.mapper.CartItemMapper;
import com.et.be.online.mapper.SceneMapper;
import com.et.be.online.mapper.ShoppingSessionMapper;
import com.et.be.online.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private ShoppingSessionMapper shoppingSessionMapper;



    @Override
    public void addCartItems(CartItemDTO cartItemDTO) {
        // todo sessionid
        Long sessionId = getSessionId();
        // if item already add and final quantity < = 3 ,quantity plus else insert new item
        QueryWrapper<CartItem> wrapper = Wrappers.query();
         wrapper.eq("product_code",cartItemDTO.getProductCode());
         wrapper.eq("session_id",sessionId);
        CartItem cartItem = cartItemMapper.selectOne(wrapper);
        boolean cartItemExists = !BeanUtil.isEmpty(cartItem);

        ProductRO product = sceneMapper.getSingleProduct(cartItemDTO.getProductCode());

        double discountprice = product.getPrice() * (100 -product.getDiscountPercent() )/ 100;

        // if cart item exists and total quantity <= 3 ,update
        if(cartItemExists && (cartItem.getQuantity() + cartItemDTO.getQuantity()) <= 3){
            int quantity = cartItem.getQuantity() + cartItemDTO.getQuantity();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity()) // update quantity
                    .setTotal(discountprice*quantity)
                    .setModifiedAt(new Date());
            cartItemMapper.updateById(cartItem);
        }
        // if product number exceed , return
        if(cartItemExists && (cartItem.getQuantity() + cartItemDTO.getQuantity()) > 3){
            throw new ErrorCodeException(OnlineCodeEnum.CART_ITEM_EXCEED);
        }
        // insert new item
        if(!cartItemExists){
            cartItem = new CartItem();
            cartItem.setQuantity(cartItemDTO.getQuantity())
                    .setTotal(discountprice*cartItemDTO.getQuantity())
                    .setSessionId(sessionId)
                    .setProductCode(cartItemDTO.getProductCode())
                    .setCreatedAt(new Date())
                    .setModifiedAt(new Date());
            cartItemMapper.insert(cartItem);
        }
        // update shopping_session total
        Double subtotal = cartItemMapper.selectCartItemTotalBySessionId(sessionId);
        shoppingSessionMapper.updateShoppingSessionTotalById(sessionId,subtotal);


    }

    @Override
    public List<CartItemVO> cart() {
        // todo sessionId
        Long sessionId = getSessionId();
        return cartItemMapper.cart(sessionId);
    }

    /**
     *  remove cart item by productcode
     * @param productCode product code
     * @return
     */
    @Override
    public int delCartItem(String productCode) {
        // todo sessionId
        Long sessionId = getSessionId();
        QueryWrapper<CartItem> wrapper = Wrappers.query();
        wrapper.eq("product_code",productCode)
                .eq("session_id",sessionId);
        // remove item
        int re = cartItemMapper.delete(wrapper);
        // update shopping_session
        Double subtotal = cartItemMapper.selectCartItemTotalBySessionId(sessionId);
        shoppingSessionMapper.updateShoppingSessionTotalById(sessionId,subtotal);
        return re;
    }

    @Override
    public int updateCartItem(CartItemDTO cartItemDTO) {
        // todo sessionId
        Long sessionId = getSessionId();
        QueryWrapper<CartItem> wrapper = Wrappers.query();
        wrapper.eq("product_code",cartItemDTO.getProductCode())
                .eq("session_id",sessionId);

        ProductRO product = sceneMapper.getSingleProduct(cartItemDTO.getProductCode());
        double discountprice = product.getPrice() * (100 -product.getDiscountPercent() )/ 100;

        CartItem cartItem = cartItemMapper.selectOne(wrapper);
        cartItem.setQuantity(cartItemDTO.getQuantity())
                .setModifiedAt(new Date())
                .setTotal(discountprice*cartItemDTO.getQuantity())   ;
        int res =cartItemMapper.updateById(cartItem);

        // update shopping_session
        Double subtotal = cartItemMapper.selectCartItemTotalBySessionId(sessionId);
        shoppingSessionMapper.updateShoppingSessionTotalById(sessionId,subtotal);

        return res;
    }

    @Override
    public int removeItem() {
        Long sessionId = getSessionId();
        QueryWrapper<CartItem> wrapper = Wrappers.query();
        wrapper.eq("session_id",sessionId);
        return cartItemMapper.delete(wrapper);
    }

    private Long getSessionId(){
        // email unique key replace username
        String email= UserInfo.getUsername();
        return shoppingSessionMapper.queryShoppingSession(email).getId();
    }
}
