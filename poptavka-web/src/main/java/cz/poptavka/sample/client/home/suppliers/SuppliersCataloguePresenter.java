package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.view.client.SelectionChangeEvent;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;

@Presenter(view = SuppliersCatalogueView.class)
public class SuppliersCataloguePresenter
        extends BasePresenter<SuppliersCataloguePresenter.SuppliersCatalogueViewInterface, DisplaySuppliersEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SuppliersCatalogueViewInterface { //extends LazyView {

//        FlexTable getTable();
        HorizontalPanel getPanel();

        SingleSelectionModel getSelectionModel();

        Widget getWidgetView();

//        void setFlexTable(int columns, ArrayList<CategoryDetail> categories);

        void createCatalogue(int columns, ArrayList<CategoryDetail> categories);
    }

    public void bind() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                CategoryDetail selected = (CategoryDetail) view.getSelectionModel().getSelectedObject();
                if (selected != null) {
                    eventBus.getCategory(Long.toString(selected.getId()));
                }
            }
        });
//        view.getTable().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                LOGGER.info("1");
//                Cell cell = view.getTable().getCellForEvent(event);
//                LOGGER.info("2");
//                HorizontalPanel panel = (HorizontalPanel) view.getTable()
//                        .getWidget(cell.getRowIndex(), cell.getCellIndex());
//                LOGGER.info("3");
////                Window.alert(Integer.toString(panel.getWidgetCount()));
//                Label label =  (Label) panel.getLayoutData();
//                Widget w = panel.getWidget(0);
//                LOGGER.info("4");
//                Window.alert((w).toString());
//                        //+ " " + ((Label) panel.getWidget(2)).getText());
//                LOGGER.info("5");
//            }
//        });
    }

    private SupplierInfoPresenter presenter = null;

    public void onAtSuppliers() {
        eventBus.getCategories();

        eventBus.setBodyWidget(view.getWidgetView());
    }


    /**
     * Fills category listBox with given list of localities.
     * @param list - data (categories)
     */
    public void onSetCategoryData(final ArrayList<CategoryDetail> list) {
//        view.setFlexTable(4, list);
        view.createCatalogue(4, list);
    }
}
