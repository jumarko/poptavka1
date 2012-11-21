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

//Ak multiple, musi pouzivat eventBus.addHandler
//vyuzitie multiple:
/* e.g. one screen would have two chart components with different supporting data.
 * The presenter responsibility was the same for both, display a bar chart from
 * the under laying data (a simple multi-column query).
 * vid: http://www.summa-tech.com/blog/2011/04/27/mvp4g-multiple-presenters-part-1/*/
//co nie je nas pripad
@Presenter(view = SupplierInfoView.class) //, multiple = true)  -- nemusi multiple - ved pouziva len tu
public class SupplierInfoPresenter
        extends LazyPresenter<SupplierInfoPresenter.SupplierInfoInterface, SupplierCreationEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SupplierInfoInterface extends LazyView {

        Widget getWidgetView();

        HasValueChangeHandlers<String> getEmailBox();

        StatusIconLabel getMailStatus();

        boolean validateEmail();

        BusinessUserDetail createSupplier();

        SimplePanel getAddressHolder();
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

    public void onInitSupplierForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
        eventBus.initAddressWidget(view.getAddressHolder());
    }

    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        view.getMailStatus().setVisible(true);
        if (isAvailable) {
            view.getMailStatus().setState(State.ACCEPT_16);
            view.getMailStatus().setDescription(MSGS.mailAvailable());
        } else {
            view.getMailStatus().setState(State.ERROR_16);
            view.getMailStatus().setDescription(MSGS.mailNotAvailable());
        }
    }
}
