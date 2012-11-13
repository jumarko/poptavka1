/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eprovement.poptavka.client.common.validation;

import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
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
    @GwtValidation(value = {FullSupplierDetail.class, FullDemandDetail.class,
            AddressDetail.class, EmailDialogDetail.class, LocalityDetail.class, CategoryDetail.class },
    groups = {Default.class })
    public interface GwtValidator extends Validator {
    }

    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GwtValidator.class);
    }
}
