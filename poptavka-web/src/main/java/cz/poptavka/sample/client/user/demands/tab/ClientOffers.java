package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import com.mvp4g.client.view.ReverseViewInterface;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.demands.tab.ClientOffersPresenter.IList;
import cz.poptavka.sample.client.user.widget.grid.ColumnFactory;
import cz.poptavka.sample.client.user.widget.grid.UniversalGrid;
import cz.poptavka.sample.shared.domain.demandsModule.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.demandsModule.ClientOfferDetail;
import java.util.Arrays;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class ClientOffers extends Composite implements ReverseViewInterface<ClientOffersPresenter>, IList {

    private static ClientOfferListUiBinder uiBinder = GWT.create(ClientOfferListUiBinder.class);

    interface ClientOfferListUiBinder extends UiBinder<Widget, ClientOffers> {
    }
    //attrribute preventing repeated loading of offer detail, when clicked on the same offer
    private long lastOpenedoffer = -1;
    //table handling buttons
    @UiField
    ListBox actionList;
    @UiField(provided = true)
    ListBox pageSizeComboDemands, pageSizeComboOfferMessages;
    @UiField
    Button acceptBtn, denyBtn, replyBtn, backBtn;
    //DataGridattributes
    @UiField(provided = true)
    DataGrid<ClientDemandDetail> demandGrid;
    @UiField(provided = true)
    UniversalGrid<ClientOfferDetail> offerGrid;
    @UiField(provided = true)
    SimplePager demandPager;
    @UiField(provided = true)
    SimplePager offersPager;
    //presenter
    private ClientOffersPresenter presenter;
    //detailWrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    DockLayoutPanel demandsPanel, offersPanel;
    @UiField
    Label chosenDemandTitle;
    private SingleSelectionModel<ClientDemandDetail> selectionModelDemandTable;

    @Override
    public void setPresenter(ClientOffersPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ClientOffersPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        pageSizeComboDemands = new ListBox();
        pageSizeComboDemands.addItem("5");
        pageSizeComboDemands.addItem("10");
        pageSizeComboDemands.addItem("15");
        pageSizeComboDemands.setSelectedIndex(1);

        pageSizeComboOfferMessages = new ListBox();
        pageSizeComboOfferMessages.addItem("5");
        pageSizeComboOfferMessages.addItem("10");
        pageSizeComboOfferMessages.addItem("15");
        pageSizeComboOfferMessages.setSelectedIndex(1);

        this.initDemandsTable();
        this.initOffersTable();

        initWidget(uiBinder.createAndBindUi(this));

        actionList.addItem("more actions");
        actionList.addItem("marked as read");
        actionList.addItem("marked as unread");
        actionList.addItem("add star");
        actionList.addItem("remove star");

        displayClientOffers();
    }

    public void initDemandsTable() {
        //offerGrid init
        demandGrid = new DataGrid<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
//        demandGrid.setDataProvider(new ListDataProvider<ClientDemandDetail>());
        demandGrid.setEmptyTableWidget(new Label("No data available."));
        demandGrid.setPageSize(this.getDemandPageSize());
        // Add a selection model so we can select cells.
        selectionModelDemandTable = new SingleSelectionModel(ClientDemandDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModelDemandTable);
        //init table
        initDemandTableColumns();

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        demandPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        demandPager.setDisplay(demandGrid);

    }

    @Override
    public void initOffersTable() {
        //offerGrid init
        offerGrid = new UniversalGrid<ClientOfferDetail>(ClientOfferDetail.KEY_PROVIDER);
        offerGrid.setEmptyTableWidget(new Label("No data available."));
        demandGrid.setPageSize(this.getOffersPageSize());
        // Add a selection model so we can select cells.
        final SelectionModel<ClientOfferDetail> selectionModelOfferTable =
                new MultiSelectionModel<ClientOfferDetail>(ClientOfferDetail.KEY_PROVIDER);
        offerGrid.setSelectionModel(selectionModelOfferTable,
                DefaultSelectionEventManager.<ClientOfferDetail>createCheckboxManager());
        //init table
        initOfferTableColumns(selectionModelOfferTable);

        //TODO Martin dorobit demandPager pre offers
//        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        offersPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        offersPager.setDisplay(offerGrid);

    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initDemandTableColumns() {
        ListHandler sort = new ListHandler(new ArrayList());
        //init column factory
        ColumnFactory<ClientDemandDetail> factory = new ColumnFactory<ClientDemandDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<ClientDemandDetail, String> action = createCommonDemandUpdater();

        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
        //because this is working with Date instead of String
        FieldUpdater<ClientDemandDetail, Date> dateAction = createDemandDateUpdater();


// **** State collumn
//        Column<ClientDemandDetail, DemandStatus> stateColumn = factory.createStateColumn();
//        //TODO
//        //testing if assigning style in columnFactory works - works well 7.11.11 Beho
//        //but keep here for reference
//        //starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
//        demandGrid.setColumnWidth(stateColumn, ColumnFactory.WIDTH_40, Unit.PX);
//        demandGrid.addColumn(stateColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

// **** demand title column
        Column<ClientDemandDetail, String> demandTitleCol =
                factory.createTitleColumn(sort, true); //demandGrid.getSortHandler(), true);
        demandTitleCol.setFieldUpdater(action);
        demandGrid.addColumn(demandTitleCol, Storage.MSGS.title());

// **** offer price column
        Column<ClientDemandDetail, String> priceCol = factory.createPriceColumn(sort); //demandGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        demandGrid.addColumn(priceCol, Storage.MSGS.price());

// **** end/finish date column
        Column<ClientDemandDetail, Date> endDateCol =
                factory.createDateColumn(sort, //demandGrid.getSortHandler(),
                ColumnFactory.DATE_FINISHED);
        endDateCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(endDateCol, Storage.MSGS.endDate());

// **** accepted column
        Column<ClientDemandDetail, Date> validToDateCol =
                factory.createDateColumn(sort, //demandGrid.getSortHandler(),
                ColumnFactory.DATE_VALIDTO);
        validToDateCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(validToDateCol, Storage.MSGS.validTo());
    }

    private FieldUpdater<ClientDemandDetail, Date> createDemandDateUpdater() {
        return new FieldUpdater<ClientDemandDetail, Date>() {

            @Override
            public void update(int index, ClientDemandDetail object, Date value) {
                //for pure display detail action
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
                //display offerGrid
            }
        };
    }

    private FieldUpdater<ClientDemandDetail, String> createCommonDemandUpdater() {
        return new FieldUpdater<ClientDemandDetail, String>() {

            @Override
            public void update(int index, ClientDemandDetail object, String value) {
//                TableDisplay obj = (TableDisplay) object;
                object.setRead(true);
                offerGrid.redraw();
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
                //display offerGrid
            }
        };
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initOfferTableColumns(final SelectionModel<ClientOfferDetail> selectionModel) {
        //init column factory
        ColumnFactory<ClientOfferDetail> factory = new ColumnFactory<ClientOfferDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<ClientOfferDetail, String> action = createCommonOfferUpdater();

        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
        //because this is working with Date instead of String
        FieldUpdater<ClientOfferDetail, Date> dateAction = createOfferDateUpdater();

        //STAR COLUMN FIELD UPDATER
        FieldUpdater<ClientOfferDetail, Boolean> starUpdater = createOfferStarredUpdater();

// **** ROW selection column and set it's width to 40px.
        //contains custom header providing selecting all visible items
        final Header<Boolean> header = factory.createCheckBoxHeader();
        setHeaderUpdater(selectionModel, header);

        offerGrid.addColumn(factory.createCheckboxColumn(selectionModel), header);
        offerGrid.setColumnWidth(offerGrid.getColumn(ColumnFactory.COL_ZERO), ColumnFactory.WIDTH_40, Unit.PX);

// **** Star collumn with defined valueUpdater and custom style
        Column<ClientOfferDetail, Boolean> starColumn = factory.createStarColumn();
        starColumn.setFieldUpdater(starUpdater);
        //TODO
        //testing if assigning style in columnFactory works - works well 7.11.11 Beho
        //but keep here for reference
        //starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        offerGrid.setColumnWidth(starColumn, ColumnFactory.WIDTH_40, Unit.PX);
        offerGrid.addColumn(starColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

// **** offer name column
        Column<ClientOfferDetail, String> supplierNameCol =
                factory.createSenderColumn(offerGrid.getSortHandler(), true);
        supplierNameCol.setFieldUpdater(action);
        offerGrid.addColumn(supplierNameCol, Storage.MSGS.supplierName());

// **** offer price column
        Column<ClientOfferDetail, String> priceCol = factory.createPriceColumn(offerGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        offerGrid.addColumn(priceCol, Storage.MSGS.price());

// **** received date column
        Column<ClientOfferDetail, Date> receivedCol =
                factory.createDateColumn(offerGrid.getSortHandler(),
                ColumnFactory.DATE_RECEIVED);
        receivedCol.setFieldUpdater(dateAction);
        offerGrid.addColumn(receivedCol, Storage.MSGS.received());

// **** client rating column
        Column<ClientOfferDetail, String> ratingCol =
                factory.createRatingColumn(offerGrid.getSortHandler());
        ratingCol.setFieldUpdater(action);
        //implement img header
//        final Header<ImageResource> header = factory.createRatingImgHeader();
        offerGrid.addColumn(ratingCol, "Img");

// **** accepted column
        Column<ClientOfferDetail, Date> acceptedCol =
                factory.createDateColumn(offerGrid.getSortHandler(), ColumnFactory.DATE_ACCEPTED);
        acceptedCol.setFieldUpdater(dateAction);
        offerGrid.addColumn(acceptedCol, Storage.MSGS.accepted());
    }

    private void setHeaderUpdater(final SelectionModel<ClientOfferDetail> selectionModel, Header<Boolean> header) {
        //select
        header.setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<ClientOfferDetail> rows = offerGrid.getVisibleItems();
                for (ClientOfferDetail row : rows) {
                    selectionModel.setSelected(row, value);
                }

            }
        });
    }

    private FieldUpdater<ClientOfferDetail, Boolean> createOfferStarredUpdater() {
        return new FieldUpdater<ClientOfferDetail, Boolean>() {

            @Override
            public void update(int index, ClientOfferDetail object, Boolean value) {
//                TableDisplay obj = (TableDisplay) object;
                object.setStarred(!value);
                offerGrid.redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                presenter.updateStarStatus(Arrays.asList(item), !value);
            }
        };
    }

    private FieldUpdater<ClientOfferDetail, Date> createOfferDateUpdater() {
        return new FieldUpdater<ClientOfferDetail, Date>() {

            @Override
            public void update(int index, ClientOfferDetail object, Date value) {
                //for pure display detail action
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };
    }

    private FieldUpdater<ClientOfferDetail, String> createCommonOfferUpdater() {
        return new FieldUpdater<ClientOfferDetail, String>() {

            @Override
            public void update(int index, ClientOfferDetail object, String value) {
//                TableDisplay obj = (TableDisplay) object;
                object.setRead(true);
                offerGrid.redraw();
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };
    }

    @Override
    public void displayClientOffers() {
        demandGrid.setSize("600px", "300px");
        demandsPanel.setSize("600px", "300px");
        demandsPanel.setVisible(true);
        offersPanel.setVisible(false);
    }

    @Override
    public void displayClientOfferMessages() {
        offerGrid.setSize("600px", "300px");
        offersPanel.setSize("600px", "300px");
        demandsPanel.setVisible(false);
        offersPanel.setVisible(true);
    }

    @Override
    public Button getAccpetBtn() {
        return acceptBtn;
    }

    @Override
    public Button getDenyBtn() {
        return denyBtn;
    }

    @Override
    public Button getReplyBtn() {
        return replyBtn;
    }

    @Override
    public Button getBackBtn() {
        return backBtn;
    }
//    @Override
//    public Button getReadBtn() {
//        return readBtn;
//    }
//
//    @Override
//    public Button getUnreadBtn() {
//        return unreadBtn;
//    }
//
//    @Override
//    public Button getStarBtn() {
//        return starBtn;
//    }
//
//    @Override
//    public Button getUnstarBtn() {
//        return unstarBtn;
//    }

    @Override
    public ListBox getActionList() {
        return actionList;
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<ClientDemandDetail> set = getSelectedMessageList();
        Iterator<ClientDemandDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getDemandId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientDemandDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientDemandDetail> model =
                (MultiSelectionModel<ClientDemandDetail>) demandGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public int getDemandPageSize() {
        return Integer.valueOf(pageSizeComboDemands.getItemText(pageSizeComboDemands.getSelectedIndex()));
    }

    @Override
    public int getOffersPageSize() {
        return Integer.valueOf(pageSizeComboOfferMessages.getItemText(pageSizeComboOfferMessages.getSelectedIndex()));
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public SingleSelectionModel<ClientDemandDetail> getSelectionModelDemandTable() {
        return selectionModelDemandTable;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<ClientDemandDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public UniversalGrid<ClientOfferDetail> getOfferGrid() {
        return offerGrid;
    }

    @Override
    public ListDataProvider<ClientDemandDetail> getDemandDataProvider() {
//        return demandGrid.getDataProvider();
        return null;
    }

    @Override
    public ListDataProvider<ClientOfferDetail> getOfferDataProvider() {
        return offerGrid.getDataProvider();
    }

    @Override
    public Label getChosenDemandTitle() {
        return chosenDemandTitle;
    }
}
