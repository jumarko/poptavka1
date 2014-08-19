package com.eprovement.poptavka.rest.payment;

public class PaymentSetting {
    public static final String PAYPAL_SANDBOX_VALIDATION_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr";
    public static final String PAYPAL_PRODUCTION_VALIDATION_URL = "https://www.paypal.com/cgi-bin/webscr";
    public static final String COMPLETED_STATUS_VALUE = "Completed";
    public static final String VERIFIED_RESPONSE_VALUE = "VERIFIED";
    private String businessEmail;

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }
}
