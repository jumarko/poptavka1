/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.service.root;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
public interface RootRPCServiceAsync {

    void unsubscribe(String password, AsyncCallback<Boolean> callback);

    void getCreditCount(long userID, AsyncCallback<Integer> callback);
}
