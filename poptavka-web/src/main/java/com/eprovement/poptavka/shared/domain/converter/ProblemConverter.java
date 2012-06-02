/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;

public class ProblemConverter extends AbstractConverter<Problem, ProblemDetail> {
    @Override
    public ProblemDetail convertToTarget(Problem source) {
        ProblemDetail detail = new ProblemDetail();

        detail.setId(source.getId());
        detail.setText(source.getText());

        return detail;
    }

    @Override
    public Problem converToSource(ProblemDetail source) {
        throw new UnsupportedOperationException();
    }
}
