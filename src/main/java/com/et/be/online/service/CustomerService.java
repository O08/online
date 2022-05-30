package com.et.be.online.service;

import com.et.be.online.domain.mo.Customer;

public interface CustomerService {


    /**
     *  sign up
     * @param signUpInfo
     * @return false create account fail; true create account success
     */
    boolean doSignUp(String signUpInfo);

    Customer getCustomerByEmail(String email);

}
