package com.eprovement.poptavka.rest.payment;


import java.util.Date;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.eprovement.poptavka.domain.enums.PaypalTransactionStatus;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.service.userservice.UserServiceService;

public class PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private UserServiceService userServiceService;

    @Transactional(readOnly = false)
    public void saveCredits(String transactionNumber, long orderNumber,
            float amount, PaypalTransactionStatus status, Date paymentDate) {
        Validate.notNull(transactionNumber);
        Validate.notNull(orderNumber);
        UserService userService = userServiceService
                .getUserServiceByOrderNumber(orderNumber);
        if (status == PaypalTransactionStatus.COMPLETED) {
            if (!transactionNumber.equals(userService.getTransactionNumber())) {
                float userAmount = Float.valueOf(amount);
                Service service = userService.getService();
                Validate.notNull(service);
                float servicePrice = service.getPrice().floatValue();
                if (servicePrice == userAmount) {
                    updateUserService(transactionNumber, service.getCredits(), userService,
                            Status.ACTIVE, status, paymentDate);
                } else {
                    LOGGER.error("The price of service is different from the amount paid by user !");
                    LOGGER.error(
                            "[transactionNumber={}, orderNumber={}, userAmount={}, servicePrice={}]",
                            transactionNumber, orderNumber, userAmount, servicePrice);
                }
            } else {
                LOGGER.error("It is only allowed once recharged credits to the transaction !");
                LOGGER.error(
                        "[transactionNumber={}, orderNumber={}, amount={}]",
                        transactionNumber, orderNumber, amount);
            }
        } else {
            updateUserService(transactionNumber, null, userService, null,
                    status, paymentDate);
        }
    }

    private void updateUserService(String transactionNumber, Integer amount,
            UserService userService, Status status, PaypalTransactionStatus paypalStatus, Date paymentDate) {
        userService.getBusinessUser().getBusinessUserData().addCredits(amount);
        userService.setStatus(status);
        userService.setTransactionNumber(transactionNumber);
        userService.setResponse(new Date());
        userService.setRequest(paymentDate);
        userService.setTransactionStatus(paypalStatus);
        userServiceService.update(userService);
    }
}
