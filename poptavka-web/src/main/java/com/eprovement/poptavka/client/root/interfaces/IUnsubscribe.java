/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
public interface IUnsubscribe {

    public interface Presenter {

        void onInitUnsubscribe(String password);

        void onResponseUnsubscribe(Boolean result);
    }

    public interface View extends LazyView, IsWidget {

        Button getCancel();

        Button getUnsubscribe();

        SimplePanel getFooterContainer();
    }
}
