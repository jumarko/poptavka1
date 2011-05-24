/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import cz.poptavka.sample.shared.domain.DemandDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class AdministrationView extends Composite implements
        AdministrationPresenter.AdministrationInterface {

    private static AdministrationViewUiBinder uiBinder =
            GWT.create(AdministrationViewUiBinder.class);

    /**
     * @return the dataProvider
     */
    public ListDataProvider<DemandDetail> getDataProvider() {
        return dataProvider;
    }

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdministrationView> {
    }

    // TODO ivlcek replace with RPC
    private static List<DemandDetail> demandsinfo = generateDemandDetail();
    // TODO ivlcek remove

    private static List<DemandDetail> generateDemandDetail() {
        DemandDetail detail1 = new DemandDetail();
        detail1.setId(1);
        detail1.setTitle("blala1 blala1 blala1 blala1 blala1 blala1");
        Date date1 = new Date(2010, 10, 2);
        detail1.setEndDate(date1);
        detail1.setPrice(new BigDecimal(2000));
        DemandDetail detail2 = new DemandDetail();
        detail2.setId(2);
        detail2.setTitle("blala2");
        Date date2 = new Date(2010, 10, 12);
        detail2.setEndDate(date2);
        detail2.setPrice(new BigDecimal(21000));
        DemandDetail detail3 = new DemandDetail();
        detail3.setId(3);
        detail3.setTitle("blala3");
        Date date3 = new Date(2010, 10, 22);
        detail3.setEndDate(date3);
        detail3.setPrice(new BigDecimal(1500));
        demandsinfo = new ArrayList<DemandDetail>();
        demandsinfo.add(detail1);
        demandsinfo.add(detail2);
        demandsinfo.add(detail3);
        return demandsinfo;
    }

    /**
     * The pager used to change the range of data.
     */
    @UiField
    CellTable<DemandDetail> cellTable;

//    /**
//     * The pager used to change the range of data.
//     */
//    @UiField(provided = true)
//    SimplePager pager;
    /**
     * The list of cells that are editable.
     */
    private List<AbstractEditableCell<?, ?>> editableCells;

    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<DemandDetail> dataProvider;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public CellTable<DemandDetail> getCellTable() {
        return cellTable;
    }

//    @Override
//    public SimplePager getPager() {
//        return pager;
//    }
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initCellTable();
    }

    private void initCellTable() {
        // init the table.
        cellTable.setWidth("100%");
        cellTable.setPageSize(6);
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
        editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

        // TextCell.
        addColumn(new TextCell(), "Title", new GetValue<String>() {

            public String getValue(DemandDetail contact) {
                return contact.getTitle();
            }
        }, null);

        // Add a selection model to handle user selection.
        final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();
        cellTable.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DemandDetail selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    Window.alert("You selected: " + selected.getTitle());
                }
            }
        });


        // Create a data provider.
        dataProvider = new ListDataProvider<DemandDetail>();

        // Connect the table to the data provider.
        getDataProvider().addDataDisplay(cellTable);

        // Add the data to the data provider, which automatically pushes it to
        // the
        // widget.
//        List<DemandDetail> list = getDataProvider().getList();
//        for (DemandDetail demands : demandsinfo) {
//            list.add(demands);
//        }

    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(DemandDetail demandDetail);
    }

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
        if (cell instanceof AbstractEditableCell<?, ?>) {
            editableCells.add((AbstractEditableCell<?, ?>) cell);
        }
        cellTable.addColumn(column, headerText);
        return column;
    }

    /**
     * The key provider that provides the unique ID of a DemandDetail.
     */
    private static final ProvidesKey<DemandDetail> KEY_PROVIDER = new ProvidesKey<DemandDetail>() {

        @Override
        public Object getKey(DemandDetail item) {
            return item == null ? null : item.getId();
        }
    };
}
