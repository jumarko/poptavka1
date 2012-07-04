package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.OverflowComposite;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HomeSuppliersView extends OverflowComposite
        implements HomeSuppliersPresenter.SuppliersViewInterface {

    private static SuppliersViewUiBinder uiBinder = GWT.create(SuppliersViewUiBinder.class);

    interface SuppliersViewUiBinder extends UiBinder<Widget, HomeSuppliersView> {
    }
    //Table constants
    private static final int SUPPLIER_NAME_COL_WIDTH = 100;
    private static final int RATING_COL_WIDTH = 30;
    private static final int ADDRESS_COL_WIDTH = 60;
    private static final int LOCALITY_COL_WIDTH = 50;
    private static final int TABLE_HEIGHT = 300;
    //
    private static final Logger LOGGER = Logger.getLogger("SupplierCreationView");
    @UiField(provided = true)
    CellList categoriesList;
    @UiField(provided = true)
    DataGrid dataGrid;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    FlowPanel path;
    @UiField
    HorizontalPanel root;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    @UiField
    Label reklama, filterLabel, categoryLoadingLabel;
    @UiField
    HTMLPanel detail, child;
    @UiField
    SupplierDetailView supplierDetail;
    @UiField
    Button contactBtn;
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>();
    private SingleSelectionModel<FullSupplierDetail> selectionSupplierModel;
    private final SingleSelectionModel<CategoryDetail> selectionRootModel =
            new SingleSelectionModel<CategoryDetail>();

    @Override
    public void createView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(0);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        path.setStyleName(StyleResource.INSTANCE.common().hyperlinkInline());
        reklama.setVisible(true);

        detail.setVisible(false);
        LOGGER.info("CreateView pre DisplaySuppliers");
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    @Override
    public Label getCategoryLoadingLabel() {
        return categoryLoadingLabel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public FlowPanel getPath() {
        return path;
    }

    //removes last one
    @Override
    public void removePath() {
        path.remove(path.getWidgetCount() - 1);
    }

    @Override
    public void addPath(Widget widget) {
        path.add(widget);
    }

    @Override
    public DataGrid getDataGrid() {
        return dataGrid;
    }

    @Override
    public CellList getCategoriesList() {
        return categoriesList;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public SingleSelectionModel getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

    @Override
    public SingleSelectionModel getSelectionSupplierModel() {
        return selectionSupplierModel;
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

        supplierDetail.displaySuppliersDetail(fullSupplierDetail);
    }

    @Override
    public void hideSuppliersDetail() {
        reklama.setVisible(true);
        detail.setVisible(false);
    }

    @Override
    public HTMLPanel getChildSection() {
        return child;
    }

    //************ ROOT SECTION ********************************************
    @Override
    public HorizontalPanel getRootSection() {
        return root;
    }

    @Override
    public SingleSelectionModel getSelectionRootModel() {
        return selectionRootModel;
    }

    @Override
    public void displayRootCategories(int columns, ArrayList<CategoryDetail> rootCategories) {
        if (rootCategories.isEmpty()) {
            root.clear();
            return;
        }
        root.clear();
        int size = rootCategories.size();
        int subSize = 0;
        int startIdx = 0;
        if (size < columns) {
            columns = size;
        }
        while (columns != 0) {
            if (size % columns == 0) {
                subSize = size / columns;
            } else {
                subSize = size / columns + 1;
            }
            CellList cellList = null;
            cellList = new CellList<CategoryDetail>(new RootCategoryCell());
            //TOTO Martin - loading indikator nepomoze, pretoze tieto cellListy sa vytvaraju
            //tu v case, ked su data uz k dispozicii
            cellList.setLoadingIndicator(new Label(Storage.MSGS.loadingRootCategories()));
            cellList.setRowCount(subSize, true);
            cellList.setSelectionModel(selectionRootModel);
            cellList.setRowData(rootCategories.subList(startIdx, startIdx + subSize));
            root.add(cellList);
            startIdx += subSize;
            size -= subSize;
            columns--;
        }
    }

    private void initDataGrid() {
        categoriesList = new CellList<CategoryDetail>(new SubCategoryCell());
        categoriesList.setSelectionModel(selectionCategoryModel);
        categoriesList.setLoadingIndicator(new Label(Storage.MSGS.loadingCategories()));
        // Create a DataGrid.
        GWT.log("Admin Suppliers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<FullSupplierDetail>();
        dataGrid.setEmptyTableWidget(new Label(Storage.MSGS.noData()));
        selectionSupplierModel = new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(selectionSupplierModel);

        dataGrid.setMinimumTableWidth(SUPPLIER_NAME_COL_WIDTH + RATING_COL_WIDTH
                + ADDRESS_COL_WIDTH + LOCALITY_COL_WIDTH, Unit.PX);
        dataGrid.setHeight(Integer.toString(TABLE_HEIGHT) + "px");

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
        addColumn(new TextCell(), Storage.MSGS.supplierName(), true, SUPPLIER_NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(FullSupplierDetail object) {
                        return object.getCompanyName();
                    }
                });

        // SupplierRating.
        addColumn(new TextCell(), Storage.MSGS.rating(), true, RATING_COL_WIDTH,
                new GetValue() {

                    @Override
                    public String getValue(FullSupplierDetail object) {
                        if (object.getOverallRating() == -1) {
                            return "";
                        } else {
                            return Integer.toString(object.getOverallRating());
                        }
                    }
                });

        // Address.
        addColumn(new TextCell(), Storage.MSGS.address(), false, ADDRESS_COL_WIDTH,
                new GetValue() {

                    @Override
                    public String getValue(FullSupplierDetail object) {
                        StringBuilder str = new StringBuilder();
                        for (AddressDetail detail : object.getAddresses()) {
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
        addColumn(new TextCell(), Storage.MSGS.locality(), false, LOCALITY_COL_WIDTH,
                new GetValue() {

                    @Override
                    public String getValue(FullSupplierDetail object) {
                        StringBuilder str = new StringBuilder();
                        if (object.getLocalities() != null) {
                            for (String loc : object.getLocalities().values()) {
                                str.append(loc);
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

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

        C getValue(FullSupplierDetail supplierDetailForDisplaySuppliers);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<FullSupplierDetail, C> addColumn(Cell<C> cell,
            String headerText, boolean sort, int width, final GetValue<C> getter) {
        Column<FullSupplierDetail, C> column = new Column<FullSupplierDetail, C>(cell) {

            @Override
            public C getValue(FullSupplierDetail demand) {
                return getter.getValue(demand);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
    private static final ProvidesKey<FullSupplierDetail> KEY_PROVIDER = new ProvidesKey<FullSupplierDetail>() {

        @Override
        public Object getKey(FullSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };
}

/**
 * Root Category Cell .
 */
class RootCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getSuppliers());
        text.append(")");

        sb.appendEscaped(text.toString());
    }
}

/**
 * Sub Category Cell .
 */
class SubCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getSuppliers());
        text.append(")");

        sb.appendEscaped(text.toString());
    }
}