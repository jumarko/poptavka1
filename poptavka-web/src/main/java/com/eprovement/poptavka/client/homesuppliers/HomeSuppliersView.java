package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategoryTreeViewModel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
    private static final int SUPPLIER_NAME_COL_WIDTH = 100;
    private static final int RATING_COL_WIDTH = 30;
    private static final int ADDRESS_COL_WIDTH = 60;
    private static final int LOCALITY_COL_WIDTH = 50;
    //
    private static final Logger LOGGER = Logger.getLogger("SupplierCreationView");
    CellList categoriesList;
    @UiField(provided = true)
    CellTree cellTree;
    @UiField(provided = true)
    UniversalAsyncGrid<FullSupplierDetail> dataGrid;
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    @UiField
    Label reklama, filterLabel;
    @UiField
    HTMLPanel detail;
    @UiField
    SupplierDetailView supplierDetail;
    @UiField
    Button contactBtn;
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "businessUser.businessUserData.companyName", "overalRating", "", ""
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
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(0);

        initDataGrid();
        initCellTree();
        initWidget(uiBinder.createAndBindUi(this));

        reklama.setVisible(true);
        detail.setVisible(false);
        LOGGER.info("CreateView pre DisplaySuppliers");
    }

    public void initCellTree() {
        //Workaround for issue: CellTree disappeared when clicking but outside tree nodes
        CellTree.Resources resource = GWT.create(CellTree.Resources.class);
        StyleInjector.injectAtEnd("." + resource.cellTreeStyle().cellTreeTopItem() + " {margin-top: 0px;}");
        cellTree = new CellTree(new CategoryTreeViewModel(
                selectionCategoryModel,
                homeSuppliersPresenter.getCategoryService(),
                Constants.WITHOUT_CHECK_BOXES,
                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS), null);
        Storage.setTree(cellTree);
        // cellTree.setSize("300px", "100px");
        cellTree.setAnimationEnabled(true);
    }

    private void initDataGrid() {
        // Create a DataGrid.
        GWT.log("Admin Suppliers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new UniversalAsyncGrid<FullSupplierDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setEmptyTableWidget(new Label(Storage.MSGS.noData()));

        dataGrid.setSelectionModel(new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER));

        dataGrid.setMinimumTableWidth(SUPPLIER_NAME_COL_WIDTH + RATING_COL_WIDTH
                + ADDRESS_COL_WIDTH + LOCALITY_COL_WIDTH, Unit.PX);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);
        pager.setPageSize(this.getPageSize());
        StyleResource.INSTANCE.layout().ensureInjected();

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add columns to the table.
     */
    private void initTableColumns() {

        // Company name.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((FullSupplierDetail) object).getCompanyName();
                    }
                });

        // SupplierRating.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.rating(), true, RATING_COL_WIDTH,
                new UniversalAsyncGrid.GetValue() {
                    @Override
                    public String getValue(Object object) {
                        if (((FullSupplierDetail) object).getOverallRating() == -1) {
                            return "";
                        } else {
                            return Integer.toString(((FullSupplierDetail) object).getOverallRating());
                        }
                    }
                });

        // Address.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.address(), false, ADDRESS_COL_WIDTH,
                new UniversalAsyncGrid.GetValue() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (AddressDetail detail : ((FullSupplierDetail) object).getAddresses()) {
                            str.append(detail.getCity());
                            str.append(", ");
                        }
                        if (str.length() != 0) {
                            str.delete(str.length() - 1, str.length());
                        }
                        return str.toString();
                    }
                });

        // Locality.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.locality(), false, LOCALITY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        if (((FullSupplierDetail) object).getLocalities() != null) {
                            for (LocalityDetail loc : ((FullSupplierDetail) object).getLocalities()) {
                                str.append(loc.getName());
                                str.append(", ");
                            }
                            if (str.length() > 2) {
                                str.delete(str.length() - 2, str.length());
                            }
                        }
                        return str.toString();
                    }
                });
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public UniversalAsyncGrid getDataGrid() {
        return dataGrid;
    }

    @Override
    public CellList getCategoriesList() {
        return categoriesList;
    }

    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    @Override
    public SingleSelectionModel getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

    @Override
    public SplitLayoutPanel getSplitter() {
        return null; //split;
    }

    @Override
    public Button getContactBtn() {
        return contactBtn;
    }

    @Override
    public void displaySuppliersDetail(FullSupplierDetail fullSupplierDetail) {
        reklama.setVisible(false);
        detail.setVisible(true);

        supplierDetail.setSupplierDetail(fullSupplierDetail);
    }

    @Override
    public void hideSuppliersDetail() {
        reklama.setVisible(true);
        detail.setVisible(false);
    }
}
