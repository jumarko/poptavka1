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
import cz.poptavka.sample.shared.domain.message.MessageDetail;

public class SingleDemandConversationTable extends CellTable<MessageDetail> {

    private ListDataProvider<MessageDetail> dataProvider = new ListDataProvider<MessageDetail>();

    public SingleDemandConversationTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<MessageDetail> sorHandler = new ListHandler<MessageDetail>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.

        final SelectionModel<MessageDetail> selectionModel = new NoSelectionModel<MessageDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel, DefaultSelectionEventManager.<MessageDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<MessageDetail> tableSelectionModel,
            ListHandler<MessageDetail> sortHandler, final LocalizableMessages msgs) {
        // MultipleSelection Checkbox
        // Column<OfferDemandMessage, Boolean> checkBoxColumn = new Column<OfferDemandMessage, Boolean>(
        // new CheckboxCell(true, false)) {
        // @Override
        // public Boolean getValue(OfferDemandMessage object) {
        // return tableSelectionModel.isSelected(object);
        // }
        // };

        // Demand Title Column
        Column<MessageDetail, String> titleColumn = (new Column<MessageDetail, String>(new TextCell()) {
            @Override
            public String getValue(MessageDetail object) {
                return object.getSubject();
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<MessageDetail, String> dateColumn = (new Column<MessageDetail, String>(new TextCell()) {
            @Override
            public String getValue(MessageDetail object) {
                return dateFormat.format(object.getCreated());
            }
        });

        // Demand sent Date column
        Column<MessageDetail, String> stateColumn = (new Column<MessageDetail, String>(new TextCell()) {
            @Override
            public String getValue(MessageDetail object) {
                return object.getMessageState();
            }
        });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn, new Comparator<MessageDetail>() {
            @Override
            public int compare(MessageDetail o1, MessageDetail o2) {
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
        dateColumn.setSortable(true);
//         add columns into table
//         this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(dateColumn, msgs.endDate());
        this.addColumn(stateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<MessageDetail> KEY_PROVIDER = new ProvidesKey<MessageDetail>() {
        @Override
        public Object getKey(MessageDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public ListDataProvider<MessageDetail> getDataProvider() {
        return dataProvider;
    }

}
