package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierAccountInfoView.class, multiple = true)
public class SupplierAccountInfoPresenter
        extends LazyPresenter<SupplierAccountInfoPresenter.SupplierAccountInfoInterface, SupplierCreationEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SupplierAccountInfoInterface extends LazyView {

        Widget getWidgetView();

        HasValueChangeHandlers<String> getEmailBox();

        StatusIconLabel getMailStatus();

        boolean validateEmail();

        BusinessUserDetail updateBusinessUserDetail(BusinessUserDetail user);

    }

    @Override
    public void bindView() {
        view.getEmailBox().addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> value) {
                if (view.validateEmail()) {
                    view.getMailStatus().setVisible(true);
                    eventBus.checkFreeEmail(value.getValue());
                } else {
                    view.getMailStatus().setVisible(false);
                }
            }
        });
    }

    public void initSupplierForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        view.getMailStatus().setVisible(true);
        if (isAvailable) {
            view.getMailStatus().setState(State.ACCEPT_16);
            view.getMailStatus().setDescription(MSGS.formUserRegMailAvailable());
        } else {
            view.getMailStatus().setState(State.ERROR_16);
            view.getMailStatus().setDescription(MSGS.formUserRegMailNotAvailable());
        }
    }
}