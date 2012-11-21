package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationEventBus;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

@Presenter(view = SupplierServiceView.class)
public class SupplierServicePresenter extends
        LazyPresenter<SupplierServicePresenter.SupplierServiceInterface, SupplierCreationEventBus> {

    public interface SupplierServiceInterface extends LazyView {

        void setServices(ArrayList<ServiceDetail> services);

        int getSelectedService();

        Widget getWidgetView();
    }

    public void onInitServiceForm(SimplePanel embedToWidget) {
        eventBus.getServices();
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onSetServices(ArrayList<ServiceDetail> services) {
        view.setServices(services);
    }
}
