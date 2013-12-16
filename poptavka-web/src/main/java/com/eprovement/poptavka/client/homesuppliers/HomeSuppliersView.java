package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersPresenter.HomeSuppliersViewInterface;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.AddressColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.LogoColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail.SupplierField;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;
import java.util.List;

/**
 * Home suppliers module's view.
 *
 * @author Martin Slavkovsky
 */
public class HomeSuppliersView extends OverflowComposite implements HomeSuppliersViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface HomeSuppliersViewUiBinder extends UiBinder<Widget, HomeSuppliersView> {
    }
    private static HomeSuppliersViewUiBinder uiBinder = GWT.create(HomeSuppliersViewUiBinder.class);

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid<FullSupplierDetail> dataGrid;
    @UiField SimplePanel detailPanel, footerPanel, categoryTreePanel;
    @UiField Label filterLabel;
    /** Class attributes. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);
    private UniversalPagerWidget pager;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initTableAndPager();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initTableAndPager() {
        // Create a DataGrid.
        GWT.log("Admin Suppliers initDataGrid initialized");

        //Create pager
        pager = new UniversalPagerWidget();

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        CssInjector.INSTANCE.ensureGridStylesInjected(GRSCS);
        dataGrid = new UniversalAsyncGrid<FullSupplierDetail>(initSort(), pager.getPageSize(), GRSCS);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        // Selection handler
        dataGrid.setSelectionModel(new SingleSelectionModel<FullSupplierDetail>(FullSupplierDetail.KEY_PROVIDER));

        // bind pager to grid
        pager.addStyleName("item");
        pager.setDisplay(dataGrid);

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add columns to the table.
     */
    private void initTableColumns() {

        // Company logo
        /**************************************************************************/
        dataGrid.addColumn(
            MSGS.columnLogo(),
            GRSCS.dataGridStyle().colWidthLogo(),
            new LogoColumn());

        // Company name.
        /**************************************************************************/
        //IdentityColumn - A passthrough column, useful for giving cells access to the entire row object.
        Column<FullSupplierDetail, SupplierCell> companyNameCol = new IdentityColumn(new SupplierCell());
        companyNameCol.setSortable(false);
        dataGrid.addColumn(
            MSGS.columnSupplierName(),
            GRSCS.dataGridStyle().colWidthTitle(),
            companyNameCol);
        dataGrid.getHeader(dataGrid.getColumnIndex(companyNameCol)).setHeaderStyleNames("companyHeader");

        // SupplierRating.
        /**************************************************************************/
        dataGrid.addColumn(
            MSGS.columnRating(),
            GRSCS.dataGridStyle().colWidthRatting(),
            new RatingColumn(null));

        // Address.
        /**************************************************************************/
        dataGrid.addColumn(
            MSGS.columnAddress(),
            GRSCS.dataGridStyle().colWidthLocality(),
            new AddressColumn(null));
    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList(
                null, //Logo
                new SortPair(UserField.COMPANY_NAME),
                new SortPair(SupplierField.OVERALL_RATING),
                null); //Address
        List<SortPair> defaultSort = Arrays.asList(
                new SortPair(SupplierField.OVERALL_RATING));
        return new SortDataHolder(defaultSort, sortColumns);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** Table. **/
    @Override
    public UniversalAsyncGrid<FullSupplierDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    /** Filter. **/
    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    /** Other. **/
    @Override
    public SimplePanel getCategoryTreePanel() {
        return categoryTreePanel;
    }

    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public SimplePanel getFooterPanel() {
        return footerPanel;
    }

    @Override
    public Widget getToolbarContent() {
        return pager;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
