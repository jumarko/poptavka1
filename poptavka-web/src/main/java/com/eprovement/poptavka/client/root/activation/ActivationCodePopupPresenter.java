package com.eprovement.poptavka.client.root.activation;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
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
                eventBus.activateUser(user, view.getActivationCodeBox().getText());
            }
        });
        view.getSendAgainButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.sendActivationCodeAgain(user);
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
        view.getStatusLabel().setMessage(MSGS.activationCodeSent() + " " + user.getEmail());
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    public void onResponseActivateUser(UserActivationResult activationResult) {
        view.getStatusLabel().setPassedSmall(activated);

        //inform user
        switch(activationResult) {
            case OK :
                reportActivationSuccessAndLoginUser();
                break;
            case ERROR_UNKNOWN_USER:
                reportActivationFailure(MSGS.activationFailedUnknownUser());
                break;
            case ERROR_INCORRECT_ACTIVATION_CODE:
                reportActivationFailure(MSGS.activationFailedIncorrectActivationCode());
                break;
            case ERROR_EXPIRED_ACTIVATION_CODE:
                reportActivationFailure(MSGS.activationFailedExpiredActivationCode());
                break;
            default:
                reportActivationFailure(MSGS.activationFailedUnknownError());
        }

    }

    public void onResponseSendActivationCodeAgain(boolean sent) {
        view.getStatusLabel().setPassedSmall(sent);

        //inform user
        if (sent) {
            view.getStatusLabel().setMessage(
                    MSGS.activationNewCodeSent() + " " + user.getEmail());
        } else {
            reportActivationFailure(MSGS.activationFailedSentNewCode());
        }
    }


    private void reportActivationSuccessAndLoginUser() {
        view.getStatusLabel().setMessage(MSGS.activationPassed());
        view.getStatusLabel().setState(StatusIconLabel.State.ACCEPT_24);
        //close activation popup
        ((PopupPanel) view.getWidgetView()).hide();
        //login user automatically
        eventBus.autoLogin(
                user.getEmail(),
                user.getPassword(),
                widgetToLoad);
    }


    private void reportActivationFailure(String errorMessage) {
        view.getStatusLabel().setMessage(errorMessage);
        view.getStatusLabel().setState(StatusIconLabel.State.ERROR_24);
        view.getReportButton().setVisible(true);
    }

}
