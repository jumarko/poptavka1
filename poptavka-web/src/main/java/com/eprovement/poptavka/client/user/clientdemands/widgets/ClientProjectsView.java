package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
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

public class ClientProjectsView extends Composite
        implements ClientProjectsPresenter.ClientProjectsLayoutInterface {

    private static ClientProjectsLayoutViewUiBinder uiBinder = GWT.create(ClientProjectsLayoutViewUiBinder.class);

    interface ClientProjectsLayoutViewUiBinder extends UiBinder<Widget, ClientProjectsView> {
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
    /* DemandConversationTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientProjectConversationDetail> conversationGrid;
    //table columns
    private Header checkHeader;
    private Column<ClientProjectConversationDetail, Boolean> checkColumn;
    private Column<ClientProjectConversationDetail, Boolean> starColumn;
    private Column<ClientProjectConversationDetail, String> supplierNameColumn;
    private Column<ClientProjectConversationDetail, String> bodyPreviewColumn;
    private Column<ClientProjectConversationDetail, String> dateColumn;
    //table column width constatnts
    private static final int SUPPLIER_NAME_COL_WIDTH = 20;
    private static final int BODY_PREVIEW_COL_WIDTH = 30;
    private static final int DATE_COL_WIDTH = 20;
    //pager definition
    @UiField(provided = true)
    SimplePager conversationPager;
    @UiField(provided = true)
    ListBox conversationPageSize;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    //table handling buttons
    @UiField
    Button replyButton;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label demandTitlelabel;
    @UiField(provided = true)
    ListBox actions;
    @UiField
    HorizontalPanel demandHeader, conversationHeader;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //init pagesize lsit
        demandPageSize = new ListBox();
        demandPageSize.addItem("10");
        demandPageSize.addItem("20");
        demandPageSize.addItem("30");
        demandPageSize.setSelectedIndex(2);

        conversationPageSize = new ListBox();
        conversationPageSize.addItem("10");
        conversationPageSize.addItem("20");
        conversationPageSize.addItem("30");
        conversationPageSize.setSelectedIndex(2);

        actions = new ListBox();
        actions.addItem(Storage.MSGS.action());
        actions.addItem(Storage.MSGS.read());
        actions.addItem(Storage.MSGS.unread());
        actions.addItem(Storage.MSGS.star());
        actions.addItem(Storage.MSGS.unstar());
        actions.setSelectedIndex(0);

        initDemandTable();
        initConversationTable();

        initWidget(uiBinder.createAndBindUi(this));
        setConversationTableVisible(false);
    }

    /**
     * Initialize this example.
     */
    private void initDemandTable() {
        List<String> gridColumns = Arrays.asList(
                new String[]{
                    "status", "title", "price", "finnishDate", "validTo"
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
        demandPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        demandPager.setDisplay(demandGrid);

        initDemandTableColumns();
    }

    /**
     * Initialize this example.
     */
    private void initConversationTable() {
        List<String> gridColumns = Arrays.asList(new String[]{"supplierName", "body", "date"});
        // Create a CellTable.
        conversationGrid = new UniversalAsyncGrid<ClientProjectConversationDetail>(gridColumns);
        conversationGrid.setWidth("800px");
        conversationGrid.setHeight("500px");

        conversationGrid.setRowCount(Integer.valueOf(
                conversationPageSize.getItemText(conversationPageSize.getSelectedIndex())), true);
        conversationGrid.setPageSize(getConversationPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientProjectConversationDetail> selectionModel =
                new MultiSelectionModel<ClientProjectConversationDetail>(ClientProjectConversationDetail.KEY_PROVIDER);
        conversationGrid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<ClientProjectConversationDetail>createCheckboxManager());

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        conversationPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        conversationPager.setDisplay(conversationGrid);

        initConversationTableColumns();
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand Status column
        demandGrid.addStatusColumn(Storage.MSGS.status());
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
    public void initConversationTableColumns() {
        // CheckBox column
        checkHeader = new Header<Boolean>(new CheckboxCell()) {

            @Override
            public Boolean getValue() {
                return false;
            }
        };
        checkColumn = conversationGrid.addCheckboxColumn(checkHeader);
        // Star Column
        starColumn = conversationGrid.addStarColumn();
        // Demand title column
        supplierNameColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectConversationDetail detail = (ClientProjectConversationDetail) object;
                        return ClientProjectConversationDetail.displayHtml(detail.getSupplierName(), detail.isRead());
                    }
                });

        // Demand price column
        bodyPreviewColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.text(), false, BODY_PREVIEW_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectConversationDetail detail = (ClientProjectConversationDetail) object;
                        StringBuilder str = new StringBuilder();
                        str.append(((ClientProjectConversationDetail) object).getMessageDetail().getBody());
                        str.append("...");
                        return ClientProjectConversationDetail.displayHtml(str.toString(), detail.isRead());
                    }
                });

        // Date column
        dateColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.date(), true, DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectConversationDetail detail = (ClientProjectConversationDetail) object;
                        return ClientProjectConversationDetail.displayHtml(
                                formatter.format(detail.getDate()),
                                detail.isRead());
                    }
                });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    // Columns
    @Override
    public Header getCheckHeader() {
        return checkHeader;
    }

    @Override
    public Column<ClientProjectConversationDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    @Override
    public Column<ClientProjectConversationDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    @Override
    public Column<ClientProjectConversationDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    @Override
    public Column<ClientProjectConversationDetail, String> getBodyPreviewColumn() {
        return bodyPreviewColumn;
    }

    @Override
    public Column<ClientProjectConversationDetail, String> getDateColumn() {
        return dateColumn;
    }

    // Buttons
    @Override
    public Button getReplyButton() {
        return replyButton;
    }

    @Override
    public ListBox getActions() {
        return actions;
    }

    // Others
    @Override
    public UniversalAsyncGrid<ClientProjectDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public UniversalAsyncGrid<ClientProjectConversationDetail> getConversationGrid() {
        return conversationGrid;
    }

    //Nemusi byt override nie?
    @Override
    public int getDemandPageSize() {
        return Integer.valueOf(demandPageSize.getItemText(demandPageSize.getSelectedIndex()));
    }

    //Nemusi byt override nie?
    @Override
    public int getConversationPageSize() {
        return Integer.valueOf(conversationPageSize.getItemText(conversationPageSize.getSelectedIndex()));
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<ClientProjectConversationDetail> set = getSelectedMessageList();
        Iterator<ClientProjectConversationDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientProjectConversationDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientProjectConversationDetail> model =
                (MultiSelectionModel<ClientProjectConversationDetail>) conversationGrid.getSelectionModel();
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
    public void setConversationTableVisible(boolean visible) {
        demandGrid.setVisible(!visible);
        demandPager.setVisible(!visible);
        demandPageSize.setVisible(!visible);
        demandHeader.setVisible(!visible);

        conversationGrid.setVisible(visible);
        conversationPager.setVisible(visible);
        conversationPageSize.setVisible(visible);
        conversationHeader.setVisible(visible);
    }

    @Override
    public void setDemandTitleLabel(String text) {
        demandTitlelabel.setText(text);
    }
}
