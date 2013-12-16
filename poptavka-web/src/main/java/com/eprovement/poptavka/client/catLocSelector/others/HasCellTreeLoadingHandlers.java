/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;

/**
 * Implement in widget that want to handle TreeLoading handler.
 * @author Martin Slavkovsky
 */
public interface HasCellTreeLoadingHandlers {

    LoadingStateChangeEvent.Handler getLoadingHandler();
}
