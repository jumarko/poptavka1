/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;

/**
 * Converts Problem to ProblemDetail.
 * @author Juraj Martinka
 */
public final class ProblemConverter extends AbstractConverter<Problem, ProblemDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ProblemConverter.
     */
    private ProblemConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ProblemDetail convertToTarget(Problem source) {
        ProblemDetail detail = new ProblemDetail();

        detail.setId(source.getId());
        detail.setText(source.getText());

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Problem convertToSource(ProblemDetail source) {
        throw new UnsupportedOperationException();
    }
}
