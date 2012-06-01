/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.user.Problem;
import cz.poptavka.sample.shared.domain.adminModule.ProblemDetail;

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
