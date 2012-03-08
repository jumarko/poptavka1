package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.demands.DemandModuleEventBus;
//import cz.poptavka.sample.client.user.widget.DevelDetailWrapperPresenter;
import cz.poptavka.sample.client.user.widget.DevelDetailWrapperPresenter;
import cz.poptavka.sample.client.user.widget.grid.UniversalGrid;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demandsModule.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.demandsModule.ClientOfferDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Presenter(view = ClientOffers.class)
public class ClientOffersPresenter extends LazyPresenter<ClientOffersPresenter.IList, DemandModuleEventBus> {

    public interface IList extends LazyView {

        Widget getWidgetView();

        int getDemandPageSize();

        int getOffersPageSize();

        //control buttons getters
//        Button getReadBtn();
//
//        Button getUnreadBtn();
//
//        Button getStarBtn();
//
//        Button getUnstarBtn();
        Button getAccpetBtn();

        Button getDenyBtn();

        Button getReplyBtn();

        Button getBackBtn();

        Label getChosenDemandTitle();

        ListBox getActionList();

        void displayClientOffers();

        void displayClientOfferMessages();

        //table getters
        void initOffersTable();

        DataGrid<ClientDemandDetail> getDemandGrid();

        UniversalGrid<ClientOfferDetail> getOfferGrid();

        ListDataProvider<ClientDemandDetail> getDemandDataProvider();

        ListDataProvider<ClientOfferDetail> getOfferDataProvider();

        SingleSelectionModel<ClientDemandDetail> getSelectionModelDemandTable();

        List<Long> getSelectedIdList();

        Set<ClientDemandDetail> getSelectedMessageList();

        //detail wrapper
        SimplePanel getWrapperPanel();
    }
    //viewType
    private ViewType type = ViewType.EDITABLE;
    private DevelDetailWrapperPresenter detailSection = null;
    //remove this annotation for production
    @SuppressWarnings("unused")
    private boolean initialized = false;
    private SearchModuleDataHolder searchDataHolder;

    /** Defines button actions. */
    @Override
    public void bindView() {
        view.getSelectionModelDemandTable().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getChosenDemandTitle().setText(view.getSelectionModelDemandTable().getSelectedObject().getTitle());
                eventBus.requestClientsOfferMessages(
                        view.getSelectionModelDemandTable().getSelectedObject().getDemandId());
            }
        });
        view.getBackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.filterClientOffersCount(searchDataHolder);
//                eventBus.requestClientsOfferMessages(0L);
            }
        });
//        view.getReadBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
////                internalReadUpdate(true);
//                updateReadStatus(view.getSelectedIdList(), true);
//            }
//        });
//        view.getUnreadBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
////                internalReadUpdate(false);
//                updateReadStatus(view.getSelectedIdList(), false);
//            }
//        });
//        view.getStarBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
////                internalStarUpdate(true);
//                updateStarStatus(view.getSelectedIdList(), true);
//            }
//        });
//        view.getUnstarBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
////                internalStarUpdate(false);
//                updateStarStatus(view.getSelectedIdList(), false);
//            }
//        });
        view.getActionList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActionList().getSelectedIndex()) {
                    case 1: // mark as read
                        updateReadStatus(view.getSelectedIdList(), true);
                        break;
                    case 2: // mark as unread
                        updateReadStatus(view.getSelectedIdList(), false);
                        break;
                    case 3: // add star
                        updateStarStatus(view.getSelectedIdList(), true);
                        break;
                    case 4: // remove star
                        updateStarStatus(view.getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
                view.getActionList().setSelectedIndex(0);
            }
        });
    }

    /**
     * Init view and fetch new supplier's offers. offer request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitClientOffers(SearchModuleDataHolder filter) {
//        commented code is from production code
//        if (!initialized) {
        Storage.setCurrentlyLoadedView("clientOffers");
        searchDataHolder = filter;
//        eventBus.requestClientsOffers();
//        orderColumns.clear();
//        orderColumns.put(columnNames[0], OrderType.ASC);
        eventBus.filterClientOffersCount(searchDataHolder);
//        eventBus.requestClientsOfferMessages(0L);
//        }
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (detailSection == null) {
            detailSection = eventBus.addHandler(DevelDetailWrapperPresenter.class);
            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
        initialized = true;
    }
    private AsyncDataProvider dataProvider = null;
    private int start = 0;

    public void onCreateAsyncClientDemandDataProvider(final int resultCount) {
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<ClientDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<ClientDemandDetail> display) {
                display.setRowCount(resultCount);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.DESC);
                eventBus.filterClientOffers(start, start + length, searchDataHolder, orderColumns);

//                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDemandGrid());
        this.createAsyncSortHandler();
    }
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "title", "price", "endDate", "validToDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDemandGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<ClientDemandDetail, String> column = (Column<ClientDemandDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDemandGrid().getColumnIndex(column)), orderType);

                //view.getPageSize()
                eventBus.filterClientOffers(start, 10, searchDataHolder, orderColumns);
            }
        };
        view.getDemandGrid().addColumnSortHandler(sortHandler);
    }

//    private AsyncDataProvider dataProviderMessages = null;
//    private int startMessages = 0;
//
//    public void onCreateAsyncClientOfferMessagesDataProvider(final int resultCount) {
//        this.startMessages = 0;
//        this.dataProviderMessages = new AsyncDataProvider<ClientDemandDetail>() {
//
//            @Override
//            protected void onRangeChanged(HasData<ClientDemandDetail> display) {
//                display.setRowCount(resultCount);
//                startMessages = display.getVisibleRange().getStart();
//                int length = display.getVisibleRange().getLength();
//
//                orderColumnsMessages.clear();
//                orderColumnsMessages.put(gridColumnsMessages.get(0), OrderType.DESC);
// eventBus.filterClientOffers(startMessages, startMessages + length, searchDataHolder, orderColumnsMessages);
//
////                eventBus.loadingHide();
//            }
//        };
//        this.dataProviderMessages.addDataDisplay(view.getOfferGrid());
//        this.createAsyncSortHandlerMessages();
//    }
//    private AsyncHandler sortHandlerMessages = null;
//    private Map<String, OrderType> orderColumnsMessages = new HashMap<String, OrderType>();
//    //list of grid columns, used to sort them. First must by blank (checkbox in table)
//    private final String[] columnNamesMessages = new String[]{
//        "title", "price", "endDate", "validToDate"
//    };
//    private List<String> gridColumnsMessages = Arrays.asList(columnNamesMessages);
//
//    public void createAsyncSortHandlerMessages() {
//        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
//        sortHandlerMessages = new AsyncHandler(view.getDemandGrid()) {
//
//            @Override
//            public void onColumnSort(ColumnSortEvent event) {
//                orderColumnsMessages.clear();
//                OrderType orderType = OrderType.DESC;
//                if (event.isSortAscending()) {
//                    orderType = OrderType.ASC;
//                }
//                Column<ClientDemandDetail, String> column = (Column<ClientDemandDetail, String>) event.getColumn();
//                if (column == null) {
//                    return;
//                }
//                orderColumnsMessages.put(gridColumnsMessages.get(
//                        view.getDemandGrid().getColumnIndex(column)), orderType);
//
//                //view.getPageSize()
//                eventBus.filterClientOffers(startMessages, 10, searchDataHolder, orderColumnsMessages);
//            }
//        };
//        view.getOfferGrid().addColumnSortHandler(sortHandlerMessages);
//    }
    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from offerModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
        detailSection.develRemoveReplyWidget();
        eventBus.removeHandler(detailSection);
    }

    /**
     * @param data
     */
    public void onResponseClientsOffers(ArrayList<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsOffers");
        view.displayClientOffers();

        dataProvider.updateRowData(start, data);
        view.getDemandGrid().flush();
        view.getDemandGrid().redraw();
    }

    public void onResponseClientsOfferMessages(ArrayList<ClientOfferDetail> data) {
        GWT.log("++ onResponseClientsOffers");
        view.displayClientOfferMessages();

        if (data.size() > 0) {
            List<ClientOfferDetail> list = view.getOfferDataProvider().getList();
            list.clear();
            for (ClientOfferDetail d : data) {
                list.add(d);
            }
            view.getOfferDataProvider().refresh();
        } else {
            view.getOfferDataProvider().getList().clear();
            view.getOfferGrid().displayEmptyTable();
        }
    }
    //call eventBus to update READ status
    //called in ClickEvent of action button.

    public void updateReadStatus(List<Long> selectedIdList, boolean newStatus) {
        eventBus.requestReadStatusUpdate(selectedIdList, newStatus);
    }

    /**
     * Triggered by action button: read/unread button
     * When changing read of multiple offers by action button. Visible change has to be done manually;
     *
     * @param isRead
     */
    private void internalReadUpdate(boolean isRead) {
        Iterator<ClientDemandDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            ClientDemandDetail message = it.next();
            message.setRead(isRead);
        }
        view.getOfferDataProvider().refresh();
        view.getOfferGrid().redraw();
    }

    /**
     * Call eventBus to update STARRED status.
     * T
     * his method is called by clicking star image on single offer by default. Also is called in ClickEvent of
     * action button.
     * @param list
     * @param newStatus
     */
    public void updateStarStatus(List<Long> list, boolean newStatus) {
        eventBus.requestStarStatusUpdate(list, newStatus);
    }

    /**
     * Triggered by action button: star/unstar buttons
     * when starring multiple offers by action button. Visible change has to be done manually;
     *
     * @param isStared
     */
    private void internalStarUpdate(boolean isStared) {
        Iterator<ClientDemandDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            ClientDemandDetail message = it.next();
            message.setStarred(isStared);
        }
        view.getOfferDataProvider().refresh();
        view.getOfferGrid().redraw();
    }

    /**
     * New data are fetched from db.
     *
     * @param offerId ID for offer detail
     * @param messageId ID for offer related conversation
     * @param userMessageId ID for offer related conversation
     */
    public void displayDetailContent(Long offerId, long messageId, Long userMessageId) {
        //TODO
        // Uncoment if fake data no longer userd
        //
        //
        //can be solved by enum in future or can be accesed from storage class
//        detailSection.showLoading(DevelDetailWrapperPresenter.DETAIL);
//        eventBus.requestOfferDetail(offerId, type);
        //add conversation loading events and so on
//        detailSection.showLoading(DevelDetailWrapperPresenter.CHAT);
//        eventBus.requestChatForSupplierList(messageId, userMessageId, Storage.getUser().getUserId());
        //init default replyWidget
        //it is initalized now, because we do not need to have it visible before first offer selection
//        detailSection.initReplyWidget();
    }

    public void onSendMessageResponse(MessageDetail sentMessage, ViewType handlingType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(handlingType)) {
            detailSection.addConversationMessage(sentMessage);
        }
    }
}
