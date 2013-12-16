/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;

/**
 * Converts Invoice to InvoiceDetail.
 * @author Juraj Martinka
 */
public final class InvoiceConverter extends AbstractConverter<Invoice, InvoiceDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter;
    private final Converter<UserService, UserServiceDetail> userServiceConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates InvoiceConverter.
     */
    private InvoiceConverter(Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter,
            Converter<UserService, UserServiceDetail> userServiceConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(paymentMethodConverter);
        Validate.notNull(userServiceConverter);

        this.paymentMethodConverter = paymentMethodConverter;
        this.userServiceConverter = userServiceConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public InvoiceDetail convertToTarget(Invoice source) {
        InvoiceDetail detail = new InvoiceDetail();

        detail.setBankAccountNumber(source.getBankAccountNumber());
        detail.setBankCode(source.getBankCode());
        detail.setConstSymbol(source.getConstSymbol());
        detail.setDueDate(convertDate(source.getDueDate()));
        detail.setId(source.getId());
        detail.setInvoiceNumber(source.getInvoiceNumber());
        detail.setIssueDate(convertDate(source.getIssueDate()));
        detail.setPaymentMethod(paymentMethodConverter.convertToTarget(source.getPaymentMethod()));
        detail.setShipmentDate(convertDate(source.getShipmentDate()));
        detail.setTaxBasis(source.getTaxBasis());
        detail.setTotalPrice(source.getTotalPrice());
        List<UserServiceDetail> userServices = new ArrayList<UserServiceDetail>();
        for (UserService userService : source.getUserServices()) {
            userServices.add(userServiceConverter.convertToTarget(userService));
            userServices.add(userServiceConverter.convertToTarget(userService));
        }
        detail.setUserServices(userServices);
        detail.setVariableSymbol(source.getVariableSymbol());
        detail.setVat(source.getVat());
        detail.setVatRate(source.getVatRate());

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Invoice convertToSource(InvoiceDetail source) {
        throw new UnsupportedOperationException();
    }
}
