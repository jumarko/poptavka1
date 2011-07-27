package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.event.dom.client.ChangeEvent;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
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
import cz.poptavka.sample.client.home.HomeEventBus;

import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Presenter(view = DisplaySuppliersView.class)
public class DisplaySuppliersPresenter
        extends BasePresenter<DisplaySuppliersPresenter.DisplaySuppliersViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface DisplaySuppliersViewInterface { //extends LazyView {

        ListBox getLocalityList();

        int getPageSize();

        ListBox getPageSizeCombo();

        Long getSelectedLocality();

        AsyncDataProvider<SupplierDetail> getDataProvider();

        void setDataProvider(AsyncDataProvider<SupplierDetail> dataProvider);

        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        CellList getList();

        SimplePager getPager();

        HorizontalPanel getPanel();

        Widget getWidgetView();

        SingleSelectionModel getSelectionModel();

        SplitLayoutPanel getSplitter();

        void displaySubCategories(int columns, ArrayList<CategoryDetail> categories);
    }
    private int columns = 4;
    private Long lastUsedCategoryID = 0L;
    private ArrayList<Long> historyTokens = new ArrayList<Long>();

    public void bind() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionModel().getSelectedObject();
                //does it really need to be?
//                if (selected.getParentName().equals("") || selected.getParentName() == null) {
//                    root = true;
//                }
                if (selected != null) {
                    eventBus.getSuppliersCountCategory(selected.getId());
                    eventBus.setCategoryID(Long.valueOf(selected.getId()));
                    historyTokens.add(selected.getId());
                    eventBus.addToPath(selected);
                    eventBus.getSubCategories(Long.toString(selected.getId()));
//                    eventBus.getSuppliers(Long.valueOf(selected.getId()), null);
                }
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                //TODO Martin najdenych dodavatelov podla categorie filtruj aj podla zvolenej lokality
                //TODO - 10 prerobit na view.get pocet v tabulke
                eventBus.getSuppliers2(1, 10, lastUsedCategoryID, view.getSelectedLocality());
            }
        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getList().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSize());

                view.getList().setRowCount(newPage, true);

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
                if (view.getSelectedLocality() == null) {
                    eventBus.getSuppliers3(start, start + length, lastUsedCategoryID);
                } else {
                    eventBus.getSuppliers2(start, start + length, lastUsedCategoryID, view.getSelectedLocality());
                }
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getList());
    }

//    public void onSetTotalFound(long count) {
//        this.totalFound = count;
//    }

    public void onSetCategoryID(Long categoryID) {
        this.lastUsedCategoryID = categoryID;
    }

//    private String category = "root";

    public void onAddToPath(CategoryDetail categoryDetail) {
        Hyperlink link = new Hyperlink(" -> " + categoryDetail.getName(),
                "!public/addToPath?" + categoryDetail.getId());
//        link.addStyleName("cz.poptavka.sample.client.resources.StyleResource.layout.homeMenu");
        view.addPath(link);
    }

    public void onAtSuppliers() {
        eventBus.loadingShow(MSGS.loading());
        root = true;
        //
        view.getSplitter().getWidget(1).setVisible(false);
        view.getSplitter().getWidget(2).setVisible(false);
        view.getPath().setVisible(false);
        //
        view.getPath().clear();
        historyTokens.clear();
        //
        eventBus.getCategories();
        eventBus.getLocalities();

        Hyperlink link = new Hyperlink("root", "!public/addToPath?root");
        view.addPath(link);
        eventBus.setBodyWidget(view.getWidgetView());
    }

    public void onDisplaySubcategories(ArrayList<CategoryDetail> subcategories) {
        if (root) {
            root = false;
        } else {
            view.getSplitter().getWidget(1).setVisible(true);
            view.getSplitter().getWidget(2).setVisible(true);
            view.getPath().setVisible(true);
        }
        view.displaySubCategories(columns, subcategories);
        eventBus.loadingHide();
    }
    private List<Supplier> suppliers = Arrays.asList(
            new Supplier(), new Supplier(), new Supplier(), new Supplier());
    private boolean root = true;

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

//    public void onGetSuppliers(Long category, Long locality) {
//        LOGGER.info("GetSuppliers(long,long): '" + category + "' '" + locality + "'");
//        //TODO martin - dorobit, vsetko podla view + asynch data provider.
//        if (locality == null) {
//            eventBus.getSuppliers3(1, view.getPageSize(), category);
//        } else {
//            eventBus.getSuppliers2(1, view.getPageSize(), category, locality);
//        }
//    }

    public void onDisplaySuppliers(ArrayList<SupplierDetail> list) {
        view.getList().setRowData(start, list);
    }
}