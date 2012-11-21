package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategoryTreeViewModel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.AsyncDataGrid;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.resources.TreeResources;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.cell.RatingCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
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
    //Table constants
    private static final int SUPPLIER_NAME_COL_WIDTH = 120;
    private static final int RATING_COL_WIDTH = 50;
    private static final int ADDRESS_COL_WIDTH = 60;
    private static final int LOGO_COL_WIDTH = 25;
    //
    private static final Logger LOGGER = Logger.getLogger("SupplierCreationView");
    @UiField(provided = true)
    CellTree cellTree;
    @UiField(provided = true)
    UniversalAsyncGrid<FullSupplierDetail> dataGrid;
    @UiField(provided = true)
    UniversalPagerWidget pager;
    @UiField
    Label reklama, filterLabel;
    @UiField
    DecoratorPanel filterLabelPanel;
    @UiField
    HTMLPanel detail;
    @UiField
    SupplierDetailView supplierDetailView;
    @UiField
    Button contactBtn;
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "", "businessUser.businessUserData.companyName", "overalRating", "businessUser.address.city"
            });
    /**
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
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
        initTableAndPager();
        initCellTree();
        initWidget(uiBinder.createAndBindUi(this));

        reklama.setVisible(true);
        detail.setVisible(false);
        LOGGER.info("CreateView pre DisplaySuppliers");

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    public void initCellTree() {
        //Workaround for issue: CellTree disappeared when clicking but outside tree nodes
        CellTree.Resources resource = GWT.create(TreeResources.class);
        StyleInjector.injectAtEnd("." + resource.cellTreeStyle().cellTreeTopItem() + " {margin-top: 0px;}");
        cellTree = new CellTree(new CategoryTreeViewModel(
                selectionCategoryModel,
                homeSuppliersPresenter.getCategoryService(),
                Constants.WITHOUT_CHECK_BOXES,
                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS), null, resource);
        Storage.setTree(cellTree);
        // cellTree.setSize("300px", "100px");
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
        dataGrid = new UniversalAsyncGrid<FullSupplierDetail>(
                KEY_PROVIDER, gridColumns, pager.getPageSize(), resource);
        dataGrid.setMinimumTableWidth(LOGO_COL_WIDTH + SUPPLIER_NAME_COL_WIDTH
                + RATING_COL_WIDTH + ADDRESS_COL_WIDTH, Unit.PX);
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
        dataGrid.addColumn(logoCol, Storage.MSGS.logo());
        dataGrid.setColumnWidth(logoCol, LOGO_COL_WIDTH, Unit.PX);

        // Company name.
        /**************************************************************************/
        //IdentityColumn - A passthrough column, useful for giving cells access to the entire row object.
        Column<FullSupplierDetail, SupplierCell> companyNameCol = new IdentityColumn(new SupplierCell());
        companyNameCol.setSortable(true);
        dataGrid.addColumn(companyNameCol, Storage.MSGS.supplierName());
        dataGrid.setColumnWidth(companyNameCol, SUPPLIER_NAME_COL_WIDTH, Unit.PX);

        // SupplierRating.
        /**************************************************************************/
        Column<FullSupplierDetail, RatingCell> ratingCol = new IdentityColumn(new RatingCell());
        //set column style
        ratingCol.setSortable(true);
        ratingCol.setCellStyleNames(Storage.RSCS.grid().cellTableLogoColumn());
        dataGrid.addColumn(ratingCol, Storage.MSGS.rating());
        dataGrid.setColumnWidth(ratingCol, RATING_COL_WIDTH, Unit.PX);

        // Address.
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.address(), false, ADDRESS_COL_WIDTH,
                new UniversalAsyncGrid.GetValue() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (AddressDetail addr : ((FullSupplierDetail) object).getAddresses()) {
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

    @Override
    public DecoratorPanel getFilterLabelPanel() {
        return filterLabelPanel;
    }

    /** Detail. **/
    @Override
    public void displaySuppliersDetail(FullSupplierDetail fullSupplierDetail) {
        reklama.setVisible(false);
        detail.setVisible(true);

        supplierDetailView.setSupplierDetail(fullSupplierDetail);
    }

    @Override
    public void hideSuppliersDetail() {
        reklama.setVisible(true);
        detail.setVisible(false);
    }

    /** Buttons. **/
    @Override
    public Button getContactBtn() {
        return contactBtn;
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
