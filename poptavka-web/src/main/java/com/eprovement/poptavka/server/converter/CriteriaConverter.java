/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.shared.search.SearchDefinition;

/**
 * Converts ResultCriteria to SearchDefinition and vice versa.
 * @author Juraj Martinka
 */
public final class CriteriaConverter extends AbstractConverter<ResultCriteria, SearchDefinition> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates CriteriaConverter.
     */
    private CriteriaConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public SearchDefinition convertToTarget(ResultCriteria source) {
        throw new UnsupportedOperationException("Convertion ResultCriteria to SearchDefinition failed!");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public ResultCriteria convertToSource(SearchDefinition definition) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder()
                    .firstResult(definition.getFirstResult())
                    .maxResults(definition.getMaxResult()).build();
        //TODO LATER Martin 17.4.2013 - ResultCriteria is no longer used in frontend - todo delete
//                    .orderByColumns(definition.getOrderColumns()).build();
        return resultCriteria;
    }
}
