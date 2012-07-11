package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.cell.ClickableDateCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.DemandStatusImageCell;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demandsModule.ClientDemandDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientProjectsView extends Composite
        implements ClientProjectsPresenter.ClientProjectsLayoutInterface {

    private static ClientProjectsLayoutViewUiBinder uiBinder = GWT.create(ClientProjectsLayoutViewUiBinder.class);

    interface ClientProjectsLayoutViewUiBinder extends UiBinder<Widget, ClientProjectsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;

    //table handling buttons
    @UiField Button readBtn, unreadBtn, starBtn, unstarBtn;

    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientDemandDetail> demandGrid;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "status", "title", "price", "finnishDate", "validTo"
            });
    //table columns
    private Column<ClientDemandDetail, String> titleColumn;
    private Column<ClientDemandDetail, String> priceColumn;
    private Column<ClientDemandDetail, Date> finnishDateColumn;
    private Column<ClientDemandDetail, Date> validToDateColumn;
    //table column width constatnts
    private static final int STATUS_COL_WIDTH = 15;
    private static final int TITLE_COL_WIDTH = 50;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int FINNISH_DATE_COL_WIDTH = 30;
    private static final int VALID_TO_DATE_COL_WIDTH = 30;

    //pager definition
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize;

    //detail WrapperPanel
    @UiField SimplePanel wrapperPanel;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //init pagesize lsit
        pageSize = new ListBox();
        pageSize.addItem("5");
        pageSize.addItem("10");
        pageSize.addItem("15");
        pageSize.addItem("20");
        pageSize.addItem("25");
        pageSize.addItem("30");
        pageSize.setSelectedIndex(2);

        initDemandTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize this example.
     */
    private void initDemandTable() {
        // Create a CellTable.
        demandGrid = new UniversalAsyncGrid<ClientDemandDetail>(gridColumns);
        demandGrid.setEmptyTableWidget(new Label(Storage.MSGS.noData()));
        demandGrid.setWidth("800px");
        demandGrid.setHeight("500px");
//        demandGrid.setLoadingIndicator(new Label("Loading, please wait ..."));
        demandGrid.setRowCount(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())), true);
        demandGrid.setPageSize(this.getPageSize());
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandDetail> selectionModel =
            new MultiSelectionModel<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
            .<ClientDemandDetail>createCheckboxManager());

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(demandGrid);

        initDemandTableColumns();
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand Status column
        demandGrid.addColumn(new DemandStatusImageCell(), Storage.MSGS.status(), true, STATUS_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<DemandStatus>() {

                    @Override
                    public DemandStatus getValue(Object object) {
                        return ((ClientDemandDetail) object).getDemandStatus();
                    }
                });

        // Demand title column
        titleColumn = demandGrid.addColumn(
                new TextCell(), Storage.MSGS.title(), true, TITLE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((ClientDemandDetail) object).getTitle();
                    }
                });

        // Demand price column
        priceColumn = demandGrid.addColumn(
                new TextCell(), Storage.MSGS.price(), true, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((ClientDemandDetail) object).getPrice();
                    }
                });

        // Finnish date column
        finnishDateColumn = demandGrid.addColumn(
                new ClickableDateCell(), Storage.MSGS.finnishDate(), true, FINNISH_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((ClientDemandDetail) object).getEndDate();
                    }
                });

        // Valid-to date column
        validToDateColumn = demandGrid.addColumn(
                new ClickableDateCell(), Storage.MSGS.validTo(), true, VALID_TO_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((ClientDemandDetail) object).getValidToDate();
                    }
                });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    // Columns
    @Override
    public Column<ClientDemandDetail, Date> getFinnishDateColumn() {
        return finnishDateColumn;
    }

    @Override
    public Column<ClientDemandDetail, String> getPriceColumn() {
        return priceColumn;
    }

    @Override
    public Column<ClientDemandDetail, String> getTitleColumn() {
        return titleColumn;
    }

    @Override
    public Column<ClientDemandDetail, Date> getValidToDateColumn() {
        return validToDateColumn;
    }
    // Buttons
    @Override
    public Button getReadBtn() {
        return readBtn;
    }

    @Override
    public Button getStarBtn() {
        return starBtn;
    }

    @Override
    public Button getUnreadBtn() {
        return unreadBtn;
    }

    @Override
    public Button getUnstarBtn() {
        return unstarBtn;
    }

    // Others
    @Override
    public UniversalAsyncGrid<ClientDemandDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<ClientDemandDetail> set = getSelectedMessageList();
        Iterator<ClientDemandDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientDemandDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientDemandDetail> model
            = (MultiSelectionModel<ClientDemandDetail>) demandGrid.getSelectionModel();
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
