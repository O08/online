package com.et.be.base.config;

import com.et.be.online.constant.SysConfigConstant;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.ApplicationContext;

public class PaypalCrentials {
    static String clientId = "AZDS0IhUZvJTO99unlvSDMfbZIP-p-UecYXZdJoweha9LFuqKXKcQIGZgfVaX6oGiAOJAUuJD7JwyTl1";
    static String secret = "EK2YaOrw3oLSDWIRzvb9BWGTjiPPhY1fFUu5ylhUsGYLc_h_dlpJ0hr_LDEkbO9MyKP2P83YcywbPaem";


    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    // Creating a client for the environment
    public static PayPalHttpClient client = new PayPalHttpClient(environment);

    public static ApplicationContext applicationContext = new ApplicationContext().brandName(SysConfigConstant.BRAND_NAME)
            .landingPage("BILLING")
            .cancelUrl("http://127.0.0.1:8081/payPalV2/cancel")
            .returnUrl("http://127.0.0.1:8081/payPalV2/returnUrl");
}
