package cz.poptavka.sample.client.user.demands.widgets;

import java.util.Comparator;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.NoSelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;

public class GlobalDemandOfferTable extends CellTable<OfferDemandDetail> {

    private ListDataProvider<OfferDemandDetail> dataProvider = new ListDataProvider<OfferDemandDetail>();

    public GlobalDemandOfferTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<OfferDemandDetail> sorHandler = new ListHandler<OfferDemandDetail>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final SelectionModel<OfferDemandDetail> selectionModel = new NoSelectionModel<OfferDemandDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel, DefaultSelectionEventManager.<OfferDemandDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<OfferDemandDetail> tableSelectionModel,
            ListHandler<OfferDemandDetail> sortHandler, LocalizableMessages msgs) {
        // MultipleSelection Checkbox
        // Column<OfferDemandDetail, Boolean> checkBoxColumn = new Column<OfferDemandDetail, Boolean>(
        // new CheckboxCell(true, false)) {
        // @Override
        // public Boolean getValue(OfferDemandDetail object) {
        // return tableSelectionModel.isSelected(object);
        // }
        // };

        // Demand Title Column
        Column<OfferDemandDetail, String> titleColumn = (new Column<OfferDemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return object.getTitle() + "(" + object.getNumberOfOffers() + "/" + object.getMaxOffers() + ")";
            }
        });

        // Demand Price Column
        Column<OfferDemandDetail, String> priceColumn = (new Column<OfferDemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                // TODO add 'none' value into Localizable resources
                return (object.getPrice() == null ? "None" : object.getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<OfferDemandDetail, String> endDateColumn = (new Column<OfferDemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // Demand sent Date column
        Column<OfferDemandDetail, String> validToDateColumn = (new Column<OfferDemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn, new Comparator<OfferDemandDetail>() {
            @Override
            public int compare(OfferDemandDetail o1, OfferDemandDetail o2) {
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
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn, new Comparator<OfferDemandDetail>() {
            @Override
            public int compare(OfferDemandDetail o1, OfferDemandDetail o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });

        endDateColumn.setSortable(true);
//        validToDateColumn.setSortable(true);
//        Comparator<OfferDemandDetail> dateComparator = new Comparator<OfferDemandDetail>() {
//            @Override
//            public int compare(OfferDemandDetail o1, OfferDemandDetail o2) {
//                // TODO Auto-generated method stub
//                return o1.getEndDate().compareTo(o2.getEndDate());
//            }
//        };
//        sortHandler.setComparator(endDateColumn, dateComparator);
//        sortHandler.setComparator(validToDateColumn, dateComparator);
        // add columns into table
        // this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
        this.addColumn(endDateColumn, msgs.endDate());
        this.addColumn(validToDateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<OfferDemandDetail> KEY_PROVIDER = new ProvidesKey<OfferDemandDetail>() {
        @Override
        public Object getKey(OfferDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public ListDataProvider<OfferDemandDetail> getDataProvider() {
        return dataProvider;
    }

}
