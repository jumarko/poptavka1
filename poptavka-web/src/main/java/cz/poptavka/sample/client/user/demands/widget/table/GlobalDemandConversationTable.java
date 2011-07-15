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
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;

public class GlobalDemandConversationTable extends CellTable<ClientDemandMessageDetail> {

    private ListDataProvider<ClientDemandMessageDetail> dataProvider
        = new ListDataProvider<ClientDemandMessageDetail>();

    public GlobalDemandConversationTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<ClientDemandMessageDetail> sorHandler
            = new ListHandler<ClientDemandMessageDetail>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final SelectionModel<ClientDemandMessageDetail> selectionModel
            = new NoSelectionModel<ClientDemandMessageDetail>(KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
            DefaultSelectionEventManager.<ClientDemandMessageDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<ClientDemandMessageDetail> tableSelectionModel,
            ListHandler<ClientDemandMessageDetail> sortHandler, final LocalizableMessages msgs) {
        // MultipleSelection Checkbox
        // Column<OfferDemandMessage, Boolean> checkBoxColumn = new Column<OfferDemandMessage, Boolean>(
        // new CheckboxCell(true, false)) {
        // @Override
        // public Boolean getValue(OfferDemandMessage object) {
        // return tableSelectionModel.isSelected(object);
        // }
        // };

        // Demand Title Column
        Column<ClientDemandMessageDetail, String> titleColumn
            = (new Column<ClientDemandMessageDetail, String>(new TextCell()) {
                @Override
                public String getValue(ClientDemandMessageDetail object) {
                    return object.getDemandTitle() + " (" + object.getUnreadSubmessages() + ")";
                }
            });

        // Demand Price Column
        Column<ClientDemandMessageDetail, String> priceColumn
            = (new Column<ClientDemandMessageDetail, String>(new TextCell()) {
                @Override
                public String getValue(ClientDemandMessageDetail object) {
                    // TODO add 'none' value into Localizable resources
                    return (object.getPrice() == null ? msgs.notEntered()
                            : object.getPrice().toString());
                }
            });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<ClientDemandMessageDetail, String> endDateColumn
            = (new Column<ClientDemandMessageDetail, String>(new TextCell()) {
                @Override
                public String getValue(ClientDemandMessageDetail object) {
                    return dateFormat.format(object.getEndDate());
                }
            });

        // Demand sent Date column
        Column<ClientDemandMessageDetail, String> validToDateColumn
            = (new Column<ClientDemandMessageDetail, String>(new TextCell()) {
                @Override
                public String getValue(ClientDemandMessageDetail object) {
                    return dateFormat.format(object.getEndDate());
                }
            });

        // TODO rework sorting from offerTable
        // sort methods ****************************
//        titleColumn.setSortable(true);
//        sortHandler.setComparator(titleColumn, new Comparator<ClientDemandMessageDetail>() {
//            @Override
//            public int compare(ClientDemandMessageDetail o1, ClientDemandMessageDetail o2) {
//                if (o1 == o2) {
//                    return 0;
//                }
//                // Compare the name columns.
//                if (o1 != null) {
//                    return (o2 != null) ? o1.getSubject().compareTo(o2.getSubject()) : 1;
//                }
//                return -1;
//            }
//        });
//        priceColumn.setSortable(true);
//        sortHandler.setComparator(priceColumn, new Comparator<ClientDemandMessageDetail>() {
//            @Override
//            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
//                return o1.getPrice().compareTo(o2.getPrice());
//            }
//        });
//        endDateColumn.setSortable(true);
//        validToDateColumn.setSortable(true);
//        Comparator<OfferDemandMessage> endComparator = new Comparator<ClientDemandMessageDetail>() {
//            @Override
//            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
//                return o1.getEndDate().compareTo(o2.getEndDate());
//            }
//        };
//        Comparator<OfferDemandMessage> validComparator = new Comparator<OfferDemandMessage>() {
//            @Override
//            public int compare(OfferDemandMessage o1, OfferDemandMessage o2) {
//                return o1.getValidToDate().compareTo(o2.getValidToDate());
//            }
//        };
//        sortHandler.setComparator(endDateColumn, endComparator);
//        sortHandler.setComparator(validToDateColumn, validComparator);
////         add columns into table
////         this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
        this.addColumn(endDateColumn, msgs.endDate());
        this.addColumn(validToDateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<ClientDemandMessageDetail> KEY_PROVIDER
        = new ProvidesKey<ClientDemandMessageDetail>() {
            @Override
            public Object getKey(ClientDemandMessageDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    public ListDataProvider<ClientDemandMessageDetail> getDataProvider() {
        return dataProvider;
    }

}
