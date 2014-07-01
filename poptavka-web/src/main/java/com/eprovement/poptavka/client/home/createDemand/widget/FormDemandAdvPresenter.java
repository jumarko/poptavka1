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
 * Defines form for advanced step in demand creation process.
 *
 * @author Beho, Martin Slavkovsky
 */
@Presenter(view = FormDemandAdvView.class)
public class FormDemandAdvPresenter extends
    LazyPresenter<FormDemandAdvPresenter.FormDemandAdvViewInterface, DemandCreationEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface FormDemandAdvViewInterface extends LazyView, ProvidesValidate, IsWidget {

        FullDemandDetail updateAdvDemandInfo(FullDemandDetail demandToUpdate);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onInitDemandAdvForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view);
    }

}
