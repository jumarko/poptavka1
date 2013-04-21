package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Column<IUniversalDetail, Integer> ratingColumn;
    private Column<IUniversalDetail, String> priceColumn;
    private Column<IUniversalDetail, Date> urgencyColumn;
    private Column<IUniversalDetail, String> receiveDateColumn;
    private Column<IUniversalDetail, String> finnishDateColumn;
    //table column contants
    private static final String COLUMN_CHECK_BOX = "";
    private static final String COLUMN_STAR = "";
    private static final String COLUMN_DEMAND_TITLE = DemandField.TITLE.getValue();
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PRICE = DemandField.PRICE.getValue();
    private static final String COLUMN_URGENCY = DemandField.VALID_TO.getValue();
    private static final String COLUMN_RECEIVED_DATE = "receivedDate"; //Prijate
    private static final String COLUMN_FINNISH_DATE = "endDate"; //Dodanie/dorucenie
    private List<String> gridColumns = new ArrayList<String>();
    //Other
    private MultiSelectionModel<IUniversalDetail> selectionModel;

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

        initTable(keyProvider);
        //create columns (also fills gridColumns list)
        switch (loadedView) {
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                initDemandDiscussions();
                break;
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
        setDataGridRowStyles();
    }

    /**
     * Initialize table: universalAsyncGrid.
     */
    private void initTable(ProvidesKey<IUniversalDetail> keyProvider) {
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        selectionModel = new MultiSelectionModel<IUniversalDetail>(keyProvider);
        setSelectionModel(selectionModel, DefaultSelectionEventManager.<IUniversalDetail>createCheckboxManager());
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
     * Generate table schema for ClientDemandDiscussions widget.
     */
    private void initDemandDiscussions() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(""); //company name, first name, last name
        gridColumns.add(MessageField.BODY.getValue());
        gridColumns.add("demand.".concat(DemandField.RATING.getValue())); //demand rating
        gridColumns.add(MessageField.SENT.getValue());
        initSort(new SortPair("demand.".concat(COLUMN_RATING), OrderType.DESC));
    }

    /**
     * Generate table schema for ClientAcceptedOffers widget.
     */
    private void initClientOffers() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(COLUMN_DEMAND_TITLE);
        gridColumns.add(COLUMN_PRICE);
        gridColumns.add(COLUMN_RATING);
        gridColumns.add(COLUMN_FINNISH_DATE);
        gridColumns.add(COLUMN_RECEIVED_DATE);
        initSort(new SortPair(COLUMN_RATING, OrderType.DESC));
        initTableColumns();
    }

    /**
     * Generate table schema for ClientAssignedDemands widget.
     */
    private void initClientAssignedDemands() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(COLUMN_DEMAND_TITLE);
        gridColumns.add(COLUMN_PRICE);
        gridColumns.add(COLUMN_FINNISH_DATE);
        gridColumns.add(COLUMN_RATING);
        gridColumns.add(COLUMN_RECEIVED_DATE);
        initSort(new SortPair(COLUMN_FINNISH_DATE, OrderType.DESC));
        initTableColumns();
    }

    /**
     * Generate table schema for SupplierPotentialDemands widget.
     */
    private void initSupplierPotentialDemands() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(COLUMN_DEMAND_TITLE);
        gridColumns.add(COLUMN_RATING);
        gridColumns.add(COLUMN_PRICE);
        gridColumns.add(COLUMN_URGENCY);
        initSort(new SortPair(COLUMN_URGENCY, OrderType.DESC));
        initTableColumns();
    }

    /**
     * Generate table schema for SupplierOffers widget.
     */
    private void initSupplierOffers() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(COLUMN_DEMAND_TITLE);
        gridColumns.add(COLUMN_RATING);
        gridColumns.add(COLUMN_PRICE);
        gridColumns.add(COLUMN_FINNISH_DATE);
        gridColumns.add(COLUMN_RECEIVED_DATE);
        initSort(new SortPair(COLUMN_RATING, OrderType.DESC));
        initTableColumns();
    }

    /**
     * Generate table schema for SupplierAssignedDemands widget.
     */
    private void initSupplierAssignedDemands() {
        gridColumns.clear();
        gridColumns.add(COLUMN_CHECK_BOX);
        gridColumns.add(COLUMN_STAR);
        gridColumns.add(COLUMN_DEMAND_TITLE);
        gridColumns.add(COLUMN_RATING);
        gridColumns.add(COLUMN_PRICE);
        gridColumns.add(COLUMN_FINNISH_DATE);
        gridColumns.add(COLUMN_RECEIVED_DATE);
        initSort(new SortPair(COLUMN_FINNISH_DATE, OrderType.DESC));
        initTableColumns();
    }

    private void initSort(SortPair sortPair) {
        setGridColumns(new SortDataHolder(Arrays.asList(sortPair), gridColumns));
    }

    /**
     * Create all columns to the grid according to needed schema represented by gridColumns attribute.
     */
    public void initTableColumns() {
        if (gridColumns.contains(COLUMN_CHECK_BOX)) {
            checkColumn = this.addCheckboxColumn();
        }
        if (gridColumns.contains(COLUMN_STAR)) {
            starColumn = super.addStarColumn();
        }
        if (gridColumns.contains(COLUMN_DEMAND_TITLE)) {
            demandTitleColumn = this.addDemandTitleColumn();
        }
        if (gridColumns.contains(COLUMN_RATING)) {
            ratingColumn = super.addRatingColumn();
        }
        if (gridColumns.contains(COLUMN_PRICE)) {
            priceColumn = this.addPriceColumn();
        }
        if (gridColumns.contains(COLUMN_URGENCY)) {
            urgencyColumn = super.addUrgentColumn();
        }
        if (gridColumns.contains(COLUMN_RECEIVED_DATE)) {
            receiveDateColumn = this.addReceivedDateColumn();
        }
        if (gridColumns.contains(COLUMN_FINNISH_DATE)) {
            finnishDateColumn = this.addFinnishDateColumn();
        }
    }

    public Column<IUniversalDetail, Boolean> addCheckboxColumn() {
        checkHeader = new Header<Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue() {
                return false;
            }
        };
        return super.addCheckboxColumn(checkHeader);
    }

    @Override
    public Column<IUniversalDetail, Boolean> addStarColumn() {
        starColumn = super.addStarColumn();
        return starColumn;
    }

    private Column<IUniversalDetail, String> addDemandTitleColumn() {
        return super.addColumn(
                TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    IUniversalDetail detail = (IUniversalDetail) object;
                    if (detail.getMessageCount() > 0) {
                        StringBuilder title = new StringBuilder();
                        title.append(detail.getTitle());
                        title.append(" (");
                        title.append(detail.getMessageCount());
                        title.append(")");
                        return title.toString();
                    } else {
                        return detail.getTitle();
                    }
                }
            });
    }

    private Column<IUniversalDetail, String> addPriceColumn() {
        return super.addColumn(
                TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnPrice(),
                true, Constants.COL_WIDTH_PRICE,
                new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    return Storage.CURRENCY_FORMAT.format(((IUniversalDetail) object).getPrice());
                }
            });
    }

    private Column<IUniversalDetail, String> addReceivedDateColumn() {
        return super.addColumn(
                TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnReceived(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    return Storage.DATE_FORMAT_SHORT.format(((IUniversalDetail) object).getReceivedDate());
                }
            });
    }

    private Column<IUniversalDetail, String> addFinnishDateColumn() {
        return super.addColumn(
                TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnDeliveryDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    return Storage.DATE_FORMAT_SHORT.format(((IUniversalDetail) object).getDeliveryDate());
                }
            });
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

    public Column<IUniversalDetail, Integer> getRatingColumn() {
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
