/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.validation;

import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.ContactUsDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * {@link AbstractGwtValidatorFactory} that creates the specified {@link GwtValidator}.
 */
public final class SampleValidatorFactory extends AbstractGwtValidatorFactory {

    /**
     * Validator marker for the Validation Sample project. Only the classes
     * listed in the {@link GwtValidation} annotation can be validated.
     */
    @GwtValidation(value = {FullClientDetail.class, FullSupplierDetail.class, FullDemandDetail.class,
            AddressDetail.class, ContactUsDetail.class, ICatLocDetail.class, ICatLocDetail.class,
            MessageDetail.class, UserDetail.class, BusinessUserDetail.class, OfferMessageDetail.class },
    groups = {Default.class, Extended.class, SearchGroup.class })
    public interface GwtValidator extends Validator {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GwtValidator.class);
    }
}
