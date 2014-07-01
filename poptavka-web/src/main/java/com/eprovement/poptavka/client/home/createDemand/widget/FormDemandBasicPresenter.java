/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.widget;


import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.home.createDemand.DemandCreationEventBus;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Defines form for basic step in demand creation process.
 *
 * @author Beho, Martin Slavkovsky
 */
@Presenter(view = FormDemandBasicView.class)
public class FormDemandBasicPresenter
    extends LazyPresenter<FormDemandBasicPresenter.FormDemandBasicInterface, DemandCreationEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface FormDemandBasicInterface extends LazyView, ProvidesValidate, IsWidget {

        FullDemandDetail updateBasicDemandInfo(FullDemandDetail demandToUpdate);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onInitDemandBasicForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view);
    }

}
