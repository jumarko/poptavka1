/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;

public final class ProblemConverter extends AbstractConverter<Problem, ProblemDetail> {

    private ProblemConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ProblemDetail convertToTarget(Problem source) {
        ProblemDetail detail = new ProblemDetail();

        detail.setId(source.getId());
        detail.setText(source.getText());

        return detail;
    }

    @Override
    public Problem convertToSource(ProblemDetail source) {
        throw new UnsupportedOperationException();
    }
}
