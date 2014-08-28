package com.eprovement.poptavka.rest.payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.eprovement.poptavka.domain.enums.PaypalTransactionStatus;

/**
 * The payment information sent by Paypal.
 */
public class PaymentInfo {
    private static final String PAYMENT_DATE_PATTERN = "HH:mm:ss MMM dd, yyyy z";

    /**
     * The status of the payment.
     */
    private PaypalTransactionStatus status;

    /**
     * The merchant's original transaction identification number for the payment
     * from the buyer, against which the case was registered.
     */
    private String transactionID;

    /**
     * Primary email address of the payment recipient.
     */
    private String receiverEmail;

    /**
     * Full amount of the customer's payment, before transaction fee is
     * subtracted.
     */
    private float amount;

    /**
     * The currency of the payment.
     */
    private String currency;

    /**
     * The service ID.
     */
    private String itemNumber;

    /**
     * The user's service ID.
     */
    private long orderNumber;

    /**
     * The date when the user paid for credits.
     */
    private Date paymentDate;

    public PaypalTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = PaypalTransactionStatus.valueOf(status.toUpperCase());
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDateText) throws ParseException {
        if (paymentDateText != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(PAYMENT_DATE_PATTERN, Locale.ENGLISH);
            this.paymentDate = sdf.parse(paymentDateText);
        }
    }
}
