package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
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

public class ClientDemandsView extends Composite
        implements ClientDemandsPresenter.ClientDemandsLayoutInterface {

    private static ClientDemandsLayoutViewUiBinder uiBinder = GWT.create(ClientDemandsLayoutViewUiBinder.class);

    interface ClientDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientDemandsView> {
    }
    /**************************************************************************/
    /* DemandTable Attributes                                                 */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientDemandDetail> demandGrid;
    //table column width constatnts
    private static final int TITLE_COL_WIDTH = 50;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int FINNISH_DATE_COL_WIDTH = 30;
    private static final int VALID_TO_DATE_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    UniversalPagerWidget demandPager;
    /**************************************************************************/
    /* DemandConversationTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientDemandConversationDetail> conversationGrid;
    //table columns
    private Header checkHeader;
    private Column<ClientDemandConversationDetail, Boolean> checkColumn;
    private Column<ClientDemandConversationDetail, Boolean> starColumn;
    private Column<ClientDemandConversationDetail, ImageResource> replyColumn;
    private Column<ClientDemandConversationDetail, String> supplierNameColumn;
    private Column<ClientDemandConversationDetail, String> bodyPreviewColumn;
    private Column<ClientDemandConversationDetail, String> dateColumn;
    //table column width constatnts
    private static final int SUPPLIER_NAME_COL_WIDTH = 20;
    private static final int BODY_PREVIEW_COL_WIDTH = 30;
    private static final int DATE_COL_WIDTH = 20;
    //pager definition
    @UiField(provided = true)
    UniversalPagerWidget conversationPager;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label demandTitlelabel;
    @UiField(provided = true)
    ListBox actions;
    @UiField
    HorizontalPanel demandHeader, conversationHeader;
    @UiField
    Button backBtn;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        actions = new ListBox();
        actions.addItem(Storage.MSGS.action());
        actions.addItem(Storage.MSGS.read());
        actions.addItem(Storage.MSGS.unread());
        actions.addItem(Storage.MSGS.star());
        actions.addItem(Storage.MSGS.unstar());
        actions.setSelectedIndex(0);

        initDemandTableAndPager();
        initConversationTableAndPager();
        initWidget(uiBinder.createAndBindUi(this));

        setConversationTableVisible(false);
    }

    /**
     * Initialize this example.
     */
    private void initDemandTableAndPager() {
        List<String> gridColumns = Arrays.asList(
                new String[]{
                    "status", "title", "price", "finnishDate", "validTo"
                });
        // Create a Pager.
        demandPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        demandGrid = new UniversalAsyncGrid<ClientDemandDetail>(
                gridColumns, demandPager.getPageSize(), resource);
        demandGrid.setWidth("800px");
        demandGrid.setHeight("500px");
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandDetail> selectionModel =
                new SingleSelectionModel<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel);

        // Bind pager to demandGrid
        demandPager.setDisplay(demandGrid);

        initDemandTableColumns();
    }

    /**
     * Initialize this example.
     */
    private void initConversationTableAndPager() {
        List<String> gridColumns = Arrays.asList(new String[]{"supplierName", "body", "date"});

        // Create a Pager.
        conversationPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        conversationGrid = new UniversalAsyncGrid<ClientDemandConversationDetail>(
                gridColumns, demandPager.getPageSize(), resource);
        conversationGrid.setWidth("800px");
        conversationGrid.setHeight("500px");

        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandConversationDetail> selectionModel =
                new MultiSelectionModel<ClientDemandConversationDetail>(ClientDemandConversationDetail.KEY_PROVIDER);
        conversationGrid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<ClientDemandConversationDetail>createCheckboxManager());

        // Bind pager to Table
        conversationPager.setDisplay(conversationGrid);

        initConversationTableColumns();
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand Status column
        demandGrid.addDemandStatusColumn(Storage.MSGS.status());
        // Demand title column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, TITLE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(clientDetail.getTitle(), clientDetail.isRead());
                    }
                });

        // Demand price column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), true, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(clientDetail.getPrice(), clientDetail.isRead());
                    }
                });

        // Finnish date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.finnishDate(), true, FINNISH_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
                                formatter.format(clientDetail.getEndDate()),
                                clientDetail.isRead());
                    }
                });

        // Valid-to date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.validTo(), true, VALID_TO_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
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
        // Reply Column
        replyColumn = conversationGrid.addIconColumn(
                Storage.RSCS.images().replyImage(),
                Storage.MSGS.replyExplanationText());
        // Demand title column
        supplierNameColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandConversationDetail detail = (ClientDemandConversationDetail) object;
                        return ClientDemandConversationDetail.displayHtml(detail.getSupplierName(), detail.isRead());
                    }
                });

        // Demand price column
        bodyPreviewColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.text(), false, BODY_PREVIEW_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandConversationDetail detail = (ClientDemandConversationDetail) object;
                        StringBuilder str = new StringBuilder();
                        str.append(((ClientDemandConversationDetail) object).getMessageDetail().getBody());
                        str.append("...");
                        return ClientDemandConversationDetail.displayHtml(str.toString(), detail.isRead());
                    }
                });

        // Date column
        dateColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.date(), true, DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandConversationDetail detail = (ClientDemandConversationDetail) object;
                        return ClientDemandConversationDetail.displayHtml(
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
    public Column<ClientDemandConversationDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    @Override
    public Column<ClientDemandConversationDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    public Column<ClientDemandConversationDetail, ImageResource> getReplyColumn() {
        return replyColumn;
    }

    @Override
    public Column<ClientDemandConversationDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    @Override
    public Column<ClientDemandConversationDetail, String> getBodyPreviewColumn() {
        return bodyPreviewColumn;
    }

    @Override
    public Column<ClientDemandConversationDetail, String> getDateColumn() {
        return dateColumn;
    }

    @Override
    public Button getBackBtn() {
        return backBtn;
    }

    @Override
    public ListBox getActions() {
        return actions;
    }

    //Pagers
    @Override
    public SimplePager getDemandPager() {
        return demandPager.getPager();
    }

    @Override
    public SimplePager getConversationPager() {
        return conversationPager.getPager();
    }

    // Others
    @Override
    public UniversalAsyncGrid<ClientDemandDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public UniversalAsyncGrid<ClientDemandConversationDetail> getConversationGrid() {
        return conversationGrid;
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<ClientDemandConversationDetail> set = getSelectedMessageList();
        Iterator<ClientDemandConversationDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientDemandConversationDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientDemandConversationDetail> model =
                (MultiSelectionModel<ClientDemandConversationDetail>) conversationGrid.getSelectionModel();
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
    //netreba lebo vonversationTableVisible setuje viditelnost oboch tabuliek
    @Override
    public void setDemandTableVisible(boolean visible) {
        demandGrid.setVisible(visible);
//        demandGrid.redraw();
        //Potrebne??? - ano - ak dam spat, musim deselectovat
        if (!visible) {
            SingleSelectionModel selectionModel = (SingleSelectionModel) demandGrid.getSelectionModel();
            if (selectionModel.getSelectedObject() != null) {
                selectionModel.setSelected(selectionModel.getSelectedObject(), false);
            }
        }
        demandHeader.setVisible(visible);
    }

    @Override
    public void setConversationTableVisible(boolean visible) {
        conversationGrid.setVisible(visible);
//        conversationGrid.redraw();
        conversationHeader.setVisible(visible);
    }

    @Override
    public void setDemandTitleLabel(String text) {
        demandTitlelabel.setText(text);
    }
}
