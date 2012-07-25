package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientAssignedProjectsView extends Composite
        implements ClientAssignedProjectsPresenter.ClientAssignedProjectsLayoutInterface {

    private static ClientAssignedProjectsLayoutViewUiBinder uiBinder =
            GWT.create(ClientAssignedProjectsLayoutViewUiBinder.class);

    interface ClientAssignedProjectsLayoutViewUiBinder extends UiBinder<Widget, ClientAssignedProjectsView> {
    }
    /**************************************************************************/
    /* DemandContestTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientProjectContestantDetail> contestantsGrid;
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
    private static final int PRICE_COL_WIDTH = 30;
    private static final int RECEIVED_COL_WIDTH = 30;
    private static final int RATING_COL_WIDTH = 20;
    private static final int ACCEPTED_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    SimplePager contestantsPager;
    @UiField(provided = true)
    ListBox contestantsPageSize;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
    //table handling buttons
    @UiField
    Button closeBtn, replyBtn;
    @UiField(provided = true)
    ListBox actions;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    VerticalPanel contestantsHeader;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        contestantsPageSize = new ListBox();
        contestantsPageSize.addItem("5");
        contestantsPageSize.addItem("10");
        contestantsPageSize.addItem("15");
        contestantsPageSize.addItem("20");
        contestantsPageSize.addItem("25");
        contestantsPageSize.addItem("30");
        contestantsPageSize.setSelectedIndex(2);

        actions = new ListBox();
        actions.addItem(Storage.MSGS.action());
        actions.addItem(Storage.MSGS.read());
        actions.addItem(Storage.MSGS.unread());
        actions.addItem(Storage.MSGS.star());
        actions.addItem(Storage.MSGS.unstar());
        actions.setSelectedIndex(0);

        initContestantsTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize this example.
     */
    private void initContestantsTable() {
        List<String> gridColumns = Arrays.asList(
                new String[]{"supplierName", "price", "receivedDate", "rating", "acceptedDate"});
        // Create a CellTable.
        contestantsGrid = new UniversalAsyncGrid<ClientProjectContestantDetail>(gridColumns);
        contestantsGrid.setWidth("800px");
        contestantsGrid.setHeight("500px");

        contestantsGrid.setRowCount(Integer.valueOf(
                contestantsPageSize.getItemText(contestantsPageSize.getSelectedIndex())), true);
        contestantsGrid.setPageSize(getContestPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientProjectContestantDetail> selectionModel =
                new MultiSelectionModel<ClientProjectContestantDetail>(ClientProjectContestantDetail.KEY_PROVIDER);
        contestantsGrid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<ClientProjectContestantDetail>createCheckboxManager());

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        contestantsPager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        contestantsPager.setDisplay(contestantsGrid);

        initContestantsTableColumns();
    }

    /**
     * Create all columns to the grid.
     */
    public void initContestantsTableColumns() {
        // CheckBox column
        checkHeader = new Header<Boolean>(new CheckboxCell()) {

            @Override
            public Boolean getValue() {
                return false;
            }
        };
        checkColumn = contestantsGrid.addCheckboxColumn(checkHeader);
        // Star Column
        starColumn = contestantsGrid.addStarColumn();
        // Demand title column
        supplierNameColumn = contestantsGrid.addColumn(
                contestantsGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectContestantDetail.displayHtml(detail.getSupplierName(), detail.isRead());
                    }
                });

        // Demand price column
        priceColumn = contestantsGrid.addColumn(
                contestantsGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        ClientProjectContestantDetail detail = (ClientProjectContestantDetail) object;
                        return ClientProjectContestantDetail.displayHtml(detail.getPrice(), detail.isRead());
                    }
                });

        // Received date column
        receiveDateColumn = contestantsGrid.addColumn(
                contestantsGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, RECEIVED_COL_WIDTH,
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
        ratingColumn = contestantsGrid.addColumn(
                contestantsGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.accepted(), true, RATING_COL_WIDTH,
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
        acceptedDateColumn = contestantsGrid.addColumn(
                contestantsGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.accepted(), true, ACCEPTED_COL_WIDTH,
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
    public UniversalAsyncGrid<ClientProjectContestantDetail> getContestantsGrid() {
        return contestantsGrid;
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
    public Button getCloseBtn() {
        return closeBtn;
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
    public int getContestPageSize() {
        return Integer.valueOf(contestantsPageSize.getItemText(contestantsPageSize.getSelectedIndex()));
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
                (MultiSelectionModel<ClientProjectContestantDetail>) contestantsGrid.getSelectionModel();
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
}
