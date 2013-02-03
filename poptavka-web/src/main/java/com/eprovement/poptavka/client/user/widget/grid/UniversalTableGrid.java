package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This widget was created mainly for ClientAssignedProjects, SupplierPotentialProjects,
 * SupplierContests, SupplierAssignedProjects widgets. Those listed widgets use similar
 * tables, therefore this widget was design to cover all common functionality as:
 * UniversalAsyncGrid - see UniversalAsyncGrid class
 * MultiSelectionModel
 *
 * As for this widget has no presenter, action handlers for column field updater
 * must be defined in widget's presenter that is using this one.
 *
 * @author Martin Slavkovsky
 */
public class UniversalTableGrid extends UniversalAsyncGrid<IUniversalDetail> {

    /**************************************************************************/
    /* DemandContestTable Attrinbutes                                         */
    /**************************************************************************/
    //table columns
    private Header checkHeader;
    private Column<IUniversalDetail, Boolean> checkColumn;
    private Column<IUniversalDetail, Boolean> starColumn;
    private Column<IUniversalDetail, String> demandTitleColumn;
    private Column<IUniversalDetail, String> ratingColumn;
    private Column<IUniversalDetail, String> priceColumn;
    private Column<IUniversalDetail, Date> urgencyColumn;
    private Column<IUniversalDetail, String> receiveDateColumn;
    private Column<IUniversalDetail, String> finnishDateColumn;
    //table column width constatnts
    private static final int DEMAND_TITLE_COL_WIDTH = 50;
    private static final int RATING_COL_WIDTH = 10;
    private static final int PRICE_COL_WIDTH = 20;
    private static final int COL_WIDTH = 30;
    //table column contants
    private static final String DEMAND_TITLE_COLUMN = "demandTitle";
    private static final String RATING_COLUMN = "rating";
    private static final String PRICE_COLUMN = "price";
    private static final String URGENCY_COLUMN = "endDate";
    private static final String RECEIVED_DATE_COLUMN = "receivedDate"; //Prijate
    private static final String FINNISH_DATE_COLUMN = "finnishDate"; //Dodanie/dorucenie
    private List<String> gridColumns = new ArrayList<String>();
    //Other
    private MultiSelectionModel<IUniversalDetail> selectionModel;
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Constuctor.
     * @param loadedView - define Constant in Constants class which define widget
     * for which table schema is generated.
     */
    public UniversalTableGrid(ProvidesKey<IUniversalDetail> keyProvider,
            int loadedView, int pageSize, Resources resources) {
        super(pageSize, resources);
        switch (loadedView) {
            case Constants.CLIENT_OFFERED_DEMANDS:
                initClientOffers();
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                initClientAssignedDemands();
                break;
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                initSupplierPotentialDemands();
                break;
            case Constants.SUPPLIER_OFFERS:
                initSupplierOffers();
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                initSupplierAssignedDemands();
                break;
            default:
                break;
        }
        initTable(keyProvider);
        setDataGridRowStyles();
    }

    /**
     * Initialize table: universalAsyncGrid.
     */
    private void initTable(ProvidesKey<IUniversalDetail> keyProvider) {
        setGridColumns(gridColumns);
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        selectionModel = new MultiSelectionModel<IUniversalDetail>(keyProvider);
        setSelectionModel(selectionModel, DefaultSelectionEventManager.<IUniversalDetail>createCheckboxManager());

        initTableColumns();
    }

    /**
     * Set custom row style according to condition.
     */
    private void setDataGridRowStyles() {
        setRowStyles(new RowStyles<IUniversalDetail>() {
            @Override
            public String getStyleNames(IUniversalDetail row, int rowIndex) {
                if (!row.isRead()) {
                    return Storage.RSCS.grid().unread();
                }
                return "";
            }
        });
    }

    /**
     * Generate table schema for ClientAcceptedOffers widget.
     */
    private void initClientOffers() {
        gridColumns.clear();
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Generate table schema for ClientAssignedDemands widget.
     */
    private void initClientAssignedDemands() {
        gridColumns.clear();
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Generate table schema for SupplierPotentialDemands widget.
     */
    private void initSupplierPotentialDemands() {
        gridColumns.clear();
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(URGENCY_COLUMN);
//        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Generate table schema for SupplierOffers widget.
     */
    private void initSupplierOffers() {
        gridColumns.clear();
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Generate table schema for SupplierAssignedDemands widget.
     */
    private void initSupplierAssignedDemands() {
        gridColumns.clear();
        gridColumns.add(DEMAND_TITLE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
    }

    /**
     * Create all columns to the grid according to needed schema represented by gridColumns attribute.
     */
    public void initTableColumns() {
        // CheckBox column header - always create this header
        checkHeader = new Header<Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue() {
                return false;
            }
        };
        // CheckBox column - always create this column
        checkColumn = addCheckboxColumn(checkHeader);
        // Star Column - always create this column
        starColumn = addStarColumn();

        addDemandTitleColumn();
        addRatingColumn();
        addPriceColumn();

        if (gridColumns.contains(URGENCY_COLUMN)) {
            urgencyColumn = addUrgentColumn(Storage.MSGS.columnUrgency());
        }

        addReceivedDateColumn();
        addFinnishDateColumn();
    }

    private void addDemandTitleColumn() {
        if (gridColumns.contains(DEMAND_TITLE_COLUMN)) {
            demandTitleColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnTitle(), true, DEMAND_TITLE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return ((IUniversalDetail) object).getTitle();
                        }
                    });
        }
    }

    private void addRatingColumn() {
        if (gridColumns.contains(RATING_COLUMN)) {
            ratingColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnRating(), true, RATING_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return Integer.toString(((IUniversalDetail) object).getRating());
                        }
                    });
        }
    }

    private void addPriceColumn() {
        if (gridColumns.contains(PRICE_COLUMN)) {
            priceColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnPrice(), false, PRICE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return ((IUniversalDetail) object).getPrice();
                        }
                    });
        }
    }

    private void addReceivedDateColumn() {
        if (gridColumns.contains(RECEIVED_DATE_COLUMN)) {
            receiveDateColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnReceived(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return formatter.format(((IUniversalDetail) object).getReceivedDate());
                        }
                    });
        }
    }

    private void addFinnishDateColumn() {
        if (gridColumns.contains(FINNISH_DATE_COLUMN)) {
            finnishDateColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnDeliveryDate(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return formatter.format(((IUniversalDetail) object).getDeliveryDate());
                        }
                    });
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Columns
    public Column<IUniversalDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    public Column<IUniversalDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    public Column<IUniversalDetail, String> getDemandTitleColumn() {
        return demandTitleColumn;
    }

    public Column<IUniversalDetail, String> getPriceColumn() {
        return priceColumn;
    }

    public Column<IUniversalDetail, String> getReceivedColumn() {
        return receiveDateColumn;
    }

    public Column<IUniversalDetail, String> getFinnishDateColumn() {
        return finnishDateColumn;
    }

    public Column<IUniversalDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    public Column<IUniversalDetail, Date> getUrgencyColumn() {
        return urgencyColumn;
    }

    //Header
    public Header getCheckHeader() {
        return checkHeader;
    }

    //Selection
    @Override
    public MultiSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * Get selected objects.
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<IUniversalDetail> getSelectedObjects() {
        return new ArrayList<IUniversalDetail>(getSelectionModel().getSelectedSet());
    }

    /**
     * Get IDs of selected objects.
     * @return
     */
    public List<Long> getSelectedUserMessageIds() {
        List<Long> idList = new ArrayList<Long>();
        for (IUniversalDetail detail : getSelectedObjects()) {
            idList.add(detail.getUserMessageId());
        }
        return idList;
    }

    public IsWidget getWidgetView() {
        return this;
    }
}
