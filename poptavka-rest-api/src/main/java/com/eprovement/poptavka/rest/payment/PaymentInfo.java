package com.eprovement.poptavka.rest.payment;

/**
 * The payment information sent by Paypal.
 */
public class PaymentInfo {
    /**
     * The status of the payment.
     */
    private String status;

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
     * The user's service ID.
     */
    private long itemNumber;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public long getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }
}
