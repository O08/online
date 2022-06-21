package com.et.be.base.controller;


import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.paypal.v2.api.PayPalPayService;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.et.be.base.config.PaypalConfigurer;
import com.et.be.base.exception.ErrorCodeException;

import com.et.be.base.handlers.PayPalPayMessageHandler;
import com.et.be.base.interceptor.PaypalMessageInterceptor;
import com.et.be.online.domain.mo.OrderDetails;
import com.et.be.online.domain.mo.PaymentDetails;
import com.et.be.online.domain.mo.PaypalV2Order;
import com.et.be.online.enums.OnlineCodeEnum;
import com.et.be.online.enums.OrderStatusEnum;
import com.et.be.online.service.OrderDetailsService;
import com.et.be.online.service.OrderFlowService;
import com.et.be.online.service.OrderService;
import com.et.be.online.service.PaymentDetailsService;
import com.et.be.util.PaypalCheckOutUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2018/05/06 10:30
 */
@RestController
@RequestMapping("payPalV2")
@Slf4j
public class PayPalV2PayController {


    private PayService service = null;
    @Autowired
    private PayPalPayMessageHandler payPalPayMessageHandler;

    @Autowired
    private PaypalMessageInterceptor paypalMessageInterceptor;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private OrderDetailsService orderDetailService;


    @PostConstruct
    public void init() {
//        PayPalConfigStorage storage = new PayPalConfigStorage();
//        storage.setClientID("AZDS0IhUZvJTO99unlvSDMfbZIP-p-UecYXZdJoweha9LFuqKXKcQIGZgfVaX6oGiAOJAUuJD7JwyTl1");
//        storage.setClientSecret("EK2YaOrw3oLSDWIRzvb9BWGTjiPPhY1fFUu5ylhUsGYLc_h_dlpJ0hr_LDEkbO9MyKP2P83YcywbPaem");
//        storage.setTest(true);
//        //发起付款后的页面转跳地址
//        storage.setReturnUrl("http://127.0.0.1:8081/payPalV2/payBackBefore.json");
//        // 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
//        //取消按钮转跳地址,
//        storage.setCancelUrl("http://127.0.0.1:8081/payPalV2/cancel");
        service = new PayPalPayService(new PaypalConfigurer().getPayPalConfigStorage());
    }

   @Autowired
   private OrderService orderService;

    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param orderDetailsId 订单号
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay(Long orderDetailsId) {
        // verify
        boolean verify = orderService.payVerify(orderDetailsId, OrderStatusEnum.CREATED.name());

        if(!verify){
            throw new ErrorCodeException(OnlineCodeEnum.PROCESSING);
        }
        PaypalV2Order paypalV2Order = orderService.newPaypalV2Order(orderDetailsId);
        // 创建支付订单
        String toPayHtml = PaypalCheckOutUtil.toPay(paypalV2Order);

        //某些支付下单时无法设置单号，通过下单后返回对应单号，如 paypal，友店。
        String tradeNo = paypalV2Order.getTradeNo();

        System.out.println("支付订单号：" + tradeNo + "  这里可以进行回存");
        // 回存交易流水 订单与支付流水进行关联
        orderService.newPaymentDetails(orderDetailsId,tradeNo);

        return toPayHtml;
    }


    /**
     *  failed to pay ,try again
     * @param orderDetailsId
     * @return
     */
    @RequestMapping(value = "rePay.html", produces = "text/html;charset=UTF-8")
    public String rePay(Long orderDetailsId) {
        // verify
        boolean verify = orderService.payVerify(orderDetailsId, OrderStatusEnum.FAIL.name());

        if(!verify){
            throw new ErrorCodeException(OnlineCodeEnum.PROCESSING);
        }
        PaypalV2Order paypalV2Order = orderService.newPaypalV2Order(orderDetailsId);
        // 创建支付订单
        String toPayHtml = PaypalCheckOutUtil.toPay(paypalV2Order);

        //某些支付下单时无法设置单号，通过下单后返回对应单号，如 paypal，友店。
        String tradeNo = paypalV2Order.getTradeNo();

        System.out.println("支付订单号：" + tradeNo + "  这里可以进行回存");
        // 回存交易流水 订单与支付流水进行关联 update tradeNo
        orderService.updatePaymentDetails(orderDetailsId,tradeNo);

        return toPayHtml;
    }

    /**
     * 申请退款接口
     *
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund() {
        // TODO 这里需要  refundAmount， curType， description， tradeNo
        RefundOrder order = new RefundOrder();
        order.setCurType(DefaultCurType.USD);
        order.setDescription(" description ");
        order.setTradeNo("paypal 平台的单号, 支付下单返回的单号");
        order.setRefundAmount(BigDecimal.valueOf(0.01));
        RefundResult refundResult = service.refund(order);
        System.out.println("退款成功之后返回退款单号：" + refundResult.getRefundNo());
        return refundResult;
    }

    /**
     * 查询退款
     *
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery() {
        RefundOrder order = new RefundOrder();
        order.setRefundNo("退款成功之后返回的退款单号");
        return service.refundquery(order);
    }
    /**
     * 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * PayPal确认付款调用的接口
     * 用户确认付款后，paypal调用的这个方法执行付款
     *
     * @param request 请求
     * @return 付款成功信息
     * @throws IOException IOException
     */
    @GetMapping(value = "returnUrl")
    public void returnUrl(HttpServletRequest request,HttpServletResponse response)  {
        NoticeParams noticeParams = service.getNoticeParams(new HttpRequestNoticeParams(request));
        // 日志记录
        log.info("###paypal  returnUrl ###params:{}",new Gson().toJson(noticeParams));
        String tradeNo = (String) noticeParams.getBody().get("token");

        try {
            // call paypal capture order api
            orderService.paypalOrdersCapture( tradeNo) ;

            // redirect to success page
            PaymentDetails paymentDetails = paymentDetailsService.getPaymentDetailsByTradeNo(tradeNo);
             response.sendRedirect("/pay-result.html?payResult=success&transNO="+tradeNo+"&amount="+paymentDetails.getAmount());

        } catch (IOException e) {
            try {
                // insert order stage
                OrderDetails orderDetails = orderDetailService.getOrderDetailsByTradeNo(tradeNo);
                orderFlowService.insertOrderFlow(orderDetails.getId(),OrderStatusEnum.FAIL.name());
                // redirect to fail page
                response.sendRedirect("/pay-result.html?payResult=error&orderNo="+orderDetails.getId());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }


    /**
     * 支付回调地址
     *
     * @param request 请求
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * 付款之后不会进行扣款，需要调用 {@link PayPalPayService#ordersCapture(String)}进行扣款，并返回 captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 注意：最好在付款成功之后回调时进行调用 {@link PayPalPayService#ordersCapture(String)}
     * 确认订单并返回确认后订单信息
     * <b>注意：此方法一个订单只能调用一次, 建议在支付回调时进行调用</b>
     * 这里主要用来获取captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 详情： https://developer.paypal.com/docs/api/orders/v2/#orders_capture
     *
     */
    @RequestMapping(value = "payBack.json")
    public void payBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        service.addPayMessageInterceptor(paypalMessageInterceptor);
//        service.setPayMessageHandler(payPalPayMessageHandler);
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
       service.payBack(new HttpRequestNoticeParams(request));
       // response.sendRedirect("/pay-result.html?payResult=success");
    }


    @RequestMapping(value = "cancel")
    public void cancelPaypalPay(HttpServletRequest request,HttpServletResponse response) throws IOException {


        log.info("paypal取消支付回调********************************");

        NoticeParams noticeParams = service.getNoticeParams(new HttpRequestNoticeParams(request));
        // 日志记录
        log.info("###paypal取消支付 ###params:{}",new Gson().toJson(noticeParams));
        String tradeNo = (String) noticeParams.getBody().get("token");
        // update statues
         orderService.cancelOrderItems(tradeNo);


        response.sendRedirect("/pay-result.html?payResult=cancel");
    }


    /**
     * paypal webhook 异步回调
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify.do")
    @ResponseBody
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {

        NoticeParams noticeParams = service.getNoticeParams(new HttpRequestNoticeParams(request));
        boolean verified = service.verify(noticeParams);


        log.info("ipn:"+noticeParams.getBody().toString());

        if(verified) {
            //支付状态
            String saleState=(String) noticeParams.getBody().get("resource");

            if(saleState.equalsIgnoreCase("completed")) {
                // 支付成功 处理逻辑
//                orderService.dealSuccess();
                response.setStatus(200);
                return;

//                if (paySuccess(payment, request)) {
//                    response.setStatus(200);
//                    return;
//                }else{
//                    log.warn("此支付订单更新失败，订单ID=");
//                    //这里返回500或者其他HTTP错误码，即可重发
//                    response.setStatus(500);
//                    return;
//                }
            }
        }
    }







}
