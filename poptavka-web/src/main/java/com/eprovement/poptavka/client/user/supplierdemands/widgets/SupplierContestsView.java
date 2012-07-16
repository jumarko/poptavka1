package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialProjectDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
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

public class SupplierContestsView extends Composite
        implements SupplierContestsPresenter.SupplierContestsLayoutInterface {

    private static SupplierContestsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierContestsLayoutViewUiBinder.class);

    interface SupplierContestsLayoutViewUiBinder extends UiBinder<Widget, SupplierContestsView> {
    }
    /**************************************************************************/
    /* DemandContestTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<SupplierPotentialProjectDetail> grid;
    //table columns
    private Header checkHeader;
    private Column<SupplierPotentialProjectDetail, Boolean> checkColumn;
    private Column<SupplierPotentialProjectDetail, Boolean> starColumn;
    private Column<SupplierPotentialProjectDetail, String> clientNameColumn;
    private Column<SupplierPotentialProjectDetail, String> ratingColumn;
    private Column<SupplierPotentialProjectDetail, String> priceColumn;
    private Column<SupplierPotentialProjectDetail, String> receiveDateColumn;
    private Column<SupplierPotentialProjectDetail, String> acceptedDateColumn;
    //table column width constatnts
    private static final int CLIENT_NAME_COL_WIDTH = 20;
    private static final int RATING_COL_WIDTH = 20;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int RECEIVED_COL_WIDTH = 30;
    private static final int ACCEPTED_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat("dd-mm-yyyy");
    //table handling buttons
    @UiField
    Button downloadOfferBtn, editOfferBtn, replyBtn;
    @UiField(provided = true)
    ListBox actions;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label titlelabel;
    @UiField
    VerticalPanel header;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        pageSize = new ListBox();
        pageSize.addItem("5");
        pageSize.addItem("10");
        pageSize.addItem("15");
        pageSize.addItem("20");
        pageSize.addItem("25");
        pageSize.addItem("30");
        pageSize.setSelectedIndex(2);

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
        grid = new UniversalAsyncGrid<SupplierPotentialProjectDetail>(gridColumns);
        grid.setWidth("800px");
        grid.setHeight("500px");

        grid.setRowCount(Integer.valueOf(
                pageSize.getItemText(pageSize.getSelectedIndex())), true);
        grid.setPageSize(getPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<SupplierPotentialProjectDetail> selectionModel =
                new MultiSelectionModel<SupplierPotentialProjectDetail>(SupplierPotentialProjectDetail.KEY_PROVIDER);
        grid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<SupplierPotentialProjectDetail>createCheckboxManager());

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(grid);

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
        checkColumn = grid.addCheckboxColumn(checkHeader);
        // Star Column
        starColumn = grid.addStarColumn();
        // Demand title column
        clientNameColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.client(), true, CLIENT_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                        return SupplierPotentialProjectDetail.displayHtml(detail.getClientName(), detail.isRead());
                    }
                });
        // Rating column
        ratingColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.rating(), true, RATING_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                        return ClientProjectDetail.displayHtml(
                                Integer.toString(detail.getRating()),
                                detail.isRead());
                    }
                });
        // Demand price column
        priceColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                        return SupplierPotentialProjectDetail.displayHtml(detail.getPrice(), detail.isRead());
                    }
                });
        // Received date column
        receiveDateColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, RECEIVED_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                        return ClientProjectDetail.displayHtml(
                                formatter.format(detail.getReceivedDate()),
                                detail.isRead());
                    }
                });
        // Accepted date column
        acceptedDateColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, ACCEPTED_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
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
    public UniversalAsyncGrid<SupplierPotentialProjectDetail> getGrid() {
        return grid;
    }

    //Columns
    @Override
    public Column<SupplierPotentialProjectDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, String> getClientNameColumn() {
        return clientNameColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, String> getPriceColumn() {
        return priceColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, String> getReceivedColumn() {
        return receiveDateColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, String> getAcceptedColumn() {
        return receiveDateColumn;
    }

    @Override
    public Column<SupplierPotentialProjectDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    //Header
    @Override
    public Header getCheckHeader() {
        return checkHeader;
    }

    //Buttons
    @Override
    public Button getEditOfferBtn() {
        return editOfferBtn;
    }

    @Override
    public Button getDownloadOfferBtn() {
        return downloadOfferBtn;
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
    public int getPageSize() {
        return Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<SupplierPotentialProjectDetail> set = getSelectedMessageList();
        Iterator<SupplierPotentialProjectDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<SupplierPotentialProjectDetail> getSelectedMessageList() {
        MultiSelectionModel<SupplierPotentialProjectDetail> model =
                (MultiSelectionModel<SupplierPotentialProjectDetail>) grid.getSelectionModel();
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
    public void setTitleLabel(String text) {
        titlelabel.setText(text);
    }
}
