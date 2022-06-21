package com.et.be.online.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.base.exception.ErrorCodeException;
import com.et.be.base.security.UserInfo;
import com.et.be.online.domain.mo.*;
import com.et.be.online.domain.vo.CartItemVO;
import com.et.be.online.domain.vo.OrderHistoryVO;
import com.et.be.online.domain.vo.OrderSummaryVO;
import com.et.be.online.enums.OnlineCodeEnum;
import com.et.be.online.enums.OrderStatusEnum;
import com.et.be.online.enums.PaymentProviderEnum;
import com.et.be.online.enums.PaymentStatusEnum;
import com.et.be.online.mapper.OrderDetailsMapper;
import com.et.be.online.mapper.PaymentDetailsMapper;
import com.et.be.online.mapper.ShoppingSessionMapper;
import com.et.be.online.service.*;
import com.et.be.util.DeleveryUtil;
import com.et.be.util.PaypalCheckOutUtil;
import com.et.be.util.TaxUtil;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingSessionMapper shoppingSessionMapper;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private PaymentDetailsMapper paymentDetailsMapper;

    @Autowired
    private OrderFlowService orderFlowService;

    @Override
    public OrderSummaryVO orderSummary() {
        OrderSummaryVO orderSummary = new OrderSummaryVO();
        List<CartItemVO> items = cartItemService.cart();
        // items fee
        Double subTotal = items.stream().map( item -> item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100 )
                .mapToDouble(Double::doubleValue).sum();
        // tax
        Double tax = items.stream().map( item -> TaxUtil.averageTax(item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100))
                .mapToDouble(Double::doubleValue).sum();
        // shipping fee
        Double shipping = items.stream().map( item -> DeleveryUtil.averageDeleveryFee(item.getQuantity() *item.getWeight()))
                .mapToDouble(Double::doubleValue).sum();
        Double total = subTotal + tax + shipping;
        orderSummary.setItems(items)
                .setTax(tax)
                .setShipping(shipping)
                .setSubtotal(subTotal)
                .setTotal(total);

       // insert order flow statue machine creating this stage use user id replace order details id
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        List<OrderFlow> orderFlows = orderFlowService.lambdaQuery().eq(OrderFlow::getOrderId, customer.getId())
                .eq(OrderFlow::getStage, OrderStatusEnum.CREATING.name()).list();
        if(orderFlows.size() == 0){
            orderFlowService.insertOrderFlowCommon(customer.getId(),OrderStatusEnum.CREATING.name());
        }
        return orderSummary;
    }

    @Override
    public Long newOrder(Long addressId) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());

        // verify
        boolean verify =payVerify(customer.getId(),OrderStatusEnum.CREATING.name());
        if(!verify){
            throw new ErrorCodeException(OnlineCodeEnum.PROCESSING);
        }

        // get cart item
        List<CartItemVO> items = cartItemService.cart();

        // calculate fee
        // items fee
        Double subTotal = items.stream().map( item -> item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100 )
                .mapToDouble(Double::doubleValue).sum();
        // tax
        Double tax = items.stream().map( item -> TaxUtil.averageTax(item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100))
                .mapToDouble(Double::doubleValue).sum();
        // shipping fee
        Double shipping = items.stream().map( item -> DeleveryUtil.averageDeleveryFee(item.getQuantity() *item.getWeight()))
                .mapToDouble(Double::doubleValue).sum();
        Double total = subTotal + tax + shipping;


        // new order Details
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setUserId(customer.getId())
                .setAddressId(addressId)
                .setTotal(BigDecimal.valueOf(total))
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        orderDetailsMapper.insert(orderDetails);

        // new order item
        List<OrderItem> orderItems = items.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderDetails.getId())
                    .setSubTotal(item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100)
                    .setTax(TaxUtil.averageTax(item.getQuantity() * item.getPrice()*(100- item.getDiscountPercent())/100))
                    .setShipping(DeleveryUtil.averageDeleveryFee(item.getQuantity() *item.getWeight()))
                    .setProductCode(item.getProductCode())
                    .setProductName(item.getProductName())
                    .setPrice(item.getPrice())
                    .setDiscountPercent(item.getDiscountPercent())
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setOrderStatus(OrderStatusEnum.CREATED.name())
                    .setCreatedAt(new Date())
                    .setModifiedAt(new Date());
            return orderItem;
        }).collect(Collectors.toList());
        orderItemService.saveBatch(orderItems);

        // remove cart item
        cartItemService.removeItem();

        // update cart session total
        ShoppingSession shoppingSession = shoppingSessionMapper.queryShoppingSession(UserInfo.getUsername());
        shoppingSessionMapper.updateShoppingSessionTotalById(shoppingSession.getId(),0.00);

        // insert order flow statue machine
        orderFlowService.insertOrderFlow(orderDetails.getId(),OrderStatusEnum.CREATED.name());
        return orderDetails.getId();
    }

    @Override
    public List<OrderHistoryVO> orderHistory() {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        return orderItemService.orderHistory(customer.getId());
    }

    /**
     * 支付明细
     * @param orderDetailsId
     * @param tradeNo
     */
    @Override
    public void newPaymentDetails(Long orderDetailsId, String tradeNo) {
        // get order details
        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailsId);
        // new payment Detail
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAmount(orderDetails.getTotal())
                .setProvider(PaymentProviderEnum.PAYPALPAY.name())
                .setTradNo(tradeNo)
                .setStatus(PaymentStatusEnum.CREATED.name())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        paymentDetailsMapper.insert(paymentDetails);

        // update order detail payment id
        orderDetails.setPaymentId(paymentDetails.getId())
                .setModifiedAt(new Date());
        orderDetailsMapper.updateById(orderDetails);

    }
    // 一件商品 一个capture id
    @Override
    public void paypalOrdersCapture(String tradeNo) throws IOException {
        // pay success  , return
        PaymentDetails payStatus = paymentDetailsMapper.selectOne(new LambdaQueryWrapper<PaymentDetails>().eq(PaymentDetails::getTradNo, tradeNo));
        if(PaymentStatusEnum.SUCCESS.name().equals(payStatus.getStatus())){
            return;
        }
        // call paypal order capture api 一个订单只允许调用一次
        HttpResponse<Order> ordersCaptureInfo = PaypalCheckOutUtil.ordersCapture(tradeNo);

        // store  capture id  一件商品 一个 capture id 情况
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (PurchaseUnit purchaseUnit : ordersCaptureInfo.result().purchaseUnits()) {
            Capture capture = purchaseUnit.payments().captures().get(0);
            OrderItem orderItem = new OrderItem();
            orderItem.setCaptureId(capture.id())
                    .setId(Long.valueOf(purchaseUnit.referenceId()))
                    .setOrderStatus(OrderStatusEnum.PAID.name())
                    .setModifiedAt(new Date());
            orderItems.add(orderItem);
        }
        orderItemService.updateBatchById(orderItems);

        // update payment status SUCCESS
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(PaymentStatusEnum.SUCCESS.name())
                .setTradNo(tradeNo)
                .setModifiedAt(new Date());
        UpdateWrapper<PaymentDetails> paymentDetailsUpdateWrapper = Wrappers.update();
        paymentDetailsUpdateWrapper.lambda().eq(PaymentDetails::getTradNo,tradeNo);
        paymentDetailsMapper.update(paymentDetails,paymentDetailsUpdateWrapper);

        // new shipment
    }

    /**
     * paypal purchase unit list
     * @param orderDetailId order detail id
     * @return
     */
    @Override
    public  PaypalV2Order newPaypalV2Order(Long orderDetailId){
        // formatter double
        DecimalFormat doubleFormatter = new DecimalFormat("#.00");
        doubleFormatter.setRoundingMode(RoundingMode.HALF_UP);

        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailId);
        // pay success  , return
        PaymentDetails payStatus = paymentDetailsMapper.selectOne(new LambdaQueryWrapper<PaymentDetails>().eq(PaymentDetails::getId, orderDetails.getPaymentId()));
        if(!BeanUtil.isEmpty(payStatus) && PaymentStatusEnum.SUCCESS.name().equals( payStatus.getStatus())){
            throw new ErrorCodeException(OnlineCodeEnum.ALREADY_PAY);
        }

        List<OrderItem> items = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, orderDetailId).list();



        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        // address
        CustomerAddress customerAddress = customerAddressService.getById(orderDetails.getAddressId());
        //  purchaseUnits fill
        List<PurchaseUnitRequest> purchaseUnitRequests = items.stream().map(orderItem ->{
               Item item = new Item().name(orderItem.getProductName())
                       .unitAmount(new Money().currencyCode("USD").value(doubleFormatter.format(orderItem.getPrice()*(100- orderItem.getDiscountPercent())/100)))
                       .tax(new Money().currencyCode("USD").value(doubleFormatter.format(orderItem.getTax())))
                       .quantity(String.valueOf(orderItem.getQuantity()));

               List<Item> paypalItems = new ArrayList<>();
               paypalItems.add(item);
               // return PurchaseUnitRequest
              return  new PurchaseUnitRequest().referenceId(orderItem.getId().toString()) // 订单id
                      .customId(orderItem.getId().toString()) // seem not to work
                   .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(doubleFormatter.format(orderItem.getSubTotal()+orderItem.getTax()+orderItem.getShipping()))
                           .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("USD").value(doubleFormatter.format(orderItem.getSubTotal())))
                                   .shipping(new Money().currencyCode("USD").value(doubleFormatter.format(orderItem.getShipping())))
                                   .taxTotal(new Money().currencyCode("USD").value(doubleFormatter.format(orderItem.getTax()))))
                   ).items(paypalItems)
                       .shippingDetail(new ShippingDetail().name(new Name().fullName(customer.getUsername()))
                               .addressPortable(new AddressPortable()
                                       .addressLine1(customerAddress.getAddressLine1())
                                       .addressLine2(customerAddress.getAddressLine2())
                                       .adminArea2(customerAddress.getCity())
                                       .adminArea1(customerAddress.getState())
                                       .postalCode(customerAddress.getPostalCode())
                                       .countryCode(customerAddress.getCountry())
                               ));
           }).collect(Collectors.toList());
        // return paypalV2Order
        PaypalV2Order paypalV2Order = new PaypalV2Order();
        paypalV2Order.setPurchaseUnitRequests(purchaseUnitRequests);

        return paypalV2Order;
    }

    /**
     * verify order status
     * @param id
     */
    @Override
    public boolean payVerify(Long id,String stage){
        // verify identity or order exists and debounce
        return orderFlowService.deleteOrderFlow(id,stage)> 0 ;

    }

    /**
     * update payment details tradeno
     * @param orderDetailsId
     * @param tradeNo
     */
    @Override
    public void updatePaymentDetails(Long orderDetailsId, String tradeNo) {
        // get payment details id
        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailsId);
        // update payment Detail
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(orderDetails.getPaymentId())
                .setProvider(PaymentProviderEnum.PAYPALPAY.name())
                .setTradNo(tradeNo)
                .setStatus(PaymentStatusEnum.CREATED.name())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        paymentDetailsMapper.updateById(paymentDetails);

    }

    @Override
    public void cancelOrderItems(String tradeNo) {
        // get payment details id
        LambdaQueryWrapper<PaymentDetails> paymentDetailsLambdaQueryWrapper =new LambdaQueryWrapper<>();
        paymentDetailsLambdaQueryWrapper.eq(PaymentDetails::getTradNo,tradeNo);
        PaymentDetails paymentDetails = paymentDetailsMapper.selectOne(paymentDetailsLambdaQueryWrapper);

        // get order details id
        LambdaQueryWrapper<OrderDetails> OrderDetailsWrapper =new LambdaQueryWrapper<>();
        OrderDetailsWrapper.eq(OrderDetails::getPaymentId,paymentDetails.getId());
        OrderDetails orderDetails = orderDetailsMapper.selectOne(OrderDetailsWrapper);

        // update order item status by order details id status cancel
        orderItemService.update(new LambdaUpdateWrapper<OrderItem>().set(OrderItem::getOrderStatus,OrderStatusEnum.CANCELLED.name())
                .eq(OrderItem::getOrderId,orderDetails.getId()));
    }
}
