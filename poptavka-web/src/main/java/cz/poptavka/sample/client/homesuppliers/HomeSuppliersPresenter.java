package cz.poptavka.sample.client.homesuppliers;

import com.google.gwt.event.dom.client.ChangeEvent;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.resources.StyleResource;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Presenter(view = HomeSuppliersView.class)
public class HomeSuppliersPresenter
        extends BasePresenter<HomeSuppliersPresenter.SuppliersViewInterface, HomeSuppliersEventBus> {

    private final static Logger LOGGER = Logger.getLogger("DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    public interface SuppliersViewInterface { //extends LazyView {
        //******** ROOT SECTION **********

        HorizontalPanel getRootSection();

        SingleSelectionModel getSelectionRootModel();

        void displayRootCategories(int columns, ArrayList<CategoryDetail> categories);

        //******** CHILD SECTION **********
        HTMLPanel getChildSection();

        Label getFilterLabel();
//        CellList getCategoryList();
//        ListBox getLocalityList();

        int getPageSize();

        ListBox getPageSizeCombo();

//        String getSelectedLocality();
        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        DataGrid getDataGrid();

        SimplePager getPager();

        Widget getWidgetView();

        SingleSelectionModel getSelectionCategoryModel();

        SingleSelectionModel getSelectionSupplierModel();

        SplitLayoutPanel getSplitter();

        void displaySubCategories(int columns, ArrayList<CategoryDetail> categories);

        void displaySuppliersDetail(FullSupplierDetail userDetail);

        void hideSuppliersDetail();
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
                    eventBus.atDisplaySuppliers(selected);
                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                        searchDataHolder.initHomeSuppliers();
                    }
                    searchDataHolder.getHomeSuppliers().setSupplierCategory(
                            new CategoryDetail(selected.getId(), selected.getName()));
                }
            }
        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    view.hideSuppliersDetail();
                    view.getSelectionSupplierModel().setSelected(
                            view.getSelectionSupplierModel().getSelectedObject(), false);
                    eventBus.setCategoryID(selected.getId());
                    historyTokens.add(selected.getId());
                    eventBus.addToPath(selected);
                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                        searchDataHolder.initHomeSuppliers();
                    }
                    searchDataHolder.getHomeSuppliers().setSupplierCategory(
                            new CategoryDetail(selected.getId(), selected.getName()));
                    eventBus.getSubCategories(selected.getId());
                }
            }
        });
        view.getSelectionSupplierModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullSupplierDetail selected = (FullSupplierDetail) view.getSelectionSupplierModel().getSelectedObject();

                if (selected != null) {
                    view.displaySuppliersDetail(selected);
                }
            }
        });
//        view.getLocalityList().addChangeHandler(new ChangeHandler() {
//
//            @Override
//            public void onChange(ChangeEvent event) {
//                view.hideSuppliersDetail();
////                view.getSuppliersList().setRowData(0, new ArrayList<FullSupplierDetail>());
////                eventBus.resetPager(totalFound);
//                eventBus.getSuppliersCountByCategoryLocality(lastUsedCategoryID, view.getSelectedLocality());
////                eventBus.getSuppliersByCategoryLocality(1, view.getPageSize(),
////                        lastUsedCategoryID, view.getSelectedLocality());
//            }
//        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getDataGrid().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSizeCombo().
                        getItemText(view.getPageSizeCombo().getSelectedIndex()));

                view.getDataGrid().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);

//                int page = view.getPager().getPageStart() / view.getPageSize();
//                view.getPager().setPageStart(page * view.getPageSize());
//                view.getPager().setPageSize(view.getPageSize());
            }
        });

    }

    public void onStart() {
        // TODO praso
    }

    public void onForward() {
        // TODO praso - switch css to selected menu button.
        //eventBus.selectCompanyMenu();
    }
    private SearchModuleDataHolder searchDataHolder = null;

    public void onInitHomeSuppliersModule(SearchModuleDataHolder searchDataHolder, String location) {
        Storage.setCurrentlyLoadedView("homeSuppliers");
        view.getPath().clear();
        view.addPath(new Hyperlink("root", "!public/addToPath?root"));
        this.searchDataHolder = searchDataHolder;
        eventBus.loadingShow(MSGS.loading());

        if (searchDataHolder == null) {

            view.getChildSection().setVisible(false);
            view.getRootSection().setVisible(true);
            //display root categories on whole page
            view.getFilterLabel().setVisible(false);
            eventBus.getCategories();
        } else {
            view.getFilterLabel().setVisible(true);
            view.getChildSection().setVisible(true);
            view.getRootSection().setVisible(false);

            if (searchDataHolder.getHomeSuppliers().getSupplierCategory() == null) {
                eventBus.getSubCategories(null);
            } else {
                eventBus.getSubCategories(searchDataHolder.getHomeSuppliers().getSupplierCategory().getId());
            }
        }

        if (location.equals("home")) {
            Storage.setCurrentlyLoadedView("homeSuppliers");
            eventBus.setBodyHolderWidget(view.getWidgetView());
        } else if (location.equals("user")) {
            Storage.setCurrentlyLoadedView("userSuppliers");
            eventBus.setUserBodyHolderWidget(view.getWidgetView());
        }
    }

    public void onRootWithSearchDataHolder() {
        view.getFilterLabel().setVisible(true);
        view.getChildSection().setVisible(true);
        view.getRootSection().setVisible(false);

        if (searchDataHolder.getHomeSuppliers().getSupplierCategory() == null) {
            eventBus.getSubCategories(null);
        } else {
            eventBus.getSubCategories(searchDataHolder.getHomeSuppliers().getSupplierCategory().getId());
        }
    }

    public void onAtSuppliers() {
// TODO praso - it shouldn't be necessary to call setBodyWidget since we use autodisplay feature
//        eventBus.setBodyWidget(view.getWidgetView());
    }

    /**
     * Called from suppliers display widget root by click on one of root category.
     * Methods initialize Path, fills category list of Suppliers widget.
     * @param category
     */
    public void onAtDisplaySuppliers(CategoryDetail categoryDetail) {
        eventBus.loadingShow(MSGS.loading());
        //
        view.getChildSection().setVisible(true);
        view.getRootSection().setVisible(false);

        view.getPath().clear();
        historyTokens.clear();
        //
        eventBus.getSubCategories(categoryDetail.getId());
//        eventBus.getLocalities();

        view.addPath(new Hyperlink("root", "!public/addToPath?root"));
        view.addPath(new Hyperlink(categoryDetail.getName(),
                "!public/addToPath?" + Long.toString(categoryDetail.getId())));
// TODO praso - it shouldn't be necessary to call setBodyWidget since we use autodisplay feature
//        eventBus.setBodyWidget(view.getWidgetView());
    }
//    public void onResetDisplaySuppliersPager(int totalFoundNew) {
//        this.totalFound = totalFoundNew;
//        view.getDataGrid().setPageSize(0);
//        view.getPager().setPage(0);
//        if (!dataProviderInitialized) {
//            this.dataProvider.addDataDisplay(view.getDataGrid());
//            dataProviderInitialized = true;
//        }
//        this.createAsyncSortHandler();
//    }
    private int start = 0;
//    private int totalFound = 0;
    private int length = 0;
//    private boolean dataProviderInitialized = false;
//    public void onCreateAsyncDataProviderSupplier(final long totalFound) {
//        this.start = 0;
    private AsyncDataProvider dataProvider = null;

    public void onCreateAsyncDataProvider(final int totalFound) {
        this.start = 0;
//        view.getDataGrid().setPageSize(0);
//        view.getPager().setPage(0);
//        this.dataProvider = new AsyncDataProvider<FullDemandDetail>() {

//    private AsyncDataProvider dataProvider = new AsyncDataProvider<SupplierDetail>() {
        this.dataProvider = new AsyncDataProvider<SupplierDetail>() {

            @Override
            protected void onRangeChanged(HasData<SupplierDetail> display) {
//                view.getDataGrid().setPageSize(view.getPageSize());
                display.setRowCount(totalFound);
                if (totalFound == 0) {
                    return;
                }
                start = display.getVisibleRange().getStart();
                length = display.getVisibleRange().getLength();
//            if (searchDataHolder == null) {
//            if (view.getLocalityList().getSelectedIndex() == 0) {
//                eventBus.getSuppliersByCategory(start, start + length, lastUsedCategoryID);
//            } else {
                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.ASC);
                eventBus.getSuppliers(start, start + length, searchDataHolder, orderColumns);
//            }
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        this.createAsyncSortHandler();
    }
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "businessUser.businessUserData.companyName", "overalRating", "", ""
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void createAsyncSortHandler() {
        AsyncHandler sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<FullSupplierDetail, String> column = (Column<FullSupplierDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);
//TODO Uncomment - iba chcem zistit, ci sa to bude stale volat 2x
                eventBus.getSuppliers(start, start + length, searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
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
//        if (parentCategory == null) { //root
        eventBus.getSuppliersCount(searchDataHolder);
//        } else {
//            eventBus.getSuppliersCountByCategory(parentCategory);
//        }
        eventBus.loadingHide();
    }

    public void onDisplaySuppliers(List<FullSupplierDetail> list) {
//        view.getSuppliersList().setRowCount(0, true);
//        view.getDataGrid().setRowData(start, new ArrayList<FullSupplierDetail>());
        view.getDataGrid().redraw();
        view.getDataGrid().setRowData(start, list);
    }

    /**
     * Fills locality listBox with given list of localities.
     * @param list - data (localities)
     */
//    public void onSetLocalityData(final ArrayList<LocalityDetail> list) {
//        final ListBox box = view.getLocalityList();
//        box.clear();
//        box.setVisible(true);
//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//
//            @Override
//            public void execute() {
//                box.addItem("All localities...");
//                for (int i = 0; i < list.size(); i++) {
//                    box.addItem(list.get(i).getName(), list.get(i).getCode());
//                }
//                box.setSelectedIndex(0);
//                LOGGER.info("Locality List filled");
//            }
//        });
//    }
    public void onAddToPath(CategoryDetail categoryDetail) {
        Hyperlink link = new Hyperlink(" -> " + categoryDetail.getName(),
                "!public/addToPath?" + categoryDetail.getId());
        link.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
        view.addPath(link);
    }

    public void onRemoveFromPath(Long id) {
//view.getSuppliersList().setRowData(0, new ArrayList<FullSupplierDetail>());
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