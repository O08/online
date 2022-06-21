package com.et.be.util;

import com.et.be.base.config.Kuaidi100Config;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleveryUtil {
    @Autowired
    private Kuaidi100Config kuaidi100Config;

    /**
     * 查询物流轨迹
     * @param deliveryCompany 查询的快递公司的编码
     * @param shipNO 查询的快递单号， 单号的最小长度4个字符，最大长度32个字符
     * @param phone 收、寄件人的电话号码（手机和固定电话均可，只能填写一个，顺丰速运和丰网速运必填，其他快递公司选填。如座机号码有分机号，分机号无需传入。）
     * @return
     */
    public HttpResult queryDelevery(String  deliveryCompany, String shipNO , String phone) throws Exception {
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(deliveryCompany);
        queryTrackParam.setNum(shipNO);
        queryTrackParam.setPhone(phone);
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(kuaidi100Config.getCustomer());
        queryTrackReq.setSign(SignUtils.querySign(param ,kuaidi100Config.getKey(),kuaidi100Config.getCustomer()));

        IBaseClient baseClient = new QueryTrack();

        return baseClient.execute(queryTrackReq);

    }

    /**
     * 快递费用
     * @return
     */
    public static Double calculateDeleveryFee(){
        return 200.00;
    }

    /**
     * 简单快递费用计算 以美元计价 kg计量
     * @return
     */
    public static Double averageDeleveryFee(int weight){
        return 3+45.0*weight/1000;
    }


}
