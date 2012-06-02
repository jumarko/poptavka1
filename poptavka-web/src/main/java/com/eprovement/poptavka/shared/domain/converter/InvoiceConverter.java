/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import java.util.ArrayList;
import java.util.List;

public class InvoiceConverter extends AbstractConverter<Invoice, InvoiceDetail> {

    private final PaymentMethodConverter paymentMethodConverter = new PaymentMethodConverter();

    @Override
    public InvoiceDetail convertToTarget(Invoice source) {
        InvoiceDetail detail = new InvoiceDetail();

        detail.setBankAccountNumber(source.getBankAccountNumber());
        detail.setBankCode(source.getBankCode());
        detail.setConstSymbol(source.getConstSymbol());
        detail.setDueDate(source.getDueDate());
        detail.setId(source.getId());
        detail.setInvoiceNumber(source.getInvoiceNumber());
        detail.setIssueDate(source.getIssueDate());
        detail.setPaymentMethod(paymentMethodConverter.convertToTarget(source.getPaymentMethod()));
        detail.setShipmentDate(source.getShipmentDate());
        detail.setTaxBasis(source.getTaxBasis());
        detail.setTotalPrice(source.getTotalPrice());
        List<UserServiceDetail> userServices = new ArrayList<UserServiceDetail>();
        for (UserService userService : source.getUserServices()) {
            userServices.add(UserServiceDetail.createAccessRoleDetail(userService));
        }
        detail.setUserServices(userServices);
        detail.setVariableSymbol(source.getVariableSymbol());
        detail.setVat(source.getVat());
        detail.setVatRate(source.getVatRate());

        return detail;

    }

    @Override
    public Invoice converToSource(InvoiceDetail source) {
        throw new UnsupportedOperationException();
    }
}
