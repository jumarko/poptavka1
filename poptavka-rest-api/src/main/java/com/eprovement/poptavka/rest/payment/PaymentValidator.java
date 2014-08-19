package com.eprovement.poptavka.rest.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentValidator {
    private static final String CURRENCY_USD = "USD";

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
        return isCurrencyValid(paymentInfo.getCurrency()) && isReceiverEmailValid(paymentInfo.getReceiverEmail());
    }

    private boolean isCurrencyValid(String currency) {
        if (CURRENCY_USD.equals(currency)) {
            return true;
        }
        LOGGER.error("Only {} currency is valid :: not {}", CURRENCY_USD, currency);
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

    public boolean isResponseValid(String responseContent) {
        if (PaymentSetting.VERIFIED_RESPONSE_VALUE.equals(responseContent)) {
            return true;
        }
        LOGGER.error("Not verified response :: {}", responseContent);
        return false;
    }
}
