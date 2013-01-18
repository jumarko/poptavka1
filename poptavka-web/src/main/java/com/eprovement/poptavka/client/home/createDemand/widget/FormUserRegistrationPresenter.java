package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.home.createDemand.DemandCreationEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = FormUserRegistrationView.class, multiple = true)
public class FormUserRegistrationPresenter
        extends LazyPresenter<FormUserRegistrationPresenter.FormRegistrationInterface, DemandCreationEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //Constants
    private static final int SHORT = 5;
    private static final int LONG = 8;
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    //Attributes
    private BusinessUserDetail clientToRegister;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface FormRegistrationInterface extends LazyView {

        /** SETTERS. **/
        void setMailFlag(boolean flag);

        void setPasswordFlag(boolean flag);

        void setPasswordLengthFlag(boolean flag);

        void toggleCompanyButtons(boolean toggle);

        /** GETTERS. **/
        //Panels
        SimplePanel getAddressHolder();

        //Buttons
        RadioButton getPersonButton();

        RadioButton getCompanyButton();

        Button getBackButton();

        Button getRegisterButton();

        //TextBoxes
        HasValueChangeHandlers<String> getEmailBox();

        PasswordTextBox getPwdBox();

        PasswordTextBox getPwdConfirmBox();

        //StatusIconLabels
        StatusIconLabel getMailStatus();

        StatusIconLabel getPwdStatus();

        StatusIconLabel getPwdConfirmStatus();

        //data
        BusinessUserDetail getNewClient();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /** Injecting widget. **/
    public void initRegistrationForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
        eventBus.initAddressWidget(view.getAddressHolder());
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getPersonButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> value) {
                if (value.getValue()) {
                    view.toggleCompanyButtons(false);
                }
            }
        });
        view.getCompanyButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> value) {
                if (value.getValue()) {
                    view.toggleCompanyButtons(true);
                }
            }
        });
        view.getBackButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                //just for toggle button
//                eventBus.toggleLoginRegistration(); ???
                eventBus.restoreDefaultFirstTab();

            }
        });
        view.getRegisterButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //TODO Martin - dorobit ostane validacie, nie len addresa
                if (((AddressSelectorView) view.getAddressHolder().getWidget()).isValid()) {
                    eventBus.loadingShow(MSGS.progressRegisterClient());
                    clientToRegister = view.getNewClient();
                    //register a shof popup na activation code
                    eventBus.registerNewClient(clientToRegister);
                }
            }
        });
        view.getEmailBox().addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> value) {
                /** UI method **/
                initVisualMailCheck(value.getValue());
            }
        });
        view.getPwdBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPwdCheck(view.getPwdBox().getText());
            }
        });
        view.getPwdConfirmBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent arg0) {
                initVisualPwdConfirmCheck();
            }
        });
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        if (isAvailable) {
            view.setMailFlag(true);

            // TODO change to global status changer eventBus call
            view.getMailStatus().setState(State.ACCEPT_16);
            view.getMailStatus().setDescription(MSGS.formUserRegMailAvailable());
        } else {
            view.setMailFlag(false);

            // TODO change to global status changer eventBus call
            view.getMailStatus().setState(State.ERROR_16);
        }
    }

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    /** Visualization methods. **/
    private void initVisualMailCheck(String value) {
        // TODO change to global status changer eventBus call
        view.getMailStatus().setState(State.LOAD_24);
        view.getMailStatus().setVisible(true);
        if (value.contains("@") && value.contains(".")) {
            eventBus.checkFreeEmail(value);
        } else {
            // TODO change to global status changer eventBus call
            view.getMailStatus().setStateWithDescription(State.ERROR_16, MSGS.formUserRegMalformedEmail());
        }
    }

    private void initVisualPwdCheck(String value) {
        view.getPwdStatus().setVisible(true);
        if (value.length() <= SHORT) {

            // TODO change to global status changer eventBus call
            view.getPwdStatus().setStateWithDescription(State.ERROR_16, MSGS.formUserRegShortPassword());
            view.setPasswordLengthFlag(false);
            return;
        }
        view.setPasswordLengthFlag(true);
        if ((value.length() <= LONG) && (value.length() > SHORT)) {
            view.setPasswordFlag(true);

            // TODO change to global status changer eventBus call
            view.getPwdStatus().setStateWithDescription(State.INFO_16, MSGS.formUserRegSemiStrongPassword());
        }
        if (value.length() > LONG) {
            view.setPasswordFlag(true);

            // TODO change to global status changer eventBus call
            view.getPwdStatus().setStateWithDescription(State.ACCEPT_16, MSGS.formUserRegStrongPassword());
        }
    }

    private void initVisualPwdConfirmCheck() {
        view.getPwdConfirmStatus().setVisible(true);
        if (!view.getPwdBox().getText().equals(view.getPwdConfirmBox().getText())) {
            view.setMailFlag(false);

            // TODO change to global status changer eventBus call
            view.getPwdConfirmStatus().setStateWithDescription(State.ERROR_16, MSGS.formUserRegPasswordsUnmatch());
        } else {
            view.setMailFlag(true);

            // TODO change to global status changer eventBus call
            view.getPwdConfirmStatus().setStateWithDescription(State.ACCEPT_16, MSGS.formUserRegPasswordsMatch());
        }
    }
}
