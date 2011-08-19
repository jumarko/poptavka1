package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.event.dom.client.ChangeEvent;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.user.UserEventBus;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.ArrayList;

@Presenter(view = AdminSuppliersView.class)//, multiple = true)
public class AdminSuppliersPresenter
        extends BasePresenter<AdminSuppliersPresenter.SuppliersViewInterface, UserEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    public interface SuppliersViewInterface { //extends LazyView {
        //******** ROOT SECTION **********

        HorizontalPanel getRootSection();

        SingleSelectionModel getSelectionRootModel();

        void displayRootCategories(int columns, ArrayList<CategoryDetail> categories);

        //******** CHILD SECTION **********
        HTMLPanel getChildSection();

        ListBox getLocalityList();

        int getPageSize();

        ListBox getPageSizeCombo();

        String getSelectedLocality();

        AsyncDataProvider<SupplierDetail> getDataProvider();

        void setDataProvider(AsyncDataProvider<SupplierDetail> dataProvider);

        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        CellList getSuppliersList();

        SimplePager getPager();

        Widget getWidgetView();

        SingleSelectionModel getSelectionCategoryModel();

        SingleSelectionModel getSelectionSupplierModel();

        SplitLayoutPanel getSplitter();

        void displaySubCategories(int columns, ArrayList<CategoryDetail> categories);

        void displaySuppliersDetail(UserDetail userDetail);
    }
    private int columns = 4;
    private Long lastUsedCategoryID = null;
    private ArrayList<Long> historyTokens = new ArrayList<Long>();

    @Override
    public void bind() {
        view.getSelectionRootModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionRootModel().getSelectedObject();

                if (selected != null) {
                    eventBus.atDisplaySuppliers(selected.getId());
                }
            }
        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    eventBus.setCategoryID(selected.getId());
                    historyTokens.add(selected.getId());
                    eventBus.addToPath(selected);
                    eventBus.getSubCategories(selected.getId());
                }
            }
        });
        view.getSelectionSupplierModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                UserDetail selected = (UserDetail) view.getSelectionSupplierModel().getSelectedObject();

                if (selected != null) {
                    view.displaySuppliersDetail(selected);
                }
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                eventBus.getSuppliersByCategoryLocality(1, view.getPageSize(),
                        lastUsedCategoryID, view.getSelectedLocality());
            }
        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getSuppliersList().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSize());

                view.getSuppliersList().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);
            }
        });
    }
    private AsyncDataProvider dataProvider = new AsyncDataProvider<SupplierDetail>() {

        @Override
        protected void onRangeChanged(HasData<SupplierDetail> display) {
            //just for initializing cellTable
            //will be implemented later, when allDemandsCount value will be retrieved
        }
    };
    private int start = 0;
//    private long totalFound = 0;

    public void onCreateAsyncDataProviderSupplier(final long totalFound) {
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<SupplierDetail>() {

            @Override
            protected void onRangeChanged(HasData<SupplierDetail> display) {
                display.setRowCount((int) totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                if (view.getLocalityList().getSelectedIndex() == 0) {
                    eventBus.getSuppliersByCategory(start, start + length, lastUsedCategoryID);
                } else {
                    eventBus.getSuppliersByCategoryLocality(start, start + length,
                            lastUsedCategoryID, view.getSelectedLocality());
                }
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getSuppliersList());
    }

    public void onInvokeAdminSuppliers() {
        eventBus.loadingShow(MSGS.loading());

//        view.getChildSection().setVisible(false);
//        view.getRootSection().setVisible(true);

        eventBus.getCategories();

        eventBus.displayAdminContent(view.getWidgetView());
//        eventBus.setBodyWidget(view.getWidgetView());
    }

    /**
     * Called from suppliers display widget root by click on one of root category.
     * Methods initialize Path, fills category list of Suppliers widget.
     * @param category
     */
    public void onAtDisplaySuppliers(Long category) {
        eventBus.loadingShow(MSGS.loading());
        //
        view.getChildSection().setVisible(true);
        view.getRootSection().setVisible(false);

        view.getPath().clear();
        historyTokens.clear();
        //
        eventBus.getSubCategories(category);
        eventBus.getLocalities();

        Hyperlink link = new Hyperlink("root", "!public/addToPath?root");
        view.addPath(link);
        eventBus.displayContent(view.getWidgetView());
//        eventBus.setBodyWidget(view.getWidgetView());
    }

    public void onDisplayRootcategories(ArrayList<CategoryDetail> rootCategories) {
        view.displayRootCategories(COLUMNS, rootCategories);
        eventBus.loadingHide();
    }

    /**
     * Cares for displaying sub categories of chosen parent category and displaying suppliers for
     * chosen selection of sub categories and parent category.
     * @param subcategories
     * @param parentCategory
     */
    public void onDisplaySubCategories(ArrayList<CategoryDetail> subcategories, Long parentCategory) {
        view.displaySubCategories(columns, subcategories);
        lastUsedCategoryID = parentCategory;
        if (parentCategory != null) {
            eventBus.getSuppliersCountByCategory(parentCategory);
        }
        eventBus.loadingHide();
    }

    public void onDisplaySuppliers(ArrayList<UserDetail> list) {
        view.getSuppliersList().setRowData(start, list);
    }

    /**
     * Fills locality listBox with given list of localities.
     * @param list - data (localities)
     */
    public void onSetLocalityData(final ArrayList<LocalityDetail> list) {
        final ListBox box = view.getLocalityList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("All localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(),
                            String.valueOf(list.get(i).getId()));
                }
                box.setSelectedIndex(0);
                LOGGER.info("Locality List filled");
            }
        });
    }

    public void onAddToPath(CategoryDetail categoryDetail) {
        Hyperlink link = new Hyperlink(" -> " + categoryDetail.getName(),
                "!public/addToPath?" + categoryDetail.getId());
        view.addPath(link);
    }

    public void onRemoveFromPath(Long id) {
        int idx = historyTokens.indexOf(id);
        for (int i = 0; i < historyTokens.size();) {
            if (i > idx) {
                view.getPath().remove(i + 1);
                historyTokens.remove(i);
            } else {
                i++;
            }
        }
    }

    public void onSetCategoryID(Long categoryID) {
        this.lastUsedCategoryID = categoryID;
    }
}