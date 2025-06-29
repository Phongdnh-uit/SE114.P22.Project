package com.se114p12.backend.dtos.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VnPayRequestDTO {
    private String orderInfo;
    private String orderType;
    private String txnRef;
    private int amount;
    private String bankCode;
    private String language;

    private String billingMobile;
    private String billingEmail;
    private String billingFullname;
    private String billingAddress;
    private String billingCity;
    private String billingCountry;
    private String billingState;

    private String invMobile;
    private String invEmail;
    private String invCustomer;
    private String invAddress;
    private String invCompany;
    private String invTaxCode;
    private String invType;
}