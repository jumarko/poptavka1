package cz.poptavka.sample.client.home.suppliers;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;

@Presenter(view = RootView.class)
public class RootPresenter
        extends BasePresenter<RootPresenter.RootViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    public interface RootViewInterface {

        HorizontalPanel getPanel();

        Widget getWidgetView();

        SingleSelectionModel getSelectionModel();

        void displayRootCategories(int columns, ArrayList<CategoryDetail> categories);
    }

    public void bind() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionModel().getSelectedObject();

                if (selected != null) {
                    eventBus.atDisplaySuppliers(selected.getId());
                }
            }
        });
    }

    public void onAtSuppliers() {
        eventBus.loadingShow(MSGS.loading());

        eventBus.getCategories();

        eventBus.setBodyWidget(view.getWidgetView());
    }

    public void onDisplayRootcategories(ArrayList<CategoryDetail> rootCategories) {
        view.displayRootCategories(COLUMNS, rootCategories);
        eventBus.loadingHide();
    }
}