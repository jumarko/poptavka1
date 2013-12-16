/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.widget;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.home.createDemand.DemandCreationEventBus;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;


/**
 * Defines form for advanced step in demand creation process.
 *
 * @author Beho, Martin Slavkovsky
 */
@Presenter(view = FormDemandAdvView.class, multiple = true)
public class FormDemandAdvPresenter extends
    LazyPresenter<FormDemandAdvPresenter.FormDemandAdvViewInterface, DemandCreationEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface FormDemandAdvViewInterface extends LazyView {

        Widget getWidgetView();

        boolean isValid();

        FullDemandDetail updateAdvDemandInfo(FullDemandDetail demandToUpdate);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onInitDemandAdvForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
