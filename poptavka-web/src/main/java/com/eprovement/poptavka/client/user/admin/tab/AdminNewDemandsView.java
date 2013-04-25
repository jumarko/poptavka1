package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.cell.CreatedDateCell;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AdminNewDemandsView extends Composite
        implements AdminNewDemandsPresenter.AdminNewDemandsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AdminNewDemandsView.AdminNewDemandsLayoutViewUiBinder uiBinder =
            GWT.create(AdminNewDemandsView.AdminNewDemandsLayoutViewUiBinder.class);

    interface AdminNewDemandsLayoutViewUiBinder extends UiBinder<Widget, AdminNewDemandsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField HorizontalPanel toolBar;
    @UiField Button approveBtn, createConversationBtn;
    @UiField Label filterLabel;
    @UiField DecoratorPanel filterLabelPanel;
    @UiField SimplePanel detailPanel;

    /** Class attributes. **/
    private Header checkHeader;
    private Column<FullDemandDetail, String> createdDateColumn;
    private Column<FullDemandDetail, String> demnadTitleColumn;
    private Column<FullDemandDetail, String> localityColumn;
    private MultiSelectionModel selectionModel;
    private LoadingDiv loadingDiv = new LoadingDiv();
    /** Constants. **/
    private static final String LOCALITY_COL_WIDTH = "150px";

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTableAndPager();
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    private void initTableAndPager() {
        pager = new UniversalPagerWidget();
        // Create a DataGrid
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalAsyncGrid<FullDemandDetail>(initSort(), pager.getPageSize(), resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        // Selection handler
        selectionModel = new MultiSelectionModel(FullDemandDetail.KEY_PROVIDER);
        dataGrid.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<FullDemandDetail>createCheckboxManager(0));
        // bind pager to grid
        pager.setDisplay(dataGrid);

        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Check box collumn
        /**************************************************************************/
        checkHeader = new Header<Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue() {
                return false;
            }
        };
        dataGrid.addCheckboxColumn(checkHeader);

        // Date of creation
        /**************************************************************************/
        createdDateColumn = dataGrid.addColumn(new CreatedDateCell(), Storage.MSGS.columnCreatedDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<Date>() {
                    @Override
                    public Date getValue(Object object) {
                        return ((FullDemandDetail) object).getCreated();
                    }
                });

        // Demand Info
        /**************************************************************************/
        demnadTitleColumn = dataGrid.addColumn(new ClickableTextCell(), Storage.MSGS.columnDemandTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((FullDemandDetail) object).getTitle();
                    }
                });

        // Locality
        /**************************************************************************/
        localityColumn = dataGrid.addColumn(new ClickableTextCell(), Storage.MSGS.columnLocality(),
                false, LOCALITY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (LocalityDetail loc : ((FullDemandDetail) object).getLocalities()) {
                            str.append(loc.getName());
                            str.append(",\n");
                        }
                        if (str.length() > 0) {
                            str.delete(str.length() - 2, str.length());
                        }
                        return str.toString();
                    }
                });

        // Urgence
        /**************************************************************************/
        dataGrid.addUrgentColumn();
    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList();
        List<SortPair> defaultSort = Arrays.asList();
        return new SortDataHolder(defaultSort, sortColumns);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void loadingDivShow(Widget holderWidget) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new LoadingDiv();
        }
        holderWidget.getElement().appendChild(loadingDiv.getElement());
    }

    @Override
    public void loadingDivHide(Widget holderWidget) {
        GWT.log("  - loading div removed");
        if (holderWidget.getElement().isOrHasChild(loadingDiv.getElement())) {
            holderWidget.getElement().removeChild(loadingDiv.getElement());
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalAsyncGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public MultiSelectionModel<FullDemandDetail> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public Header getCheckHeader() {
        return checkHeader;
    }

    @Override
    public Column<FullDemandDetail, String> getCreatedDateColumn() {
        return createdDateColumn;
    }

    @Override
    public Column<FullDemandDetail, String> getDemnadTitleColumn() {
        return demnadTitleColumn;
    }

    @Override
    public Column<FullDemandDetail, String> getLocalityColumn() {
        return localityColumn;
    }

    /** Filter. **/
    @Override
    public DecoratorPanel getFilterLabelPanel() {
        return filterLabelPanel;
    }

    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    /** Button. **/
    @Override
    public Button getApproveBtn() {
        return approveBtn;
    }

    @Override
    public Button getCreateConversationBtn() {
        return createConversationBtn;
    }

    /** Widget view. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
