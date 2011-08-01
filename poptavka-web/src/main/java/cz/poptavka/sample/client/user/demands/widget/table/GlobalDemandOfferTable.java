package cz.poptavka.sample.client.user.demands.widget.table;

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
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;

public class GlobalDemandOfferTable extends CellTable<OfferDemandMessage> {

    private ListDataProvider<OfferDemandMessage> dataProvider = new ListDataProvider<OfferDemandMessage>();

    public GlobalDemandOfferTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<OfferDemandMessage> sorHandler = new ListHandler<OfferDemandMessage>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final SelectionModel<OfferDemandMessage> selectionModel = new NoSelectionModel<OfferDemandMessage>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel, DefaultSelectionEventManager.<OfferDemandMessage>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<OfferDemandMessage> tableSelectionModel,
            ListHandler<OfferDemandMessage> sortHandler, final LocalizableMessages msgs) {
        // MultipleSelection Checkbox
        // Column<OfferDemandMessage, Boolean> checkBoxColumn = new Column<OfferDemandMessage, Boolean>(
        // new CheckboxCell(true, false)) {
        // @Override
        // public Boolean getValue(OfferDemandMessage object) {
        // return tableSelectionModel.isSelected(object);
        // }
        // };

        // Demand Title Column
        Column<OfferDemandMessage, String> titleColumn = (new Column<OfferDemandMessage, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandMessage object) {
                return object.getDemandTitle() + " (" + object.getOfferCount() + "/" + object.getMaxOfferCount() + ")";
            }
        });

        // Demand Price Column
        Column<OfferDemandMessage, String> priceColumn = (new Column<OfferDemandMessage, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandMessage object) {
                // TODO add 'none' value into Localizable resources
                return (object.getPrice() == null ? msgs.notEntered() : object.getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<OfferDemandMessage, String> endDateColumn = (new Column<OfferDemandMessage, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandMessage object) {
                // TODO Temporary null check, CANNOT be null
                if (object.getEndDate() == null) {
                    return "endDate NOT SET, BUT HAVE TO BE";
                }
//                return dateFormat.format(object.getEndDate());

                return object.getEndDate().toString();
            }
        });

        // Demand sent Date column
        Column<OfferDemandMessage, String> createdDateColumn = (new Column<OfferDemandMessage, String>(new TextCell()) {
            @Override
            public String getValue(OfferDemandMessage object) {
                // TODO Temporary null check, CANNOT be null
                if (object.getEndDate() == null) {
                    return "endDate NOT SET, BUT HAVE TO BE";
                }
                return object.getValidToDate().toString();
            }
        });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn, new Comparator<OfferDemandMessage>() {
            @Override
            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
                if (o1 == o2) {
                    return 0;
                }
                // Compare the name columns.
                if (o1 != null) {
                    return (o2 != null) ? o1.getSubject().compareTo(o2.getSubject()) : 1;
                }
                return -1;
            }
        });
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn, new Comparator<OfferDemandMessage>() {
            @Override
            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });
        endDateColumn.setSortable(true);
        createdDateColumn.setSortable(true);
        Comparator<OfferDemandMessage> endComparator = new Comparator<OfferDemandMessage>() {
            @Override
            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
                return o1.getEndDate().compareTo(o2.getEndDate());
            }
        };
        Comparator<OfferDemandMessage> validComparator = new Comparator<OfferDemandMessage>() {
            @Override
            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
                return o1.getValidToDate().compareTo(o2.getValidToDate());
            }
        };
        sortHandler.setComparator(endDateColumn, endComparator);
        sortHandler.setComparator(createdDateColumn, validComparator);
//         add columns into table
//         this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
        this.addColumn(endDateColumn, msgs.endDate());
        this.addColumn(createdDateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<OfferDemandMessage> KEY_PROVIDER = new ProvidesKey<OfferDemandMessage>() {
        @Override
        public Object getKey(OfferDemandMessage item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public ListDataProvider<OfferDemandMessage> getDataProvider() {
        return dataProvider;
    }

}
