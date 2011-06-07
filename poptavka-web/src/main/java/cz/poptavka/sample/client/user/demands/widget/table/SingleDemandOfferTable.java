package cz.poptavka.sample.client.user.demands.widget.table;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.OfferDetail;

public class SingleDemandOfferTable extends CellTable<OfferDetail> {

    private ListDataProvider<OfferDetail> dataProvider
        = new ListDataProvider<OfferDetail>();

    public SingleDemandOfferTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<OfferDetail> sorHandler = new ListHandler<OfferDetail>(
                dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final MultiSelectionModel<OfferDetail> selectionModel = new MultiSelectionModel<OfferDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<OfferDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<OfferDetail> tableSelectionModel,
        ListHandler<OfferDetail> sortHandler, LocalizableMessages msgs) {
        // MultipleSelection Checkbox
//        Column<OfferDetail, Boolean> checkBoxColumn = new Column<OfferDetail, Boolean>(
//                new CheckboxCell(true, false)) {
//            @Override
//            public Boolean getValue(OfferDetail object) {
//                return tableSelectionModel.isSelected(object);
//            }
//        };

        // Demand Title Column
        Column<OfferDetail, String> titleColumn = (new Column<OfferDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDetail object) {
                return object.getSupplierName();
            }
        });

        // Demand Price Column
        Column<OfferDetail, String> priceColumn = (new Column<OfferDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDetail object) {
                // TODO add 'none' value into Localizable resources
                return (object.getPrice() == null ? "None" : object.getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_MEDIUM);

//        // Demand Finish Column
//        Column<OfferDetail, String> endDateColumn = (new Column<OfferDetail, String>(
//                new TextCell()) {
//            @Override
//            public String getValue(OfferDetail object) {
//                return dateFormat.format(object.getEndDate());
//            }
//        });
//
//        // Demand sent Date column
//        Column<OfferDetail, String> validToDateColumn = (new Column<OfferDetail, String>(
//                new TextCell()) {
//            @Override
//            public String getValue(OfferDetail object) {
//                return dateFormat.format(object.getFinishDate());
//            }
//        });

//        // sort methods ****************************
//        titleColumn.setSortable(true);
//        sortHandler.setComparator(titleColumn, new Comparator<OfferDetail>() {
//            @Override
//            public int compare(OfferDetail o1,
//                    OfferDetail o2) {
//                if (o1 == o2) {
//                    return 0;
//                }
//                // Compare the name columns.
//                if (o1 != null) {
//                    return (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
//                }
//                return -1;
//            }
//        });
//        priceColumn.setSortable(true);
//        sortHandler.setComparator(priceColumn, new Comparator<OfferDetail>() {
//            @Override
//            public int compare(OfferDetail o1,
//                    OfferDetail o2) {
//                return o1.getPrice().compareTo(o2.getPrice());
//            }
//        });
//
//        endDateColumn.setSortable(true);
//        validToDateColumn.setSortable(true);
//        Comparator<OfferDetail> dateComparator = new Comparator<OfferDetail>() {
//            @Override
//            public int compare(OfferDetail o1, OfferDetail o2) {
//                // TODO Auto-generated method stub
//                return o1.getEndDate().compareTo(o2.getEndDate());
//            }
//        };
//        sortHandler.setComparator(endDateColumn, dateComparator);
//        sortHandler.setComparator(validToDateColumn, dateComparator);
        // add columns into table
    //    this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
//        this.addColumn(endDateColumn, msgs.endDate());
//        this.addColumn(validToDateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<OfferDetail> KEY_PROVIDER = new ProvidesKey<OfferDetail>() {
        @Override
        public Object getKey(OfferDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public  ListDataProvider<OfferDetail> getDataProvider() {
        return dataProvider;
    }

}
