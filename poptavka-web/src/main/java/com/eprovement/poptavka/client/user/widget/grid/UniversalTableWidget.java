package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
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
    UniversalAsyncGrid<FullOfferDetail> grid;
    //table columns
    private Header checkHeader;
    private Column<FullOfferDetail, Boolean> checkColumn;
    private Column<FullOfferDetail, Boolean> starColumn;
    private Column<FullOfferDetail, OfferStateType> offerStateColumn;
    private Column<FullOfferDetail, String> clientNameColumn;
    private Column<FullOfferDetail, String> supplierNameColumn;
    private Column<FullOfferDetail, String> demandTitleColumn;
    private Column<FullOfferDetail, String> ratingColumn;
    private Column<FullOfferDetail, String> priceColumn;
    private Column<FullOfferDetail, Date> urgencyColumn;
    private Column<FullOfferDetail, String> receiveDateColumn;
    private Column<FullOfferDetail, String> deliveryDateColumn;
    private Column<FullOfferDetail, ImageResource> replyImageColumn;
    private Column<FullOfferDetail, ImageResource> acceptOfferImageColumn;
    private Column<FullOfferDetail, ImageResource> declineOfferImageColumn;
    private Column<FullOfferDetail, ImageResource> closeDemandImageColumn;
    private Column<FullOfferDetail, ImageResource> sendOfferImageColumn;
    private Column<FullOfferDetail, ImageResource> editOfferImageColumn;
    private Column<FullOfferDetail, ImageResource> downloadOfferImageColumns;
    private Column<FullOfferDetail, ImageResource> finnishedImageColumn;
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
    private static final String DELIVERY_DATE_COLUMN = "deliveryDate"; //Dodanie/dorucenie
    private static final String REPLY_IMAGE_COLUMN = "reply";
    private static final String ACCEPT_OFFER_IMAGE_COLUMN = "acceptOffer";
    private static final String DECLINE_OFFER_IMAGE_COLUMN = "declineOffer";
    private static final String CLOSE_DEMAND_IMAGE_COLUMN = "closeDemand";
    private static final String SEND_OFFER_IMAGE_COLUMN = "sendOffer";
    private static final String EDIT_OFFER_IMAGE_COLUMN = "editOffer";
    private static final String DOWNLOAD_OFFER_IMAGE_COLUMN = "downloadOffer";
    private static final String FINNISHED_IMAGE_COLUMN = "finnished";
    private List<String> gridColumns = new ArrayList<String>();
    //pager definition
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize, actionBox;
    @UiField
    Label tableNameLabel;
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
        initTable();
        initActionBox();

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
        grid = new UniversalAsyncGrid<FullOfferDetail>(gridColumns);
        grid.setHeight("500px");


        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<FullOfferDetail> selectionModel =
                new MultiSelectionModel<FullOfferDetail>();
        grid.setSelectionModel(
                selectionModel, DefaultSelectionEventManager.<FullOfferDetail>createCheckboxManager());

        initPager();
        initTableColumns();
    }

    private void initActionBox() {
        actionBox = new ListBox();
        actionBox.addItem(Storage.MSGS.action());
        actionBox.addItem(Storage.MSGS.read());
        actionBox.addItem(Storage.MSGS.unread());
        actionBox.addItem(Storage.MSGS.star());
        actionBox.addItem(Storage.MSGS.unstar());
        actionBox.setSelectedIndex(0);
    }

    /**
     * Generate table schema for ClientAcceptedOffers widget.
     */
    private void initClientOffers() {
        gridColumns.clear();
        gridColumns.add(SUPPLIER_NAME_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(DELIVERY_DATE_COLUMN);
        gridColumns.add(RATING_COLUMN);
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
        gridColumns.add(DELIVERY_DATE_COLUMN);
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
        gridColumns.add(RATING_COLUMN);
        gridColumns.add(PRICE_COLUMN);
        gridColumns.add(URGENCY_COLUMN);
        gridColumns.add(RECEIVED_DATE_COLUMN);
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
        gridColumns.add(DELIVERY_DATE_COLUMN);
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
        gridColumns.add(DELIVERY_DATE_COLUMN);
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
        checkColumn = grid.addCheckboxColumn(checkHeader);
        // Star Column - always create this column
        starColumn = grid.addStarColumn();
        // Offer state column - always create this column
        offerStateColumn = grid.addOfferStateColumn(Storage.MSGS.state());

        addImageColumns();
        addClientNameColumn();
        addSupplierNameColumn();
        addDemandTitleColumn();
        addRatingColumn();
        addPriceColumn();

        if (gridColumns.contains(URGENCY_COLUMN)) {
            urgencyColumn = grid.addUrgentColumn(Storage.MSGS.urgency());
        }

        addReceivedDateColumn();
        addDeliveryDateColumn();
    }

    private void addImageColumns() {
        if (gridColumns.contains(REPLY_IMAGE_COLUMN)) {
            replyImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().replyImage(),
                    Storage.MSGS.replyExplanationText());
        }
        if (gridColumns.contains(ACCEPT_OFFER_IMAGE_COLUMN)) {
            acceptOfferImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().acceptOfferImage(),
                    Storage.MSGS.acceptOfferExplanationText());
        }
        if (gridColumns.contains(DECLINE_OFFER_IMAGE_COLUMN)) {
            declineOfferImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().declineOfferImage(),
                    Storage.MSGS.declineOfferExplanationText());
        }
        if (gridColumns.contains(CLOSE_DEMAND_IMAGE_COLUMN)) {
            closeDemandImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().closeDemandImage(),
                    Storage.MSGS.closeDemandExplanationText());
        }
        if (gridColumns.contains(SEND_OFFER_IMAGE_COLUMN)) {
            sendOfferImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().sendOfferImage(),
                    Storage.MSGS.sendOfferExplanationText());
        }
        if (gridColumns.contains(EDIT_OFFER_IMAGE_COLUMN)) {
            editOfferImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().editOfferImage(),
                    Storage.MSGS.editOfferExplanationText());
        }
        if (gridColumns.contains(DOWNLOAD_OFFER_IMAGE_COLUMN)) {
            downloadOfferImageColumns = grid.addIconColumn(
                    Storage.RSCS.images().downloadOfferImage(),
                    Storage.MSGS.downloadOfferExplanationText());
        }
        if (gridColumns.contains(FINNISHED_IMAGE_COLUMN)) {
            finnishedImageColumn = grid.addIconColumn(
                    Storage.RSCS.images().finnishedImage(),
                    Storage.MSGS.finnishedExplanationText());
        }
    }

    private void addClientNameColumn() {
        if (gridColumns.contains(CLIENT_NAME_COLUMN)) {
            clientNameColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.client(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return FullOfferDetail.displayHtml(
                                    detail.getOfferDetail().getClientName(), detail.isRead());
                        }
                    });
        }
    }

    private void addSupplierNameColumn() {
        if (gridColumns.contains(SUPPLIER_NAME_COLUMN)) {
            supplierNameColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.supplierName(), true, NAME_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return FullOfferDetail.displayHtml(
                                    detail.getOfferDetail().getSupplierName(), detail.isRead());
                        }
                    });
        }
    }

    private void addDemandTitleColumn() {
        if (gridColumns.contains(DEMAND_TITLE_COLUMN)) {
            demandTitleColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, DEMAND_TITLE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return FullOfferDetail.displayHtml(
                                    detail.getOfferDetail().getDemandTitle(), detail.isRead());
                        }
                    });
        }
    }

    private void addRatingColumn() {
        if (gridColumns.contains(RATING_COLUMN)) {
            ratingColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.rating(), true, RATING_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return ClientDemandDetail.displayHtml(
                                    Integer.toString(detail.getOfferDetail().getRating()),
                                    detail.isRead());
                        }
                    });
        }
    }

    private void addPriceColumn() {
        if (gridColumns.contains(PRICE_COLUMN)) {
            priceColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), false, PRICE_COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return FullOfferDetail.displayHtml(
                                    detail.getOfferDetail().getPrice().toString(), detail.isRead());
                        }
                    });
        }
    }

    private void addReceivedDateColumn() {
        if (gridColumns.contains(RECEIVED_DATE_COLUMN)) {
            receiveDateColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.received(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return ClientDemandDetail.displayHtml(
                                    formatter.format(detail.getOfferDetail().getCreatedDate()),
                                    detail.isRead());
                        }
                    });
        }
    }

    private void addDeliveryDateColumn() {
        if (gridColumns.contains(DELIVERY_DATE_COLUMN)) {
            deliveryDateColumn = grid.addColumn(
                    grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.deliveryDate(), true, COL_WIDTH,
                    new UniversalAsyncGrid.GetValue<String>() {
                        @Override
                        public String getValue(Object object) {
                            FullOfferDetail detail = (FullOfferDetail) object;
                            return ClientDemandDetail.displayHtml(
                                    formatter.format(detail.getOfferDetail().getFinishDate()),
                                    detail.isRead());
                        }
                    });
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table
    public UniversalAsyncGrid<FullOfferDetail> getGrid() {
        return grid;
    }

    //Columns
    public Column<FullOfferDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    public Column<FullOfferDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    public Column<FullOfferDetail, String> getClientNameColumn() {
        return clientNameColumn;
    }

    public Column<FullOfferDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    public Column<FullOfferDetail, String> getDemandTitleColumn() {
        return demandTitleColumn;
    }

    public Column<FullOfferDetail, String> getPriceColumn() {
        return priceColumn;
    }

    public Column<FullOfferDetail, String> getReceivedColumn() {
        return receiveDateColumn;
    }

    public Column<FullOfferDetail, String> getDeliveryColumn() {
        return deliveryDateColumn;
    }

    public Column<FullOfferDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    public Column<FullOfferDetail, Date> getUrgencyColumn() {
        return urgencyColumn;
    }

    public Column<FullOfferDetail, ImageResource> getReplyImageColumn() {
        return replyImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getAcceptOfferImageColumn() {
        return acceptOfferImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getDeclineOfferImageColumn() {
        return declineOfferImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getCloseDemandImageColumn() {
        return closeDemandImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getSendOfferImageColumn() {
        return sendOfferImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getEditOfferImageColumn() {
        return editOfferImageColumn;
    }

    public Column<FullOfferDetail, ImageResource> getDownloadOfferImageColumns() {
        return downloadOfferImageColumns;
    }

    public Column<FullOfferDetail, ImageResource> getFinnishedImageColumn() {
        return finnishedImageColumn;
    }

    //Header
    public Header getCheckHeader() {
        return checkHeader;
    }

    //Label
    public Label getTableNameLabel() {
        return tableNameLabel;
    }

    //ListBox
    public ListBox getActionBox() {
        return actionBox;
    }

    /**
     * Get selected objects.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<FullOfferDetail> getSelectedMessageList() {
        MultiSelectionModel<FullOfferDetail> model =
                (MultiSelectionModel<FullOfferDetail>) grid.getSelectionModel();
        return model.getSelectedSet();
    }

    /**
     * Get IDs of selected objects.
     * @return
     */
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<FullOfferDetail> set = getSelectedMessageList();
        Iterator<FullOfferDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageDetail().getId());
        }
        return idList;
    }

    public IsWidget getWidgetView() {
        return this;
    }
}
