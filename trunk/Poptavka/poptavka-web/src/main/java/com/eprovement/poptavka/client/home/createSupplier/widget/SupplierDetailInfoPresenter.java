package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierDetailInfoView.class, multiple = true)
public class SupplierDetailInfoPresenter
        extends LazyPresenter<SupplierDetailInfoPresenter.SupplierDetailInfoInterface, SupplierCreationEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SupplierDetailInfoInterface extends LazyView {

        Widget getWidgetView();

        BusinessUserDetail updateBusinessUserDetail(BusinessUserDetail user);

        SimplePanel getAddressHolder();
    }

    public void initSupplierForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
        eventBus.initAddressWidget(view.getAddressHolder());
    }
}
