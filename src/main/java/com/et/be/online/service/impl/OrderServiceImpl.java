package com.et.be.online.service.impl;

import com.egzosn.pay.paypal.v2.bean.PayPalOrder;
import com.egzosn.pay.paypal.v2.bean.order.AddressPortable;
import com.egzosn.pay.paypal.v2.bean.order.Name;
import com.egzosn.pay.paypal.v2.bean.order.ShippingDetail;
import com.et.be.config.security.UserInfo;
import com.et.be.online.constant.SysConfigConstant;
import com.et.be.online.domain.mo.*;
import com.et.be.online.domain.vo.CartItemVO;
import com.et.be.online.domain.vo.OrderHistoryVO;
import com.et.be.online.domain.vo.OrderSummaryVO;
import com.et.be.online.enums.PaymentProviderEnum;
import com.et.be.online.enums.PaymentStatusEnum;
import com.et.be.online.mapper.OrderDetailsMapper;
import com.et.be.online.mapper.OrderItemMapper;
import com.et.be.online.mapper.PaymentDetailsMapper;
import com.et.be.online.mapper.ShoppingSessionMapper;
import com.et.be.online.service.*;
import com.et.be.util.DeleveryUtil;
import com.et.be.util.TaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public PayPalOrder newPayPalOrder(Long orderDetailId) {

        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailId);
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        // return paypal order
        CustomerAddress customerAddress = customerAddressService.getById(orderDetails.getAddressId());
        PayPalOrder payPalOrder = new PayPalOrder();
        payPalOrder.setBrandName(SysConfigConstant.BRAND_NAME);
        payPalOrder.setDescription(SysConfigConstant.ORDER_DESCRIPTION);
        payPalOrder.setPrice(orderDetails.getTotal());
        payPalOrder.setShippingDetail(new ShippingDetail()
                .name(new Name().fullName(customer.getUsername()))
                .addressPortable(new AddressPortable()
                        .addressLine1(customerAddress.getAddressLine1())
                        .addressLine2(customerAddress.getAddressLine2())
                        .adminArea2(customerAddress.getCity())
                        .adminArea1(customerAddress.getState())
                        .postalCode(customerAddress.getPostalCode())
                        .countryCode(customerAddress.getCountry())));
        return payPalOrder;
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
}
