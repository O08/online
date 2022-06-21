package com.et.be.online.service;

import com.et.be.online.domain.dto.CustomerDTO;
import com.et.be.online.domain.dto.PasswordResetDTO;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.mo.SignUpInfo;

public interface CustomerService {


    /**
     *  sign up
     * @param signUpInfo
     * @return false create account fail; true create account success
     */
    boolean doSignUp(SignUpInfo signUpInfo);

    Customer getCustomerByEmail(String email);

    int doEditName(CustomerDTO customerDTO);

    int doEditPassword(String customerDTO);

    int doEditPhone(CustomerDTO customerDTO);

    int doCloseAccount();

    boolean doSendPasswordResetEmail(String email);

    boolean doPasswordReset(PasswordResetDTO passwordResetDTO);
}
