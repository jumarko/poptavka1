/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.domain.enums;

/**
 * Payment transaction statuses.
 * Is sent from Paypal after finnished transaction.
 *
 * @author Martin Slavkovsky
 * @since 23.7.2014
 */
public enum PaypalTransactionStatus {

    /**
     * A reversal has been canceled.
     * For example, you won a dispute with the customer,
     * and the funds for the transaction that was reversed have been returned to you.
     */
    CANCELED_REVERSAL,
    /**
     * The payment has been completed, and the funds have been added successfully to your account balance.
     */
    COMPPLETED,
    /**
     * The payment was denied.
     * This happens only if the payment was previously pending because of
     * one of the reasons listed for the pending_reason variable or the Fraud_Management_Filters_x variable.
     */
    DENIED,
    /**
     * This authorization has expired and cannot be captured. Toto je zrejme autorizacia medzi userom a Paypalom.
     */
    EXPIRED,
    /**
     * The payment has failed. This happens only if the payment was made from your customer's bank account.
     */
    FAILED,
    /**
     * The payment is pending. See pending_reason for more information.
     */
    PENDING,
    /**
     * You refunded the payment.
     */
    REFUNDED,
    /**
     * A payment was reversed due to a chargeback or other type of reversal.
     * The funds have been removed from your account balance and returned to the buyer.
     * The reason for the reversal is specified in the ReasonCode element.
     */
    REVERSED,
    /**
     * A payment has been accepted.
     */
    PROCESSED,
    /**
     * This authorization has been voided.
     */
    VOIDED;
}
