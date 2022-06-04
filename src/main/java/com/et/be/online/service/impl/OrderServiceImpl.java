package com.et.be.online.service.impl;


import cn.hutool.core.util.NumberUtil;
import com.et.be.config.security.UserInfo;
import com.et.be.online.constant.SysConfigConstant;
import com.et.be.online.domain.mo.*;
import com.et.be.online.domain.vo.CartItemVO;
import com.et.be.online.domain.vo.OrderHistoryVO;
import com.et.be.online.domain.vo.OrderSummaryVO;
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


        return orderSummary;
    }

    @Override
    public Long newOrder(Long addressId) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
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
        // new payment Detail
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setProvider(PaymentProviderEnum.PAYPALPAY.name())
                .setTradNo(tradeNo)
                .setStatus(PaymentStatusEnum.CREATED.name())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        paymentDetailsMapper.insert(paymentDetails);

        // update order detail payment id
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPaymentId(paymentDetails.getId())
                .setId(orderDetailsId)
                .setModifiedAt(new Date());
        orderDetailsMapper.updateById(orderDetails);

    }
    @Override
    public void paypalOrdersCapture(String tradeNo) throws IOException {

        HttpResponse<Order> ordersCaptureInfo = PaypalCheckOutUtil.ordersCapture(tradeNo);

        // store  capture id

        PaymentDetails paymentDetails = new PaymentDetails();
        //paymentDetails.setCaptureId()


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

        List<OrderItem> items = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, orderDetailId).list();
        // calculate fee
        double total = items.stream().map(item -> {
            return item.getSubTotal() + item.getShipping() + item.getTax();
        }).mapToDouble(Double::doubleValue).sum();
        double itemTotal = items.stream().mapToDouble(OrderItem::getSubTotal).sum();
        double shipping = items.stream().mapToDouble(OrderItem::getShipping).sum();
        double tax = items.stream().mapToDouble(OrderItem::getTax).sum();


        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailId);
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        // address
        CustomerAddress customerAddress = customerAddressService.getById(orderDetails.getAddressId());
        //  purchaseUnits fill
        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId("PUHF")
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(doubleFormatter.format(total))
                .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("USD").value(doubleFormatter.format(itemTotal)))
                                                      .shipping(new Money().currencyCode("USD").value(doubleFormatter.format(shipping)))
                                                      .taxTotal(new Money().currencyCode("USD").value(doubleFormatter.format(tax))))
                        )
                .items( items.stream().map(
                        item -> new Item().name(item.getProductName())
                                          .unitAmount(new Money().currencyCode("USD").value(doubleFormatter.format(item.getPrice()*(100- item.getDiscountPercent())/100)))
                                          .tax(new Money().currencyCode("USD").value(doubleFormatter.format(item.getTax())))
                                          .quantity(String.valueOf(item.getQuantity()))
                          ).collect(Collectors.toList())
                )
                .shippingDetail(new ShippingDetail().name(new Name().fullName(customer.getUsername()))
                        .addressPortable(new AddressPortable()
                                            .addressLine1(customerAddress.getAddressLine1())
                                            .addressLine2(customerAddress.getAddressLine2())
                                            .adminArea2(customerAddress.getCity())
                                            .adminArea1(customerAddress.getState())
                                            .postalCode(customerAddress.getPostalCode())
                                            .countryCode(customerAddress.getCountry())
                        ));
        purchaseUnitRequests.add(purchaseUnitRequest);
        PurchaseUnitRequest purchaseUnitRequest2 =  new PurchaseUnitRequest().referenceId("PUHF2")
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(doubleFormatter.format(total))
                        .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("USD").value(doubleFormatter.format(itemTotal)))
                                .shipping(new Money().currencyCode("USD").value(doubleFormatter.format(shipping)))
                                .taxTotal(new Money().currencyCode("USD").value(doubleFormatter.format(tax))))
                )
                .items( items.stream().map(
                                item -> new Item().name(item.getProductName())
                                        .unitAmount(new Money().currencyCode("USD").value(doubleFormatter.format(item.getPrice()*(100- item.getDiscountPercent())/100)))
                                        .tax(new Money().currencyCode("USD").value(doubleFormatter.format(item.getTax())))
                                        .quantity(String.valueOf(item.getQuantity()))
                        ).collect(Collectors.toList())
                )
                .shippingDetail(new ShippingDetail().name(new Name().fullName(customer.getUsername()))
                        .addressPortable(new AddressPortable()
                                .addressLine1(customerAddress.getAddressLine1())
                                .addressLine2(customerAddress.getAddressLine2())
                                .adminArea2(customerAddress.getCity())
                                .adminArea1(customerAddress.getState())
                                .postalCode(customerAddress.getPostalCode())
                                .countryCode(customerAddress.getCountry())
                        ));
        purchaseUnitRequests.add(purchaseUnitRequest2);
        // return paypalV2Order
        PaypalV2Order paypalV2Order = new PaypalV2Order();
        paypalV2Order.setPurchaseUnitRequests(purchaseUnitRequests);

        return paypalV2Order;
    }
}
