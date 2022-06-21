package com.et.be.util;

import com.et.be.base.config.PaypalCrentials;
import com.et.be.online.domain.mo.PaypalV2Order;
import com.google.gson.Gson;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class PaypalCheckOutUtil {
    public static HttpResponse<Order> ordersCapture(String approveOrderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(approveOrderId);
        HttpResponse<Order> response = PaypalCrentials.client.execute(request);
        log.info("paypal capture response:"+ new Gson().toJson(response));
        return response;
    }
    public static HttpResponse<Order>  createOrder(List<PurchaseUnitRequest> purchaseUnitRequests) throws IOException {
        Order order = null;
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        orderRequest.applicationContext(PaypalCrentials.applicationContext);
        orderRequest.purchaseUnits(purchaseUnitRequests);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);


            // Call API with your client and get a response for your call
        HttpResponse<Order> response = PaypalCrentials.client.execute(request);
        log.info("paypal create order response:"+ new Gson().toJson(response));

      return response;
    }

    public static  String  toPay(PaypalV2Order paypalV2Order) {
        HttpResponse<Order> response = null;
        try {
            response = createOrder(paypalV2Order.getPurchaseUnitRequests());
            if (response.statusCode() == 201) {
                // 某些支付下单时无法设置单号，通过下单后返回对应单号，如 paypal，友店。
                paypalV2Order.setTradeNo(response.result().id());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String approveHref = response.result().links().stream().filter(link -> link.rel().equals("approve")).findFirst().get().href();

        return String.format("<script type=\"text/javascript\">location.href=\"%s\"</script>", approveHref);

    }

}
