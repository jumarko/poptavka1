package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.infoWidgets.InfoWidgetsEventBus;
import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView;
import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView.IErrorPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 * Displays all erroneous states on body page.
 * @author ivlcek, Martin Slavkovsky
 */
@Presenter(view = ErrorView.class)
public class ErrorPresenter extends BasePresenter<IErrorView, InfoWidgetsEventBus> implements
        IErrorPresenter {

    private String errorId;

    /**
     * Bind objects and their action handlers.
     */
    @Override
    public void bind() {
        view.getReportinButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // display popup for reporting error to customer support
                eventBus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, errorId);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onDisplayError(int errorResponseCode, String errorId) {
        this.errorId = errorId;
        view.setErrorResponseCode(errorResponseCode);
        view.setErrorId(errorId);
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Error", null, false);
    }
}