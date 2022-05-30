package com.et.be.online.enums;

import com.et.be.inbox.enums.inter.Code;

// 电商 异常 code 2000 开始
public enum OnlineCodeEnum implements Code {


    /**
     *  购物车中一款产品超出顾客可购买数量
     */
    CART_ITEM_EXCEED(2001, "A product in your cart  exceeds the number of customers can buy!");

    private int code;
    private String desc;

    OnlineCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
