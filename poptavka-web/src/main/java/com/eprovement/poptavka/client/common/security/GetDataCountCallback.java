/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * A new AsyncCallback that can handle GetDataCount requests.
 *
 * @author Martin Slavkovsky
 * @since 15.5.2014
 */
public class GetDataCountCallback extends SecuredAsyncCallback<Integer> {

    private UniversalAsyncGrid grid;

    public GetDataCountCallback(EventBusWithLookup lookupEventbus, UniversalAsyncGrid grid) {
        super(lookupEventbus);
        this.grid = grid;
    }

    @Override
    public void onSuccess(Integer result) {
        grid.getDataProvider().updateRowCount(result, true);
    }
}
