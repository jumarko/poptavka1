package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.home.createDemand.DemandCreationEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ActivationCodePopupView.class, multiple = true)
public class ActivationCodePopupPresenter
        extends LazyPresenter<ActivationCodePopupPresenter.ActivationCodePopupInterface, DemandCreationEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private BusinessUserDetail client;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface ActivationCodePopupInterface extends LazyView {

        /** TEXTBOX. **/
        TextBox getActivationCodeBox();

        /** BUTTONS. **/
        Button getCloseButton();

        Button getActivateButton();

        Button getSendAgainButton();

        Button getReportButton();

        /** LABEL. **/
        StatusIconLabel getStatusLabel();

        /** WIDGET. **/
        Widget getWidgetView();
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getCloseButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ((PopupPanel) view.getWidgetView()).hide();
            }
        });
        view.getActivateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.activateClient(view.getActivationCodeBox().getText());
            }
        });
        view.getSendAgainButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.sentActivationCodeAgain(client);
            }
        });
        view.getReportButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //presmerovat na root module kde sa toto zavola
                eventBus.initEmailDialogPopup();
            }
        });
    }

    /**************************************************************************/
    /*                                                                         */
    /**************************************************************************/
    public void goToActivationCodePopup(BusinessUserDetail client) {
        this.client = client;
        view.getStatusLabel().setMessage(MSGS.activationCodeSent() + client.getEmail());
    }
}
