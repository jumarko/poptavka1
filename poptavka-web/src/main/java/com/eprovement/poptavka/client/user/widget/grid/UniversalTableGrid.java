package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
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
    private Column<IUniversalDetail, String> clientNameColumn;
    private Column<IUniversalDetail, String> supplierNameColumn;
    private Column<IUniversalDetail, String> demandTitleColumn;
    private Column<IUniversalDetail, String> ratingColumn;
    private Column<IUniversalDetail, String> priceColumn;
    private Column<IUniversalDetail, Date> urgencyColumn;
    private Column<IUniversalDetail, String> receiveDateColumn;
    private Column<IUniversalDetail, String> finnishDateColumn;
    private Column<IUniversalDetail, ImageResource> replyImageColumn;
    private Column<IUniversalDetail, ImageResource> acceptOfferImageColumn;
    private Column<IUniversalDetail, ImageResource> declineOfferImageColumn;
    private Column<IUniversalDetail, ImageResource> closeDemandImageColumn;
    private Column<IUniversalDetail, ImageResource> sendOfferImageColumn;
    private Column<IUniversalDetail, ImageResource> editOfferImageColumn;
    private Column<IUniversalDetail, ImageResource> downloadOfferImageColumns;
    private Column<IUniversalDetail, ImageResource> finnishedImageColumn;
    //table column width constatnts
    private static final int NAME_COL_WIDTH = 50;
    private static final int DEMAND_TITLE_COL_WIDTH = 50;
    private static final int RATING_COL_WIDTH = 10;
    private static final int PRICE_COL_WIDTH = 20;
    private static final int COL_WIDTH = 30;
    //table column contants
    private static final String CLIENT_NAME_COLUMN = "clientName";
    private static final String SUPPLIER_NAME_COLUMN = "supplierName";
    private static final String DEMAND_TITLE_COLUMN = "demandTitle";
    private static final String RATING_COLUMN = "rating";
    private static final String PRICE_COLUMN = "price";
    private static final String URGENCY_COLUMN = "endDate";
    private static final String RECEIVED_DATE_COLUMN = "receivedDate"; //Prijate
    private static final String FINNISH_DATE_COLUMN = "finnishDate"; //Dodanie/dorucenie
    private static final String REPLY_IMAGE_COLUMN = "reply";
    private static final String ACCEPT_OFFER_IMAGE_COLUMN = "acceptOffer";
    private static final String DECLINE_OFFER_IMAGE_COLUMN = "declineOffer";
    private static final String CLOSE_DEMAND_IMAGE_COLUMN = "closeDemand";
    private static final String SEND_OFFER_IMAGE_COLUMN = "sendOffer";
    private static final String EDIT_OFFER_IMAGE_COLUMN = "editOffer";
    private static final String DOWNLOAD_OFFER_IMAGE_COLUMN = "downloadOffer";
    private static final String FINNISHED_IMAGE_COLUMN = "finnished";
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
                initClientAcceptedOffers();
                break;
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                initSupplierPotentialProjects();
                break;
            case Constants.SUPPLIER_OFFERS:
                initSupplierContests();
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                initSupplierAssignedProjects();
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
                if (row.getUnreadMessageCount() > 0) {
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
        gridColumns.add(SUPPLIER_NAME_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        gridColumns.add(ACCEPT_OFFER_IMAGE_COLUMN);
        gridColumns.add(DECLINE_OFFER_IMAGE_COLUMN);
        gridColumns.add(REPLY_IMAGE_COLUMN);
    }

    /**
     * Generate table schema for ClientAcceptedOffers widget.
     */
    private void initClientAcceptedOffers() {
        gridColumns.clear();
        gridColumns.add(SUPPLIER_NAME_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        gridColumns.add(CLOSE_DEMAND_IMAGE_COLUMN);
        gridColumns.add(REPLY_IMAGE_COLUMN);
    }

    /**
     * Generate table schema for SupplierPotentialProjects widget.
     */
    private void initSupplierPotentialProjects() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(DEMAND_TITLE_COLUMN);
//        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(URGENCY_COLUMN);
//        gridColumns.add(RECEIVED_DATE_COLUMN);
        gridColumns.add(REPLY_IMAGE_COLUMN);
        gridColumns.add(SEND_OFFER_IMAGE_COLUMN);
    }

    /**
     * Generate table schema for SupplierContests widget.
     */
    private void initSupplierContests() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        gridColumns.add(REPLY_IMAGE_COLUMN);
        gridColumns.add(EDIT_OFFER_IMAGE_COLUMN);
        gridColumns.add(DOWNLOAD_OFFER_IMAGE_COLUMN);
    }

    /**
     * Generate table schema for SupplierAssignedProjects widget.
     */
    private void initSupplierAssignedProjects() {
        gridColumns.clear();
        gridColumns.add(CLIENT_NAME_COLUMN);
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(FINNISH_DATE_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
        gridColumns.add(FINNISHED_IMAGE_COLUMN);
        gridColumns.add(REPLY_IMAGE_COLUMN);
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

        addImageColumns();
        addClientNameColumn();
        addSupplierNameColumn();
        addDemandTitleColumn();
        addRatingColumn();
        addPriceColumn();

        if (gridColumns.contains(URGENCY_COLUMN)) {
            urgencyColumn = addUrgentColumn(Storage.MSGS.urgency());
        }

        addReceivedDateColumn();
        addFinnishDateColumn();
    }

    private void addImageColumns() {
        if (gridColumns.contains(REPLY_IMAGE_COLUMN)) {
            replyImageColumn = addIconColumn(
                    Storage.RSCS.images().replyImage(),
                    Storage.MSGS.replyExplanationText());
        }
        if (gridColumns.contains(ACCEPT_OFFER_IMAGE_COLUMN)) {
            acceptOfferImageColumn = addIconColumn(
                    Storage.RSCS.images().acceptOfferImage(),
                    Storage.MSGS.acceptOfferExplanationText());
        }
        if (gridColumns.contains(DECLINE_OFFER_IMAGE_COLUMN)) {
            declineOfferImageColumn = addIconColumn(
                    Storage.RSCS.images().declineOfferImage(),
                    Storage.MSGS.declineOfferExplanationText());
        }
        if (gridColumns.contains(CLOSE_DEMAND_IMAGE_COLUMN)) {
            closeDemandImageColumn = addIconColumn(
                    Storage.RSCS.images().closeDemandImage(),
                    Storage.MSGS.closeDemandExplanationText());
        }
        if (gridColumns.contains(SEND_OFFER_IMAGE_COLUMN)) {
            sendOfferImageColumn = addIconColumn(
                    Storage.RSCS.images().sendOfferImage(),
                    Storage.MSGS.sendOfferExplanationText());
        }
        if (gridColumns.contains(EDIT_OFFER_IMAGE_COLUMN)) {
            editOfferImageColumn = addIconColumn(
                    Storage.RSCS.images().editOfferImage(),
                    Storage.MSGS.editOfferExplanationText());
        }
        if (gridColumns.contains(DOWNLOAD_OFFER_IMAGE_COLUMN)) {
            downloadOfferImageColumns = addIconColumn(
                    Storage.RSCS.images().downloadOfferImage(),
                    Storage.MSGS.downloadOfferExplanationText());
        }
        if (gridColumns.contains(FINNISHED_IMAGE_COLUMN)) {
            finnishedImageColumn = addIconColumn(
                    Storage.RSCS.images().finnishedImage(),
                    Storage.MSGS.finnishedExplanationText());
        }
    }

    private void addClientNameColumn() {
        if (gridColumns.contains(CLIENT_NAME_COLUMN)) {
            clientNameColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.client(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return ((IUniversalDetail) object).displayUserNameWithUnreadMessageCounts(
                                    IUniversalDetail.CLIENT_NAME);
                        }
                    });
        }
    }

    private void addSupplierNameColumn() {
        if (gridColumns.contains(SUPPLIER_NAME_COLUMN)) {
            supplierNameColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            return ((IUniversalDetail) object).displayUserNameWithUnreadMessageCounts(
                                    IUniversalDetail.SUPPLIER_NAME);
                        }
                    });
        }
    }

    private void addDemandTitleColumn() {
        if (gridColumns.contains(DEMAND_TITLE_COLUMN)) {
            demandTitleColumn = addColumn(
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, DEMAND_TITLE_COL_WIDTH,
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
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.rating(), true, RATING_COL_WIDTH,
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
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
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
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, COL_WIDTH,
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
                    TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.deliveryDate(), true, COL_WIDTH,
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

    public Column<IUniversalDetail, String> getClientNameColumn() {
        return clientNameColumn;
    }

    public Column<IUniversalDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
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

    public Column<IUniversalDetail, ImageResource> getReplyImageColumn() {
        return replyImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getAcceptOfferImageColumn() {
        return acceptOfferImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getDeclineOfferImageColumn() {
        return declineOfferImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getCloseDemandImageColumn() {
        return closeDemandImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getSendOfferImageColumn() {
        return sendOfferImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getEditOfferImageColumn() {
        return editOfferImageColumn;
    }

    public Column<IUniversalDetail, ImageResource> getDownloadOfferImageColumns() {
        return downloadOfferImageColumns;
    }

    public Column<IUniversalDetail, ImageResource> getFinnishedImageColumn() {
        return finnishedImageColumn;
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
    public Set<IUniversalDetail> getSelectedMessageList() {
        MultiSelectionModel<IUniversalDetail> model =
                (MultiSelectionModel<IUniversalDetail>) getSelectionModel();
        return model.getSelectedSet();
    }

    /**
     * Get IDs of selected objects.
     * @return
     */
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<IUniversalDetail> set = getSelectedMessageList();
        Iterator<IUniversalDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    public IsWidget getWidgetView() {
        return this;
    }
}
