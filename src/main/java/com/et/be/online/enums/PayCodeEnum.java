package com.et.be.online.enums;

import com.et.be.online.enums.inter.Code;

public enum PayCodeEnum implements Code {


    /**
     * 验签失败 signVerified
     */
    SIGN_VERIFIED_FAILURE(601,"验签失败") ;


    private int code;
    private String desc;

    PayCodeEnum(int code, String desc) {
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
