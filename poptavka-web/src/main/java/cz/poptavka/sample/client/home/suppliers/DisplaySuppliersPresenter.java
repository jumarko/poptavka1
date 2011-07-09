package cz.poptavka.sample.client.home.suppliers;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;

@Presenter(view = DisplaySuppliersView.class)
public class DisplaySuppliersPresenter
    extends BasePresenter<DisplaySuppliersPresenter.DisplaySuppliersViewInterface, DisplaySuppliersEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface DisplaySuppliersViewInterface { //extends LazyView {

        Label getPath();

        FlexTable getTable();

        CellList getList();

        SimplePager getPager();

        Widget getWidgetView();
    }

    private SupplierInfoPresenter presenter = null;

    public void onAtSuppliers() {
        eventBus.setBodyWidget(view.getWidgetView());
        LOGGER.info("Display Suppliers presenter");
    }
}
