/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.activation.ActivationEmail;
import cz.poptavka.sample.shared.domain.adminModule.ActivationEmailDetail;

public class ActivationEmailConverter extends AbstractConverter<ActivationEmail, ActivationEmailDetail> {

    @Override
    public ActivationEmailDetail convertToTarget(ActivationEmail source) {
        ActivationEmailDetail detail = new ActivationEmailDetail();

        detail.setId(source.getId());
        detail.setActivationLink(source.getActivationLink());
        detail.setTimeout(source.getValidTo());

        return detail;
    }

    @Override
    public ActivationEmail converToSource(ActivationEmailDetail source) {
        throw new UnsupportedOperationException();
    }
}
