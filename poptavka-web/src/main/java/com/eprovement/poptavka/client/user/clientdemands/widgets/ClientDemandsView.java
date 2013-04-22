package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
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
    @UiField(provided = true) UniversalAsyncGrid<ClientDemandDetail> demandGrid;
    //pager definition
    @UiField(provided = true) UniversalPagerWidget demandPager;
    /**************************************************************************/
    /* DemandConversationTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true) UniversalTableGrid conversationGrid;
    //pager definition
    @UiField(provided = true) UniversalPagerWidget conversationPager;
    //table columns
    private Column<IUniversalDetail, String> supplierNameColumn;
    private Column<IUniversalDetail, String> bodyPreviewColumn;
    private Column<IUniversalDetail, String> dateColumn;
    //table column width constatnts
    private static final String SUPPLIER_NAME_COL_WIDTH = "30%";
    private static final String BODY_PREVIEW_COL_WIDTH = "70%";
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField SimplePanel detailPanel, actionBox;
    @UiField Label demandTitlelabel;
    @UiField HorizontalPanel demandHeader, conversationHeader;
    @UiField Button backBtn, editDemandButton, deleteDemandButton;
    @UiField HTMLPanel choiceButtonsPanel;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //for change monitors
        Storage.RSCS.common().ensureInjected();

        initDemandTableAndPager();
        initConversationTableAndPager();
        initWidget(uiBinder.createAndBindUi(this));

        setConversationTableVisible(false);
    }

    /**
     * Initialize this example.
     */
    private void initDemandTableAndPager() {
        // Create a Pager.
        demandPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        demandGrid = new UniversalAsyncGrid<ClientDemandDetail>(
                initSort(), demandPager.getPageSize(), resource);
        demandGrid.setWidth("100%");
        demandGrid.setHeight("100%");
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
        // Create a Pager.
        conversationPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        conversationGrid = new UniversalTableGrid(
                ClientDemandConversationDetail.KEY_PROVIDER,
                Constants.CLIENT_DEMAND_DISCUSSIONS,
                conversationPager.getPageSize(),
                resource);
        conversationGrid.setWidth("100%");
        conversationGrid.setHeight("100%");
        // Bind pager to Table
        conversationPager.setDisplay(conversationGrid);

        initConversationTableColumns();
    }

    private SortDataHolder initSort() {
        List<String> gridColumns = Arrays.asList(new String[]{
            DemandField.DEMAND_STATUS.getValue(),
            DemandField.TITLE.getValue(),
            DemandField.PRICE.getValue(),
            DemandField.END_DATE.getValue(),
            DemandField.VALID_TO.getValue()
        });
        List<SortPair> sortPairs = Arrays.asList(
                new SortPair(FullDemandDetail.DemandField.CREATED.getValue(), OrderType.DESC));
        return new SortDataHolder(sortPairs, gridColumns);
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand Status column
        demandGrid.addDemandStatusColumn(Storage.MSGS.columnStatus());
        // Demand title column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        ClientDemandDetail detail = (ClientDemandDetail) object;
                        str.append(detail.getDemandTitle());
                        if (detail.getUnreadSubmessagesCount() > 0) {
                            str.append(" (");
                            str.append(detail.getUnreadSubmessagesCount());
                            str.append(")");
                        }
                        return str.toString();
                    }
                });

        // Demand price column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnPrice(),
                true, Constants.COL_WIDTH_PRICE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.CURRENCY_FORMAT.format(((ClientDemandDetail) object).getPrice());
                    }
                });

        // End date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnEndDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.DATE_FORMAT_SHORT.format(((ClientDemandDetail) object).getEndDate());
                    }
                });

        // Urgency (Valid-to date) column
        demandGrid.addUrgentColumn();
    }

    /**
     * Create all columns to the grid.
     */
    public void initConversationTableColumns() {
        // Check box column
        conversationGrid.addCheckboxColumn();

        // Star column
        conversationGrid.addStarColumn();

        // Supplier name column
        supplierNameColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnSupplierName(),
                false, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandConversationDetail detail = (ClientDemandConversationDetail) object;
                        StringBuilder str = new StringBuilder();
                        str.append(detail.getDisplayName());
                        if (detail.getMessageCount() > 0) {
                            str.append(" (");
                            str.append(detail.getMessageCount());
                            str.append(")");
                        }
                        return str.toString();
                    }
                });

        // Text column
        bodyPreviewColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnText(),
                true, BODY_PREVIEW_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((ClientDemandConversationDetail) object).getMessageBody();
                    }
                });

        // Demand rating column
        conversationGrid.addRatingColumn();

        // Date column
        dateColumn = conversationGrid.addColumn(
                conversationGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.DATE_FORMAT_SHORT.format(
                                ((ClientDemandConversationDetail) object).getMessageSent());
                    }
                });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    // Columns
    @Override
    public Column<IUniversalDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    @Override
    public Column<IUniversalDetail, String> getBodyPreviewColumn() {
        return bodyPreviewColumn;
    }

    @Override
    public Column<IUniversalDetail, String> getDateColumn() {
        return dateColumn;
    }

    /** Button. **/
    @Override
    public Button getBackBtn() {
        return backBtn;
    }

    @Override
    public Button getEditDemandButton() {
        return editDemandButton;
    }

    @Override
    public Button getDeleteDemandButton() {
        return deleteDemandButton;
    }

    /** Html. **/
    @Override
    public HTMLPanel getChoiceButtonsPanel() {
        return choiceButtonsPanel;
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

    @Override
    public SimplePanel getActionBox() {
        return actionBox;
    }

    // Others
    @Override
    public UniversalAsyncGrid<ClientDemandDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public UniversalTableGrid getConversationGrid() {
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
    public SimplePanel getDetailPanel() {
        return detailPanel;
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
        demandGrid.redraw();
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
        conversationGrid.redraw();
        conversationHeader.setVisible(visible);
    }

    @Override
    public void setDemandTitleLabel(String text) {
        demandTitlelabel.setText(text);
    }
}
