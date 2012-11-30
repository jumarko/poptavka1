package com.eprovement.poptavka.client.root.activation;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.RootEventBus;
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
        extends LazyPresenter<ActivationCodePopupPresenter.ActivationCodePopupInterface, RootEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private int widgetToLoad = Constants.NONE;
    private BusinessUserDetail user;

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
                eventBus.activateUser(view.getActivationCodeBox().getText());
            }
        });
        view.getSendAgainButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.sentActivationCodeAgain(user);
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
    /* Initialization                                                         */
    /**************************************************************************/
    public void initActivationCodePopup(BusinessUserDetail user, int widgetToLoad) {
        this.user = user;
        this.widgetToLoad = widgetToLoad;
        view.getStatusLabel().setMessage(MSGS.activationCodeSent() + user.getEmail());
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    public void onResponseActivateUser(boolean activated) {
        view.getStatusLabel().setPassedSmall(activated);

        //inform user
        if (activated) {
            view.getStatusLabel().setMessage(MSGS.activationPassed());
            //close activation popup
            ((PopupPanel) view.getWidgetView()).hide();
            //login user automatically
            eventBus.autoLogin(
                    user.getEmail(),
                    user.getPassword(),
                    widgetToLoad);
        } else {
            view.getStatusLabel().setMessage(MSGS.activationFailed());
            view.getReportButton().setVisible(true);
        }
    }

    public void onResponseSendActivationCodeAgain(boolean sent) {
        view.getStatusLabel().setPassedSmall(sent);

        //inform user
        if (sent) {
            view.getStatusLabel().setMessage(
                    MSGS.newActivationCodeSent() + user.getEmail());
        } else {
            view.getStatusLabel().setMessage(MSGS.newActivationCodeSentFailed());
            view.getReportButton().setVisible(true);
        }
    }
}
