package com.et.be.Feature.transaction.controller;

import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.spring.boot.autoconfigue.PayAutoConfiguration;
import com.egzosn.pay.spring.boot.core.PayServiceManager;
import com.egzosn.pay.spring.boot.core.bean.MerchantPayOrder;
import com.egzosn.pay.spring.boot.core.bean.MerchantQueryOrder;
import com.egzosn.pay.spring.boot.core.merchant.PaymentPlatformMerchantDetails;
import com.egzosn.pay.spring.boot.core.provider.MerchantDetailsManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Controller
@Api(value = "提供支付功能", tags = "交易系统-支付")
public class PayController {

    @Autowired
    private PayServiceManager manager;

    @Autowired
    private MerchantDetailsManager<PaymentPlatformMerchantDetails> merchantDetailsManager;

    @GetMapping("merchantExists")
    public Map<String, Object> merchantExists() {
        return new MapGen<String, Object>("exist", merchantDetailsManager.merchantExists("1")).getAttr();
    }


    @RequestMapping("/payment/goAliPay.html")
    @ApiOperation("跳转到阿里支付页面")
    public String goAliPay(String detailsId, String wayTrade, BigDecimal price) {
        MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, wayTrade, "订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""));
         manager.toPay(payOrder);
        PayAutoConfiguration a =new PayAutoConfiguration();
        return "a";
    }

    /**
     * 支付回调地址
     * @param request 请求
     * @param detailsId 列表id
     * @return 支付是否成功
     * @throws IOException IOException
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * </p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     */
    @RequestMapping(value = "payBack{detailsId}.json")
    public String payBack(HttpServletRequest request, @PathVariable String detailsId) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return manager.payBack(detailsId, request.getParameterMap(), request.getInputStream());
    }


    /**
     * 查询
     * 入参根据实际情况来填写，
     * 必填参数 detailsId
     * 普通查询 tradeNo,outTradeNo
     * <b>具体详情查看pay-java-demo内对应demo</b>
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query(MerchantQueryOrder order) {
//
        return manager.query(order);
    }

    /**
     * 交易关闭接口
     *
     * @param detailsId  id
     * @param tradeNo    tradeNo
     * @param outTradeNo outTradeNo
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close(String detailsId, String tradeNo, String outTradeNo) {
        final MerchantQueryOrder order = new MerchantQueryOrder();
        order.setDetailsId(detailsId);
        order.setTradeNo(tradeNo);
        order.setOutTradeNo(outTradeNo);
        return manager.close(order);
    }

    /**
     * 申请退款接口
     *
     * @param detailsId 账户id
     * @param order     订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund(String detailsId, RefundOrder order) {

        return manager.refund(detailsId, order);
    }

    /**
     * 查询退款
     *
     * @param order 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundQuery(String detailsId, RefundOrder order) {
        return manager.refundQuery(detailsId, order);
    }

    /**
     * 下载对账单
     * 必填参数 detailsId
     * 普通查询 billDate, billType
     * <b>具体详情查看pay-java-demo内对应demo</b>
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @RequestMapping("downloadBill")
    public Object downloadBill(MerchantQueryOrder order) {
        return manager.downloadBill(order);

    }

    /**
     * 提现
     * @param detailsId
     * @param order
     * @return
     */
    @RequestMapping("withdraw")
    public Object withdraw(String detailsId, TransferOrder order) {
        return manager.transfer(detailsId, order);

    }

}
