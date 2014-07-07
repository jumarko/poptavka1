package com.eprovement.poptavka.rest.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentValidator.class);

    @Autowired
    private SystemUtil systemUtil;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentSetting paymentSetting;

    public String getPaypalValidationURL() {
        return systemUtil.isProduction() ? PaymentSetting.PAYPAL_PRODUCTION_VALIDATION_URL
                : PaymentSetting.PAYPAL_SANDBOX_VALIDATION_URL;
    }

    public boolean isPaymentValid(PaymentInfo paymentInfo) {
        if (isStatusValid(paymentInfo.getStatus()) && isTransactionIDValid(paymentInfo.getTransactionID())
                && isReceiverEmailValid(paymentInfo.getReceiverEmail())) {
            return true;
        }
        return false;
    }

    private boolean isReceiverEmailValid(String receiverEmail) {
        String businessEmail = paymentSetting.getBusinessEmail();
        if (businessEmail.equals(receiverEmail)) {
            return true;
        }
        LOGGER.error("Unknown business email :: {}", receiverEmail);
        return false;
    }

    private boolean isTransactionIDValid(String transactionID) {
        if (!paymentService.containsTransaction(transactionID)) {
            return true;
        }
        LOGGER.error("Duplicate transaction :: {}", transactionID);
        return false;
    }

    private boolean isStatusValid(String status) {
        if (PaymentSetting.COMPLETED_STATUS_VALUE.equals(status)) {
            return true;
        }
        LOGGER.error("Not completed status :: {}", status);
        return false;
    }

    public boolean isResponseValid(String responseContent) {
        if (PaymentSetting.VERIFIED_RESPONSE_VALUE.equals(responseContent)) {
            return true;
        }
        LOGGER.error("Not verified response :: {}", responseContent);
        return false;
    }
}
