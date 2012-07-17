package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientContestsView extends Composite
        implements ClientContestsPresenter.ClientContestsLayoutInterface {

    private static ClientContestsLayoutViewUiBinder uiBinder = GWT.create(ClientContestsLayoutViewUiBinder.class);

    interface ClientContestsLayoutViewUiBinder extends UiBinder<Widget, ClientContestsView> {
    }
    /**************************************************************************/
    /* DemandTable Attrinbutes                                                */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientProjectDetail> demandGrid;
    //table column width constatnts
    private static final int TITLE_COL_WIDTH = 50;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int FINNISH_DATE_COL_WIDTH = 30;
    private static final int VALID_TO_DATE_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    SimplePager demandPager;
    @UiField(provided = true)
    ListBox demandPageSize;
    /**************************************************************************/
    /* DemandContestTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientProjectContestantDetail> contestGrid;
    //table columns
    private Header checkHeader;
    private Column<ClientProjectContestantDetail, Boolean> checkColumn;
    private Column<ClientProjectContestantDetail, Boolean> starColumn;
    private Column<ClientProjectContestantDetail, String> supplierNameColumn;
    private Column<ClientProjectContestantDetail, String> priceColumn;
    private Column<ClientProjectContestantDetail, String> receiveDateColumn;
    private Column<ClientProjectContestantDetail, String> ratingColumn;
    private Column<ClientProjectContestantDetail, String> acceptedDateColumn;
    //table column width constatnts
    private static final int SUPPLIER_NAME_COL_WIDTH = 20;
    private static final int RECEIVED_COL_WIDTH = 30;
    private static final int RATING_COL_WIDTH = 20;
    private static final int ACCEPTED_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    SimplePager contestPager;
    @UiField(provided = true)
    ListBox contestPageSize;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    //table handling buttons
    @UiField
    Button acceptBtn, denyBtn, replyBtn, backBtn;
    @UiField(provided = true)
    ListBox actions;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label demandTitlelabel;
    @UiField
    HorizontalPanel demandHeader;
    @UiField
    VerticalPanel contestHeader;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //init pagesize lsit
        demandPageSize = new ListBox();
        demandPageSize.addItem("5");
        demandPageSize.addItem("10");
        demandPageSize.addItem("15");
        demandPageSize.addItem("20");
        demandPageSize.addItem("25");
        demandPageSize.addItem("30");
        demandPageSize.setSelectedIndex(2);

        contestPageSize = new ListBox();
        contestPageSize.addItem("5");
        contestPageSize.addItem("10");
        contestPageSize.addItem("15");
        contestPageSize.addItem("20");
        contestPageSize.addItem("25");
        contestPageSize.addItem("30");
        contestPageSize.setSelectedIndex(2);

        actions = new ListBox();
        actions.addItem(Storage.MSGS.action());
        actions.addItem(Storage.MSGS.read());
        actions.addItem(Storage.MSGS.unread());
        actions.addItem(Storage.MSGS.star());
        actions.addItem(Storage.MSGS.unstar());
        actions.setSelectedIndex(0);

        initDemandTable();
        initContestTable();

        initWidget(uiBinder.createAndBindUi(this));
        setContestTableVisible(false);
    }

    /**
     * Initialize this example.
     */
    private void initDemandTable() {
        List<String> gridColumns = Arrays.asList(
                new String[]{
                    "title", "price", "finnishDate", "validTo"
                });
        // Create a CellTable.
        demandGrid = new UniversalAsyncGrid<ClientProjectDetail>(gridColumns);
        demandGrid.setWidth("800px");
        demandGrid.setHeight("500px");
//        demandGrid.setLoadingIndicator(new Label("Loading, please wait ..."));
        demandGrid.setRowCount(Integer.valueOf(demandPageSize.getItemText(demandPageSize.getSelectedIndex())), true);
        demandGrid.setPageSize(getDemandPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientProjectDetail> selectionModel =
                new SingleSelectionModel<ClientProjectDetail>(ClientProjectDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        demandPager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        demandPager.setDisplay(demandGrid);

        initDemandTableColumns();
    }

    /**
     * Initialize this example.
     */
    private void initContestTable() {
        List<String> gridColumns = Arrays.asList(
                new String[]{"supplierName", "price", "receivedDate", "rating", "acceptedDate"});
        // Create a CellTable.
        contestGrid = new UniversalAsyncGrid<ClientProjectContestantDetail>(gridColumns);
        contestGrid.setWidth("800px");
        contestGrid.setHeight("500px");

        contestGrid.setRowCount(Integer.valueOf(
                contestPageSize.getItemText(contestPageSize.getSelectedIndex())), true);
        contestGrid.setPageSize(getContestPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientProjectContestantDetail> selectionModel =
                new MultiSelectionModel<ClientProjectContestantDetail>(ClientProjectContestantDetail.KEY_PROVIDER);
        contestGrid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<ClientProjectContestantDetail>createCheckboxManager());

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        contestPager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        contestPager.setDisplay(contestGrid);

        initContestTableColumns();
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand title column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, TITLE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectDetail clientDetail = (ClientProjectDetail) object;
                        return ClientProjectDetail.displayHtml(clientDetail.getTitle(), clientDetail.isRead());
                    }
                });

        // Demand price column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), true, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectDetail clientDetail = (ClientProjectDetail) object;
                        return ClientProjectDetail.displayHtml(clientDetail.getPrice(), clientDetail.isRead());
                    }
                });

        // Finnish date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.finnishDate(), true, FINNISH_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectDetail clientDetail = (ClientProjectDetail) object;
                        return ClientProjectDetail.displayHtml(
                                formatter.format(clientDetail.getEndDate()),
                                clientDetail.isRead());
                    }
                });

        // Valid-to date column
        demandGrid.addColumn(
                new TextCell(), Storage.MSGS.validTo(), true, VALID_TO_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectDetail clientDetail = (ClientProjectDetail) object;
                        return ClientProjectDetail.displayHtml(
                                formatter.format(clientDetail.getValidToDate()),
                                clientDetail.isRead());
                    }
                });
    }

    /**
     * Create all columns to the grid.
     */
    public void initContestTableColumns() {
        // CheckBox column
        checkHeader = new Header<Boolean>(new CheckboxCell()) {

            @Override
            public Boolean getValue() {
                return false;
            }
        };
        checkColumn = contestGrid.addCheckboxColumn(checkHeader);
        // Star Column
        starColumn = contestGrid.addStarColumn();
        // Demand title column
        supplierNameColumn = contestGrid.addColumn(
                contestGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectContestantDetail.displayHtml(detail.getSupplierName(), detail.isRead());
                    }
                });

        // Demand price column
        priceColumn = contestGrid.addColumn(
                contestGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectContestantDetail.displayHtml(detail.getPrice(), detail.isRead());
                    }
                });

        // Received date column
        receiveDateColumn = contestGrid.addColumn(
                contestGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, RECEIVED_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectDetail.displayHtml(
                                formatter.format(detail.getReceiveDate()),
                                detail.isRead());
                    }
                });
        // Rating columne
        ratingColumn = contestGrid.addColumn(
                contestGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.accepted(), true, RATING_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectDetail.displayHtml(
                                Integer.toString(detail.getRating()),
                                detail.isRead());
                    }
                });
        // Accepted Date column
        acceptedDateColumn = contestGrid.addColumn(
                contestGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.accepted(), true, ACCEPTED_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectDetail.displayHtml(
                                formatter.format(detail.getAcceptedDate()),
                                detail.isRead());
                    }
                });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table
    @Override
    public UniversalAsyncGrid<ClientProjectContestantDetail> getContestGrid() {
        return contestGrid;
    }

    @Override
    public UniversalAsyncGrid<ClientProjectDetail> getDemandGrid() {
        return demandGrid;
    }

    //Columns
    @Override
    public Column<ClientProjectContestantDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, String> getPriceColumn() {
        return priceColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, String> getReceivedColumn() {
        return receiveDateColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    @Override
    public Column<ClientProjectContestantDetail, String> getAcceptedColumn() {
        return acceptedDateColumn;
    }

    //Header
    @Override
    public Header getCheckHeader() {
        return checkHeader;
    }

    //Buttons
    @Override
    public Button getBackBtn() {
        return backBtn;
    }

    @Override
    public Button getAcceptBtn() {
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

    //ListBox
    @Override
    public ListBox getActions() {
        return actions;
    }

    //Nemusi byt override nie?
    @Override
    public int getDemandPageSize() {
        return Integer.valueOf(demandPageSize.getItemText(demandPageSize.getSelectedIndex()));
    }

    //Nemusi byt override nie?
    @Override
    public int getContestPageSize() {
        return Integer.valueOf(contestPageSize.getItemText(contestPageSize.getSelectedIndex()));
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<ClientProjectContestantDetail> set = getSelectedMessageList();
        Iterator<ClientProjectContestantDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientProjectContestantDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientProjectContestantDetail> model =
                (MultiSelectionModel<ClientProjectContestantDetail>) contestGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setContestTableVisible(boolean visible) {
        demandGrid.setVisible(!visible);
        demandPager.setVisible(!visible);
        demandPageSize.setVisible(!visible);
        demandHeader.setVisible(!visible);

        contestGrid.setVisible(visible);
        contestPager.setVisible(visible);
        contestPageSize.setVisible(visible);
        contestHeader.setVisible(visible);
    }

    @Override
    public void setDemandTitleLabel(String text) {
        demandTitlelabel.setText(text);
    }
}
