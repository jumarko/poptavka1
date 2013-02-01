package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierAccountInfoView.class, multiple = true)
public class SupplierAccountInfoPresenter
        extends LazyPresenter<SupplierAccountInfoPresenter.SupplierAccountInfoInterface, SupplierCreationEventBus> {

    public interface SupplierAccountInfoInterface extends LazyView {

        Widget getWidgetView();

        ValidationMonitor getEmailBox();

        void initVisualFreeEmailCheck(Boolean isAvailable);

        BusinessUserDetail updateBusinessUserDetail(BusinessUserDetail user);
    }

    @Override
    public void bindView() {
        ((TextBox) view.getEmailBox().getWidget()).addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> value) {
                if (view.getEmailBox().isValid()) {
                    eventBus.checkFreeEmail(value.getValue());
                }
            }
        });
    }

    public void initSupplierForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        view.initVisualFreeEmailCheck(isAvailable);
    }
}
