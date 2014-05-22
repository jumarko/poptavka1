/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 * A new AsyncCallback that can handle GetDataCount requests.
 *
 * @author Martin Slavkovsky
 * @since 15.5.2014
 */
public class GetDataCallback<T> extends SecuredAsyncCallback<List<T>> {

    private UniversalAsyncGrid grid;
    private int requestId;

    public GetDataCallback(EventBusWithLookup lookupEventbus, UniversalAsyncGrid grid, int requestId) {
        super(lookupEventbus);
        this.grid = grid;
        this.requestId = requestId;
    }

    @Override
    public void onSuccess(List<T> result) {
        grid.updateRowData(result, requestId);
    }
}
