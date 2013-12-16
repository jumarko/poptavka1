/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.google.gwt.user.client.ui.Composite;
import com.mvp4g.client.view.ReverseViewInterface;

/**
 * Class that extends Composite and implements ReverseViewInterface.
 *
 * @author Beho
 * @param <P>
 */
public class ReverseCompositeView<P> extends Composite implements
        ReverseViewInterface<P> {
    protected P presenter;

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    public P getPresenter() {
        return presenter;
    }
}
