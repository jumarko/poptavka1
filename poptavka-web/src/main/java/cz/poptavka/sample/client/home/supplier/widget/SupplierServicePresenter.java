package cz.poptavka.sample.client.home.supplier.widget;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.HomeEventBus;

@Presenter(view = SupplierServiceView.class)
public class SupplierServicePresenter extends
    LazyPresenter<SupplierServicePresenter.SupplierServiceInterface, HomeEventBus> {

    public interface SupplierServiceInterface extends LazyView {

        Widget getWidgetView();
    }

    public void onInitServiceForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
