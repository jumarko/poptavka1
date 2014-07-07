package com.eprovement.poptavka.rest.payment;

import org.springframework.beans.factory.annotation.Autowired;

import com.eprovement.poptavka.service.userservice.UserServiceService;

public class PaymentService {
    @Autowired
    private UserServiceService userServiceService;

    public void saveCredits(String transactionID, long userServiceId, float amount) {
        userServiceService.addCredits(userServiceId, (int) amount);
    }

    public boolean containsTransaction(String transactionID) {
        return false;
    }
}
