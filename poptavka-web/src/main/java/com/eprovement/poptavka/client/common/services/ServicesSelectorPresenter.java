package com.eprovement.poptavka.client.common.services;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGrid;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

@Presenter(view = ServicesSelectorView.class, multiple = true)
public class ServicesSelectorPresenter extends LazyPresenter<
        ServicesSelectorPresenter.SupplierServiceInterface, RootEventBus> {

    public interface SupplierServiceInterface extends LazyView {

        void setServices(ArrayList<ServiceDetail> services);

        ServiceDetail getSelectedService();

        UniversalGrid getTable();

        Widget getWidgetView();
    }

    public void initServicesWidget(SimplePanel embedToWidget) {
        eventBus.getServices();
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onSetServices(ArrayList<ServiceDetail> services) {
        view.setServices(services);
    }
}
