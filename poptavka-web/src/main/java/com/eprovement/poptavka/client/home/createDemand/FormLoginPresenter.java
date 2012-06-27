package com.eprovement.poptavka.client.home.createDemand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.main.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;

@Presenter(view = FormLoginView.class)
public class FormLoginPresenter extends LazyPresenter<FormLoginPresenter.FormLoginInterface, DemandCreationEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface FormLoginInterface extends LazyView, ProvidesValidate {

        Widget getWidgetView();

        HasClickHandlers getLoginBtn();

        HasClickHandlers getRegisterBtn();

        String getLogin();

        String getPassword();

        boolean isValid();
    }

    @Override
    public void bindView() {
        view.getLoginBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                if (view.isValid()) {
                    processLogin();
                }
            }
        });
        view.getRegisterBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.toggleLoginRegistration();
                eventBus.initRegistrationForm((SimplePanel) view.getWidgetView().getParent());
            }
        });
    }

    /** Init widget. **/
    public void onInitLoginForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    private void processLogin() {
        String login = view.getLogin();
        String pass = view.getPassword();
        // TODO hash passwd

        //verifying existing user
        eventBus.verifyExistingClient(new BusinessUserDetail(view.getLogin(), view.getPassword()));
        //signal event
        eventBus.loadingShow(MSGS.progressLogingUser());
    }
}
