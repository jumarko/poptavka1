/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.user.client.ui.SimplePanel;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.demand.DemandOriginDetail;
import java.util.Comparator;

/**
 *
 * @author martin.slavkovsky
 */
public class AdminDemandOriginView extends Composite implements
        AdminDemandOriginPresenter.AdminDemandTypeInterface {

    private static AdministrationViewUiBinder uiBinder =
            GWT.create(AdministrationViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh, addRow, deleteRow;
    @UiField
    Label changesLabel;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SimplePanel getAdminSupplierDetail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdminDemandOriginView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<DemandOriginDetail> dataGrid;
    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<DemandOriginDetail> dataProvider;
    private SingleSelectionModel<DemandOriginDetail> selectionModel;
    private ListHandler<DemandOriginDetail> sortHandler;
    /** Editable Columns in CellTable. **/
    private Column<DemandOriginDetail, String> idColumn;
    private Column<DemandOriginDetail, String> valueColumn;
    private Column<DemandOriginDetail, String> descriptionColumn;
    private Column<DemandOriginDetail, String> urlColumn;

    public AdminDemandOriginView() {
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        changesLabel.setText("0");
        deleteRow.setEnabled(false);
    }

    @Override
    public Column<DemandOriginDetail, String> getNameColumn() {
        return valueColumn;
    }

    @Override
    public Column<DemandOriginDetail, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    @Override
    public Column<DemandOriginDetail, String> getUrlColumn() {
        return urlColumn;
    }

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("Admin Offers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<DemandOriginDetail>();
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Don't use KeyProvider - not acting right
        // Create dataProvider
        dataProvider = new ListDataProvider<DemandOriginDetail>();
        dataProvider.addDataDisplay(dataGrid);
        // Add a selection model to handle user selection.
        selectionModel = new SingleSelectionModel<DemandOriginDetail>();
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<DemandOriginDetail>createCheckboxManager());
        // Create sortHandler
        sortHandler = new ListHandler<DemandOriginDetail>(dataProvider.getList());
        // Initialize the columns.
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
        // mouse selection.
        addColumn(new CheckboxCell(true, false), "<br/>", 15, new GetValue<Boolean>() {

            @Override
            public Boolean getValue(DemandOriginDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        });

        // ID
        idColumn = addColumn(new TextCell(), "ID", 40, new GetValue<String>() {

            @Override
            public String getValue(DemandOriginDetail detail) {
                return Long.toString(detail.getId());
            }
        });
        sortHandler.setComparator(idColumn, new Comparator<DemandOriginDetail>() {

            @Override
            public int compare(DemandOriginDetail o1, DemandOriginDetail o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        // Value
        valueColumn = addColumn(new EditTextCell(), "Name", 40, new GetValue<String>() {

            @Override
            public String getValue(DemandOriginDetail detail) {
                return detail.getName();
            }
        });
        sortHandler.setComparator(valueColumn, new Comparator<DemandOriginDetail>() {

            @Override
            public int compare(DemandOriginDetail o1, DemandOriginDetail o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // Description
        descriptionColumn = addColumn(new EditTextCell(), "Description", 40, new GetValue<String>() {

            @Override
            public String getValue(DemandOriginDetail detail) {
                return detail.getDescription();
            }
        });
        sortHandler.setComparator(descriptionColumn, new Comparator<DemandOriginDetail>() {

            @Override
            public int compare(DemandOriginDetail o1, DemandOriginDetail o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });

        // Url
        urlColumn = addColumn(new EditTextCell(), "Url", 40, new GetValue<String>() {

            @Override
            public String getValue(DemandOriginDetail detail) {
                return detail.getDescription();
            }
        });
        sortHandler.setComparator(urlColumn, new Comparator<DemandOriginDetail>() {

            @Override
            public int compare(DemandOriginDetail o1, DemandOriginDetail o2) {
                return o1.getUrl().compareTo(o2.getUrl());
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(DemandOriginDetail offerDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<DemandOriginDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<DemandOriginDetail, C> column = new Column<DemandOriginDetail, C>(cell) {

            @Override
            public C getValue(DemandOriginDetail object) {
                return getter.getValue(object);
            }
        };
        if (headerText.endsWith("<br/>")) {
            dataGrid.addColumn(column, SafeHtmlUtils.fromSafeConstant("<br/>"));
        } else {
            column.setSortable(true);
            dataGrid.addColumn(column, headerText);
        }
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }

    @Override
    public DataGrid<DemandOriginDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public Button getAddRowBtn() {
        return addRow;
    }

    @Override
    public Button getDeleteRowBtn() {
        return deleteRow;
    }

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }

    @Override
    public ListDataProvider<DemandOriginDetail> getDataProvider() {
        return dataProvider;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<DemandOriginDetail> getSelectionModel() {
        return selectionModel;
    }
}
