package cz.poptavka.sample.client.user.widget.grid.unused;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;

import java.util.Random;

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
                    return object.getClientName() + " (" + object.getUnreadSubmessages() + ")";
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
        addColumn(new ImageStatus(), msgs.status(), 40, new GetValue<String>() {

            public String getValue(ClientDemandMessageDetail object) {
//                return object.getDemandStatus();
                return null;
            }
        });
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

    private static class ImageStatus extends AbstractCell<String> {

        private Random rnd = new Random();

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }

            String imageHtml = null;


//            diffDays = rnd.nextInt(15); //TODO Martin - docasne, potom vymazat

            //TODO Martin - i18
            if (value.equals(DemandStatus.TEMPORARY.getValue())) { //(0-4) velmi specha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().urgent()).getHTML();
                GWT.log("temporary");

            } else if (value.equals(DemandStatus.ACTIVE.getValue())) { //(5-8) specha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessUrgent()).getHTML();

            } else if (value.equals(DemandStatus.ASSIGNED.getValue())) { //(9-12) nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().normal()).getHTML();

            } else if (value.equals(DemandStatus.CANCELED.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            } else if (value.equals(DemandStatus.CLOSED.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            } else if (value.equals(DemandStatus.FINISHED.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            } else if (value.equals(DemandStatus.INACTIVE.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            } else if (value.equals(DemandStatus.NEW.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().newDemand()).getHTML();
            } else if (value.equals(DemandStatus.TO_BE_CHECKED.getValue())) { //(13-oo) vobec nespecha
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            }
            sb.appendHtmlConstant("<table>");
            // Add the contact image.
            sb.appendHtmlConstant("<tr><td>");
            sb.appendHtmlConstant(imageHtml);
            sb.appendHtmlConstant("</td></tr></table>");
        }
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C>
     *            the cell type
     */
    private interface GetValue<C> {

        C getValue(ClientDemandMessageDetail contact);
    }

    /**
     * Add a column with a header.
     *
     * @param <C>
     *            the cell type
     * @param cell
     *            the cell used to render the column
     * @param headerText
     *            the header string
     * @param getter
     *            the value getter for the cell
     */
    private <C> Column<ClientDemandMessageDetail, C> addColumn(Cell<C> cell,
            String headerText, int width, final GetValue<C> getter) {
        Column<ClientDemandMessageDetail, C> column = new Column<ClientDemandMessageDetail, C>(cell) {

            @Override
            public C getValue(ClientDemandMessageDetail demand) {
                return getter.getValue(demand);
            }
        };
        this.addColumn(column, headerText);
        this.setColumnWidth(column, width, Unit.PX);
        return column;
    }

}
