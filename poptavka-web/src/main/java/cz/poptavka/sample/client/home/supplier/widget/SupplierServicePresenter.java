package cz.poptavka.sample.client.home.supplier.widget;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.supplier.SupplierCreationEventBus;

@Presenter(view = SupplierServiceView.class)
public class SupplierServicePresenter extends
    LazyPresenter<SupplierServicePresenter.SupplierServiceInterface, SupplierCreationEventBus> {

    public interface SupplierServiceInterface extends LazyView {

        Widget getWidgetView();
    }

    public void initServiceForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
