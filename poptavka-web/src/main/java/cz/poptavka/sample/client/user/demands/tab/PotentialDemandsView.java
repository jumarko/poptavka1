package cz.poptavka.sample.client.user.demands.tab;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.DemandDetail;

/**
 * View representing potential demands for supplier. Supplier can list them, reply to selected,
 * cancel displaying and other magic tricks
 *
 * @author Beho
 *
 */
public class PotentialDemandsView extends Composite implements PotentialDemandsPresenter.IPotentialDemands {

    private static PotentialDemandsViewUiBinder uiBinder = GWT.create(PotentialDemandsViewUiBinder.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    interface PotentialDemandsViewUiBinder extends UiBinder<Widget, PotentialDemandsView> {
    }

    @UiField(provided = true)
    CellTable<DemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;
    private ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();

    private final SingleSelectionModel<DemandDetail> selectionModel =
        new SingleSelectionModel<DemandDetail>(KEY_PROVIDER);

    @Override
    public void createView() {
        initCellWidget();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initCellWidget() {

        // Init table
        cellTable = new CellTable<DemandDetail>(KEY_PROVIDER);
        cellTable.setPageSize(5);
        cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        cellTable.setSelectionModel(selectionModel);

        // dataProvider
        dataProvider.addDataDisplay(cellTable);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);

        ListHandler<DemandDetail> sorHandler = new ListHandler<DemandDetail>(dataProvider.getList());
        cellTable.addColumnSortHandler(sorHandler);

        initTableColumns(sorHandler);
    }


    private void initTableColumns(ListHandler<DemandDetail> sortHandler) {
        // Demand Title Column
        Column<DemandDetail, String> titleColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return object.getTitle();
            }
        });

        // Demand Price Column
        Column<DemandDetail, String> priceColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return object.getPrice().toString();
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<DemandDetail, String> endDateColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // Demand Finish Column
        Column<DemandDetail, String> expireDateColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return dateFormat.format(object.getExpireDate());
            }
        });

        // sort methods
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                GWT.log("sort");
                if (o1 == o2) {
                    return 0;
                }

                // Compare the name columns.
                if (o1 != null) {
                    return (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
                }
                return -1;
            }
        });
//        priceColumn.setSortable(true);
//        sortHandler.setComparator(priceColumn, new Comparator<DemandDetail>() {
//            @Override
//            public int compare(DemandDetail o1, DemandDetail o2) {
//                return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
//            }
//        });

        // add columns into table
        cellTable.addColumn(titleColumn, MSGS.title());
        cellTable.addColumn(priceColumn, MSGS.price());
        cellTable.addColumn(endDateColumn, MSGS.endDate());
        cellTable.addColumn(expireDateColumn, MSGS.expireDate());


    }


    // TODO Copied
    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {
        C getValue(DemandDetail demandDetail);
    }

    // TODO Copied
    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<DemandDetail, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<DemandDetail, C> fieldUpdater) {

        Column<DemandDetail, C> column = new Column<DemandDetail, C>(cell) {
            @Override
            public C getValue(DemandDetail object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
//        if (cell instanceof AbstractEditableCell<?, ?>) {
//            editableCells.add((AbstractEditableCell<?, ?>) cell);
//        }
        cellTable.addColumn(column, headerText);
        return column;

    }


    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SingleSelectionModel<DemandDetail> getSelectionModel() {
        return selectionModel;
    }

    private static final ProvidesKey<DemandDetail> KEY_PROVIDER = new ProvidesKey<DemandDetail>() {
        @Override
        public Object getKey(DemandDetail item) {
            return item == null ? null : item.getId();
        }
    };

    @Override
    public ListDataProvider<DemandDetail> getProvider() {
        return dataProvider;
    }

}
