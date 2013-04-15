package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategoryTreeViewModel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.footer.FooterView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.celltree.CustomCellTree;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.client.user.widget.detail.UserDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Home suppliers module's view.
 *
 * @author Martin Slavkovsky
 */
public class HomeSuppliersView extends OverflowComposite
        implements ReverseViewInterface<HomeSuppliersPresenter>, HomeSuppliersPresenter.SuppliersViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface SuppliersViewUiBinder extends UiBinder<Widget, HomeSuppliersView> {
    }
    private static SuppliersViewUiBinder uiBinder = GWT.create(SuppliersViewUiBinder.class);
    /**************************************************************************/
    /* Home Supplier presenter                                                */
    /**************************************************************************/
    private HomeSuppliersPresenter homeSuppliersPresenter;

    @Override
    public void setPresenter(HomeSuppliersPresenter presenter) {
        this.homeSuppliersPresenter = presenter;
    }

    @Override
    public HomeSuppliersPresenter getPresenter() {
        return homeSuppliersPresenter;
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    @UiField(provided = true) CellTree cellTree;
    @UiField(provided = true) UniversalAsyncGrid<FullSupplierDetail> dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField(provided = true) Widget footer;
    @UiField DockLayoutPanel detailPanel;
    @UiField Label reklama, filterLabel;
    @UiField UserDetailView userDetailView;
    /** Class Attributes. **/
    private @Inject FooterView footerView;
    private static final Logger LOGGER = Logger.getLogger("SupplierCreationView");
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "", "businessUser.businessUserData.companyName", "overalRating", "businessUser.address.city"
            });
    /** Constants. **/
    private static final String ADDRESS_COL_WIDTH = "120px";
    private static final String LOGO_COL_WIDTH = "70px";
    /** The key provider that provides the unique ID of a FullSupplierDetail. */
    private static final ProvidesKey<FullSupplierDetail> KEY_PROVIDER = new ProvidesKey<FullSupplierDetail>() {
        @Override
        public Object getKey(FullSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        footer = footerView;

        initTableAndPager();
        initCellTree();
        initWidget(uiBinder.createAndBindUi(this));

        LOGGER.info("CreateView pre DisplaySuppliers");

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    public void initCellTree() {
        //Workaround for issue: CellTree disappeared when clicking but outside tree nodes
        CellTree.Resources resource = GWT.create(CustomCellTree.class);
        StyleInjector.injectAtEnd("." + resource.cellTreeStyle().cellTreeTopItem() + " {margin-top: 0px;}");
        cellTree = new CellTree(new CategoryTreeViewModel(
                selectionCategoryModel,
                homeSuppliersPresenter.getCategoryService(),
                homeSuppliersPresenter.getEventBus(),
                Constants.WITHOUT_CHECK_BOXES,
                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS,
                homeSuppliersPresenter.getCategoryLoadingHandler()), null, resource);
        cellTree.setAnimationEnabled(true);
    }

    private void initTableAndPager() {
        // Create a DataGrid.
        GWT.log("Admin Suppliers initDataGrid initialized");

        //Create pager
        pager = new UniversalPagerWidget();

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalAsyncGrid<FullSupplierDetail>(gridColumns, pager.getPageSize(), resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        // Selection handler
        dataGrid.setSelectionModel(new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER));

        // bind pager to grid
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
        Column<FullSupplierDetail, ImageResource> logoCol =
                new Column<FullSupplierDetail, ImageResource>(new ImageResourceCell()) {
                    @Override
                    public ImageResource getValue(FullSupplierDetail object) {
                        //return eventBus.getSupplerLogo(object.getLogoId()); -- returns ImageResource
                        return Storage.RSCS.images().contactImage();
                    }
                };
        //set column style
        logoCol.setSortable(false);
        logoCol.setCellStyleNames(Storage.RSCS.grid().cellTableLogoColumn());
        dataGrid.addColumn(logoCol, Storage.MSGS.columnLogo());
        dataGrid.setColumnWidth(logoCol, LOGO_COL_WIDTH);

        // Company name.
        /**************************************************************************/
        //IdentityColumn - A passthrough column, useful for giving cells access to the entire row object.
        Column<FullSupplierDetail, SupplierCell> companyNameCol = new IdentityColumn(new SupplierCell());
        companyNameCol.setSortable(true);
        dataGrid.addColumn(companyNameCol, Storage.MSGS.columnSupplierName());
        dataGrid.setColumnWidth(companyNameCol, Constants.COL_WIDTH_TITLE);

        // SupplierRating.
        /**************************************************************************/
        dataGrid.addRatingColumn();

        // Address.
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnAddress(), false, ADDRESS_COL_WIDTH,
                new UniversalAsyncGrid.GetValue() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (AddressDetail addr : ((FullSupplierDetail) object).getUserData().getAddresses()) {
                            str.append(addr.getCity());
                            str.append(", ");
                        }
                        if (!str.toString().isEmpty()) {
                            str.delete(str.length() - 2, str.length());
                        }
                        return str.toString();
                    }
                });
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** CellTree. **/
    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    @Override
    public SingleSelectionModel getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

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

    /** Detail. **/
    @Override
    public void displaySuppliersDetail(FullSupplierDetail fullSupplierDetail) {
        reklama.setVisible(false);
        detailPanel.setVisible(true);

        userDetailView.setSupplierDetail(fullSupplierDetail);
    }

    @Override
    public void hideSuppliersDetail() {
        reklama.setVisible(true);
        detailPanel.setVisible(false);
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
