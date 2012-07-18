package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This widget was created mainly for ClientAssignedProjects, SupplierPotentialProjects,
 * SupplierContests, SupplierAssignedProjects widgets. Those listed widgets use similar
 * tables, therefore this widget was design to cover all common functionality as:
 * UniversalAsyncGrid - see UniversalAsyncGrid class
 * MultiSelectionModel
 * SimplePager
 * PageSize list box selection for selecting different number of visible items.
 *
 * As for this widget has no presenter, action handlers for column field updater
 * must be defined in widget's presenter that is using this one.
 *
 * @author Martin
 */
public class UniversalTableWidget extends Composite {

    private static UniversalTableWidgetUiBinder uiBinder =
            GWT.create(UniversalTableWidgetUiBinder.class);

    interface UniversalTableWidgetUiBinder extends UiBinder<Widget, UniversalTableWidget> {
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
    private Column<SupplierPotentialProjectDetail, String> supplierNameColumn;
    private Column<SupplierPotentialProjectDetail, String> demandTitleColumn;
    private Column<SupplierPotentialProjectDetail, String> ratingColumn;
    private Column<SupplierPotentialProjectDetail, String> priceColumn;
    private Column<SupplierPotentialProjectDetail, Date> urgencyColumn;
    private Column<SupplierPotentialProjectDetail, String> receiveDateColumn;
    private Column<SupplierPotentialProjectDetail, String> deliveryDateColumn;
    //table column width constatnts
    private static final int NAME_COL_WIDTH = 20;
    private static final int DEMAND_TITLE_COL_WIDTH = 30;
    private static final int RATING_COL_WIDTH = 20;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int COL_WIDTH = 30;
    //table column contants
    private static final String CLIENT_NAME_COLUMN = "clientName";
    private static final String SUPPLIER_NAME_COLUMN = "supplierName";
    private static final String DEMAND_TITLE_COLUMN = "demandTitle";
    private static final String RATING_COLUMN = "rating";
    private static final String PRICE_COLUMN = "price";
    private static final String URGENCY_COLUMN = "endDate";
    private static final String RECEIVED_DATE_COLUMN = "receivedDate"; //Prijate
    private static final String DELIVERY_DATE_COLUMN = "deliveryDate"; //Dodanie/dorucenie
    private List<String> gridColumns = new ArrayList<String>();
    //pager definition
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize;
    //Pager definition
    //How many options in page size combo is generated.
    private static final int PAGE_SIZE_ITEMS_COUNT = 5;
    //Represent gab between page size options.
    private static final int PAGE_SIZE_MULTIPLICANT = 5;
    //Which of the items of pageSize combo is selected. by default.
    private static final int PAGE_SIZE_ITEM_SELECTED = 2;
    //Other
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    /**
     * Constuctor.
     * @param loadedView - define Constant in Constants class which define widget
     * for which table schema is generated.
     */
    public UniversalTableWidget(int loadedView) {
        //load custom grid cssStyle
//        Storage.RSCS.grid().ensureInjected();

        switch (loadedView) {
            case Constants.CLIENT_ASSIGNED_PROJECTS:
                initClientAcceptedOffers();
                break;
            case Constants.SUPPLIER_POTENTIAL_PROJECTS:
                initSupplierPotentialProjects();
                break;
            case Constants.SUPPLIER_CONTESTS:
                initSupplierContests();
                break;
            case Constants.SUPPLIER_ASSIGNED_PROJECTS:
                initSupplierAssignedProjects();
                break;
            default:
                break;
        }

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize pager and page size list box.
     */
    private void initPager() {
        pageSize = new ListBox();
        for (int i = 1; i < PAGE_SIZE_ITEMS_COUNT; i++) {
            pageSize.addItem(Integer.toString(i * PAGE_SIZE_MULTIPLICANT));
        }
        pageSize.setSelectedIndex(PAGE_SIZE_ITEM_SELECTED);

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);

        pager.setPageSize(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())));
        grid.setPageSize(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())));
    }

    /**
     * Initialize table: universalAsyncGrid.
     */
    private void initTable() {
        // Create a CellTable.
        grid = new UniversalAsyncGrid<SupplierPotentialProjectDetail>(gridColumns);
        grid.setWidth("800px");
        grid.setHeight("500px");


        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<SupplierPotentialProjectDetail> selectionModel =
                new MultiSelectionModel<SupplierPotentialProjectDetail>(SupplierPotentialProjectDetail.KEY_PROVIDER);
        grid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<SupplierPotentialProjectDetail>createCheckboxManager());

        initPager();
        initContestantsTableColumns();
    }

    /**
     * Generate table schema for ClientAcceptedOffers widget.
     */
    private void initClientAcceptedOffers() {
        gridColumns.clear();
        gridColumns.add(SUPPLIER_NAME_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(DELIVERY_DATE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Generate table schema for SupplierPotentialProjects widget.
     */
    private void initSupplierPotentialProjects() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(URGENCY_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        initTable();
    }

    /**
     * Generate table schema for SupplierContests widget.
     */
    private void initSupplierContests() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(DELIVERY_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        initTable();
    }

    /**
     * Generate table schema for SupplierAssignedProjects widget.
     */
    private void initSupplierAssignedProjects() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(DELIVERY_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        initTable();
    }

    /**
     * Create all columns to the grid according to needed schema represented by gridColumns attribute.
     */
    private void initContestantsTableColumns() {
        // CheckBox column header - always create this header
        checkHeader = new Header<Boolean>(new CheckboxCell()) {

            @Override
            public Boolean getValue() {
                return false;
            }
        };
        // CheckBox column - always create this column
        checkColumn = grid.addCheckboxColumn(checkHeader);
        // Star Column - always create this column
        starColumn = grid.addStarColumn();

        // Client name column
        if (gridColumns.contains(CLIENT_NAME_COLUMN)) {
            clientNameColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.client(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return SupplierPotentialProjectDetail.displayHtml(
                                    detail.getClientName(), detail.isRead());
                        }
                    });
        }
        // Supplier name column
        if (gridColumns.contains(SUPPLIER_NAME_COLUMN)) {
            supplierNameColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return SupplierPotentialProjectDetail.displayHtml(
                                    detail.getSupplierName(), detail.isRead());
                        }
                    });
        }
        // Demand title column
        if (gridColumns.contains(DEMAND_TITLE_COLUMN)) {
            demandTitleColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, DEMAND_TITLE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return SupplierPotentialProjectDetail.displayHtml(
                                    detail.getDemandTitle(), detail.isRead());
                        }
                    });
        }
        // Rating columne
        if (gridColumns.contains(RATING_COLUMN)) {
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
        }
        // Demand price column
        if (gridColumns.contains(PRICE_COLUMN)) {
            priceColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return SupplierPotentialProjectDetail.displayHtml(
                                    detail.getPrice(), detail.isRead());
                        }
                    });
        }
        // Urgency column
        if (gridColumns.contains(URGENCY_COLUMN)) {
            urgencyColumn = grid.addUrgentColumn(Storage.MSGS.urgency());
        }
        // Received date column
        if (gridColumns.contains(RECEIVED_DATE_COLUMN)) {
            receiveDateColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return ClientProjectDetail.displayHtml(
                                    formatter.format(detail.getReceivedDate()),
                                    detail.isRead());
                        }
                    });
        }
        // Delivery date column
        if (gridColumns.contains(DELIVERY_DATE_COLUMN)) {
            deliveryDateColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.deliveryDate(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {

                        @Override
                        public String getValue(Object object) {
                            SupplierPotentialProjectDetail detail = (SupplierPotentialProjectDetail) object;
                            return ClientProjectDetail.displayHtml(
                                    formatter.format(detail.getDeliveryDate()),
                                    detail.isRead());
                        }
                    });
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table
    public UniversalAsyncGrid<SupplierPotentialProjectDetail> getGrid() {
        return grid;
    }

    //Columns
    public Column<SupplierPotentialProjectDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    public Column<SupplierPotentialProjectDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getClientNameColumn() {
        return clientNameColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getDemandTitleColumn() {
        return demandTitleColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getPriceColumn() {
        return priceColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getReceivedColumn() {
        return receiveDateColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getDeliveryColumn() {
        return deliveryDateColumn;
    }

    public Column<SupplierPotentialProjectDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    public Column<SupplierPotentialProjectDetail, Date> getUrgencyColumn() {
        return urgencyColumn;
    }

    //Header
    public Header getCheckHeader() {
        return checkHeader;
    }

    /**
     * Get selected objects.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<SupplierPotentialProjectDetail> getSelectedMessageList() {
        MultiSelectionModel<SupplierPotentialProjectDetail> model =
                (MultiSelectionModel<SupplierPotentialProjectDetail>) grid.getSelectionModel();
        return model.getSelectedSet();
    }

    /**
     * Get IDs of selected objects.
     * @return
     */
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<SupplierPotentialProjectDetail> set = getSelectedMessageList();
        Iterator<SupplierPotentialProjectDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    public IsWidget getWidgetView() {
        return this;
    }
}
