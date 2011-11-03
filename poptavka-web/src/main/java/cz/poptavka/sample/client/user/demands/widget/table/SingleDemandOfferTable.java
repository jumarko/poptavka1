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
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

public class SingleDemandOfferTable extends CellTable<FullOfferDetail> {

    private ListDataProvider<FullOfferDetail> dataProvider
        = new ListDataProvider<FullOfferDetail>();

    public SingleDemandOfferTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<FullOfferDetail> sorHandler = new ListHandler<FullOfferDetail>(
                dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final MultiSelectionModel<FullOfferDetail> selectionModel = new MultiSelectionModel<FullOfferDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<FullOfferDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<FullOfferDetail> tableSelectionModel,
        ListHandler<FullOfferDetail> sortHandler, LocalizableMessages msgs) {
        // MultipleSelection Checkbox
//        Column<FullOfferDetail, Boolean> checkBoxColumn = new Column<FullOfferDetail, Boolean>(
//                new CheckboxCell(true, false)) {
//            @Override
//            public Boolean getValue(FullOfferDetail object) {
//                return tableSelectionModel.isSelected(object);
//            }
//        };

        // Demand Title Column
        Column<FullOfferDetail, String> titleColumn = (new Column<FullOfferDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(FullOfferDetail object) {
                return object.getOfferDetail().getSupplierName();
            }
        });

        // Demand Price Column
        Column<FullOfferDetail, String> priceColumn = (new Column<FullOfferDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(FullOfferDetail object) {
                // TODO add 'none' value into Localizable resources
                return (object.getOfferDetail().getPrice() == null ? "None"
                        : object.getOfferDetail().getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_MEDIUM);

//        // Demand Finish Column
//        Column<FullOfferDetail, String> endDateColumn = (new Column<FullOfferDetail, String>(
//                new TextCell()) {
//            @Override
//            public String getValue(FullOfferDetail object) {
//                return dateFormat.format(object.getEndDate());
//            }
//        });
//
//        // Demand sent Date column
//        Column<FullOfferDetail, String> validToDateColumn = (new Column<FullOfferDetail, String>(
//                new TextCell()) {
//            @Override
//            public String getValue(FullOfferDetail object) {
//                return dateFormat.format(object.getFinishDate());
//            }
//        });

//        // sort methods ****************************
//        titleColumn.setSortable(true);
//        sortHandler.setComparator(titleColumn, new Comparator<FullOfferDetail>() {
//            @Override
//            public int compare(FullOfferDetail o1,
//                    FullOfferDetail o2) {
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
//        sortHandler.setComparator(priceColumn, new Comparator<FullOfferDetail>() {
//            @Override
//            public int compare(FullOfferDetail o1,
//                    FullOfferDetail o2) {
//                return o1.getPrice().compareTo(o2.getPrice());
//            }
//        });
//
//        endDateColumn.setSortable(true);
//        validToDateColumn.setSortable(true);
//        Comparator<FullOfferDetail> dateComparator = new Comparator<FullOfferDetail>() {
//            @Override
//            public int compare(FullOfferDetail o1, FullOfferDetail o2) {
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

    private static final ProvidesKey<FullOfferDetail> KEY_PROVIDER = new ProvidesKey<FullOfferDetail>() {
        @Override
        public Object getKey(FullOfferDetail item) {
            return item == null ? null : item.getOfferDetail().getDemandId();
        }
    };

    public  ListDataProvider<FullOfferDetail> getDataProvider() {
        return dataProvider;
    }

}
