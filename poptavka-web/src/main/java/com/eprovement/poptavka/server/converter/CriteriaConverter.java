/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import java.util.ArrayList;
import java.util.Collection;

public final class CriteriaConverter implements Converter<ResultCriteria, SearchDefinition> {

    private CriteriaConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public SearchDefinition convertToTarget(ResultCriteria source) {
        throw new UnsupportedOperationException("Convertion ResultCriteria to SearchDefinition failed!");
    }

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

    @Override
    public ArrayList<SearchDefinition> convertToTargetList(Collection<ResultCriteria> sourceObjects) {
        throw new UnsupportedOperationException("Convertion List<ResultCriteria> to List<SearchDefinition> failed!");
    }

    @Override
    public ArrayList<ResultCriteria> convertToSourceList(Collection<SearchDefinition> targetObjects) {
        throw new UnsupportedOperationException("Convertion List<ResultCriteria> to List<SearchDefinition> failed!");
    }
}
