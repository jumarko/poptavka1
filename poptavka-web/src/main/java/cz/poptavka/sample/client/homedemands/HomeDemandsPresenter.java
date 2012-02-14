/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.homedemands;

import com.google.gwt.core.client.GWT;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.widget.detail.DemandDetailView;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This presenter is to replace DemandsPresenter.java.
 *
 * @author praso
 */
@Presenter(view = HomeDemandsView.class)
public class HomeDemandsPresenter extends BasePresenter<
        HomeDemandsPresenter.HomeDemandsViewInterface, HomeDemandsEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomeDemandsPresenter.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface HomeDemandsViewInterface {

        DemandDetailView getDemandDetail();

        SimplePanel getDemandDetailPanel();

        Widget getWidgetView();

        Button getOfferBtn();

        ListBox getPageSizeCombo();

        int getPageSize();

        DataGrid<FullDemandDetail> getDataGrid();

        SimplePager getPager();

        Label getBannerLabel();

//        HTMLPanel getDemandView();
        SingleSelectionModel<FullDemandDetail> getSelectionModel();
    }

    /**
     * Bind objects and their action handlers.
     */
    @Override
    public void bind() {
        // Add a selection model to handle user selection.
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullDemandDetail selected = view.getSelectionModel().getSelectedObject();
                if (selected != null) {
                    view.getBannerLabel().setVisible(false);
                    view.getDemandDetailPanel().setVisible(true);
                    view.getOfferBtn().setVisible(true);
                    eventBus.setDemand(selected);
                } else {
                    view.getDemandDetailPanel().setVisible(false);
                    view.getOfferBtn().setVisible(false);
                    view.getBannerLabel().setVisible(true);
                }
            }
        });

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
            }
        });
    }

    public void onStart() {
        // TODO praso - probably history initialization will be here
    }

    public void onForward() {
        // TODO praso - here we can switch css for selected menu button
        //eventBus.selectHomeDemandsMenuButton();
    }
    private SearchModuleDataHolder searchDataHolder; //need to remember for asynchDataProvider if asking for more data

    public void onInitHomeDemandsModule(SearchModuleDataHolder searchDataHolder, String location) {
        orderColumns.clear();
        orderColumns.put(columnNames[0], OrderType.ASC);
        eventBus.filterDemandsCount(searchDataHolder, orderColumns);
//        }
        this.searchDataHolder = searchDataHolder;

        if (location.equals("home")) {
            Storage.setCurrentlyLoadedView("homeDemands");
            eventBus.setHomeBodyHolderWidget(view.getWidgetView());
        } else if (location.equals("user")) {
            Storage.setCurrentlyLoadedView("userDemands");
            eventBus.setUserBodyHolderWidget(view.getWidgetView());
        }
    }
    private AsyncDataProvider dataProvider = null;
    private int start = 0;

    public void onCreateAsyncDataProvider(final int resultCount) {
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<FullDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullDemandDetail> display) {
                display.setRowCount(resultCount);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.DESC);
                eventBus.filterDemands(start, start + length, searchDataHolder, orderColumns);

                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        this.createAsyncSortHandler();
    }
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "createdDate", "category", "title", "locality", "price"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<FullDemandDetail, String> column = (Column<FullDemandDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.filterDemands(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onDisplayDemands(List<FullDemandDetail> result) {
//        dataProvider.updateRowData(0, new ArrayList<FullDemandDetail>());
        dataProvider.updateRowData(start, result);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        eventBus.loadingHide();
    }

    public void onSetDemand(FullDemandDetail demand) {
        view.getDemandDetailPanel().setVisible(true);
        view.getOfferBtn().setVisible(true);
        view.getDemandDetail().setDemanDetail(demand);
//        view.setDemand(demand);
    }
}