package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
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
    @UiField Button approveBtn;
    @UiField Label bannerLabel, filterLabel;
    @UiField DecoratorPanel filterLabelPanel;
    @UiField DemandDetailView demandDetail;

    /** Class attributes. **/
    private Header checkHeader;
    private Column<FullDemandDetail, String> createdDateColumn;
    private Column<FullDemandDetail, String> demnadTitleColumn;
    private Column<FullDemandDetail, String> localityColumn;
    private MultiSelectionModel selectionModel;
    private final DateTimeFormat formatter = DateTimeFormat.getFormat(Storage.MSGS.formatDate());
    private static final List<String> GRID_COLUMNS = Arrays.asList(
            new String[]{
                "createdDate", "title", "locality", "endDate"
            });
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
        dataGrid = new UniversalAsyncGrid<FullDemandDetail>(GRID_COLUMNS, pager.getPageSize(), resource);
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
        createdDateColumn = dataGrid.addColumn(new ClickableTextCell(), Storage.MSGS.columnCreatedDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        FullDemandDetail demandDetail = (FullDemandDetail) object;
                        if (demandDetail.getCreated() == null) {
                            return Storage.MSGS.commonNotDefined();
                        } else {
                            Date now = new Date();
                            long millis = now.getTime() - demandDetail.getCreated().getTime();
                            if (Constants.DAY_LENGTH <= millis && millis < 2 * Constants.DAY_LENGTH) {
                                return Storage.MSGS.creationDateYesterday();
                            } else {
                                return formatter.format(demandDetail.getCreated());
                            }
                        }
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

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void displayDemandDetail(FullDemandDetail fullDemandDetail) {
        bannerLabel.setVisible(false);
        demandDetail.setVisible(true);

        demandDetail.setDemanDetail(fullDemandDetail);
    }

    @Override
    public void hideDemandDetail() {
        bannerLabel.setVisible(true);
        demandDetail.setVisible(false);
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

    /** Widget view. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
