package com.et.be.online.enums;


import com.et.be.online.enums.inter.Code;

// 电商 异常 code 2000 开始
public enum OnlineCodeEnum implements Code {


    /**
     *  购物车中一款产品超出顾客可购买数量
     */
    CART_ITEM_EXCEED(2001, "A product in your cart  exceeds the number of customers can buy!"),
    /**
     * 订单状态:正在处理
     */
    PROCESSING(2002, "processing!"),
    ALREADY_PAY(2003,"order already pay!"),
    NOT_SIGN_UP(2004,"The email address you entered is not valid!"),
    INVALID_TOKEN(2005,"token expired!"),
    NOT_SIGN_IN(2006,"Not Sign In!")
    ;

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
